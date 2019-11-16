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
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeNames;
import de.p2tools.p2Lib.guiTools.PCheckBoxCell;
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

        final TableColumn<PaymentTypeData, Long> nrColumn = new TableColumn<>(PaymentTypeNames.NR);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));

        final TableColumn<PaymentTypeData, String> nameColumn = new TableColumn<>(PaymentTypeNames.NAME);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        final TableColumn<PaymentTypeData, Object> kontoColumn = new TableColumn<>(PaymentTypeNames.KONTO);
        kontoColumn.setCellValueFactory(new PropertyValueFactory<>("financeAccountData"));

        final TableColumn<PaymentTypeData, Boolean> einzugColumn = new TableColumn<>(PaymentTypeNames.EINZUG);
        einzugColumn.setCellValueFactory(new PropertyValueFactory<>("einzug"));
        einzugColumn.setCellFactory(new PCheckBoxCell().cellFactoryBool);

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
