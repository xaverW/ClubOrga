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
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
import javafx.collections.transformation.FilteredList;

import java.util.ArrayList;

public class FinanceReportFactory {

    public static String KONTO = "Konten:\n";
    public static String KATEGORIE = "Kategorie:\n";

    private FinanceReportFactory() {
    }

    public static void makeReportData(ClubConfig clubConfig) {
        clubConfig.financeReportDataList.clear();

        // tabel header
        clubConfig.financeAccountDataList.stream().forEach(acc -> {
            clubConfig.financeReportDataList.getAccounts().add(acc.getKonto());
        });
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

            // die Konten holen
            ArrayList<Long> accountList = new ArrayList<>();
            ArrayList<Long> categoryList = new ArrayList<>();
            clubConfig.financeAccountDataList.stream().forEach(financeAccountData -> {

                if (financeData.getFinanceAccountData().equals(financeAccountData)) {
                    reportData.getAccountList().add(new FinanceReportAccountData(financeAccountData.getId(), financeData.getGesamtbetrag()));
                } else {
                    reportData.getAccountList().add(new FinanceReportAccountData(ProgConst.FILTER_ID_NOT_SELECTED, 0));
                }

            });

            // die Kategorien der Transaktionen aufsummieren
            long money;
            for (FinanceCategoryData cat : clubConfig.financeCategoryDataList) {

                money = 0;
                for (TransactionData tr : financeData.getTransactionDataList()) {
                    if (tr.getFinanceCategoryData().equals(cat)) {
                        money += tr.getBetrag();
                    }
                }
                if (money != 0) {
                    reportData.getCategoryList().add(new FinanceReportCategoryData(cat.getId(), money));
                } else {
                    reportData.getCategoryList().add(new FinanceReportCategoryData(ProgConst.FILTER_ID_NOT_SELECTED, 0));
                }

            }

            // Daten in Liste eintragen
            clubConfig.financeReportDataList.add(reportData);
        });

    }

    public static void makeSumReportData(ClubConfig clubConfig) {
        long sum = 0;
        FinanceReportData reportDataSum = new FinanceReportData();

        sum = 0;
        final FilteredList<FinanceReportData> financeReportData = clubConfig.financeReportDataList.getFilteredList();
        for (FinanceReportData reportData : financeReportData) {
            sum += reportData.getGesamtbetrag();
        }
        reportDataSum.setGesamtbetrag(sum);

        for (int i = 0; i < clubConfig.financeAccountDataList.size(); ++i) {
            sum = 0;
            for (FinanceReportData reportData : financeReportData) {
                sum += reportData.getAccountList().get(i).getBetrag();
            }
            final long id = clubConfig.financeAccountDataList.get(i).getId();
            reportDataSum.getAccountList().add(new FinanceReportAccountData(id, sum));
        }

        for (int i = 0; i < clubConfig.financeCategoryDataList.size(); ++i) {
            sum = 0;
            for (FinanceReportData reportData : financeReportData) {
                sum += reportData.getCategoryList().get(i).getBetrag();
            }
            final long id = clubConfig.financeCategoryDataList.get(i).getId();
            reportDataSum.getCategoryList().add(new FinanceReportCategoryData(id, sum));
        }

        clubConfig.financeReportDataListSum.clear();
        clubConfig.financeReportDataListSum.add(reportDataSum);
    }
}
