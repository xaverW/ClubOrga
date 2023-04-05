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


package de.p2tools.cluborga.gui.guiFinanceReport;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.data.financeData.FinanceReportData;
import de.p2tools.cluborga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.cluborga.data.financeData.categoryData.FinanceCategoryData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.function.Predicate;

public class FinanceReportFilterPredicate {
    private final static BooleanProperty filtered = new SimpleBooleanProperty(false);

    public static boolean getFiltered() {
        return filtered.get();
    }

    public static Predicate<FinanceReportData> getProperty(ClubConfig clubConfig, boolean onlySelected) {

        filtered.set(false);

        final Integer searchGeschaeftsJahr = clubConfig.FINANCE_REPORT_FILTER_GESCHAEFTS_JAHR.get();
        final FinanceAccountData searchAccountData = clubConfig.FINANCE_REPORT_FILTER_ACCOUNT.get();
        final FinanceCategoryData searchCategoryData = clubConfig.FINANCE_REPORT_FILTER_CATEGORY.get();

        Predicate<FinanceReportData> predicate = financeData -> true;

        if (onlySelected) {
            filtered.set(true);
            predicate = predicate.and(financeReportData -> financeReportData.isSelected());
        }

        if (searchGeschaeftsJahr != null && searchGeschaeftsJahr > 0) {
            filtered.set(true);
            predicate = predicate.and(financeReportData -> financeReportData.getGeschaeftsJahr() == searchGeschaeftsJahr);
        }

        if (searchAccountData != null) {
            filtered.set(true);
            predicate = predicate.and(financeReportData -> financeReportData.getAccountList()
                    .stream()
                    .anyMatch(financeReportAccountData ->
                            financeReportAccountData.getFinanceAccountId() == searchAccountData.getId())
            );
        }

        if (searchCategoryData != null) {
            filtered.set(true);
            predicate = predicate.and(financeReportData -> financeReportData.getCategoryList()
                    .stream()
                    .anyMatch(financeReportCategoryData ->
                            financeReportCategoryData.getFinanceCategoryId() == searchCategoryData.getId())
            );
        }

        return predicate;
    }
}
