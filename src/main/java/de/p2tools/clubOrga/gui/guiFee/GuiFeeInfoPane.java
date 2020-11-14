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

package de.p2tools.clubOrga.gui.guiFee;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.feeData.FeeFieldNames;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PDatePropertyPicker;
import de.p2tools.p2Lib.guiTools.PTextField;
import de.p2tools.p2Lib.guiTools.PTextFieldLong;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GuiFeeInfoPane extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final VBox vbLeft = new VBox();
    private final VBox vbRight = new VBox();

    private final PTextFieldLong txtNr = new PTextFieldLong();
    private final PTextFieldLong txtMitgliedNr = new PTextFieldLong(true);
    private final PTextField txtMitgliedName = new PTextField(true);

    private final PDatePropertyPicker pDatePicker = new PDatePropertyPicker();
    private final TextArea txtText = new TextArea();


    private final ProgData progData;
    private final ClubConfig clubConfig;
    private FeeData feeData = null;

    DoubleProperty doublePropertyInfo;

    public GuiFeeInfoPane(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
        progData = ProgData.getInstance();

        doublePropertyInfo = clubConfig.GUI_PANEL_FEE_DIVIDER_INFO;

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
        pDatePicker.setMaxWidth(Double.MAX_VALUE);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        gridPane.add(new Label(FeeFieldNames.NR_), 0, row);
        gridPane.add(txtNr, 1, row);

        gridPane.add(new Label(FeeFieldNames.MEMBER_NO), 0, ++row);
        gridPane.add(txtMitgliedNr, 1, row);
        gridPane.add(new Label(FeeFieldNames.MEMBER_NAME_), 0, ++row);
        gridPane.add(txtMitgliedName, 1, row);
        gridPane.add(new Label(FeeFieldNames.BEZAHLT_), 0, ++row);
        gridPane.add(pDatePicker, 1, row);

        vbLeft.getChildren().add(gridPane);
    }

    private void initInfoRight() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        gridPane.add(new Label(FeeFieldNames.TEXT_), 0, ++row);
        gridPane.add(txtText, 1, row);

        vbRight.getChildren().add(gridPane);
    }

    public void setFee(FeeData feeData) {
        if (this.feeData != null &&
                feeData != null &&
                this.feeData.equals(feeData)) {
            return;
        }

        unbind();
        this.feeData = feeData;
        bind();
    }

    private void setDisableAll() {
        vbRight.setDisable(feeData == null);
        vbLeft.setDisable(feeData == null);
    }

    private void unbind() {
        setDisableAll();
        if (feeData == null) {
            pDatePicker.clearDate();
            return;
        }

        txtNr.unBind();
        txtMitgliedNr.unBind();
        txtMitgliedName.textProperty().unbindBidirectional(feeData.memberNameProperty());
        txtText.textProperty().unbindBidirectional(feeData.textProperty());
        pDatePicker.clearDate();
    }

    private void bind() {
        setDisableAll();
        if (feeData == null) {
            txtNr.setText("");
            txtMitgliedNr.setText("");
            txtMitgliedName.setText("");
            txtText.setText("");
            return;
        }

        txtNr.bindBidirectional(feeData.noProperty());
        txtMitgliedNr.bindBidirectional(feeData.memberNoProperty());
        txtMitgliedName.textProperty().bindBidirectional(feeData.memberNameProperty());
        txtText.textProperty().bindBidirectional(feeData.textProperty());
        pDatePicker.setpDateProperty(feeData.bezahltProperty());
    }
}
