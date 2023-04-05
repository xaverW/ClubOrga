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
import de.p2tools.cluborga.data.feeData.FeeData;
import de.p2tools.cluborga.data.feeData.FeeFieldNames;
import de.p2tools.p2lib.guitools.PTableFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class ClubTableAddMissingFee {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTableAddMissingFee(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }

    public TableColumn[] initColumn(TableView table) {
        ArrayList<TableColumn> tc = new ArrayList<>();

        table.getColumns().clear();

        final TableColumn<FeeData, Long> noColumn = new TableColumn<>(FeeFieldNames.NR);
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));

        final TableColumn<FeeData, Long> mNoColumn = new TableColumn<>(FeeFieldNames.MEMBER_NO);
        mNoColumn.setCellValueFactory(new PropertyValueFactory<>("memberNo"));

        final TableColumn<FeeData, String> mNameColumn = new TableColumn<>(FeeFieldNames.MEMBER_NAME);
        mNameColumn.setCellValueFactory(new PropertyValueFactory<>("memberName"));

        final TableColumn<FeeData, Long> betragColumn = new TableColumn<>(FeeFieldNames.BETRAG);
        betragColumn.setCellValueFactory(new PropertyValueFactory<>("betrag"));
        betragColumn.setCellFactory((final TableColumn<FeeData, Long> param) -> new PTableFactory.PCellMoney<>());

        final TableColumn<FeeData, String> jahrColumn = new TableColumn<>(FeeFieldNames.JAHR);
        jahrColumn.setCellValueFactory(new PropertyValueFactory<>("jahr"));

        final TableColumn<FeeData, String> zahlartColumn = new TableColumn<>(FeeFieldNames.ZAHLART);
        zahlartColumn.setCellValueFactory(new PropertyValueFactory<>("paymentTypeData"));

        tc.add(noColumn);
        tc.add(mNoColumn);
        tc.add(mNameColumn);
        tc.add(betragColumn);
        tc.add(jahrColumn);
        tc.add(zahlartColumn);

        return tc.toArray(new TableColumn[]{});
    }
}
