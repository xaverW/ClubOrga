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
import de.p2tools.cluborga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.cluborga.data.financeData.accountData.FinanceAccountFactory;
import de.p2tools.cluborga.data.financeData.accountData.FinanceAccountFieldNames;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.PTextField;
import de.p2tools.p2lib.guitools.ptoggleswitch.PToggleSwitch;
import de.p2tools.p2lib.tools.PException;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class FinanceAccountDataDialogController extends PDialogExtra {

    private Button btnOk = new Button("Ok");
    private Button btnCancel = new Button("Abbrechen");

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private FinanceAccountData dataOrg = null;
    private FinanceAccountData dataCopy = null;

    private boolean ok = false;

    public FinanceAccountDataDialogController(ClubConfig clubConfig, FinanceAccountData dataOrg) {
        super(clubConfig.getStage(), clubConfig.FINANCE_ACCOUNT_DATA_DIALOG_SIZE,
                "Daten ändern", true, true, DECO.NO_BORDER);

        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.dataOrg = dataOrg;

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
        btnCancel.setOnAction(a -> close());
    }


    private void initPanel() {
        if (dataOrg == null) {
            throw new PException("stateDataOrg == null");
        }

        dataCopy = new FinanceAccountData(clubConfig);
        FinanceAccountData.copyData(dataOrg, dataCopy);

        final GridPane gridPane = new GridPane();
        VBox.setVgrow(gridPane, Priority.ALWAYS);
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        addData(gridPane);
        getVBoxCont().getChildren().add(gridPane);
    }

    private void addData(GridPane gridPane) {
        int row = 0;
        Config[] configs = dataCopy.getConfigsArr();
        long id = dataCopy.getId();
        PToggleSwitch tglGiro = new PToggleSwitch();
        tglGiro.setText(FinanceAccountFieldNames.GIRO_);

        for (int i = 0; i < configs.length; ++i) {
            Control control;
            Config configData = configs[i];

            if (configData.getName().equals(FinanceAccountFieldNames.ID)) {
                continue;

            } else if (configData.getName().equals(FinanceAccountFieldNames.NO) &&
                    id < FinanceAccountFactory.ACCOUNT_TYPE_SIZE) {
                control = new PTextField(dataCopy.getNo() + "", true);

            } else if (configData.getName().equals(FinanceAccountFieldNames.GIRO) &&
                    id == FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_GIRO.getId()) {
                gridPane.add(tglGiro, 0, row, 2, 1);
                ++row;

                tglGiro.setCheckBoxDisable(true);
                tglGiro.setSelected(true);
                continue;
            } else if (configData.getName().equals(FinanceAccountFieldNames.GIRO) &&
                    id == FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_BAR.getId()) {
                gridPane.add(tglGiro, 0, row, 2, 1);
                ++row;
                tglGiro.setCheckBoxDisable(true);
                tglGiro.setSelected(false);
                continue;
            } else if (configData.getName().equals(FinanceAccountFieldNames.GIRO)) {
                gridPane.add(tglGiro, 0, row, 2, 1);
                ++row;
                tglGiro.selectedProperty().bindBidirectional(dataCopy.giroProperty());
                continue;
            } else if (configData.getName().equals(FinanceAccountFieldNames.BIC) ||
                    configData.getName().equals(FinanceAccountFieldNames.IBAN) ||
                    configData.getName().equals(FinanceAccountFieldNames.BANK)) {
                control = ((Config) configData).getControl();
                control.disableProperty().bind(tglGiro.selectedProperty().not());

            } else if (configData.getName().equals(FinanceAccountFieldNames.DESCRIPTION)) {
                control = new TextArea();
                GridPane.setVgrow(control, Priority.ALWAYS);
                ((TextArea) control).textProperty().bindBidirectional(dataCopy.descriptionProperty());

            } else if (configData instanceof Config) {
                control = ((Config) configData).getControl();

            } else {
                throw new PException("FinanceAccountDataDialogController.addData");
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
        FinanceAccountData.copyData(dataCopy, dataOrg);
        return ret;
    }
}
