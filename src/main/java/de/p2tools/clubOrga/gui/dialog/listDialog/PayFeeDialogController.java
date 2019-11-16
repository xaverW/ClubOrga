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
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Beitrag bezahlen: Buchungsdatum wird gesetzt,
 * opt. kann ein Eintrag in den Finanzen angelegt werden und
 * eine SEPA-Datei angelegt werden
 */
public class PayFeeDialogController extends abListDialogController {

    final Label lblDatum = new Label("Buchungsdatum");
    final PDatePicker pDatePickerFeeDateBuchungsdatum = new PDatePicker();
    final PLocalDate pDateFeeDataBuchungsdatum = new PLocalDate();

    // Finanzen
    private final PToggleSwitch tglFinances = new PToggleSwitch("Eintrag in den Finanzen anlegen", true);
    private final CheckBox chkTransaction = new CheckBox("als Transaktionen eines Finanzeintrags");
    private final PYearPicker pYearPickerFinanceDataGeschaeftsjahr = new PYearPicker();
    private final PComboBoxObjectId<FinanceCategoryData> cboCategory = new PComboBoxObjectId<>();

    // SEPA
    private final PToggleSwitch tglSepa = new PToggleSwitch("SEPA Dateien erstellen (bei Bankeinzug)", true);
    private final PComboBoxString cboSepaDirList = new PComboBoxString();
    private final PComboBoxString cboSepaFile = new PComboBoxString();
    private final PComboBoxString cboSepaBegleit = new PComboBoxString();
    private final Button btnSepaDirList = new Button();
    private final Button btnProposeSepaFile = new Button();
    private final Button btnProposeSepaBegleit = new Button();
    private final Button btnHelpChk = PButton.helpButton(getStage(), "Transaktionen",
            "Ist diese Option aktiv, werden alle Beiträge als Transaktionen in einem Finanzeintrag " +
                    "erstellt." + P2LibConst.LINE_SEPARATORx2 +
                    "Andernfalls wird für jeden Beitrag ein Finanzeintrag mit einer Transaktion erstellt.");

    public PayFeeDialogController(ClubConfig clubConfig, ObservableList<FeeData> feeDataList) {
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
        chkTransaction.disableProperty().bind(tglFinances.selectedProperty().not());
        btnHelpChk.disableProperty().bind(tglFinances.selectedProperty().not());

//        feeDataList.addListener((ListChangeListener<FeeData>) c -> initChkTransaction());
//        initChkTransaction();

        pYearPickerFinanceDataGeschaeftsjahr.disableProperty().bind(tglFinances.selectedProperty().not());
        pYearPickerFinanceDataGeschaeftsjahr.setMaxWidth(Double.MAX_VALUE);


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


        Label lblOrdner = new Label("Ordner:");
        Label lblSEPA = new Label("SEPA-Datei:");
        Label lblBegleit = new Label("Begleitzettel:");
        lblOrdner.disableProperty().bind(tglSepa.selectedProperty().not());
        lblSEPA.disableProperty().bind(tglSepa.selectedProperty().not());
        lblBegleit.disableProperty().bind(tglSepa.selectedProperty().not());


        btnOk.disableProperty().bind(tglFinances.selectedProperty()
                .and(cboCategory.getSelectionModel().selectedItemProperty().isNull()));

        gridPane.add(lblDatum, 0, row);
        gridPane.add(pDatePickerFeeDateBuchungsdatum, 1, row, 2, 1);
        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(new Label(" "), 0, ++row);

        gridPane.add(tglFinances, 0, ++row, 3, 1);
        gridPane.add(new Label(" "), 0, ++row);

        gridPane.add(new Label(FinanceFieldNames.GESCHAEFTSJAHR_), 0, ++row);
        gridPane.add(pYearPickerFinanceDataGeschaeftsjahr, 1, row, 2, 1);

        gridPane.add(new Label(FinanceFieldNames.KATEGORIE_), 0, ++row);
        gridPane.add(cboCategory, 1, row, 2, 1);

        gridPane.add(chkTransaction, 1, ++row);
        gridPane.add(btnHelpChk, 2, row);

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

//    private void initChkTransaction() {
//        chkTransaction.disableProperty().unbind();
//        btnHelpChk.disableProperty().unbind();
//
//        if (feeDataList.size() <= 1) {
//            chkTransaction.setDisable(true);
//            btnHelpChk.setDisable(true);
//
//        } else {
//            chkTransaction.disableProperty().bind(tglFinances.selectedProperty().not());
//            btnHelpChk.disableProperty().bind(tglFinances.selectedProperty().not());
//        }
//    }

    @Override
    boolean check() {
        boolean ret = true;

        if (tglSepa.isSelected()) {
            // wenn SEPA, dann die Daten erstellen
            Path sepa = Paths.get(clubConfig.PAY_FEE_SEPA_DIR.getValue(), clubConfig.PAY_FEE_SEPA_FILE.getValue());
            Path sepaBegleit = Paths.get(clubConfig.PAY_FEE_SEPA_DIR.getValue(), clubConfig.PAY_FEE_SEPA_BEGLEIT_FILE.getValue());

            if (!PFileUtils.checkFileToCreate(stage, sepa) || !PFileUtils.checkFileToCreate(stage, sepaBegleit)) {
                ret = false;
            }

            // nur die Beiträge mit "Bankeinzug" nehmen
            ObservableList<FeeData> feeDataListSepa = FXCollections.observableArrayList();
            feeDataList.stream().forEach(feeData -> {
                if (feeData.getPaymentTypeData().isEinzug()) {
                    feeDataListSepa.add(feeData);
                }
            });
            ExportSepa.createSepaFile(clubConfig, feeDataListSepa);
        }

        // Beiträge bezahlen
        feeDataList.stream().forEach(fee -> fee.payFeeData(pDateFeeDataBuchungsdatum));

        if (tglFinances.isSelected()) {
            // wenn Finanzen anlegen, dann die Infos dazu sammeln und anlegen
            List<FeeData> feeDataListTmp = new ArrayList<>();

            while (!feeDataList.isEmpty()) {
                feeDataListTmp.clear();
                FeeData fee = feeDataList.remove(0);
                feeDataListTmp.add(fee);
                getKonto(feeDataList, feeDataListTmp, fee.getPaymentTypeData().getId());

                clubConfig.financeDataList.addFinanceFromPayedFee(clubConfig, feeDataListTmp, chkTransaction.isSelected(),
                        pDateFeeDataBuchungsdatum, pYearPickerFinanceDataGeschaeftsjahr.getValue(),
                        cboCategory.getSelValue());
            }
        }

        return ret;
    }

    private void getKonto(List<FeeData> listFrom, List<FeeData> listTo, long paymentType) {
        Iterator<FeeData> it = listFrom.listIterator();
        while (it.hasNext()) {
            FeeData feeData = it.next();
            if (feeData.getPaymentTypeData().getId() == paymentType) {
                listTo.add(feeData);
                it.remove();
            }
        }
    }

}
