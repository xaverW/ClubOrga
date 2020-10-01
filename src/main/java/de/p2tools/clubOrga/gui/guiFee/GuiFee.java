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

package de.p2tools.clubOrga.gui.guiFee;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.gui.dialog.dataDialog.DataDialogController;
import de.p2tools.clubOrga.gui.table.ClubTable;
import de.p2tools.clubOrga.gui.tools.GuiFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import javafx.beans.property.DoubleProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

public class GuiFee extends BorderPane {

    private final SplitPane splitPane = new SplitPane();

    private final ScrollPane scrollPaneTable = new ScrollPane();
    private final TabPane tabPane = new TabPane();
    private final HBox hBoxSelect = new HBox(10);
    private final VBox vBoxTable = new VBox(0);
    private final TableView<FeeData> tableView = new TableView<>();

    private final Tab tabFilter = new Tab("Filter");
    private final Tab tabInfo = new Tab("Beitrag");

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private final GuiFeeInfoPane guiFeeInfoPane;
    private final GuiFeeFilterPane feeFilterController;
    private final FilteredList<FeeData> filteredList;
    private final SortedList<FeeData> sortedList;

    DoubleProperty doublePropertyCont;


    public GuiFee(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;

        doublePropertyCont = clubConfig.GUI_PANEL_FEE_DIVIDER_CONT;
        guiFeeInfoPane = new GuiFeeInfoPane(clubConfig);
        feeFilterController = new GuiFeeFilterPane(clubConfig);

        filteredList = clubConfig.feeDataList.getFilteredList();
        sortedList = clubConfig.feeDataList.getSortedList();
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
        GuiFactory.setPaneTitle(this, "Beiträge");
        setCenter(splitPane);
        setRight(new GuiFeeMenu(clubConfig, this));

        tabFilter.setTooltip(new Tooltip("damit kann die Tabelle gefiltert werden"));
        tabFilter.setClosable(false);
        tabFilter.setContent(feeFilterController);

        tabInfo.setTooltip(new Tooltip("zeigt Infos zu dem in der Tabelle markierten Eintrag"));
        tabInfo.setClosable(false);
        tabInfo.setContent(guiFeeInfoPane);

        tabPane.getTabs().addAll(tabFilter, tabInfo);
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

        btnClearFilter.setOnAction(a -> feeFilterController.clearFilter());
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        new ClubTable(clubConfig).setTable(tableView, ClubTable.TABLE.FEE);

        tableView.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent != null && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                changeFee();
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<FeeData> feeData = getSel();
                if (feeData.isPresent()) {
                    ContextMenu contextMenu = new GuiFeeContextMenu(clubConfig, tableView).getContextMenu(feeData.get());
                    tableView.setContextMenu(contextMenu);
                }
            }
        });
        tableView.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> setFeeInfo());
    }

    public Optional<FeeData> getSel() {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return Optional.empty();
        }
    }

    public ArrayList<FeeData> getSelList() {
        final ArrayList<FeeData> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
        }
        return ret;
    }

//    public void updateTable() {
//        tableView.refresh();
//    }

    public void saveTable() {
        new ClubTable(clubConfig).saveTable(tableView, ClubTable.TABLE.FEE);
    }

    public void resetTable() {
        new ClubTable(clubConfig).resetTable(tableView, ClubTable.TABLE.FEE);
    }

    private void setFeeInfo() {
        FeeData feeData = tableView.getSelectionModel().getSelectedItem();
        guiFeeInfoPane.setFee(feeData);
    }

    public void changeFee() {
        setFeeInfo();
        final Optional<FeeData> feeDataOpt = getSel();
        if (!feeDataOpt.isPresent()) {
            return;
        }

        final FeeData feeData = feeDataOpt.get();
        if (new DataDialogController(clubConfig, feeData).isOk()) {
            tableView.refresh();
            updateFilteredList();
        }
    }

    public void updateFilteredList() {
        setFilter(false);
    }

    private void setFilter(boolean onlySelected) {
        if (onlySelected) {
            clubConfig.feeDataList.clearSelected();
            tableView.getSelectionModel().getSelectedItems().stream().forEach(feeData -> feeData.setSelected(true));
        }

        filteredList.setPredicate(FeeFilterPredicate.getFeePredicate(clubConfig, onlySelected));
        boolean filtered = FeeFilterPredicate.getFiltered();
        if (filtered) {
            if (!tabFilter.getStyleClass().contains("filterTabStyle")) {
                tabFilter.getStyleClass().add("filterTabStyle");
            }
        } else {
            tabFilter.getStyleClass().remove("filterTabStyle");
        }
    }

    private void addFilterListener() {
        clubConfig.feeFilterChange.addListener((observable, oldValue, newValue) -> setFilter(false));
        setFilter(false);
    }
}
