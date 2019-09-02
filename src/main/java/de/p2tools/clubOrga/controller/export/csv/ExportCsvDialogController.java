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
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.clubOrga.controller.export.ExportFactory;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceReportData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.gui.tools.GuiFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialog.PDialogExtra;
import de.p2tools.p2Lib.dialog.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBoxString;
import de.p2tools.p2Lib.tools.file.PFileName;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ExportCsvDialogController extends PDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnCancel = new Button("Abbrechen");
    private Button btnHelp;

    private final PComboBoxString cboExportDir = new PComboBoxString();
    private final Button btnExportDir = new Button();
    private final PComboBoxString cboExportFile = new PComboBoxString();
    private final Button btnProposeFileName = new Button();

    private final ClubConfig clubConfig;
    private final List<MemberData> memberDataList;
    private final List<FinanceData> financeDataList;
    private final List<FinanceReportData> financeReportDataList;
    private boolean ok = false;

    enum exporting {MEMBER, FINANCES, FINANCEREPORTS}

    private final exporting exportingWhat;

    public ExportCsvDialogController(Stage ownerForCenteringDialog, ClubConfig clubConfig,
                                     List<MemberData> memberDataList) {

        super(ownerForCenteringDialog, clubConfig.EXPORT_CSV_DIALOG_SIZE,
                "Daten in CVS-Datei exportieren");

        this.clubConfig = clubConfig;
        this.memberDataList = memberDataList;
        this.financeDataList = null;
        this.financeReportDataList = null;

        exportingWhat = exporting.MEMBER;
        init(getvBoxDialog(), true);
    }

    public ExportCsvDialogController(Stage ownerForCenteringDialog, ClubConfig clubConfig,
                                     List<FinanceData> financeDataList,
                                     List<FinanceReportData> financeReportDataList) {

        super(ownerForCenteringDialog, clubConfig.EXPORT_CSV_DIALOG_SIZE,
                "Daten in CVS-Datei exportieren");

        this.clubConfig = clubConfig;
        this.memberDataList = null;
        this.financeDataList = financeDataList;
        this.financeReportDataList = financeReportDataList;

        if (financeDataList != null) {
            exportingWhat = exporting.FINANCES;
        } else {
            exportingWhat = exporting.FINANCEREPORTS;
        }
        init(getvBoxDialog(), true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    protected void make() {
        btnHelp = PButton.helpButton(getStage(), (exportingWhat == exporting.MEMBER ? "Mitgliederdaten" : "Finanzdaten") +
                        " in CVS-Datei exportieren",
                "Hier können die " + (exportingWhat == exporting.MEMBER ? "Mitgliederdaten" : "Finanzdaten") +
                        " in eine CSV-Datei exportiert werden. " +
                        "Damit ist es möglich, die Daten auch in anderen Programmen zu verwenden." +
                        "\n\n" +
                        "(Das Dateiformat CSV beschreibt den Aufbau einer Textdatei " +
                        "zur Speicherung oder zum Austausch einfach strukturierter Daten.)");

        HBox hBoxHelp = new HBox();
        hBoxHelp.setAlignment(Pos.CENTER_LEFT);
        hBoxHelp.getChildren().addAll(btnHelp);

        HBox hBox = new HBox();
        HBox.setHgrow(hBox, Priority.ALWAYS);
        getHboxOk().getChildren().addAll(btnHelp, hBox, btnOk, btnCancel);

        btnOk.setOnAction(a -> {
            if (check()) {
                ok = true;
                close();
            }
        });
        btnCancel.setOnAction(a -> close());

        cboExportDir.setMaxWidth(Double.MAX_VALUE);
        cboExportFile.setMaxWidth(Double.MAX_VALUE);

        HBox hBoxTitle = GuiFactory.getDialogTitle((exportingWhat == exporting.MEMBER ? "Mitgliederdaten" : "Finanzdaten") +
                " in CVS-Datei exportieren");

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

        getVboxCont().getChildren().addAll(hBoxTitle, gridPane);

        initListener();
    }

    private void initListener() {
        cboExportFile.init(clubConfig.CBO_LIST_EXPORT_CSV_FILE, clubConfig.EXPORT_CSV_FILE);
        if (cboExportFile.getSelValue().isEmpty()) {
            cboExportFile.selectElement(clubConfig.clubData.getName() + "." + ProgConst.CSV_SUFFIX);
        }

        cboExportDir.init(clubConfig.CBO_LIST_EXPORT_CSV_DIR, clubConfig.EXPORT_CSV_DIR);
        btnExportDir.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnExportDir.setTooltip(new Tooltip("Speicherpfad auswählen"));
        btnExportDir.setOnAction(event -> PDirFileChooser.DirChooser(getStage(), cboExportDir,
                ProgInfos.getClubExportsDir(clubConfig.getClubPath()).toString()));

        btnProposeFileName.setGraphic(new ProgIcons().ICON_BUTTON_GUI_GEN_NAME);
        btnProposeFileName.setTooltip(new Tooltip("einen Dateinamen vorschlagen"));
        btnProposeFileName.setOnAction(event -> {
            String fileName = clubConfig.EXPORT_CSV_FILE.getValueSafe();

            if (fileName.isEmpty()) {
                fileName = clubConfig.clubData.getName();
            }

            final String suffix = ProgConst.CSV_SUFFIX;
            final String destPath = cboExportDir.getSelValue();

            clubConfig.EXPORT_CSV_FILE.setValue(PFileName.getNextFileNameWithDate(destPath, fileName, suffix));
        });

        btnOk.disableProperty().bind(cboExportDir.getSelectionModel().selectedItemProperty().isNull()
                .or(cboExportFile.getSelectionModel().selectedItemProperty().isNull()));
    }

    private boolean check() {
        String destDir = cboExportDir.getSelValue();
        String destFile = cboExportFile.getSelValue();

        if (!ExportFactory.check(getStage(), destDir, destFile)) {
            return false;
        }

        Path dFile = Paths.get(destDir, destFile);
        switch (exportingWhat) {
            case MEMBER:
                if (!CsvFactory.exportMember(memberDataList, dFile)) {
                    PAlert.showErrorAlert(getStage(), "CVS-Datei", "Die CVS-Datei konnte nicht erstellt werden.");
                    return false;
                }
                break;
            case FINANCES:
                if (!CsvFactory.exportFinances(financeDataList, dFile)) {
                    PAlert.showErrorAlert(getStage(), "CVS-Datei", "Die CVS-Datei konnte nicht erstellt werden.");
                    return false;
                }
                break;
            case FINANCEREPORTS:
            default:
                if (!CsvFactory.exportFinancesReport(clubConfig, financeReportDataList, dFile)) {
                    PAlert.showErrorAlert(getStage(), "CVS-Datei", "Die CVS-Datei konnte nicht erstellt werden.");
                    return false;
                }
        }

        return true;
    }

}
