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
import de.p2tools.cluborga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.cluborga.data.financeData.accountData.FinanceAccountFieldNames;
import de.p2tools.p2lib.guitools.ptable.CellCheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class ClubTableFinanceAccount {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTableFinanceAccount(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }

    public TableColumn[] initColumn(TableView table) {
        ArrayList<TableColumn> tc = new ArrayList<>();

        table.getColumns().clear();

        final TableColumn<FinanceAccountData, Long> nrColumn = new TableColumn<>(FinanceAccountFieldNames.NO);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));

        final TableColumn<FinanceAccountData, String> kontoColumn = new TableColumn<>(FinanceAccountFieldNames.NAME);
        kontoColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        final TableColumn<FinanceAccountData, Boolean> giroColumn = new TableColumn<>(FinanceAccountFieldNames.GIRO);
        giroColumn.setCellValueFactory(new PropertyValueFactory<>("giro"));
        giroColumn.setCellFactory(new CellCheckBox().cellFactoryBool);


        final TableColumn<FinanceAccountData, String> textColumn = new TableColumn<>(FinanceAccountFieldNames.DESCRIPTION);
        textColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        tc.add(nrColumn);
        tc.add(kontoColumn);
        tc.add(giroColumn);
        tc.add(textColumn);

        return tc.toArray(new TableColumn[]{});
    }
}
