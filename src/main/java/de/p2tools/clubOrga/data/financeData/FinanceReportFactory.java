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
    private FinanceReportFactory() {
    }

    public static String KONTO = "Konten:\n";
    public static String KATEGORIE = "Kategorie:\n";


    public static void makeReportData(ClubConfig clubConfig, FinanceReportDataList financeReportDataList) {
        // tabelData
        financeReportDataList.getAccounts().clear();
        clubConfig.financeAccountDataList.stream().forEach(acc -> {
            financeReportDataList.getAccounts().add(acc.getKonto());
        });

        financeReportDataList.getCategories().clear();
        clubConfig.financeCategoryDataList.stream().forEach(cat -> {
            financeReportDataList.getCategories().add(cat.getKategorie());
        });

        clubConfig.financeDataList.stream().forEach(fi -> {
            FinanceReportData data = new FinanceReportData();
            data.setNr(fi.getNr());
            data.setBelegNr(fi.getBelegNr());
            data.setGeschaeftsJahr(fi.getGeschaeftsJahr());
            data.setGesamtbetrag(fi.getGesamtbetrag());
            data.setBuchungsDatum(fi.getBuchungsDatum());

            clubConfig.financeAccountDataList.stream().forEach(acc -> {
                if (fi.getFinanceAccountData().getId() == acc.getId()) {
                    data.getAccountList().add(fi.getGesamtbetrag());
                } else {
                    data.getAccountList().add(0L);
                }
            });

            long money = 0;
            for (FinanceCategoryData cat : clubConfig.financeCategoryDataList) {
                money = 0;

                for (TransactionData tr : fi.getTransactionDataList()) {
                    if (tr.getFinanceCategoryData().getId() == cat.getId()) {
                        money += tr.getBetrag();
                    }
                }

                data.getCategoryList().add(money);

            }

            financeReportDataList.add(data);
        });

    }

}
