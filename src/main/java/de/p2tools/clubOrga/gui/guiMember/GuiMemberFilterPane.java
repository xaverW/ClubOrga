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
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.memberData.stateData.StateData;
import de.p2tools.clubOrga.gui.FilterPane;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PFilterControl.PFilterComboBoxObject;
import de.p2tools.p2Lib.guiTools.PFilterControl.PFilterTextField;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GuiMemberFilterPane extends FilterPane {

    private final PFilterTextField filterTxtNachname = new PFilterTextField();
    private final PFilterComboBoxObject<FeeRateData> filterCboBeitrag = new PFilterComboBoxObject<>();
    private final PFilterComboBoxObject<StateData> filterCboStatus = new PFilterComboBoxObject<>();
    private final PFilterComboBoxObject<PaymentTypeData> filterCboPaymentType = new PFilterComboBoxObject<>();

    private final PToggleSwitch tglFee = new PToggleSwitch("Beiträge:");
    private final PToggleSwitch tglFeePayed = new PToggleSwitch("angelegte Beiträge:");
    private final Label lblFee = new Label("");
    private final Label lblFeePayed = new Label("");


    private final ClubConfig clubConfig;

    public GuiMemberFilterPane(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;

        initLayout();
        initFilter();
    }

    private void initLayout() {
        int row = 0;
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(new Label("Nachname:"), 0, row);
        gridPane.add(filterTxtNachname, 1, row);
        gridPane.add(new Label("Status:"), 0, ++row);
        gridPane.add(filterCboStatus, 1, row);
        gridPane.add(new Label("Beitrag:"), 0, ++row);
        gridPane.add(filterCboBeitrag, 1, row);
        gridPane.add(new Label("Zahlart:"), 0, ++row);
        gridPane.add(filterCboPaymentType, 1, row);

        row = 0;
        gridPane.add(tglFee, 3, row);
        gridPane.add(lblFee, 4, row);
        gridPane.add(tglFeePayed, 3, ++row);
        gridPane.add(lblFeePayed, 4, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(), PColumnConstraints.getCcPrefMaxSize(250),
                PColumnConstraints.getCcMinSize(20),
                PColumnConstraints.getCcPrefSize(), PColumnConstraints.getCcPrefSize());

        vbFilter.getChildren().addAll(gridPane);
    }

    private void initFilter() {
        filterCboStatus.init(clubConfig.stateDataList, clubConfig.MEMBER_FILTER_STATUS);
        clubConfig.MEMBER_FILTER_STATUS.addListener((observable, oldValue, newValue) -> {
            announceFilterChange();
        });
        filterCboBeitrag.init(clubConfig.feeRateDataList, clubConfig.MEMBER_FILTER_BEITRAG);
        clubConfig.MEMBER_FILTER_BEITRAG.addListener((observable, oldValue, newValue) -> {
            announceFilterChange();
        });
        filterCboPaymentType.init(clubConfig.paymentTypeDataList, clubConfig.MEMBER_FILTER_PAYMENT_TYPE);
        clubConfig.MEMBER_FILTER_PAYMENT_TYPE.addListener((observable, oldValue, newValue) -> {
            announceFilterChange();
        });

        filterTxtNachname.init(clubConfig.MEMBER_FILTER_NACHNAME);
        clubConfig.MEMBER_FILTER_NACHNAME.addListener((observable, oldValue, newValue) -> {
            announceFilterChange();
        });


        tglFee.setAllowIndeterminate(true);
        tglFeePayed.setAllowIndeterminate(true);
        lblFee.visibleProperty().bind(tglFee.indeterminateProperty().not());
        lblFeePayed.visibleProperty().bind(tglFeePayed.indeterminateProperty().not());

        tglFee.setLabelRight(lblFee, "alle angelegt", "fehlen", "");
        tglFeePayed.setLabelRight(lblFeePayed, "sind bezahlt", "nicht bezahlt", "");

        tglFee.selectedProperty().bindBidirectional(clubConfig.MEMBER_FILTER_FEE);
        tglFee.selectedProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());
        tglFee.indeterminateProperty().bindBidirectional(clubConfig.MEMBER_FILTER_FEE_OFF);
        tglFee.indeterminateProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());

        tglFeePayed.selectedProperty().bindBidirectional(clubConfig.MEMBER_FILTER_FEE_PAYED);
        tglFeePayed.selectedProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());
        tglFeePayed.indeterminateProperty().bindBidirectional(clubConfig.MEMBER_FILTER_FEE_PAYED_OFF);
        tglFeePayed.indeterminateProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());
    }

    private void announceFilterChange() {
        clubConfig.memberFilterChange.setValue(!clubConfig.memberFilterChange.getValue());
    }

    public void clearFilter() {
        clubConfig.memberDataList.clearSelected();
        filterTxtNachname.clearText();
        tglFee.setIndeterminate(true);
        tglFeePayed.setIndeterminate(true);
        filterCboStatus.clearSelection();
        filterCboBeitrag.clearSelection();
        filterCboPaymentType.clearSelection();

        announceFilterChange();
    }
}
