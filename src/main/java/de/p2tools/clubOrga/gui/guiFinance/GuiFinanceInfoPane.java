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
import de.p2tools.p2Lib.guiTools.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class GuiFinanceInfoPane extends AnchorPane {

    private final ScrollPane scrollPaneLeft = new ScrollPane();

    private final VBox vbLeft = new VBox();

    private final Label lblFinance = new Label("Finanzdaten");

    // FinanceData
    private final PTextFieldLong txtFinanceNr = new PTextFieldLong();
    private final TextField txtFinanceBelegNr = new TextField();
    private final PYearPicker pYearPickerFinanceGeschaeftsjahr = new PYearPicker();
    private final PDatePropertyPicker pdpFinanceBuchungsdatum = new PDatePropertyPicker();
    private final PComboBoxObject<FinanceAccountData> cboAccount = new PComboBoxObject<>();
    private final TextArea txtFinanceText = new TextArea();


    // TransactionData
    private final PTextFieldLong txtTransactionNr = new PTextFieldLong();
    private final PTextFieldMoney txtTransactionBetrag = new PTextFieldMoney();
    private final PComboBoxObject<FinanceCategoryData> cboCategory = new PComboBoxObject<>();
    private final TextArea txtTransactionText = new TextArea();

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private FinanceData financeData = null;
    private TransactionData transactionData = null;

    public GuiFinanceInfoPane(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
        progData = ProgData.getInstance();

        getChildren().addAll(scrollPaneLeft);
        AnchorPane.setLeftAnchor(scrollPaneLeft, 0.0);
        AnchorPane.setBottomAnchor(scrollPaneLeft, 0.0);
        AnchorPane.setRightAnchor(scrollPaneLeft, 0.0);
        AnchorPane.setTopAnchor(scrollPaneLeft, 0.0);

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

        initInfoLeft();
        setDisableAll();
    }

    private void initInfoLeft() {
        Button btnNew = new Button("");
        btnNew.setTooltip(new Tooltip("eine neue Transaktion hinzufügen"));
        btnNew.setGraphic(new ProgIcons().ICON_BUTTON_ADD);
        btnNew.setOnAction(a -> {
            if (financeData != null) {
                TransactionData tr = financeData.getTransactionDataList().getLast();
                TransactionData transactionData = new TransactionData(financeData.getTransactionDataList().getNextNr(), clubConfig);
                if (tr != null) {
                    transactionData.setFinanceCategoryData(tr.getFinanceCategoryData());
                }
                financeData.getTransactionDataList().add(transactionData);
                clubConfig.guiFinance.setFinancesInfo();
            }
        });


        cboAccount.init(clubConfig.financeAccountDataList, null);
        cboAccount.setMaxWidth(Double.MAX_VALUE);

        cboCategory.init(clubConfig.financeCategoryDataList, null);
        cboCategory.setMaxWidth(Double.MAX_VALUE);


        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        int row = 0;
        int c = 0;

        pYearPickerFinanceGeschaeftsjahr.setMaxWidth(Double.MAX_VALUE);
        pdpFinanceBuchungsdatum.setMaxWidth(Double.MAX_VALUE);

        gridPane.add(new Label(FinanceFieldNames.BELEG_NR_), c, row);
        gridPane.add(txtFinanceBelegNr, c + 1, row);

        gridPane.add(new Label(FinanceFieldNames.GESCHAEFTSJAHR_), c, ++row);
        gridPane.add(pYearPickerFinanceGeschaeftsjahr, c + 1, row);

        gridPane.add(new Label(FinanceFieldNames.KONTO_), c, ++row);
        gridPane.add(cboAccount, c + 1, row);

        gridPane.add(new Label(FinanceFieldNames.TEXT_), c, ++row);
        HBox hBox = new HBox(10);
        HBox.setHgrow(txtFinanceText, Priority.ALWAYS);
        hBox.setAlignment(Pos.BOTTOM_LEFT);
        hBox.getChildren().addAll(txtFinanceText, btnNew);
        gridPane.add(hBox, c + 1, row, 4, 1);
        GridPane.setVgrow(hBox, Priority.ALWAYS);


//        gridPane.add(new Label(" "), 2, 0);

        row = 0;
        c = 3;

        gridPane.add(new Label(FinanceFieldNames.BETRAG_), c, row);
        gridPane.add(txtTransactionBetrag, c + 1, row);

        gridPane.add(new Label(FinanceFieldNames.BUCHUNGS_DATUM_), c, ++row);
        gridPane.add(pdpFinanceBuchungsdatum, c + 1, row);


        gridPane.add(new Label(FinanceFieldNames.KATEGORIE_), c, ++row);
        gridPane.add(cboCategory, c + 1, row);

//        gridPane.add(new Label(FinanceFieldNames.TEXT_), 3, ++row);
//        gridPane.add(txtTransactionText, 4, row);
//
//        GridPane.setVgrow(txtTransactionText, Priority.ALWAYS);
//        VBox.setVgrow(gridPane, Priority.ALWAYS);


        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcMinSize(20),
                PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        VBox.setVgrow(gridPane, Priority.ALWAYS);

        vbLeft.getChildren().addAll(gridPane);
    }

    public void setFinanceData(FinanceData financeData) {
        if (this.financeData != null &&
                financeData != null &&
                this.financeData.equals(financeData)) {
            return;
        }

        unbindFinanceData();
        unbindTransaction();

        this.financeData = financeData;
        if (financeData == null || financeData.getTransactionDataList().size() != 1) {
            // dann ists nichts für uns
            this.transactionData = null;
            return;
        }

        this.transactionData = financeData.getTransactionDataList().get(0);
        bindFinanceData();
        bindTransaction();
    }

    private void setDisableAll() {
        vbLeft.setDisable(financeData == null);
    }

    private void bindFinanceData() {
        setDisableAll();
        if (financeData == null) {
            txtFinanceNr.setText("");
            txtFinanceBelegNr.setText("");
            txtFinanceText.setText("");
            pYearPickerFinanceGeschaeftsjahr.unbind();
            cboAccount.unbindSelValueProperty();
            pdpFinanceBuchungsdatum.clearDate();
            return;
        }

        txtFinanceNr.bindBidirectional(financeData.nrProperty());
        txtFinanceBelegNr.textProperty().bindBidirectional(financeData.belegNrProperty());
        txtFinanceText.textProperty().bindBidirectional(financeData.textProperty());
        pYearPickerFinanceGeschaeftsjahr.bindBidirectional(financeData.geschaeftsJahrProperty());
        pdpFinanceBuchungsdatum.setpDateProperty(financeData.buchungsDatumProperty());
        cboAccount.bindSelValueProperty(financeData.financeAccountDataProperty());

    }

    private void unbindFinanceData() {
        setDisableAll();
        if (financeData == null) {
            return;
        }

        txtFinanceNr.unBind();
        txtFinanceBelegNr.textProperty().unbindBidirectional(financeData.belegNrProperty());
        txtFinanceText.textProperty().unbindBidirectional(financeData.textProperty());
        pYearPickerFinanceGeschaeftsjahr.unbind();
        pdpFinanceBuchungsdatum.clearDate();
        cboAccount.unbindSelValueProperty();
    }

    private void bindTransaction() {
        if (transactionData == null) {
            txtTransactionNr.setText("");
            txtTransactionBetrag.setText("");
//            cboAccount.unbindSelValueProperty();
            cboCategory.unbindSelValueProperty();
            txtTransactionText.setText("");
            return;
        }

        txtTransactionNr.bindBidirectional(transactionData.nrProperty());
        txtTransactionBetrag.bindBidirectional(transactionData.betragProperty());
//        cboAccount.bindSelValueProperty(transactionData.financeAccountDataProperty());
        cboCategory.bindSelValueProperty(transactionData.financeCategoryDataProperty());
        txtTransactionText.textProperty().bindBidirectional(transactionData.textProperty());
    }

    private void unbindTransaction() {
        if (transactionData == null) {
            return;
        }

        txtTransactionNr.unBind();
        txtTransactionBetrag.unBind();
//        cboAccount.unbindSelValueProperty();
        cboCategory.unbindSelValueProperty();
    }
}
