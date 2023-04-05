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


package de.p2tools.cluborga.data.feeData;

import java.time.LocalDate;

public class FeeDataWorker extends FeeDataBase {

    public void setBill(LocalDate pDate) {
        setRechnung(pDate);
    }

    public void payFeeData(LocalDate pDate) {
        setBezahlt(pDate);
    }

    public void setSQ(LocalDate pDate) {
        setSpendenQ(pDate);
    }

    public boolean isFeePayed() {
        return !getBezahlt().isEqual(LocalDate.MIN);
    }

}
