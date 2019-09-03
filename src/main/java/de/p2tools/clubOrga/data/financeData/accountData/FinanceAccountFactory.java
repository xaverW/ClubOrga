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


package de.p2tools.clubOrga.data.financeData.accountData;

import de.p2tools.clubOrga.config.club.ClubConfig;

public class FinanceAccountFactory {

    public static int ACCOUNT_TYPE_SIZE = ACCOUNT_TYPE.values().length;

    public enum ACCOUNT_TYPE {

        ACCOUNT_BAR(0, 1, "Bar", "Barkasse"),
        ACCOUNT_GIRO(1, 2, "Giro", "Girokonto");

        private final int id;
        private final int shownNo;
        private final String name;
        private final String description;

        ACCOUNT_TYPE(final int id, final int shownNo, final String name, final String description) {
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

    private FinanceAccountFactory() {
    }

    public static boolean searchAccount(ClubConfig clubConfig, FinanceAccountData financeAccountData) {
        boolean found = clubConfig.financeDataList.stream()
                .filter(financeData -> financeData.getFinanceAccountData().equals(financeAccountData))
                .findAny().isPresent();
        return found;
    }
}
