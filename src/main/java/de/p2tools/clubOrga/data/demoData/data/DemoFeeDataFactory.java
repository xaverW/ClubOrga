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
import de.p2tools.clubOrga.data.demoData.DemoConst;
import de.p2tools.clubOrga.data.demoData.DemoDataFactory;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFactory;
import de.p2tools.clubOrga.data.financeData.TransactionData;
import de.p2tools.clubOrga.data.financeData.TransactionDataList;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountFactory;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryFactory;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.p2Lib.tools.date.PLocalDate;

import java.time.LocalDateTime;
import java.util.*;

public class DemoFeeDataFactory {

    private DemoFeeDataFactory() {

    }

    public static ArrayList<FeeData> getDemoFeeData(ClubConfig clubConfig,
                                                    List<MemberData> memberDataList,
                                                    DemoConst.ADD_AMOUNT_FEE addAmountFee) {

        if (!clubConfig.DEMO_DATA_MEMBER.get()) {
            // keine Mitglieder, keine Beiträge
            return null;
        }

        if (memberDataList == null || memberDataList.isEmpty()) {
            // keine Mitglieder, keine Beiträge
            return null;
        }

        if (addAmountFee == DemoConst.ADD_AMOUNT_FEE.FEE_ADD_NONE) {
            // dann sollen keine Beiträge angelegt werden
            return null;
        }


        Random random = new Random();
        ArrayList<FeeData> newFeeDataList = new ArrayList<>();

        // keine Mitglieder die Beitragsfrei sind
        memberDataList.stream()
                .filter(memberData -> !memberData.memberIsFeeFree())
                .forEach(memberData -> {

                    switch (addAmountFee) {
                        case FEE_ADD_SOME:
                            if (random.nextBoolean()) {
                                newFeeDataList.addAll(clubConfig.feeDataList.createMissingFeesForMember(memberData));
                            }
                            break;
                        case FEE_ADD_ALL:
                        default:
                            newFeeDataList.addAll(clubConfig.feeDataList.createMissingFeesForMember(memberData));
                            break;
                    }

                });


        setFeeNr(clubConfig, newFeeDataList);
        return newFeeDataList;
    }

    private static void setFeeNr(ClubConfig clubConfig, List<FeeData> feeDateList) {
        // Datum setzen
        for (FeeData feeData : feeDateList) {

            int year = feeData.getJahr();
            LocalDateTime dateTime = DemoDataFactory.getRandomDate(year);

            feeData.getErstellDatum().setPDate(dateTime.toLocalDate());
        }

        // nach Datum sortieren
        Collections.sort(feeDateList, Comparator.comparing(FeeData::getErstellDatum));

        // und noch die Nummer setzen
        long no = clubConfig.feeDataList.getNextNr();
        for (FeeData feeData : feeDateList) {
            feeData.setNr(no++);
        }
    }

    public static ArrayList<FinanceData> payDemoFee(ClubConfig clubConfig, List<FeeData> feeDataList, DemoConst.ADD_AMOUNT_FEE payFee) {

        if (payFee == DemoConst.ADD_AMOUNT_FEE.FEE_ADD_NONE) {
            // dann sollen auch keine Beiträge bezahlt werden
            return null;
        }

        if (feeDataList == null || feeDataList.isEmpty()) {
            // keine Beiträge, keine Finanzen
            return null;
        }


        Random random = new Random();
        // Beiträge ohne Bankeinzug bezahlen
        ArrayList<FinanceData> newFinanceDataList = new ArrayList<>();
        feeDataList.stream()

                .filter(feeData -> !feeData.getMemberData().getPaymentTypeData().isBankeinzug())
                .forEach(feeData -> {

                    switch (payFee) {
                        case FEE_ADD_SOME:
                            if (random.nextBoolean()) {
                                addFinance(clubConfig, feeData, newFinanceDataList);
                            }
                            break;
                        case FEE_ADD_ALL:
                        default:
                            addFinance(clubConfig, feeData, newFinanceDataList);
                            break;
                    }

                });

        // Beiträge mit Bankeizug bezahlen
        ArrayList<FeeData> feeDataListEinzug = new ArrayList<>();
        feeDataList.stream()
                .filter(feeData -> feeData.getMemberData().getPaymentTypeData().isBankeinzug())
                .forEach(feeData -> {
                    switch (payFee) {
                        case FEE_ADD_SOME:
                            if (random.nextBoolean()) {
                                feeDataListEinzug.add(feeData);
                            }
                            break;
                        case FEE_ADD_ALL:
                        default:
                            feeDataListEinzug.add(feeData);
                            break;
                    }

                });

        addTransaction(clubConfig, feeDataListEinzug, newFinanceDataList);

        return newFinanceDataList;
    }

    private static void addFinance(ClubConfig clubConfig, FeeData feeData, ArrayList<FinanceData> newFinanceDataList) {
        newFinanceDataList.add(getFinance(clubConfig, feeData));
    }

    private static FinanceData financeData = null;
    private static int year = 0;
    private static boolean foundYear = true;

    private static void addTransaction(ClubConfig clubConfig,
                                       ArrayList<FeeData> feeDataList,
                                       ArrayList<FinanceData> financeDataList) {

        FinanceCategoryData financeCategoryData =
                clubConfig.financeCategoryDataList.getCategoryDataStandard(FinanceCategoryFactory.CATEGORY_TYPE.CATEGORY_BEITRAG);

        FinanceAccountData financeAccountData =
                clubConfig.financeAccountDataList.getAccountDataStandard(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_GIRO);

        HashSet<Integer> yearHash = new HashSet<>(DemoDataFactory.maxYear);

        while (foundYear) {
            foundYear = false;
            year = 0;

            feeDataList.stream().forEach(feeData -> {
                int y = feeData.getJahr();

                if (!foundYear && yearHash.add(y)) {
                    foundYear = true;
                    financeData = getFinance(clubConfig, feeData);
                    financeDataList.add(financeData);
                    year = y;
                } else if (year == y) {
                    PLocalDate pLocalDate = financeData.getBuchungsDatum();
                    feeData.getErstellDatum().setPDate(pLocalDate.getLocalDate());
                    feeData.setBezahlt(pLocalDate);
                    feeData.setRechnung(pLocalDate);

                    final TransactionDataList trList = financeData.getTransactionDataList();
                    TransactionData transactionData = new TransactionData(trList.getNextNr(), clubConfig);
//                    transactionData.setBelegNr(transactionData.getNr() + "");
                    trList.add(transactionData);

                    transactionData.setFinanceAccountData(financeAccountData);
                    transactionData.setFinanceCategoryData(financeCategoryData);
                    transactionData.setBetrag(feeData.getBetrag());
                    transactionData.setFeeData(feeData);
                }


            });

        }
    }

    private static FinanceData getFinance(ClubConfig clubConfig, FeeData feeData) {

        FinanceCategoryData financeCategoryData =
                clubConfig.financeCategoryDataList.getCategoryDataStandard(FinanceCategoryFactory.CATEGORY_TYPE.CATEGORY_BEITRAG);

        PLocalDate pLocalDate = feeData.getErstellDatum();
        feeData.setBezahlt(pLocalDate);
        feeData.setRechnung(pLocalDate);

        FinanceData financeData = FinanceFactory.getNewFinanceDataForFeeData(clubConfig, feeData, pLocalDate,
                feeData.getJahr(), financeCategoryData);

        financeData.getBuchungsDatum().setPDate(pLocalDate.getLocalDate());
        financeData.getErstellDatum().setPDate(pLocalDate.getLocalDate());

        return financeData;
    }
}
