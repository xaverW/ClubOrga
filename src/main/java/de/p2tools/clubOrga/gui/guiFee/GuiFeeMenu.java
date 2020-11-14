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

package de.p2tools.clubOrga.gui.guiFee;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.controller.export.csv.ExportCsvDialogController;
import de.p2tools.clubOrga.controller.newsletter.Newsletter;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.gui.dialog.listDialog.BillForFeeDialogController;
import de.p2tools.clubOrga.gui.dialog.listDialog.PayFeeDialogController;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class GuiFeeMenu extends VBox {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private final GuiFee guiFee;

    public GuiFeeMenu(ClubConfig clubConfig, GuiFee guiFee) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.guiFee = guiFee;

        initCont();
    }

    private void initCont() {
        this.setSpacing(10);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_CENTER);

        final MenuButton mb = new MenuButton("");
        mb.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU);
        mb.getStyleClass().add("btnFunction");

        MenuItem miChangeFee = new MenuItem("aktuellen Beitrag ändern");
        miChangeFee.setOnAction(a -> guiFee.changeFee());
        MenuItem miDelFee = new MenuItem("ausgewählte Beiträge löschen");
        miDelFee.setOnAction(a -> delFee());

        MenuItem miPayFee = new MenuItem("ausgewählte Beiträge bezahlen");
        miPayFee.setOnAction(a -> payFee());
        MenuItem miFeeBill = new MenuItem("Rechnung ausgewählter Beiträge erstellen");
        miFeeBill.setOnAction(a -> createBillForFee(BillForFeeDialogController.TYPE.BILL));
        MenuItem miSQ = new MenuItem("Spendenquittung ausgewählter Beiträge erstellen");
        miSQ.setOnAction(a -> createBillForFee(BillForFeeDialogController.TYPE.SQ));

        MenuItem miNewsletterAkt = new MenuItem("für markierte Beiträge Serienbrief erstellen");
        miNewsletterAkt.setOnAction(event -> feeNewsletterAkt());
        MenuItem miNewsletter = new MenuItem("für alle angezeigten Beiträge Serienbrief erstellen");
        miNewsletter.setOnAction(event -> feeNewsletter());

        MenuItem miExportSel = new MenuItem("markierte Beiträge exportieren");
        miExportSel.setOnAction(a -> exportFee(clubConfig.guiFee.getSelList()));
        MenuItem miExportShown = new MenuItem("angezeigte Beiträge exportieren");
        miExportShown.setOnAction(a -> exportFee(clubConfig.feeDataList.getFilteredList()));
        MenuItem miExportAll = new MenuItem("alle Beiträge exportieren");
        miExportAll.setOnAction(a -> exportFee(clubConfig.feeDataList));

        Menu mNewsletter = new Menu("Serienbrief");
        mNewsletter.getItems().addAll(miNewsletterAkt, miNewsletter);
        Menu mExport = new Menu("Export");
        mExport.getItems().addAll(miExportSel, miExportShown, miExportAll);

        mb.getItems().addAll(miChangeFee, miDelFee,
                new SeparatorMenuItem(), miPayFee, miFeeBill, miSQ,
                new SeparatorMenuItem(), mNewsletter, mExport);


        // Buttons
        Button btnDel = PButton.getButton(new ProgIcons().ICON_BUTTON_REMOVE,
                "alle markierten Beiträge löschen");
        btnDel.setOnAction(a -> delFee());

        Button btnChange = PButton.getButton(new ProgIcons().ICON_BUTTON_MEMBER_CHANGE,
                "den markierten Beitrag ändern");
        btnChange.setOnAction(a -> guiFee.changeFee());

        Button btnPay = PButton.getButton(new ProgIcons().ICON_EURO, "markierte Beiträge bezahlen");
        btnPay.setOnAction(a -> payFee());
        getChildren().addAll(mb, btnDel, btnChange, btnPay);
    }


    private void delFee() {
        List<FeeData> feeData = guiFee.getSelList();
        if (!feeData.isEmpty()) {
            clubConfig.feeDataList.feeDataListRemoveAll(feeData);
        }
    }

    private void createBillForFee(BillForFeeDialogController.TYPE type) {
        final ObservableList<FeeData> list = FXCollections.observableArrayList();
        list.addAll(clubConfig.guiFee.getSelList());
        if (list.isEmpty()) {
            return;
        }

        // checken ob bereits erledigte dabei sind
        boolean found;
        if (type == BillForFeeDialogController.TYPE.BILL) {
            found = list.stream().filter(feeData -> !feeData.getRechnung().isEmpty()).findAny().isPresent();
        } else {
            found = list.stream().filter(feeData -> !feeData.getSpendenQ().isEmpty()).findAny().isPresent();
        }
        if (found) {
            String s1 = type == BillForFeeDialogController.TYPE.BILL ? "Rechnung" : "Spendenquittung";
            String s2 = s1 + "en";
            PAlert.BUTTON button = PAlert.showAlert_yes_no(clubConfig.getStage(),
                    s1 + " erstellen",
                    s1 + " bereits erstellt",
                    "Es sind bereits erstellte " + s2 + " dabei. Sollen die nochmal erstellt werden?");
            if (button != PAlert.BUTTON.YES) {
                return;
            }
        }

        new BillForFeeDialogController(clubConfig, list, type);
    }

    private void exportFee(List<FeeData> feeDataList) {
        if (feeDataList != null && !feeDataList.isEmpty()) {
            new ExportCsvDialogController(clubConfig.getStage(), clubConfig,
                    null, feeDataList, null, null);
        }
    }

    private void payFee() {
        final ObservableList<FeeData> list = FXCollections.observableArrayList();
        list.addAll(clubConfig.guiFee.getSelList());
        if (list.isEmpty()) {
            // leere Liste
            return;
        }

        // checken ob bereits bezahlte dabei sind
        boolean found = list.stream().filter(feeData -> feeData.isFeePayed()).findAny().isPresent();
        if (found) {
            PAlert.BUTTON button = PAlert.showAlert_yes_no(clubConfig.getStage(),
                    "Beitrag bezahlen", "Beitrag bereits bezahlt",
                    "Es sind bereits bezahlte Beiträge dabei. Sollen die nochmal bezahlt werden?");
            if (button != PAlert.BUTTON.YES) {
                return;
            }
        }

        // dann jetzt den Bezahlendialog
        new PayFeeDialogController(clubConfig, list);
    }

    private void feeNewsletterAkt() {
        List<FeeData> feeDataList = guiFee.getSelList();
        if (!feeDataList.isEmpty()) {
            Newsletter.feeNewsletter(clubConfig, feeDataList);
        }
    }

    private void feeNewsletter() {
        List<FeeData> feeDataList = clubConfig.feeDataList.getFilteredList();
        if (!feeDataList.isEmpty()) {
            Newsletter.feeNewsletter(clubConfig, feeDataList);
        }
    }
}
