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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;

public class InfoFactoryPdf {
    private static ClubConfig clubConfig;
    private static final String PADDING = "\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0";
    private static final String UNDERLINE = "-----------------------------------------------------------------";

    private InfoFactoryPdf() {
    }

    public static void generateInfoList(ClubConfig cConfig, ArrayList<String> list) {
        clubConfig = cConfig;
        list.add(UNDERLINE);
        list.add(clubConfig.clubData.getName());
        list.add(clubConfig.clubData.getStrasse());
        list.add(clubConfig.clubData.getPlz() + " " + clubConfig.clubData.getOrt());
        list.add("Speicherpfad: " + clubConfig.getClubPath());
        list.add(UNDERLINE);
        list.add("");
        list.add("");

        generateMemberInfo(list);
        generateFeeInfo(list);
        generateFinanceInfo(list, false);
        generateFinanceInfo(list, true);
    }

    private static void generateMemberInfo(ArrayList<String> list) {
        String padding = "";
        list.add(UNDERLINE);
        list.add("Mitglieder");
        list.add(padding + "Anzahl Mitglieder: " + clubConfig.memberDataList.size());
        padding += PADDING;
        String tmp = padding;

        padding += PADDING;
        list.add(padding + "Stati");
        padding += PADDING;
        final String padStati = padding;
        clubConfig.stateDataList.stream().forEach(stateData -> {
            list.add(padStati + "Mitglieder mit Status " + stateData.getName() + ": " +
                    clubConfig.memberDataList.stream()
                            .filter(memberData -> memberData.getStateData().equals(stateData)).count());
        });

        padding = tmp;
        padding += PADDING;
        list.add(padding + "Beitragssätze");
        padding += PADDING;
        final String padBeitrag = padding;
        clubConfig.feeRateDataList.stream().forEach(feeRateData -> {
            list.add(padBeitrag + "Mitglieder mit Beitragssatz " + feeRateData.getName() + ": " +
                    clubConfig.memberDataList.stream()
                            .filter(memberData -> memberData.getFeeRateData().equals(feeRateData)).count());
        });

        padding = tmp;
        padding += PADDING;
        list.add(padding + "Zahlarten");
        padding += PADDING;
        final String padZahlarten = padding;
        clubConfig.paymentTypeDataList.stream().forEach(paymentTypeData -> {
            list.add(padZahlarten + "Mitglieder mit Zahlart " + paymentTypeData.getName() + ": " +
                    clubConfig.memberDataList.stream()
                            .filter(memberData -> memberData.getPaymentTypeData().equals(paymentTypeData)).count());
        });
    }

    private static void generateFeeInfo(ArrayList<String> list) {
        String padding = "";
        list.add(UNDERLINE);
        list.add("Beiträge");
        list.add(padding + "Anzahl Beiträge: " + clubConfig.feeDataList.size());
        padding += PADDING;
        String tmp = padding;

        list.add(padding + "Rechnung");
        padding += PADDING;
        list.add(padding + "Rechnung wurde erstellt: " +
                clubConfig.feeDataList.stream()
                        .filter(data -> !data.getRechnung().isEqual(LocalDate.MIN)).count());
        list.add(padding + "Rechnung wurde nicht erstellt: " +
                clubConfig.feeDataList.stream()
                        .filter(data -> data.getRechnung().isEqual(LocalDate.MIN)).count());

        padding = tmp;
        list.add(padding + "Bezahlt");
        padding += PADDING;
        list.add(padding + "Beitrag ist bezahlt: " +
                clubConfig.feeDataList.stream()
                        .filter(data -> !data.getBezahlt().isEqual(LocalDate.MIN)).count());
        list.add(padding + "Beitrag ist nicht bezahlt: " +
                (int) clubConfig.feeDataList.stream()
                        .filter(data -> data.getBezahlt().isEqual(LocalDate.MIN)).count());

        padding = tmp;
        list.add("Spendenquittung");
        padding += PADDING;
        list.add(padding + "Spendenquittung wurde erstellt: " +
                clubConfig.feeDataList.stream()
                        .filter(data -> !data.getSpendenQ().isEqual(LocalDate.MIN)).count());
        list.add(padding + "Spendenquittung wurde nicht erstellt: " +
                clubConfig.feeDataList.stream()
                        .filter(data -> data.getSpendenQ().isEqual(LocalDate.MIN)).count());
    }


    private static void generateFinanceInfo(ArrayList<String> list, boolean isActYear) {
        final int actYear = LocalDate.now().getYear();
        long countEntrys = clubConfig.financeDataList.stream()
                .filter(financeData -> financeData.getGeschaeftsJahr() == actYear).count();

        String padding = "";
        list.add(UNDERLINE);
        list.add(isActYear ? "Finanzen - aktuelles Jahr " + actYear : "Finanzen");
        //----------------------------------
        list.add(padding + (isActYear ? "Saldo im Jahr " + actYear + ": " + 0.01 * clubConfig.financeDataList.getSaldo(actYear) :
                "Saldo über den gesamten Zeitraum: " + 0.01 * clubConfig.financeDataList.getSaldo()));

        TreeMap<String, Long> treeMapAccount = isActYear ? generateFinanceInfoActYearAccount() : generateFinanceInfoAccount();
        padding += PADDING;
        final String tmpPadding = padding + PADDING;
        list.add(padding + "Konten");
        treeMapAccount.entrySet().stream().forEach(tree -> {
            list.add(tmpPadding + tree.getKey() + ": " + 0.01 * tree.getValue());
        });

        TreeMap<String, Long> treeMapCategory = isActYear ? generateFinanceInfoCategory() : generateFinanceInfoCategory();
        list.add(padding + "Kategorien");
        treeMapCategory.entrySet().stream().forEach(tree -> {
            list.add(tmpPadding + tree.getKey() + ": " + 0.01 * tree.getValue());
        });
    }

    private static TreeMap<String, Long> generateFinanceInfoAccount() {
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

        return treeMapAccount;
    }

    private static TreeMap<String, Long> generateFinanceInfoCategory() {
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
        return treeMapCategory;
    }

    private static TreeMap<String, Long> generateFinanceInfoActYearCategory() {
        final int actYear = LocalDate.now().getYear();
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

        return treeMapCategory;
    }

    private static TreeMap<String, Long> generateFinanceInfoActYearAccount() {
        final int actYear = LocalDate.now().getYear();
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

        return treeMapAccount;
    }
}
