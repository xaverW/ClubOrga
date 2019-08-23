/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;

public class FinanceReportFactory {

    public static String KONTO = "Konten:\n";
    public static String KATEGORIE = "Kategorie:\n";

    private FinanceReportFactory() {
    }

    public static void makeReportData(ClubConfig clubConfig) {
        // tabel header
        clubConfig.financeReportDataList.getAccounts().clear();
        clubConfig.financeAccountDataList.stream().forEach(acc -> {
            clubConfig.financeReportDataList.getAccounts().add(acc.getKonto());
        });

        clubConfig.financeReportDataList.getCategories().clear();
        clubConfig.financeCategoryDataList.stream().forEach(cat -> {
            clubConfig.financeReportDataList.getCategories().add(cat.getKategorie());
        });


        // table data
        clubConfig.financeDataList.stream().forEach(financeData -> {
            FinanceReportData reportData = new FinanceReportData();

            reportData.setNr(financeData.getNr());
            reportData.setBelegNr(financeData.getBelegNr());
            reportData.setGeschaeftsJahr(financeData.getGeschaeftsJahr());
            reportData.setBuchungsDatum(financeData.getBuchungsDatum());
            reportData.setGesamtbetrag(financeData.getGesamtbetrag());

            clubConfig.financeAccountDataList.stream().forEach(financeAccountData -> {

                if (financeData.getFinanceAccountData().getId() == financeAccountData.getId()) {
                    reportData.getAccountList().add(new FinanceReportAccountData(financeAccountData, financeData.getGesamtbetrag()));
                } else {
                    reportData.getAccountList().add(new FinanceReportAccountData(null, 0));
                }

            });

            long money;
            for (FinanceCategoryData cat : clubConfig.financeCategoryDataList) {

                money = 0;
                for (TransactionData tr : financeData.getTransactionDataList()) {
                    if (tr.getFinanceCategoryData().getId() == cat.getId()) {
                        money += tr.getBetrag();
                    }
                }
                if (money != 0) {
                    reportData.getCategoryList().add(new FinanceReportCategoryData(cat, money));
                } else {
                    reportData.getCategoryList().add(new FinanceReportCategoryData(null, 0));
                }

            }

            clubConfig.financeReportDataList.add(reportData);
        });

    }

}
