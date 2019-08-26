/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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

package de.p2tools.clubOrga.gui.guiFinanceReport;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.clubOrga.data.financeData.FinanceReportData;
import de.p2tools.clubOrga.data.financeData.FinanceReportFactory;
import de.p2tools.clubOrga.gui.tools.GuiFactory;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.tools.date.PLocalDateProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiFinanceReport extends BorderPane {

    private final HBox hBoxSelect = new HBox(10);
    private final VBox vBoxTable = new VBox(0);
    private final ScrollPane scrollPaneTable = new ScrollPane();
    private final TableView<FinanceReportData> tableView = new TableView<>();
    private final ScrollPane scrollPaneTableSum = new ScrollPane();
    private final TableView<FinanceReportData> tableViewSum = new TableView<>();

    private final ClubConfig clubConfig;

    private final FilteredList<FinanceReportData> filteredList;
    private final SortedList<FinanceReportData> sortedList;
    private final SortedList<FinanceReportData> sortedListSum;

    private final GuiFinanceReportFilterPane reportFilterPane;
    private final GuiFinanceReportMenu reportMenu;


    public GuiFinanceReport(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;

        filteredList = clubConfig.financeReportDataList.getFilteredList();
        sortedList = clubConfig.financeReportDataList.getSortedList();
        sortedListSum = clubConfig.financeReportDataListSum.getSortedList();

        reportFilterPane = new GuiFinanceReportFilterPane(clubConfig);
        reportMenu = new GuiFinanceReportMenu(clubConfig, this);

        addFilterListener();

        scrollPaneTable.setFitToHeight(true);
        scrollPaneTable.setFitToWidth(true);
        scrollPaneTable.setContent(tableView);
        scrollPaneTable.setMaxHeight(Double.MAX_VALUE);

        scrollPaneTableSum.setFitToHeight(true);
        scrollPaneTableSum.setFitToWidth(true);
        scrollPaneTableSum.setContent(tableViewSum);
        scrollPaneTableSum.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(scrollPaneTable, Priority.ALWAYS);
        vBoxTable.getChildren().addAll(scrollPaneTable, scrollPaneTableSum, hBoxSelect);

        setStyle("-fx-background-color: -fx-background;");

        initCont();
        initSelButton();
        initTable();
    }

    public void refreshTable() {
        tableView.setItems(null);
        tableView.getColumns().clear();
        initTable();
        tableView.refresh();
    }

    public void isShown() {
        if (tableView.getSelectionModel().getSelectedItem() == null) {
            tableView.getSelectionModel().selectFirst();
        }
    }

    private void initCont() {
        GuiFactory.setPaneTitle(this, "Finanzübersicht");
        setCenter(vBoxTable);
        setRight(reportMenu);
        setBottom(reportFilterPane);
    }

    private void initSelButton() {
        Button btnSelAll = new Button("alles");
        Button btnClear = new Button("löschen");
        Button btnInvert = new Button("umkehren");
        Button btnOnlySel = new Button("Auswahl filtern");
        Button btnClearFilter = new Button("Filter löschen");
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.getChildren().add(btnClearFilter);

        hBoxSelect.getStyleClass().add("filter-panel");
        hBoxSelect.setPadding(new Insets(10));
        hBoxSelect.setAlignment(Pos.CENTER_LEFT);
        hBoxSelect.getChildren().addAll(new Label("Auswahl:"), btnSelAll, btnClear, btnInvert, btnOnlySel, hBox);

        btnSelAll.setOnAction(a -> tableView.getSelectionModel().selectAll());
        btnClear.setOnAction(a -> tableView.getSelectionModel().clearSelection());
        btnInvert.setOnAction(a -> PTableFactory.invertSelection(tableView));
        btnOnlySel.setOnAction(a -> {
//            if (getSelList().size() <= 0) {
//                return;
//            }
//            setFilter(true);
        });

        btnClearFilter.setOnAction(a -> {
        });
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(false);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        tableViewSum.setTableMenuButtonVisible(false);
        tableViewSum.setEditable(false);
        tableViewSum.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableViewSum.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableViewSum.setMinHeight(0);
        tableViewSum.getStyleClass().add("noheader");

        // create the date
        FinanceReportFactory.makeReportData(clubConfig);
        // init the table column
        initColumn(tableView);
        initColumnSum(tableViewSum);
        tableView.getColumns().stream().forEach(c -> c.setReorderable(false));
        tableViewSum.getColumns().stream().forEach(c -> {
            c.setResizable(false);
            c.setReorderable(false);
        });
        for (int i = 0; i < tableView.getColumns().size(); ++i) {
            final int ii = i;
            tableView.getColumns().get(i).widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    System.out.println(tableView.getColumns().get(ii).getPrefWidth());
                    tableViewSum.getColumns().get(ii).setPrefWidth(newValue.doubleValue());
                }
            });
        }
        // bind the date with the table
        tableView.setItems(sortedList);
        tableViewSum.setItems(sortedListSum);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        sortedListSum.comparatorProperty().bind(tableViewSum.comparatorProperty());
    }

    private void setFilter(boolean onlySelected) {
        if (onlySelected) {
            clubConfig.financeReportDataList.clearSelected();
            tableView.getSelectionModel().getSelectedItems().stream()
                    .forEach(financeReportData -> financeReportData.setSelected(true));
        }

        filteredList.setPredicate(FinanceReportFilterPredicate.getProperty(clubConfig, onlySelected));
        boolean filtered = FinanceReportFilterPredicate.getFiltered();
    }

    private void addFilterListener() {
        clubConfig.financesReportFilterChange.addListener((observable, oldValue, newValue) -> setFilter(false));
        setFilter(false);
    }

    private void initColumn(TableView table) {
        final TableColumn<FinanceData, Long> nrColumn = new TableColumn<>(FinanceFieldNames.NR);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));

        final TableColumn<FinanceData, String> belegNrColumn = new TableColumn<>(FinanceFieldNames.BELEG_NR);
        belegNrColumn.setCellValueFactory(new PropertyValueFactory<>("belegNr"));

        final TableColumn<FinanceData, Integer> geschaeftsJahrColumn = new TableColumn<>(FinanceFieldNames.GESCHAEFTSJAHR);
        geschaeftsJahrColumn.setCellValueFactory(new PropertyValueFactory<>("geschaeftsJahr"));

        final TableColumn<FinanceData, PLocalDateProperty> buchungsDatumColumn = new TableColumn<>(FinanceFieldNames.BUCHUNGS_DATUM);
        buchungsDatumColumn.setCellValueFactory(new PropertyValueFactory<>("buchungsDatum"));

        final TableColumn<FinanceData, Long> gesamtbetragColumn = new TableColumn<>(FinanceFieldNames.GESAMTBETRAG);
        gesamtbetragColumn.setCellValueFactory(new PropertyValueFactory<>("gesamtbetrag"));
        gesamtbetragColumn.setCellFactory((final TableColumn<FinanceData, Long> param) -> new PTableFactory.PCellMoney<>());
        gesamtbetragColumn.getStyleClass().add("moneyColumn");

        table.getColumns().addAll(nrColumn, belegNrColumn, geschaeftsJahrColumn,
                buchungsDatumColumn, /*erstellDatumColumn,*/
                gesamtbetragColumn);

        for (int i = 0; i < clubConfig.financeReportDataList.getAccounts().size(); i++) {
            final int curCol = i;
            final TableColumn<FinanceReportData, Long> column = new TableColumn<>(
                    clubConfig.financeReportDataList.getAccounts().get(i)
            );
            column.setCellFactory((final TableColumn<FinanceReportData, Long> param) ->
                    new PTableFactory.PCellMoney<>(false));
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getAccountList().get(curCol).getBetrag())

            );
            column.getStyleClass().add("accColumn");
            tableView.getColumns().add(column);
        }

        for (int i = 0; i < clubConfig.financeReportDataList.getCategories().size(); i++) {
            final int curCol = i;
            final TableColumn<FinanceReportData, Long> column = new TableColumn<>(
                    clubConfig.financeReportDataList.getCategories().get(i)
            );
            column.setCellFactory((final TableColumn<FinanceReportData, Long> param) ->
                    new PTableFactory.PCellMoney<>(false));
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getCategoryList().get(curCol).getBetrag())
            );
            column.getStyleClass().add("catColumn");
            tableView.getColumns().add(column);
        }

    }

    private void initColumnSum(TableView table) {
        final TableColumn<FinanceData, Long> nrColumn = new TableColumn<>(FinanceFieldNames.NR);
        final TableColumn<FinanceData, String> belegNrColumn = new TableColumn<>(FinanceFieldNames.BELEG_NR);
        final TableColumn<FinanceData, Integer> geschaeftsJahrColumn = new TableColumn<>(FinanceFieldNames.GESCHAEFTSJAHR);
        final TableColumn<FinanceData, PLocalDateProperty> buchungsDatumColumn = new TableColumn<>(FinanceFieldNames.BUCHUNGS_DATUM);

        final TableColumn<FinanceData, Long> gesamtbetragColumn = new TableColumn<>(FinanceFieldNames.GESAMTBETRAG);
        gesamtbetragColumn.setCellValueFactory(new PropertyValueFactory<>("gesamtbetrag"));
        gesamtbetragColumn.setCellFactory((final TableColumn<FinanceData, Long> param) -> new PTableFactory.PCellMoney<>());
        gesamtbetragColumn.getStyleClass().add("moneyColumn");

        table.getColumns().addAll(nrColumn, belegNrColumn, geschaeftsJahrColumn,
                buchungsDatumColumn, /*erstellDatumColumn,*/
                gesamtbetragColumn);

        for (int i = 0; i < clubConfig.financeReportDataList.getAccounts().size(); i++) {
            final int curCol = i;
            final TableColumn<FinanceReportData, Long> column = new TableColumn<>(
                    clubConfig.financeReportDataList.getAccounts().get(i)
            );
            column.setCellFactory((final TableColumn<FinanceReportData, Long> param) ->
                    new PTableFactory.PCellMoney<>(false));
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getAccountList().get(curCol).getBetrag())

            );
            column.getStyleClass().add("accColumn");
            tableViewSum.getColumns().add(column);
        }

        for (int i = 0; i < clubConfig.financeReportDataList.getCategories().size(); i++) {
            final int curCol = i;
            final TableColumn<FinanceReportData, Long> column = new TableColumn<>(
                    clubConfig.financeReportDataList.getCategories().get(i)
            );
            column.setCellFactory((final TableColumn<FinanceReportData, Long> param) ->
                    new PTableFactory.PCellMoney<>(false));
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getCategoryList().get(curCol).getBetrag())
            );
            column.getStyleClass().add("catColumn");
            tableViewSum.getColumns().add(column);
        }

    }

}
