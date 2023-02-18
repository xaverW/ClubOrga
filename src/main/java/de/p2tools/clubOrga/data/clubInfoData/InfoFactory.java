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


package de.p2tools.clubOrga.data.clubInfoData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import javafx.scene.control.TreeItem;

import java.time.LocalDate;
import java.util.TreeMap;

public class InfoFactory {
    private static ClubConfig clubConfig;

    private InfoFactory() {
    }

    public static TreeItem<ClubInfoData> generate(ClubConfig cConfig) {
        clubConfig = cConfig;

        TreeItem<ClubInfoData> club = createGroup("Verein", true, false);
        club.getValue().setText(clubConfig.clubData.getName());
        TreeItem<ClubInfoData> root = new TreeItem<>(club.getValue());

        ClubInfoData clubInfoDataPath = new ClubInfoData("Speicherpfad");
        clubInfoDataPath.setText(clubConfig.getClubPath());
        root.getChildren().add(new TreeItem<>(clubInfoDataPath));

        root.getChildren().addAll(generateMemberInfo());
        root.getChildren().add(generateFeeInfo());
        root.getChildren().add(generateFinanceInfo());
        root.getChildren().add(generateFinanceInfoActYear());

        root.setExpanded(true);
        root.getChildren().stream().forEach(ch -> ch.setExpanded(true));

        return root;
    }

    private static TreeItem<ClubInfoData> generateMemberInfo() {
        TreeItem<ClubInfoData> treeItemMember = createGroup("Mitglieder", clubConfig.memberDataList.size(), "",
                true, false);

        TreeItem<ClubInfoData> stateItem = createGroup("Stati", false, false);
        clubConfig.stateDataList.stream().forEach(stateData -> {
            ClubInfoData ciStati = new ClubInfoData();
            ciStati.setName(stateData.getName());
            ciStati.setText("Mitglieder mit diesem Status");
            ciStati.setAmount(clubConfig.memberDataList.stream()
                    .filter(memberData -> memberData.getStateData().equals(stateData)).count());
            TreeItem<ClubInfoData> item = new TreeItem<>(ciStati);
            stateItem.getChildren().add(item);
        });

        TreeItem<ClubInfoData> feeRate = createGroup("Beitragssätze");
        clubConfig.feeRateDataList.stream().forEach(feeRateData -> {
            ClubInfoData ciFeeRate = new ClubInfoData();
            ciFeeRate.setName(feeRateData.getName());
            ciFeeRate.setText("Mitglieder mit diesem Beitragssatz");
            ciFeeRate.setAmount(clubConfig.memberDataList.stream()
                    .filter(memberData -> memberData.getFeeRateData().equals(feeRateData)).count());
            TreeItem<ClubInfoData> item = new TreeItem<>(ciFeeRate);
            feeRate.getChildren().add(item);
        });

        TreeItem<ClubInfoData> paymentType = createGroup("Zahlarten");
        clubConfig.paymentTypeDataList.stream().forEach(paymentTypeData -> {
            ClubInfoData ciPaymentType = new ClubInfoData();
            ciPaymentType.setName(paymentTypeData.getName());
            ciPaymentType.setText("Mitglieder mit dieser Zahlart");
            ciPaymentType.setAmount(clubConfig.memberDataList.stream()
                    .filter(memberData -> memberData.getPaymentTypeData().equals(paymentTypeData)).count());
            TreeItem<ClubInfoData> item = new TreeItem<>(ciPaymentType);
            paymentType.getChildren().add(item);
        });


        treeItemMember.getChildren().addAll(stateItem, feeRate, paymentType);
        return treeItemMember;
    }

