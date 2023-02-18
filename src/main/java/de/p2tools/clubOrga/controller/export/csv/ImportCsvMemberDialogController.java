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

package de.p2tools.clubOrga.controller.export.csv;


import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBoxString;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ImportCsvMemberDialogController extends PDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnCancel = new Button("Abbrechen");
    private Button btnHelp;

    private final PComboBoxString cboImportFile = new PComboBoxString();
    private final Button btnImportFile = new Button();

    private final ClubConfig clubConfig;
    private boolean ok = false;


    public ImportCsvMemberDialogController(Stage ownerForCenteringDialog, ClubConfig clubConfig) {
        super(ownerForCenteringDialog, clubConfig.IMPORT_MEMBER_CSV_DIALOG_SIZE,
                "Mitglieder aus CVS-Datei importieren", true, true);

        this.clubConfig = clubConfig;

        init(true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    protected void make() {
        btnHelp = PButton.helpButton(getStage(), "Mitglieder aus CVS-Datei importieren",
                "Hier können die Mitgliederdaten aus einer CSV-Datei importiert werden." +
                        "\n\n" +
                        "(Das Dateiformat CSV beschreibt den Aufbau einer Textdatei " +
                        "zur Speicherung oder zum Austausch einfach strukturierter Daten.)");


//        HBox hBoxHelp = new HBox();
//        hBoxHelp.setAlignment(Pos.CENTER_LEFT);
//        hBoxHelp.getChildren().addAll(btnHelp);
//
//        HBox hBox = new HBox();
//        HBox.setHgrow(hBox, Priority.ALWAYS);
//        getHboxOk().getChildren().addAll(btnHelp, hBox, btnOk, btnCancel);

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

        cboImportFile.setMaxWidth(Double.MAX_VALUE);

        getHBoxTitle().getChildren().add(new Label("Mitglieder aus CVS-Datei importieren"));
//        HBox hBoxTitle = GuiFactory.getDialogTitle("Mitglieder aus CVS-Datei importieren");

        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        int row = 0;
//        gridPane.add(new Label("Mitglieder aus CVS-Datei importieren"), 0, ++row, 2, 1);
        gridPane.add(new Label("Datei:"), 0, ++row);
        gridPane.add(cboImportFile, 1, row);
        gridPane.add(btnImportFile, 2, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());

//        ScrollPane scDir = new ScrollPane();
//        scDir.setFitToHeight(true);
//        scDir.setFitToWidth(true);
//
//        scDir.setContent(gridPane);
//        VBox.setVgrow(scDir, Priority.ALWAYS);

        getVBoxCont().getChildren().addAll(/*hBoxTitle,*/ gridPane);

        initListener();
    }

    private void initListener() {
        cboImportFile.init(clubConfig.CBO_LIST_IMPORT_CSV_FILE, clubConfig.IMPORT_CSV_FILE);
        btnImportFile.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnImportFile.setTooltip(new Tooltip("eine Datei auswählen"));
        btnImportFile.setOnAction(event -> PDirFileChooser.FileChooser(getStage(), cboImportFile,
                ProgInfos.getClubExportsDir(clubConfig.getClubPath()).toString()));

        btnOk.disableProperty().bind(cboImportFile.getSelectionModel().selectedItemProperty().isNull());
    }

    private boolean check() {
        String srcFile = cboImportFile.getSelValue();

        if (srcFile.isEmpty()) {
            PAlert.showErrorAlert(getStage(), "Datei fehlt", "Es wurde keine Datei für den " +
                    "Import angegeben!");
            return false;
        }

        Path dFile = Paths.get(srcFile);

        if (!dFile.toFile().exists()) {
            PAlert.showErrorAlert(getStage(), "Importdatei", "Die angegebene Datei existiert nicht.");
            return false;
        }

        if (dFile.toFile().exists() && !dFile.toFile().isFile()) {
            PAlert.showErrorAlert(getStage(), "Importdatei", "Die angegebene Datei ist keine Datei.");
            return false;
        }

        if (!CsvFactoryMember.importMember(clubConfig, dFile)) {
            PAlert.showErrorAlert(getStage(), "Mitgliederimport", "Es konnten keine Mitglieder aus der " +
                    "CVS-Datei importiert werden.");
            return false;
        }

        return true;
    }

}
