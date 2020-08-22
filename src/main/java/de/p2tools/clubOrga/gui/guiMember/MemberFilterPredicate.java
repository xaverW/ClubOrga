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


package de.p2tools.clubOrga.gui.guiMember;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.clubOrga.data.feeData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.stateData.StateData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.function.Predicate;

public class MemberFilterPredicate {

    private final static BooleanProperty filtered = new SimpleBooleanProperty(false);

    public static boolean getFiltered() {
        return filtered.get();
    }

    public static Predicate<MemberData> getMemberPredicate(ClubConfig clubConfig, boolean onlySelected) {

        filtered.set(false);

        final String searchMemberName = clubConfig.MEMBER_FILTER_NACHNAME.getValueSafe().toLowerCase();
        final StateData stateData = clubConfig.MEMBER_FILTER_STATUS.get();
        final FeeRateData feeRateData = clubConfig.MEMBER_FILTER_BEITRAG.get();
        final PaymentTypeData paymentTypeData = clubConfig.MEMBER_FILTER_PAYMENT_TYPE.get();

        final boolean searchFeeCreated = clubConfig.MEMBER_FILTER_FEE_CREATED.get();
        final boolean searchFeeCreatedOff = clubConfig.MEMBER_FILTER_FEE_CREATED_OFF.get();
        final boolean searchFeePayed = clubConfig.MEMBER_FILTER_FEE_PAYED.get();
        final boolean searchFeePayedOff = clubConfig.MEMBER_FILTER_FEE_PAYED_OFF.get();

        Predicate<MemberData> predicate = memberData -> true;

        if (!searchMemberName.isEmpty()) {
            filtered.set(true);
            predicate = predicate.and(memberData -> memberData.getNachname().toLowerCase().startsWith(searchMemberName));
        }

        if (stateData != null) {
            filtered.set(true);
            predicate = predicate.and(memberData -> memberData.stateDataProperty().get().equals(stateData));
        }

        if (feeRateData != null) {
            filtered.set(true);
            predicate = predicate.and(memberData -> memberData.feeRateDataProperty().get().equals(feeRateData));
        }

        if (paymentTypeData != null) {
            filtered.set(true);
            predicate = predicate.and(memberData -> memberData.paymentTypeDataProperty().get().equals(paymentTypeData));
        }

        if (!searchFeeCreatedOff) {
            filtered.set(true);
            predicate = predicate.and(memberData -> (searchFeeCreated ? !memberData.feeIsMissing() : memberData.feeIsMissing()));
        }

        if (!searchFeePayedOff) {
            filtered.set(true);
            predicate = predicate.and(memberData -> (searchFeePayed ? !memberData.feesNotPayed() : memberData.feesNotPayed()));
        }

        if (onlySelected) {
            filtered.set(true);
            predicate = predicate.and(memberData -> memberData.isSelected());
        }

        return predicate;
    }
}
