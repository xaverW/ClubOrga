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
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.TransactionData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.gui.dialog.dataDialog.DataDialogController;
import de.p2tools.clubOrga.gui.table.ClubTable;
import de.p2tools.clubOrga.gui.tools.GuiFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Optional;

public class GuiFinance extends BorderPane {

    private final SplitPane splitPane = new SplitPane();
    private final StackPane stackPane = new StackPane();

    private final ScrollPane scrollPaneTable = new ScrollPane();
    private final TabPane tabPane = new TabPane();
    private final HBox hBoxSelect = new HBox(10);
    private final VBox vBoxTable = new VBox(0);
    private final TableView<FinanceData> tableView = new TableView<>();

    Tab tabFilter = new Tab("Filter");
    Tab tabInfo = new Tab("Buchung");
    Tab tabReport = new Tab("Report");

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private final GuiFinanceInfoPaneTrTable guiFinanceInfoPaneTrTable;
    private final GuiFinanceInfoPane guiFinanceInfoPane;
    private final GuiFinanceReportpane guiFinanceReportpane;
    private final GuiFinanceFilterPane financeFilterController;
    private final FilteredList<FinanceData> filteredList;
    private final SortedList<FinanceData> sortedList;

    DoubleProperty doublePropertyCont;


    public GuiFinance(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;

        filteredList = clubConfig.financeDataList.getFilteredList();
        sortedList = clubConfig.financeDataList.getSortedList();

        doublePropertyCont = clubConfig.GUI_PANEL_FINANDES_DIVIDER_CONT;
        guiFinanceInfoPaneTrTable = new GuiFinanceInfoPaneTrTable(clubConfig);
        guiFinanceInfoPane = new GuiFinanceInfoPane(clubConfig);
        guiFinanceReportpane = new GuiFinanceReportpane(clubConfig, filteredList);

        financeFilterController = new GuiFinanceFilterPane(clubConfig);

        addFilterListener();

        scrollPaneTable.setFitToHeight(true);
        scrollPaneTable.setFitToWidth(true);
        scrollPaneTable.setContent(tableView);
        scrollPaneTable.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(scrollPaneTable, Priority.ALWAYS);
        vBoxTable.getChildren().addAll(scrollPaneTable, hBoxSelect);

        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.getItems().addAll(vBoxTable, tabPane);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(doublePropertyCont);

        setStyle("-fx-background-color: -fx-background;");

        initCont();
        initSelButton();
        initTable();
//        PTableFactory.addAutoScroll(tableView);
        // macht Probleme beim "neustart"
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
        guiFinanceReportpane.setFinanceDataChanged();//todo wegen des Startproblems beim zweiten start des selben clubs
    }

    private void initCont() {
        GuiFactory.setPaneTitle(this, "Finanzen");
        setCenter(splitPane);
        setRight(new GuiFinanceMenu(clubConfig, this));

        tabFilter.setTooltip(new Tooltip("damit kann die Tabelle gefiltert werden"));
        tabFilter.setClosable(false);
        tabFilter.setContent(financeFilterController);

        tabInfo.setTooltip(new Tooltip("zeigt Infos zu dem in der Tabelle markierten Eintrag"));
        tabInfo.setClosable(false);
        tabInfo.setContent(stackPane);
        stackPane.getChildren().addAll(guiFinanceInfoPane, guiFinanceInfoPaneTrTable);
//        tabInfo.setContent(guiFinanceInfoPaneTrTable);

        tabReport.setTooltip(new Tooltip("zeigt einen Report von den in der Tabelle angezeigten Finanzen"));
        tabReport.setClosable(false);
        tabReport.setContent(guiFinanceReportpane);

        tabPane.getTabs().addAll(tabFilter, tabInfo, tabReport);
        tabPane.getSelectionModel().select(tabInfo);
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

        btnClearFilter.setOnAction(a -> financeFilterController.clearFilter());
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        new ClubTable(clubConfig).setTable(tableView, ClubTable.TABLE.FINANCE);

        tableView.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent != null && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                changeFinances();
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<FinanceData> financeData = getSel();
                if (financeData.isPresent()) {
                    ContextMenu contextMenu = new GuiFinanceContextMenu(clubConfig, tableView).getContextMenu(financeData.get());
                    tableView.setContextMenu(contextMenu);
                }
            }
        });
        tableView.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> setFinancesInfo());

        clubConfig.financeDataList.sizeProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!clubConfig.financeDataList.isEmpty()) {
                    tableView.getSelectionModel().select(clubConfig.financeDataList.get(clubConfig.financeDataList.size() - 1));
                }
            }
        });
    }

    public Optional<FinanceData> getSel() {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return Optional.empty();
        }
    }

    public ArrayList<FinanceData> getSelList() {
        final ArrayList<FinanceData> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
        }
        return ret;
    }

    public void saveTable() {
        new ClubTable(clubConfig).saveTable(tableView, ClubTable.TABLE.FINANCE);
        guiFinanceInfoPaneTrTable.saveTable();
        guiFinanceReportpane.saveTable();
    }

    public void resetTable() {
        new ClubTable(clubConfig).resetTable(tableView, ClubTable.TABLE.FINANCE);
    }

    public void setFinancesInfo() {
        FinanceData financeData = tableView.getSelectionModel().getSelectedItem();
        guiFinanceInfoPaneTrTable.setFinanceData(financeData);
        guiFinanceInfoPane.setFinanceData(financeData);

        if (financeData == null || financeData.getTransactionDataList().size() <= 1) {
            guiFinanceInfoPaneTrTable.setFinanceData(null);
            guiFinanceInfoPane.setFinanceData(financeData);
            guiFinanceInfoPane.toFront();
        } else {
            guiFinanceInfoPane.setFinanceData(null);
            guiFinanceInfoPaneTrTable.setFinanceData(financeData);
            guiFinanceInfoPaneTrTable.toFront();
        }
    }

    public void changeFinances() {
        setFinancesInfo();

        FinanceData financeData = tableView.getSelectionModel().getSelectedItem();
        if (financeData == null) {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return;
        }

        MemberData memberData = null;
        FeeData feeData = null;
        TransactionData transactionData = null;

        if (financeData.getTransactionDataList().size() == 1) {
            feeData = financeData.getTransactionDataList().get(0).getFeeData();
            memberData = feeData == null ? null : feeData.getMemberData();
            transactionData = financeData.getTransactionDataList().get(0);
        }
        if (new DataDialogController(clubConfig, DataDialogController.OPEN.FINANCE_PANE,
                memberData, feeData, financeData, transactionData).isOk()) {
            tableView.refresh();
        }
        guiFinanceReportpane.setFinanceDataChanged();
    }

    private void setFilter(boolean onlySelected) {
        if (onlySelected) {
            clubConfig.financeDataList.clearSelected();
            tableView.getSelectionModel().getSelectedItems().stream().forEach(financeData -> financeData.setSelected(true));
        }

        filteredList.setPredicate(FinanceFilterPredicate.getFinanceProperty(clubConfig, onlySelected));
        boolean filtered = FinanceFilterPredicate.getFiltered();
        if (filtered) {
            if (!tabFilter.getStyleClass().contains("filterTabStyle")) {
                tabFilter.getStyleClass().add("filterTabStyle");
            }
        } else {
            tabFilter.getStyleClass().remove("filterTabStyle");
        }

//        guiFinanceReportpane.setFinanceDataChanged();
    }

    private void addFilterListener() {
        clubConfig.financesFilterChange.addListener((observable, oldValue, newValue) -> setFilter(false));
        setFilter(false);
    }
}
