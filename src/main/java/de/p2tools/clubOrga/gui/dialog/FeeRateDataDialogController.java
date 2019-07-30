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
import de.p2tools.p2Lib.dialog.PDialog;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.tools.PException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;


public class FeeRateDataDialogController extends PDialog {

    private Button btnOk = new Button("Ok");
    private Button btnCancel = new Button("Abbrechen");

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private FeeRateData feeRateDataOrg = null;
    private FeeRateData feeRateDataCopy = null;

    private final VBox vBoxCont = new VBox(10);
    private boolean ok = false;

    public FeeRateDataDialogController(ClubConfig clubConfig, FeeRateData memberDataOrg) {
        super(clubConfig.getStage(), clubConfig.FEE_RATE_DATA_DIALOG_SIZE, "Daten ändern", true, true);

        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.feeRateDataOrg = memberDataOrg;

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
        if (feeRateDataOrg == null) {
            PException.throwPException("feeRateDataOrg == null");
        }

        feeRateDataCopy = new FeeRateData(clubConfig);
        FeeRateData.copyData(feeRateDataOrg, feeRateDataCopy);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        addFeeRateData(gridPane);
        vBoxCont.getChildren().add(gridPane);
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
                ((TextArea) control).textProperty().bindBidirectional(feeRateDataCopy.textProperty());

            } else if (configData.getName().equals(FeeRateFieldNames.BETRAG) && id == FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getId()) {
                control = new Label();
                ((Label) control).setText(feeRateDataCopy.getBetrag() + "");

            } else if (configData.getName().equals(FeeRateFieldNames.NR) && id <= FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getId()) {
                control = new Label();
                ((Label) control).setText(feeRateDataCopy.getNr() + "");

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
