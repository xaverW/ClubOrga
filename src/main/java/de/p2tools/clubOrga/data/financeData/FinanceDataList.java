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
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.date.PLocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FinanceDataList extends FinanceDataListWorker {

    public FinanceDataList(ClubConfig clubConfig) {
        super(clubConfig);
    }

    public boolean financeDataListRemoveAll(Collection<FinanceData> list) {
        if (PAlert.BUTTON.YES == PAlert.showAlert_yes_no(clubConfig.getStage(), "Löschen",
                "Finanzdaten löschen?",
                list.size() == 1 ? "Soll 1 Finanzeintrag gelöscht werden?" :
                        "Sollen " + list.size() + " Finanzen gelöscht werden?")) {

            return removeAll(list);
        }

        return false;
    }

    /**
     * für bezahlte Beiträge wird ein Eintrag in den Finanzen angelegt
     * onlyTransaction: Beiträge werden als Transaktionen nur eines Finanzeintrags angelegt
     *
     * @param clubConfig
     * @param list
     * @param onlyTransaction
     * @param buchungsDatum
     * @param geschaeftsjahr
     * @param financeCategoryData
     */
    public void addFinanceFromPayedFee(ClubConfig clubConfig, List<FeeData> list, boolean onlyTransaction,
                                       PLocalDate buchungsDatum, int geschaeftsjahr,
                                       FinanceCategoryData financeCategoryData) {


        ArrayList<FinanceData> financeDataList = new ArrayList<>();

        list.stream().forEach(feeData -> {

            if (onlyTransaction && !financeDataList.isEmpty()) {
                final FinanceData financeData = financeDataList.get(0);
                FinanceFactory.addNewTransactionDataForFeeData(clubConfig, financeData, feeData,
                        financeCategoryData);

            } else {
                final FinanceAccountData financeAccountData = feeData.getPaymentTypeData().getFinanceAccountData();
                final FinanceData financeData = FinanceFactory.getNewFinanceDataForFeeData(clubConfig, feeData,
                        buchungsDatum, geschaeftsjahr, financeAccountData, financeCategoryData);
                financeDataList.add(financeData);
            }

        });

        super.addAll(financeDataList);
    }

}
