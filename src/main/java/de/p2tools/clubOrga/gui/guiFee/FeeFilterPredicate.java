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


package de.p2tools.clubOrga.gui.guiFee;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.feeData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.gui.tools.PPredicate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDate;
import java.util.function.Predicate;

public class FeeFilterPredicate {

    private final static BooleanProperty filtered = new SimpleBooleanProperty(false);

    public static boolean getFiltered() {
        return filtered.get();
    }

    public static Predicate<FeeData> getFeePredicate(ClubConfig clubConfig, boolean onlySelected) {

        filtered.set(false);

        final MemberData memberData = clubConfig.FEE_FILTER_MITGLIED.get();
        final Integer searchJahr = clubConfig.FEE_FILTER_JAHR.get();
        final PaymentTypeData paymentTypeData = clubConfig.FEE_FILTER_PAYMENT_TYPE.get();


        final boolean searchRechnung = clubConfig.FEE_FILTER_RECHNUNG.get();
        final boolean searchRechnungOff = clubConfig.FEE_FILTER_RECHNUNG_OFF.get();

        final boolean searchBezahlt = clubConfig.FEE_FILTER_BEZAHLT.get();
        final boolean searchBezahltOff = clubConfig.FEE_FILTER_BEZAHLT_OFF.get();

        final boolean searchSq = clubConfig.FEE_FILTER_SQ.get();
        final boolean searchSqOff = clubConfig.FEE_FILTER_SQ_OFF.get();

        final boolean searchDelMember = clubConfig.FEE_FILTER_DEL_MEMBER.get();
        final boolean searchDelMemberOff = clubConfig.getFEE_FILTER_DEL_MEMBER_OFF.get();

        Predicate<FeeData> predicate = feeData -> true;

        if (onlySelected) {
            filtered.set(true);
            predicate = predicate.and(feeData -> feeData.isSelected());
        }

        if (searchDelMemberOff && memberData != null) {
            filtered.set(true);
            predicate = predicate.and(feeData -> PPredicate.compareObject(feeData.getMemberData(), memberData));
        }

        if (paymentTypeData != null) {
            filtered.set(true);
            predicate = predicate.and(feeData -> PPredicate.compareObject(feeData.getPaymentTypeData(), paymentTypeData));
        }

        if (!searchDelMemberOff) {
            filtered.set(true);
            predicate = predicate.and(feeData -> (searchDelMember ? feeData.getMemberData() == null : feeData.getMemberData() != null));
        }

        if (searchJahr != null && searchJahr > 0) {
            filtered.set(true);
            predicate = predicate.and(feeData -> feeData.getJahr() == searchJahr);
        }

        if (!searchBezahltOff) {
            filtered.set(true);
            predicate = predicate.and(feeData -> (searchBezahlt ? !feeData.getBezahlt().isEqual(LocalDate.MIN) : feeData.getBezahlt().isEqual(LocalDate.MIN)));
        }

        if (!searchRechnungOff) {
            filtered.set(true);
            predicate = predicate.and(feeData -> (searchRechnung ? !feeData.getRechnung().isEqual(LocalDate.MIN) : feeData.getRechnung().isEqual(LocalDate.MIN)));
        }

        if (!searchSqOff) {
            filtered.set(true);
            predicate = predicate.and(feeData -> (searchSq ? !feeData.getSpendenQ().isEqual(LocalDate.MIN) : feeData.getSpendenQ().isEqual(LocalDate.MIN)));
        }

        return predicate;
    }


}
