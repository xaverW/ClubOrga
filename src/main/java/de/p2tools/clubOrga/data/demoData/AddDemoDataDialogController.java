/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

package de.p2tools.clubOrga.data.demoData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.clubData.ClubFieldNames;
import de.p2tools.clubOrga.data.demoData.data.DemoClubDataFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;


public class AddDemoDataDialogController extends PDialogExtra {

    private final ClubConfig clubConfig;

    private final TabPane tabPane = new TabPane();
    private final Tab tabClub = new Tab("Verein");
    private final Tab tabMember = new Tab("Mitglieder");
    private final Tab tabFinances = new Tab("Finanzen");

    private final Slider slMember = new Slider();
    private final Slider slFinance = new Slider();

    private final PToggleSwitch tglRemove = new PToggleSwitch("alte Vereinsdaten löschen");

    private final TextField txtClubName = new TextField();
    private final TextField txtClubOrt = new TextField();
    private final TextField txtClubPlz = new TextField();
    private final TextField txtClubStrasse = new TextField();
    private final TextField txtClubTelefon = new TextField();
    private final TextField txtClubEmail = new TextField();
    private final TextField txtClubWebsite = new TextField();
    private final TextField txtClubKontoNr = new TextField();
    private final TextField txtClubBic = new TextField();
    private final TextField txtClubIban = new TextField();
    private final TextField txtClubBank = new TextField();
    private final TextField txtClubGlaeubigerId = new TextField();

    private final RadioButton rbNone = new RadioButton("keine Beiträge");
    private final RadioButton rbSome = new RadioButton("einen Teil der Beiträge");
    private final RadioButton rbAll = new RadioButton("alle Beiträge");

    private final RadioButton rbFinanceNone = new RadioButton("keine bezahlen");
    private final RadioButton rbFinanceSome = new RadioButton("einen Teil bezahlen");
    private final RadioButton rbFinanceAll = new RadioButton("alle bezahlen");

    BooleanProperty propertyRemoveOldData;
    BooleanProperty propertyClub;
    BooleanProperty propertyMember;
    BooleanProperty propertyFinances;

    IntegerProperty propertyAddMember;
    IntegerProperty propertyAddFinance;
    private boolean ok = false;


    public AddDemoDataDialogController(ClubConfig clubConfig) {
        super(clubConfig.getStage(), clubConfig.ADD_DEMO_DATA_DIALOG_SIZE, "Demodaten anlegen", true, true);

        this.clubConfig = clubConfig;

        this.propertyRemoveOldData = clubConfig.DEMO_DATA_REMOVE_OLD_DATA;
        this.propertyClub = clubConfig.DEMO_DATA_CLUB;
        this.propertyMember = clubConfig.DEMO_DATA_MEMBER;
        this.propertyFinances = clubConfig.DEMO_DATA_FINANCES;
        this.propertyAddMember = clubConfig.DEMO_DATA_ADD_MEMBER;
        this.propertyAddFinance = clubConfig.DEMO_DATA_ADD_FINANCE;

        init(getvBoxDialog(), true);
    }

