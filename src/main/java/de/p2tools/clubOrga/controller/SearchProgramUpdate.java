/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.clubOrga.config.prog.ProgConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.gui.tools.StringFormatters;
import de.p2tools.p2Lib.checkForUpdates.SearchProgInfo;
import de.p2tools.p2Lib.tools.ProgramTools;
import javafx.stage.Stage;

import java.util.Date;

public class SearchProgramUpdate {

    //    BooleanProperty updateProp;
//    BooleanProperty updateBetaProp;
    private final Stage stage;

    public SearchProgramUpdate(Stage stage) {
        this.stage = stage;
//        updateProp = ProgConfig.SYSTEM_UPDATE_SEARCH.getBooleanProperty();
//        updateBetaProp = ProgConfig.SYSTEM_UPDATE_BETA_SEARCH.getBooleanProperty();
    }

    /**
     * @param showError
     * @param showProgramInformation
     * @return
     */
    public boolean checkVersion(boolean showError, boolean showProgramInformation, boolean showUpdate) {
        // prüft auf neue Version,
        ProgConfig.SYSTEM_UPDATE_DATE.setValue(StringFormatters.FORMATTER_yyyyMMdd.format(new Date()));

        return new SearchProgInfo(stage).checkUpdate(ProgConst.URL_PROG_UPDATE,
                ProgramTools.getProgVersionInt(),
                ProgConfig.SYSTEM_UPDATE_VERSION_SHOWN,
                ProgConfig.SYSTEM_UPDATE_INFO_NR_SHOWN,
                ProgConfig.SYSTEM_UPDATE_SEARCH,
                showUpdate,
                showProgramInformation, showError);
    }

    /**
     * @param showError
     * @param showProgramInformation
     * @return
     */
    public boolean checkBetaVersion(boolean showError, boolean showProgramInformation) {
        // prüft auf neue beta Version,

        return new SearchProgInfo(stage).checkBetaUpdate(ProgConst.URL_PROG_BETA_UPDATE,
                ProgramTools.getProgVersionInt(), ProgramTools.getBuildInt(),
                ProgConfig.SYSTEM_UPDATE_BETA_VERSION_SHOWN,
                ProgConfig.SYSTEM_UPDATE_BETA_BUILD_NO_SHOWN,
                ProgConfig.SYSTEM_UPDATE_BETA_SEARCH,
                showProgramInformation, showError);
    }

}
