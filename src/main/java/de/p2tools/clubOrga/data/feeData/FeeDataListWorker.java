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
import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.log.PLog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeeDataListWorker extends FeeDataListBase {

    public FeeDataListWorker(ClubConfig clubConfig) {
        super(clubConfig);
    }

    /**
     * init Liste nach Programmstart
     *
     * @param clubConfig
     */
    public synchronized void initListAfterClubLoad(ClubConfig clubConfig) {
        this.stream().forEach(feeData -> feeData.initAfterLoad());
        initAfterAdding();
    }

    public synchronized int countMissingFeesForMember(MemberData memberData) {
        // zählt nicht angelegte (fehlende) Beiträge für das Mitglied
        int ret = 0;

        List<FeeData> list = getFeesForMember(memberData);
        String startFee = memberData.getZahlungsbeginn().getDateTime(PLocalDate.FORMAT_yyyy);
        List<String> years = PDateFactory.getYearListSince(startFee);

        for (String year : years) {
            boolean found = list.stream().filter(feeData -> (feeData.getJahr() + "").equals(year)).findAny().isPresent();
            if (!found) {
                ++ret;
            }
        }

        return ret;
    }

    public synchronized List<FeeData> createMissingFeesForMember(MemberData memberData) {
        // liefert nicht angelegte (fehlende) Beiträge für das Mitglied

        ArrayList<MemberData> list = new ArrayList<>(1);
        list.add(memberData);
        return createMissingFeesForMember(list);
    }

    public synchronized List<FeeData> createMissingFeesForMember(List<MemberData> memberDataList) {
        // liefert nicht angelegte (fehlende) Beiträge für die Mitgliederliste

        List<FeeData> missingFees = new ArrayList<>(2 * memberDataList.size());
        List<FeeData> feeList = new ArrayList<>();

        boolean foundFeeFree = false;
        for (MemberData memberData : memberDataList) {

            feeList.clear();
            if (memberData.memberIsFeeFree()) {
                foundFeeFree = true;
                continue;
            }

            getFeesForMember(feeList, memberData);

            try {
                final String startFee = memberData.getZahlungsbeginn().getYear();
                final int start = Integer.parseInt(startFee);
                List<Integer> years = PDateFactory.getYearListSince(start);

                for (Integer year : years) {
                    FeeData found = feeList.stream()
                            .filter(feeData -> (feeData.getJahr() == year))
                            .findAny().orElse(null);

                    if (found == null) {
                        FeeData feeData = FeeFactory.getNewFeeForMember(clubConfig, memberData, year);
                        missingFees.add(feeData);
                    }
                }

            } catch (Exception ex) {
                PLog.errorLog(987541254, "fehlender Zahlungsbeginn");
            }

        }

        long no = clubConfig.feeDataList.getNextNr();
        for (FeeData feeData : missingFees) {
            feeData.setNo(no++);
        }

        return missingFees;
    }

    public synchronized int countFeesNotPayedForMember(MemberData memberData) {
        // zählt angelegte nicht bezahlte Beiträge für das Mitglied
        List<FeeData> list = new ArrayList<>();
        getFeesForMember(list, memberData);
        return (int) list.stream().filter(f -> !f.isFeePayed()).count();
    }

    public synchronized List<FeeData> getFeesForMember(MemberData memberData) {
        List<FeeData> feeDataList = new ArrayList<>();
        getFeesForMember(feeDataList, memberData);
        return feeDataList;
    }

    public synchronized void getFeesForMember(List<FeeData> feeDataList, MemberData memberData) {
        feeDataList.addAll(this.stream()
//                .filter(feeData -> !memberData.memberIsFeeFree())
                .filter(feeData -> feeData.getMemberData() != null)
                .filter(feeData -> feeData.getMemberData().equals(memberData))
                .collect(Collectors.toList()));
    }

}
