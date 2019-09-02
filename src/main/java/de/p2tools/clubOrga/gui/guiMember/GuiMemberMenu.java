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

package de.p2tools.clubOrga.gui.guiMember;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.controller.export.csv.ExportCsvDialogController;
import de.p2tools.clubOrga.controller.export.csv.ImportCsvMemberDialogController;
import de.p2tools.clubOrga.controller.newsletter.Newsletter;
import de.p2tools.clubOrga.data.feeData.FeeFactory;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberFactory;
import de.p2tools.clubOrga.gui.dialog.dataDialog.DataDialogController;
import de.p2tools.p2Lib.guiTools.PButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class GuiMemberMenu extends VBox {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private final GuiMember guiMember;


    public GuiMemberMenu(ClubConfig clubConfig, GuiMember guiMember) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.guiMember = guiMember;

        initCont();
    }

    private void initCont() {
        this.setSpacing(10);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_CENTER);

        final MenuButton mb = new MenuButton("");
        mb.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU);
        mb.getStyleClass().add("btnFunction");

        // Mitglied
        MenuItem miAddMember = new MenuItem("neues Mitglied anlegen");
        miAddMember.setOnAction(a -> addNewMember());

        MenuItem miChangeMember = new MenuItem("aktuelles Mitglied ändern");
        miChangeMember.setOnAction(a -> guiMember.changeMember());

        MenuItem miRemoveMember = new MenuItem("markierte Mitglieder löschen");
        miRemoveMember.setOnAction(a -> removeMember());

        // Beitrag
        MenuItem miAddFee = new MenuItem("Beitrag für aktuelles Mitglied anlegen");
        miAddFee.setOnAction(a -> FeeFactory.generateFeeForMember(clubConfig, clubConfig.guiMember.getSel()));

        MenuItem miMemberFee = new MenuItem("fehlende Beiträge für angezeigte Mitglieder anlegen");
        miMemberFee.setOnAction(a -> generateFee(clubConfig.memberDataList.getFilteredList()));

        MenuItem miAllMemberFee = new MenuItem("alle fehlenden Beiträge anlegen");
        miAllMemberFee.setOnAction(a -> generateFee(clubConfig.memberDataList));


        // Menü Export
        MenuItem miNewsletterSel = new MenuItem("Serienbrief für markierte Mitglieder erstellen");
        miNewsletterSel.setOnAction(event -> createNewsletter(clubConfig.guiMember.getSelList()));

        MenuItem miNewsletterShown = new MenuItem("Serienbrief für angezeigte Mitglieder erstellen");
        miNewsletterShown.setOnAction(event -> createNewsletter(clubConfig.memberDataList.getFilteredList()));

        MenuItem miNewsletterAll = new MenuItem("Serienbrief für alle Mitglieder erstellen");
        miNewsletterAll.setOnAction(event -> createNewsletter(clubConfig.memberDataList));

        MenuItem miExportSel = new MenuItem("markierte Mitglieder exportieren");
        miExportSel.setOnAction(a -> exportMember(clubConfig.guiMember.getSelList()));

        MenuItem miExportShown = new MenuItem("angezeigte Mitglieder exportieren");
        miExportShown.setOnAction(a -> exportMember(clubConfig.memberDataList.getFilteredList()));

        MenuItem miExportAll = new MenuItem("alle Mitglieder exportieren");
        miExportAll.setOnAction(a -> exportMember(clubConfig.memberDataList));


        Menu mFee = new Menu("Beiträge anlegen");
        mFee.getItems().addAll(miAddFee, miMemberFee, miAllMemberFee);
        Menu mNewsletter = new Menu("Serienbrief");
        mNewsletter.getItems().addAll(miNewsletterSel, miNewsletterShown, miNewsletterAll);
        Menu mExport = new Menu("Export");
        mExport.getItems().addAll(miExportSel, miExportShown, miExportAll);

        mb.getItems().addAll(miAddMember, miChangeMember, miRemoveMember,
                new SeparatorMenuItem(), mFee, mNewsletter, mExport);

        // extra Buttons
        Button btnNew = PButton.getButton(new ProgIcons().ICON_BUTTON_ADD, "neues Mitglied anlegen");
        btnNew.setOnAction(a -> addNewMember());

        Button btnDel = PButton.getButton(new ProgIcons().ICON_BUTTON_REMOVE, "markierte Mitglieder löschen");
        btnDel.setOnAction(a -> removeMember());

        Button btnChange = PButton.getButton(new ProgIcons().ICON_BUTTON_MEMBER_CHANGE, "aktuelles Mitglied ändern");
        btnChange.setOnAction(a -> guiMember.changeMember());

        getChildren().addAll(mb, btnNew, btnDel, btnChange);
    }

    private void createNewsletter(List<MemberData> memberData) {
        if (!memberData.isEmpty()) {
            Newsletter.memberNewsletter(clubConfig, memberData);
        }
    }

    private void exportMember(List<MemberData> memberDataList) {
        if (memberDataList != null && !memberDataList.isEmpty()) {
            new ExportCsvDialogController(clubConfig.getStage(), clubConfig, memberDataList);
        }
    }

    private void importMember() {
        new ImportCsvMemberDialogController(clubConfig.getStage(), clubConfig);
    }

    private void addNewMember() {
        MemberData memberData = MemberFactory.getNewMemberData(clubConfig, "", true);
        if (new DataDialogController(clubConfig, DataDialogController.OPEN.MEMBER_PANE,
                memberData, null, null, null).isOk()) {
            clubConfig.memberDataList.add(memberData);
        }
    }

    private void removeMember() {
        List<MemberData> data = guiMember.getSelList();
        if (!data.isEmpty()) {
            clubConfig.memberDataList.memberDataListRemoveAll(data);
        }
    }

    private void generateFee(List<MemberData> memberDataList) {
        if (memberDataList.isEmpty()) {
            return;
        }
        FeeFactory.generateMissingFeesForMembers(clubConfig, memberDataList);
    }
}
