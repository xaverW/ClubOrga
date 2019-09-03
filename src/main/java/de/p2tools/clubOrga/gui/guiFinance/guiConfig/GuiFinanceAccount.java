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

package de.p2tools.clubOrga.gui.guiFinance.guiConfig;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountFactory;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountFieldNames;
import de.p2tools.clubOrga.gui.dialog.FinanceAccountDataDialogController;
import de.p2tools.clubOrga.gui.table.ClubTable;
import de.p2tools.clubOrga.gui.tools.GuiFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
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

public class GuiFinanceAccount extends BorderPane {

    private final ScrollPane scrollPaneTable = new ScrollPane();
    private final HBox hBoxSelect = new HBox(10);
    private final VBox vBoxRight = new VBox(10);
    private final VBox vBoxTable = new VBox(0);
    private final TableView<FinanceAccountData> tableView = new TableView<>();
    private final GridPane gridPane = new GridPane();

    private final PTextFieldLong txtNr = new PTextFieldLong();
    private final PToggleSwitch tglGiro = new PToggleSwitch();
    private final TextField txtKonto = new TextField();
    private final TextField txtBic = new TextField();
    private final TextField txtIban = new TextField();
    private final TextField txtBank = new TextField();
    private final TextArea txtText = new TextArea();

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private final FilteredList<FinanceAccountData> filteredList;
    private final SortedList<FinanceAccountData> sortedList;

    private FinanceAccountData financeAccountData = null;

    public GuiFinanceAccount(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;

        filteredList = clubConfig.financeAccountDataList.getFilteredList();
        sortedList = clubConfig.financeAccountDataList.getSortedList();

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
        GuiFactory.setPaneTitle(this, "Konten");
        setCenter(vBoxTable);
        setRight(vBoxRight);

        Button btnNew = new Button("");
        btnNew.setGraphic(new ProgIcons().ICON_BUTTON_ADD);
        btnNew.setOnAction(a -> {
            FinanceAccountData financeAccountData = new FinanceAccountData(clubConfig);
            if (new FinanceAccountDataDialogController(clubConfig, financeAccountData).isOk()) {
                clubConfig.financeAccountDataList.add(financeAccountData);
            }
        });

        Button btnDel = new Button();
        btnDel.setGraphic(new ProgIcons().ICON_BUTTON_REMOVE);
        btnDel.setOnAction(a -> {
            FinanceAccountData item = tableView.getSelectionModel().getSelectedItem();
            if (item == null) {
                new PAlert().showInfoNoSelection(clubConfig.getStage());
                return;
            }

            if (!clubConfig.financeAccountDataList.checkRemove(item)) {
                return;
            }

            if (FinanceAccountFactory.searchAccount(clubConfig, item)) {
                PAlert.showErrorAlert(clubConfig.getStage(), "Konto wird noch verwendet",
                        "Das Konto wird noch in Finanzdaten verwendet. " +
                                "Bitte zuerst die Finanzdaten ändern.");
                return;
            }

            clubConfig.financeAccountDataList.remove(item);
        });

        Button btnChange = new Button("");
        btnChange.setGraphic(new ProgIcons().ICON_BUTTON_MEMBER_CHANGE);
        btnChange.setOnAction(a -> showInfo());

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

        new ClubTable(clubConfig).setTable(tableView, ClubTable.TABLE.FINANCE_ACCOUNT);

        tableView.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent != null && mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                showInfo();
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<FinanceAccountData> rateData = getSel();
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

        tglGiro.setText(FinanceAccountFieldNames.GIRO_);
        txtBic.disableProperty().bind(tglGiro.selectedProperty().not());
        txtIban.disableProperty().bind(tglGiro.selectedProperty().not());
        txtBank.disableProperty().bind(tglGiro.selectedProperty().not());


        int r = 0;
        gridPane.add(new Label(FinanceAccountFieldNames.NR_), 0, r);
        gridPane.add(txtNr, 1, r);

        gridPane.add(new Label(FinanceAccountFieldNames.KONTO_), 0, ++r);
        gridPane.add(txtKonto, 1, r);


        gridPane.add(new Label(" "), 0, ++r);
        gridPane.add(tglGiro, 0, ++r, 2, 1);

        gridPane.add(new Label(FinanceAccountFieldNames.BIC_), 0, ++r);
        gridPane.add(txtBic, 1, r);

        gridPane.add(new Label(FinanceAccountFieldNames.IBAN_), 0, ++r);
        gridPane.add(txtIban, 1, r);

        gridPane.add(new Label(FinanceAccountFieldNames.BANK_), 0, ++r);
        gridPane.add(txtBank, 1, r);


        r = 0;
        gridPane.add(new Label(" "), 2, r);
        gridPane.add(new Label(FinanceAccountFieldNames.DESCRIPTION_), 3, r);
        gridPane.add(txtText, 3, ++r, 1, 6);

        vBoxTable.getChildren().add(gridPane);
    }

