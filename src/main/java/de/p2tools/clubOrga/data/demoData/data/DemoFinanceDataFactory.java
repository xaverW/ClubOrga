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


package de.p2tools.clubOrga.data.demoData.data;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.demoData.DemoDataFactory;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFactory;
import de.p2tools.clubOrga.data.financeData.TransactionData;
import de.p2tools.clubOrga.data.financeData.TransactionDataList;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountFactory;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class DemoFinanceDataFactory {
    private DemoFinanceDataFactory() {

    }

    private static final String[] arrText = {
            "für Jan", "für Christian", "für Patrick", "für Marcel", "für Fabian", "für Tim", "für Benjamin", "für Maximilian",
            "für Simon", "für Marco", "für Michael", "für Markus", "für Nils", "für Julian", "für Felix", "für Florian", "für Philipp", "für Daniel", "für Timo", "für Matthias"};


    public static ArrayList<FinanceCategoryData> getDemoFinanceCategory(ClubConfig clubConfig) {

        if (!clubConfig.DEMO_DATA_FINANCES.get()) {
            // nichts anlegen
            return null;
        }

        ArrayList<FinanceCategoryData> list = new ArrayList<>();
        // Finanzen eintragen
        FinanceCategoryData fcBrot = new FinanceCategoryData(clubConfig);
        fcBrot.setCategory("Brot und Spiele");
        list.add(fcBrot);

        FinanceCategoryData fcBildung = new FinanceCategoryData(clubConfig);
        fcBildung.setCategory("Bildung");
        list.add(fcBildung);

        FinanceCategoryData fcMaterial = new FinanceCategoryData(clubConfig);
        fcMaterial.setCategory("Süßwaren");
        list.add(fcMaterial);

        FinanceCategoryData fcAuto = new FinanceCategoryData(clubConfig);
        fcAuto.setCategory("Auto");
        list.add(fcAuto);

        FinanceCategoryData fcSport = new FinanceCategoryData(clubConfig);
        fcSport.setCategory("Sport");
        list.add(fcSport);

        FinanceCategoryData fcWein = new FinanceCategoryData(clubConfig);
        fcWein.setCategory("Weinkeller");
        list.add(fcWein);

        FinanceCategoryData fcKonzert = new FinanceCategoryData(clubConfig);
        fcKonzert.setCategory("Konzerte");
        list.add(fcKonzert);


        long no = clubConfig.financeCategoryDataList.getNextNr();
        for (FinanceCategoryData categoryData : list) {
            categoryData.setNo(no++);
        }

        return list;
    }

    public static ArrayList<FinanceData> getDemoFinance(ClubConfig clubConfig, ArrayList<FinanceCategoryData> financeCategoryData, int number) {

        if (!clubConfig.DEMO_DATA_FINANCES.get()) {
            // nichts anlegen
            return null;
        }

        if (number <= 0) {
            // nichts anlegen
            return null;
        }

        ArrayList<FinanceData> newFinance = new ArrayList<>(number);
        Random random = new Random();

        FinanceAccountData financeAccountDataBar = clubConfig.financeAccountDataList.
                getAccountDataStandard(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_BAR);
        FinanceAccountData financeAccountDataGiro = clubConfig.financeAccountDataList.
                getAccountDataStandard(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_GIRO);

        for (int i = 0; i < number; ++i) {
            FinanceData financeData = FinanceFactory.getNewFinanceWithId(clubConfig);
            financeData.setText(arrText[random.nextInt(arrText.length)]);

            FinanceAccountData fa = random.nextBoolean() ? financeAccountDataBar : financeAccountDataGiro;
            financeData.setFinanceAccountData(fa);

            // Datum
            LocalDateTime dateTime = DemoDataFactory.getRandomDate();
            financeData.setGeschaeftsJahr(dateTime.getYear());
            financeData.getBuchungsDatum().setPLocalDate(dateTime.toLocalDate());
            financeData.getErstellDatum().setPLocalDate(dateTime.toLocalDate());


            // Transaktion
            TransactionDataList transactionDataList = financeData.getTransactionDataList();
            TransactionData transactionData = new TransactionData(transactionDataList.getNextNr(), clubConfig);

            final int size = financeCategoryData.size();
            FinanceCategoryData fc = financeCategoryData.get(random.nextInt(size));
            transactionData.setFinanceCategoryData(fc);

            transactionData.setBetrag((1 + random.nextInt(20)) * (random.nextBoolean() ? -1 : 1) * 1000); // +/- 10 .. 200
            transactionDataList.add(transactionData);


            newFinance.add(financeData);
        }

        return newFinance;
    }
}
