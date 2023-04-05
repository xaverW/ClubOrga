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


package de.p2tools.cluborga.data.memberData.stateData;

public class StateDataFactory {

    public static int STATE_TYPE_SIZE = STATE_TYPE.values().length;

    public enum STATE_TYPE {

        STATE_ACTIVE(0, 1, "aktiv", "aktives Mitglied"),
        STATE_PASSIVE(1, 2, "passiv", "passives Mitglied");

        private final int id;
        private final int shownNo;
        private final String name;
        private final String description;

        STATE_TYPE(final int id, final int shownNo, final String name, final String description) {
            this.id = id;
            this.shownNo = shownNo;
            this.name = name;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public int getShownNo() {
            return shownNo;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    private StateDataFactory() {
    }
}
