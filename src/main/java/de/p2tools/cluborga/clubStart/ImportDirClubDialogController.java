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
import de.p2tools.cluborga.controller.ClubStartFactory;
import de.p2tools.cluborga.data.knownClubData.KnownClubData;
import de.p2tools.cluborga.data.knownClubData.KnownClubDataFactory;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.dialogs.PDirFileChooser;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.guitools.PButton;
import de.p2tools.p2lib.guitools.PColumnConstraints;
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

public class ImportDirClubDialogController extends PDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnCancel = new Button("Abbrechen");
    private Button btnHelp;

    private final TextField txtImportDir = new TextField();
    private final Button btnImportDir = new Button();

    private boolean ok = false;
    private final ProgData progData;


    public ImportDirClubDialogController(Stage stage) {
        super(stage, ProgConfig.IMPORT_DIR_CLUB_DIALOG_SIZE, "Verein importieren", true, true);

        this.progData = ProgData.getInstance();
        init(true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    protected void make() {
        btnHelp = PButton.helpButton(getStage(), "Verein importieren",
                "Sind bereits Vereinsdaten auf dem Rechner vorhanden, " +
                        "muss zum Import lediglich der Speicherort (Ordner) " +
                        "des Vereins dem Programm bekannt gemacht werden.");

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


        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        int row = 0;
        gridPane.add(new Label("Verein aus Verzeichnis importieren"), 0, ++row, 2, 1);
        gridPane.add(new Label("Pfad:"), 0, ++row);
        gridPane.add(txtImportDir, 1, row);
        gridPane.add(btnImportDir, 2, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());

        VBox.setVgrow(gridPane, Priority.ALWAYS);
        getVBoxCont().getChildren().add(gridPane);

        initListener();
    }

    private void initListener() {
        btnImportDir.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnImportDir.setOnAction(event -> PDirFileChooser.DirChooser(getStage(), txtImportDir));

    }

    private boolean check() {
        String sourceFile = txtImportDir.getText();

        if (sourceFile.isEmpty()) {
            PAlert.showErrorAlert(getStage(), "Verzeichnis fehlt", "Es wurde kein Verzeichnis für den " +
                    "Import angegeben!");
            return false;
        }

        Path src = Paths.get(sourceFile);
        if (!src.toFile().exists() || !src.toFile().isDirectory()) {
            PAlert.showErrorAlert(getStage(), "Verzeichnis fehlt", "Das angegebene Verzeichnis existiert nicht " +
                    "oder ist kein Ordner.");
            return false;
        }

        // Prüfen ob schon in der Liste
        for (KnownClubData knownClubData : progData.knownClubDataList) {
            if (knownClubData.getClubpath().equals(sourceFile)) {
                return ClubStartFactory.startClub(null, knownClubData);
            }
        }

        KnownClubData knownClubData = KnownClubDataFactory.importKnownClubData(progData, sourceFile);
        if (knownClubData == null) {
            PAlert.showErrorAlert(getStage(), "Verein importieren",
                    "Das Programm kann den Verein nicht importieren.");
            return false;
        }

        return ClubStartFactory.startClub(null, knownClubData);
    }

}
