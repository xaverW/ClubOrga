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
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryFactory;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryFieldNames;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PTextField;
import de.p2tools.p2Lib.tools.PException;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class FinanceCategoryDataDialogController extends PDialogExtra {

    private Button btnOk = new Button("Ok");
    private Button btnCancel = new Button("Abbrechen");

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private FinanceCategoryData dataOrg = null;
    private FinanceCategoryData dataCopy = null;

    private boolean ok = false;

    public FinanceCategoryDataDialogController(ClubConfig clubConfig, FinanceCategoryData dataOrg) {
        super(clubConfig.getStage(), clubConfig.FINANCE_CATEGORY_DATA_DIALOG_SIZE,
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
            PException.throwPException("stateDataOrg == null");
        }

        dataCopy = new FinanceCategoryData(clubConfig);
        FinanceCategoryData.copyData(dataOrg, dataCopy);

        final GridPane gridPane = new GridPane();
        VBox.setVgrow(gridPane, Priority.ALWAYS);
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize());

        addData(gridPane);
        getVBoxCont().getChildren().add(gridPane);
    }

    private void addData(GridPane gridPane) {
        int row = 0;
        Config[] configs = dataCopy.getConfigsArr();
        long id = dataCopy.getId();

        for (int i = 0; i < configs.length; ++i) {
            Control control;
            Config configData = configs[i];

            if (configData.getName().equals(FinanceCategoryFieldNames.ID)) {
                continue;

            } else if (configData.getName().equals(FinanceCategoryFieldNames.NO) &&
                    id < FinanceCategoryFactory.CATEGORY_TYPE_SIZE) {
                control = new PTextField(dataCopy.getNo() + "", true);

            } else if (configData.getName().equals(FinanceCategoryFieldNames.DESCRIPTION)) {
                control = new TextArea();
                GridPane.setVgrow(control, Priority.ALWAYS);
                ((TextArea) control).textProperty().bindBidirectional(dataCopy.descriptionProperty());

            } else if (configData instanceof Config) {
                control = ((Config) configData).getControl();

            } else {
                throw new PException("FinanceCategoryDataDialogController.addData");
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
        FinanceCategoryData.copyData(dataCopy, dataOrg);
        return ret;
    }
}
