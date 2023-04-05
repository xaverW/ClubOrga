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

package de.p2tools.cluborga.gui.dialog.listDialog;


import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgIcons;
import de.p2tools.cluborga.controller.ClubFactory;
import de.p2tools.cluborga.controller.newsletter.Newsletter;
import de.p2tools.cluborga.controller.newsletter.NewsletterFactory;
import de.p2tools.cluborga.data.feeData.FeeData;
import de.p2tools.p2lib.dialogs.PDirFileChooser;
import de.p2tools.p2lib.guitools.PComboBoxString;
import de.p2tools.p2lib.guitools.PLDatePicker;
import de.p2tools.p2lib.guitools.ptoggleswitch.PToggleSwitch;
import de.p2tools.p2lib.tools.file.PFileName;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * Rechnung erstellen: Rechnungsdatum wird gesetzt,
 * Serienbrief kann optional noch erstellt werden
 */
public class BillForFeeDialogController extends abListDialogController {

    final Label lblDatum = new Label("Buchungsdatum");
    final PLDatePicker pDatePickerFeeDateBuchungsdatum = new PLDatePicker();

    private final PToggleSwitch tglBill = new PToggleSwitch("Rechnung erstellen", true);

    private final PComboBoxString pCboTemplate = new PComboBoxString();
    private final PComboBoxString pCboNewsletterPath = new PComboBoxString();
    private final PComboBoxString pCboNewsletterName = new PComboBoxString();
    private final Label lblTemplate = new Label("Vorlage:");
    private final Label lblPath = new Label("Pfad:");
    private final Label lblName = new Label("Dateiname:");
    private final Label lblNewsletter = new Label("Rechnung speichern");

    private final Button btnTemplate = new Button();
    private final Button btnNewsletterPath = new Button();
    private final Button btnProposeFileName = new Button();

    final StringProperty newsletterTemplateProperty;
    final StringProperty newsletterDirProperty;
    final StringProperty newsletterNameProperty;
    final BooleanProperty addNewsletterProperty;
    private final String proposeFileName;

    private final ClubConfig clubConfig;

    private final TYPE type;

    public enum TYPE {BILL, SQ}

    public BillForFeeDialogController(ClubConfig clubConfig, ObservableList<FeeData> feeDataList, TYPE type) {
        super(clubConfig, clubConfig.BILL_FOR_FEE_DIALOG_SIZE, clubConfig.BILL_FOR_FEE_DIALOG_DIVIDER,
                feeDataList, type.equals(TYPE.BILL) ? "Rechnung erstellen" : "Spendenquittung erstellen");

        this.clubConfig = clubConfig;
        this.type = type;
        switch (type) {
            case BILL:
                newsletterTemplateProperty = clubConfig.NEWSLETTER_TEMPLATE_BILL;
                newsletterDirProperty = clubConfig.NEWSLETTER_DIR_BILL;
                newsletterNameProperty = clubConfig.NEWSLETTER_NAME_BILL;
                addNewsletterProperty = clubConfig.FEE_DIALOG_ADD_BILL;

                helpHeader = "Rechnung erstellen";
                helpText = "Hiermit wird das Rechnungsdatum des Beitrags gesetzt, was anzeigt, dass " +
                        "die Rechnung für den Beitrag erstellt wurde (und wann). Zusätzlich kann auch " +
                        "tatsächlich eine Rechnung aus einer Vorlage erstellt werden.";
                lblDatum.setText("Rechnungsdatum:");
                proposeFileName = "Rechnung";
                lblNewsletter.setText("Rechnung speichern");
                break;
            case SQ:
            default:
                newsletterTemplateProperty = clubConfig.NEWSLETTER_TEMPLATE_SQ;
                newsletterDirProperty = clubConfig.NEWSLETTER_DIR_SQ;
                newsletterNameProperty = clubConfig.NEWSLETTER_NAME_SQ;
                addNewsletterProperty = clubConfig.FEE_DIALOG_ADD_SQ;

                helpHeader = "Spendenquittung erstellen";
                helpText = "Hiermit wird das Datum der Spendenquittung des Beitrags gesetzt, was anzeigt, dass " +
                        "die Spendenquittung für den Beitrag erstellt wurde (und wann). Zusätzlich kann auch " +
                        "tatsächlich eine Spendenquittung aus einer Vorlage erstellt werden.";
                lblDatum.setText("Spendendatum:");
                proposeFileName = "Spendenquittung";
                lblNewsletter.setText("Spendenquittung speichern");
                tglBill.setText("Spendenquittung erstellen");
                break;

        }
        init();
    }

    @Override
    public void make() {
        super.make();

        pDatePickerFeeDateBuchungsdatum.setMaxWidth(Double.MAX_VALUE);

        tglBill.selectedProperty().bindBidirectional(addNewsletterProperty);

        pCboTemplate.setMaxWidth(Double.MAX_VALUE);
        pCboNewsletterName.setMaxWidth(Double.MAX_VALUE);
        pCboNewsletterPath.setMaxWidth(Double.MAX_VALUE);

        btnTemplate.setTooltip(new Tooltip("eine Vorlage auswählen"));
        btnNewsletterPath.setTooltip(new Tooltip("einen Ordner zum Speichern auswählen"));
        btnProposeFileName.setTooltip(new Tooltip("einen Dateinamen vorschlagen"));

        gridPane.add(lblDatum, 0, row);
        gridPane.add(pDatePickerFeeDateBuchungsdatum, 1, row, 2, 1);
        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(new Label(" "), 0, ++row);


        gridPane.add(tglBill, 0, ++row, 3, 1);
        gridPane.add(new Label(" "), 0, ++row);

        gridPane.add(lblTemplate, 0, ++row);
        gridPane.add(pCboTemplate, 1, row);
        gridPane.add(btnTemplate, 2, row);

        gridPane.add(new Label(" "), 0, ++row);

        gridPane.add(lblNewsletter, 0, ++row, 2, 1);
        gridPane.add(lblPath, 0, ++row);
        gridPane.add(pCboNewsletterPath, 1, row);
        gridPane.add(btnNewsletterPath, 2, row);

        gridPane.add(lblName, 0, ++row);
        gridPane.add(pCboNewsletterName, 1, row);
        gridPane.add(btnProposeFileName, 2, row);

        initListener();
    }

