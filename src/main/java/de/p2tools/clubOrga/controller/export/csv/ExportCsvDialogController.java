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
import de.p2tools.clubOrga.controller.ClubFactory;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.feeData.FeeFieldNames;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.clubOrga.data.financeData.FinanceReportData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberFieldNames;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBoxString;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2Lib.tools.file.PFileName;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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
    private final List<FeeData> feeDataList;
    private final List<FinanceData> financeDataList;
    private final List<FinanceReportData> financeReportDataList;
    private boolean ok = false;

    enum exporting {MEMBER, FEE, FINANCES, FINANCEREPORTS}

    private final exporting exportingWhat;

    public ExportCsvDialogController(Stage ownerForCenteringDialog, ClubConfig clubConfig,
                                     List<MemberData> memberDataList,
                                     List<FeeData> feeDataList,
                                     List<FinanceData> financeDataList,
                                     List<FinanceReportData> financeReportDataList) {


        super(ownerForCenteringDialog, clubConfig.EXPORT_CSV_DIALOG_SIZE,
                "Daten in CVS-Datei exportieren", true, true);

        this.clubConfig = clubConfig;
        this.memberDataList = memberDataList;
        this.feeDataList = feeDataList;
        this.financeDataList = financeDataList; //aus Menü Finanzen
        this.financeReportDataList = financeReportDataList; //aus Menü Finanzübersicht: Konten..., Kategorien...

        if (memberDataList != null) {
            exportingWhat = exporting.MEMBER;
        } else if (feeDataList != null) {
            exportingWhat = exporting.FEE;
        } else if (financeDataList != null) {
            exportingWhat = exporting.FINANCES;
        } else {
            exportingWhat = exporting.FINANCEREPORTS;
        }
        init(true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    protected void make() {
        String text;
        switch (exportingWhat) {
            case MEMBER:
                text = "Mitgliederdaten";
                break;
            case FEE:
                text = "Beitragsdaten";
                break;
            case FINANCES:
            case FINANCEREPORTS:
                text = "Finanzdaten";
                break;
            default:
                text = "Daten";
        }
        btnHelp = PButton.helpButton(getStage(), text +
                        " in CVS-Datei exportieren",
                "Hier können die " + text +
                        " in eine CSV-Datei exportiert werden. " +
                        "Damit ist es möglich, die Daten auch in anderen Programmen zu verwenden." +
                        "\n\n" +
                        "(Das Dateiformat CSV beschreibt den Aufbau einer Textdatei " +
                        "zur Speicherung oder zum Austausch einfach strukturierter Daten.)");

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

        getHBoxTitle().getChildren().add(new Label(text +
                " in CVS-Datei exportieren"));

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

        getvBoxCont().getChildren().addAll(gridPane);
        if (exportingWhat == exporting.MEMBER) {
            addMemberColumn();
        } else if (exportingWhat == exporting.FEE) {
            addFeeColumn();
        } else if (exportingWhat == exporting.FINANCES) {
            addFinanceColumn();
        }

        initListener();
    }

    private void addMemberColumn() {

        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(25);

        int row = 0;

        CheckBox chkNo = new CheckBox(MemberFieldNames.NR);
        chkNo.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Nr);
        gridPane.add(chkNo, 0, ++row);

        CheckBox chkNachname = new CheckBox(MemberFieldNames.NACHNAME);
        chkNachname.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Nachname);
        gridPane.add(chkNachname, 0, ++row);

        CheckBox chkVorname = new CheckBox(MemberFieldNames.VORNAME);
        chkVorname.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Vorname);
        gridPane.add(chkVorname, 0, ++row);

        CheckBox chkAnrede = new CheckBox(MemberFieldNames.ANREDE);
        chkAnrede.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Anrede);
        gridPane.add(chkAnrede, 0, ++row);

        CheckBox chkEmail = new CheckBox(MemberFieldNames.EMAIL);
        chkEmail.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Email);
        gridPane.add(chkEmail, 0, ++row);

        CheckBox chkTelefon = new CheckBox(MemberFieldNames.TELEFON);
        chkTelefon.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Telefon);
        gridPane.add(chkTelefon, 0, ++row);

        CheckBox chkStrasse = new CheckBox(MemberFieldNames.STRASSE);
        chkStrasse.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Strasse);
        gridPane.add(chkStrasse, 0, ++row);

        row = 0;
        CheckBox chkPLZ = new CheckBox(MemberFieldNames.PLZ);
        chkPLZ.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_PLZ);
        gridPane.add(chkPLZ, 1, ++row);

        CheckBox chkOrt = new CheckBox(MemberFieldNames.ORT);
        chkOrt.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Ort);
        gridPane.add(chkOrt, 1, ++row);

        CheckBox chkLand = new CheckBox(MemberFieldNames.LAND);
        chkLand.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Land);
        gridPane.add(chkLand, 1, ++row);

        CheckBox chkStatus = new CheckBox(MemberFieldNames.STATUS);
        chkStatus.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Status);
        gridPane.add(chkStatus, 1, ++row);

        CheckBox chkBeitrag = new CheckBox(MemberFieldNames.BEITRAG);
        chkBeitrag.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Beitrag);
        gridPane.add(chkBeitrag, 1, ++row);

        CheckBox chkBeitragssatz = new CheckBox(MemberFieldNames.BEITRAGSSATZ);
        chkBeitragssatz.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Beitragssatz);
        gridPane.add(chkBeitragssatz, 1, ++row);

        CheckBox chkBank = new CheckBox(MemberFieldNames.BANK);
        chkBank.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Bank);
        gridPane.add(chkBank, 1, ++row);

        row = 0;
        CheckBox chkIban = new CheckBox(MemberFieldNames.IBAN);
        chkIban.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Iban);
        gridPane.add(chkIban, 2, ++row);

        CheckBox chkBic = new CheckBox(MemberFieldNames.BIC);
        chkBic.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Bic);
        gridPane.add(chkBic, 2, ++row);

        CheckBox chkKontoinhaber = new CheckBox(MemberFieldNames.KONTOINHABER);
        chkKontoinhaber.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Kontoinhaber);
        gridPane.add(chkKontoinhaber, 2, ++row);

        CheckBox chkZahlart = new CheckBox(MemberFieldNames.ZAHLART);
        chkZahlart.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Zahlart);
        gridPane.add(chkZahlart, 2, ++row);

        CheckBox chkZahlungsbeginn = new CheckBox(MemberFieldNames.ZAHLUNGSBEGINN);
        chkZahlungsbeginn.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Zahlungsbeginn);
        gridPane.add(chkZahlungsbeginn, 2, ++row);

        CheckBox chkSepabeginn = new CheckBox(MemberFieldNames.SEPABEGINN);
        chkSepabeginn.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Sepabeginn);
        gridPane.add(chkSepabeginn, 2, ++row);

        CheckBox chkBeitritt = new CheckBox(MemberFieldNames.BEITRITT);
        chkBeitritt.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_DATA_Beitritt);
        gridPane.add(chkBeitritt, 2, ++row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcPrefSize());

        PToggleSwitch tglAll = new PToggleSwitch("Alle Felder exportieren");
        tglAll.selectedProperty().bindBidirectional(clubConfig.MEMBER_EXPORT_ALL);
        gridPane.setDisable(tglAll.isSelected());
        tglAll.selectedProperty().addListener((observable, oldValue, newValue) -> gridPane.setDisable(tglAll.isSelected()));

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(25, 10, 10, 10));
        HBox.setHgrow(tglAll, Priority.ALWAYS);
        hBox.getChildren().add(tglAll);

        getvBoxCont().getChildren().addAll(gridPane, hBox);
    }

    private void addFeeColumn() {

        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(25);

        int row = 0;
        CheckBox chkNo = new CheckBox(FeeFieldNames.NR);
        chkNo.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_Nr);
        gridPane.add(chkNo, 0, row++);

        CheckBox chkMemberNo = new CheckBox(FeeFieldNames.MEMBER_NO);
        chkMemberNo.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_MemberNr);
        gridPane.add(chkMemberNo, 0, row++);

        CheckBox chkMembername = new CheckBox(FeeFieldNames.MEMBER_NAME);
        chkMembername.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_MemberName);
        gridPane.add(chkMembername, 0, row++);

        CheckBox chkBetrag = new CheckBox(FeeFieldNames.BETRAG);
        chkBetrag.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_Betrag);
        gridPane.add(chkBetrag, 0, row++);

        row = 0;
        CheckBox chkBetragInWords = new CheckBox(FeeFieldNames.BETRAG_IN_WORDS);
        chkBetragInWords.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_BetragInWords);
        gridPane.add(chkBetragInWords, 1, row++);

        CheckBox chkJahr = new CheckBox(FeeFieldNames.JAHR);
        chkJahr.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_Jahr);
        gridPane.add(chkJahr, 1, row++);

        CheckBox chkZahlart = new CheckBox(FeeFieldNames.ZAHLART);
        chkZahlart.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_Zahlart);
        gridPane.add(chkZahlart, 1, row++);

        CheckBox chkBezahlt = new CheckBox(FeeFieldNames.BEZAHLT);
        chkBezahlt.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_Bezahlt);
        gridPane.add(chkBezahlt, 1, row++);

        row = 0;
        CheckBox chkRechnung = new CheckBox(FeeFieldNames.RECHNUNG);
        chkRechnung.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_Rechnung);
        gridPane.add(chkRechnung, 2, row++);

        CheckBox chkSpendenQ = new CheckBox(FeeFieldNames.SPENDEN_Q);
        chkSpendenQ.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_SpendenQ);
        gridPane.add(chkSpendenQ, 2, row++);

        CheckBox chkErstelldatum = new CheckBox(FeeFieldNames.ERSTELLDATUM);
        chkErstelldatum.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_Erstelldatum);
        gridPane.add(chkErstelldatum, 2, row++);

        CheckBox chkText = new CheckBox(FeeFieldNames.TEXT);
        chkText.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_DATA_Text);
        gridPane.add(chkText, 2, row++);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcPrefSize());

        PToggleSwitch tglAll = new PToggleSwitch("Alle Felder exportieren");
        tglAll.selectedProperty().bindBidirectional(clubConfig.FEE_EXPORT_ALL);
        gridPane.setDisable(tglAll.isSelected());
        tglAll.selectedProperty().addListener((observable, oldValue, newValue) -> gridPane.setDisable(tglAll.isSelected()));

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(25, 10, 10, 10));
        HBox.setHgrow(tglAll, Priority.ALWAYS);
        hBox.getChildren().add(tglAll);

        getvBoxCont().getChildren().addAll(gridPane, hBox);
    }

    private void addFinanceColumn() {

        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(25);

        int row = 0;
        CheckBox chkNo = new CheckBox(FinanceFieldNames.NR);
        chkNo.selectedProperty().bindBidirectional(clubConfig.FINANCE_EXPORT_DATA_Nr);
        gridPane.add(chkNo, 0, row++);

        CheckBox chkNachname = new CheckBox(FinanceFieldNames.BELEG_NR);
        chkNachname.selectedProperty().bindBidirectional(clubConfig.FINANCE_EXPORT_DATA_BelegNr);
        gridPane.add(chkNachname, 0, row++);

        CheckBox chkVorname = new CheckBox(FinanceFieldNames.GESAMTBETRAG);
        chkVorname.selectedProperty().bindBidirectional(clubConfig.FINANCE_EXPORT_DATA_Gesamtbetrag);
        gridPane.add(chkVorname, 0, row++);

        row = 0;
        CheckBox chkTelefon = new CheckBox(FinanceFieldNames.KONTO);
        chkTelefon.selectedProperty().bindBidirectional(clubConfig.FINANCE_EXPORT_DATA_Konto);
        gridPane.add(chkTelefon, 1, row++);

        CheckBox chkStrasse = new CheckBox(FinanceFieldNames.KATEGORIE);
        chkStrasse.selectedProperty().bindBidirectional(clubConfig.FINANCE_EXPORT_DATA_Kategorie);
        gridPane.add(chkStrasse, 1, row++);

        CheckBox chkPLZ = new CheckBox(FinanceFieldNames.GESCHAEFTSJAHR);
        chkPLZ.selectedProperty().bindBidirectional(clubConfig.FINANCE_EXPORT_DATA_Geschäftsjahr);
        gridPane.add(chkPLZ, 1, row++);

        row = 0;
        CheckBox chkOrt = new CheckBox(FinanceFieldNames.BUCHUNGSDATUM);
        chkOrt.selectedProperty().bindBidirectional(clubConfig.FINANCE_EXPORT_DATA_Buchungsdatum);
        gridPane.add(chkOrt, 2, row++);

        CheckBox chkLand = new CheckBox(FinanceFieldNames.ERSTELLDATUM);
        chkLand.selectedProperty().bindBidirectional(clubConfig.FINANCE_EXPORT_DATA_Erstelldatum);
        gridPane.add(chkLand, 2, row++);

        CheckBox chkStatus = new CheckBox(FinanceFieldNames.TEXT);
        chkStatus.selectedProperty().bindBidirectional(clubConfig.FINANCE_EXPORT_DATA_Text);
        gridPane.add(chkStatus, 2, row++);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcPrefSize());

        PToggleSwitch tglAll = new PToggleSwitch("Alle Felder exportieren");
        tglAll.selectedProperty().bindBidirectional(clubConfig.FINANCE_EXPORT_ALL);
        gridPane.setDisable(tglAll.isSelected());
        tglAll.selectedProperty().addListener((observable, oldValue, newValue) -> gridPane.setDisable(tglAll.isSelected()));

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(25, 10, 10, 10));
        HBox.setHgrow(tglAll, Priority.ALWAYS);
        hBox.getChildren().add(tglAll);

        getvBoxCont().getChildren().addAll(gridPane, hBox);
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

        if (ClubFactory.getDestinationPath(clubConfig.getStage(), destDir, destFile).isEmpty()) {
            return false;
        }

        Path dFile = Paths.get(destDir, destFile);
        switch (exportingWhat) {
            case MEMBER:
                if (!CsvFactoryMember.exportMember(clubConfig, memberDataList, dFile)) {
                    PAlert.showErrorAlert(getStage(), "CVS-Datei", "Die CVS-Datei konnte nicht erstellt werden.");
                    return false;
                }
                break;
            case FEE:
                if (!CsvFactoryFee.exportFee(clubConfig, feeDataList, dFile)) {
                    PAlert.showErrorAlert(getStage(), "CVS-Datei", "Die CVS-Datei konnte nicht erstellt werden.");
                    return false;
                }
                break;
            case FINANCES:
                if (!CsvFactoryFinance.exportFinances(clubConfig, financeDataList, dFile)) {
                    PAlert.showErrorAlert(getStage(), "CVS-Datei", "Die CVS-Datei konnte nicht erstellt werden.");
                    return false;
                }
                break;
            case FINANCEREPORTS:
            default:
                if (!CsvFactoryFinanceReport.exportFinancesReport(clubConfig, financeReportDataList, dFile)) {
                    PAlert.showErrorAlert(getStage(), "CVS-Datei", "Die CVS-Datei konnte nicht erstellt werden.");
                    return false;
                }
        }

        return true;
    }

}
