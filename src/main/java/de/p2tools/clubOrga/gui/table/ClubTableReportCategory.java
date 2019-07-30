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

package de.p2tools.clubOrga.gui.table;


import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.financeData.FinanceReportData;
import de.p2tools.clubOrga.data.financeData.FinanceReportFieldNames;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class ClubTableReportCategory {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTableReportCategory(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }

    public TableColumn[] initColumn(TableView table, boolean category) {
        ArrayList<TableColumn> tc = new ArrayList<>();
        table.getColumns().clear();


        final TableColumn<FinanceReportData, String> categoryColumn;
        if (category) {
            categoryColumn = new TableColumn<>(FinanceReportFieldNames.CATEGORY);
        } else {
            categoryColumn = new TableColumn<>(FinanceReportFieldNames.ACCOUNT);
        }
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));


        final TableColumn<FinanceReportData, Long> betragColumn = new TableColumn<>(FinanceReportFieldNames.BETRAG);
        betragColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        betragColumn.setCellFactory((final TableColumn<FinanceReportData, Long> param) -> new PTableFactory.PCellMoney<>());


        tc.add(categoryColumn);
        tc.add(betragColumn);

        return tc.toArray(new TableColumn[]{});
    }

}
