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

package de.p2tools.clubOrga.gui.guiFinance;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.clubOrga.data.financeData.FinanceReportData;
import de.p2tools.clubOrga.data.financeData.FinanceReportDataList;
import de.p2tools.clubOrga.data.financeData.FinanceReportFactory;
import de.p2tools.clubOrga.gui.tools.GuiFactory;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import javafx.beans.property.ReadOnlyObjectWrapper;
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

    private final ScrollPane scrollPaneTable = new ScrollPane();
    private final HBox hBoxSelect = new HBox(10);
    private final VBox vBoxTable = new VBox(0);
    private final TableView<FinanceReportData> tableView = new TableView<>();

    private final ClubConfig clubConfig;

    private final FinanceReportDataList financeReportDataList;
    private final FilteredList<FinanceReportData> filteredList;
    private final SortedList<FinanceReportData> sortedList;

    private final GuiFinanceReportFilterPane reportFilterPane;
    private final GuiFinanceReportMenu reportMenu;


    public GuiFinanceReport(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;

        financeReportDataList = new FinanceReportDataList(clubConfig);
        filteredList = new FilteredList<>(financeReportDataList, p -> true);
        sortedList = new SortedList<>(filteredList);

        reportFilterPane = new GuiFinanceReportFilterPane(clubConfig);
        reportMenu = new GuiFinanceReportMenu(clubConfig, this);

        addFilterListener();

        scrollPaneTable.setFitToHeight(true);
        scrollPaneTable.setFitToWidth(true);
        scrollPaneTable.setContent(tableView);
        scrollPaneTable.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(scrollPaneTable, Priority.ALWAYS);
        vBoxTable.getChildren().addAll(scrollPaneTable, hBoxSelect);

        setStyle("-fx-background-color: -fx-background;");

        initCont();
        initSelButton();
        initTable();
    }

    public FinanceReportDataList getFinanceReportDataList() {
        return financeReportDataList;
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
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        initColumn(tableView);
        tableView.setItems(sortedList);
    }

    private void setFilter(boolean onlySelected) {
//        if (onlySelected) {
//            clubConfig.financeDataList.clearSelected();
//            tableView.getSelectionModel().getSelectedItems().stream().forEach(financeData -> financeData.setSelected(true));
//        }

//        filteredList.setPredicate(FinanceFilterPredicate.getFinanceProperty(clubConfig, onlySelected));
        boolean filtered = FinanceFilterPredicate.getFiltered();
    }

    private void addFilterListener() {
//        clubConfig.financesFilterChange.addListener((observable, oldValue, newValue) -> setFilter(false));
        setFilter(false);
    }

    private void initColumn(TableView table) {
        FinanceReportFactory.makeReportData(clubConfig, financeReportDataList);

        final TableColumn<FinanceReportData, String> belegNrColumn = new TableColumn<>(FinanceFieldNames.BELEG_NR);
        belegNrColumn.setCellValueFactory(new PropertyValueFactory<>("belegNr"));

        final TableColumn<FinanceReportData, Long> gesamtbetragColumn = new TableColumn<>(FinanceFieldNames.GESAMTBETRAG);
        gesamtbetragColumn.setCellValueFactory(new PropertyValueFactory<>("gesamtbetrag"));
        gesamtbetragColumn.setCellFactory((final TableColumn<FinanceReportData, Long> param) ->
                new PTableFactory.PCellMoney<>());

        table.getColumns().add(belegNrColumn);
        table.getColumns().add(gesamtbetragColumn);

        for (int i = 0; i < financeReportDataList.getAccounts().size(); i++) {
            final int curCol = i;
            final TableColumn<FinanceReportData, Long> column = new TableColumn<>(
                    financeReportDataList.getAccounts().get(i)
            );
            column.setCellFactory((final TableColumn<FinanceReportData, Long> param) ->
                    new PTableFactory.PCellMoney<>(false));
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getAccountList().get(curCol))
            );
            tableView.getColumns().add(column);
        }

        for (int i = 0; i < financeReportDataList.getCategories().size(); i++) {
            final int curCol = i;
            final TableColumn<FinanceReportData, Long> column = new TableColumn<>(
                    financeReportDataList.getCategories().get(i)
            );
            column.setCellFactory((final TableColumn<FinanceReportData, Long> param) ->
                    new PTableFactory.PCellMoney<>(false));
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getCategoryList().get(curCol))
            );
            tableView.getColumns().add(column);
        }


    }

}
