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


package de.p2tools.clubOrga.config.club;


import de.p2tools.p2Lib.configFile.pConfData.PColorData;
import de.p2tools.p2Lib.configFile.pConfData.PColorList;
import de.p2tools.p2Lib.configFile.pData.PData;
import javafx.scene.paint.Color;

public class ProgColorList extends PColorList {

    // Filter wenn RegEx
    public static final PColorData FILTER_REGEX = addNewKey("filter-regex", Color.rgb(153, 214, 255), "Filter ist RegEx");
    public static final PColorData FILTER_REGEX_ERROR = addNewKey("filter-regex-error", Color.RED, "Filter ist Regex, fehlerhaft");

    public static final PColorData CLUB_AUTOSTART = addNewKey("club-autostart", Color.rgb(145, 200, 255), "Autostart Club");

    public static PData getConfigsData() {
        return PColorList.getPData();
    }

}
