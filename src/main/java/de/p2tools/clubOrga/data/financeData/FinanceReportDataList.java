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

import de.p2tools.clubOrga.config.club.ClubConfig;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.List;

public class FinanceReportDataList extends SimpleListProperty<FinanceReportData> {

    private FilteredList<FinanceReportData> filteredList = null;
    private SortedList<FinanceReportData> sortedList = null;

    final ClubConfig clubConfig;
    private final List<String> accounts = new ArrayList<>();
    private final List<String> categories = new ArrayList<>();


    public FinanceReportDataList(ClubConfig clubConfig) {
        super(FXCollections.observableArrayList());
        this.clubConfig = clubConfig;
    }

    public SortedList<FinanceReportData> getSortedList() {
        if (sortedList == null) {
            sortedList = new SortedList<>(getFilteredList());
        }
        return sortedList;
    }

    public FilteredList<FinanceReportData> getFilteredList() {
        if (filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
        }
        return filteredList;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String[] getHeaderArr() {
        ArrayList<String> headers = new ArrayList<>();
        headers.add(FinanceFieldNames.NR);
        headers.add(FinanceFieldNames.BELEG_NR);
        headers.add(FinanceFieldNames.GESAMTBETRAG);
        headers.add(FinanceFieldNames.GESCHAEFTSJAHR);
        headers.add(FinanceFieldNames.BUCHUNGS_DATUM);
        headers.add(FinanceFieldNames.ERSTELLDATUM);

        headers.addAll(getAccounts());
        headers.addAll(getCategories());

        String[] HEADERS = headers.toArray(new String[]{});
        for (int i = 0; i < headers.size(); ++i) {
            HEADERS[i] = HEADERS[i].replace(FinanceReportFactory.KONTO, "");
            HEADERS[i] = HEADERS[i].replace(FinanceReportFactory.KATEGORIE, "");
        }
        return HEADERS;
    }

    public void clearSelected() {
        this.stream().forEach(financeReportData -> financeReportData.setSelected(false));
    }
}
