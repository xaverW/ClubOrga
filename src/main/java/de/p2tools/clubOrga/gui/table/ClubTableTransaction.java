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
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.clubOrga.data.financeData.TransactionData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.ArrayList;

public class ClubTableTransaction {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTableTransaction(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }

    public TableColumn[] initColumn(TableView table) {
        ArrayList<TableColumn> tc = new ArrayList<>();

        table.getColumns().clear();

        final TableColumn<TransactionData, Long> nrColumn = new TableColumn<>(FinanceFieldNames.NR);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));

        final TableColumn<TransactionData, Long> betragColumn = new TableColumn<>(FinanceFieldNames.BETRAG);
        betragColumn.setCellValueFactory(new PropertyValueFactory<>("betrag"));
        betragColumn.setCellFactory((final TableColumn<TransactionData, Long> param) -> new PTableFactory.PCellMoney<>());

        final TableColumn<TransactionData, String> kategorieColumn = new TableColumn<>(FinanceFieldNames.KATEGORIE);
        kategorieColumn.setCellValueFactory(new PropertyValueFactory<>("financeCategoryData"));

        final TableColumn<TransactionData, String> mitgliedColumn = new TableColumn<>("Mitgliedsbeitrag");
        mitgliedColumn.setCellFactory(cellFactoryNachname); //todo evtl. MemberData in die TransactionData mit aufnehmen??

        tc.add(nrColumn);
        tc.add(betragColumn);
//        tc.add(kontoColumn);
        tc.add(kategorieColumn);
        tc.add(mitgliedColumn);

        return tc.toArray(new TableColumn[]{});
    }

    private Callback<TableColumn<TransactionData, String>, TableCell<TransactionData, String>> cellFactoryNachname
            = (final TableColumn<TransactionData, String> param) -> {

        final TableCell<TransactionData, String> cell = new TableCell<TransactionData, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                    return;
                }

                TransactionData transactionData = getTableView().getItems().get(getIndex());
                FeeData feeData = null;
                MemberData memberData = null;
                if (transactionData.getFeeData() != null) {
                    feeData = transactionData.getFeeData();
                }
                if (feeData != null) {
                    memberData = feeData.getMemberData();
                }
                if (memberData != null) {
                    setText(memberData.toString());
                } else {
                    setText("");
                }
            }
        };

        return cell;
    };
}
