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
import de.p2tools.cluborga.controller.export.zip.ZipFactory;
import de.p2tools.cluborga.data.knownClubData.KnownClubData;
import de.p2tools.cluborga.data.knownClubData.KnownClubDataFactory;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.dialogs.PDirFileChooser;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.guitools.PButton;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImportZipClubDialogController extends PDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnCancel = new Button("Abbrechen");
    private Button btnHelp;

    private final TextField txtZipFile = new TextField();
    private final Button btnZipFile = new Button();
    private final TextField txtDestDir = new TextField();
    private final Button btnDestDir = new Button();

    private boolean ok = false;
    private final ProgData progData;


    public ImportZipClubDialogController(Stage stage) {
        super(stage, ProgConfig.IMPORT_ZIP_CLUB_DIALOG_SIZE, "Verein importieren", true, true);

        this.progData = ProgData.getInstance();
        init(true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    protected void make() {
        btnHelp = PButton.helpButton(getStage(), "Verein importieren",
                "Ein erstellter Verein kann in eine ZIP-Datei gesichert werden. " +
                        "Es werden damit alle Infos zum Verein gesichert." +
                        "\n\n" +
                        "Beim Import aus einer ZIP-Datei wird der Inhalt dieser " +
                        "ZIP-Datei wieder in ein beliebiges Verzeichnis entpackt werden." +
                        "\n\n" +
                        "Der Verein kann somit wieder mit dem Programm gestartet werden.");

        addOkCancelButtons(btnOk, btnCancel);
        addHlpButton(btnHelp);

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
        gridPane.add(new Label("ZIP-Datei für den Import"), 0, ++row, 2, 1);
        gridPane.add(new Label("Pfad:"), 0, ++row);
        gridPane.add(txtZipFile, 1, row);
        gridPane.add(btnZipFile, 2, row);

        gridPane.add(new Label(" "), 1, ++row);

        gridPane.add(new Label("Verzeichnis in das der Verein kopiert werden soll"), 0, ++row, 2, 1);
        gridPane.add(new Label("Pfad:"), 0, ++row);
        gridPane.add(txtDestDir, 1, row);
        gridPane.add(btnDestDir, 2, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());

        VBox.setVgrow(gridPane, Priority.ALWAYS);
        getVBoxCont().getChildren().add(gridPane);

        initListener();
    }

    private void initListener() {
        btnZipFile.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnZipFile.setOnAction(event -> PDirFileChooser.FileChooserOpenFile(getStage(), txtZipFile));

        btnDestDir.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnDestDir.setOnAction(event -> PDirFileChooser.DirChooser(getStage(), txtDestDir));
    }

    private boolean check() {
        if (!checkDir()) {
            return false;
        }

        return true;
    }

    private boolean checkDir() {
        String zipFile = txtZipFile.getText();
        String destDir = txtDestDir.getText();

        if (zipFile.isEmpty()) {
            PAlert.showErrorAlert(getStage(), "ZIP-Datei fehlt", "Es wurde keine ZIP-Datei für den " +
                    "Import angegeben!");
            return false;
        }
        if (destDir.isEmpty()) {
            PAlert.showErrorAlert(getStage(), "Verzeichnis fehlt", "Es wurde kein Verzeichnis für den " +
                    "Import angegeben!");
            return false;
        }

        if (!ZipFactory.unzipFileToFolder(getStage(), zipFile, destDir)) {
            return false;
        }

        KnownClubData knownClubData = KnownClubDataFactory.importKnownClubData(progData, destDir);
        if (knownClubData == null) {
            PAlert.showErrorAlert(getStage(), "Verein importieren",
                    "Das Programm kann den Verein nicht importieren.");
            return false;
        }

        return ClubStartFactory.startClub(null, knownClubData);
    }

}
