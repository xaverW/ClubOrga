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
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.extraData.ExtraData;
import de.p2tools.clubOrga.data.extraData.ExtraKind;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.guiTools.pTable.CellCheckBox;
import de.p2tools.p2Lib.tools.date.PLDateProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.ArrayList;

public class ClubTableFinance {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTableFinance(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }

    public TableColumn[] initColumn(TableView table) {
        ArrayList<TableColumn> tc = new ArrayList<>();

        table.getColumns().clear();

        final TableColumn<FinanceData, Long> noColumn = new TableColumn<>(FinanceFieldNames.NR);
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));

        final TableColumn<FinanceData, String> belegNrColumn = new TableColumn<>(FinanceFieldNames.BELEG_NR);
        belegNrColumn.setCellValueFactory(new PropertyValueFactory<>("receiptNo"));

        final TableColumn<FinanceData, String> textColumn = new TableColumn<>(FinanceFieldNames.TEXT);
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        final TableColumn<FinanceData, Long> gesamtbetragColumn = new TableColumn<>(FinanceFieldNames.GESAMTBETRAG);
        gesamtbetragColumn.setCellValueFactory(new PropertyValueFactory<>("gesamtbetrag"));
        gesamtbetragColumn.setCellFactory((final TableColumn<FinanceData, Long> param) -> new PTableFactory.PCellMoney<>());

        final TableColumn<FinanceData, String> kontoColumn = new TableColumn<>(FinanceFieldNames.KONTO);
        kontoColumn.setCellValueFactory(new PropertyValueFactory<>("financeAccountData"));

        final TableColumn<FinanceData, String> kategorieColumn = new TableColumn<>(FinanceFieldNames.KATEGORIE);
        kategorieColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        final TableColumn<FinanceData, Integer> geschaeftsJahrColumn = new TableColumn<>(FinanceFieldNames.GESCHAEFTSJAHR);
        geschaeftsJahrColumn.setCellValueFactory(new PropertyValueFactory<>("geschaeftsJahr"));

        final TableColumn<FinanceData, PLDateProperty> buchungsDatumColumn = new TableColumn<>(FinanceFieldNames.BUCHUNGSDATUM);
        buchungsDatumColumn.setCellValueFactory(new PropertyValueFactory<>("buchungsDatum"));

        final TableColumn<FinanceData, LocalDate> erstellDatumColumn = new TableColumn<>(FinanceFieldNames.ERSTELLDATUM);
        erstellDatumColumn.setCellValueFactory(new PropertyValueFactory<>("erstellDatum"));

        tc.add(noColumn);
        tc.add(belegNrColumn);
        tc.add(textColumn);
        tc.add(gesamtbetragColumn);
        tc.add(kontoColumn);
        tc.add(kategorieColumn);
        tc.add(geschaeftsJahrColumn);
        tc.add(buchungsDatumColumn);
        tc.add(erstellDatumColumn);

        addExtra(tc);

        return tc.toArray(new TableColumn[]{});
    }

    private void addExtra(ArrayList<TableColumn> tc) {
        for (int i = 0; i < ProgConst.MAX_EXTRA_DATA_MAX_FINANCE; ++i) {

            final int ii = i;
            ExtraData extraData = clubConfig.extraDataListFinance.get(i);
            if (!extraData.isOn()) {
                continue;
            }

            final TableColumn extraCol = new TableColumn();
            extraCol.textProperty().bind(extraData.nameProperty());

            if (extraData.getKind().equals(ExtraKind.EXTRA_KIND.STRING.toString())) {

                extraCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<FinanceData, String>, ObservableValue<String>>)
                        p -> p.getValue().getExtraDataPropertyList().get(ii).getStringProp()
                );
                tc.add(extraCol);

            } else if (extraData.getKind().equals(ExtraKind.EXTRA_KIND.INTEGER.toString())) {

                extraCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<FinanceData, Integer>, ObservableValue<Number>>)
                        p -> p.getValue().getExtraDataPropertyList().get(ii).getIntProp()
                );
                tc.add(extraCol);

            } else if (extraData.getKind().equals(ExtraKind.EXTRA_KIND.BOOLEAN.toString())) {

                extraCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<FinanceData, Boolean>, ObservableValue<Boolean>>)
                        p -> p.getValue().getExtraDataPropertyList().get(ii).getBoolProp()
                );
                extraCol.setCellFactory(new CellCheckBox().cellFactoryBool);
                tc.add(extraCol);

            }

        }
    }

}
