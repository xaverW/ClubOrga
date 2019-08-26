/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.clubOrga.data.financeData;

import de.p2tools.clubOrga.config.prog.ProgConst;

public class FinanceReportCategoryData {

    //    private final LongProperty id = new SimpleLongProperty(0);
    private long financeAccountId = ProgConst.FILTER_ID_NOT_SELECTED;
    private long betrag = 0;

    public FinanceReportCategoryData() {
    }

    public FinanceReportCategoryData(long id, long betrag) {
        this.financeAccountId = financeAccountId;
        setBetrag(betrag);
    }

    public long getFinanceAccountId() {
        return financeAccountId;
    }

//    public ObjectProperty<FinanceAccountData> financeAccountIdProperty() {
//        return financeAccountId;
//    }

//    public void setFinanceAccountId(FinanceAccountData financeAccountId) {
//        this.financeAccountId.set(financeAccountId);
//    }

//    public long getId() {
//        return id.get();
//    }
//
//    public LongProperty idProperty() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id.set(id);
//    }

    public long getBetrag() {
        return betrag;
    }

//    public LongProperty betragProperty() {
//        return betrag;
//    }

    public void setBetrag(long betrag) {
        this.betrag = betrag;
    }
}
