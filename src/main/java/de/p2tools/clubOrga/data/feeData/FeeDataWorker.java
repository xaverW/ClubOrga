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

import de.p2tools.p2Lib.tools.date.PLocalDate;

import java.util.function.Predicate;

public class FeeDataWorker extends FeeDataBase {


    public Predicate<FeeDataWorker> isBezahlt = fee -> !fee.getBezahlt().isEmpty();

    public void setBill(PLocalDate pDate) {
        setRechnung(pDate);
    }

    public void payFeeData(PLocalDate pDate) {
        setBezahlt(pDate);
    }

    public void setSQ(PLocalDate pDate) {
        setSpendenQ(pDate);
    }

    public boolean isFeePayed() {
        return getBezahlt().isEmpty();
    }

    public boolean isBankeinzug() {
        return (getPaymentTypeData().isBankeinzug());
    }

}
