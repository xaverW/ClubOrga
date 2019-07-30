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

package de.p2tools.clubOrga.gui.guiMember;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateWorker;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberFieldNames;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.memberData.stateData.StateData;
import de.p2tools.p2Lib.guiTools.*;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GuiMemberInfoPane extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final VBox vbLeft = new VBox();
    private final VBox vbRight = new VBox();

    private final PTextFieldLong txtNr = new PTextFieldLong();
    private final TextField txtNachname = new TextField();
    private final TextField txtVorname = new TextField();
    private final PComboBoxString cboAnrede = new PComboBoxString();
    private final TextArea txtText = new TextArea();

    private final TextField txtEmail = new TextField();
    private final TextField txtTelefon = new TextField();
    private final TextField txtStrasse = new TextField();
    private final PComboBoxString cboPlz = new PComboBoxString();
    private final PComboBoxString cboOrt = new PComboBoxString();

    private final PTextFieldMoney txtBeitrag = new PTextFieldMoney();
    private final PComboBoxObject<FeeRateData> cboBeitragssatz = new PComboBoxObject();
    private final PComboBoxObject<StateData> cboStatus = new PComboBoxObject<>();
    private final PComboBoxObject<PaymentTypeData> cboPaymentType = new PComboBoxObject<>();

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private MemberData memberData = null;
    private FeeRateWorker feeRateWorker;

    DoubleProperty doublePropertyInfo;

    public GuiMemberInfoPane(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
        progData = ProgData.getInstance();

        doublePropertyInfo = clubConfig.GUI_PANEL_MEMBER_DIVIDER_INFO;
        feeRateWorker = new FeeRateWorker(cboBeitragssatz, txtBeitrag, clubConfig);

        ScrollPane scrollPaneLeft = new ScrollPane();
        scrollPaneLeft.setFitToHeight(true);
        scrollPaneLeft.setFitToWidth(true);

        ScrollPane scrollPaneRight = new ScrollPane();
        scrollPaneRight.setFitToHeight(true);
        scrollPaneRight.setFitToWidth(true);

        getChildren().addAll(splitPane);
        splitPane.getItems().addAll(scrollPaneLeft, scrollPaneRight);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(doublePropertyInfo);

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);

        vbLeft.setPadding(new Insets(15, 15, 15, 15));
        vbLeft.setSpacing(20);
        vbLeft.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        vbLeft.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        scrollPaneLeft.setContent(vbLeft);

        vbRight.setPadding(new Insets(15, 15, 15, 15));
        vbRight.setSpacing(20);
        vbRight.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        vbRight.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        scrollPaneRight.setContent(vbRight);

        initInfoLeft();
        initInfoRight();
        setDisableAll();
    }

    private void initInfoLeft() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        cboAnrede.init(clubConfig.CBO_LIST_ANREDE, "", null);
        cboAnrede.setMaxWidth(Double.MAX_VALUE);
        cboPlz.init(clubConfig.CBO_LIST_PLZ, "", null);
        cboPlz.setRegEx(MemberFieldNames.PLZ_REGEX);
        cboPlz.setMaxWidth(Double.MAX_VALUE);
        cboOrt.init(clubConfig.CBO_LIST_ORT, "", null);
        cboOrt.setMaxWidth(Double.MAX_VALUE);


        int row = 0;
        gridPane.add(new Label(MemberFieldNames.NR_), 0, row);
        gridPane.add(txtNr, 1, row);

        gridPane.add(new Label(MemberFieldNames.NACHNAME_), 0, ++row);
        gridPane.add(txtNachname, 1, row);

        gridPane.add(new Label(MemberFieldNames.VORNAME_), 0, ++row);
        gridPane.add(txtVorname, 1, row);

        gridPane.add(new Label(MemberFieldNames.ANREDE_), 0, ++row);
        gridPane.add(cboAnrede, 1, row);

        gridPane.add(new Label(MemberFieldNames.EMAIL_), 0, ++row);
        gridPane.add(txtEmail, 1, row);

        gridPane.add(new Label(MemberFieldNames.TELEFON_), 0, ++row);
        gridPane.add(txtTelefon, 1, row);

        gridPane.add(new Label(MemberFieldNames.STRASSE_), 0, ++row);
        gridPane.add(txtStrasse, 1, row);

        gridPane.add(new Label(MemberFieldNames.PLZ_), 0, ++row);
        gridPane.add(cboPlz, 1, row);

        gridPane.add(new Label(MemberFieldNames.ORT_), 0, ++row);
        gridPane.add(cboOrt, 1, row);

        vbLeft.getChildren().add(gridPane);
    }

    private void initInfoRight() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        cboStatus.init(clubConfig.stateDataList, null);
        cboStatus.setMaxWidth(Double.MAX_VALUE);

        cboPaymentType.init(clubConfig.paymentTypeDataList, null);
        cboPaymentType.setMaxWidth(Double.MAX_VALUE);

        int row = 0;
        gridPane.add(new Label(MemberFieldNames.BEITRAG_), 0, row);
        gridPane.add(txtBeitrag, 1, row);

        gridPane.add(new Label(MemberFieldNames.BEITRAGSSATZ_), 0, ++row);
        gridPane.add(cboBeitragssatz, 1, row);

        gridPane.add(new Label(MemberFieldNames.STATUS_), 0, ++row);
        gridPane.add(cboStatus, 1, row);

        gridPane.add(new Label(MemberFieldNames.ZAHLART_), 0, ++row);
        gridPane.add(cboPaymentType, 1, row);

        gridPane.add(new Label(MemberFieldNames.TEXT_), 0, ++row);
        gridPane.add(txtText, 1, row);

        vbRight.getChildren().add(gridPane);
    }

    public void setMember(MemberData memberData) {
        if (this.memberData != null &&
                memberData != null &&
                this.memberData.equals(memberData)) {
            return;
        }

        unbind();
        this.memberData = memberData;
        bind();
    }

    private void setDisableAll() {
        vbRight.setDisable(memberData == null);
        vbLeft.setDisable(memberData == null);
    }

    private void unbind() {
        setDisableAll();
        if (memberData == null) {
            return;
        }
        txtNr.unBind();
        txtNachname.textProperty().unbindBidirectional(memberData.nachnameProperty());
        txtVorname.textProperty().unbindBidirectional(memberData.vornameProperty());
        cboAnrede.unbindSelValueProperty();
        txtEmail.textProperty().unbindBidirectional(memberData.emailProperty());
        txtTelefon.textProperty().unbindBidirectional(memberData.telefonProperty());
        txtStrasse.textProperty().unbindBidirectional(memberData.strasseProperty());
        cboPlz.unbindSelValueProperty();
        cboOrt.unbindSelValueProperty();
        txtBeitrag.unBind();

        feeRateWorker.unbind();

        cboStatus.unbindSelValueProperty();
        cboPaymentType.unbindSelValueProperty();
        txtText.textProperty().unbindBidirectional(memberData.textProperty());
    }

    private void bind() {
        setDisableAll();
        if (memberData == null) {
            txtNr.setText("");
            txtNachname.setText("");
            txtVorname.setText("");
            cboAnrede.selectElement("");
            txtEmail.setText("");
            txtTelefon.setText("");
            txtStrasse.setText("");
            cboPlz.selectElement("");
            cboOrt.selectElement("");
            txtBeitrag.setText("");
            cboStatus.unbindSelValueProperty();
            cboPaymentType.unbindSelValueProperty();
            txtText.setText("");
            feeRateWorker.clearCbo();
            return;
        }

        txtNr.bindBidirectional(memberData.nrProperty());
        txtNachname.textProperty().bindBidirectional(memberData.nachnameProperty());
        txtVorname.textProperty().bindBidirectional(memberData.vornameProperty());
        cboAnrede.bindSelValueProperty(memberData.anredeProperty());

        txtEmail.textProperty().bindBidirectional(memberData.emailProperty());
        txtTelefon.textProperty().bindBidirectional(memberData.telefonProperty());
        txtStrasse.textProperty().bindBidirectional(memberData.strasseProperty());
        cboPlz.bindSelValueProperty(memberData.plzProperty());
        cboOrt.bindSelValueProperty(memberData.ortProperty());

        feeRateWorker.bind(memberData);

        cboStatus.bindSelValueProperty(memberData.stateDataProperty());
        cboPaymentType.bindSelValueProperty(memberData.paymentTypeDataProperty());
        txtText.textProperty().bindBidirectional(memberData.textProperty());
    }
}
