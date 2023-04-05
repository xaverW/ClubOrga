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

package de.p2tools.cluborga.gui.guiFinance.guiConfig;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.config.prog.ProgIcons;
import de.p2tools.cluborga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.cluborga.data.financeData.categoryData.FinanceCategoryFactory;
import de.p2tools.cluborga.data.financeData.categoryData.FinanceCategoryFieldNames;
import de.p2tools.cluborga.gui.dialog.FinanceCategoryDataDialogController;
import de.p2tools.cluborga.gui.table.ClubTable;
import de.p2tools.cluborga.gui.tools.GuiFactory;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.PTextFieldLong;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.util.Optional;

public class GuiFinanceCategory extends BorderPane {

    private final ScrollPane scrollPaneTable = new ScrollPane();
    private final HBox hBoxSelect = new HBox(10);
    private final VBox vBoxRight = new VBox(10);
    private final VBox vBoxTable = new VBox(0);
    private final TableView<FinanceCategoryData> tableView = new TableView<>();
    private final GridPane gridPane = new GridPane();

    private final PTextFieldLong txtNr = new PTextFieldLong();
    private final TextField txtKategorie = new TextField();
    private final TextArea txtText = new TextArea();

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private final FilteredList<FinanceCategoryData> filteredList;
    private final SortedList<FinanceCategoryData> sortedList;

    private FinanceCategoryData financeCategoryData = null;

    public GuiFinanceCategory(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;

        filteredList = clubConfig.financeCategoryDataList.getFilteredList();
        sortedList = clubConfig.financeCategoryDataList.getSortedList();

        scrollPaneTable.setFitToHeight(true);
        scrollPaneTable.setFitToWidth(true);
        scrollPaneTable.setContent(tableView);
        scrollPaneTable.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(scrollPaneTable, Priority.ALWAYS);
        vBoxTable.getChildren().addAll(scrollPaneTable, hBoxSelect);

        setStyle("-fx-background-color: -fx-background;");

        initCont();
        initTable();
        setDisableAll();
    }

    public void isShown() {
        if (tableView.getSelectionModel().getSelectedItem() == null) {
            tableView.getSelectionModel().selectFirst();
        }
    }

    private void initCont() {
        GuiFactory.setPaneTitle(this, "Kategorien");
        setCenter(vBoxTable);
        setRight(vBoxRight);

        Button btnNew = new Button("");
        btnNew.setGraphic(new ProgIcons().ICON_BUTTON_ADD);
        btnNew.setOnAction(a -> {
            FinanceCategoryData stateData = new FinanceCategoryData(clubConfig);
            if (new FinanceCategoryDataDialogController(clubConfig, stateData).isOk()) {
                clubConfig.financeCategoryDataList.add(stateData);
            }
        });

        Button btnDel = new Button();
        btnDel.setGraphic(new ProgIcons().ICON_BUTTON_REMOVE);
        btnDel.setOnAction(a -> {
            FinanceCategoryData item = tableView.getSelectionModel().getSelectedItem();
            if (item == null) {
                new PAlert().showInfoNoSelection(clubConfig.getStage());
                return;
            }

            if (!clubConfig.financeCategoryDataList.checkRemove(item)) {
                return;
            }

            if (FinanceCategoryFactory.searchCategory(clubConfig, item)) {
                PAlert.showErrorAlert(clubConfig.getStage(), "Kategorie wird noch verwendet",
                        "Die Kategorie wird noch in Finanzdaten verwendet. " +
                                "Bitte zuerst die Finanzdaten ändern.");
                return;
            }

            clubConfig.financeCategoryDataList.remove(item);
        });

        Button btnChange = new Button("");
        btnChange.setGraphic(new ProgIcons().ICON_BUTTON_MEMBER_CHANGE);
        btnChange.setOnAction(a -> changeRate());

        final MenuButton mb = new MenuButton("");
        mb.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU);
        mb.getStyleClass().add("btnFunction");
        mb.setVisible(false);

        vBoxRight.setSpacing(10);
        vBoxRight.setPadding(new Insets(5));
        vBoxRight.setAlignment(Pos.TOP_CENTER);
        vBoxRight.getChildren().add(mb);
        vBoxRight.getChildren().addAll(btnNew, btnDel, btnChange);

        setGrid();
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        new ClubTable(clubConfig).setTable(tableView, ClubTable.TABLE.FINANCE_CATEGORY);

        tableView.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent != null && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                changeRate();
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<FinanceCategoryData> rateData = getSel();
                if (rateData.isPresent()) {
                    ContextMenu contextMenu = getContextMenu(rateData.get());
                    tableView.setContextMenu(contextMenu);
                }
            }
        });
        tableView.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> setInfo());
    }

    private void setGrid() {
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());


        int r = 0;
        gridPane.add(new Label(FinanceCategoryFieldNames.NO_), 0, r);
        gridPane.add(txtNr, 1, r);
        gridPane.add(new Label(FinanceCategoryFieldNames.DATEGORY_), 0, ++r);
        gridPane.add(txtKategorie, 1, r);

        r = 0;
        gridPane.add(new Label(" "), 2, r);
        gridPane.add(new Label(FinanceCategoryFieldNames.DESCRIPTION_), 3, r);
        gridPane.add(txtText, 3, ++r, 2, 2);

        vBoxTable.getChildren().add(gridPane);
    }

    public Optional<FinanceCategoryData> getSel() {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return Optional.empty();
        }
    }

    public void saveTable() {
        new ClubTable(clubConfig).saveTable(tableView, ClubTable.TABLE.FINANCE_CATEGORY);
    }

    private void setInfo() {
        FinanceCategoryData data = tableView.getSelectionModel().getSelectedItem();
        if (this.financeCategoryData != null &&
                data != null &&
                this.financeCategoryData.equals(data)) {
            return;
        }

        unbind();
        this.financeCategoryData = data;
        bind();
    }

    private void changeRate() {
        setInfo();

        FinanceCategoryData data = tableView.getSelectionModel().getSelectedItem();
        if (data == null) {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return;
        }

        if (new FinanceCategoryDataDialogController(clubConfig, financeCategoryData).isOk()) {
            tableView.refresh();
        }
    }

    private void setDisableAll() {
        gridPane.setDisable(financeCategoryData == null);
    }

    private void unbind() {
        setDisableAll();
        if (financeCategoryData == null) {
            return;
        }

        txtNr.unBind();
        txtKategorie.textProperty().unbindBidirectional(financeCategoryData.categoryProperty());
        txtText.textProperty().unbindBidirectional(financeCategoryData.descriptionProperty());
    }

    private void bind() {
        setDisableAll();
        if (financeCategoryData == null) {
            txtNr.setText("");
            txtKategorie.setText("");
            txtText.setText("");
            return;
        }

        txtNr.setStateLabel(financeCategoryData.getId() < FinanceCategoryFactory.CATEGORY_TYPE_SIZE);

        txtNr.bindBidirectional(financeCategoryData.noProperty());
        txtKategorie.textProperty().bindBidirectional(financeCategoryData.categoryProperty());
        txtText.textProperty().bindBidirectional(financeCategoryData.descriptionProperty());
    }

    private ContextMenu getContextMenu(FinanceCategoryData data) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new ClubTable(clubConfig).resetTable(tableView, ClubTable.TABLE.FINANCE_CATEGORY));
        contextMenu.getItems().add(resetTable);

        return contextMenu;
    }

}
