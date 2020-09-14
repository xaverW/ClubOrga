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


package de.p2tools.clubOrga.data.memberData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateFactory;
import de.p2tools.clubOrga.data.feeData.paymentType.PaymentTypeFactory;
import de.p2tools.clubOrga.data.memberData.stateData.StateDataFactory;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.PIndex;

public class MemberFactory {
    private MemberFactory() {
    }

    /**
     * ist zum Anlegen eines neuen Mitglieds
     * nötiges INIT wird hier gemacht
     *
     * @param clubConfig
     * @param nachName
     * @return
     */
    public static MemberData getNewMemberData(ClubConfig clubConfig, String nachName, boolean addNo) {
        MemberData memberData = new MemberData(clubConfig);

        memberData.setId(PIndex.getIndex());
        memberData.setNachname(nachName);
        if (addNo) {
            memberData.setNo(clubConfig.memberDataList.getNextNr());
        }

        memberData.setFeeRateData(clubConfig.feeRateDataList.getRateDataStandard(FeeRateFactory.RATE_TYPE.RATE_STANDARD));
        memberData.setBeitrag(memberData.getFeeRateData().getBetrag());

        memberData.setStateData(clubConfig.stateDataList.getStateDataStandard(StateDataFactory.STATE_TYPE.STATE_ACTIVE));
        memberData.setStatus(memberData.getStateData().getId());

        memberData.setPaymentTypeData(clubConfig.paymentTypeDataList.getStateDataStandard(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_UEBERWEISUNG));
        memberData.setPaymentType(memberData.getPaymentTypeData().getId());

        return memberData;
    }

    /**
     * copy memberData FROM to the memeberData TO
     *
     * @param memberDataFrom
     * @param memberDataTo
     */
    public static MemberData copyMemberData(ClubConfig clubConfig,
                                            MemberData memberDataFrom, MemberData memberDataTo) {
        if (memberDataFrom == null) {
            return null;
        }
        if (memberDataTo == null) {
            memberDataTo = MemberFactory.getNewMemberData(clubConfig, "", false);
        }

        Config[] configs = memberDataFrom.getConfigsArr();
        Config[] configsCopy = memberDataTo.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }

        memberDataTo.setFeeRateData(memberDataFrom.getFeeRateData());
        memberDataTo.setStateData(memberDataFrom.getStateData());
        memberDataTo.setPaymentTypeData(memberDataFrom.getPaymentTypeData());

        return memberDataTo;
    }

    public static String getShortIban(MemberData memberData) {
        if (memberData == null) {
            return "";
        }

        return getShortIban(memberData.getIban());
    }

    public static String getShortIban(String iban) {
        if (iban == null || iban.isEmpty()) {
            return "";
        }

        String ret;
        if (iban.length() > 10) {
            ret = iban.substring(0, 5)
                    + "****"
                    + iban.substring(iban.length() - 3);
        } else if (iban.length() > 5) {
            ret = iban.substring(0, 3)
                    + "****";
        } else {
            ret = "***";
        }

        return ret;
    }
}
