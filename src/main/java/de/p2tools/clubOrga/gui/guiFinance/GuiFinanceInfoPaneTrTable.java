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

package de.p2tools.clubOrga.gui.guiFinance;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.clubOrga.data.financeData.TransactionData;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.clubOrga.gui.dialog.dataDialog.DataDialogController;
import de.p2tools.clubOrga.gui.table.ClubTable;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.*;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuiFinanceInfoPaneTrTable extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final ScrollPane scrollPaneLeft = new ScrollPane();
    private final ScrollPane scrollPaneRight = new ScrollPane();

    private final VBox vbLeft = new VBox();
    private final VBox vbRight = new VBox();
    private final VBox vbTable = new VBox();

    private final Label lblFinance = new Label("Finanzdaten");
    private final Label lblTransactionList = new Label("Transaktionen");
    private final Label lblTransaction = new Label("Transaktionsdaten");

    // FinanceData
    private final PTextFieldLong txtFinanceNr = new PTextFieldLong();
    private final TextField txtFinanceBelegNr = new TextField();
    private final PYearPicker pYearPickerFinanceGeschaeftsjahr = new PYearPicker();
    private final PTextFieldMoney txtFinanceBetrag = new PTextFieldMoney(true);
    private final PLDatePropertyPicker pdpFinanceBuchungsdatum = new PLDatePropertyPicker();
    private final PComboBoxObject<FinanceAccountData> cboAccount = new PComboBoxObject<>();
    private final TextArea txtFinanceText = new TextArea();

    // TransactionData
    private final ScrollPane scrollPaneTable = new ScrollPane();
    private final TableView<TransactionData> tableView = new TableView<>();
    private final PTextFieldLong txtTransactionNr = new PTextFieldLong();
    private final PTextFieldMoney txtTransactionBetrag = new PTextFieldMoney();
    private final PComboBoxObject<FinanceCategoryData> cboCategory = new PComboBoxObject<>();
    private final TextArea txtTransactionText = new TextArea();

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private FinanceData financeData = null;
    private TransactionData transactionData = null;

    DoubleProperty doublePropertyInfo_0;
    DoubleProperty doublePropertyInfo_1;

    public GuiFinanceInfoPaneTrTable(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
        progData = ProgData.getInstance();

        doublePropertyInfo_0 = clubConfig.GUI_PANEL_FINANCES_DIVIDER_INFO_0;
        doublePropertyInfo_1 = clubConfig.GUI_PANEL_FINANCES_DIVIDER_INFO_1;

        getChildren().addAll(splitPane);
        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.getItems().addAll(scrollPaneLeft, vbTable, scrollPaneRight);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(doublePropertyInfo_0);
        splitPane.getDividers().get(1).positionProperty().bindBidirectional(doublePropertyInfo_1);

        //=====================
        // Left
        scrollPaneLeft.setFitToHeight(true);
        scrollPaneLeft.setFitToWidth(true);

        HBox hb = new HBox();
        hb.getChildren().add(lblFinance);
        hb.setAlignment(Pos.CENTER);
        lblFinance.getStyleClass().add("headerLabel");
        lblFinance.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblFinance, Priority.ALWAYS);
        lblFinance.setAlignment(Pos.CENTER);
        vbLeft.getChildren().add(hb);

        vbLeft.setPadding(new Insets(10));
        vbLeft.setSpacing(10);
        scrollPaneLeft.setContent(vbLeft);

        //=====================
        // Table
        hb = new HBox();
        hb.getChildren().add(lblTransactionList);
        hb.setAlignment(Pos.CENTER);
        lblTransactionList.getStyleClass().add("headerLabel");
        lblTransactionList.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblTransactionList, Priority.ALWAYS);
        lblTransactionList.setAlignment(Pos.CENTER);
        vbTable.getChildren().add(hb);

        scrollPaneTable.setFitToHeight(true);
        scrollPaneTable.setFitToWidth(true);
        scrollPaneTable.setContent(tableView);

        vbTable.setPadding(new Insets(10));
        vbTable.setSpacing(10);
        VBox.setVgrow(scrollPaneTable, Priority.ALWAYS);
        vbTable.getChildren().addAll(scrollPaneTable);

        //=====================
        // Right
        scrollPaneRight.setFitToHeight(true);
        scrollPaneRight.setFitToWidth(true);

        hb = new HBox();
        hb.getChildren().add(lblTransaction);
        hb.setAlignment(Pos.CENTER);
        lblTransaction.getStyleClass().add("headerLabel");
        lblTransaction.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblTransaction, Priority.ALWAYS);
        lblTransaction.setAlignment(Pos.CENTER);
        vbRight.getChildren().add(hb);

        vbRight.setPadding(new Insets(10));
        vbRight.setSpacing(10);
        scrollPaneRight.setContent(vbRight);


        initInfoLeft();
        initTransactionTable();
        initInfoRight();
        setDisableAll();
    }

    public void saveTable() {
        new ClubTable(clubConfig).saveTable(tableView, ClubTable.TABLE.TRANSACTION);
    }

    public Optional<TransactionData> getSel() {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return Optional.empty();
        }
    }

    public ArrayList<TransactionData> getSelList() {
        final ArrayList<TransactionData> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
        }
        return ret;
    }

    private void showDialog() {
        TransactionData transactionData = tableView.getSelectionModel().getSelectedItem();
        int no = tableView.getSelectionModel().getSelectedIndex();
        if (financeData == null || transactionData == null) {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return;
        }

        if (new DataDialogController(clubConfig, DataDialogController.OPEN.TRANSACTION_PANE,
                financeData, no).isOk()) {
            tableView.refresh();
        }
    }

    private void initInfoLeft() {
        cboAccount.init(clubConfig.financeAccountDataList, null);
        cboAccount.setMaxWidth(Double.MAX_VALUE);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;

        pYearPickerFinanceGeschaeftsjahr.setMaxWidth(Double.MAX_VALUE);
        pdpFinanceBuchungsdatum.setMaxWidth(Double.MAX_VALUE);

        gridPane.add(new Label(FinanceFieldNames.BELEG_NR_), 0, row);
        gridPane.add(txtFinanceBelegNr, 1, row);

        gridPane.add(new Label(FinanceFieldNames.GESCHAEFTSJAHR_), 0, ++row);
        gridPane.add(pYearPickerFinanceGeschaeftsjahr, 1, row);

        gridPane.add(new Label(FinanceFieldNames.GESAMTBETRAG_), 0, ++row);
        gridPane.add(txtFinanceBetrag, 1, row);

        gridPane.add(new Label(FinanceFieldNames.BUCHUNGSDATUM_), 0, ++row);
        gridPane.add(pdpFinanceBuchungsdatum, 1, row);

        gridPane.add(new Label(FinanceFieldNames.KONTO_), 0, ++row);
        gridPane.add(cboAccount, 1, row);

        gridPane.add(new Label(FinanceFieldNames.TEXT_), 0, ++row);
        gridPane.add(txtFinanceText, 1, row);

        GridPane.setVgrow(txtFinanceText, Priority.ALWAYS);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        vbLeft.getChildren().addAll(gridPane);
    }

    private void initTransactionTable() {
        Button btnNew = new Button("");
        btnNew.setTooltip(new Tooltip("eine neue Transaktion hinzufügen"));
        btnNew.setGraphic(new ProgIcons().ICON_BUTTON_ADD);
        btnNew.setOnAction(a -> {
            if (financeData != null) {
                TransactionData transactionData =
                        new TransactionData(financeData.getTransactionDataList().getNextNr(), clubConfig);

                TransactionData tr = financeData.getTransactionDataList().getLast();
                if (tr != null) {
                    transactionData.setFinanceCategoryData(tr.getFinanceCategoryData());
                }

                financeData.getTransactionDataList().add(transactionData);
                tableView.getSelectionModel().select(transactionData);
            }
        });

        Button btnDel = new Button();
        btnDel.setTooltip(new Tooltip("markierte Transaktion löschen"));
        btnDel.setGraphic(new ProgIcons().ICON_BUTTON_REMOVE);
        btnDel.setOnAction(a -> {
            List<TransactionData> transactionData = getSelList();
            if (transactionData.isEmpty()) {
                return;
            }

            int i = financeData.getTransactionDataList().size();
            if (i - transactionData.size() < 1) {
                PAlert.showErrorAlert(clubConfig.getStage(), "Transaktion löschen",
                        "Es können nicht alle Transaktionen gelöscht werden, " +
                                "mindestens eine muss erhalten bleiben");
                return;
            }

            financeData.financeDataRemoveTransactions(clubConfig.getStage(), transactionData);
            if (financeData.getTransactionDataList().size() == 1) {
                clubConfig.guiFinance.setFinancesInfo();
            }
        });

        HBox hBoxBtn = new HBox(10);
        hBoxBtn.setAlignment(Pos.CENTER_RIGHT);
        hBoxBtn.getChildren().addAll(btnDel, btnNew);
        vbTable.getChildren().add(hBoxBtn);

        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent != null && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                showDialog();
            }
        });

        new ClubTable(clubConfig).setTable(tableView, ClubTable.TABLE.TRANSACTION);
        new ClubTable(clubConfig).addResetMenue(tableView, ClubTable.TABLE.TRANSACTION);

        tableView.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> setTransactionData());

    }

    private void initInfoRight() {
        cboCategory.init(clubConfig.financeCategoryDataList, null);
        cboCategory.setMaxWidth(Double.MAX_VALUE);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;

        gridPane.add(new Label(FinanceFieldNames.Nr_), 0, row);
        gridPane.add(txtTransactionNr, 1, row);

        gridPane.add(new Label(FinanceFieldNames.BETRAG_), 0, ++row);
        gridPane.add(txtTransactionBetrag, 1, row);

        gridPane.add(new Label(FinanceFieldNames.KATEGORIE_), 0, ++row);
        gridPane.add(cboCategory, 1, row);

        gridPane.add(new Label(FinanceFieldNames.TEXT_), 0, ++row);
        gridPane.add(txtTransactionText, 1, row);

        GridPane.setVgrow(txtTransactionText, Priority.ALWAYS);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        vbRight.getChildren().add(gridPane);
    }

    public void setFinanceData(FinanceData financeData) {
        if (this.financeData != null &&
                financeData != null &&
                this.financeData.equals(financeData)) {
            return;
        }

        unbindFinanceData();
        this.financeData = financeData;
        bindFinanceData();
    }

    private void setDisableAll() {
        vbLeft.setDisable(financeData == null);
        vbTable.setDisable(financeData == null);
        vbRight.setDisable(transactionData == null);
    }

    private void bindFinanceData() {
        setDisableAll();
        if (financeData == null) {
            txtFinanceNr.setText("");
            txtFinanceBelegNr.setText("");
            txtFinanceBetrag.setText("");
            txtFinanceText.setText("");
            pYearPickerFinanceGeschaeftsjahr.unbind();
            cboAccount.unbindSelValueProperty();
            pdpFinanceBuchungsdatum.unbindPDateProperty();
            return;
        }

        txtFinanceNr.bindBidirectional(financeData.noProperty());
        txtFinanceBelegNr.textProperty().bindBidirectional(financeData.receiptNoProperty());
        txtFinanceBetrag.bindBidirectional(financeData.gesamtbetragProperty());
        txtFinanceText.textProperty().bindBidirectional(financeData.textProperty());
        pYearPickerFinanceGeschaeftsjahr.bindBidirectional(financeData.geschaeftsJahrProperty());
        pdpFinanceBuchungsdatum.bindPDateProperty(financeData.buchungsDatumProperty());
        cboAccount.bindSelValueProperty(financeData.financeAccountDataProperty());

        tableView.setItems(financeData.getTransactionDataList());
        if (tableView.getItems().size() > 0) {
            tableView.getSelectionModel().select(0);
        }
    }

    private void unbindFinanceData() {
        setDisableAll();
        if (financeData == null) {
            return;
        }

        txtFinanceNr.unBind();
        txtFinanceBelegNr.textProperty().unbindBidirectional(financeData.receiptNoProperty());
        txtFinanceBetrag.unBind();
        txtFinanceText.textProperty().unbindBidirectional(financeData.textProperty());
        pYearPickerFinanceGeschaeftsjahr.unbind();
        pdpFinanceBuchungsdatum.unbindPDateProperty();
        cboAccount.unbindSelValueProperty();

        tableView.setItems(null);
    }

    private void setTransactionData() {
        unbindTransaction();
        transactionData = tableView.getSelectionModel().getSelectedItem();
        bindTransaction();
    }

    private void bindTransaction() {
        vbRight.setDisable(transactionData == null);
        if (transactionData == null) {
            txtTransactionNr.setText("");
            txtTransactionBetrag.setText("");
            cboCategory.unbindSelValueProperty();
            txtTransactionText.setText("");
            return;
        }

        txtTransactionNr.bindBidirectional(transactionData.noProperty());
        txtTransactionBetrag.bindBidirectional(transactionData.betragProperty());
        cboCategory.bindSelValueProperty(transactionData.financeCategoryDataProperty());
        txtTransactionText.textProperty().bindBidirectional(transactionData.textProperty());
    }

    private void unbindTransaction() {
        vbRight.setDisable(transactionData == null);
        if (transactionData == null) {
            return;
        }

        txtTransactionNr.unBind();
        txtTransactionBetrag.unBind();
        cboCategory.unbindSelValueProperty();
        txtTransactionText.textProperty().unbindBidirectional(transactionData.textProperty());
    }
}
