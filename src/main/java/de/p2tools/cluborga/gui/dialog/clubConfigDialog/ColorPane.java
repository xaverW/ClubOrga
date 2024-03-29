/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.cluborga.config.prog.ProgConfig;
import de.p2tools.cluborga.config.prog.ProgConst;
import de.p2tools.cluborga.gui.tools.HelpText;
import de.p2tools.p2lib.data.PColorData;
import de.p2tools.p2lib.data.PColorList;
import de.p2tools.p2lib.guitools.PButton;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.PToggleSwitch;
import de.p2tools.p2lib.tools.PColorFactory;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Collection;

public class ColorPane {
    private final Stage stage;
    BooleanProperty propDarkTheme = ProgConfig.SYSTEM_DARK_THEME;
    private final PToggleSwitch tglDarkTheme = new PToggleSwitch("Dunkles Erscheinungsbild der Programmoberfläche");

    public ColorPane(Stage stage) {
        this.stage = stage;
    }

    public void makeColor(Collection<TitledPane> result) {
        final VBox vBox = new VBox();
        vBox.setFillWidth(true);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(0, 0, 10, 0));

        tglDarkTheme.selectedProperty().bindBidirectional(propDarkTheme);
        final Button btnHelpTheme = PButton.helpButton(stage, "Erscheinungsbild der Programmoberfläche",
                HelpText.DARK_THEME);

        gridPane.add(tglDarkTheme, 0, 0);
        gridPane.add(btnHelpTheme, 1, 0);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize());

        TableView<PColorData> tableView = new TableView<>();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        initTableColor(tableView);
        tglDarkTheme.selectedProperty().addListener((u, o, n) -> {
            tableView.refresh();
        });

        Button button = new Button("Alle _Farben zurücksetzen");
        button.setOnAction(event -> {
            PColorList.resetAllColor();
        });
        HBox hBox = new HBox();
        hBox.getChildren().add(button);
        hBox.setPadding(new Insets(0));
        hBox.setAlignment(Pos.CENTER_RIGHT);

        vBox.getChildren().addAll(gridPane, tableView, hBox);

        TitledPane tpColor = new TitledPane("Farben", vBox);
        result.add(tpColor);
    }

    public void close() {
        tglDarkTheme.selectedProperty().unbindBidirectional(propDarkTheme);
    }

    private void initTableColor(TableView<PColorData> tableView) {

        final TableColumn<PColorData, String> textColumn = new TableColumn<>("Beschreibung");
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        textColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<PColorData, String> changeColumn = new TableColumn<>("Farbe");
        changeColumn.setCellFactory(cellFactoryChange);
        changeColumn.getStyleClass().add("alignCenter");

        final TableColumn<PColorData, Color> colorColumn = new TableColumn<>("Farbe");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(cellFactoryColor);
        colorColumn.getStyleClass().add("alignCenter");

        final TableColumn<PColorData, Color> colorOrgColumn = new TableColumn<>("Original");
        colorOrgColumn.setCellValueFactory(new PropertyValueFactory<>("resetColor"));
        colorOrgColumn.setCellFactory(cellFactoryColorReset);
        colorOrgColumn.getStyleClass().add("alignCenter");

        final TableColumn<PColorData, String> resetColumn = new TableColumn<>("Reset");
        resetColumn.setCellFactory(cellFactoryReset);
        resetColumn.getStyleClass().add("alignCenter");

        tableView.setMinHeight(ProgConst.MIN_TABLE_HEIGHT);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        tableView.getColumns().addAll(textColumn, changeColumn, colorColumn, colorOrgColumn, resetColumn);
        tableView.setItems(PColorList.getInst());
    }

    private Callback<TableColumn<PColorData, String>, TableCell<PColorData, String>> cellFactoryChange
            = (final TableColumn<PColorData, String> param) -> {

        final TableCell<PColorData, String> cell = new TableCell<>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                PColorData pColorData = getTableView().getItems().get(getIndex());

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                final ColorPicker colorPicker = new ColorPicker();
                colorPicker.getStyleClass().add("split-button");

                colorPicker.setValue(pColorData.getColor());
                colorPicker.setOnAction(a -> {
                    Color fxColor = colorPicker.getValue();
                    pColorData.setColor(fxColor);
                });
                hbox.getChildren().addAll(colorPicker);
                setGraphic(hbox);
            }
        };

        return cell;
    };

    private Callback<TableColumn<PColorData, String>, TableCell<PColorData, String>> cellFactoryReset
            = (final TableColumn<PColorData, String> param) -> {

        final TableCell<PColorData, String> cell = new TableCell<>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                PColorData pColorData = getTableView().getItems().get(getIndex());

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                final Button button = new Button("Reset");
                button.setOnAction(a -> {
                    pColorData.resetColor();
                });

                hbox.getChildren().add(button);
                setGraphic(hbox);
            }
        };

        return cell;
    };

    private Callback<TableColumn<PColorData, Color>, TableCell<PColorData, Color>> cellFactoryColor
            = (final TableColumn<PColorData, Color> param) -> {

        final TableCell<PColorData, Color> cell = new TableCell<PColorData, Color>() {


            @Override
            public void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                PColorData pColorData = getTableView().getItems().get(getIndex());
                setStyle("-fx-background-color:" + pColorData.getColorSelectedToWeb());
            }

        };

        return cell;
    };

    private Callback<TableColumn<PColorData, Color>, TableCell<PColorData, Color>> cellFactoryColorReset
            = (final TableColumn<PColorData, Color> param) -> {

        final TableCell<PColorData, Color> cell = new TableCell<PColorData, Color>() {


            @Override
            public void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                PColorData pColorData = getTableView().getItems().get(getIndex());
                setStyle("-fx-background-color:" + PColorFactory.getColorToWeb(pColorData.getResetColor()));
            }

        };

        return cell;
    };

}
