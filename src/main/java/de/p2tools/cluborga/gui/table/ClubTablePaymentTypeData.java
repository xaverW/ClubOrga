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
import de.p2tools.cluborga.data.feeData.paymentType.PaymentTypeData;
import de.p2tools.cluborga.data.feeData.paymentType.PaymentTypeNames;
import de.p2tools.p2lib.guitools.ptable.CellCheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class ClubTablePaymentTypeData {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTablePaymentTypeData(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }

    public TableColumn[] initColumn(TableView table) {
        ArrayList<TableColumn> tc = new ArrayList<>();

        table.getColumns().clear();

        final TableColumn<PaymentTypeData, Long> nrColumn = new TableColumn<>(PaymentTypeNames.NO);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));

        final TableColumn<PaymentTypeData, String> nameColumn = new TableColumn<>(PaymentTypeNames.NAME);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        final TableColumn<PaymentTypeData, Object> kontoColumn = new TableColumn<>(PaymentTypeNames.ACCOUNT);
        kontoColumn.setCellValueFactory(new PropertyValueFactory<>("financeAccountData"));

        final TableColumn<PaymentTypeData, Boolean> einzugColumn = new TableColumn<>(PaymentTypeNames.DIRECT_DEBIT);
        einzugColumn.setCellValueFactory(new PropertyValueFactory<>("directDebit"));
        einzugColumn.setCellFactory(new CellCheckBox().cellFactoryBool);

        final TableColumn<PaymentTypeData, String> textColumn = new TableColumn<>(PaymentTypeNames.DESCRIPTION);
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));


        tc.add(nrColumn);
        tc.add(nameColumn);
        tc.add(kontoColumn);
        tc.add(einzugColumn);
        tc.add(textColumn);

        return tc.toArray(new TableColumn[]{});
    }

}
