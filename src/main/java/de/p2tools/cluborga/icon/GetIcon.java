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

package de.p2tools.cluborga.icon;

import de.p2tools.cluborga.config.prog.ProgConst;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GetIcon {

    private final static String PFAD_PROGRAMM = ProgConst.P2_ICON_PATH + "program/";

    public static ImageView getImageView(String strIcon, int w, int h) {
        return new ImageView(getImage(strIcon, PFAD_PROGRAMM, w, h));
    }

    public static ImageView getImageView(String strIcon) {
        return new ImageView(getImage(strIcon, PFAD_PROGRAMM, 0, 0));
    }

    public static Image getImage(String strIcon, int w, int h) {
        return getImage(strIcon, PFAD_PROGRAMM, w, h);
    }

    public static Image getImage(String strIcon) {
        return getImage(strIcon, PFAD_PROGRAMM, 0, 0);
    }

    public static Image getImage(String strIcon, String path, int w, int h) {
        Image icon;
        icon = getStdImage(strIcon, path, w, h);
        return icon;
    }

    private static Image getStdImage(String strIcon, String path, int w, int h) {
        return new Image(GetIcon.class.getResource(path + strIcon).toExternalForm(),
                w, h, false, true);
    }
}
