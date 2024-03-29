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

package de.p2tools.cluborga.gui.table;


import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.data.financeData.FinanceCalculationData;
import de.p2tools.cluborga.data.financeData.FinanceCalculationFieldNames;
import de.p2tools.p2lib.guitools.PTableFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class ClubTableCalculation {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTableCalculation(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }

    public TableColumn[] initColumn(TableView table, boolean category) {
        ArrayList<TableColumn> tc = new ArrayList<>();
        table.getColumns().clear();


        final TableColumn<FinanceCalculationData, String> categoryColumn;
        if (category) {
            categoryColumn = new TableColumn<>(FinanceCalculationFieldNames.CATEGORY);
        } else {
            categoryColumn = new TableColumn<>(FinanceCalculationFieldNames.ACCOUNT);
        }
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));


        final TableColumn<FinanceCalculationData, Long> betragColumn = new TableColumn<>(FinanceCalculationFieldNames.BETRAG);
        betragColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        betragColumn.setCellFactory((final TableColumn<FinanceCalculationData, Long> param) -> new PTableFactory.PCellMoney<>());


        tc.add(categoryColumn);
        tc.add(betragColumn);

        return tc.toArray(new TableColumn[]{});
    }

}
