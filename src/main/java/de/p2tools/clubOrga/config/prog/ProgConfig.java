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


package de.p2tools.clubOrga.config.prog;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.pData.PDataProgConfig;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class ProgConfig extends PDataProgConfig {

    private static final ArrayList<Config> arrayList = new ArrayList<>();

    public static StringProperty SYSTEM_UPDATE_DATE = addStr("system-update-date"); // Datum der letzten Prüfung
    public static StringProperty SYSTEM_LOG_DIR = addStr("system-log-dir", "");
    public static BooleanProperty SYSTEM_UPDATE_SEARCH = addBool("system-update-search-prog-update", true);
    public static BooleanProperty SYSTEM_UPDATE_BETA_SEARCH = addBool("system-update-beta-search", false);
    public static IntegerProperty SYSTEM_UPDATE_VERSION_SHOWN = addInt("system-update-version-shown", 0);
    public static IntegerProperty SYSTEM_UPDATE_INFO_NR_SHOWN = addInt("system-update-info-nr-shown", 0);
    public static IntegerProperty SYSTEM_UPDATE_BETA_VERSION_SHOWN = addInt("system-update-beta-version-shown", 0);
    public static IntegerProperty SYSTEM_UPDATE_BETA_BUILD_NO_SHOWN = addInt("system-update-beta-build-nr-shown", 0);

//    public static MLConfigs SYSTEM_UPDATE_BETA_VERSION_SHOWN = addNewKey("system-update-beta-version-shown"); // zuletzt angezeigtes Update mit versionNo
//    public static MLConfigs SYSTEM_UPDATE_BETA_BUILD_NO_SHOWN = addNewKey("system-update-beta-build-nr-shown"); // zuletzt angezeigtes Update mit buildNo
//    public static MLConfigs SYSTEM_UPDATE_PROGSET_VERSION = addNewKey("system-update-progset-version");


    // GuiStart
    public static StringProperty SYSTEM_SIZE_CLUB_SELECTOR_GUI = addStr("system-size-club-selector-gui", "800:500");

    // ConfigDialog
    public static BooleanProperty START_CLUB_SELECTOR_FIRST = addBool("start-club-slector-first", false);
    public static StringProperty IMPORT_DIR_CLUB_DIALOG_SIZE = addStr("import-dir-club-dialog-size", "700:400");
    public static StringProperty IMPORT_ZIP_CLUB_DIALOG_SIZE = addStr("import-zip-club-dialog-size", "700:400");
    public static StringProperty ADD_NEW_CLUB_DIALOG_SIZE = addStr("add-new-club-dialog-size", "700:400");
    public static BooleanProperty SYSTEM_DARK_THEME = addBool("system-dark-theme", false);


    private static ProgConfig instance;

    private ProgConfig() {
        super(arrayList, "ProgConfig");
    }

    public static final ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }

    private static StringProperty addStr(String key) {
        return addStrProp(arrayList, key);
    }

    private static StringProperty addStr(String key, String init) {
        return addStrProp(arrayList, key, init);
    }

    private static IntegerProperty addInt(String key, int init) {
        return addIntProp(arrayList, key, init);
    }

    private static BooleanProperty addBool(String key, boolean init) {
        return addBoolProp(arrayList, key, init);
    }

}
