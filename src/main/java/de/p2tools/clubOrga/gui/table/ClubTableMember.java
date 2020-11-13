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
import de.p2tools.clubOrga.data.extraData.ExtraData;
import de.p2tools.clubOrga.data.extraData.ExtraKind;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberFieldNames;
import de.p2tools.p2Lib.guiTools.PCheckBoxCell;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.ArrayList;

public class ClubTableMember {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTableMember(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }

    public TableColumn[] initColumn(TableView table) {
        ArrayList<TableColumn> tc = new ArrayList<>();

        table.getColumns().clear();

        final TableColumn<MemberData, Long> nrColumn = new TableColumn<>(MemberFieldNames.NR);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));

        final TableColumn<MemberData, String> nachnameColumn = new TableColumn<>(MemberFieldNames.NACHNAME);
        nachnameColumn.setCellValueFactory(new PropertyValueFactory<>("nachname"));

        final TableColumn<MemberData, String> vornameColumn = new TableColumn<>(MemberFieldNames.VORNAME);
        vornameColumn.setCellValueFactory(new PropertyValueFactory<>("vorname"));

        final TableColumn<MemberData, String> anredeColumn = new TableColumn<>(MemberFieldNames.ANREDE);
        anredeColumn.setCellValueFactory(new PropertyValueFactory<>("anrede"));

        final TableColumn<MemberData, String> emailColumn = new TableColumn<>(MemberFieldNames.EMAIL);
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        final TableColumn<MemberData, String> telefonColumn = new TableColumn<>(MemberFieldNames.TELEFON);
        telefonColumn.setCellValueFactory(new PropertyValueFactory<>("telefon"));

        final TableColumn<MemberData, String> strasseColumn = new TableColumn<>(MemberFieldNames.STRASSE);
        strasseColumn.setCellValueFactory(new PropertyValueFactory<>("strasse"));

        final TableColumn<MemberData, String> plzColumn = new TableColumn<>(MemberFieldNames.PLZ);
        plzColumn.setCellValueFactory(new PropertyValueFactory<>("plz"));

        final TableColumn<MemberData, String> ortColumn = new TableColumn<>(MemberFieldNames.ORT);
        ortColumn.setCellValueFactory(new PropertyValueFactory<>("ort"));

        final TableColumn<MemberData, String> landColumn = new TableColumn<>(MemberFieldNames.LAND);
        landColumn.setCellValueFactory(new PropertyValueFactory<>("land"));

        final TableColumn<MemberData, Object> statusColumn = new TableColumn<>(MemberFieldNames.STATUS);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("stateData"));

        final TableColumn<MemberData, Long> beitragColumn = new TableColumn<>(MemberFieldNames.BEITRAG);
        beitragColumn.setCellValueFactory(new PropertyValueFactory<>("beitrag"));
        beitragColumn.setCellFactory((final TableColumn<MemberData, Long> param) -> new PTableFactory.PCellMoney<>());

        final TableColumn<MemberData, Object> beitragssatzColumn = new TableColumn<>(MemberFieldNames.BEITRAGSSATZ);
        beitragssatzColumn.setCellValueFactory(new PropertyValueFactory<>("feeRateData"));

        final TableColumn<MemberData, Object> zahlartColumn = new TableColumn<>(MemberFieldNames.ZAHLART);
        zahlartColumn.setCellValueFactory(new PropertyValueFactory<>("paymentTypeData"));

        final TableColumn<MemberData, String> bankColumn = new TableColumn<>(MemberFieldNames.BANK);
        bankColumn.setCellValueFactory(new PropertyValueFactory<>("bank"));

        final TableColumn<MemberData, String> ibanColumn = new TableColumn<>(MemberFieldNames.IBAN);
        ibanColumn.setCellValueFactory(new PropertyValueFactory<>("iban"));

        final TableColumn<MemberData, String> bicColumn = new TableColumn<>(MemberFieldNames.BIC);
        bicColumn.setCellValueFactory(new PropertyValueFactory<>("bic"));

        final TableColumn<MemberData, String> kontoinhaberColumn = new TableColumn<>(MemberFieldNames.KONTOINHABER);
        kontoinhaberColumn.setCellValueFactory(new PropertyValueFactory<>("kontoinhaber"));

        final TableColumn<MemberData, PLocalDate> ZahlungsbeginnColumn = new TableColumn<>(MemberFieldNames.ZAHLUNGSBEGINN);
        ZahlungsbeginnColumn.setCellValueFactory(new PropertyValueFactory<>("zahlungsbeginn"));

        final TableColumn<MemberData, PLocalDate> beitrittColumn = new TableColumn<>(MemberFieldNames.BEITRITT);
        beitrittColumn.setCellValueFactory(new PropertyValueFactory<>("beitritt"));

        final TableColumn<MemberData, PLocalDate> erstellDatumColumn = new TableColumn<>(MemberFieldNames.ERSTELLDATUM);
        erstellDatumColumn.setCellValueFactory(new PropertyValueFactory<>("erstellDatum"));

        tc.add(nrColumn);
        tc.add(nachnameColumn);
        tc.add(vornameColumn);
        tc.add(anredeColumn);
        tc.add(emailColumn);
        tc.add(telefonColumn);
        tc.add(strasseColumn);
        tc.add(plzColumn);
        tc.add(ortColumn);
        tc.add(landColumn);
        tc.add(statusColumn);
        tc.add(beitragColumn);
        tc.add(beitragssatzColumn);
        tc.add(zahlartColumn);
        tc.add(bankColumn);
        tc.add(ibanColumn);
        tc.add(bicColumn);
        tc.add(kontoinhaberColumn);
        tc.add(ZahlungsbeginnColumn);
        tc.add(beitrittColumn);
        tc.add(erstellDatumColumn);

        addExtra(tc);

        return tc.toArray(new TableColumn[]{});
    }


    private void addExtra(ArrayList<TableColumn> tc) {
        for (int i = 0; i < clubConfig.extraDataListMember.size(); ++i) {

            final int ii = i;
            ExtraData extraData = clubConfig.extraDataListMember.get(ii);
            if (!extraData.isOn()) {
                continue;
            }

            final TableColumn extraCol = new TableColumn();
            extraCol.textProperty().bind(extraData.nameProperty());

            if (extraData.getKind().equals(ExtraKind.EXTRA_KIND.STRING.toString())) {

                extraCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<MemberData, String>, ObservableValue<String>>)
                        p -> p.getValue().getExtraDataPropertyList().get(ii).getStringProp()
                );
                tc.add(extraCol);

            } else if (extraData.getKind().equals(ExtraKind.EXTRA_KIND.INTEGER.toString())) {

                extraCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<MemberData, Integer>, ObservableValue<Number>>)
                        p -> p.getValue().getExtraDataPropertyList().get(ii).getIntProp()
                );
                tc.add(extraCol);

            } else if (extraData.getKind().equals(ExtraKind.EXTRA_KIND.BOOLEAN.toString())) {

                extraCol.setCellValueFactory((Callback<TableColumn.CellDataFeatures<MemberData, Boolean>, ObservableValue<Boolean>>)
                        p -> p.getValue().getExtraDataPropertyList().get(ii).getBoolProp()
                );
                extraCol.setCellFactory(new PCheckBoxCell().cellFactoryBool);
                tc.add(extraCol);

            }

        }
    }

    private Callback<TableColumn<MemberData, Long>, TableCell<MemberData, Long>> cellFactoryBeitragssatz
            = (final TableColumn<MemberData, Long> param) -> {

        final TableCell<MemberData, Long> cell = new TableCell<MemberData, Long>() {

            @Override
            public void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                    return;
                }

                MemberData memberData = getTableView().getItems().get(getIndex());
                FeeRateData feeRateData = memberData.getFeeRateData();
                setText(feeRateData.getName());
            }
        };
        return cell;
    };
}
