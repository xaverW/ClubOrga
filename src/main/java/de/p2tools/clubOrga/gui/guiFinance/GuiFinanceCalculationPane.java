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

package de.p2tools.clubOrga.gui.guiFinance;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.financeData.FinanceCalculationData;
import de.p2tools.clubOrga.data.financeData.FinanceCalculationDataList;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.gui.table.ClubTable;
import de.p2tools.p2Lib.guiTools.PTextFieldMoney;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class GuiFinanceCalculationPane extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();

    private final VBox vbCategory = new VBox();
    private final VBox vbAccount = new VBox();
    private final VBox vbSaldo = new VBox();

    private final Label lblTitleSaldo = new Label("Saldo");
    private final Label lblTitleAccount = new Label("Konten");
    private final Label lblTitleCategory = new Label("Kategorien");

    private final PTextFieldMoney lblSum = new PTextFieldMoney(true);

    private final ScrollPane scrollPaneTableCategory = new ScrollPane();
    private final ScrollPane scrollPaneTableAccount = new ScrollPane();
    private final TableView<FinanceCalculationData> tableViewCategory = new TableView<>();
    private final TableView<FinanceCalculationData> tableViewAccount = new TableView<>();

    private final FinanceCalculationDataList calculationDataListAccount;
    private final FinanceCalculationDataList calculationDataListCategory;

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private final FilteredList<FinanceData> financeDataList;

    DoubleProperty doublePropertySplit_0;
    DoubleProperty doublePropertySplit_1;

    public GuiFinanceCalculationPane(ClubConfig clubConfig, FilteredList<FinanceData> financeDataFilteredList) {
        this.clubConfig = clubConfig;
        progData = ProgData.getInstance();
        this.financeDataList = financeDataFilteredList;

        calculationDataListAccount = new FinanceCalculationDataList(clubConfig, false);
        calculationDataListCategory = new FinanceCalculationDataList(clubConfig, true);
        lblSum.bindBidirectional(calculationDataListAccount.getSumProperty());

        doublePropertySplit_0 = clubConfig.GUI_PANEL_FINANCES_DIVIDER_CALCULATION_0;
        doublePropertySplit_1 = clubConfig.GUI_PANEL_FINANCES_DIVIDER_CALCULATION_1;

        getChildren().addAll(splitPane);
        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.getItems().addAll(vbSaldo, vbAccount, vbCategory);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(doublePropertySplit_0);
        splitPane.getDividers().get(1).positionProperty().bindBidirectional(doublePropertySplit_1);

        initGui();
        initTable();

        financeDataList.addListener((ListChangeListener<FinanceData>) c -> {
            setFinanceDataChanged();
        });
    }

    public void saveTable() {
        new ClubTable(clubConfig).saveTable(tableViewAccount, ClubTable.TABLE.CALCULATION_ACCOUNT);
        new ClubTable(clubConfig).saveTable(tableViewCategory, ClubTable.TABLE.CALCULATION_CATEGORY);
    }

    public void setFinanceDataChanged() {
        if (clubConfig.isClubIsStarting()) {
//            System.out.println("erst starten");
            return;
        }

//        System.out.println("--> setFinanceDataChanged");
//        lblSum.setText(reportDataListCategory.addData(financeDataList) + "");
        calculationDataListAccount.addData(financeDataList);
        calculationDataListCategory.addData(financeDataList);
    }

    private void initGui() {
        //=====================
        // saldo
        addTitle(vbSaldo, lblTitleSaldo);

        HBox hbSaldo = new HBox(10);
        hbSaldo.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(lblSum, Priority.ALWAYS);
        lblSum.setMaxWidth(Double.MAX_VALUE);
        Label lbl = new Label("Saldo: ");
        lbl.setMinWidth(Region.USE_PREF_SIZE);
        hbSaldo.getChildren().addAll(lbl, lblSum);
        vbSaldo.getChildren().addAll(hbSaldo);

        //=====================
        // table account
        addTitle(vbAccount, lblTitleAccount);

        scrollPaneTableAccount.setFitToHeight(true);
        scrollPaneTableAccount.setFitToWidth(true);
        scrollPaneTableAccount.setContent(tableViewAccount);

        VBox.setVgrow(scrollPaneTableAccount, Priority.ALWAYS);
        vbAccount.getChildren().addAll(scrollPaneTableAccount);

        //=====================
        // table category
        addTitle(vbCategory, lblTitleCategory);

        scrollPaneTableCategory.setFitToHeight(true);
        scrollPaneTableCategory.setFitToWidth(true);
        scrollPaneTableCategory.setContent(tableViewCategory);

        VBox.setVgrow(scrollPaneTableCategory, Priority.ALWAYS);
        vbCategory.getChildren().addAll(scrollPaneTableCategory);
    }

    private void addTitle(VBox vBox, Label lbl) {
        HBox hb = new HBox();
        hb.getChildren().add(lbl);
        hb.setAlignment(Pos.CENTER);
        lbl.getStyleClass().add("headerLabel");
        lbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lbl, Priority.ALWAYS);
        lbl.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hb);

        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
    }

    private void initTable() {
        tableViewAccount.setTableMenuButtonVisible(true);
        tableViewAccount.setEditable(false);
        tableViewAccount.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableViewAccount.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        tableViewCategory.setTableMenuButtonVisible(true);
        tableViewCategory.setEditable(false);
        tableViewCategory.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableViewCategory.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        tableViewAccount.setItems(calculationDataListAccount);
        tableViewCategory.setItems(calculationDataListCategory);

        new ClubTable(clubConfig).setTable(tableViewAccount, ClubTable.TABLE.CALCULATION_ACCOUNT);
        new ClubTable(clubConfig).addResetMenue(tableViewAccount, ClubTable.TABLE.CALCULATION_ACCOUNT);

        new ClubTable(clubConfig).setTable(tableViewCategory, ClubTable.TABLE.CALCULATION_CATEGORY);
        new ClubTable(clubConfig).addResetMenue(tableViewCategory, ClubTable.TABLE.CALCULATION_CATEGORY);
    }

}
