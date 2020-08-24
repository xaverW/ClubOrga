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

package de.p2tools.clubOrga.controller.export.zip;


import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.clubOrga.controller.ProgSaveFactory;
import de.p2tools.clubOrga.controller.export.ExportFactory;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBoxString;
import de.p2tools.p2Lib.tools.file.PFileName;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ExportZipDialogController extends PDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnCancel = new Button("Abbrechen");
    private Button btnHelp;

    private final PComboBoxString cboExportDir = new PComboBoxString();
    private final Button btnExportDir = new Button();
    private final PComboBoxString cboExportFile = new PComboBoxString();
    private final Button btnProposeFileName = new Button();

    private final ClubConfig clubConfig;
    private boolean ok = false;


    public ExportZipDialogController(ClubConfig clubConfig) {
        super(clubConfig.getStage(), clubConfig.EXPORT_CLUB_DIALOG_SIZE,
                "Verein exportieren", true, true);
        this.clubConfig = clubConfig;
        init(true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    protected void make() {
        btnHelp = PButton.helpButton(getStage(), "Verein exportieren",
                "Beim Export der Vereinsdaten wird der Inhalt des Ordners mit den Vereinsdaten " +
                        "in eine ZIP-Datei gepackt. Diese kann wieder in einen beliebigen Ordner entpackt " +
                        "werden und das Programm kann dann wieder mit diesem Verein arbeiten.");

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

        cboExportDir.setMaxWidth(Double.MAX_VALUE);
        cboExportFile.setMaxWidth(Double.MAX_VALUE);

        getHBoxTitle().getChildren().add(new Label("Vereinsdaten in ZIP-Datei exportieren"));
//        HBox hBoxTitle = GuiFactory.getDialogTitle("Vereinsdaten in ZIP-Datei exportieren");

        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        int row = 0;
        gridPane.add(new Label("Pfad:"), 0, ++row);
        gridPane.add(cboExportDir, 1, row);
        gridPane.add(btnExportDir, 2, row);

        gridPane.add(new Label("Dateiname:"), 0, ++row);
        gridPane.add(cboExportFile, 1, row);
        gridPane.add(btnProposeFileName, 2, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());

        getvBoxCont().getChildren().addAll(/*hBoxTitle,*/ gridPane);

        initListener();
    }

    private void initListener() {
        cboExportFile.init(clubConfig.CBO_LIST_EXPORT_ZIP_FILE, clubConfig.EXPORT_ZIP_FILE);
        if (cboExportFile.getSelValue().isEmpty()) {
            cboExportFile.selectElement(clubConfig.clubData.getName() + "." + ProgConst.ZIP_SUFFIX);
        }

        cboExportDir.init(clubConfig.CBO_LIST_EXPORT_ZIP_DIR, clubConfig.EXPORT_ZIP_DIR);
        btnExportDir.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnExportDir.setTooltip(new Tooltip("Speicherpfad auswählen"));
        btnExportDir.setOnAction(event -> PDirFileChooser.DirChooser(getStage(), cboExportDir,
                ProgInfos.getClubExportsDir(clubConfig.getClubPath()).toString()));

        btnProposeFileName.setGraphic(new ProgIcons().ICON_BUTTON_GUI_GEN_NAME);
        btnProposeFileName.setTooltip(new Tooltip("einen Dateinamen vorschlagen"));
        btnProposeFileName.setOnAction(event -> {
            String fileName = clubConfig.EXPORT_ZIP_FILE.getValueSafe();

            if (fileName.isEmpty()) {
                fileName = clubConfig.clubData.getName();
            }

            final String suffix = ProgConst.ZIP_SUFFIX;
            final String destPath = cboExportDir.getSelValue();

            clubConfig.EXPORT_ZIP_FILE.setValue(PFileName.getNextFileNameWithDate(destPath, fileName, suffix));
        });

        btnOk.disableProperty().bind(cboExportDir.getSelectionModel().selectedItemProperty().isNull()
                .or(cboExportFile.getSelectionModel().selectedItemProperty().isNull()));
    }

    private boolean check() {
        String destDir = cboExportDir.getSelValue();
        String destFile = cboExportFile.getSelValue();

        // erst mal alles sichern
        ProgSaveFactory.saveClub(clubConfig);
        Path pathClub = Paths.get(clubConfig.getClubPath());
//        if (pathClub.toFile().exists() && !pathClub.toFile().isDirectory()) {
//            PAlert.showErrorAlert(getStage(), "Verein", "Der angegebene Speicherort des Vereins " +
//                    "existiert nicht");
//            //???? todo nach dem ersten Start??
//            return false;
//        }

        if (!ExportFactory.check(getStage(), destDir, destFile)) {
            return false;
        }

        Path pathExportFile = Paths.get(destDir, destFile);
        if (!ZipFactory.exportClub(clubConfig.getStage(), pathClub, pathExportFile)) {
            return false;
        }

        return true;
    }

}