    private void initListener() {
        pCboTemplate.init(clubConfig.CBO_LIST_NEWSLETTER_TEMPLATE, newsletterTemplateProperty);
        pCboNewsletterPath.init(clubConfig.CBO_LIST_NEWSLETTER_DIR, clubConfig.getClubPath(), newsletterDirProperty);
        pCboNewsletterName.init(clubConfig.CBO_LIST_NEWSLETTER_FILE, newsletterNameProperty);

        pCboTemplate.disableProperty().bind(tglBill.selectedProperty().not());
        pCboNewsletterPath.disableProperty().bind(tglBill.selectedProperty().not());
        pCboNewsletterName.disableProperty().bind(tglBill.selectedProperty().not());
        lblTemplate.disableProperty().bind(tglBill.selectedProperty().not());
        lblNewsletter.disableProperty().bind(tglBill.selectedProperty().not());
        lblPath.disableProperty().bind(tglBill.selectedProperty().not());
        lblName.disableProperty().bind(tglBill.selectedProperty().not());
        btnTemplate.disableProperty().bind(tglBill.selectedProperty().not());
        btnNewsletterPath.disableProperty().bind(tglBill.selectedProperty().not());
        btnProposeFileName.disableProperty().bind(tglBill.selectedProperty().not());

        btnTemplate.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnTemplate.setOnAction(event -> PDirFileChooser.FileChooser(clubConfig.getStage(), pCboTemplate, clubConfig.getClubPath()));

        btnNewsletterPath.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnNewsletterPath.setOnAction(event -> PDirFileChooser.DirChooser(clubConfig.getStage(), pCboNewsletterPath, clubConfig.getClubPath()));

        btnProposeFileName.setGraphic(new ProgIcons().ICON_BUTTON_GUI_GEN_NAME);
        btnProposeFileName.setOnAction(event -> {
            getProposedFileName();
        });
//        getProposedFileName();

        btnOk.disableProperty().bind(tglBill.selectedProperty().and(
                        pCboTemplate.getSelectionModel().selectedItemProperty().isNull()
                                .or(pCboTemplate.getSelectionModel().selectedItemProperty().isEqualTo(""))

                                .or(pCboNewsletterPath.getSelectionModel().selectedItemProperty().isNull())
                                .or(pCboNewsletterPath.getSelectionModel().selectedItemProperty().isEqualTo(""))

                                .or(pCboNewsletterName.getSelectionModel().selectedItemProperty().isNull())
                                .or(pCboNewsletterName.getSelectionModel().selectedItemProperty().isEqualTo(""))
                )
        );

    }

    private void getProposedFileName() {
        String fileName = newsletterNameProperty.getValueSafe();
        NewsletterFactory.NEWSLETTER_TYPE newsletterType;

        final String sourceFile;
        if (/*pCboTemplate.getSelValue().isEmpty() ||*/
                (sourceFile = ClubFactory.getAbsolutFilePath(clubConfig, getStage(), pCboTemplate.getSelValue()))
                        .isEmpty()) {
            return;
        }
        if ((newsletterType = NewsletterFactory.getType(sourceFile)) == null) {
            return;
        }

        if (fileName.isEmpty()) {
            fileName = proposeFileName;
        }

        final String suffix = NewsletterFactory.getSuffix(newsletterType);
        String destPath = newsletterDirProperty.getValueSafe();

        final String newFileName = PFileName.getNextFileNameWithDate(destPath, fileName, suffix);
        newsletterNameProperty.setValue(newFileName);
    }

    @Override
    boolean check() {

        if (tglBill.isSelected()) {
            final String destP = pCboNewsletterPath.getSelValue();
            final String destF = pCboNewsletterName.getSelValue();

            final String sourceFile;
            if ((sourceFile = ClubFactory.getAbsolutFilePath(clubConfig, getStage(), pCboTemplate.getSelValue())).isEmpty()) {
                return false;
            }

            NewsletterFactory.NEWSLETTER_TYPE newsletterType;
            if ((newsletterType = NewsletterFactory.getType(sourceFile)) == null) {
                return false;
            }

            if (ClubFactory.getDestinationPath(getStage(), destP, destF).isEmpty()) {
                return false;
            }

            Newsletter.createDocument(clubConfig, newsletterType,
                    null, feeDataList,
                    sourceFile, destP, destF);
        }

        switch (type) {
            case BILL:
                feeDataList.stream().forEach(fee -> fee.setBill(pDatePickerFeeDateBuchungsdatum.getDateLDate()));
                break;
            case SQ:
            default:
                feeDataList.stream().forEach(fee -> fee.setSQ(pDatePickerFeeDateBuchungsdatum.getDateLDate()));
        }

        clubConfig.guiFee.updateFilteredList();
        return true;
    }

}
