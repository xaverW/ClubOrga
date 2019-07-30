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
import de.p2tools.clubOrga.controller.newsletter.Newsletter;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.gui.dialog.listDialog.BillForFeeDialogController;
import de.p2tools.clubOrga.gui.dialog.listDialog.FeePayDialogController;
import de.p2tools.p2Lib.guiTools.PButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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

        MenuItem miFeeBill = new MenuItem("Rechnung erstellen");
        miFeeBill.setOnAction(a -> createBillForFee(BillForFeeDialogController.TYPE.BILL));

        MenuItem miPayFee = new MenuItem("Beitrag bezahlen");
        miPayFee.setOnAction(a -> payFee());

        MenuItem miSQ = new MenuItem("Spendenquittung erstellen");
        miSQ.setOnAction(a -> createBillForFee(BillForFeeDialogController.TYPE.SQ));

        MenuItem miNewsletter = new MenuItem("Serienbrief erstellen");
        miNewsletter.setOnAction(event -> feeNewsletter());

        mb.getItems().addAll(miFeeBill, miPayFee, miSQ, miNewsletter);


        // Buttons
        Button btnDel = PButton.getButton(new ProgIcons().ICON_BUTTON_REMOVE, "Beitrag löschen");
        btnDel.setOnAction(a -> {
            List<FeeData> feeData = guiFee.getSelList();
            if (!feeData.isEmpty()) {
                clubConfig.feeDataList.feeDataListRemoveAll(feeData);
            }
        });

        Button btnChange = PButton.getButton(new ProgIcons().ICON_BUTTON_MEMBER_CHANGE, "Beitrag ändern");
        btnChange.setOnAction(a -> guiFee.changeFee());

        Button btnPay = PButton.getButton(new ProgIcons().ICON_EURO, "Beitrag bezahlen");
        btnPay.setOnAction(a -> payFee());
        getChildren().addAll(mb, btnDel, btnChange, btnPay);
    }


    private void createBillForFee(BillForFeeDialogController.TYPE type) {
        final ObservableList<FeeData> list = FXCollections.observableArrayList();
        list.addAll(clubConfig.guiFee.getSelList());
        if (list.isEmpty()) {
            return;
        }
        new BillForFeeDialogController(clubConfig, list, type);
    }

    private void payFee() {
        final ObservableList<FeeData> list = FXCollections.observableArrayList();
        list.addAll(clubConfig.guiFee.getSelList());
        if (list.isEmpty()) {
            return;
        }
        new FeePayDialogController(clubConfig, list);
    }

    private void feeNewsletter() {
        List<FeeData> feeData = clubConfig.guiFee.getSelList();
        if (!feeData.isEmpty()) {
            Newsletter.feeNewsletter(clubConfig, feeData);
        }
    }

}
