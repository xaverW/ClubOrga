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
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateFieldNames;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeFactory;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeNames;
import de.p2tools.clubOrga.gui.dialog.PaymentTypeDataDialogController;
import de.p2tools.clubOrga.gui.table.ClubTable;
import de.p2tools.clubOrga.gui.tools.GuiFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBoxObject;
import de.p2tools.p2Lib.guiTools.PTextFieldLong;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.util.Optional;

public class GuiMemberPaymentType extends BorderPane {

    private final ScrollPane scrollPaneTable = new ScrollPane();
    private final HBox hBoxSelect = new HBox(10);
    private final VBox vBoxRight = new VBox(10);
    private final VBox vBoxTable = new VBox(0);
    private final TableView<PaymentTypeData> tableView = new TableView<>();
    private final GridPane gridPane = new GridPane();

    private final PTextFieldLong txtNr = new PTextFieldLong();
    private final TextField txtName = new TextField();
    private final TextArea txtText = new TextArea();
    private final PComboBoxObject<FinanceAccountData> cboFinanceAccountData = new PComboBoxObject<>();
    private final PToggleSwitch tglEinzug = new PToggleSwitch();

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private final FilteredList<PaymentTypeData> filteredList;
    private final SortedList<PaymentTypeData> sortedList;

    private PaymentTypeData paymentTypeData = null;

    public GuiMemberPaymentType(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;

        filteredList = clubConfig.paymentTypeDataList.getFilteredList();
        sortedList = clubConfig.paymentTypeDataList.getSortedList();

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
        GuiFactory.setPaneTitle(this, "Zahlarten");
        setCenter(vBoxTable);
        setRight(vBoxRight);

        Button btnNew = new Button("");
        btnNew.setGraphic(new ProgIcons().ICON_BUTTON_ADD);
        btnNew.setOnAction(a -> {
            PaymentTypeData data = new PaymentTypeData(clubConfig);
            if (new PaymentTypeDataDialogController(clubConfig, data).isOk()) {
                clubConfig.paymentTypeDataList.add(data);
            }
        });

        Button btnDel = new Button();
        btnDel.setGraphic(new ProgIcons().ICON_BUTTON_REMOVE);
        btnDel.setOnAction(a -> {
            PaymentTypeData item = tableView.getSelectionModel().getSelectedItem();
            if (item == null) {
                new PAlert().showInfoNoSelection(clubConfig.getStage());
            } else {
                clubConfig.paymentTypeDataList.remove(item);
            }
        });

        Button btnChange = new Button("");
        btnChange.setGraphic(new ProgIcons().ICON_BUTTON_MEMBER_CHANGE);
        btnChange.setOnAction(a -> changePaymentType());

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

        new ClubTable(clubConfig).setTable(tableView, ClubTable.TABLE.MEMBER_PAYMENT_TYPE);

        tableView.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent != null && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                changePaymentType();
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<PaymentTypeData> rateData = getSel();
                if (rateData.isPresent()) {
                    ContextMenu contextMenu = getContextMenu(rateData.get());
                    tableView.setContextMenu(contextMenu);
                }
            }
        });
        tableView.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> setPaymentType());
    }

    private void setGrid() {
        cboFinanceAccountData.init(clubConfig.financeAccountDataList, null);
        cboFinanceAccountData.setMaxWidth(Double.MAX_VALUE);
        cboFinanceAccountData.setOnAction(a -> {
            if (this.paymentTypeData == null) {
                return;
            }

            FinanceAccountData financeAccountData = cboFinanceAccountData.getSelValue();
            if (financeAccountData == null) {
                return;
            }

            this.paymentTypeData.setKonto(financeAccountData.getId());
            this.paymentTypeData.setFinanceAccountData(financeAccountData);
            tglEinzug.setDisable(!financeAccountData.isGiro());
            if (!financeAccountData.isGiro()) {
                this.paymentTypeData.setEinzug(false);
            }
        });

        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());


        int r = 0;
        gridPane.add(new Label(PaymentTypeNames.NR_), 0, r);
        gridPane.add(txtNr, 1, r);
        gridPane.add(new Label(PaymentTypeNames.NAME_), 0, ++r);
        gridPane.add(txtName, 1, r);
        gridPane.add(new Label(PaymentTypeNames.KONTO_), 0, ++r);
        gridPane.add(cboFinanceAccountData, 1, r);
        gridPane.add(new Label(PaymentTypeNames.EINZUG_), 0, ++r);
        gridPane.add(tglEinzug, 1, r);

        r = 0;
        txtText.setPrefRowCount(3);
        gridPane.add(new Label(" "), 2, r);
        gridPane.add(new Label(FeeRateFieldNames.DESCRIPTION_), 3, r);
        gridPane.add(txtText, 3, ++r, 1, 3);

        vBoxTable.getChildren().add(gridPane);
    }

    public Optional<PaymentTypeData> getSel() {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return Optional.empty();
        }
    }

    public void saveTable() {
        new ClubTable(clubConfig).saveTable(tableView, ClubTable.TABLE.MEMBER_PAYMENT_TYPE);
    }

    private void setPaymentType() {
        PaymentTypeData selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (this.paymentTypeData != null &&
                selectedItem != null &&
                this.paymentTypeData.equals(selectedItem)) {
            return;
        }

        unbind();
        this.paymentTypeData = selectedItem;
        bind();
    }

    private void changePaymentType() {
        setPaymentType();

        PaymentTypeData item = tableView.getSelectionModel().getSelectedItem();
        if (item == null) {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return;
        }

        if (new PaymentTypeDataDialogController(clubConfig, paymentTypeData).isOk()) {
            tableView.refresh();
        }
    }

    private void setDisableAll() {
        gridPane.setDisable(paymentTypeData == null);
    }


    private void unbind() {
        setDisableAll();
        if (paymentTypeData == null) {
            return;
        }

        txtNr.unBind();
        txtName.textProperty().unbindBidirectional(paymentTypeData.nameProperty());
        txtText.textProperty().unbindBidirectional(paymentTypeData.textProperty());
        cboFinanceAccountData.unbindSelValueProperty();
        tglEinzug.selectedProperty().unbindBidirectional(paymentTypeData.einzugProperty());
    }

    private void bind() {
        setDisableAll();
        if (paymentTypeData == null) {
            txtNr.setText("");
            txtName.setText("");
            txtText.setText("");
            cboFinanceAccountData.unbindSelValueProperty();
            tglEinzug.setSelected(false);
            return;
        }


        txtNr.setStateLabel(paymentTypeData.getId() < PaymentTypeFactory.PAYMENT_TYPE_SIZE);
        cboFinanceAccountData.setDisable(paymentTypeData.getId() < PaymentTypeFactory.PAYMENT_TYPE_SIZE);

        txtNr.bindBidirectional(paymentTypeData.nrProperty());
        txtName.textProperty().bindBidirectional(paymentTypeData.nameProperty());
        txtText.textProperty().bindBidirectional(paymentTypeData.textProperty());
        cboFinanceAccountData.bindSelValueProperty(paymentTypeData.financeAccountDataProperty());
        tglEinzug.selectedProperty().bindBidirectional(paymentTypeData.einzugProperty());
        tglEinzug.setDisable(paymentTypeData.getId() < PaymentTypeFactory.PAYMENT_TYPE_SIZE ||
                !paymentTypeData.getFinanceAccountData().isGiro());
    }

    private ContextMenu getContextMenu(PaymentTypeData data) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new ClubTable(clubConfig).resetTable(tableView, ClubTable.TABLE.MEMBER_STATE));
        contextMenu.getItems().add(resetTable);

        return contextMenu;
    }

}
