/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.clubOrga.controller.ClubFactory;
import de.p2tools.clubOrga.controller.newsletter.Newsletter;
import de.p2tools.clubOrga.controller.newsletter.NewsletterFactory;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.dialog.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.PComboBoxString;
import de.p2tools.p2Lib.guiTools.PDatePicker;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.file.PFileName;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * fehlende Beiträge anlegen
 */
public class AddMissingFeeDialogController extends abListDialogController {

    private final PToggleSwitch tglBill = new PToggleSwitch("Rechnung erstellen", true);

    private final Label lblFeeDate = new Label("Rechnungsdatum");
    private final PDatePicker pDatePickerDate = new PDatePicker();
    final PLocalDate pDateData = new PLocalDate();

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
    private String destFile = "";

    private final ClubConfig clubConfig;
    private ObservableList<FeeData> feeDataList;


    public AddMissingFeeDialogController(ClubConfig clubConfig, ObservableList<FeeData> feeDataList) {
        super(clubConfig,
                clubConfig.FEE_DIALOG_SIZE,
                clubConfig.ADD_MISSING_FEE_DIALOG_DIVIDER,
                feeDataList, "Beitrag anlegen");

        this.clubConfig = clubConfig;
        this.feeDataList = feeDataList;

        newsletterTemplateProperty = clubConfig.NEWSLETTER_TEMPLATE_BILL;
        newsletterDirProperty = clubConfig.NEWSLETTER_DIR_BILL;
        newsletterNameProperty = clubConfig.NEWSLETTER_NAME_BILL;
        addNewsletterProperty = clubConfig.FEE_DIALOG_ADD_BILL;

        helpHeader = "Beitrag anlegen";
        helpText = "In diesem Dialog werden alle Mitglieder aufgeführt, für die noch nicht alle Beiträge " +
                "angelegt wurden. Aus der Liste können auch Mitglieder gelöscht werden, für die keine Beitrag " +
                "angelegt werden soll." + PConst.LINE_SEPARATORx2 +
                "Zusätzlich kann auch noch eine Rechnung für jeden Beitrag erstellt werden. Dafür kann dann " +
                "noch das Rechnungsdatum und die Vorlage und desweiteren der Speicherort ausgewählt werden.";
        proposeFileName = "Rechnung";

        init();
    }

    void init() {
        init(getvBoxDialog(), true);
    }


    @Override
    public void make() {
        super.make();

        pDatePickerDate.setDate(pDateData);
        pDatePickerDate.setMaxWidth(Double.MAX_VALUE);

        tglBill.selectedProperty().bindBidirectional(addNewsletterProperty);

        pCboTemplate.setMaxWidth(Double.MAX_VALUE);
        pCboNewsletterName.setMaxWidth(Double.MAX_VALUE);
        pCboNewsletterPath.setMaxWidth(Double.MAX_VALUE);

        gridPane.add(tglBill, 0, ++row, 3, 1);
        gridPane.add(new Label(" "), 0, ++row);

        gridPane.add(lblFeeDate, 0, ++row);
        gridPane.add(pDatePickerDate, 1, row, 2, 1);

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

        feeDataList.addListener((ListChangeListener<FeeData>) c -> {
            long no = clubConfig.feeDataList.getNextNr();
            for (FeeData feeData : feeDataList) {
                feeData.setNr(no++);
            }
        });
        initListener();
    }

    private void initListener() {
        pCboTemplate.init(clubConfig.CBO_LIST_NEWSLETTER_TEMPLATE, newsletterTemplateProperty);
        pCboNewsletterPath.init(clubConfig.CBO_LIST_NEWSLETTER_DIR, newsletterDirProperty);
        pCboNewsletterName.init(clubConfig.CBO_LIST_NEWSLETTER_FILE, newsletterNameProperty);

        lblFeeDate.disableProperty().bind(tglBill.selectedProperty().not());
        pDatePickerDate.disableProperty().bind(tglBill.selectedProperty().not());
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
        btnTemplate.setTooltip(new Tooltip("eine Vorlage auswählen"));
        btnTemplate.setOnAction(event -> PDirFileChooser.FileChooser(clubConfig.getStage(), pCboTemplate,
                ProgInfos.getClubDirectory(clubConfig.getClubPath()).toString(),
                ProgInfos.getClubDirectory(clubConfig.getClubPath()).toString()));

        btnNewsletterPath.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnNewsletterPath.setTooltip(new Tooltip("einen Speicherordner auswählen"));
        btnNewsletterPath.setOnAction(event -> PDirFileChooser.DirChooser(clubConfig.getStage(), pCboNewsletterPath,
                ProgInfos.getClubDirectory(clubConfig.getClubPath()).toString()));

        btnProposeFileName.setGraphic(new ProgIcons().ICON_BUTTON_GUI_GEN_NAME);
        btnProposeFileName.setTooltip(new Tooltip("einen Dateinamen vorschlagen"));
        btnProposeFileName.setOnAction(event -> {
            String sourceFile = ClubFactory.getAbsolutFilePath(clubConfig, getStage(), newsletterTemplateProperty.getValueSafe());
            if (sourceFile.isEmpty()) {
                return;
            }

            String fileName = newsletterNameProperty.getValueSafe();
            NewsletterFactory.NEWSLETTER_TYPE newsletterType;
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
        });

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
                    null, feeDataList, null,
                    sourceFile, destP, destF);

            feeDataList.stream().forEach(fee -> fee.setBill(pDateData));
        }

        return true;
    }
}
