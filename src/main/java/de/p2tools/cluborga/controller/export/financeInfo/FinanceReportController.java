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

package de.p2tools.cluborga.controller.export.financeInfo;


import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgConst;
import de.p2tools.cluborga.config.prog.ProgIcons;
import de.p2tools.cluborga.config.prog.ProgInfos;
import de.p2tools.cluborga.controller.ClubFactory;
import de.p2tools.cluborga.data.financeData.FinanceData;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.dialogs.PDirFileChooser;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.guitools.PButton;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.PComboBoxString;
import de.p2tools.p2lib.tools.file.PFileName;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FinanceReportController extends PDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnCancel = new Button("Abbrechen");
    private Button btnHelp;

    private final PComboBoxString cboExportDir = new PComboBoxString();
    private final Button btnExportDir = new Button();
    private final PComboBoxString cboExportFile = new PComboBoxString();
    private final Button btnProposeFileName = new Button();
    private final CheckBox chkShort = new CheckBox("Transaktionen nur Kurzinfos");

    private final ClubConfig clubConfig;
    private final List<FinanceData> financeDataList;
    private boolean ok = false;

    public FinanceReportController(Stage stage, ClubConfig clubConfig, List<FinanceData> financeData) {
        super(stage, clubConfig.EXPORT_FINANCE_REPORT,
                "Finanzbeleg erstellen", true, true);

        this.clubConfig = clubConfig;
        this.financeDataList = financeData;
        init(true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    protected void make() {
        btnHelp = PButton.helpButton(getStage(), "Finanzbeleg erstellen",
                "Es kann ein Beleg für die ausgewählten Finanzdaten erstellt werden. " +
                        "Ein PDF-Dokument wird erstellt " +
                        "und der Speicherort dafür kann hier ausgewählt werden.");

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

        getHBoxTitle().getChildren().add(new Label("Finanzbeleg erstellen"));

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

        gridPane.add(chkShort, 1, ++row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());

        getVBoxCont().getChildren().addAll(/*hBoxTitle,*/ gridPane);

        initListener();
    }

    private void initListener() {
        cboExportFile.init(clubConfig.CBO_LIST_EXPORT_FILE_FINANZREPORT, clubConfig.EXPORT_FILE_FINANZREPORT);
        if (cboExportFile.getSelValue().isEmpty()) {
            cboExportFile.selectElement(clubConfig.clubData.getName() + "." + ProgConst.PDF_SUFFIX);
        }

        cboExportDir.init(clubConfig.CBO_LIST_DIR_FINANZREPORT, clubConfig.EXPORT_DIR_FINANZREPORT);
        btnExportDir.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnExportDir.setTooltip(new Tooltip("Speicherpfad auswählen"));
        btnExportDir.setOnAction(event -> PDirFileChooser.DirChooser(getStage(), cboExportDir,
                ProgInfos.getClubExportsDir(clubConfig.getClubPath()).toString()));

        btnProposeFileName.setGraphic(new ProgIcons().ICON_BUTTON_GUI_GEN_NAME);
        btnProposeFileName.setTooltip(new Tooltip("einen Dateinamen vorschlagen"));
        btnProposeFileName.setOnAction(event -> {
            String fileName = clubConfig.EXPORT_FILE_FINANZREPORT.getValueSafe();

            if (fileName.isEmpty()) {
                fileName = clubConfig.clubData.getName();
            }

            final String suffix = ProgConst.PDF_SUFFIX;
            final String destPath = cboExportDir.getSelValue();

            clubConfig.EXPORT_FILE_FINANZREPORT.setValue(PFileName.getNextFileNameWithDate(destPath, fileName, suffix));
        });

        chkShort.selectedProperty().bindBidirectional(clubConfig.EXPORT_TRANSACTION_SHORT);
        btnOk.disableProperty().bind(cboExportDir.getSelectionModel().selectedItemProperty().isNull()
                .or(cboExportFile.getSelectionModel().selectedItemProperty().isNull()));
    }

    private boolean check() {
        String destDir = cboExportDir.getSelValue();
        String destFile = cboExportFile.getSelValue();

        if (ClubFactory.getDestinationPath(clubConfig.getStage(), destDir, destFile).isEmpty()) {
            return false;
        }

        Path dFile = Paths.get(destDir, destFile);
        if (!FinanceReceiptFactory.writeBeleg(dFile.toString(), financeDataList, clubConfig.EXPORT_TRANSACTION_SHORT.get())) {
            PAlert.showErrorAlert(getStage(), "Finanzbeleg", "Der Finanzbeleg konnte nicht erstellt werden.");
            return false;
        }

        return true;
    }

}