    @Override
    protected void make() {
        final Button btnHelp = PButton.helpButton(getStage(), "Demodaten einfügen",
                "In diesem Dialog kann man Demodaten: Mitglieder, Beiträge und Finanzen anlegen. " +
                        "Bei den Beiträgen lässt sich noch auswählen, ob und wievile schon bezahlt sein sollen. " +
                        "Mit \"alte Vereinsdaten löschen\" werden die vorhandenen Daten gelöscht. Sie sind " +
                        "damit verloren!");

        final Button btnOk = new Button("Ok");
        btnOk.setMaxWidth(Double.MAX_VALUE);
        btnOk.disableProperty().bind(propertyClub.not().
                and(propertyMember.not().or(propertyAddMember.greaterThan(0).not()).
                        and(propertyFinances.not().or(propertyAddFinance.greaterThan(0).not())
                        )
                )
        );
        btnOk.setOnAction(a -> {
            if (check()) {
                ok = true;
                close();
            }
        });

        final Button btnCancel = new Button("Abbrechen");
        btnCancel.setMaxWidth(Double.MAX_VALUE);
        btnCancel.setOnAction(a -> close());

        tglRemove.setMinWidth(Region.USE_PREF_SIZE);
        tglRemove.selectedProperty().bindBidirectional(propertyRemoveOldData);

        TilePane tilePane = new TilePane(10, 10);
        tilePane.setPrefColumns(2);
        tilePane.setAlignment(Pos.CENTER_RIGHT);
        tilePane.getChildren().addAll(btnOk, btnCancel);
        HBox.setHgrow(tilePane, Priority.ALWAYS);

        HBox hBoxOk = getHboxOk();
        hBoxOk.getChildren().addAll(btnHelp, tglRemove, tilePane);

        initTabs();
    }

    private void initTabs() {
        initSlider(slMember, propertyAddMember, 500);
        initSlider(slFinance, propertyAddFinance, 500);

        initTabClub();
        initTabMember();
        initTabFinaces();

        tabClub.setClosable(false);
        tabMember.setClosable(false);
        tabFinances.setClosable(false);
        tabPane.getTabs().addAll(tabClub, tabMember, tabFinances);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        getVboxCont().getChildren().add(tabPane);
    }

