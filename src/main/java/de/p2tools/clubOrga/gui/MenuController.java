/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.clubOrga.gui;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.controller.ClubStartFactory;
import de.p2tools.clubOrga.controller.ProgQuitFactory;
import de.p2tools.clubOrga.controller.export.ExportCsvDialogController;
import de.p2tools.clubOrga.controller.export.ExportZipDialogController;
import de.p2tools.clubOrga.controller.export.ImportCsvMemberDialogController;
import de.p2tools.clubOrga.data.deleteData.DeleteDataFactory;
import de.p2tools.clubOrga.data.demoData.DemoData;
import de.p2tools.clubOrga.gui.dialog.AboutDialogController;
import de.p2tools.clubOrga.gui.dialog.clubConfigDialog.ProgConfigDialogController;
import de.p2tools.p2Lib.checkForUpdates.SearchProgInfo;
import de.p2tools.p2Lib.dialog.ProgInfoDialog;
import de.p2tools.p2Lib.tools.ProgramTools;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

public class MenuController {

    private final ProgData progData;
    private ClubConfig clubConfig;

    public MenuController(ProgData progData) {
        this.progData = progData;
    }

    public void setClubConfig(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
    }

    public void addMenu(MenuBar menuBar) {
        Menu menuFile = new Menu("Datei");
        Menu menuExport = new Menu("Export");
        Menu menuConfig = new Menu("Einstellungen");
        Menu menuHelp = new Menu("Hilfe");
        menuBar.getMenus().addAll(menuFile, menuExport, menuConfig, menuHelp);


        MenuItem miNewStartClub = new MenuItem("Einen anderen Verein starten");
        MenuItem miExportClub = new MenuItem("Diesen Verein in eine ZIP-Datei sichern");
        MenuItem miQuit = new MenuItem("Programm beenden");
        MenuItem miNewsletter = new MenuItem("Serienbrief erstellen");
        MenuItem miExportMember = new MenuItem("alle Mitglieder in CSV-Datei exportieren");
        MenuItem miImportMember = new MenuItem("Mitglieder aus CSV-Datei importieren");
        MenuItem miClubConfig = new MenuItem("Programmeinstellungen");
        MenuItem miDemoData = new MenuItem("Demodaten einfügen");
        MenuItem miDeleteData = new MenuItem("alle Vereinsdaten löschen");
        MenuItem miUpdate = new MenuItem("Gibt es ein Update?");
        MenuItem miAbout = new MenuItem("Über dieses Programm");
        MenuItem miTest = new MenuItem("Test");


        // Menü Datei
        miNewStartClub.setOnAction(event -> ClubStartFactory.startClubSelector(clubConfig.getStage()));
        miQuit.setOnAction(e -> ProgQuitFactory.quitProgram(progData, clubConfig));

        menuFile.getItems().addAll(miNewStartClub, new SeparatorMenuItem(), miQuit);


        // Menü Export
        miExportMember.setOnAction(event -> exportAllMember());
        miImportMember.setOnAction(event -> importMember());
        miExportClub.setOnAction(event -> new ExportZipDialogController(clubConfig));

        menuExport.getItems().addAll(miExportMember, miImportMember, miExportClub);


        // Menü Einstellungen
        miClubConfig.setOnAction(a -> new ProgConfigDialogController(clubConfig));

        miDemoData.setOnAction(a -> new DemoData().addDemoData(clubConfig));
        miDeleteData.setOnAction(a -> DeleteDataFactory.deleteAllData(clubConfig));
        menuConfig.getItems().addAll(miClubConfig, miDemoData, miDeleteData);


        // Menü Hilfe
        miUpdate.setOnAction(event -> new SearchProgInfo(clubConfig.getStage()).checkUpdate(ProgConst.WEBSITE_PROG_UPDATE,
                ProgramTools.getProgVersionInt(),
                ProgConfig.SYSTEM_LAST_INFO_NR, true, true));
        // todo

        miAbout.setOnAction(event -> new AboutDialogController(clubConfig));

        miTest.setOnAction(event -> {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setResizable(true); // todo bei Oracle Jdk10 unter Linux geht der Dialog nur manchmal auf, stimmt was beim JDK nicht
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

            alert.setTitle("Test");
            alert.setHeaderText("Test Test");
            alert.setContentText("Test Test Test");

            alert.showAndWait();
        });

        if (ProgData.debug) {
            menuHelp.getItems().addAll(miUpdate, miAbout, miTest);
        } else {
            menuHelp.getItems().addAll(miUpdate, miAbout);
        }


        // ProgInfoDialog
        if (ProgData.debug) {
            final MenuItem miInfo = new MenuItem("Programminfos");
            miInfo.setOnAction(event -> new ProgInfoDialog());
            menuHelp.getItems().addAll(new SeparatorMenuItem(), miInfo);
        }
    }

//    private void exportData() {
//        switch (selPane) {
//            case CLUB:
//                break;
//            case MEMBER:
//                List<MemberData> memberData = clubConfig.guiMember.getSelList();
//                if (!memberData.isEmpty()) {
//                    Newsletter.exportMemberDataList(knownClubData, memberData);
//                }
//                break;
//            case FEE:
//                List<FeeData> feeData = clubConfig.guiFee.getSelList();
//                if (!feeData.isEmpty()) {
//                    Newsletter.exportFeeDataList(knownClubData, feeData);
//                }
//                break;
//            case FINANCE:
////                List<FinanceData> financeData = clubConfig.guiFinance.getSelList();
////                if (!financeData.isEmpty()) {
////                    Newsletter.exportFinanceDataList(knownClubData, financeData);
////                }
//                break;
//            default:
////                Newsletter.exportClubData(knownClubData);
//        }
//
//    }

    private void exportAllMember() {
        new ExportCsvDialogController(clubConfig.getStage(), clubConfig, clubConfig.memberDataList, null);
    }

    private void importMember() {
        new ImportCsvMemberDialogController(clubConfig.getStage(), clubConfig);
    }
}
