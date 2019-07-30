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
import de.p2tools.clubOrga.data.feeData.FeeFieldNames;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberDataList;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.gui.FilterPane;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PFilterControl.PFilterComboBoxObject;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class GuiFeeFilterPane extends FilterPane {

    private final PFilterComboBoxObject<PaymentTypeData> filterCboMitgliedZahlart = new PFilterComboBoxObject<>();
    private final PFilterComboBoxObject<MemberData> filterCboMitgliedName = new PFilterComboBoxObject<>();
    private final PToggleSwitch tglDelMember = new PToggleSwitch("Beiträge gelöschter Mitglieder:");
    private final PToggleSwitch tglBezahlt = new PToggleSwitch("Bezahlt:");
    private final PToggleSwitch tglRechnung = new PToggleSwitch("Rechnungen:");
    private final PToggleSwitch tglSq = new PToggleSwitch("Spendenquittungen:");
    private final Label lblDelMember = new Label("");
    private final Label lblBezahlt = new Label("");
    private final Label lblRechnung = new Label("");
    private final Label lblSq = new Label("");
    private final Label lblNachname = new Label(FeeFieldNames.MITGLIED_NAME_);
    private final Label lblJahr = new Label(FeeFieldNames.JAHR_);
    private final Label lblZahlart = new Label(FeeFieldNames.ZAHLART_);
    private final PFilterComboBoxObject<Integer> filterCboJahr = new PFilterComboBoxObject<>();

    private final ClubConfig clubConfig;


    public GuiFeeFilterPane(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;

        initLayout();
        initFilter();
    }

    private void initLayout() {
        VBox vBox = new VBox();
        vBox.setSpacing(10);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        int row = 0;

        gridPane.add(lblNachname, 0, row);
        gridPane.add(filterCboMitgliedName, 1, row);
        gridPane.add(lblJahr, 0, ++row);
        gridPane.add(filterCboJahr, 1, row);
        gridPane.add(lblZahlart, 0, ++row);
        gridPane.add(filterCboMitgliedZahlart, 1, row);

        // leere Spalte
        gridPane.add(new Label(" "), 2, row);

        row = 0;
        gridPane.add(tglRechnung, 3, row);
        gridPane.add(lblRechnung, 4, row);
        gridPane.add(tglBezahlt, 3, ++row);
        gridPane.add(lblBezahlt, 4, row);
        gridPane.add(tglSq, 3, ++row);
        gridPane.add(lblSq, 4, row);

        gridPane.add(tglDelMember, 3, ++row);
        gridPane.add(lblDelMember, 4, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(), PColumnConstraints.getCcPrefMaxSize(250),
                PColumnConstraints.getCcMinSize(20),
                PColumnConstraints.getCcPrefSize(), PColumnConstraints.getCcPrefSize());

        vBox.getChildren().add(gridPane);
        vbFilter.getChildren().add(vBox);
    }

    private void initFilter() {
        SortedList<MemberData> sortedList = new SortedList<MemberData>(clubConfig.memberDataList);
        sortedList.setComparator(MemberDataList.getComperator());

        filterCboMitgliedName.disableProperty().bind(tglDelMember.indeterminateProperty().not());
        lblNachname.disableProperty().bind(tglDelMember.indeterminateProperty().not());

        filterCboMitgliedName.init(sortedList, clubConfig.FEE_FILTER_MITGLIED);
        clubConfig.FEE_FILTER_MITGLIED.addListener((observable, oldValue, newValue) -> {
            announceFilterChange();
        });

        filterCboMitgliedZahlart.init(clubConfig.paymentTypeDataList, clubConfig.FEE_FILTER_PAYMENT_TYPE);
        clubConfig.FEE_FILTER_PAYMENT_TYPE.addListener((observable, oldValue, newValue) -> {
            announceFilterChange();
        });

        filterCboJahr.init(clubConfig.feeDataList.getJahrList(), clubConfig.FEE_FILTER_JAHR);
        clubConfig.FEE_FILTER_JAHR.addListener((observable, oldValue, newValue) -> {
            announceFilterChange();
        });

        tglDelMember.setAllowIndeterminate(true);
        tglBezahlt.setAllowIndeterminate(true);
        tglRechnung.setAllowIndeterminate(true);
        tglSq.setAllowIndeterminate(true);

        tglDelMember.setLabelRight(lblDelMember, "anzeigen", "nicht anzeigen", "");
        tglBezahlt.setLabelRight(lblBezahlt, "bezahlt", "nicht bezahlt", "");
        tglRechnung.setLabelRight(lblRechnung, "erstellt", "nicht erstellt", "");
        tglSq.setLabelRight(lblSq, "erstellt", "nicht erstellt", "");

        tglDelMember.selectedProperty().bindBidirectional(clubConfig.FEE_FILTER_DEL_MEMBER);
        tglDelMember.selectedProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());
        tglDelMember.indeterminateProperty().bindBidirectional(clubConfig.getFEE_FILTER_DEL_MEMBER_OFF);
        tglDelMember.indeterminateProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());

        tglBezahlt.selectedProperty().bindBidirectional(clubConfig.FEE_FILTER_BEZAHLT);
        tglBezahlt.selectedProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());
        tglBezahlt.indeterminateProperty().bindBidirectional(clubConfig.FEE_FILTER_BEZAHLT_OFF);
        tglBezahlt.indeterminateProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());

        tglRechnung.selectedProperty().bindBidirectional(clubConfig.FEE_FILTER_RECHNUNG);
        tglRechnung.selectedProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());
        tglRechnung.indeterminateProperty().bindBidirectional(clubConfig.FEE_FILTER_RECHNUNG_OFF);
        tglRechnung.indeterminateProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());

        tglSq.selectedProperty().bindBidirectional(clubConfig.FEE_FILTER_SQ);
        tglSq.selectedProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());
        tglSq.indeterminateProperty().bindBidirectional(clubConfig.FEE_FILTER_SQ_OFF);
        tglSq.indeterminateProperty().addListener((observable, oldValue, newValue) -> announceFilterChange());
    }

    private void announceFilterChange() {
        clubConfig.feeFilterChange.setValue(!clubConfig.feeFilterChange.getValue());
    }

    public void clearFilter() {
        clubConfig.feeDataList.clearSelected();

        filterCboMitgliedName.clearSelection();
        filterCboMitgliedZahlart.clearSelection();
        filterCboJahr.clearSelection();

        tglDelMember.setIndeterminate(true);
        tglBezahlt.setIndeterminate(true);
        tglRechnung.setIndeterminate(true);
        tglSq.setIndeterminate(true);

        announceFilterChange();
    }


}