    private static TreeItem<ClubInfoData> generateFeeInfo() {
        TreeItem<ClubInfoData> treeItemFeeInfo = createGroup("Beiträge", "" + clubConfig.feeDataList.size(), "",
                true, false);

        TreeItem<ClubInfoData> treeItemFee = createGroup("Rechnung");
        ClubInfoData ciRechnungOk = new ClubInfoData();
        ciRechnungOk.setName("erstellt");
        ciRechnungOk.setText("Rechnung wurde erstellt");
        ciRechnungOk.setAmount(clubConfig.feeDataList.stream()
                .filter(data -> !data.getRechnung().isEqual(LocalDate.MIN)).count());
        treeItemFee.getChildren().add(new TreeItem<>(ciRechnungOk));
        ClubInfoData ciRechnung = new ClubInfoData();
        ciRechnung.setName("fehlt");
        ciRechnung.setText("Rechnung wurde nicht erstellt");
        ciRechnung.setAmount(clubConfig.feeDataList.stream()
                .filter(data -> data.getRechnung().isEqual(LocalDate.MIN)).count());
        treeItemFee.getChildren().add(new TreeItem<>(ciRechnung));

        TreeItem<ClubInfoData> treeItemPayed = createGroup("Bezahlt");
        ClubInfoData ciBezahlt = new ClubInfoData();
        ciBezahlt.setName("bezahlt");
        ciBezahlt.setText("Beitrag ist bezahlt");
        ciBezahlt.setAmount(clubConfig.feeDataList.stream()
                .filter(data -> !data.getBezahlt().isEqual(LocalDate.MIN)).count());
        treeItemPayed.getChildren().add(new TreeItem<>(ciBezahlt));
        ClubInfoData ciNichtBezahlt = new ClubInfoData();
        ciNichtBezahlt.setName("nicht bezahlt");
        ciNichtBezahlt.setText("Beitrag ist nicht bezahlt");
        ciNichtBezahlt.setAmount((int) clubConfig.feeDataList.stream()
                .filter(data -> data.getBezahlt().isEqual(LocalDate.MIN)).count());
        treeItemPayed.getChildren().add(new TreeItem<>(ciNichtBezahlt));

        TreeItem<ClubInfoData> treeItemSq = createGroup("Spendenquittung");
        ClubInfoData ciSqOk = new ClubInfoData();
        ciSqOk.setName("erstellt");
        ciSqOk.setText("Spendenquittung wurde erstellt");
        ciSqOk.setAmount(clubConfig.feeDataList.stream()
                .filter(data -> !data.getSpendenQ().isEqual(LocalDate.MIN)).count());
        treeItemSq.getChildren().add(new TreeItem<>(ciSqOk));
        ClubInfoData ciSq = new ClubInfoData();
        ciSq.setName("fehlt");
        ciSq.setText("Spendenquittung wurde nicht erstellt");
        ciSq.setAmount(clubConfig.feeDataList.stream()
                .filter(data -> data.getSpendenQ().isEqual(LocalDate.MIN)).count());
        treeItemSq.getChildren().add(new TreeItem<>(ciSq));

        treeItemFeeInfo.getChildren().addAll(treeItemFee, treeItemPayed, treeItemSq);

        return treeItemFeeInfo;
    }

    private static TreeItem<ClubInfoData> generateFinanceInfo() {
        TreeItem<ClubInfoData> financeItem = createGroup("Finanzen", clubConfig.financeDataList.size(),
                "Buchungen",
                true, false);

        TreeItem<ClubInfoData> treeItemAll = createGroup("Saldo", 0.01 * clubConfig.financeDataList.getSaldo(),
                "Saldo über den gesamten Zeitraum",
                false, false);
        financeItem.getChildren().add(treeItemAll);


        TreeMap<String, Long> treeMapAccount = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        clubConfig.financeDataList.stream()
                .forEach(financeData -> {
                    if (treeMapAccount.containsKey(financeData.getFinanceAccountData().getName())) {
                        long old = treeMapAccount.get(financeData.getFinanceAccountData().getName());
                        treeMapAccount.put(financeData.getFinanceAccountData().getName(),
                                old + financeData.financeDataGetSumBetrag());
                    } else {
                        treeMapAccount.put(financeData.getFinanceAccountData().getName(),
                                financeData.financeDataGetSumBetrag());
                    }
                });

        TreeItem treeItemAccount = createGroup("Konten", "", "", false, true);
        financeItem.getChildren().add(treeItemAccount);
        treeMapAccount.entrySet().stream()
                .forEach(ac -> {
                    ClubInfoData cid = new ClubInfoData(ac.getKey(), 0.01 * ac.getValue(), "");
                    treeItemAccount.getChildren().add(new TreeItem<>(cid));
                });


        TreeMap<String, Long> treeMapCategory = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        clubConfig.financeDataList.stream().forEach(financeData -> financeData.getTransactionDataList()
                .stream()
                .forEach(transactionData -> {
                    if (treeMapCategory.containsKey(transactionData.getFinanceCategoryData().getCategory())) {
                        long old = treeMapCategory.get(transactionData.getFinanceCategoryData().getCategory());
                        treeMapCategory.put(transactionData.getFinanceCategoryData().getCategory(), old + transactionData.getBetrag());
                    } else {
                        treeMapCategory.put(transactionData.getFinanceCategoryData().getCategory(), transactionData.getBetrag());
                    }
                })
        );

        TreeItem treeItemCategory = createGroup("Kategorien", "", "", false, true);
        financeItem.getChildren().add(treeItemCategory);
        treeMapCategory.entrySet().stream()
                .forEach(ac -> {
                    ClubInfoData cid = new ClubInfoData(ac.getKey(), 0.01 * ac.getValue(), "");
                    treeItemCategory.getChildren().add(new TreeItem<>(cid));
                });


        return financeItem;
    }

