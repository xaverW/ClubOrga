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


package de.p2tools.cluborga.data.feeData.feeRateData;

public class FeeRateFactory {

    public static int RATE_TYPE_SIZE = RATE_TYPE.values().length;

    public enum RATE_TYPE {

        RATE_STANDARD(0, 1, 2500, "Standardbeitrag", "das ist der Standardbeitrag"),
        RATE_FREE(1, 2, 314, "freie Beitragswahl", "der Beitrag kann frei festgelegt werden"),
        RATE_WITHOUT(2, 3, 0, "ohne Beitrag", "es muss kein Beitrag bezahlt werden");

        private final int id;
        private final int shownNo;
        private final long betrag;
        private final String name;
        private final String description;

        RATE_TYPE(final int id, final int shownNo, final long betrag, final String name, final String description) {
            this.id = id;
            this.shownNo = shownNo;
            this.betrag = betrag;
            this.name = name;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public int getShownNo() {
            return shownNo;
        }

        public long getBetrag() {
            return betrag;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    private FeeRateFactory() {
    }
}
