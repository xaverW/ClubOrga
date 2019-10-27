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

package de.p2tools.clubOrga.gui.dialog.listDialog;


import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.gui.table.ClubTable;
import de.p2tools.p2Lib.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public abstract class abListDialogController extends PDialogExtra {

    final Button btnOk = new Button("Ok");
    final Button btnCancel = new Button("Abbrechen");
    Button btnHelp;

    private final SplitPane splitPane = new SplitPane();

    private final TableView<FeeData> tableView = new TableView<>();
    private final ScrollPane scrollPaneTable = new ScrollPane();

    private final VBox vBox = new VBox(10);


    String helpHeader = "";
    String helpText = "";

    final GridPane gridPane = new GridPane();
    int row = 0;

    private boolean ok = false;
    Stage stage;

    ObservableList<FeeData> feeDataList;
    DoubleProperty dividerProp;

    final ClubConfig clubConfig;


    public abListDialogController(ClubConfig clubConfig, ObservableList<FeeData> feeDataList, String title) {
        super(clubConfig.getStage(), clubConfig.FEE_DIALOG_SIZE, title);

        this.clubConfig = clubConfig;
        this.feeDataList = feeDataList;

        dividerProp = clubConfig.FEE_DIALOG_DIVIDER;
    }

    public abListDialogController(ClubConfig clubConfig, StringProperty size, DoubleProperty dividerProp,
                                  ObservableList<FeeData> feeDataList, String title) {
        super(clubConfig.getStage(), size, title);

        this.clubConfig = clubConfig;
        this.feeDataList = feeDataList;
        this.dividerProp = dividerProp;
    }

    void init() {
        init(getVBoxCompleteDialog(), true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    protected void make() {
        this.stage = getStage();
        btnOk.setOnAction(a -> {
            if (check()) {
                ok = true;
                close();
            }
        });
        btnCancel.setOnAction(a -> close());

        btnHelp = PButton.helpButton(getStage(), helpHeader, helpText);
//        HBox hBoxHelp = new HBox();
//        hBoxHelp.setAlignment(Pos.CENTER_LEFT);
//        hBoxHelp.getChildren().addAll(btnHelp);

        HBox hBox = new HBox();
        HBox.setHgrow(hBox, Priority.ALWAYS);
        getHboxOk().getChildren().addAll(btnHelp, hBox, btnOk, btnCancel);

        scrollPaneTable.setContent(tableView);
        scrollPaneTable.setFitToHeight(true);
        scrollPaneTable.setFitToWidth(true);

        VBox.setVgrow(splitPane, Priority.ALWAYS);
        SplitPane.setResizableWithParent(vBox, false);
        splitPane.getItems().addAll(scrollPaneTable, vBox);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(dividerProp);


        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());


        vBox.getChildren().add(gridPane);
        getVboxCont().getChildren().add(splitPane);

        initTable();
    }

    boolean check() {
        return true;
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        new ClubTable(clubConfig).setTable(tableView, ClubTable.TABLE.PAY_FEE);
        new ClubTable(clubConfig).addResetMenue(tableView, ClubTable.TABLE.PAY_FEE);

        tableView.setItems(feeDataList);

        feeDataList.addListener(new ListChangeListener<FeeData>() {
            @Override
            public void onChanged(Change<? extends FeeData> c) {
                if (feeDataList.isEmpty()) {
                    close();
                }
            }
        });
    }

    @Override
    public void close() {
        new ClubTable(clubConfig).saveTable(tableView, ClubTable.TABLE.PAY_FEE);
        super.close();
    }

}
