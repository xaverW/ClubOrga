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


package de.p2tools.cluborga.data.financeData;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.p2lib.tools.PIndex;

import java.text.DecimalFormat;

public class FinanceData extends FinanceDataWorker {
    private static final DecimalFormat DF;

    static {
        DF = new DecimalFormat("###,##0.00");
    }

    public FinanceData(ClubConfig clubConfig) {
        setClubConfig(clubConfig);
        transactionDataList = new TransactionDataList();
        addListenerToTransactionDataList();

        addExtra();
    }

    public void initDataAfterClubLoad(ClubConfig clubConfig) {
        this.initFinanceAccountData(clubConfig);
    }

    private void addTransaction(TransactionData transactionData) {
        getTransactionDataList().add(transactionData);
    }

    private long getNewId() {
        return PIndex.getIndex();
    }


    private void addListenerToTransactionDataList() {
        transactionDataList.addListener((observable, oldValue, newValue) -> {
            setTransactionValues();
        });
        transactionDataList.listChangedProperty().addListener((observable, oldValue, newValue) -> {
            setTransactionValues();
        });
    }

    public void setTransactionValues() {
        financeDataGetSumBetrag();
        financeDataGetKategorieList();
    }

    @Override
    public int compareTo(FinanceData o) {
        return o.getBuchungsDatum().compareTo(o.getBuchungsDatum());
    }
}
