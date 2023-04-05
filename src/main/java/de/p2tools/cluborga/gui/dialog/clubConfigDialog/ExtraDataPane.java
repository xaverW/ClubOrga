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

package de.p2tools.cluborga.gui.dialog.clubConfigDialog;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.config.prog.ProgIcons;
import de.p2tools.cluborga.data.extraData.ExtraData;
import de.p2tools.cluborga.data.extraData.ExtraDataList;
import de.p2tools.cluborga.data.extraData.ExtraKind;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.PTextFieldInteger;
import de.p2tools.p2lib.tools.PException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ExtraDataPane extends AnchorPane {

    private final GridPane gridPane = new GridPane();
    private final ProgData progData;
    private final ClubConfig clubConfig;
    private final Stage stage;

    private ExtraDataList extraDataList;

    public ExtraDataPane(Stage stage, ClubConfig clubConfig) {
        progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.stage = stage;
    }

    public TitledPane getExtraPane(ExtraDataList extraDataList) {
        this.extraDataList = extraDataList;

        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        TitledPane tpConfig = new TitledPane("Namen", gridPane);

        initGrid();
        return tpConfig;
    }

    private void initGrid() {
        gridPane.getChildren().clear();

        gridPane.add(new Label("Eingeschaltet"), 0, 0);
        gridPane.add(new Label("Feldname"), 1, 0);
        gridPane.add(new Label("Startwert"), 2, 0);
        gridPane.add(new Label("Feldart"), 3, 0);

        int row = 0;
        for (int i = 0; i < extraDataList.size(); ++i) {
            ExtraData ex = extraDataList.get(i);

            // visible
            CheckBox chkOn = new CheckBox();
            chkOn.selectedProperty().bindBidirectional(ex.onProperty());
            gridPane.add(chkOn, 0, ++row);

            // fieldName
            TextField txt = new TextField();
            txt.textProperty().bindBidirectional(ex.nameProperty());
            gridPane.add(txt, 1, row);

            final HBox hBoxInit = new HBox();
            makeHBox(hBoxInit, ex);
            gridPane.add(hBoxInit, 2, row);

            // kind
            ComboBox<ExtraKind.EXTRA_KIND> cboKind = new ComboBox<>();
            cboKind.getItems().setAll(ExtraKind.EXTRA_KIND.values());
            cboKind.getSelectionModel().select(ExtraKind.getExtraKind(ex.getKindSave()));
            Bindings.bindBidirectional(ex.kindProperty(), cboKind.valueProperty(),
                    new StringConverter<>() {

                        @Override
                        public String toString(ExtraKind.EXTRA_KIND t) {
                            return t.toString();
                        }

                        @Override
                        public ExtraKind.EXTRA_KIND fromString(String string) {
                            return ExtraKind.getExtraKind(string);
                        }
                    });
            cboKind.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                makeHBox(hBoxInit, ex);
            });
            gridPane.add(cboKind, 3, row);

        }


        gridPane.add(new Label(" "), 0, ++row);

        final Button btnHelp = new Button("");
        btnHelp.setTooltip(new Tooltip("Hilfe anzeigen."));
        btnHelp.setGraphic(new ProgIcons().ICON_BUTTON_HELP);
        btnHelp.setOnAction(a -> PAlert.showHelpAlert(stage, "Extrafelder",
                "Die Mitgliedsdaten, Beitragsdaten und Finanzdaten enthalten ein paar zusätzliche Felder " +
                        "die das Programm selbst nicht nutzt. Diese sind gedacht, für weitere " +
                        "Infos die für den Verein wichtig sind. Mit diesen zusätzlichen Feldern können diese Infos " +
                        "erfasst werden. Die verwendeten Felder können hier eingeschaltet und der " +
                        "Titel angepasst werden."));
        GridPane.setHalignment(btnHelp, HPos.RIGHT);
        gridPane.add(btnHelp, 3, ++row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

    }

    private void makeHBox(HBox hBox, ExtraData ex) {
        hBox.getChildren().clear();

        if (ex.getKindSave().equals(ExtraKind.EXTRA_KIND.INTEGER.toString())) {
            IntegerProperty ip = new SimpleIntegerProperty();
            PTextFieldInteger txtStart = new PTextFieldInteger(ip);
            txtStart.setText(ex.getInitValue());

            Bindings.bindBidirectional(ex.initValueProperty(), txtStart.getIntegerProperty(),
                    new StringConverter<>() {

                        @Override
                        public String toString(Number b) {
                            return b.toString();
                        }

                        @Override
                        public Integer fromString(String string) {
                            try {
                                return Integer.parseInt(string);
                            } catch (NumberFormatException ex) {
                                return 0;
                            }
                        }
                    });
            hBox.getChildren().add(txtStart);

        } else if (ex.getKindSave().equals(ExtraKind.EXTRA_KIND.BOOLEAN.toString())) {
            CheckBox cbx = new CheckBox();
            cbx.setSelected(Boolean.parseBoolean(ex.getInitValue()));
            Bindings.bindBidirectional(ex.initValueProperty(), cbx.selectedProperty(),
                    new StringConverter<>() {

                        @Override
                        public String toString(Boolean b) {
                            return b.toString();
                        }

                        @Override
                        public Boolean fromString(String string) {
                            return Boolean.parseBoolean(string);
                        }
                    });
            hBox.getChildren().addAll(cbx);

        } else if (ex.getKindSave().equals(ExtraKind.EXTRA_KIND.STRING.toString())) {
//            ex.setKind(ExtraKind.EXTRA_KIND.STRING.toString());
            TextField txtStart = new TextField();
            txtStart.textProperty().bindBidirectional(ex.initValueProperty());
            hBox.getChildren().addAll(txtStart);

        } else {
            throw new PException("ExtraData getConfig");
        }

    }

}