    private void initTabClub() {
        VBox vBox = new VBox(30);
        vBox.setPadding(new Insets(20));
        tabClub.setContent(vBox);


        Label lblMember = new Label();
        lblMember.textProperty().bind(propertyAddMember.asString());
        Label lblFinance = new Label();
        lblFinance.textProperty().bind(propertyAddFinance.asString());

        setClubProperty();

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;
        gridPane.add(new Label(ClubFieldNames.NAME_), 0, ++row);
        gridPane.add(txtClubName, 1, row);
        gridPane.add(new Label(ClubFieldNames.ORT_), 0, ++row);
        gridPane.add(txtClubOrt, 1, row);
        gridPane.add(new Label(ClubFieldNames.PLZ_), 0, ++row);
        gridPane.add(txtClubPlz, 1, row);
        gridPane.add(new Label(ClubFieldNames.STRASSE_), 0, ++row);
        gridPane.add(txtClubStrasse, 1, row);
        gridPane.add(new Label(ClubFieldNames.TELEFON_), 0, ++row);
        gridPane.add(txtClubTelefon, 1, row);
        gridPane.add(new Label(ClubFieldNames.EMAIL_), 0, ++row);
        gridPane.add(txtClubEmail, 1, row);
        gridPane.add(new Label(ClubFieldNames.WEBSITE_), 0, ++row);
        gridPane.add(txtClubWebsite, 1, row);

        gridPane.add(new Label(ClubFieldNames.KONTONR_), 0, ++row);
        gridPane.add(txtClubKontoNr, 1, row);
        gridPane.add(new Label(ClubFieldNames.BIC_), 0, ++row);
        gridPane.add(txtClubBic, 1, row);
        gridPane.add(new Label(ClubFieldNames.IBAN_), 0, ++row);
        gridPane.add(txtClubIban, 1, row);
        gridPane.add(new Label(ClubFieldNames.BANK_), 0, ++row);
        gridPane.add(txtClubBank, 1, row);

        gridPane.add(new Label(ClubFieldNames.GLAEUBIGER_ID_), 0, ++row);
        gridPane.add(txtClubGlaeubigerId, 1, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        PToggleSwitch tglClub = new PToggleSwitch("Vereinsdaten anlegen");
        tglClub.selectedProperty().bindBidirectional(propertyClub);
        gridPane.disableProperty().bind(propertyClub.not());

        vBox.getChildren().add(tglClub);
        vBox.getChildren().add(gridPane);
    }

    private void initTabMember() {
        VBox vBox = new VBox(30);
        vBox.setPadding(new Insets(20));
        tabMember.setContent(vBox);

        Label lblMember = new Label();
        lblMember.textProperty().bind(propertyAddMember.asString());

        VBox vBoxFee = new VBox(5);
        ToggleGroup tg = new ToggleGroup();
        rbNone.setToggleGroup(tg);
        rbSome.setToggleGroup(tg);
        rbAll.setToggleGroup(tg);
        switch (DemoConst.getAddAmountFee(clubConfig.DEMO_DATA_ADD_FEE.get())) {
            case FEE_ADD_NONE:
                rbNone.setSelected(true);
                break;
            case FEE_ADD_SOME:
                rbSome.setSelected(true);
                break;
            case FEE_ADD_ALL:
            default:
                rbAll.setSelected(true);
                break;
        }
        rbNone.setOnAction(a -> setFeeGeneration());
        rbSome.setOnAction(a -> setFeeGeneration());
        rbAll.setOnAction(a -> setFeeGeneration());
        vBoxFee.getChildren().addAll(rbNone, rbSome, rbAll);

        VBox vBoxFinance = new VBox(5);
        ToggleGroup tgF = new ToggleGroup();
        rbFinanceNone.setToggleGroup(tgF);
        rbFinanceSome.setToggleGroup(tgF);
        rbFinanceAll.setToggleGroup(tgF);
        switch (DemoConst.getAddAmountFee(clubConfig.DEMO_DATA_ADD_FEE_PAY.get())) {
            case FEE_ADD_NONE:
                rbFinanceNone.setSelected(true);
                break;
            case FEE_ADD_SOME:
                rbFinanceSome.setSelected(true);
                break;
            case FEE_ADD_ALL:
            default:
                rbFinanceAll.setSelected(true);
                break;
        }
        rbFinanceNone.setOnAction(a -> setFeePay());
        rbFinanceSome.setOnAction(a -> setFeePay());
        rbFinanceAll.setOnAction(a -> setFeePay());
        vBoxFinance.getChildren().addAll(rbFinanceNone, rbFinanceSome, rbFinanceAll);


        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(new Label("Anzahl Mitglieder anlegen:"), lblMember);
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;
        gridPane.add(hBox, 0, ++row, 2, 1);
        gridPane.add(slMember, 0, ++row, 2, 1);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(new Label("Beiträge für die angelegten Mitglieder anlegen:"), 0, ++row);
        gridPane.add(vBoxFee, 1, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(new Label("angelegte Beiträge bezahlen:"), 0, ++row);
        gridPane.add(vBoxFinance, 1, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());


        PToggleSwitch tglMember = new PToggleSwitch("Mitglieder anlegen");
        tglMember.selectedProperty().bindBidirectional(propertyMember);
        gridPane.disableProperty().bind(propertyMember.not());

        vBox.getChildren().add(tglMember);
        vBox.getChildren().add(gridPane);
    }

    private void initTabFinaces() {
        VBox vBox = new VBox(30);
        vBox.setPadding(new Insets(20));
        tabFinances.setContent(vBox);

        Label lblFinance = new Label();
        lblFinance.textProperty().bind(propertyAddFinance.asString());

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(new Label("Anzahl Finanzen anlegen:"), lblFinance);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;
        gridPane.add(hBox, 0, ++row, 2, 1);
        gridPane.add(slFinance, 0, ++row, 2, 1);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());


        PToggleSwitch tglFinances = new PToggleSwitch("Finanzen anlegen");
        tglFinances.selectedProperty().bindBidirectional(propertyFinances);
        gridPane.disableProperty().bind(propertyFinances.not());

        vBox.getChildren().add(tglFinances);
        vBox.getChildren().add(gridPane);
    }

    private void setClubProperty() {
        // erst mal neue erstellen
        DemoClubDataFactory.randomizeDemoClubData(clubConfig);

        txtClubName.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_NAME);
        txtClubOrt.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_ORT);
        txtClubPlz.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_PLZ);
        txtClubStrasse.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_STRASSE);
        txtClubTelefon.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_TELEFON);
        txtClubEmail.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_EMAIL);
        txtClubWebsite.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_WEBSITE);

        txtClubKontoNr.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_KONTO_NR);
        txtClubBic.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_BIC);
        txtClubIban.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_IBAN);
        txtClubBank.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_BANK);
        txtClubGlaeubigerId.textProperty().bindBidirectional(clubConfig.DEMO_DATA_ADD_CLUB_GLAEUBIGER_ID);
    }

    private void initSlider(Slider slider, IntegerProperty ip, int max) {
        slider.setMin(0);
        slider.setMax(max);
        slider.setMaxWidth(Double.MAX_VALUE);

        slider.setShowTickLabels(true);
        slider.setMinorTickCount(9); // TickUnit / TickCount+1
        slider.setMajorTickUnit(max / 5); //
        slider.setBlockIncrement(10); // MIN_DIST
        slider.setSnapToTicks(true);
        slider.setShowTickLabels(true);

        slider.valueProperty().bindBidirectional(ip);
    }

    private void setFeeGeneration() {
        if (rbNone.isSelected()) {
            clubConfig.DEMO_DATA_ADD_FEE.set(DemoConst.ADD_AMOUNT_FEE.FEE_ADD_NONE.getNo());
        } else if (rbSome.isSelected()) {
            clubConfig.DEMO_DATA_ADD_FEE.set(DemoConst.ADD_AMOUNT_FEE.FEE_ADD_SOME.getNo());
        } else {
            clubConfig.DEMO_DATA_ADD_FEE.set(DemoConst.ADD_AMOUNT_FEE.FEE_ADD_ALL.getNo());
        }
    }

    private void setFeePay() {
        if (rbFinanceNone.isSelected()) {
            clubConfig.DEMO_DATA_ADD_FEE_PAY.set(DemoConst.ADD_AMOUNT_FEE.FEE_ADD_NONE.getNo());
        } else if (rbFinanceSome.isSelected()) {
            clubConfig.DEMO_DATA_ADD_FEE_PAY.set(DemoConst.ADD_AMOUNT_FEE.FEE_ADD_SOME.getNo());
        } else {
            clubConfig.DEMO_DATA_ADD_FEE_PAY.set(DemoConst.ADD_AMOUNT_FEE.FEE_ADD_ALL.getNo());
        }
    }

    public boolean isOk() {
        return ok;
    }

    public void close() {
        super.close();
    }

    private boolean check() {
        boolean ret = false;

        final int member = clubConfig.memberDataList.size();
        final int fee = clubConfig.feeDataList.size();
        final int finances = clubConfig.financeDataList.size();

        if (member == 0 && fee == 0 && finances == 0) {
            ret = true;

        } else if (propertyRemoveOldData.get()) {
            PAlert.BUTTON button = PAlert.showAlert_yes_no(getStage(), "Löschen",
                    "Vereinsdaten löschen",
                    "Sollen wirklich alle bestehenden Vereinsdaten gelöscht werden?" +
                            "\n\n" +
                            "Bestehende Vereinsdaten:\n" +
                            member + " Mitglieder\n" +
                            fee + " Beiträge\n" +
                            finances + " Finanzen");
            if (button == PAlert.BUTTON.YES) {
                ret = true;
            }
        } else {
            PAlert.BUTTON button = PAlert.showAlert_yes_no(getStage(), "Demodaten",
                    "Demodaten hinzufügen",
                    "Sollen wirklich zu den bestehenden Vereinsdaten weitere Demodaten hinzugefügt werden?" +
                            "\n\n" +
                            "Bestehende Vereinsdaten:\n" +
                            member + " Mitglieder\n" +
                            fee + " Beiträge\n" +
                            finances + " Finanzen");
            if (button == PAlert.BUTTON.YES) {
                ret = true;
            }
        }

        return ret;
    }
}
