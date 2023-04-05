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

package de.p2tools.cluborga.gui.guiClub;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.controller.export.clubInfos.ExportClubInfosDialogController;
import de.p2tools.cluborga.data.clubInfoData.ClubInfoData;
import de.p2tools.cluborga.data.clubInfoData.ExportVereinsinfo;
import de.p2tools.cluborga.data.clubInfoData.InfoFactory;
import de.p2tools.cluborga.data.clubInfoData.InfoFactoryPdf;
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiClubInfo extends AnchorPane {

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private final TreeTableView<ClubInfoData> treeTableView = new TreeTableView<>();
    private final String GROUP_STYLE_1 = "goupStyle1";
    private final String GROUP_STYLE_2 = "goupStyle2";
    private final String BEFORE = "_____";

    public GuiClubInfo(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;

        initCont();
        initTable();
    }

    public void isShown() {
        initTable();
    }


    private void initCont() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(10));
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);

        VBox vBox = new VBox(10);
        vBox.getChildren().add(treeTableView);
        VBox.setVgrow(treeTableView, Priority.ALWAYS);

        scrollPane.setContent(vBox);

        Button btnExport = new Button("Exportieren");
        btnExport.setTooltip(new Tooltip("Infos in Datei exportieren"));
        btnExport.setOnAction(a -> exportInfos());
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().add(new Label("Infos in Datei schreiben:"));
        hBox.getChildren().add(btnExport);
        vBox.getChildren().add(hBox);

        this.getChildren().add(scrollPane);
    }

    private void exportInfos() {
        ArrayList<String> list = new ArrayList<>();
        InfoFactoryPdf.generateInfoList(clubConfig, list);
        ExportClubInfosDialogController infosDialogController = new ExportClubInfosDialogController(clubConfig, list);

        if (infosDialogController.isOk()) {
//            if (!new CreateClubInfos(clubConfig).createPdf(infosDialogController.destDir, infosDialogController.destFile, list)) {
//                PLog.errorLog(774196698, "Die Vereinsinfos konnten nicht in ein PDF geschrieben werden.");
//
//                Path pathExportFile = Paths.get(infosDialogController.destDir, infosDialogController.destFile);
//                PAlert.showErrorAlert(clubConfig.getStage(), "Export der Vereinsinfos fehlgeschlagen",
//                        "Das Schreiben der Verinsinfos:" + P2LibConst.LINE_SEPARATOR +
//                                pathExportFile.toString() + P2LibConst.LINE_SEPARATOR +
//                                "hat nicht geklappt");
//            }
            if (!new ExportVereinsinfo(clubConfig).createPdf(infosDialogController.destDir, infosDialogController.destFile, list)) {
                PLog.errorLog(774196698, "Die Vereinsinfos konnten nicht in ein PDF geschrieben werden.");

                Path pathExportFile = Paths.get(infosDialogController.destDir, infosDialogController.destFile);
                PAlert.showErrorAlert(clubConfig.getStage(), "Export der Vereinsinfos fehlgeschlagen",
                        "Das Schreiben der Verinsinfos:" + P2LibConst.LINE_SEPARATOR +
                                pathExportFile.toString() + P2LibConst.LINE_SEPARATOR +
                                "hat nicht geklappt");
            }
        }
    }

    private void initTable() {
        treeTableView.getColumns().clear();

        treeTableView.setRoot(InfoFactory.generate(clubConfig));
        treeTableView.getColumns().addAll(createColumns());
        addRowFact(treeTableView);
    }

    private void addRowFact(TreeTableView<ClubInfoData> table) {
        table.setRowFactory(tableview -> new TreeTableRow<>() {
            @Override
            public void updateItem(ClubInfoData clubInfoData, boolean empty) {
                super.updateItem(clubInfoData, empty);

                getStyleClass().removeAll(GROUP_STYLE_1);
                getStyleClass().removeAll(GROUP_STYLE_2);
                if (clubInfoData == null || empty) {
                    setStyle("");

                } else {
                    if (clubInfoData.isGroup1()) {
                        getStyleClass().add(GROUP_STYLE_1);

                    } else if (clubInfoData.isGroup2()) {
                        getStyleClass().add(GROUP_STYLE_2);
                    }
                }
            }
        });
    }

    private List<TreeTableColumn<ClubInfoData, ?>> createColumns() {
        final TreeTableColumn<ClubInfoData, String> countColunm = createColumn("Anzahl", "amount", 150);
        registerRenderer(countColunm);
        return Arrays.asList(
                createColumn("Name", "name", 300),
                countColunm,
                createColumn("Beschreibung", "text", 1000)
        );
    }

    private void registerRenderer(TreeTableColumn<ClubInfoData, String> countColumn) {
        countColumn.setCellFactory(new Callback<>() {
            public TreeTableCell<ClubInfoData, String> call(TreeTableColumn<ClubInfoData, String> P) {
                return new TreeTableCell<>() {
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setStyle("-fx-alignment: CENTER_RIGHT;");
                        setPadding(new Insets(0, 10, 0, 10));
                        setText(item);
                    }
                };
            }
        });
    }

    private <V> TreeTableColumn<ClubInfoData, V> createColumn(String title, String attrName, int width) {
        TreeTableColumn<ClubInfoData, V> column = new TreeTableColumn<>(title);
        column.setPrefWidth(width);
        column.setCellValueFactory(new TreeItemPropertyValueFactory<>(attrName));
        return column;
    }

}