    private static TreeItem<ClubInfoData> generateFinanceInfoActYear() {
        final int actYear = LocalDate.now().getYear();
        long countEntrys = clubConfig.financeDataList.stream()
                .filter(financeData -> financeData.getGeschaeftsJahr() == actYear).count();

        TreeItem<ClubInfoData> financeItem = createGroup("Finanzen - aktuelles Jahr " + actYear,
                countEntrys, "Buchungen",
                true, false);

        TreeItem<ClubInfoData> treeItemActYear = createGroup("Saldo", 0.01 * clubConfig.financeDataList.getSaldo(actYear),
                "Saldo im Jahr: " + actYear, false, false);
        financeItem.getChildren().add(treeItemActYear);


        TreeMap<String, Long> treeMapAccount = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        clubConfig.financeDataList.stream()
                .filter(financeData -> financeData.getGeschaeftsJahr() == actYear)
                .forEach(financeData -> {
                    if (treeMapAccount.containsKey(financeData.getFinanceAccountData().getName())) {
                        long old = treeMapAccount.get(financeData.getFinanceAccountData().getName());
                        treeMapAccount.put(financeData.getFinanceAccountData().getName(),
                                old + financeData.financeDataGetSumBetrag());
                    } else {
                        treeMapAccount.put(financeData.getFinanceAccountData().getName(),
                                financeData.financeDataGetSumBetrag());
                    }
                });

        TreeItem treeItemAccount = createGroup("Konten", "", "", false, true);
        financeItem.getChildren().add(treeItemAccount);
        treeMapAccount.entrySet().stream()
                .forEach(ac -> {
                    ClubInfoData cid = new ClubInfoData(ac.getKey(), 0.01 * ac.getValue(), "");
                    treeItemAccount.getChildren().add(new TreeItem<>(cid));
                });


        TreeMap<String, Long> treeMapCategory = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        clubConfig.financeDataList.stream()
                .filter(financeData -> financeData.getGeschaeftsJahr() == actYear)
                .forEach(financeData -> financeData.getTransactionDataList()
                        .stream()
                        .forEach(transactionData -> {
                            if (treeMapCategory.containsKey(transactionData.getFinanceCategoryData().getCategory())) {
                                long old = treeMapCategory.get(transactionData.getFinanceCategoryData().getCategory());
                                treeMapCategory.put(transactionData.getFinanceCategoryData().getCategory(), old + transactionData.getBetrag());
                            } else {
                                treeMapCategory.put(transactionData.getFinanceCategoryData().getCategory(), transactionData.getBetrag());
                            }
                        })
                );

        TreeItem treeItemCategory = createGroup("Kategorien", "", "", false, true);
        financeItem.getChildren().add(treeItemCategory);
        treeMapCategory.entrySet().stream()
                .forEach(ac -> {
                    ClubInfoData cid = new ClubInfoData(ac.getKey(), 0.01 * ac.getValue(), "");
                    treeItemCategory.getChildren().add(new TreeItem<>(cid));
                });


        return financeItem;
    }

    private static TreeItem<ClubInfoData> createGroup(String name) {
        ClubInfoData clubInfoData = new ClubInfoData(name);

        return new TreeItem<>(clubInfoData);
    }

    private static TreeItem<ClubInfoData> createGroup(String name, boolean group1, boolean group2) {
        ClubInfoData clubInfoData = new ClubInfoData(name);
        clubInfoData.setGroup1(group1);
        clubInfoData.setGroup2(group2);

        return new TreeItem<>(clubInfoData);
    }

    private static TreeItem<ClubInfoData> createGroup(String name, String amount, String text, boolean group1, boolean group2) {
        ClubInfoData clubInfoData = new ClubInfoData(name, amount, text);
        clubInfoData.setGroup1(group1);
        clubInfoData.setGroup2(group2);

        return new TreeItem<>(clubInfoData);
    }

    private static TreeItem<ClubInfoData> createGroup(String name, long amount, String text, boolean group1, boolean group2) {
        ClubInfoData clubInfoData = new ClubInfoData(name, amount, text);
        clubInfoData.setGroup1(group1);
        clubInfoData.setGroup2(group2);

        return new TreeItem<>(clubInfoData);
    }

    private static TreeItem<ClubInfoData> createGroup(String name, double amount, String text, boolean group1, boolean group2) {
        ClubInfoData clubInfoData = new ClubInfoData(name, amount, text);
        clubInfoData.setGroup1(group1);
        clubInfoData.setGroup2(group2);

        return new TreeItem<>(clubInfoData);
    }
}
