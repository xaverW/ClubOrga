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

import java.text.NumberFormat;
import java.util.ArrayList;

public class FinanceReportData extends FinanceReportDataBase {
    static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    public FinanceReportData() {
        super();
    }

    public ArrayList<String> getDataRow(FinanceReportDataList financeReportDataList) {
        ArrayList<String> dataRow = new ArrayList<>();
        dataRow.add(getNr() + "");
        dataRow.add(getBelegNr());
        dataRow.add(currencyFormat.format(getGesamtbetrag() / 100));
        dataRow.add(getGeschaeftsJahr() + "");
        dataRow.add(getBuchungsDatum().toString());

        for (int i = 0; i < financeReportDataList.getAccounts().size(); i++) {
            final long l = getAccountList().get(i).getBetrag();
            if (l == 0) {
                dataRow.add("");
            } else {
                dataRow.add(currencyFormat.format(l / 100));
            }
        }

        for (int i = 0; i < financeReportDataList.getCategories().size(); i++) {
            final long l = getCategoryList().get(i).getBetrag();
            if (l == 0) {
                dataRow.add("");
            } else {
                dataRow.add(currencyFormat.format(l / 100));
            }
        }

        return dataRow;
    }


}
