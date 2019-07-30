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


package de.p2tools.clubOrga.data.extraData;

public class ExtraKind {

    public enum EXTRA_KIND {

        STRING("Text"), INTEGER("Zahl"), BOOLEAN("Ja-Nein");
        private final String name;

        EXTRA_KIND(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }


    }

    public static EXTRA_KIND getExtraKind(String str) {
        if (str.equals(EXTRA_KIND.BOOLEAN.name)) {
            return EXTRA_KIND.BOOLEAN;
        } else if (str.equals(EXTRA_KIND.INTEGER.name)) {
            return EXTRA_KIND.INTEGER;
        } else {
            return EXTRA_KIND.STRING;
        }
    }
}
