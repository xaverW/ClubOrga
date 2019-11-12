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

package de.p2tools.clubOrga.gui.dialog.listDialog;


import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.controller.sepa.ExportSepa;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeData;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.dialog.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.*;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.file.PFileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Beitrag bezahlen: Buchungsdatum wird gesetzt,
 * opt. kann ein Eintrag in den Finanzen angelegt werden und
 * eine SEPA-Datei angelegt werden
 */
public class FeePayDialogController extends abListDialogController {

    final Label lblDatum = new Label("Buchungsdatum");
    final PDatePicker pDatePickerFeeDateBuchungsdatum = new PDatePicker();
    final PLocalDate pDateFeeDataBuchungsdatum = new PLocalDate();

    // Finanzen
    private final PToggleSwitch tglFinances = new PToggleSwitch("Eintrag in den Finanzen anlegen", true);
    private final CheckBox chkTransaction = new CheckBox("als Transaktionen eines Finanzeintrags");
    private final PYearPicker pYearPickerFinanceDataGeschaeftsjahr = new PYearPicker();
    private final PComboBoxObjectId<FinanceAccountData> cboAccount = new PComboBoxObjectId<>();
    private final PComboBoxObjectId<FinanceCategoryData> cboCategory = new PComboBoxObjectId<>();

    // SEPA
    private final PToggleSwitch tglSepa = new PToggleSwitch("SEPA Dateien erstellen (bei Bankeinzug)", true);
    private final PComboBoxString cboSepaDirList = new PComboBoxString();
    private final PComboBoxString cboSepaFile = new PComboBoxString();
    private final PComboBoxString cboSepaBegleit = new PComboBoxString();
    private final Button btnSepaDirList = new Button();
    private final Button btnProposeSepaFile = new Button();
    private final Button btnProposeSepaBegleit = new Button();

    public FeePayDialogController(ClubConfig clubConfig, ObservableList<FeeData> feeDataList) {
        super(clubConfig, feeDataList, "Beitrag bezahlen");

        helpHeader = "Beitrag bezahlen";
        helpText = "In dem Dialog können Beiträge bezahlt werden. Die vorgenommenen " +
                "Einstellungen gelten für alle Beiträge in der Liste. Aus der Liste können " +
                "Beiträge auch gelöscht werden wenn sie nicht \"bezahlt\" werden sollen. " + P2LibConst.LINE_SEPARATORx2 +

                "Wenn \"Eintrag in den Finanzen anlegen\" aktiv ist, werden sie auch dort zusätzlich " +
                "eingetragen. Sind Beiträge mit der Zahlart: \"Bankeinzug\" dabei, kann für diese auch " +
                "noch eine SEPA-Datei erstellt werden.";

        init();
    }

