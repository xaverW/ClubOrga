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
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.clubOrga.data.financeData.FinanceReportData;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.tools.date.PLocalDateProperty;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class ClubTableFinanceReport {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTableFinanceReport(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }


    public TableColumn[] initColumn(TableView table) {
        ArrayList<TableColumn> tc = new ArrayList<>();

        table.getColumns().clear();

        final TableColumn<FinanceReportData, Long> nrColumn = new TableColumn<>(FinanceFieldNames.NO);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));

        final TableColumn<FinanceReportData, String> belegNrColumn = new TableColumn<>(FinanceFieldNames.RECEIPT_NO);
        belegNrColumn.setCellValueFactory(new PropertyValueFactory<>("receiptNo"));

        final TableColumn<FinanceReportData, String> textColumn = new TableColumn<>(FinanceFieldNames.TEXT);
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        final TableColumn<FinanceReportData, Integer> geschaeftsJahrColumn = new TableColumn<>(FinanceFieldNames.GESCHAEFTSJAHR);
        geschaeftsJahrColumn.setCellValueFactory(new PropertyValueFactory<>("geschaeftsJahr"));

        final TableColumn<FinanceReportData, PLocalDateProperty> buchungsDatumColumn = new TableColumn<>(FinanceFieldNames.BUCHUNGS_DATUM);
        buchungsDatumColumn.setCellValueFactory(new PropertyValueFactory<>("buchungsDatum"));

        final TableColumn<FinanceReportData, Long> gesamtbetragColumn = new TableColumn<>(FinanceFieldNames.GESAMTBETRAG);
        gesamtbetragColumn.setCellValueFactory(new PropertyValueFactory<>("gesamtbetrag"));
        gesamtbetragColumn.setCellFactory((final TableColumn<FinanceReportData, Long> param) -> new PTableFactory.PCellMoney<>());
        gesamtbetragColumn.getStyleClass().add("moneyColumn");

        tc.add(nrColumn);
        tc.add(belegNrColumn);
        tc.add(textColumn);
        tc.add(geschaeftsJahrColumn);
        tc.add(buchungsDatumColumn);
        tc.add(gesamtbetragColumn);

        for (int i = 0; i < clubConfig.financeReportDataList.getAccounts().size(); i++) {
            final int curCol = i;
            final TableColumn<FinanceReportData, Long> column = new TableColumn<>(
                    clubConfig.financeReportDataList.getAccounts().get(i)
            );
            column.setCellFactory((final TableColumn<FinanceReportData, Long> param) ->
                    new PTableFactory.PCellMoney<>(false));
            column.setCellValueFactory(
                    param -> { //todo beim löschen von Katogorien oder Konten
                        if (param.getValue().getAccountList().size() <= curCol) {
                            PLog.errorLog(910254129, ".getCategoryList().size() <= curCol");
                            return new ReadOnlyObjectWrapper<Long>(0L);
                        } else {
                            return new ReadOnlyObjectWrapper<>(
                                    param.getValue().getAccountList().get(curCol).getBetrag()
                            );
                        }
                    }
            );
            column.getStyleClass().add("accColumn");
            tc.add(column);
        }

        for (int i = 0; i < clubConfig.financeReportDataList.getCategories().size(); i++) {
            final int curCol = i;
            final TableColumn<FinanceReportData, Long> column = new TableColumn<>(
                    clubConfig.financeReportDataList.getCategories().get(i)
            );
            column.setCellFactory((final TableColumn<FinanceReportData, Long> param) ->
                    new PTableFactory.PCellMoney<>(false));
            column.setCellValueFactory(
                    param -> {
                        if (param.getValue().getCategoryList().size() <= curCol) {
                            PLog.errorLog(201450983, "getCategoryList().size() <= curCol");
                            return new ReadOnlyObjectWrapper<Long>(0L);
                        } else {
                            return new ReadOnlyObjectWrapper<>(
                                    param.getValue().getCategoryList().get(curCol).getBetrag()
                            );
                        }
                    }
            );
            column.getStyleClass().add("catColumn");
            tc.add(column);
        }

        return tc.toArray(new TableColumn[]{});
    }
}
