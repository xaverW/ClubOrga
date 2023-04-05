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


package de.p2tools.cluborga.data.financeData;

import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.tools.PArray;
import de.p2tools.p2lib.tools.PStringUtils;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class FinanceDataWorker extends FinanceDataBase {

    public boolean financeDataRemoveTransactions(Stage stage, Collection<TransactionData> list) {
        if (PAlert.BUTTON.YES == PAlert.showAlert_yes_no(stage, "Löschen",
                "Transaktionen löschen?", list.size() == 1 ?
                        "Soll 1 Transaktion gelöscht werden?" :
                        "Sollen " + list.size() + " Transaktionen gelöscht werden?")) {

            return getTransactionDataList().removeAll(list);

        }
        return false;
    }

    public long financeDataGetSumBetrag() {
        long ret = getTransactionDataList().stream().mapToLong(tr -> tr.getBetrag()).sum();
        setGesamtbetrag(ret);
        return ret;
    }

    public String financeDataGetKategorieList() {
        String ret;
        if (getTransactionDataList().isEmpty()) {
            return "";
        }

        ArrayList<String> strList = new ArrayList<>();
        getTransactionDataList().stream().filter(transactionData -> transactionData.getFinanceCategoryData() != null)
                .forEach(transactionData -> strList.add(transactionData.getFinanceCategoryData().getCategory()));

        ret = PStringUtils.appendList(strList, ", ", true, true);
        setCategory(ret);

        return ret;
    }

    public double[] getSumKategorieArray() {
        // liefert ein Array mit den aufsummierten Kategorien

        double[] arr;
        arr = PArray.getDoubleArray(getClubConfig().financeCategoryDataList.size());

        this.getTransactionDataList().stream().forEach(transactionData -> {

            for (int i = 0; i < getClubConfig().financeCategoryDataList.size(); ++i) {
                if (transactionData.getFinanceCategoryData().getId() == getClubConfig().financeCategoryDataList.get(i).getId()) {
                    arr[i] += transactionData.getBetrag();
                }
            }
        });

        return arr;
    }

}
