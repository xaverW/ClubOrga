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


package de.p2tools.cluborga.config.prog;

import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.data.PDataProgConfig;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class ProgConfig extends PDataProgConfig {

    private static final ArrayList<Config> arrayList = new ArrayList<>();

    // Configs der Programmversion
    public static StringProperty SYSTEM_PROG_VERSION = addStrProp("system-prog-version");
    public static StringProperty SYSTEM_PROG_BUILD_NO = addStrProp("system-prog-build-no");
    public static StringProperty SYSTEM_PROG_BUILD_DATE = addStrProp("system-prog-build-date");

    // Configs zur Programmupdatesuche
    public static StringProperty SYSTEM_UPDATE_DATE = addStrProp("system-update-date"); // Datum der letzten Prüfung
    public static StringProperty SYSTEM_UPDATE_PROGSET_VERSION = addStrProp("system-update-progset-version");

    public static BooleanProperty SYSTEM_UPDATE_SEARCH_ACT = addBoolProp("system-update-search-act", true); //Infos und Programm
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_BETA = addBoolProp("system-update-search-beta", false); //beta suchen
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_DAILY = addBoolProp("system-update-search-daily", false); //daily suchen

    public static StringProperty SYSTEM_UPDATE_LAST_INFO = addStrProp("system-update-last-info");
    public static StringProperty SYSTEM_UPDATE_LAST_ACT = addStrProp("system-update-last-act");
    public static StringProperty SYSTEM_UPDATE_LAST_BETA = addStrProp("system-update-last-beta");
    public static StringProperty SYSTEM_UPDATE_LAST_DAILY = addStrProp("system-update-last-daily");

    public static StringProperty SYSTEM_DOWNLOAD_DIR_NEW_VERSION = addStrProp("system-download-dir-new-version", "");

    public static StringProperty SYSTEM_LOG_DIR = addStrProp("system-log-dir", "");
    public static BooleanProperty SYSTEM_UPDATE_SEARCH = addBoolProp("system-update-search-prog-update", true);
    public static BooleanProperty SYSTEM_UPDATE_BETA_SEARCH = addBoolProp("system-update-beta-search", false);
    public static IntegerProperty SYSTEM_UPDATE_VERSION_SHOWN = addIntProp("system-update-version-shown", 0);
    public static IntegerProperty SYSTEM_UPDATE_INFO_NR_SHOWN = addIntProp("system-update-info-nr-shown", 0);
    public static IntegerProperty SYSTEM_UPDATE_BETA_VERSION_SHOWN = addIntProp("system-update-beta-version-shown", 0);
    public static IntegerProperty SYSTEM_UPDATE_BETA_BUILD_NO_SHOWN = addIntProp("system-update-beta-build-nr-shown", 0);
    public static BooleanProperty CONFIG_DIALOG_ACCORDION = addBoolProp("config_dialog-accordion", true);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_CONFIG = new SimpleIntegerProperty(-1);

//    public static MLConfigs SYSTEM_UPDATE_BETA_VERSION_SHOWN = addNewKey("system-update-beta-version-shown"); // zuletzt angezeigtes Update mit versionNo
//    public static MLConfigs SYSTEM_UPDATE_BETA_BUILD_NO_SHOWN = addNewKey("system-update-beta-build-nr-shown"); // zuletzt angezeigtes Update mit buildNo
//    public static MLConfigs SYSTEM_UPDATE_PROGSET_VERSION = addNewKey("system-update-progset-version");


    // GuiStart
    public static StringProperty CLUB_SELECTOR_GUI_SIZE = addStrProp("club-selector-gui-size", "800:500");

    // ConfigDialog
    public static BooleanProperty START_CLUB_SELECTOR_FIRST = addBoolProp("start-club-slector-first", false);
    public static StringProperty IMPORT_DIR_CLUB_DIALOG_SIZE = addStrProp("import-dir-club-dialog-size", "700:400");
    public static StringProperty IMPORT_ZIP_CLUB_DIALOG_SIZE = addStrProp("import-zip-club-dialog-size", "700:400");
    public static StringProperty ADD_NEW_CLUB_DIALOG_SIZE = addStrProp("add-new-club-dialog-size", "700:400");
    public static BooleanProperty SYSTEM_DARK_THEME = addBoolProp("system-dark-theme", false);

    private static ProgConfig instance;

    private ProgConfig() {
        super("ProgConfig");
    }

    public static final ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }
}
