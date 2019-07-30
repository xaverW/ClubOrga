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


package de.p2tools.clubOrga.data.feeData.feeRateData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.p2Lib.guiTools.PComboBoxObject;
import de.p2tools.p2Lib.guiTools.PTextFieldMoney;

public class FeeRateWorker {

    private ClubConfig clubConfig;
    private MemberData memberData = null;
    private final PTextFieldMoney txtBeitrag;
    private final PComboBoxObject<FeeRateData> cboBeitragssatz;

    public FeeRateWorker(PComboBoxObject<FeeRateData> cboBeitragssatz, PTextFieldMoney txtBeitrag, ClubConfig clubConfig) {
        this.cboBeitragssatz = cboBeitragssatz;
        this.txtBeitrag = txtBeitrag;
        this.clubConfig = clubConfig;

        cboBeitragssatz.init(clubConfig.feeRateDataList);
        cboBeitragssatz.setMaxWidth(Double.MAX_VALUE);
        cboBeitragssatz.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setTxtBeitrag();
        });
    }

    public void clearCbo() {
        cboBeitragssatz.clearSelection();
    }

    public void unbind() {
        cboBeitragssatz.unbindSelValueProperty();
        txtBeitrag.unBind();
        this.memberData = null;
    }

    public void bind(MemberData memberData) {
        this.memberData = memberData;

        cboBeitragssatz.bindSelValueProperty(memberData.feeRateDataProperty());
        txtBeitrag.bindBidirectional(memberData.beitragProperty());
//        setTxtBeitrag();
    }

    private void setTxtBeitrag() {
        FeeRateData item = cboBeitragssatz.getSelectionModel().getSelectedItem();
        if (item == null) {
            txtBeitrag.setText("");
            txtBeitrag.setLabelLike(true);
            return;
        }

        if (item.getId() == FeeRateFactory.RATE_TYPE.RATE_FREE.getId()) {
            txtBeitrag.setLabelLike(false);
        } else if (item.getId() == FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getId()) {
            txtBeitrag.setLabelLike(true);
            txtBeitrag.setText("0");
        } else {
            txtBeitrag.setLabelLike(true);
            txtBeitrag.setText(Long.toString(item.getBetrag()));
        }
    }
}
