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
import de.p2tools.clubOrga.config.prog.ProgConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.controller.ClubStartFactory;
import de.p2tools.clubOrga.controller.ProgStartFactory;
import de.p2tools.clubOrga.data.knownClubData.KnownClubData;
import de.p2tools.clubOrga.data.knownClubData.KnownClubDataFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.duration.PDuration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Club extends Application {

    protected ProgData progData;

    @Override
    public void init() {
    }

    @Override
    public void start(Stage primaryStage) {
        PDuration.counterStart("Programmstart");
        PDuration.onlyPing("Programmstart");

        progData = ProgData.getInstance();
        final boolean firstProgramStart = ProgStartFactory.loadProgConfigData();

        if (firstProgramStart) {
            // Verein mit Standarddaten/Pfad anlegen
            initNewClub(primaryStage);

        } else {
            KnownClubData knownClubData = progData.knownClubDataList.getKnownClubForStart();
            if (ProgConfig.START_CLUB_SELECTOR_FIRST.get() || knownClubData == null) {
                // dann mit der Auswahl starten
                ClubStartFactory.startClubSelector();

            } else {
                // Prog sofort mit dem Club starten
                ClubStartFactory.startClub(primaryStage, knownClubData);
            }
        }

        PDuration.counterStop("Programmstart");
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
        StartDialogController startDialogController = new StartDialogController(primaryStage, knownClubData);
        if (!startDialogController.isOk()) {
            // dann jetzt beenden -> Tschüss
            Platform.exit();
            System.exit(0);
        }
    }

}
