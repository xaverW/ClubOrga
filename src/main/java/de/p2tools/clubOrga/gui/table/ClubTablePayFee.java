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
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.feeData.FeeFieldNames;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.util.ArrayList;

public class ClubTablePayFee {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTablePayFee(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }

    public TableColumn[] initColumn(TableView table) {
        ArrayList<TableColumn> tc = new ArrayList<>();

        table.getColumns().clear();

        final TableColumn<FeeData, Long> nrColumn = new TableColumn<>(FeeFieldNames.NR);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));

        final TableColumn<FeeData, Long> mNrColumn = new TableColumn<>(FeeFieldNames.MEMBER_NR);
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

        final TableColumn<FeeData, String> startColumn = new TableColumn<>("");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        startColumn.setCellFactory(cellFactoryState);
        startColumn.getStyleClass().add("alignCenter");

        tc.add(startColumn);
        tc.add(nrColumn);
        tc.add(mNrColumn);
        tc.add(mNameColumn);
        tc.add(betragColumn);
        tc.add(jahrColumn);
        tc.add(zahlartColumn);


        return tc.toArray(new TableColumn[]{});
    }

    private Callback<TableColumn<FeeData, String>, TableCell<FeeData, String>> cellFactoryState
            = (final TableColumn<FeeData, String> param) -> {

        final TableCell<FeeData, String> cell = new TableCell<FeeData, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

//                final HBox hBox = new HBox();
//                hBox.setAlignment(Pos.CENTER);
//                hBox.setPadding(new Insets(0));
                final Button btnDel = new Button("");
                btnDel.setTooltip(new Tooltip("Beitrag aus Liste entfernen"));
                ImageView imageView = new ProgIcons().ICON_BUTTON_TABLE_DEL;
                btnDel.setGraphic(imageView);

                btnDel.setOnAction(event -> {
                    FeeData feeData = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(feeData);
                });
//                hBox.getChildren().add(btnDel);
                setGraphic(btnDel);
            }

        };
        return cell;
    };
}
