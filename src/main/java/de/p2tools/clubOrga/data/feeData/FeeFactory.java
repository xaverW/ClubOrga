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


package de.p2tools.clubOrga.data.feeData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.gui.dialog.dataDialog.DataDialogController;
import de.p2tools.clubOrga.gui.dialog.listDialog.AddMissingFeeDialogController;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.PIndex;
import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class FeeFactory {

    private FeeFactory() {
    }

    public static void generateMissingFeesForMembers(ClubConfig clubConfig, MemberData memberData) {
        ArrayList<MemberData> list = new ArrayList<>(1);
        list.add(memberData);
        generateMissingFeesForMembers(clubConfig, list);
    }

    public static void generateMissingFeesForMembers(ClubConfig clubConfig, List<MemberData> list) {
        if (list.isEmpty()) {
            return;
        }


        ArrayList<MemberData> newList = new ArrayList<>(list.size());
        list.stream().filter(memberData -> !memberData.memberIsFeeFree())
                .forEach(memberData -> newList.add(memberData));

        if (newList.isEmpty()) {
            PAlert.showInfoAlert(clubConfig.getStage(), "Beitrag", "Beitrag anlegen",
                    "Die ausgewählten Mitglieder sind alle Beitragsfrei.");
            return;
        }

        ObservableList<FeeData> feeDataList = FXCollections.observableList(new ArrayList<>());
        feeDataList.addAll(clubConfig.feeDataList.createMissingFeesForMember(newList));
        if (feeDataList.isEmpty()) {
            PAlert.showInfoAlert(clubConfig.getStage(), "Beitrag", "Beitrag anlegen",
                    "Es sind bereits alle Beiträge vorhanden.");
            return;
        }

        if (new AddMissingFeeDialogController(clubConfig, feeDataList).isOk() &&
                !feeDataList.isEmpty()) {

            clubConfig.feeDataList.addAll(feeDataList);
            PLog.sysLog("neue Beiträge anlegen: " + feeDataList.size());
        }
    }

    /**
     * ist zum Anlegen eines "leeren" Beitrags, nötiges INIT wird hier gemacht
     *
     * @param clubConfig
     * @return
     */
    public static FeeData getNewFeeWithNo(ClubConfig clubConfig) {
        FeeData feeData = new FeeData(clubConfig);
        feeData.setId(PIndex.getIndex());
        feeData.setNr(clubConfig.feeDataList.getNextNr());

        return feeData;
    }

    /**
     * Beiträge für die Liste Mitglieder erstellen
     *
     * @param clubConfig
     * @param list
     */
    public static void generateFeeForMembers(ClubConfig clubConfig, ArrayList<MemberData> list) {
        if (list.isEmpty()) {
            return;
        }

        // keine Mitglieder die Beitragsfrei sind
        Iterator<MemberData> it = list.listIterator();
        int countFeeFree = 0;
        while (it.hasNext()) {
            if (it.next().memberIsFeeFree()) {
                ++countFeeFree;
                it.remove();
            }
        }
        if (list.isEmpty()) {
            PAlert.showErrorAlert(clubConfig.getStage(), "Beitrag anlegen", "Alle Mitglieder sind " +
                    "beitragsfrei. Für diese kann kein Beitrag angelegt werden.");
            return;
        }

        if (countFeeFree == 1) {
            PAlert.showErrorAlert(clubConfig.getStage(), "Beitrag anlegen", "Es ist auch ein Mitglieder dabei, " +
                    "das Beitragsfrei ist. Für dieses wird kein Beitrag angelegt.");

        } else if (countFeeFree > 1) {
            PAlert.showErrorAlert(clubConfig.getStage(), "Beitrag anlegen", "Es sind auch " + countFeeFree + " Mitglieder dabei, " +
                    "die Beitragsfrei sind. Für diese wird kein Beitrag angelegt.");
        }

        if (list.size() == 1) {
            MemberData memberData = list.get(0);
            FeeData feeData = getNewFeeForMember(clubConfig, memberData);
            if (new DataDialogController(clubConfig, DataDialogController.OPEN.FEE_PANE,
                    memberData, feeData, null, null).isOk()) {
                clubConfig.feeDataList.add(feeData);
            }

        } else if (PAlert.BUTTON.YES == PAlert.showAlert_yes_no(clubConfig.getStage(),
                "Anlegen",
                "Beiträge anlegen?",
                "Sollen " + list.size() + " Beiträge angelegt werden?")) {

            clubConfig.feeDataList.addAktFeeForMember(clubConfig, list);
        }

    }

    /**
     * Beitrag für ein Mitglied erstellen
     *
     * @param clubConfig
     * @param optionalMemberData
     */
    public static void generateFeeForMember(ClubConfig clubConfig, Optional<MemberData> optionalMemberData) {
        if (!optionalMemberData.isPresent()) {
            return;
        }

        final MemberData memberData = optionalMemberData.get();
        if (memberData.memberIsFeeFree()) {
            PAlert.showErrorAlert(clubConfig.getStage(), "Beitrag anlegen", "Das Mitglied ist, " +
                    "Beitragsfrei. Für dieses wird kein Beitrag angelegt.");
        }

        FeeData feeData = getNewFeeForMember(clubConfig, memberData);
        if (new DataDialogController(clubConfig, DataDialogController.OPEN.FEE_PANE,
                memberData, feeData, null, null).isOk()) {
            clubConfig.feeDataList.add(feeData);
        }
    }

    /**
     * ist zum Anlegen eines neuen Mitgliedsbeitrags für das aktuelle Jahr
     *
     * @param clubConfig
     * @param memberData
     */
    public static FeeData getNewFeeForMember(ClubConfig clubConfig, MemberData memberData) {
        return getNewFeeForMember(clubConfig, memberData, PDateFactory.getAktYearInt());
    }

    /**
     * ist zum Anlegen eines neuen Mitgliedsbeitrags für das Jahr YEAR
     *
     * @param clubConfig
     * @param memberData
     * @param year
     */
    public static FeeData getNewFeeForMember(ClubConfig clubConfig, MemberData memberData, int year) {
        FeeData feeData = getNewFeeWithNo(clubConfig);

        feeData.setMemberData(memberData);
        feeData.setMemberId(memberData.getId());
        feeData.setMitgliedNr(memberData.getNr());
        feeData.setMitgliedName(memberData.getNachname());
        feeData.setBetrag(memberData.getBeitrag());
        feeData.setJahr(year);
        feeData.setPaymentTypeData(memberData.getPaymentTypeData());
        return feeData;
    }

    /**
     * liefert die Summe der Beiträge in der Liste list
     *
     * @param list
     * @return
     */
    public static double getSumFee(List<FeeData> list) {
        if (list == null) {
            return 0;
        } else {
            return list.stream().mapToDouble(fee -> fee.getBetrag()).sum();
        }
    }

    /**
     * copy feeData FROM to the feeData TO
     *
     * @param dataFrom
     * @param dataTo
     */
    public static void copyFeeData(FeeDataWorker dataFrom, FeeDataWorker dataTo) {
        if (dataFrom == null || dataTo == null) {
            return;
        }

        dataTo.setMemberData(dataFrom.getMemberData());
        dataTo.setPaymentTypeData(dataFrom.getPaymentTypeData());

        Config[] configs = dataFrom.getConfigsArr();
        Config[] configsCopy = dataTo.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }

    }
}
