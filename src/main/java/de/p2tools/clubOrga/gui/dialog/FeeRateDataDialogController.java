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
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateFactory;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateFieldNames;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigExtra;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PTextFieldMoney;
import de.p2tools.p2Lib.tools.PException;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class FeeRateDataDialogController extends PDialogExtra {

    private Button btnOk = new Button("Ok");
    private Button btnCancel = new Button("Abbrechen");

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private FeeRateData feeRateDataOrg = null;
    private FeeRateData feeRateDataCopy = null;

    private boolean ok = false;

    public FeeRateDataDialogController(ClubConfig clubConfig, FeeRateData memberDataOrg) {
        super(clubConfig.getStage(), clubConfig.FEE_RATE_DATA_DIALOG_SIZE, "Daten ändern", true, true, DECO.NONE);

        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.feeRateDataOrg = memberDataOrg;

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
        if (feeRateDataOrg == null) {
            PException.throwPException("feeRateDataOrg == null");
        }

        feeRateDataCopy = new FeeRateData(clubConfig);
        FeeRateData.copyData(feeRateDataOrg, feeRateDataCopy);

        final GridPane gridPane = new GridPane();
        VBox.setVgrow(gridPane, Priority.ALWAYS);
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        addFeeRateData(gridPane);
        getvBoxCont().getChildren().add(gridPane);
    }

    private void addFeeRateData(GridPane gridPane) {
        int row = 0;
        Config[] configs = feeRateDataCopy.getConfigsArr();

        for (int i = 0; i < configs.length; ++i) {
            Control control;
            Config configData = configs[i];
            long id = feeRateDataCopy.getId();

            if (configData.getName().equals(FeeRateFieldNames.ID)) {
                continue;

            } else if (configData.getName().equals(FeeRateFieldNames.DESCRIPTION)) {
                control = new TextArea();
                GridPane.setVgrow(control, Priority.ALWAYS);
                ((TextArea) control).textProperty().bindBidirectional(feeRateDataCopy.textProperty());

            } else if (configData.getName().equals(FeeRateFieldNames.BETRAG) && id == FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getId()) {
                control = ((ConfigExtra) configData).getControl();
                ((PTextFieldMoney) control).setLabelLike(true);

            } else if (configData.getName().equals(FeeRateFieldNames.NO) && id <= FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getId()) {
                control = new Label();
                ((Label) control).setText(feeRateDataCopy.getNo() + "");

            } else if (configData instanceof ConfigExtra) {
                // rest
                control = ((ConfigExtra) configData).getControl();

            } else {
                throw new PException("FeeRateDataDialogController.addFeeRateData");
            }

            control.setMaxWidth(Double.MAX_VALUE);
            Label lblText = new Label(configData.getName() + ":");
            lblText.setMinWidth(Region.USE_PREF_SIZE);
            gridPane.add(lblText, 0, row);
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
        FeeRateData.copyData(feeRateDataCopy, feeRateDataOrg);
        return ret;
    }
}
