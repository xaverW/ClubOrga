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

package de.p2tools.clubOrga.data.financeData;

import de.p2tools.clubOrga.config.club.ClubConfig;

public class FinanceDataListWorker extends FinanceDataListBase {

    public FinanceDataListWorker(ClubConfig clubConfig) {
        super(clubConfig);
    }

    /**
     * init Liste nach Programmstart
     *
     * @param clubConfig
     */
    public void initListAfterClubLoad(ClubConfig clubConfig) {
        this.stream().forEach(financeData -> {

            financeData.getTransactionDataList().stream().forEach(transactionData -> {
                transactionData.initDataAfterClubLoad(clubConfig);
            });

            financeData.initDataAfterClubLoad(clubConfig);
            financeData.setTransactionValues();
        });
        initAfterAdding();
    }

    public long getSaldo() {
        long saldo = this.stream().mapToLong(financeData -> financeData.financeDataGetSumBetrag()).sum();
        return saldo;
    }

    public long getSaldo(int year) {
        long saldo = this.stream().filter(financeData -> financeData.getGeschaeftsJahr() == year)
                .mapToLong(financeData -> financeData.financeDataGetSumBetrag()).sum();
        return saldo;
    }
}
