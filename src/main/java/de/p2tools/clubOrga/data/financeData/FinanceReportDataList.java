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
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;

import java.util.*;

public class FinanceReportDataList extends SimpleListProperty<FinanceReportData> {

    private final ClubConfig clubConfig;
    private final TreeMap<Long, Long> map = new TreeMap<>();
    private final Collection<FinanceReportData> reportDataList = new ArrayList<>();
    private final LongProperty sumProperty = new SimpleLongProperty();
    private final boolean category;

    public FinanceReportDataList(ClubConfig clubConfig, boolean category) {
        super(FXCollections.observableArrayList());
        this.clubConfig = clubConfig;
        this.category = category;
    }

    public LongProperty getSumProperty() {
        return sumProperty;
    }

    public long addData(List<FinanceData> list) {
        long ret = 0;
        map.clear();
        reportDataList.clear();

        for (FinanceData fd : list) {
            if (!category) {
                // dann werden die Konten gezählt
                ret += fd.getGesamtbetrag();
                addTr(fd);
            } else {
                // dann werden die Kategorieren gezählt
                for (TransactionData td : fd.getTransactionDataList()) {
                    ret += td.getBetrag();
                    addTr(td);
                }
            }
        }

        Map.Entry<Long, Long> entry;
        while ((entry = map.pollFirstEntry()) != null) {

            if (category) {
                // Category
                FinanceCategoryData ca = clubConfig.financeCategoryDataList.getById(entry.getKey());
                if (ca != null) {
                    FinanceReportData reportData = new FinanceReportData();
                    reportData.setCategory(ca.getKategorie());
                    reportData.setValue(entry.getValue());
                    reportDataList.add(reportData);
                }

            } else {
                // Account
                FinanceAccountData ca = clubConfig.financeAccountDataList.getById(entry.getKey());
                if (ca != null) {
                    FinanceReportData reportData = new FinanceReportData();
                    reportData.setCategory(ca.getKonto());
                    reportData.setValue(entry.getValue());
                    reportDataList.add(reportData);
                }
            }
        }

        this.setAll(reportDataList);
        sumProperty.set(ret);
        return ret;
    }

    private void addTr(FinanceData financeData) {
        Long l;
        final FinanceAccountData fad = financeData.getFinanceAccountData();
        if (fad == null) {
            // passiert beim zweiten mal Öffnen des Clubs, Daten werde geladen aber
            // CategoryData und AccountData sind noch nicht gesetzt
//            System.out.println("Mist"); todo wird beim laden bei jedem add aufgerufen
            return;
        }

        l = map.get(financeData.getFinanceAccountData().getId());
        if (l == null) {
            map.put(financeData.getFinanceAccountData().getId(), financeData.financeDataGetSumBetrag());
        } else {
            map.put(financeData.getFinanceAccountData().getId(), l + financeData.financeDataGetSumBetrag());
        }
    }

    private void addTr(TransactionData transactionData) {
        Long l;
        final FinanceCategoryData fcd = transactionData.getFinanceCategoryData();
        if (fcd == null) {
            // passiert beim zweiten mal Öffnen des Clubs, Daten werde geladen aber
            // CategoryData und AccountData sind noch nicht gesetzt
//            System.out.println("Mist"); todo wird beim laden bei jedem add aufgerufen
            return;
        }

        l = map.get(transactionData.getFinanceCategoryData().getId());
        if (l == null) {
            map.put(transactionData.getFinanceCategoryData().getId(), transactionData.getBetrag());
        } else {
            map.put(transactionData.getFinanceCategoryData().getId(), l + transactionData.getBetrag());
        }
    }

}
