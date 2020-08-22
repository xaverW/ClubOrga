/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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

public class TransactionData extends TransactionDataWorker {


    public TransactionData() {
        setId(getNewId());
        addListener();
    }


    /*
 ist zum Anlegen einer neuen Teilbuchung
 nötiges INIT wird hier gemacht
  */
    public TransactionData(long nr, ClubConfig clubConfig) {
        setId(getNewId());
        setNo(nr);
//        setFinanceAccountData(clubConfig.financeAccountDataList.get(0));
        setFinanceCategoryData(clubConfig.financeCategoryDataList.get(0));
        addListener();
    }

    public void initDataAfterClubLoad(ClubConfig clubConfig) {
//        this.initFinanceAccountData(clubConfig);
        this.initFinanceCategoryData(clubConfig);
        this.initFeeData(clubConfig);
    }

}
