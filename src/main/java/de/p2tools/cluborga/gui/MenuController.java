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


package de.p2tools.cluborga.gui;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.controller.ClubStartFactory;
import de.p2tools.cluborga.controller.ProgQuitFactory;
import de.p2tools.cluborga.controller.SearchProgramUpdate;
import de.p2tools.cluborga.controller.export.csv.ExportCsvDialogController;
import de.p2tools.cluborga.controller.export.csv.ImportCsvMemberDialogController;
import de.p2tools.cluborga.controller.export.zip.ExportZipDialogController;
import de.p2tools.cluborga.data.deleteData.DeleteDataFactory;
import de.p2tools.cluborga.data.demoData.DemoData;
import de.p2tools.cluborga.gui.dialog.AboutDialogController;
import de.p2tools.cluborga.gui.dialog.clubConfigDialog.ConfigDialogController;
import de.p2tools.p2lib.dialogs.ProgInfoDialog;
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
        Menu menuHelp = new Menu("Hilfe");
        Menu menuConfig = new Menu("Einstellungen");
        Menu menuFile = new Menu("Datei");
        menuBar.getMenus().addAll(menuFile, menuConfig, menuHelp);


        // Menü Datei
        MenuItem miNewStartClub = new MenuItem("Einen anderen Verein starten");
        miNewStartClub.setOnAction(event -> ClubStartFactory.startClubSelector(clubConfig.getStage()));
        MenuItem miQuit = new MenuItem("Programm beenden");
        miQuit.setOnAction(e -> ProgQuitFactory.quitProgram(progData, clubConfig));

        menuFile.getItems().addAll(miNewStartClub, new SeparatorMenuItem(), miQuit);


        // Menü Einstellungen
        MenuItem miExportMember = new MenuItem("alle Mitglieder in CSV-Datei exportieren");
        miExportMember.setOnAction(event -> exportAllMember());
        MenuItem miImportMember = new MenuItem("Mitglieder aus CSV-Datei importieren");
        miImportMember.setOnAction(event -> importMember());
        MenuItem miExportClub = new MenuItem("Diesen Verein in eine ZIP-Datei sichern");
        miExportClub.setOnAction(event -> new ExportZipDialogController(clubConfig));

        MenuItem miClubConfig = new MenuItem("Programmeinstellungen");
        miClubConfig.setOnAction(a -> new ConfigDialogController(clubConfig));

        MenuItem miDemoData = new MenuItem("Demodaten (Mitglieder, Beiträge, Finanzen) einfügen");
        miDemoData.setOnAction(a -> new DemoData().addDemoData(clubConfig));
        MenuItem miDeleteData = new MenuItem("alle Vereinsdaten löschen");
        miDeleteData.setOnAction(a -> DeleteDataFactory.deleteAllData(clubConfig));

        menuConfig.getItems().addAll(miClubConfig,
                new SeparatorMenuItem(), miExportMember, miImportMember, miExportClub,
                new SeparatorMenuItem(), miDemoData, miDeleteData);


        // Menü Hilfe
        MenuItem miUpdate = new MenuItem("Gibt es ein Update?");
        miUpdate.setOnAction(event -> new SearchProgramUpdate(progData, progData.primaryStage).searchNewProgramVersion(true));
        MenuItem miAbout = new MenuItem("Über dieses Programm");
        miAbout.setOnAction(event -> new AboutDialogController(clubConfig));

        menuHelp.getItems().addAll(miUpdate, miAbout);
        if (ProgData.debug) {
            MenuItem miTest = new MenuItem("Test");
            miTest.setOnAction(event -> {
                final Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setResizable(true); // bei Oracle Jdk10 unter Linux geht der Dialog sonst nur manchmal auf, stimmt was beim JDK nicht
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

                alert.setTitle("Test");
                alert.setHeaderText("Test Test");
                alert.setContentText("Test Test Test");

                alert.showAndWait();
            });

            final MenuItem miInfo = new MenuItem("Programminfos");
            miInfo.setOnAction(event -> new ProgInfoDialog(true));

            menuHelp.getItems().addAll(new SeparatorMenuItem(), miTest, miInfo);
        }
    }

    private void exportAllMember() {
        new ExportCsvDialogController(clubConfig.getStage(), clubConfig, clubConfig.memberDataList,
                null, null, null);
    }

    private void importMember() {
        new ImportCsvMemberDialogController(clubConfig.getStage(), clubConfig);
    }
}
