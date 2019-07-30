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
import de.p2tools.clubOrga.data.memberData.stateData.StateData;
import de.p2tools.clubOrga.data.memberData.stateData.StateDataFactory;
import de.p2tools.clubOrga.data.memberData.stateData.StateFieldNames;
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


public class StateDataDialogController extends PDialog {

    private Button btnOk = new Button("Ok");
    private Button btnCancel = new Button("Abbrechen");

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private StateData stateDataOrg = null;
    private StateData stateDataCopy = null;

    private final VBox vBoxCont = new VBox(10);
    private boolean ok = false;

    public StateDataDialogController(ClubConfig clubConfig, StateData stateDataOrg) {
        super(clubConfig.getStage(), clubConfig.STATE_DATA_DIALOG_SIZE, "Daten ändern", true, true);

        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.stateDataOrg = stateDataOrg;

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
        if (stateDataOrg == null) {
            throw new PException("stateDataOrg == null");
        }

        stateDataCopy = new StateData(clubConfig);
        StateData.copyData(stateDataOrg, stateDataCopy);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        addStateData(gridPane);
        vBoxCont.getChildren().add(gridPane);
    }

    private void addStateData(GridPane gridPane) {
        int row = 0;
        Config[] configs = stateDataCopy.getConfigsArr();

        for (int i = 0; i < configs.length; ++i) {
            Control control;
            Config configData = configs[i];
            long id = stateDataCopy.getId();

            if (configData.getName().equals(StateFieldNames.ID)) {
                continue;

            } else if (configData.getName().equals(StateFieldNames.DESCRIPTION)) {
                control = new TextArea();
                ((TextArea) control).textProperty().bindBidirectional(stateDataCopy.textProperty());

            } else if (configData.getName().equals(StateFieldNames.NR) && id < StateDataFactory.STATE_TYPE_SIZE) {
                control = new Label();
                ((Label) control).setText(stateDataCopy.getNr() + "");

            } else if (configData instanceof ConfigExtra) {
                // rest
                control = ((ConfigExtra) configData).getControl();

            } else {
                throw new PException("StateDataDialogController.addStateData");
            }

            Label lblText = new Label(configData.getName() + ":");
            lblText.setMinWidth(Region.USE_PREF_SIZE);
            control.setMaxWidth(Double.MAX_VALUE);

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
        StateData.copyData(stateDataCopy, stateDataOrg);
        return ret;
    }
}
