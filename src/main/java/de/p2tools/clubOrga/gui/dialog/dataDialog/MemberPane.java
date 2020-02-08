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


package de.p2tools.clubOrga.gui.dialog.dataDialog;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.extraData.ExtraData;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberFeeWorker;
import de.p2tools.clubOrga.data.memberData.MemberFieldNames;
import de.p2tools.p2Lib.configFile.config.ConfigExtra;
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import de.p2tools.p2Lib.guiTools.*;
import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MemberPane extends VBox {

    private final MemberData memberData;

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private final PTextFieldMoney txtBeitrag = new PTextFieldMoney();
    private final PComboBoxObject<FeeRateData> cboBeitragssatz = new PComboBoxObject();
    private final MemberFeeWorker memberFeeWorker;

    public MemberPane(ClubConfig clubConfig, MemberData memberData) {

        this.clubConfig = clubConfig;
        this.memberData = memberData;

        this.progData = ProgData.getInstance();
        memberFeeWorker = new MemberFeeWorker(cboBeitragssatz, txtBeitrag, clubConfig);
        make();
    }


    private void make() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        addMemberData(gridPane);

        ScrollPane scrollPane = new ScrollPane(gridPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        getChildren().add(scrollPane);

//        VBox.setVgrow(gridPane, Priority.ALWAYS);
//        getChildren().add(gridPane);
    }

    private void addMemberData(GridPane gridPane) {
        memberFeeWorker.bind(memberData);

        int row = 0;
        ConfigExtra[] configs = memberData.getConfigsArr();

        try {
            outerloop:
            for (int i = 0; i < configs.length; ++i) {
                Control control;
                ConfigExtra config = configs[i];

                if (config.getName().equals(MemberFieldNames.ID)) {
                    continue;

                } else if (config.getName().equals(MemberFieldNames.BEITRAG)) {
                    control = txtBeitrag;

                } else if (config.getName().equals(MemberFieldNames.BEITRAGSSATZ)) {
                    control = getPComboObject(memberData.feeRateDataProperty(), clubConfig.feeRateDataList);

                } else if (config.getName().equals(MemberFieldNames.ANREDE)) {
                    control = getPCombo((ConfigStringPropExtra) config, clubConfig.CBO_LIST_ANREDE);

                } else if (config.getName().equals(MemberFieldNames.PLZ)) {
                    final ConfigStringPropExtra configData_ = (ConfigStringPropExtra) config;
                    final PComboBoxString pCombo = getPCombo(configData_, clubConfig.CBO_LIST_PLZ);
                    pCombo.setRegEx(configData_.getRegEx());
                    control = pCombo;

                } else if (config.getName().equals(MemberFieldNames.ORT)) {
                    control = getPCombo((ConfigStringPropExtra) config, clubConfig.CBO_LIST_ORT);

                } else if (config.getName().equals(MemberFieldNames.LAND)) {
                    control = getPCombo((ConfigStringPropExtra) config, clubConfig.CBO_LIST_LAND);

                } else if (config.getName().equals(MemberFieldNames.STATUS)) {
                    control = getPComboObject(memberData.stateDataProperty(), clubConfig.stateDataList);

                } else if (config.getName().equals(MemberFieldNames.ZAHLART)) {
                    control = getPComboObject(memberData.paymentTypeDataProperty(), clubConfig.paymentTypeDataList);

                } else if (config.getName().equals(MemberFieldNames.ERSTELLDATUM)) {
                    control = new PTextField(memberData.getErstellDatum().toString(), true);

                } else {
                    // rest
                    for (ExtraData ex : clubConfig.extraDataListMember) {
                        // ExtraData die ausgeschaltet sind
                        if (config.getKey().equals(ex.getKey()) && !ex.isOn()) {
                            continue outerloop;
                        }
                    }

                    control = config.getControl();
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

    private PComboBoxString getPCombo(ConfigStringPropExtra configData, ObservableList<String> cbo_list) {
        final PComboBoxString pCombo = new PComboBoxString();
        pCombo.setMaxWidth(Double.MAX_VALUE);
        pCombo.init(cbo_list, (configData.getProperty()));

        return pCombo;
    }

    private PComboBoxObject getPComboObject(ObjectProperty objectProperty, ObservableList cbo_list) {
        final PComboBoxObject cboStatus = new PComboBoxObject<>();
        cboStatus.setMaxWidth(Double.MAX_VALUE);
        cboStatus.init(cbo_list, objectProperty);

        return cboStatus;
    }

    private boolean check() {
        boolean ret = true;
        return ret;
    }

    public boolean isOk() {
        return check();
    }
}
