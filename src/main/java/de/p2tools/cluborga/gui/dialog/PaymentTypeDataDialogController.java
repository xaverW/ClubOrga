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

package de.p2tools.cluborga.gui.dialog;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.data.feeData.paymentType.PaymentTypeData;
import de.p2tools.cluborga.data.feeData.paymentType.PaymentTypeFactory;
import de.p2tools.cluborga.data.feeData.paymentType.PaymentTypeNames;
import de.p2tools.cluborga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.PComboBoxObject;
import de.p2tools.p2lib.guitools.PTextField;
import de.p2tools.p2lib.tools.PException;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class PaymentTypeDataDialogController extends PDialogExtra {

    private Button btnOk = new Button("Ok");
    private Button btnCancel = new Button("Abbrechen");

    private final PComboBoxObject<FinanceAccountData> cboAccount = new PComboBoxObject<>();
    private final PTextField txtName = new PTextField("", true);

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private PaymentTypeData paymentTypeDataOrg = null;
    private PaymentTypeData paymentTypeDataCopy = null;
    private CheckBox chkEinzug = new CheckBox();

    private boolean ok = false;

    public PaymentTypeDataDialogController(ClubConfig clubConfig, PaymentTypeData paymentTypeDataOrg) {
        super(clubConfig.getStage(), clubConfig.PAYMENT_TYPE_DATA_DIALOG_SIZE,
                "Daten ändern", true, true, DECO.NO_BORDER);

        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.paymentTypeDataOrg = paymentTypeDataOrg;

        addOkCancelButtons(btnOk, btnCancel);
        init(true);
    }

    @Override
    public void make() {
        initPanel();
        btnOk.setOnAction(a -> {
            if (check()) {
                ok = true;
                close();
            }
        });
        btnOk.disableProperty().bind(paymentTypeDataCopy.financeAccountDataProperty().isNull());

        btnCancel.setOnAction(a -> close());
    }


    private void initPanel() {
        if (paymentTypeDataOrg == null) {
            throw new PException("paymentTypeDataOrg == null");
        }

        paymentTypeDataCopy = new PaymentTypeData(clubConfig);
        PaymentTypeData.copyData(paymentTypeDataOrg, paymentTypeDataCopy);

        final GridPane gridPane = new GridPane();
        VBox.setVgrow(gridPane, Priority.ALWAYS);
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize());

        addStateData(gridPane);
        getVBoxCont().getChildren().add(gridPane);
    }

    private void addStateData(GridPane gridPane) {
        int row = 0;
        Config[] configs = paymentTypeDataCopy.getConfigsArr();

        long id = paymentTypeDataCopy.getId();

        for (int i = 0; i < configs.length; ++i) {
            Control control;
            Config configData = configs[i];

            if (configData.getName().equals(PaymentTypeNames.ID)) {
                continue;

            } else if (configData.getName().equals(PaymentTypeNames.NO) && id < PaymentTypeFactory.PAYMENT_TYPE_SIZE) {
                control = new PTextField(paymentTypeDataCopy.getNo() + "", true);

            } else if (configData.getName().equals(PaymentTypeNames.ACCOUNT) && id < PaymentTypeFactory.PAYMENT_TYPE_SIZE) {
                control = new PTextField(paymentTypeDataCopy.getFinanceAccountData().getName(), true);

            } else if (configData.getName().equals(PaymentTypeNames.ACCOUNT)) {
                control = getPComboObject(clubConfig.financeAccountDataList, paymentTypeDataCopy.financeAccountDataProperty());


            } else if (configData.getName().equals(PaymentTypeNames.DIRECT_DEBIT)) {
                control = chkEinzug;
                chkEinzug.selectedProperty().bindBidirectional(paymentTypeDataCopy.directDebitProperty());
                if (paymentTypeDataCopy.getFinanceAccountData() == null) {
                    chkEinzug.setDisable(true);
                } else {
                    chkEinzug.setDisable(id < PaymentTypeFactory.PAYMENT_TYPE_SIZE ||
                            !paymentTypeDataCopy.getFinanceAccountData().isGiro());
                }

            } else if (configData.getName().equals(PaymentTypeNames.DESCRIPTION)) {
                control = new TextArea();
                GridPane.setVgrow(control, Priority.ALWAYS);
                ((TextArea) control).textProperty().bindBidirectional(paymentTypeDataCopy.textProperty());

            } else if (configData instanceof Config) {
                // rest
                control = ((Config) configData).getControl();

            } else {
                throw new PException("StateDataDialogController.addStateData");
            }


            gridPane.add(new Label(configData.getName() + ":"), 0, row);
            gridPane.add(control, 1, row);
            ++row;
        }
    }

    public boolean isOk() {
        return ok;
    }

    public void close() {
        super.close();
    }

    private boolean check() {
        if (paymentTypeDataCopy.financeAccountDataProperty().get() == null) {
            PAlert.showErrorAlert(this.getStage(), "Zahlart anlegen",
                    "Zum Anlegen einer Zahlart muss ein Konto ausgewählt werden");
            return false;
        }

        // geänderte Daten wieder auf ORG zurück kopieren
        PaymentTypeData.copyData(paymentTypeDataCopy, paymentTypeDataOrg);
        return true;
    }

    private PComboBoxObject getPComboObject(ObservableList<FinanceAccountData> cbo_list,
                                            ObjectProperty<FinanceAccountData> objectProperty) {

        cboAccount.setMaxWidth(Double.MAX_VALUE);
        cboAccount.init(cbo_list, objectProperty);
        cboAccount.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isGiro()) {
                chkEinzug.setDisable(false);
            } else {
                chkEinzug.setDisable(true);
                chkEinzug.setSelected(false);
            }
        });

        return cboAccount;
    }
}
