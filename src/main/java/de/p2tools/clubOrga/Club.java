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
package de.p2tools.clubOrga;

import de.p2tools.clubOrga.clubStart.StartDialogController;
import de.p2tools.clubOrga.config.club.ProgColorList;
import de.p2tools.clubOrga.config.prog.ProgConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.controller.ClubStartFactory;
import de.p2tools.clubOrga.controller.ProgStartFactory;
import de.p2tools.clubOrga.data.knownClubData.KnownClubData;
import de.p2tools.clubOrga.data.knownClubData.KnownClubDataFactory;
import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.duration.PDuration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Club extends Application {

    private Stage primaryStage;
    protected ProgData progData;

    @Override
    public void init() {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        PDuration.counterStart("Programmstart");
        PDuration.onlyPing("Programmstart");
        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;

        initP2lib();
        final boolean firstProgramStart = ProgStartFactory.loadProgConfigData();
        if (firstProgramStart) {
            // Verein mit Standarddaten/Pfad anlegen
            initNewClub(primaryStage);

        } else {
            ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> {
                addThemeCss();
                ProgColorList.setColorTheme();
            });
            ProgColorList.setColorTheme();
            addThemeCss();

            KnownClubData knownClubData = progData.knownClubDataList.getKnownClubForStart();
            if (ProgConfig.START_CLUB_SELECTOR_FIRST.get() || knownClubData == null) {
                // dann mit der Auswahl starten
                ClubStartFactory.startClubSelector();

            } else {
                // Prog sofort mit dem Club starten
                if (!ClubStartFactory.startClub(primaryStage, knownClubData)) {
                    // dann mit der Auswahl starten
                    ClubStartFactory.startClubSelector();
                }
            }
        }

        PDuration.counterStop("Programmstart");
    }

    private void initP2lib() {
        P2LibInit.initLib(primaryStage, ProgConst.PROGRAM_NAME, "", ProgConfig.SYSTEM_DARK_THEME,
                ProgData.debug, ProgData.duration);
        P2LibInit.addCssFile(ProgConst.CSS_FILE);
    }

    private void addThemeCss() {
        if (ProgConfig.SYSTEM_DARK_THEME.get()) {
            P2LibInit.addCssFile(ProgConst.CSS_FILE_DARK_THEME);
        } else {
            P2LibInit.removeCssFile(ProgConst.CSS_FILE_DARK_THEME);
        }
    }

    private void initNewClub(Stage primaryStage) {
        PDuration.onlyPing("Erster Start");
        KnownClubData knownClubData = KnownClubDataFactory.getNextNewKnownClubData();

        if (knownClubData == null) {
            PAlert.showErrorAlert((Stage) null,
                    "Neuen Verein anlegen",
                    "Das Programm kann keinen Verein anlegen. Es findet keinen Speicherplatz dafür.");
            // dann jetzt beenden -> Tschüss
            Platform.exit();
            System.exit(0);
        }

        // Verein mit Standarddaten/Pfad anlegen
        StartDialogController startDialogController = new StartDialogController(knownClubData);
        if (!startDialogController.isOk()) {
            // dann jetzt beenden -> Tschüss
            Platform.exit();
            System.exit(0);
        }
    }
}
