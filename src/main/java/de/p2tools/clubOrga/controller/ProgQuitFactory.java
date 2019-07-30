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

package de.p2tools.clubOrga.controller;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.p2Lib.tools.log.LogMessage;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;

public class ProgQuitFactory {

    private enum QUIT {PROGRAM, CLUB, CANCEL}

    private ProgQuitFactory() {
    }

    /**
     * called from program menu
     * close program
     *
     * @param progData
     * @param clubConfig
     */
    public static void quitProgram(ProgData progData, ClubConfig clubConfig) {
        progData.knownClubDataList.setClubDataAutostart(clubConfig);
        ProgSaveFactory.saveAll();

        quitProgram();
    }

    /**
     * called from "X" in the window
     * when last window, close program, otherwise close only the window
     *
     * @param progData
     * @param clubConfig
     */
    public static void quitClub(ProgData progData, ClubConfig clubConfig) {
        // Aufruf durch "X" im Programmfenster: dann erst mal schauen was noch läuft
        if ((progData.clubSelector == null || !progData.clubSelector.getStage().isShowing()) &&
                ClubStartFactory.onlyThisClubStageIsRunning(clubConfig)) {

            // dann ist nur der eine Club geöffnet
            PLog.sysLog(new String[]{PLog.LILNE2,
                    "quit all: " + clubConfig.clubData.getName()});
            ProgSaveFactory.saveAll();
            quitProgram();
            return;

        } else {
            PLog.sysLog(new String[]{PLog.LILNE2,
                    "quit only: " + clubConfig.clubData.getName()});
            ProgSaveFactory.saveClub(clubConfig);
        }

        if (clubConfig.getStage() != null) {
            clubConfig.getStage().hide();
        }
    }

    /**
     * called from "X" in the window of the clubselector
     * shut down program when last window
     */
    public static void quitClubSelector(ProgData progData) {
        if (ClubStartFactory.noClubStageIsRunning()) {
            PLog.sysLog(new String[]{PLog.LILNE2,
                    "quit all: clubSelector, no clubStage is running",});
            // dann sind auch keine Clubs geöffnet
            ProgSaveFactory.saveAll();
            quitProgram();
            return;

        } else {
            PLog.sysLog(new String[]{PLog.LILNE2,
                    "quit clubSelector: other clubStage is running"});
        }
    }

    private static void quitProgram() {
        // dann jetzt beenden -> Thüss
        LogMessage.endMsg();
        Platform.runLater(() -> {
            Platform.exit();
            System.exit(0);
        });
    }

}
