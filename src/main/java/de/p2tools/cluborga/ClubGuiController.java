/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.cluborga;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.gui.GuiFactory;
import de.p2tools.cluborga.gui.MenuController;
import de.p2tools.cluborga.gui.StatusBarController;
import de.p2tools.cluborga.gui.guiClub.GuiClub;
import de.p2tools.cluborga.gui.guiClub.GuiClubInfo;
import de.p2tools.cluborga.gui.guiFee.GuiFee;
import de.p2tools.cluborga.gui.guiFee.guiConfig.GuiFeePaymentType;
import de.p2tools.cluborga.gui.guiFee.guiConfig.GuiFeeRate;
import de.p2tools.cluborga.gui.guiFinance.GuiFinance;
import de.p2tools.cluborga.gui.guiFinance.guiConfig.GuiFinanceAccount;
import de.p2tools.cluborga.gui.guiFinance.guiConfig.GuiFinanceCategory;
import de.p2tools.cluborga.gui.guiFinanceReport.GuiFinanceReport;
import de.p2tools.cluborga.gui.guiMember.GuiMember;
import de.p2tools.cluborga.gui.guiMember.guiConfig.GuiMemberState;
import de.p2tools.p2lib.guitools.pmask.PMaskerPane;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ClubGuiController extends StackPane {

    BorderPane borderPane;
    StackPane stackPaneCont;
    private StatusBarController statusBarController;
    private Accordion accordion;
    private PMaskerPane maskerPane;
    private final MenuController menuController;

    private final ProgData progData;
    private ClubConfig clubConfig;

    public ClubGuiController() {
        progData = ProgData.getInstance();
        this.menuController = new MenuController(progData);
    }

    public void init(ClubConfig clubConfig) {
        try {
            this.clubConfig = clubConfig;
            this.getChildren().clear();

            borderPane = new BorderPane();
            stackPaneCont = new StackPane();
            menuController.setClubConfig(clubConfig);
            accordion = new Accordion();
            maskerPane = new PMaskerPane();

            // Panes
            clubConfig.pMaskerPane = maskerPane;
            clubConfig.guiClub = new GuiClub(clubConfig);
            clubConfig.guiClubInfo = new GuiClubInfo(clubConfig);
            clubConfig.guiMember = new GuiMember(clubConfig);
            clubConfig.guiMemberState = new GuiMemberState(clubConfig);
            clubConfig.guiFee = new GuiFee(clubConfig);
            clubConfig.guiFeeRate = new GuiFeeRate(clubConfig);
            clubConfig.guiFeePaymentType = new GuiFeePaymentType(clubConfig);
            clubConfig.guiFinance = new GuiFinance(clubConfig);
            clubConfig.guiFinanceReport = new GuiFinanceReport(clubConfig);
            clubConfig.guiFinanceAccount = new GuiFinanceAccount(clubConfig);
            clubConfig.guiFinanceCategory = new GuiFinanceCategory(clubConfig);

            stackPaneCont.getChildren().addAll(clubConfig.guiClub, clubConfig.guiClubInfo,
                    clubConfig.guiMember,
                    clubConfig.guiMemberState,
                    clubConfig.guiFee, clubConfig.guiFeeRate, clubConfig.guiFeePaymentType,
                    clubConfig.guiFinance, clubConfig.guiFinanceReport,
                    clubConfig.guiFinanceAccount, clubConfig.guiFinanceCategory
            );

            // Statusbar
            statusBarController = new StatusBarController(clubConfig);

            addMenu();
            makeAccordion();
            initMaskerPane();

            this.setPadding(new Insets(0));
            this.getChildren().addAll(borderPane, maskerPane);

            // ProgGUI
            borderPane.setLeft(accordion);
            borderPane.setCenter(stackPaneCont);
            borderPane.setBottom(statusBarController);

        } catch (Exception ex) {
            PLog.errorLog(912032021, ex);
        }
    }

    private void initMaskerPane() {
        StackPane.setAlignment(maskerPane, Pos.CENTER);
        maskerPane.setPadding(new Insets(4, 1, 1, 1));
        maskerPane.toFront();
    }

    private void setTopPane(GuiFactory.PANE pane) {
        switch (pane) {
            case CLUB:
                clubConfig.guiClub.toFront();
                clubConfig.guiClub.isShown();
                setSelPane(GuiFactory.PANE.CLUB);
                break;
            case CLUB_INFO:
                clubConfig.guiClubInfo.toFront();
                clubConfig.guiClubInfo.isShown();
                setSelPane(GuiFactory.PANE.CLUB_INFO);
                break;
            case MEMBER:
                clubConfig.guiMember.toFront();
                clubConfig.guiMember.isShown();
                setSelPane(GuiFactory.PANE.MEMBER);
                break;
            case MEMBER_STATE:
                clubConfig.guiMemberState.toFront();
                clubConfig.guiMemberState.isShown();
                setSelPane(GuiFactory.PANE.MEMBER_STATE);
                break;
            case FEE:
                clubConfig.guiFee.toFront();
                clubConfig.guiFee.isShown();
                setSelPane(GuiFactory.PANE.FEE);
                break;
            case FEE_RATE:
                clubConfig.guiFeeRate.toFront();
                clubConfig.guiFeeRate.isShown();
                setSelPane(GuiFactory.PANE.FEE_RATE);
                break;
            case FEE_PAYMENT_TYPE:
                clubConfig.guiFeePaymentType.toFront();
                clubConfig.guiFeePaymentType.isShown();
                setSelPane(GuiFactory.PANE.FEE_PAYMENT_TYPE);
                break;
            case FINANCE:
                clubConfig.guiFinance.toFront();
                clubConfig.guiFinance.isShown();
                setSelPane(GuiFactory.PANE.FINANCE);
                break;
            case FINANCE_CALCULATION:
                clubConfig.guiFinanceReport.toFront();
                clubConfig.guiFinanceReport.isShown();
                setSelPane(GuiFactory.PANE.FINANCE_CALCULATION);
                break;
            case FINANCE_ACCOUNT:
                clubConfig.guiFinanceAccount.toFront();
                clubConfig.guiFinanceAccount.isShown();
                setSelPane(GuiFactory.PANE.FINANCE_ACCOUNT);
                break;
            case FINANCE_CATEGORY:
                clubConfig.guiFinanceCategory.toFront();
                clubConfig.guiFinanceCategory.isShown();
                setSelPane(GuiFactory.PANE.FINANCE_CATEGORY);
                break;
        }
    }

    private void setSelPane(GuiFactory.PANE selPane) {
//        menuController.setSelPane(selPane);
        statusBarController.setSelPane(selPane);
    }

    private void makeAccordion() {
        VBox vbClub = new VBox(5);
        vbClub.setPadding(new Insets(0));
        vbClub.getStyleClass().add("vbox-accordion");

        // Verein
        TitledPane club = new TitledPane("Verein", vbClub);
        club.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setTopPane(GuiFactory.PANE.CLUB);
            }
        });
        addSelectButton(vbClub, "Verein", GuiFactory.PANE.CLUB);
        addSelectButton(vbClub, "Vereinsinfos", GuiFactory.PANE.CLUB_INFO);


        // Mitglieder
        VBox vbMember = new VBox(5);
        vbMember.setPadding(new Insets(0));
        vbMember.getStyleClass().add("vbox-accordion");

        TitledPane member = new TitledPane("Mitglieder", vbMember);
        member.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setTopPane(GuiFactory.PANE.MEMBER);
            }
        });
        addSelectButton(vbMember, "Mitglieder", GuiFactory.PANE.MEMBER);
        addSelectButton(vbMember, "Mitgliederstati", GuiFactory.PANE.MEMBER_STATE);


        // Beitrag
        VBox vbfee = new VBox(5);
        vbfee.setPadding(new Insets(0));
        vbfee.getStyleClass().add("vbox-accordion");

        TitledPane fee = new TitledPane("Beiträge", vbfee);
        fee.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setTopPane(GuiFactory.PANE.FEE);
            }
        });
        addSelectButton(vbfee, "Beiträge", GuiFactory.PANE.FEE);
        addSelectButton(vbfee, "Beitragssätze", GuiFactory.PANE.FEE_RATE);
        addSelectButton(vbfee, "Zahlarten", GuiFactory.PANE.FEE_PAYMENT_TYPE);


        // Finanzen
        VBox vbFinances = new VBox(5);
        vbFinances.setPadding(new Insets(0));
        vbFinances.getStyleClass().add("vbox-accordion");

        TitledPane finances = new TitledPane("Finanzen", vbFinances);
        finances.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setTopPane(GuiFactory.PANE.FINANCE);
            }
        });
        addSelectButton(vbFinances, "Finanzen", GuiFactory.PANE.FINANCE);
        addSelectButton(vbFinances, "Finanzübersicht", GuiFactory.PANE.FINANCE_CALCULATION);
        addSelectButton(vbFinances, "Konten", GuiFactory.PANE.FINANCE_ACCOUNT);
        addSelectButton(vbFinances, "Kategorien", GuiFactory.PANE.FINANCE_CATEGORY);

        accordion.getPanes().addAll(club, member, fee, finances);
        accordion.setExpandedPane(club);
        setTopPane(GuiFactory.PANE.CLUB);
    }

    private Button addSelectButton(VBox vBox, String title, GuiFactory.PANE pane) {
        Button button = new Button(title);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("button-accordion");
        button.setOnAction(a -> setTopPane(pane));
        vBox.getChildren().addAll(button);
        return button;
    }

    private void addMenu() {
        MenuBar menuBar = new MenuBar();
        borderPane.setTop(menuBar);
        menuController.addMenu(menuBar);
    }

}
