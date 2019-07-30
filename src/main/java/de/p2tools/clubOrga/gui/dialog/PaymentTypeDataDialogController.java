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

package de.p2tools.clubOrga.gui.dialog;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeFactory;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeNames;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigExtra;
import de.p2tools.p2Lib.dialog.PDialog;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBoxObject;
import de.p2tools.p2Lib.guiTools.PTextField;
import de.p2tools.p2Lib.tools.PException;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class PaymentTypeDataDialogController extends PDialog {

    private Button btnOk = new Button("Ok");
    private Button btnCancel = new Button("Abbrechen");

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private PaymentTypeData paymentTypeDataOrg = null;
    private PaymentTypeData paymentTypeDataCopy = null;

    private final VBox vBoxCont = new VBox(10);
    private boolean ok = false;

    public PaymentTypeDataDialogController(ClubConfig clubConfig, PaymentTypeData paymentTypeDataOrg) {
        super(clubConfig.getStage(), clubConfig.PAYMENT_TYPE_DATA_DIALOG_SIZE,
                "Daten ändern", true, true);

        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.paymentTypeDataOrg = paymentTypeDataOrg;

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        vBox.getChildren().add(vBoxCont);
        VBox.setVgrow(vBoxCont, Priority.ALWAYS);

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnOk, btnCancel);
        vBox.getChildren().add(hBox);

        init(vBox, true);
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
        btnCancel.setOnAction(a -> close());
    }


    private void initPanel() {
        if (paymentTypeDataOrg == null) {
            throw new PException("paymentTypeDataOrg == null");
        }

        paymentTypeDataCopy = new PaymentTypeData(clubConfig);
        PaymentTypeData.copyData(paymentTypeDataOrg, paymentTypeDataCopy);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize());

        addStateData(gridPane);
        vBoxCont.getChildren().add(gridPane);
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

            } else if (configData.getName().equals(PaymentTypeNames.KONTO) && id < PaymentTypeFactory.PAYMENT_TYPE_SIZE) {
                control = new PTextField(paymentTypeDataCopy.getFinanceAccountData().getKonto(), true);

            } else if (configData.getName().equals(PaymentTypeNames.KONTO)) {
                control = getPComboObject(paymentTypeDataCopy.financeAccountDataProperty(), clubConfig.financeAccountDataList);

            } else if (configData.getName().equals(PaymentTypeNames.NR) && id < PaymentTypeFactory.PAYMENT_TYPE_SIZE) {
                control = new PTextField(paymentTypeDataCopy.getNr() + "", true);

            } else if (configData.getName().equals(PaymentTypeNames.DESCRIPTION)) {
                control = new TextArea();
                ((TextArea) control).textProperty().bindBidirectional(paymentTypeDataCopy.textProperty());

            } else if (configData instanceof ConfigExtra) {
                // rest
                control = ((ConfigExtra) configData).getControl();

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
        boolean ret = true;
        // geänderte Daten wieder auf ORG zurück kopieren
        PaymentTypeData.copyData(paymentTypeDataCopy, paymentTypeDataOrg);
        return ret;
    }

    private PComboBoxObject getPComboObject(ObjectProperty objectProperty, ObservableList cbo_list) {
        final PComboBoxObject cboStatus = new PComboBoxObject<>();
        cboStatus.setMaxWidth(Double.MAX_VALUE);
        cboStatus.init(cbo_list, objectProperty);

        return cboStatus;
    }
}
