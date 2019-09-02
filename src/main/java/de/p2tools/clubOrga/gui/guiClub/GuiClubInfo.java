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

package de.p2tools.clubOrga.gui.guiClub;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.clubData.ClubInfoData;
import de.p2tools.clubOrga.data.clubData.InfoFactory;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.List;

public class GuiClubInfo extends AnchorPane {

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private final TreeTableView<ClubInfoData> treeTableView = new TreeTableView<>();

    public GuiClubInfo(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;

        initCont();
        initTable();
    }

    public void isShown() {

        initTable();
//        treeTableView.refresh();

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
        this.getChildren().add(scrollPane);
    }

    private void initTable() {
        treeTableView.getColumns().clear();

        treeTableView.setRoot(InfoFactory.generate(clubConfig));
        treeTableView.getColumns().addAll(createColumns());
        addRowFact(treeTableView);
    }

    final String GROUP_STYLE_1 = "goupStyle1";
    final String GROUP_STYLE_2 = "goupStyle2";

    private void addRowFact(TreeTableView<ClubInfoData> table) {

        table.setRowFactory(tableview -> new TreeTableRow<ClubInfoData>() {
            @Override
            public void updateItem(ClubInfoData clubInfoData, boolean empty) {
                super.updateItem(clubInfoData, empty);

                getStyleClass().removeAll(GROUP_STYLE_1);
                getStyleClass().removeAll(GROUP_STYLE_2);
                if (clubInfoData == null || empty) {
                    setStyle("");
                } else {
//                    if (clubInfoData.isGroup()) {
//                        for (int i = 0; i < getChildren().size(); i++) {
//                            getChildren().get(i).setStyle(cssFontBold);
//                        }
//
//                    } else {
//                        for (int i = 0; i < getChildren().size(); i++) {
//                            getChildren().get(i).setStyle("");
//                        }
//                    }

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
                return new TreeTableCell<ClubInfoData, String>() {
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setStyle("-fx-alignment: CENTER_RIGHT;");
                        setPadding(new Insets(0, 10, 0, 10));
                        setText(item);
//                        if (!empty) {
//                            if (item.intValue() == -1) {
//                                setText(null);
//                            } else {
//                                setText(item.toString());
//                            }
//
//                        } else {
//                            setText(null);
//                        }
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
