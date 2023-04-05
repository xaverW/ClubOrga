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

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.data.feeData.FeeData;
import de.p2tools.cluborga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.cluborga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.config.Config_pDataList;
import de.p2tools.p2lib.tools.PIndex;

import java.time.LocalDate;
import java.util.List;

public class FinanceFactory {
    private FinanceFactory() {
    }


    public static FinanceData getNewFinanceWithId(ClubConfig clubConfig) {
        FinanceData financeData = new FinanceData(clubConfig);

        financeData.setId(PIndex.getIndex());
        return financeData;
    }


    /**
     * ist zum Anlegen eines neuen Finazeintrags
     * nötiges INIT wird hier gemacht
     *
     * @param clubConfig
     * @param buchungsDatum
     * @param betrag
     * @param financeAccountData
     * @param financeCategoryData
     */
    public static FinanceData getNewFinanceData(ClubConfig clubConfig,
                                                LocalDate buchungsDatum, long betrag,
                                                FinanceAccountData financeAccountData,
                                                FinanceCategoryData financeCategoryData) {

        FinanceData financeData = getNewFinanceWithId(clubConfig);

        financeData.setNo(clubConfig.financeDataList.getNextNr());
        financeData.setReceiptNo(financeData.getNo() + "");
        financeData.setBuchungsDatum(buchungsDatum);
        financeData.setGeschaeftsJahr(LocalDate.now().getYear());
        financeData.setFinanceAccountData(financeAccountData);

        TransactionDataList transactionDataList = financeData.getTransactionDataList();
        TransactionData transactionData = new TransactionData(transactionDataList.getNextNr(), clubConfig);

        transactionData.setFinanceCategoryData(financeCategoryData);

        transactionData.setBetrag(betrag);
        transactionDataList.add(transactionData);

        return financeData;
    }

    public static FinanceData getNewFinanceDataForFeeData(ClubConfig clubConfig, List<FeeData> feeDataList,
                                                          LocalDate buchungsDatum, int geschaeftsjahr,
                                                          FinanceAccountData financeAccountData, FinanceCategoryData financeCategoryData) {

        FinanceData financeData = getNewFinanceWithId(clubConfig);

        financeData.setNo(clubConfig.financeDataList.getNextNr());
        financeData.setReceiptNo(financeData.getNo() + "");
        financeData.setBuchungsDatum(buchungsDatum);
        financeData.setGeschaeftsJahr(LocalDate.now().getYear());
        financeData.setFinanceAccountData(financeAccountData);

        TransactionDataList transactionDataList = financeData.getTransactionDataList();

        feeDataList.stream().forEach(feeData1 -> {
            TransactionData transactionData = new TransactionData(transactionDataList.getNextNr(), clubConfig);

            transactionData.setFinanceCategoryData(financeCategoryData);

            transactionData.setBetrag(feeData1.getBetrag());
            transactionDataList.add(transactionData);
        });

        financeData.setGeschaeftsJahr(geschaeftsjahr);

        return financeData;
    }

    public static FinanceData getNewFinanceDataForFeeData(ClubConfig clubConfig, FeeData feeData,
                                                          LocalDate buchungsDatum, int geschaeftsjahr,
                                                          FinanceAccountData financeAccountData,
                                                          FinanceCategoryData financeCategoryData) {

        FinanceData financeData = getNewFinanceData(clubConfig, buchungsDatum, feeData.getBetrag(), financeAccountData, financeCategoryData);

        financeData.setGeschaeftsJahr(geschaeftsjahr);
        financeData.getTransactionDataList().get(0).setFeeData(feeData);

        return financeData;
    }

    public static void addNewTransactionDataForFeeData(ClubConfig clubConfig, FinanceData financeData,
                                                       FeeData feeData,
                                                       FinanceCategoryData financeCategoryData) {

        TransactionDataList transactionDataList = financeData.getTransactionDataList();
        TransactionData transactionData = new TransactionData(transactionDataList.getNextNr(), clubConfig);

        transactionData.setFinanceCategoryData(financeCategoryData);
        transactionData.setBetrag(feeData.getBetrag());
        transactionData.setFeeData(feeData);

        transactionDataList.add(transactionData);
    }

    public static FinanceData getNewFinanceDataForFeeData(ClubConfig clubConfig, FeeData feeData,
                                                          LocalDate buchungsDatum, int geschaeftsjahr,
                                                          FinanceCategoryData financeCategoryData) {

        FinanceData financeData = getNewFinanceData(clubConfig, buchungsDatum, feeData.getBetrag(),
                feeData.getPaymentTypeData().getFinanceAccountData(), financeCategoryData);

        financeData.setGeschaeftsJahr(geschaeftsjahr);
        financeData.getTransactionDataList().get(0).setFeeData(feeData);

        return financeData;
    }


    /**
     * copy memberData FROM to the memeberData TO
     *
     * @param dataFrom
     * @param dataTo
     */
    public static FinanceData copyFinanceData(ClubConfig clubConfig, FinanceData dataFrom, FinanceData dataTo) {
        if (dataFrom == null) {
            return null;
        }
        if (dataTo == null) {
            dataTo = FinanceFactory.getNewFinanceWithId(clubConfig);
        }

        Config[] configs = dataFrom.getConfigsArr();
        Config[] configsCopy = dataTo.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {

            if (configs[i] instanceof Config_pDataList) {
                TransactionFactory.copyTransactionDataList(clubConfig,
                        dataFrom.getTransactionDataList(), dataTo.getTransactionDataList());
            } else {
                configsCopy[i].setActValue(configs[i].getActValueString());
            }
        }

        dataTo.setFinanceAccountData(dataFrom.getFinanceAccountData());

        return dataTo;
    }
}