    public Optional<FinanceAccountData> getSel() {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return Optional.empty();
        }
    }

    public void saveTable() {
        new ClubTable(clubConfig).saveTable(tableView, ClubTable.TABLE.FINANCE_ACCOUNT);
    }

    private void setInfo() {
        FinanceAccountData data = tableView.getSelectionModel().getSelectedItem();
        if (this.financeAccountData != null &&
                data != null &&
                this.financeAccountData.equals(data)) {
            return;
        }

        unbind();
        this.financeAccountData = data;
        bind();
    }

    private void showInfo() {
        setInfo();

        FinanceAccountData data = tableView.getSelectionModel().getSelectedItem();
        if (data == null) {
            new PAlert().showInfoNoSelection(clubConfig.getStage());
            return;
        }

        if (new FinanceAccountDataDialogController(clubConfig, financeAccountData).isOk()) {
            tableView.refresh();
        }
    }

    private void setDisableAll() {
        gridPane.setDisable(financeAccountData == null);
    }

    private void unbind() {
        setDisableAll();
        if (financeAccountData == null) {
            return;
        }

        txtNr.unBind();
        tglGiro.selectedProperty().unbindBidirectional(financeAccountData.giroProperty());
        txtKonto.textProperty().unbindBidirectional(financeAccountData.kontoProperty());
        txtBic.textProperty().unbindBidirectional(financeAccountData.bicProperty());
        txtIban.textProperty().unbindBidirectional(financeAccountData.ibanProperty());
        txtBank.textProperty().unbindBidirectional(financeAccountData.bankProperty());
        txtText.textProperty().unbindBidirectional(financeAccountData.descriptionProperty());
    }

    private void bind() {
        setDisableAll();
        if (financeAccountData == null) {
            txtNr.setText("");
            txtKonto.setText("");
            txtText.setText("");
            return;
        }

        txtNr.setStateLabel(financeAccountData.getId() < FinanceAccountFactory.ACCOUNT_TYPE_SIZE);
        tglGiro.setCheckBoxDisable(financeAccountData.getId() < FinanceAccountFactory.ACCOUNT_TYPE_SIZE);

        txtNr.bindBidirectional(financeAccountData.nrProperty());
        tglGiro.selectedProperty().bindBidirectional(financeAccountData.giroProperty());
        txtKonto.textProperty().bindBidirectional(financeAccountData.kontoProperty());
        txtBic.textProperty().bindBidirectional(financeAccountData.bicProperty());
        txtIban.textProperty().bindBidirectional(financeAccountData.ibanProperty());
        txtBank.textProperty().bindBidirectional(financeAccountData.bankProperty());
        txtText.textProperty().bindBidirectional(financeAccountData.descriptionProperty());
    }

    private ContextMenu getContextMenu(FinanceAccountData data) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new ClubTable(clubConfig).resetTable(tableView, ClubTable.TABLE.FINANCE_ACCOUNT));
        contextMenu.getItems().add(resetTable);

        return contextMenu;
    }

}