    @Override
    public void make() {
        super.make();

        pDatePickerFeeDateBuchungsdatum.setDate(pDateFeeDataBuchungsdatum);
        pDatePickerFeeDateBuchungsdatum.setMaxWidth(Double.MAX_VALUE);

        tglFinances.selectedProperty().bindBidirectional(clubConfig.FEE_DIALOG_ADD_FINANCES);
        chkTransaction.selectedProperty().bindBidirectional(clubConfig.FEE_DIALOG_ADD_TRANSACTIONS);

        pYearPickerFinanceDataGeschaeftsjahr.disableProperty().bind(tglFinances.selectedProperty().not());
        pYearPickerFinanceDataGeschaeftsjahr.setMaxWidth(Double.MAX_VALUE);

        chkTransaction.disableProperty().bind(tglFinances.selectedProperty().not());

        cboAccount.disableProperty().bind(tglFinances.selectedProperty().not());
        cboAccount.init(clubConfig.financeAccountDataList, clubConfig.PAY_FEE_ACCOUNT_ID);
        setFinanceAccount();

        cboAccount.setMaxWidth(Double.MAX_VALUE);
        cboAccount.getSelectionModel().selectedItemProperty().addListener((u, o, n) -> {
            checkFinanceAccountData(n);
        });
        checkFinanceAccountData(cboAccount.getSelValue());

        cboCategory.disableProperty().bind(tglFinances.selectedProperty().not());
        cboCategory.init(clubConfig.financeCategoryDataList, clubConfig.PAY_FEE_CATEGORY_ID);
        cboCategory.setMaxWidth(Double.MAX_VALUE);

        cboSepaDirList.init(clubConfig.PAY_FEE_SEPA_DIR_LIST, clubConfig.PAY_FEE_SEPA_DIR);
        cboSepaDirList.disableProperty().bind(tglSepa.selectedProperty().not());
        cboSepaDirList.setMaxWidth(Double.MAX_VALUE);

        btnSepaDirList.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnSepaDirList.disableProperty().bind(tglSepa.selectedProperty().not());
        btnSepaDirList.setOnAction(event -> PDirFileChooser.DirChooser(this.getStage(), cboSepaDirList));

        cboSepaFile.init(clubConfig.PAY_FEE_SEPA_FILE_LIST, clubConfig.PAY_FEE_SEPA_FILE);
        cboSepaFile.disableProperty().bind(tglSepa.selectedProperty().not());
        cboSepaFile.setMaxWidth(Double.MAX_VALUE);

        btnProposeSepaFile.setGraphic(new ProgIcons().ICON_BUTTON_GUI_GEN_NAME);
        btnProposeSepaFile.disableProperty().bind(tglSepa.selectedProperty().not());
        btnProposeSepaFile.setOnAction(event -> {
            String fileName = "Sepa__" + PDateFactory.getTodayInverseStr() + ".xml";
            clubConfig.PAY_FEE_SEPA_FILE.set(fileName);
        });

        cboSepaBegleit.init(clubConfig.PAY_FEE_SEPA_BEGLEIT_FILE_LIST, clubConfig.PAY_FEE_SEPA_BEGLEIT_FILE);
        cboSepaBegleit.disableProperty().bind(tglSepa.selectedProperty().not());
        cboSepaBegleit.setMaxWidth(Double.MAX_VALUE);

        btnProposeSepaBegleit.setGraphic(new ProgIcons().ICON_BUTTON_GUI_GEN_NAME);
        btnProposeSepaBegleit.disableProperty().bind(tglSepa.selectedProperty().not());
        btnProposeSepaBegleit.setOnAction(event -> {
            String fileName = "Sepa__" + PDateFactory.getTodayInverseStr() + "__Info.txt";
            clubConfig.PAY_FEE_SEPA_BEGLEIT_FILE.set(fileName);
        });

        Button btnHelp = PButton.helpButton(getStage(), "Transaktionen",
                "Ist diese Option aktiv, werden alle Beiträge als Transaktionen in einem Finanzeintrag " +
                        "erstellt." + P2LibConst.LINE_SEPARATORx2 +
                        "Andernfalls wird für jeden Beitrag ein Finanzeintrag mit einer Transaktion erstellt.");

        Label lblOrdner = new Label("Ordner:");
        Label lblSEPA = new Label("SEPA-Datei:");
        Label lblBegleit = new Label("Begleitzettel:");
        lblOrdner.disableProperty().bind(tglSepa.selectedProperty().not());
        lblSEPA.disableProperty().bind(tglSepa.selectedProperty().not());
        lblBegleit.disableProperty().bind(tglSepa.selectedProperty().not());


        btnOk.disableProperty().bind(tglFinances.selectedProperty()
                .and(cboCategory.getSelectionModel().selectedItemProperty().isNull()
                        .or(cboAccount.getSelectionModel().selectedItemProperty().isNull())
                ));

        gridPane.add(lblDatum, 0, row);
        gridPane.add(pDatePickerFeeDateBuchungsdatum, 1, row, 2, 1);
        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(new Label(" "), 0, ++row);

        gridPane.add(tglFinances, 0, ++row, 3, 1);
        gridPane.add(new Label(" "), 0, ++row);

        gridPane.add(new Label(FinanceFieldNames.GESCHAEFTSJAHR_), 0, ++row);
        gridPane.add(pYearPickerFinanceDataGeschaeftsjahr, 1, row, 2, 1);

        gridPane.add(new Label(FinanceFieldNames.KONTO_), 0, ++row);
        gridPane.add(cboAccount, 1, row, 2, 1);

        gridPane.add(new Label(FinanceFieldNames.KATEGORIE_), 0, ++row);
        gridPane.add(cboCategory, 1, row, 2, 1);

        gridPane.add(chkTransaction, 1, ++row);
        gridPane.add(btnHelp, 2, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(tglSepa, 0, ++row, 3, 1);
        gridPane.add(new Label(" "), 0, ++row);

        gridPane.add(lblOrdner, 0, ++row);
        gridPane.add(cboSepaDirList, 1, row);
        gridPane.add(btnSepaDirList, 2, row);

        gridPane.add(lblSEPA, 0, ++row);
        gridPane.add(cboSepaFile, 1, row);
        gridPane.add(btnProposeSepaFile, 2, row);

        gridPane.add(lblBegleit, 0, ++row);
        gridPane.add(cboSepaBegleit, 1, row);
        gridPane.add(btnProposeSepaBegleit, 2, row);

    }

    private void setFinanceAccount() {
        if (feeDataList.isEmpty()) {
            return;
        }

        long id = feeDataList.get(0).getZahlart();
        PaymentTypeData pa = clubConfig.paymentTypeDataList.stream()
                .filter(paymentTypeData -> paymentTypeData.getId() == id)
                .findFirst().orElse(null);
        if (pa != null) {
            cboAccount.getSelectionModel().select(pa.getFinanceAccountData());
        }
    }

    private boolean checkBankeinzugInFeeList() {
        FeeData fd = feeDataList.stream().filter(f -> f.isBankeinzug()).findAny().orElse(null);
        return fd != null;
    }

    private boolean bound = false;

    private void checkFinanceAccountData(FinanceAccountData financeAccountData) {
        if (!checkBankeinzugInFeeList()) {
            tglSepa.setSelected(false);
            tglSepa.setDisable(true);
            return;
        }

        if (financeAccountData != null && financeAccountData.isGiro()) {
            tglSepa.setDisable(false);

            if (!bound) {
                tglSepa.selectedProperty().bindBidirectional(clubConfig.FEE_DIALOG_ADD_DTAUS);
                bound = true;
            }
        } else {
            if (bound) {
                tglSepa.selectedProperty().unbindBidirectional(clubConfig.FEE_DIALOG_ADD_DTAUS);
                bound = false;
            }

            tglSepa.setSelected(false);
            tglSepa.setDisable(true);
        }
    }

    @Override
    boolean check() {
        boolean ret = true;

        if (tglSepa.isSelected()) {
            Path sepa = Paths.get(clubConfig.PAY_FEE_SEPA_DIR.getValue(), clubConfig.PAY_FEE_SEPA_FILE.getValue());
            Path sepaBegleit = Paths.get(clubConfig.PAY_FEE_SEPA_DIR.getValue(), clubConfig.PAY_FEE_SEPA_BEGLEIT_FILE.getValue());

            if (!PFileUtils.checkFileToCreate(stage, sepa) || !PFileUtils.checkFileToCreate(stage, sepaBegleit)) {
                ret = false;
            }

            // nur die Beiträge mit "Bankeinzug" nehmen
            ObservableList<FeeData> feeDataListSepa = FXCollections.observableArrayList();
            feeDataList.stream().forEach(feeData -> {
                if (feeData.isBankeinzug()) {
                    feeDataListSepa.add(feeData);
                }
            });
            ExportSepa.createSepaFile(clubConfig, cboAccount.getSelValue(), feeDataListSepa);
        }

        if (tglFinances.isSelected()) {
            feeDataList.stream().forEach(fee -> fee.payFeeData(pDateFeeDataBuchungsdatum));
            clubConfig.financeDataList.addFinanceFromPayedFee(clubConfig, feeDataList, clubConfig.FEE_DIALOG_ADD_TRANSACTIONS.get(),
                    pDateFeeDataBuchungsdatum, pYearPickerFinanceDataGeschaeftsjahr.getValue(),
                    cboAccount.getSelValue(), cboCategory.getSelValue());
        } else {
            feeDataList.stream().forEach(fee -> fee.payFeeData(pDateFeeDataBuchungsdatum));
        }

        return ret;
    }


}
