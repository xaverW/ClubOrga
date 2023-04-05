/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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
import de.p2tools.cluborga.data.financeData.FinanceFieldNames;
import de.p2tools.cluborga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.cluborga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.cluborga.gui.FilterPane;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.pfiltercontrol.PFilterComboBoxObject;
import de.p2tools.p2lib.guitools.pfiltercontrol.PFilterTextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GuiFinanceReportFilterPane extends FilterPane {

    PFilterTextField filterTxtBelegNr = new PFilterTextField();
    private final PFilterComboBoxObject<Integer> filterCboGeschaeftsJahr = new PFilterComboBoxObject<>();
    private final PFilterComboBoxObject<FinanceAccountData> filterCboAccount = new PFilterComboBoxObject<>();
    private final PFilterComboBoxObject<FinanceCategoryData> filterCboCategory = new PFilterComboBoxObject<>();

    private final Label lblJahr = new Label(FinanceFieldNames.GESCHAEFTSJAHR_);
    private final Label lblKonto = new Label(FinanceFieldNames.KONTO_);
    private final Label lblKategorie = new Label(FinanceFieldNames.KATEGORIE_);

    private final ClubConfig clubConfig;

    public GuiFinanceReportFilterPane(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;

        initLayout();
        initFilter();
    }

    private void initLayout() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;
        gridPane.add(lblJahr, 0, row);
        gridPane.add(filterCboGeschaeftsJahr, 1, row);

        row = 0;
        gridPane.add(lblKonto, 3, row);
        gridPane.add(filterCboAccount, 4, row);
        gridPane.add(lblKategorie, 3, ++row);
        gridPane.add(filterCboCategory, 4, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcPrefMaxSize(250),
                PColumnConstraints.getCcMinSize(20),
                PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcPrefMaxSize(250));
        vbFilter.getChildren().addAll(gridPane);
    }

    private void initFilter() {
        filterTxtBelegNr.init(clubConfig.FINANCE_REPORT_FILTER_BELEG_NR);
        clubConfig.FINANCE_REPORT_FILTER_BELEG_NR.addListener((observable, oldValue, newValue) -> announceFilterChange());

        filterCboGeschaeftsJahr.init(clubConfig.financeDataList.getGeschaeftsJahrList(), clubConfig.FINANCE_REPORT_FILTER_GESCHAEFTS_JAHR);
        clubConfig.FINANCE_REPORT_FILTER_GESCHAEFTS_JAHR.addListener((observable, oldValue, newValue) -> {
            announceFilterChange();
        });

        filterCboAccount.init(clubConfig.financeAccountDataList, clubConfig.FINANCE_REPORT_FILTER_ACCOUNT);
        clubConfig.FINANCE_REPORT_FILTER_ACCOUNT.addListener((observable, oldValue, newValue) -> {
            announceFilterChange();
        });

        filterCboCategory.init(clubConfig.financeCategoryDataList, clubConfig.FINANCE_REPORT_FILTER_CATEGORY);
        clubConfig.FINANCE_REPORT_FILTER_CATEGORY.addListener((observable, oldValue, newValue) -> {
            announceFilterChange();
        });
    }

    private void announceFilterChange() {
        clubConfig.financesReportFilterChange.setValue(!clubConfig.financesReportFilterChange.getValue());
    }

    public void clearFilter() {
        clubConfig.financeReportDataList.clearSelected();

        filterTxtBelegNr.clearText();
        filterCboGeschaeftsJahr.clearSelection();
        filterCboAccount.clearSelection();
        filterCboCategory.clearSelection();

        announceFilterChange();
    }

}
