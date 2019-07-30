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

package de.p2tools.clubOrga.gui.guiClub;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.clubData.ClubFieldNames;
import de.p2tools.clubOrga.gui.tools.GuiFactory;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PTextField;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class GuiClub extends BorderPane {

    private final TextField txtName = new TextField();
    private final TextField txtOrt = new TextField();
    private final TextField txtPlz = new TextField();
    private final TextField txtStrasse = new TextField();
    private final TextField txtTelefon = new TextField();
    private final TextField txtemail = new TextField();
    private final TextField txtWebsite = new TextField();
    private final TextField txtKontoNr = new TextField();
    private final TextField txtBic = new TextField();
    private final TextField txtIban = new TextField();
    private final TextField txtBank = new TextField();
    private final TextField txtGlaeubigerId = new TextField();
    private final PTextField txtErstelldatum = new PTextField();

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public GuiClub(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;

        setStyle("-fx-background-color: -fx-background;");
        initCont();
    }

    public void isShown() {
    }


    private void initCont() {
        GuiFactory.setPaneTitle(this, "Verein");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(10));
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        txtName.textProperty().bindBidirectional(clubConfig.clubData.nameProperty());
        txtOrt.textProperty().bindBidirectional(clubConfig.clubData.ortProperty());
        txtPlz.textProperty().bindBidirectional(clubConfig.clubData.plzProperty());
        txtStrasse.textProperty().bindBidirectional(clubConfig.clubData.strasseProperty());
        txtTelefon.textProperty().bindBidirectional(clubConfig.clubData.telefonProperty());
        txtemail.textProperty().bindBidirectional(clubConfig.clubData.emailProperty());
        txtWebsite.textProperty().bindBidirectional(clubConfig.clubData.websiteProperty());
        txtKontoNr.textProperty().bindBidirectional(clubConfig.clubData.kontoNrProperty());
        txtBic.textProperty().bindBidirectional(clubConfig.clubData.bicProperty());
        txtIban.textProperty().bindBidirectional(clubConfig.clubData.ibanProperty());
        txtBank.textProperty().bindBidirectional(clubConfig.clubData.bankProperty());
        txtGlaeubigerId.textProperty().bindBidirectional(clubConfig.clubData.glaeubigerIdProperty());
        txtErstelldatum.setText(clubConfig.clubData.getErstellDatum().toString());
        txtErstelldatum.setStateLabel(true);


        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(15);
        gridPane.setHgap(15);

        int row = 0;
        gridPane.add(new Label("Vereinsname:"), 0, ++row);
        gridPane.add(txtName, 1, row);
        gridPane.add(new Label(" "), 0, ++row);

        gridPane.add(new Label("Ort:"), 0, ++row);
        gridPane.add(txtOrt, 1, row);
        gridPane.add(new Label("PLZ:"), 0, ++row);
        gridPane.add(txtPlz, 1, row);
        gridPane.add(new Label("Straße:"), 0, ++row);
        gridPane.add(txtStrasse, 1, row);
        gridPane.add(new Label("Telefon:"), 0, ++row);
        gridPane.add(txtTelefon, 1, row);
        gridPane.add(new Label("Email:"), 0, ++row);
        gridPane.add(txtemail, 1, row);
        gridPane.add(new Label("Webseite:"), 0, ++row);
        gridPane.add(txtWebsite, 1, row);

        gridPane.add(new Label("Kontonr:"), 0, ++row);
        gridPane.add(txtKontoNr, 1, row);
        gridPane.add(new Label("Bic:"), 0, ++row);
        gridPane.add(txtBic, 1, row);
        gridPane.add(new Label("IBAN:"), 0, ++row);
        gridPane.add(txtIban, 1, row);
        gridPane.add(new Label("Bank:"), 0, ++row);
        gridPane.add(txtBank, 1, row);

        gridPane.add(new Label("Gläubiger ID:"), 0, ++row);
        gridPane.add(txtGlaeubigerId, 1, row);

        gridPane.add(new Label(ClubFieldNames.ERSTELLDATUM), 0, ++row);
        gridPane.add(txtErstelldatum, 1, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());


        scrollPane.setContent(gridPane);
        this.setCenter(scrollPane);
    }


}
