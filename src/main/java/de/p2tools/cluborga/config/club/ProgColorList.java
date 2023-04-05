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


package de.p2tools.cluborga.config.club;


import de.p2tools.cluborga.config.prog.ProgConfig;
import de.p2tools.p2lib.configfile.pdata.PData;
import de.p2tools.p2lib.data.PColorData;
import de.p2tools.p2lib.data.PColorList;
import javafx.scene.paint.Color;

public class ProgColorList extends PColorList {

    public static final PColorData CLUB_AUTOSTART = addNewKey("club-autostart",
            Color.rgb(145, 200, 255), Color.rgb(60, 185, 255), "Autostart Club");

    public synchronized static void setColorTheme() {
        final boolean dark = ProgConfig.SYSTEM_DARK_THEME.get();
        for (int i = 0; i < PColorList.getInst().size(); ++i) {
            PColorList.getInst().get(i).setColorTheme(dark);
        }
    }

    public static PData getConfigsData() {
        //todo PColor hat sich geänder!!!!!!!!!!!!!
        return PColorList.getInst().getNewItem();
    }

}
