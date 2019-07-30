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
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.feeData.FeeFieldNames;
import de.p2tools.p2Lib.guiTools.PCheckBoxCell;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.date.PLocalDateProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.ArrayList;

public class ClubTableFee {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTableFee(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }

    public TableColumn[] initColumn(TableView table) {
        ArrayList<TableColumn> tc = new ArrayList<>();

        table.getColumns().clear();

        final TableColumn<FeeData, Long> nrColumn = new TableColumn<>(FeeFieldNames.NR);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));

        final TableColumn<FeeData, Long> mNrColumn = new TableColumn<>(FeeFieldNames.MEMBER_NR);
        mNrColumn.setCellFactory(cellFactoryMemberNr);
        mNrColumn.setCellValueFactory(new PropertyValueFactory<>("mitgliedNr"));

        final TableColumn<FeeData, String> mNameColumn = new TableColumn<>(FeeFieldNames.MEMBER_NAME);
        mNameColumn.setCellValueFactory(new PropertyValueFactory<>("mitgliedName"));

        final TableColumn<FeeData, Long> betragColumn = new TableColumn<>(FeeFieldNames.BETRAG);
        betragColumn.setCellValueFactory(new PropertyValueFactory<>("betrag"));
        betragColumn.setCellFactory((final TableColumn<FeeData, Long> param) -> new PTableFactory.PCellMoney<>());

        final TableColumn<FeeData, String> jahrColumn = new TableColumn<>(FeeFieldNames.JAHR);
        jahrColumn.setCellValueFactory(new PropertyValueFactory<>("jahr"));

        final TableColumn<FeeData, String> zahlartColumn = new TableColumn<>(FeeFieldNames.ZAHLART);
        zahlartColumn.setCellValueFactory(new PropertyValueFactory<>("paymentTypeData"));

        final TableColumn<FeeData, PLocalDateProperty> bezahltColumn = new TableColumn<>(FeeFieldNames.BEZAHLT);
        bezahltColumn.setCellValueFactory(new PropertyValueFactory<>("bezahlt"));

        final TableColumn<FeeData, PLocalDateProperty> rechnungColumn = new TableColumn<>(FeeFieldNames.RECHNUNG);
        rechnungColumn.setCellValueFactory(new PropertyValueFactory<>("rechnung"));

        final TableColumn<FeeData, PLocalDateProperty> spendenQColumn = new TableColumn<>(FeeFieldNames.SPENDEN_Q);
        spendenQColumn.setCellValueFactory(new PropertyValueFactory<>("spendenQ"));

        final TableColumn<FeeData, PLocalDate> erstellDatumColumn = new TableColumn<>(FeeFieldNames.ERSTELLDATUM);
        erstellDatumColumn.setCellValueFactory(new PropertyValueFactory<>("erstellDatum"));

        tc.add(nrColumn);
        tc.add(mNrColumn);
        tc.add(mNameColumn);
        tc.add(betragColumn);
        tc.add(jahrColumn);
        tc.add(zahlartColumn);
        tc.add(bezahltColumn);
        tc.add(rechnungColumn);
        tc.add(spendenQColumn);
        tc.add(erstellDatumColumn);

        addExtra(tc);

        return tc.toArray(new TableColumn[]{});
    }

    private void addExtra(ArrayList<TableColumn> tc) {
        for (int i = 0; i < ProgConst.MAX_EXTRA_DATA_MAX_FEE; ++i) {

            final int ii = i;
            ExtraData extraData = clubConfig.extraDataListFee.get(i);
            if (!extraData.isOn()) {
                continue;
            }

            final TableColumn extraCol = new TableColumn();
            extraCol.textProperty().bind(extraData.nameProperty());

            if (extraData.getKind().equals(ExtraKind.EXTRA_KIND.STRING.toString())) {

                extraCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<FeeData, String>, ObservableValue<String>>)
                        p -> p.getValue().getExtraDataPropertyList().get(ii).getStringProp()
                );
                tc.add(extraCol);

            } else if (extraData.getKind().equals(ExtraKind.EXTRA_KIND.INTEGER.toString())) {

                extraCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<FeeData, Integer>, ObservableValue<Number>>)
                        p -> p.getValue().getExtraDataPropertyList().get(ii).getIntProp()
                );
                tc.add(extraCol);

            } else if (extraData.getKind().equals(ExtraKind.EXTRA_KIND.BOOLEAN.toString())) {

                extraCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<FeeData, Boolean>, ObservableValue<Boolean>>)
                        p -> p.getValue().getExtraDataPropertyList().get(ii).getBoolProp()
                );
                extraCol.setCellFactory(new PCheckBoxCell().cellFactoryBool);
                tc.add(extraCol);

            }

        }
    }

    private Callback<TableColumn<FeeData, Long>, TableCell<FeeData, Long>> cellFactoryMemberNr
            = (final TableColumn<FeeData, Long> param) -> {

        final TableCell<FeeData, Long> cell = new TableCell<>() {

            @Override
            public void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                    return;
                }

                if (item == 0) {
                    setText("");
                } else {
                    setText(item + "");
                }
            }
        };
        return cell;
    };


}
