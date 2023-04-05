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


package de.p2tools.cluborga.controller.sepa;

import de.p2tools.p2lib.tools.log.PLog;

public class SepaFactory {
    private SepaFactory() {

    }

    /**
     * @param str
     * @return
     */
    public static int parseBetragCent(String str) {
        //Betrag in Cent
        int ret = 0;
        String vor = "0";
        String hinter;
        try {
            if (str != null) {
                if (!str.equals("")) {
                    if (str.contains(".")) {
                        str = str.replace('.', ',');
                    }
                    if (str.contains(",")) {
                        int k = str.indexOf(",");
                        vor = str.substring(0, k);
                        hinter = str.substring(k + 1, str.length());
                        if (hinter.length() > 0) {
                            vor += hinter.substring(0, 1);
                        } else {
                            vor += "0";
                        }
                        if (hinter.length() > 1) {
                            vor += hinter.substring(1, 2);
                        } else {
                            vor += "0";
                        }
                    } else {
                        vor = str + "00";
                    }
                }
            }
            ret = Integer.parseInt(vor);
        } catch (NumberFormatException ex) {
            PLog.errorLog(956410101, ex, "Funktionen -  parseBetrag: " + "Falsches Zeichen bei Betrag: ");
        }
        return ret;
    }

}
