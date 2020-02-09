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

package de.p2tools.clubOrga.gui.guiMember.guiConfig;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateFactory;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateFieldNames;
import de.p2tools.clubOrga.gui.dialog.FeeRateDataDialogController;
import de.p2tools.clubOrga.gui.table.ClubTable;
import de.p2tools.clubOrga.gui.tools.GuiFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PTextFieldLong;
import de.p2tools.p2Lib.guiTools.PTextFieldMoney;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class GuiMemberFeeRate extends BorderPane {

    private final ScrollPane scrollPaneTable = new ScrollPane();
    private final VBox vBoxRight = new VBox();
    private final VBox vBoxTable = new VBox();
    private final TableView<FeeRateData> tableView = new TableView<>();
    private final GridPane gridPane = new GridPane();

    private final PTextFieldLong txtNr = new PTextFieldLong();
    private final TextField txtName = new TextField();
    private final TextArea txtText = new TextArea();
    private final PTextFieldMoney txtBetrag = new PTextFieldMoney();

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private final FilteredList<FeeRateData> filteredList;
    private final SortedList<FeeRateData> sortedList;

    private FeeRateData feeRateData = null;

    public GuiMemberFeeRate(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;

        filteredList = clubConfig.feeRateDataList.getFilteredList();
        sortedList = clubConfig.feeRateDataList.getSortedList();

        scrollPaneTable.setFitToHeight(true);
        scrollPaneTable.setFitToWidth(true);
        scrollPaneTable.setContent(tableView);
        scrollPaneTable.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(scrollPaneTable, Priority.ALWAYS);
        vBoxTable.getChildren().addAll(scrollPaneTable);

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
        GuiFactory.setPaneTitle(this, "Beitragssätze");
        setCenter(vBoxTable);
        setRight(vBoxRight);

        Button btnNew = new Button("");
        btnNew.setGraphic(new ProgIcons().ICON_BUTTON_ADD);
        btnNew.setOnAction(a -> {
            FeeRateData feeRateData = new FeeRateData(clubConfig);
            if (new FeeRateDataDialogController(clubConfig, feeRateData).isOk()) {
                clubConfig.feeRateDataList.add(feeRateData);
            }
        });

        Button btnDel = new Button();
        btnDel.setGraphic(new ProgIcons().ICON_BUTTON_REMOVE);
        btnDel.setOnAction(a -> {
            FeeRateData item = tableView.getSelectionModel().getSelectedItem();
            if (item == null) {
                new PAlert().showInfoNoSelection(clubConfig.getStage());

            } else {
                clubConfig.feeRateDataList.remove(item);
            }
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

        new ClubTable(clubConfig).setTable(tableView, ClubTable.TABLE.MEMBER_FEE);

        tableView.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent != null && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                changeRate();
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<FeeRateData> rateData = getSel();
                if (rateData.isPresent()) {
                    ContextMenu contextMenu = getContextMenu(rateData.get());
                    tableView.setContextMenu(contextMenu);
                }
            }
        });
        tableView.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> setMemberInfo());
    }

    private void setGrid() {

        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());


        int r = 0;
        gridPane.add(new Label(FeeRateFieldNames.NR_), 0, r);
        gridPane.add(txtNr, 1, r);
        gridPane.add(new Label(FeeRateFieldNames.NAME_), 0, ++r);
        gridPane.add(txtName, 1, r);
        gridPane.add(new Label(FeeRateFieldNames.BETRAG_), 0, ++r);
        gridPane.add(txtBetrag, 1, r);

        r = 0;
        gridPane.add(new Label(" "), 2, r);
        gridPane.add(new Label(FeeRateFieldNames.DESCRIPTION_), 3, r);
        gridPane.add(txtText, 3, ++r, 1, 3);

        vBoxTable.getChildren().add(gridPane);
    }

    public Optional<FeeRateData> getSel() {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return Optional.empty();
        }
    }

    public void saveTable() {
        new ClubTable(clubConfig).saveTable(tableView, ClubTable.TABLE.MEMBER_FEE);
    }

    private void setMemberInfo() {
        FeeRateData feeRateData = tableView.getSelectionModel().getSelectedItem();
        if (this.feeRateData != null &&
                feeRateData != null &&
                this.feeRateData.equals(feeRateData)) {
            return;
        }

        unbind();
        this.feeRateData = feeRateData;
        bind();
    }

    private void changeRate() {
        setMemberInfo();

        FeeRateData feeRateData = tableView.getSelectionModel().getSelectedItem();
        if (feeRateData == null) {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return;
        }

        if (new FeeRateDataDialogController(clubConfig, feeRateData).isOk()) {
            long id = feeRateData.getId();
            if (id != FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getId() &&
                    id != FeeRateFactory.RATE_TYPE.RATE_FREE.getId()) {
                // sonst ist der Beitrag entweder 0 oder frei
                clubConfig.memberDataList.stream().filter(m -> m.getFeeRateData().getId() == id).forEach(m -> {
                    m.setBeitrag(feeRateData.getBetrag());
                });
            }

            tableView.refresh();
        }
    }

    private void setDisableAll() {
        gridPane.setDisable(feeRateData == null);
    }

    private void unbind() {
        setDisableAll();
        if (feeRateData == null) {
            return;
        }

        txtNr.unBind();
        txtName.textProperty().unbindBidirectional(feeRateData.nameProperty());
        txtBetrag.unBind();
        txtText.textProperty().unbindBidirectional(feeRateData.textProperty());
    }

    private void bind() {
        setDisableAll();
        if (feeRateData == null) {
            txtNr.setText("");
            txtName.setText("");
            txtBetrag.setText("");
            txtText.setText("");
            return;
        }


        txtNr.setStateLabel(feeRateData.getId() < FeeRateFactory.RATA_TYPE_SIZE);
        txtBetrag.setLabelLike(feeRateData.getId() == FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getId());

        txtNr.bindBidirectional(feeRateData.nrProperty());
        txtName.textProperty().bindBidirectional(feeRateData.nameProperty());
        txtBetrag.bindBidirectional(feeRateData.betragProperty());
        txtText.textProperty().bindBidirectional(feeRateData.textProperty());
    }

    private ContextMenu getContextMenu(FeeRateData data) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new ClubTable(clubConfig).resetTable(tableView, ClubTable.TABLE.MEMBER_FEE));
        contextMenu.getItems().add(resetTable);

        return contextMenu;
    }

}
