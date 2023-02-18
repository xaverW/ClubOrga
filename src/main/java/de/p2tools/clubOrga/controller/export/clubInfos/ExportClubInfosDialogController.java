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

package de.p2tools.clubOrga.controller.export.clubInfos;


import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.clubOrga.controller.ClubFactory;
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

import java.util.ArrayList;

public class ExportClubInfosDialogController extends PDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnCancel = new Button("Abbrechen");
    private Button btnHelp;

    private final PComboBoxString cboExportDir = new PComboBoxString();
    private final Button btnExportDir = new Button();
    private final PComboBoxString cboExportFile = new PComboBoxString();
    private final Button btnProposeFileName = new Button();

    private final ClubConfig clubConfig;
    public String destDir = "";
    public String destFile = "";


    private boolean ok = false;


    public ExportClubInfosDialogController(ClubConfig clubConfig, ArrayList<String> infoslist) {
        super(clubConfig.getStage(), clubConfig.EXPORT_CLUB_INFOS_DIALOG_SIZE,
                "Vereinsinfos exportieren", true, true);
        this.clubConfig = clubConfig;
        init(true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    protected void make() {
        btnHelp = PButton.helpButton(getStage(), "Vereinsinfos exportieren",
                "Beim Export der Vereinsinfos werden die wichtigsten " +
                        "Kenndaten des Vereins in ein PDF geschrieben.");

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

        getHBoxTitle().getChildren().add(new Label("Vereinsinfos in ein PDF schreiben"));

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

        getVBoxCont().getChildren().addAll(/*hBoxTitle,*/ gridPane);

        initListener();
    }

    private void initListener() {
        cboExportFile.init(clubConfig.CBO_LIST_EXPORT_INFOS_FILE, clubConfig.EXPORT_INFOS_FILE);
        if (cboExportFile.getSelValue().isEmpty()) {
            cboExportFile.selectElement(clubConfig.clubData.getName() + "." + ProgConst.PDF_SUFFIX);
        }

        cboExportDir.init(clubConfig.CBO_LIST_EXPORT_INFOS_DIR, clubConfig.EXPORT_INFOS_DIR);
        btnExportDir.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnExportDir.setTooltip(new Tooltip("Speicherpfad auswählen"));
        btnExportDir.setOnAction(event -> PDirFileChooser.DirChooser(getStage(), cboExportDir,
                ProgInfos.getClubExportsDir(clubConfig.getClubPath()).toString()));

        btnProposeFileName.setGraphic(new ProgIcons().ICON_BUTTON_GUI_GEN_NAME);
        btnProposeFileName.setTooltip(new Tooltip("einen Dateinamen vorschlagen"));
        btnProposeFileName.setOnAction(event -> {
            String fileName = clubConfig.EXPORT_INFOS_FILE.getValueSafe();

            if (fileName.isEmpty()) {
                fileName = clubConfig.clubData.getName();
            }

            final String suffix = ProgConst.PDF_SUFFIX;
            final String destPath = cboExportDir.getSelValue();

            clubConfig.EXPORT_INFOS_FILE.setValue(PFileName.getNextFileNameWithDate(destPath, fileName, suffix));
        });

        btnOk.disableProperty().bind(cboExportDir.getSelectionModel().selectedItemProperty().isNull()
                .or(cboExportFile.getSelectionModel().selectedItemProperty().isNull()));
    }

    private boolean check() {
        destDir = cboExportDir.getSelValue();
        destFile = cboExportFile.getSelValue();

        if (ClubFactory.getDestinationPath(getStage(), destDir, destFile).isEmpty()) {
            return false;
        }

        return true;
    }

}
