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


package de.p2tools.clubOrga.gui.guiFinance;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.function.Predicate;

public class FinanceFilterPredicate {
    private final static BooleanProperty filtered = new SimpleBooleanProperty(false);

    public static boolean getFiltered() {
        return filtered.get();
    }

    public static Predicate<FinanceData> getFinancePredicate(ClubConfig clubConfig, boolean onlySelected) {

        filtered.set(false);

        final String searchFinanceBelegNr = clubConfig.FINANCE_FILTER_BELEG_NR.getValueSafe().toLowerCase();
        final Integer searchGeschaeftsJahr = clubConfig.FINANCE_FILTER_GESCHAEFTS_JAHR.get();
        final FinanceAccountData accountData = clubConfig.FINANCE_FILTER_ACCOUNT.get();
        final FinanceCategoryData categoryData = clubConfig.FINANCE_FILTER_CATEGORY.get();

        Predicate<FinanceData> predicate = new Predicate<FinanceData>() {
            @Override
            public boolean test(FinanceData financeData) {
                return true;
            }
        };

        if (onlySelected) {
            filtered.set(true);
            predicate = predicate.and(financeData -> financeData.isSelected());
        }

        if (!searchFinanceBelegNr.isEmpty()) {
            filtered.set(true);
            predicate = predicate.and(financeData -> financeData.getBelegNr().toLowerCase().startsWith(searchFinanceBelegNr));
        }

        if (searchGeschaeftsJahr != null && searchGeschaeftsJahr > 0) {
            filtered.set(true);
            predicate = predicate.and(financeData -> financeData.getGeschaeftsJahr() == searchGeschaeftsJahr);
        }

        if (accountData != null) {
            filtered.set(true);
            predicate = predicate.and(financeData -> financeData.getFinanceAccountData().equals(accountData));
        }

        if (categoryData != null) {
            filtered.set(true);
            predicate = predicate.and(financeData -> financeData.getTransactionDataList()
                    .stream()
                    .anyMatch(transactionData -> transactionData.getFinanceCategoryData().equals(categoryData)));
        }

        return predicate;
    }
}
