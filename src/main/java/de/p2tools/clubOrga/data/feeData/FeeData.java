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


package de.p2tools.clubOrga.data.feeData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.feeData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.feeData.paymentType.PaymentTypeDataList;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberDataList;

public class FeeData extends FeeDataWorker {

    public FeeData(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
        addExtra();
    }

    /**
     * init nach Programmstart
     */
    public void initAfterLoad() {
        MemberDataList memberDataList = clubConfig.memberDataList;
        PaymentTypeDataList paymentTypeDataList = clubConfig.paymentTypeDataList;

        MemberData memberData = memberDataList.getById(getMemberId());
        setMemberData(memberData);
        setMitgliedNr(memberData == null ? "" : memberData.getNo() + "");
        setMemberName(memberData == null ? "" : memberData.getNachname() + " " + memberData.getVorname());

        PaymentTypeData paymentTypeData = paymentTypeDataList.getById(getZahlart());
        setPaymentTypeData(paymentTypeData);
    }
}
