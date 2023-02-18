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

package de.p2tools.clubOrga.gui.guiFinance;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.controller.export.csv.ExportCsvDialogController;
import de.p2tools.clubOrga.controller.export.financeInfo.FinanceReportController;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFactory;
import de.p2tools.clubOrga.gui.dialog.dataDialog.DataDialogController;
import de.p2tools.p2Lib.guiTools.PButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

public class GuiFinanceMenu extends VBox {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private final GuiFinance guiFinance;

    public GuiFinanceMenu(ClubConfig clubConfig, GuiFinance guiFinance) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.guiFinance = guiFinance;

        initCont();
    }

    private void initCont() {
        this.setSpacing(10);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_CENTER);

        final MenuButton mb = new MenuButton("");
        mb.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU);
        mb.getStyleClass().add("btnFunction");

        MenuItem miAddFinance = new MenuItem("neuen Finanzeintrag anlegen");
        miAddFinance.setOnAction(a -> addNewFinance());
        MenuItem miChangeFinance = new MenuItem("aktuellen Finanzeintrag ändern");
        miChangeFinance.setOnAction(a -> guiFinance.changeFinances());

        MenuItem miDelFinance = new MenuItem("ausgewählte Finanzeinträge löschen");
        miDelFinance.setOnAction(a -> delFinance());

        MenuItem miFinanceReceipt = new MenuItem("einen Beleg der Finanzeinträge erstellen");
        miFinanceReceipt.setOnAction(a ->
                writeReceipt()
        );

        // Menü Export
        MenuItem miExportShown = new MenuItem("angezeigte Finanzen exportieren");
        miExportShown.setOnAction(a -> exportFinances(clubConfig.financeDataList.getFilteredList()));

        MenuItem miExportAll = new MenuItem("alle Finanzen exportieren");
        miExportAll.setOnAction(a -> exportFinances(clubConfig.financeDataList));

        Menu mExport = new Menu("Export");
        mExport.getItems().addAll(miExportShown, miExportAll);


        mb.getItems().addAll(miAddFinance, miChangeFinance, miDelFinance, miFinanceReceipt,
                mExport);


        // Button
        Button btnNew = PButton.getButton(new ProgIcons().ICON_BUTTON_ADD, "neuen Finanzeintrag anlegen");
        btnNew.setOnAction(a -> addNewFinance());

        Button btnDel = PButton.getButton(new ProgIcons().ICON_BUTTON_REMOVE, "ausgewählte Finanzeinträge löschen");
        btnDel.setOnAction(a -> delFinance());

        Button btnChange = PButton.getButton(new ProgIcons().ICON_BUTTON_MEMBER_CHANGE, "ausgewählten Finanzeintrag ändern");
        btnChange.setOnAction(a -> guiFinance.changeFinances());
        getChildren().addAll(mb, btnNew, btnDel, btnChange);
    }

    private void addNewFinance() {
        FinanceData financeData = FinanceFactory.getNewFinanceData(clubConfig, LocalDate.now(),
                0, clubConfig.financeAccountDataList.get(0), clubConfig.financeCategoryDataList.get(0));

        if (new DataDialogController(clubConfig, DataDialogController.OPEN.FINANCE_PANE,
                financeData, 0).isOk()) {
            clubConfig.financeDataList.add(financeData);
        }
    }

    private void delFinance() {
        List<FinanceData> transactionData = guiFinance.getSelList();
        if (!transactionData.isEmpty()) {
            clubConfig.financeDataList.financeDataListRemoveAll(transactionData);
        }
    }

    private void writeReceipt() {
        List<FinanceData> list = guiFinance.getSelList();
        if (!list.isEmpty()) {
            new FinanceReportController(clubConfig.getStage(), clubConfig, list);
        }
    }

    private void exportFinances(List<FinanceData> financeDataList) {
        if (financeDataList != null && !financeDataList.isEmpty()) {
            new ExportCsvDialogController(clubConfig.getStage(), clubConfig,
                    null, null, financeDataList, null);
        }
    }

}
