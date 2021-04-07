/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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
import de.p2tools.clubOrga.config.prog.ProgConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.gui.tools.StringFormatters;
import de.p2tools.p2Lib.checkForUpdates.SearchProgUpdate;
import de.p2tools.p2Lib.checkForUpdates.UpdateSearchData;
import de.p2tools.p2Lib.tools.ProgramTools;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Date;

import static java.lang.Thread.sleep;

public class SearchProgramUpdate {

    private final ClubConfig clubConfig;
    private final Stage stage;
    private static final String TITLE_TEXT_PROGRAM_VERSION_IS_UPTODATE = "Programmversion ist aktuell";
    private static final String TITLE_TEXT_PROGRAMMUPDATE_EXISTS = "Ein Programmupdate ist verfügbar";

    public SearchProgramUpdate(Stage stage, ClubConfig clubConfig) {
        this.stage = stage;
        this.clubConfig = clubConfig;
    }


    /**
     * @return
     */
    public boolean searchNewProgramVersion() {
        // prüft auf neue Version, ProgVersion und auch (wenn gewünscht) BETA-Version
        boolean ret;
        ProgConfig.SYSTEM_UPDATE_DATE.setValue(StringFormatters.FORMATTER_yyyyMMdd.format(new Date()));

        if (!ProgConfig.SYSTEM_UPDATE_SEARCH.get()) {
            // dann ist es nicht gewünscht
            return false;
        }

        UpdateSearchData updateSearchData = new UpdateSearchData(ProgConst.URL_PROG_UPDATE,
                ProgramTools.getProgVersionInt(), ProgramTools.getBuildInt(),
                ProgConfig.SYSTEM_UPDATE_VERSION_SHOWN,
                null,
                ProgConfig.SYSTEM_UPDATE_INFO_NR_SHOWN,
                ProgConfig.SYSTEM_UPDATE_SEARCH);

        UpdateSearchData updateSearchDataBeta = null;
        if (ProgConfig.SYSTEM_UPDATE_BETA_SEARCH.get()) {
            updateSearchDataBeta = new UpdateSearchData(ProgConst.URL_PROG_BETA_UPDATE,
                    ProgramTools.getProgVersionInt(), ProgramTools.getBuildInt(),
                    ProgConfig.SYSTEM_UPDATE_BETA_VERSION_SHOWN,
                    ProgConfig.SYSTEM_UPDATE_BETA_BUILD_NO_SHOWN,
                    null,
                    ProgConfig.SYSTEM_UPDATE_BETA_SEARCH);
        }

        ret = new SearchProgUpdate(stage).checkAllUpdates(updateSearchData, updateSearchDataBeta, false);
        setTitleInfo(ret);
        return ret;
    }

    public boolean searchNewVersionInfos() {
        // prüft auf neue Version und zeigts immer an, auch (wenn gewünscht) BETA-Version
        UpdateSearchData updateSearchData = new UpdateSearchData(ProgConst.URL_PROG_UPDATE,
                ProgramTools.getProgVersionInt(), ProgramTools.getBuildInt(),
                null,
                null,
                null,
                null);

        UpdateSearchData updateSearchDataBeta = null;
        if (ProgConfig.SYSTEM_UPDATE_BETA_SEARCH.get()) {
            updateSearchDataBeta = new UpdateSearchData(ProgConst.URL_PROG_BETA_UPDATE,
                    ProgramTools.getProgVersionInt(), ProgramTools.getBuildInt(),
                    null,
                    null,
                    null,
                    null);
        }

        return new SearchProgUpdate(stage).checkAllUpdates(updateSearchData, updateSearchDataBeta, true);
    }

    private void setTitleInfo(boolean newVersion) {
        if (newVersion) {
            Platform.runLater(() -> ClubStartFactory.setTitel(clubConfig, TITLE_TEXT_PROGRAMMUPDATE_EXISTS));
        } else {
            Platform.runLater(() -> ClubStartFactory.setTitel(clubConfig, TITLE_TEXT_PROGRAM_VERSION_IS_UPTODATE));
        }

        try {
            sleep(10_000);
        } catch (Exception ignore) {
        }

        Platform.runLater(() -> ClubStartFactory.setTitel(clubConfig));
    }

}
