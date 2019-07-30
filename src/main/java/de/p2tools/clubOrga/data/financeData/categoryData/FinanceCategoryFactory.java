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


package de.p2tools.clubOrga.data.financeData.categoryData;

public class FinanceCategoryFactory {

    public static int CATEGORY_TYPE_SIZE = CATEGORY_TYPE.values().length;

    public enum CATEGORY_TYPE {

        CATEGORY_BEITRAG(0, 1, "Beitrag", "Beiträge");

        private final int id;
        private final int showNo;
        private final String name;
        private final String description;

        CATEGORY_TYPE(final int id, final int showNo, final String name, final String description) {
            this.id = id;
            this.showNo = showNo;
            this.name = name;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public int getShowNo() {
            return showNo;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    private FinanceCategoryFactory() {
    }
}
