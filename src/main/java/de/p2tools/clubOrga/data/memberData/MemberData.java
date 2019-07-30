/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateFactory;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeFactory;
import de.p2tools.clubOrga.data.memberData.stateData.StateData;
import de.p2tools.clubOrga.data.memberData.stateData.StateDataFactory;

import java.util.Optional;

public class MemberData extends MemberDataBase {


    public MemberData(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
        addExtra();
    }

    public boolean memberIsFeeFree() {
        return getBeitragssatz() == FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getId();
    }


    public boolean feeIsMissing() {
        if (memberIsFeeFree()) {
            return false;
        }

        return clubConfig.feeDataList.countMissingFeesForMember(this) > 0;
    }

    public boolean feesNotPayed() {
        if (memberIsFeeFree()) {
            return false;
        }

        return clubConfig.feeDataList.countFeesNotPayedForMember(this) > 0;
    }

    @Override
    public String toString() {
        return "[" + this.getNr() + "]  " + this.getNachname() +
                (this.getVorname().isEmpty() ? "" : (", " + this.getVorname()))
                ;
    }

    public void initAfterLoad() {
        // Mitgliederstatus
        Optional<StateData> stateData = clubConfig.stateDataList.stream()
                .filter(data -> getStatus() == data.getId()).findAny();

        if (stateData.isPresent()) {
            setStateData(stateData.get());
        } else {
            setStateData(clubConfig.stateDataList.getStateDataStandard(StateDataFactory.STATE_TYPE.STATE_ACTIVE));
        }

        // Beitrag
        Optional<FeeRateData> feeRateData = clubConfig.feeRateDataList.stream()
                .filter(data -> getBeitragssatz() == data.getId()).findAny();

        if (feeRateData.isPresent()) {
            setFeeRateData(feeRateData.get());
        } else {
            setFeeRateData(clubConfig.feeRateDataList.getRateDataStandard(FeeRateFactory.RATE_TYPE.RATE_STANDARD));
        }

        // Zahlart
        Optional<PaymentTypeData> paymentTypeData = clubConfig.paymentTypeDataList.stream()
                .filter(data -> getZahlart() == data.getId()).findAny();

        if (paymentTypeData.isPresent()) {
            setPaymentTypeData(paymentTypeData.get());
        } else {
            setPaymentTypeData(clubConfig.paymentTypeDataList.getStateDataStandard(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_UEBERWEISUNG));
        }
    }

    @Override
    public int compareTo(MemberData o) {
        return o.getNachname().compareTo(getNachname());
    }
}
