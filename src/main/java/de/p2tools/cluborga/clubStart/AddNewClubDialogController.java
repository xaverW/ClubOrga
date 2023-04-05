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

package de.p2tools.cluborga.clubStart;


import de.p2tools.cluborga.config.prog.ProgConfig;
import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.config.prog.ProgIcons;
import de.p2tools.cluborga.data.knownClubData.KnownClubData;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.dialogs.PDirFileChooser;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.guitools.PButton;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.PToggleSwitch;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AddNewClubDialogController extends PDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnCancel = new Button("Abbrechen");
    private Button btnHelp;
    private final PToggleSwitch tglDemoData = new PToggleSwitch("Demodaten (Mitglieder, Beiträge, ..) im neuen Verein anlegen:");


    private final TextField txtClubDir = new TextField();
    private final Button btnSelectDir = new Button();
    private final TextField txtClubName = new TextField();

    private boolean ok = false;
    private final ProgData progData;
    private final KnownClubData knownClubData;


    public AddNewClubDialogController(Stage stage, ProgData progData, KnownClubData knownClubData) {
        super(stage, ProgConfig.ADD_NEW_CLUB_DIALOG_SIZE, "neuen Verein anlegen", true, true);

        this.progData = progData;
        this.knownClubData = knownClubData;

        init(true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    protected void make() {
        btnHelp = PButton.helpButton(getStage(), "Verein anlegen",
                "In diesem Dialog kann ein neuer Verein angelegt werden. Dazu muss ein " +
                        "leerer Ordner für die Vereinsdaten angegeben werden. Auch der Vereinsname kann " +
                        "hier bereits angegeben werden." +
                        "\n\n" +
                        "Es besteht auch die Möglichkeit, dem Verein beim Start gleich Demodaten " +
                        "mitzugeben (Mitglieder, Beiträge, Finanzen). Das ist gedacht, um mit einem " +
                        "Verein mit Demodaten, die Funktionen des Programms gefahrlos durchzuspielen.");

        addOkCancelButtons(btnOk, btnCancel);
        ButtonBar.setButtonData(btnHelp, ButtonBar.ButtonData.HELP);
        addAnyButton(btnHelp);

        btnOk.setOnAction(a -> {
            if (check()) {
                ok = true;
                close();
            }
        });
        btnCancel.setOnAction(a -> close());

        tglDemoData.selectedProperty().bindBidirectional(knownClubData.addDemoDataProperty());

        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        int row = 0;
        gridPane.add(new Label("Pfad und Name des Vereins"), 0, ++row, 2, 1);
        gridPane.add(new Label("Pfad:"), 0, ++row);
        gridPane.add(txtClubDir, 1, row);
        gridPane.add(btnSelectDir, 2, row);

        gridPane.add(new Label("Name:"), 0, ++row);
        gridPane.add(txtClubName, 1, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(tglDemoData, 0, ++row, 3, 1);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());

        VBox.setVgrow(gridPane, Priority.ALWAYS);
        getVBoxCont().getChildren().add(gridPane);

        initListener();
    }

    private void initListener() {
        btnSelectDir.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnSelectDir.setOnAction(event -> PDirFileChooser.DirChooser(getStage(), txtClubDir));

        txtClubDir.textProperty().bindBidirectional(knownClubData.clubpathProperty());
        txtClubName.textProperty().bindBidirectional(knownClubData.clubnameProperty());
    }

    private boolean check() {
        if (!checkNewClubPath(getStage(), knownClubData)) {
            return false;
        }

        return true;
    }

    private boolean checkNewClubPath(Stage stage, KnownClubData knownClubData) {
        Path path = Paths.get(knownClubData.getClubpath());
        if (path.toFile().exists() && !path.toFile().isDirectory()) {
            PAlert.showErrorAlert(stage, "Speicherort", "Der angegebene Speicherort für den Verein " +
                    "ist keine Ordner.");
            return false;
        }
        if (path.toFile().exists() && path.toFile().isDirectory() && path.toFile().listFiles().length != 0) {
            PAlert.showErrorAlert(stage, "Speicherort", "Der angegebene Ordner für den Verein " +
                    "ist nicht leer.");
            return false;
        }
        if (!path.toFile().exists() && !path.toFile().mkdirs()) {
            PAlert.showErrorAlert(stage, "Speicherort", "Der angegebene Ordner für den Verein " +
                    "kann nicht angelegt werden.");
            return false;
        }

        return true;
    }
}
