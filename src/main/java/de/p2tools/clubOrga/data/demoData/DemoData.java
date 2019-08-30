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


package de.p2tools.clubOrga.data.demoData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.controller.ProgStartFactory;
import de.p2tools.clubOrga.data.demoData.data.DemoClubDataFactory;
import de.p2tools.clubOrga.data.demoData.data.DemoFeeDataFactory;
import de.p2tools.clubOrga.data.demoData.data.DemoFinanceDataFactory;
import de.p2tools.clubOrga.data.demoData.data.DemoMemberDataFactory;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceDataBase;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DemoData {


    public void addDemoData(ClubConfig clubConfig) {

        if (new AddDemoDataDialogController(clubConfig).isOk()) {

            clubConfig.pMaskerPane.setMaskerVisible(true);
            clubConfig.pMaskerPane.setMaskerText("Demodaten erstellen");

            resetData(clubConfig);

            Thread th = new Thread(() -> {
                addNewData(true, clubConfig);
                Platform.runLater(() -> {
                    clubConfig.guiFinanceReport.isShown();
                    clubConfig.pMaskerPane.switchOffMasker();
                });
            });
            th.setName("DemoData");
            th.start();
        }
    }

    public void addDemoDataClubStart(ClubConfig clubConfig) {
        // Demodaten einfügen, angegebener Clubname bleibt erhalten
        String clubName = clubConfig.clubData.getName();
        addNewData(false, clubConfig);
        clubConfig.clubData.setName(clubName);
    }

    private void addNewData(boolean runLater, ClubConfig clubConfig) {

        // Mitglieder und Beiträge anlegen
        ArrayList<MemberData> memberDataList = DemoMemberDataFactory.getDemoMember(clubConfig, clubConfig.DEMO_DATA_ADD_MEMBER.get());

        ArrayList<FeeData> feeDataList = DemoFeeDataFactory.getDemoFeeData(clubConfig, memberDataList,
                DemoConst.getAddAmountFee(clubConfig.DEMO_DATA_ADD_FEE.get()));

        ArrayList<FinanceData> financeDataListFromFee = DemoFeeDataFactory.payDemoFee(clubConfig, feeDataList,
                DemoConst.getAddAmountFee(clubConfig.DEMO_DATA_ADD_FEE_PAY.get()));

        // Finanzen anlegen
        ArrayList<FinanceCategoryData> financeCategoryData = DemoFinanceDataFactory.getDemoFinanceCategory(clubConfig);
        ArrayList<FinanceData> financeDataList = DemoFinanceDataFactory.getDemoFinance(clubConfig, financeCategoryData, clubConfig.DEMO_DATA_ADD_FINANCE.get());


        // die Finanzlisten zusammenfassen und sortieren
        ArrayList<FinanceData> newFinances = new ArrayList<>();
        if (financeDataListFromFee != null && !financeDataListFromFee.isEmpty()) {
            newFinances.addAll(financeDataListFromFee);
        }
        if (financeDataList != null && !financeDataList.isEmpty()) {
            newFinances.addAll(financeDataList);
        }

        Collections.sort(newFinances, Comparator.comparing(FinanceDataBase::getBuchungsDatum));
        long no = clubConfig.financeDataList.getNextNr();
        for (FinanceData financeData : newFinances) {
            financeData.setNr(no);
            financeData.setBelegNr(no + "");
            ++no;
        }


        if (runLater) {
            Platform.runLater(() -> {
                addData(clubConfig, memberDataList, feeDataList,
                        financeCategoryData, newFinances);

                clubConfig.guiMember.resetTable();
                clubConfig.guiFee.resetTable();
                clubConfig.guiFinance.resetTable();

            });

        } else {
            addData(clubConfig, memberDataList, feeDataList,
                    financeCategoryData, newFinances);
        }
    }

    private void resetData(ClubConfig clubConfig) {
        if (clubConfig.DEMO_DATA_REMOVE_OLD_DATA.get()) {
            ProgStartFactory.resetClubData(clubConfig);
        }
    }

    private void addData(ClubConfig clubConfig,
                         List<MemberData> memberDataList,
                         List<FeeData> feeDataList,
                         List<FinanceCategoryData> financeCategoryData,
                         ArrayList<FinanceData> newFinances) {

        // ClubDaten
        DemoClubDataFactory.setDemoClubData(clubConfig);


        // Mitglieder eintragen
        if (memberDataList != null && !memberDataList.isEmpty()) {
            clubConfig.memberDataList.addAll(memberDataList);
        }


        // Beiträge eintragen
        if (feeDataList != null && !feeDataList.isEmpty()) {
            clubConfig.feeDataList.addAll(feeDataList);
        }


        // Finanzen eintragen
        if (financeCategoryData != null && !financeCategoryData.isEmpty()) {
            clubConfig.financeCategoryDataList.addAll(financeCategoryData);
        }

        if (!newFinances.isEmpty()) {
//            Collections.sort(newFinances, Comparator.comparing(FinanceDataBase::getBuchungsDatum));
            clubConfig.financeDataList.addAll(newFinances);
        }
    }
}
