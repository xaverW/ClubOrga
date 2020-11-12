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

package de.p2tools.clubOrga;

import de.p2tools.clubOrga.clubStart.AddNewClubDialogController;
import de.p2tools.clubOrga.clubStart.ImportDirClubDialogController;
import de.p2tools.clubOrga.clubStart.ImportZipClubDialogController;
import de.p2tools.clubOrga.config.club.ProgColorList;
import de.p2tools.clubOrga.config.prog.ProgConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.controller.ClubStartFactory;
import de.p2tools.clubOrga.controller.ProgQuitFactory;
import de.p2tools.clubOrga.data.knownClubData.KnownClubData;
import de.p2tools.clubOrga.data.knownClubData.KnownClubDataFactory;
import de.p2tools.clubOrga.gui.tools.HelpText;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ClubSelector extends PDialogExtra {

    private final ProgData progData;
    private Stage stage = null;
    private final VBox vBox = new VBox(10);
    private final TableView<KnownClubData> tableView = new TableView<>();

    public ClubSelector(Stage stage) {
        super(stage, ProgConfig.CLUB_SELECTOR_GUI_SIZE, "Verein auswählen",
                false, false, DECO.SMALL);
        this.progData = ProgData.getInstance();
        init(true);
    }

    @Override
    protected void make() {
        this.stage = getStage();
        ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> updateCss());

        getHBoxTitle().getChildren().add(new Label("Vereine verwalten"));
        initTable();
        getGenClubButton();
        addOk();
    }

    @Override
    public void close() {
        ProgQuitFactory.quitClubSelector(progData);
        super.close();
    }

    private void getGenClubButton() {
        final Button btnNew = new Button("neuen Verein anlegen");
        btnNew.setMaxWidth(Double.MAX_VALUE);
        btnNew.setTooltip(new Tooltip("Ein neuer Verein wird angelegt."));
        btnNew.setOnAction(a -> addNewClub());

        Button btnImportZip = new Button("Zip-Datei importieren");
        btnImportZip.setMaxWidth(Double.MAX_VALUE);
        btnImportZip.setTooltip(new Tooltip("Einen bereits angelegten Verein aus einer ZIP-Datei " +
                "importieren und starten."));
        btnImportZip.setOnAction(a -> importZipClub());

        Button btnImportDir = new Button("Verzeichnis importieren");
        btnImportDir.setTooltip(new Tooltip("Einen bereits angelegten Verein aus einem Verzeichnis " +
                "importieren und starten."));
        btnImportDir.setOnAction(a -> importDirClub());

        TilePane tilePane = new TilePane();
        tilePane.getChildren().addAll(btnNew, btnImportZip, btnImportDir);
        tilePane.setAlignment(Pos.CENTER_LEFT);
        tilePane.setHgap(10);
        tilePane.setVgap(5);
        getvBoxCont().getChildren().addAll(tilePane);
    }

    private void addOk() {
        Button btnOk = new Button("Ok");
        btnOk.setMaxWidth(Double.MAX_VALUE);
        btnOk.setMinWidth(Region.USE_PREF_SIZE);
        btnOk.setTooltip(new Tooltip("Dialog wieder schließen"));
        btnOk.setOnAction(a -> close()); // wenn letztes Fenster, dann schließt das Programm nicht richtig

        Button btnHelp = PButton.helpButton(stage, "Vereine verwalten", HelpText.HELP_CLUB_SELECTOR);

        CheckBox chkStartFirst = new CheckBox("Dialog immer zuerst starten");
        chkStartFirst.selectedProperty().bindBidirectional(ProgConfig.START_CLUB_SELECTOR_FIRST);

        getHBoxOverButtons().setPadding(new Insets(0, 0, 10, 0));
        getHBoxOverButtons().getChildren().addAll(chkStartFirst);
        ButtonBar.setButtonData(btnHelp, ButtonBar.ButtonData.HELP);
        addOkButton(btnOk);
        addAnyButton(btnHelp);
    }

    private void initTable() {
        getvBoxCont().getChildren().addAll(tableView);
        tableView.setMaxHeight(Double.MAX_VALUE);
        tableView.setTableMenuButtonVisible(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setItems(progData.knownClubDataList);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        final TableColumn<KnownClubData, Boolean> selectedColumn = new TableColumn<>("Autostart");
        selectedColumn.setCellValueFactory(new PropertyValueFactory<>("autostart"));
        selectedColumn.setCellFactory(callbackActive);

        final TableColumn<KnownClubData, String> nameColumn = new TableColumn<>("Vereinsame");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("clubname"));

        final TableColumn<KnownClubData, String> pathColumn = new TableColumn<>("Pfad");
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("clubpath"));

        final TableColumn<KnownClubData, String> startColumn = new TableColumn<>("");
        startColumn.setCellFactory(cellFactoryStart);
        startColumn.getStyleClass().add("alignCenter");

        tableView.getColumns().addAll(nameColumn, pathColumn, startColumn, selectedColumn);

        tableView.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                KnownClubData knownClubData = tableView.getSelectionModel().getSelectedItem();
                startClub(knownClubData);
            }
        });

        if (!progData.knownClubDataList.isEmpty()) {
            tableView.getSelectionModel().selectFirst();
        }
    }

    private Callback<TableColumn<KnownClubData, Boolean>, TableCell<KnownClubData, Boolean>> callbackActive =
            (final TableColumn<KnownClubData, Boolean> param) -> {

                final CheckBoxTableCell<KnownClubData, Boolean> cell = new CheckBoxTableCell<KnownClubData, Boolean>() {

                    @Override
                    public void updateItem(Boolean item, boolean empty) {

                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                            setText(null);
                            return;
                        }

                        TableRow<KnownClubData> currentRow = getTableRow();
                        if (currentRow != null) {

                            if (item.booleanValue()) {
                                // ist ein Autostart-Club
                                String style = ProgColorList.CLUB_AUTOSTART.getCssBackgroundSel();
                                currentRow.setStyle(style);
                            } else {
                                currentRow.setStyle("");
                            }

                        }
                    }
                };
                return cell;
            };

    private Callback<TableColumn<KnownClubData, String>, TableCell<KnownClubData, String>> cellFactoryStart
            = (final TableColumn<KnownClubData, String> param) -> {

        final TableCell<KnownClubData, String> cell = new TableCell<>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                KnownClubData knownClubData = getTableView().getItems().get(getIndex());

                final HBox hbox = new HBox();
                hbox.setSpacing(4);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                final Button btnStart;
                final Button btnDel;

                btnDel = new Button("");
                btnDel.setTooltip(new Tooltip("Verein aus der Liste löschen"));
                btnDel.setGraphic(new ProgIcons().ICON_BUTTON_TABLE_DEL_CLUB);
                btnDel.setOnAction((ActionEvent event) -> removeClub(knownClubData));

                btnStart = new Button("");
                btnStart.setTooltip(new Tooltip("Verein starten"));
                btnStart.setGraphic(new ProgIcons().ICON_BUTTON_TABLE_START_CLUB);
                btnStart.setOnAction((ActionEvent event) -> startClub(knownClubData));

                hbox.getChildren().addAll(btnDel, btnStart);
                setGraphic(hbox);
            }
        };
        return cell;
    };

    private void startClub(KnownClubData knownClubData) {
        if (knownClubData == null) {
            return;
        }
        Platform.runLater(() -> ClubStartFactory.startClub(null, knownClubData));
    }

    private void removeClub(KnownClubData knownClubData) {
        if (knownClubData != null) {
            progData.knownClubDataList.remove(knownClubData);
        }
    }

    private void addNewClub() {
        KnownClubData knownClubData = KnownClubDataFactory.getNextNewKnownClubData();

        if (knownClubData == null) {
            PAlert.showErrorAlert(stage, "Neuen Verein anlegen",
                    "Das Programm kann keinen Verein anlegen. Es findet keinen Speicherplatz dafür.");
            return;
        }

        AddNewClubDialogController addNewClubDialogController = new AddNewClubDialogController(stage, progData, knownClubData);
        if (addNewClubDialogController.isOk()) {
            tableView.getSelectionModel().select(knownClubData);
            Platform.runLater(() -> ClubStartFactory.startNewClub(knownClubData));
            close();
        }
    }

    private void importZipClub() {
        ImportZipClubDialogController imz = new ImportZipClubDialogController(stage);
    }

    private void importDirClub() {
        ImportDirClubDialogController imd = new ImportDirClubDialogController(stage);
    }

}
