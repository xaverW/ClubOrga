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
import de.p2tools.clubOrga.data.financeData.FinanceReportData;
import de.p2tools.clubOrga.data.financeData.FinanceReportFactory;
import de.p2tools.clubOrga.gui.table.ClubTable;
import de.p2tools.clubOrga.gui.tools.GuiFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public class GuiFinanceReport extends BorderPane {

    private final HBox hBoxSelect = new HBox(10);
    private final VBox vBoxTable = new VBox(0);

    private final TableView<FinanceReportData> tableView = new TableView<>();
    private final TableView<FinanceReportData> tableViewSum = new TableView<>();

    private final ClubConfig clubConfig;

    private final FilteredList<FinanceReportData> filteredList;
    private final SortedList<FinanceReportData> sortedList;
    private final SortedList<FinanceReportData> sortedListSum;

    private final GuiFinanceReportFilterPane guiFinanceReportFilterPane;
    private final GuiFinanceReportMenu guiFinanceReportMenu;

    private boolean markShownAtStartup = false;


    public GuiFinanceReport(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;

        filteredList = clubConfig.financeReportDataList.getFilteredList();
        sortedList = clubConfig.financeReportDataList.getSortedList();
        sortedListSum = clubConfig.financeReportDataListSum.getSortedList();

        guiFinanceReportFilterPane = new GuiFinanceReportFilterPane(clubConfig);
        guiFinanceReportMenu = new GuiFinanceReportMenu(clubConfig, this);

        initCont();
        initSelButton();
        initTable();
        addFilterListener();
    }

    public Optional<FinanceReportData> getSel() {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return Optional.empty();
        }
    }

    public ArrayList<FinanceReportData> getSelList() {
        final ArrayList<FinanceReportData> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
        }
        return ret;
    }

    public void saveTable() {
        new ClubTable(clubConfig).saveTable(tableView, ClubTable.TABLE.FINANCE_REPORT);
    }

    public void resetTable() {
        PLog.sysLog("FinaceReport: reset table");

        new ClubTable(clubConfig).resetTable(tableView, ClubTable.TABLE.FINANCE_REPORT);
        new ClubTable(clubConfig).resetTable(tableViewSum, ClubTable.TABLE.FINANCE_REPORT);
        addSumListener();
    }

    public void isShown() {
        if (tableView.getSelectionModel().getSelectedItem() == null) {
            tableView.getSelectionModel().selectFirst();
        }

        if (createTheDataListsAndIsChanged()) {
            resetTable();
        }
        shownAtStartUp();
    }

    private void shownAtStartUp() {
        if (markShownAtStartup) {
            return;
        }
        markShownAtStartup = true;
        bindTableScrollbars();
    }

    private void bindTableScrollbars() {
        // synchronize scrollbars (must happen after table was made visible)
        ScrollBar sc = findScrollBar(tableView, Orientation.HORIZONTAL);
        ScrollBar scSum = findScrollBar(tableViewSum, Orientation.HORIZONTAL);
        if (sc != null && scSum != null) {
            sc.valueProperty().bindBidirectional(scSum.valueProperty());
        } else {
            PLog.errorLog(945120739, "no scrollbar found");
        }
    }

    private ScrollBar findScrollBar(TableView<?> table, Orientation orientation) {
        // synchronize scrollbars (must happen after table was made visible)
        // this would be the preferred solution, but it doesn't work. it always gives back the vertical scrollbar
        // return (ScrollBar) table.lookup(".scroll-bar:horizontal");

        Set<Node> set = table.lookupAll(".scroll-bar");
        for (Node node : set) {
            ScrollBar bar = (ScrollBar) node;
            if (bar.getOrientation() == orientation) {
                return bar;
            }
        }
        return null;
    }

    private void initCont() {
        GuiFactory.setPaneTitle(this, "Finanzübersicht");
        setStyle("-fx-background-color: -fx-background;");

        VBox.setVgrow(tableView, Priority.ALWAYS);
        vBoxTable.getChildren().addAll(tableView, tableViewSum, hBoxSelect, guiFinanceReportFilterPane);

        setCenter(vBoxTable);
        setRight(guiFinanceReportMenu);
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
            if (getSelList().size() <= 0) {
                return;
            }
            setFilter(true);
        });
        btnClearFilter.setOnAction(a -> guiFinanceReportFilterPane.clearFilter());
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        tableViewSum.setTableMenuButtonVisible(false);
        tableViewSum.setEditable(false);
        tableViewSum.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableViewSum.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableViewSum.setMinHeight(0);
        tableViewSum.setPrefHeight(40);
        tableViewSum.setMaxHeight(40);
        tableViewSum.getStyleClass().add("noheader");
        tableViewSum.getStyleClass().add("sumtable");

        // create the date
        createTheDataListsAndIsChanged();

        // init the table column
        new ClubTable(clubConfig).setTable(tableView, ClubTable.TABLE.FINANCE_REPORT);
        new ClubTable(clubConfig).setTable(tableViewSum, ClubTable.TABLE.FINANCE_REPORT);
        addSumListener();

        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<FinanceReportData> financeReportData = getSel();
                if (financeReportData.isPresent()) {
                    ContextMenu contextMenu = new GuiFinanceReportContextMenu(clubConfig, tableView).
                            getContextMenu(financeReportData.get());
                    tableView.setContextMenu(contextMenu);
                }
            }
        });

        // bind the date with the table
        tableView.setItems(sortedList);
        tableViewSum.setItems(sortedListSum);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        sortedListSum.comparatorProperty().bind(tableViewSum.comparatorProperty());
    }

    private boolean createTheDataListsAndIsChanged() {
        boolean changed = FinanceReportFactory.makeReportData(clubConfig);
        FinanceReportFactory.makeSumReportData(clubConfig);
        return changed;
    }

    private void addSumListener() {
        tableView.getColumns().stream().forEach(c -> c.setReorderable(false));
        tableViewSum.getColumns().stream().forEach(c -> {
            c.setResizable(false);
            c.setReorderable(false);
        });
        for (int i = 0; i < tableView.getColumns().size(); ++i) {
            final int ii = i;
            tableView.getColumns().get(i).widthProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println(tableView.getColumns().get(ii).getPrefWidth());
                tableViewSum.getColumns().get(ii).setPrefWidth(newValue.doubleValue());
            });
        }
        for (int i = 0; i < tableView.getColumns().size(); ++i) {
            final int ii = i;
            tableView.getColumns().get(i).visibleProperty().addListener((observable, oldValue, newValue) ->
                    tableViewSum.getColumns().get(ii).setVisible(newValue));
        }
    }

    private void addFilterListener() {
        clubConfig.financesReportFilterChange.addListener((observable, oldValue, newValue) -> setFilter(false));
        setFilter(false);
    }

    private void setFilter(boolean onlySelected) {
        if (onlySelected) {
            clubConfig.financeReportDataList.clearSelected();
            tableView.getSelectionModel().getSelectedItems().stream()
                    .forEach(financeReportData -> financeReportData.setSelected(true));
        }

        filteredList.setPredicate(FinanceReportFilterPredicate.getProperty(clubConfig, onlySelected));
        FinanceReportFactory.makeSumReportData(clubConfig);
    }

}
