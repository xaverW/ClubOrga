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


package de.p2tools.cluborga.gui.dialog.dataDialog;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.data.extraData.ExtraData;
import de.p2tools.cluborga.data.feeData.FeeData;
import de.p2tools.cluborga.data.feeData.FeeFactory;
import de.p2tools.cluborga.data.feeData.FeeFieldNames;
import de.p2tools.cluborga.data.memberData.MemberData;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.config.Config_intProp;
import de.p2tools.p2lib.configfile.config.Config_stringProp;
import de.p2tools.p2lib.guitools.*;
import de.p2tools.p2lib.guitools.ptoggleswitch.PToggleSwitch;
import de.p2tools.p2lib.tools.PException;
import de.p2tools.p2lib.tools.date.PDate;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class FeePane extends VBox {

    private final FeeData feeData;
    private final MemberData memberData;
    private final ComboBox<PDate> cboJahr = new ComboBox();
    private boolean payFee = false;
    private final boolean showPay;

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public FeePane(ClubConfig clubConfig, FeeData feeData, MemberData memberData, boolean showPay) {

        this.clubConfig = clubConfig;
        this.feeData = feeData;
        this.memberData = memberData;
        this.showPay = showPay;

        this.progData = ProgData.getInstance();
        make();
    }


    private void make() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        addFeeData(gridPane);

        ScrollPane scrollPane = new ScrollPane(gridPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        getChildren().add(scrollPane);
    }

    public void payFee() {
        //wenn auf bezahlen geklickt wurde, dann jetzt bezahlen (Dialog wírd mit OK beendet)
        if (payFee) {
            FeeFactory.payFee(clubConfig, feeData);
        }
    }

    private void addFeeData(GridPane gridPane) {
        int row = 0;
        Config[] configs = feeData.getConfigsArr();

        try {
            outerloop:
            for (int i = 0; i < configs.length; ++i) {
                Control control;
                Config config = configs[i];

                if (config.getName().equals(FeeFieldNames.ID) ||
                        config.getName().equals(FeeFieldNames.MEMBER_ID)) {
                    continue;

                } else if (config.getName().equals(FeeFieldNames.JAHR)) {
                    PYearPicker pYearPicker = new PYearPicker();
                    pYearPicker.bindBidirectional(((Config_intProp) config).getProperty());
                    control = pYearPicker;

                } else if (config.getName().equals(FeeFieldNames.ZAHLART)) {
                    control = getPComboObject(feeData.paymentTypeDataProperty(), clubConfig.paymentTypeDataList);

                } else if (config.getName().equals(FeeFieldNames.BEZAHLT)) {
                    HBox hBox = new HBox(10);
                    Control c = getControl(config);
//                    feeData.bezahltProperty().addListener((observable, oldValue, newValue) -> {
//                        if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
//                            ((PDatePicker) c).setDate(newValue);
//                        }
//                    });
//                    PDatePropertyPicker c = new PDatePropertyPicker();
//                    c.bindPDateProperty(feeData.bezahltProperty());
                    c.setMaxWidth(Double.MAX_VALUE);
                    GridPane.setHgrow(c, Priority.ALWAYS);

                    PToggleSwitch tglPay = new PToggleSwitch();
                    tglPay.setTooltip(new Tooltip("diesen Beitrag bezahlen"));
                    tglPay.selectedProperty().addListener((o, ol, ne) -> payFee = true);
                    Optional<FeeData> found = Optional.ofNullable(clubConfig.feeDataList.stream()
                            .filter(f -> f.getId() == feeData.getId()).findAny().orElse(null));
                    if (!found.isPresent()) {
                        tglPay.setDisable(true);
                    }

                    tglPay.setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(tglPay, Priority.ALWAYS);
                    hBox.getChildren().addAll(new Label("Beitrag bezahlen"), tglPay);
                    GridPane.setHgrow(hBox, Priority.ALWAYS);

                    gridPane.add(new Label(config.getName() + ":"), 0, row);
                    gridPane.add(c, 1, row++);
                    if (showPay) {
                        gridPane.add(hBox, 1, row++);
                    }
                    continue;

                } else if (config.getName().equals(FeeFieldNames.ERSTELLDATUM)) {
                    control = new PTextField(feeData.getErstellDatum().toString(), true);

                } else {
                    for (ExtraData ex : clubConfig.extraDataListFee) {
                        // ExtraData die ausgeschaltet sind
                        if (config.getKey().equals(ex.getKey()) && !ex.isOn()) {
                            continue outerloop;
                        }
                    }
                    control = getControl(config);
                }

                control.setMaxWidth(Double.MAX_VALUE);
                gridPane.add(new Label(config.getName() + ":"), 0, row);
                gridPane.add(control, 1, row);
                ++row;

            }
        } catch (PException ex) {
            PLog.errorLog(951203647, ex);
        }
    }

    private Control getControl(Config config) {
        Control control;
        if (config.getName().equals(FeeFieldNames.MEMBER_NO)) {
            control = new PTextFieldLong(true);
            if (memberData != null) {
                ((PTextFieldLong) control).bindBidirectional(memberData.noProperty());
            }

        } else if (config.getName().equals(FeeFieldNames.MEMBER_NAME)) {
            control = new PTextField(true);
            if (memberData != null) {
                ((PTextField) control).bindBidirectional(memberData.nachnameProperty());
            }

        } else {
            control = config.getControl();
        }

        return control;
    }

    private PComboBoxString getPCombo(Config_stringProp configData, ObservableList<String> cbo_list) {
        final Config_stringProp configData_ = configData;
        final PComboBoxString pCombo = new PComboBoxString();
        pCombo.setMaxWidth(Double.MAX_VALUE);
        pCombo.init(cbo_list, (configData_.getProperty()));

        return pCombo;
    }

    private PComboBoxObject getPComboObject(ObjectProperty objectProperty, ObservableList cbo_list) {
        final PComboBoxObject cboStatus = new PComboBoxObject<>();
        cboStatus.setMaxWidth(Double.MAX_VALUE);
        cboStatus.init(cbo_list, objectProperty);

        return cboStatus;
    }

    public boolean isOk() {
        return true;
    }
}
