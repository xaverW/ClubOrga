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


package de.p2tools.cluborga.controller.newsletter.document;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.data.feeData.FeeData;
import de.p2tools.cluborga.data.financeData.FinanceData;
import de.p2tools.cluborga.data.memberData.MemberData;

import java.util.List;

public abstract class CreateDocument {

    public void newsletterClub(ClubConfig clubConfig) {
    }

    public void newsletterMemberData(ClubConfig clubConfig, List<MemberData> memberDataList) {
    }

    public void newsletterFeeData(ClubConfig clubConfig, List<FeeData> memberDataList) {
    }

    public void newsletterFinanceData(ClubConfig clubConfig, List<FinanceData> memberDataList) {
    }
}