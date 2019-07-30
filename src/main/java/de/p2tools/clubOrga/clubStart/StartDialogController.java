/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.clubOrga.clubStart;

import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.controller.ClubStartFactory;
import de.p2tools.clubOrga.data.knownClubData.KnownClubData;
import de.p2tools.p2Lib.dialog.PDialog;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class StartDialogController extends PDialog {

    private AnchorPane rootPane = new AnchorPane();
    private VBox vBoxDialog = new VBox();
    private VBox vBoxCont = new VBox();

    private TilePane tilePane = new TilePane();
    private StackPane stackpane;
    private Button btnCancel;
    private Button btnPrev, btnNext;
    private Button btnInfo1 = new Button(STR_INFO_1), btnInfo2 = new Button(STR_INFO_2),
            btnClub = new Button(STR_CLUB);

    private static final String STR_INFO_1 = "Infos";
    private static final String STR_INFO_2 = "Infos";
    private static final String STR_CLUB = "Verein anlegen";


    private enum State {INFO_1, INFO_2, CLUB}

    private ScrollPane infoPane_1;
    private ScrollPane infoPane_2;
    private ScrollPane clubPane;

    private State aktState = State.INFO_1;
    private boolean ok = false;

    private final ProgData progData;
    private final KnownClubData knownClubData;

    public StartDialogController(Stage stage, KnownClubData knownClubData) {
        super(stage, null, "Starteinstellungen", true);

        this.progData = ProgData.getInstance();
        this.knownClubData = knownClubData;

        init(rootPane, true);
    }

    @Override
    public void make() {
        initPanel();
        addButton();
        initStack();
        initButton();
        selectActPane();
        initTooltip();
    }

    private void closeDialog(boolean ok) {
        this.ok = ok;
        super.close();
    }

    public boolean isOk() {
        return ok;
    }

    private void initPanel() {
        try {
            vBoxDialog.setPadding(new Insets(20));
            vBoxDialog.setSpacing(20);

            vBoxCont.getStyleClass().add("dialog-border");
            vBoxCont.setSpacing(10);
            VBox.setVgrow(vBoxCont, Priority.ALWAYS);

            rootPane.getChildren().addAll(vBoxDialog);
            AnchorPane.setLeftAnchor(vBoxDialog, 0.0);
            AnchorPane.setBottomAnchor(vBoxDialog, 0.0);
            AnchorPane.setRightAnchor(vBoxDialog, 0.0);
            AnchorPane.setTopAnchor(vBoxDialog, 0.0);

            vBoxDialog.getChildren().add(vBoxCont);

        } catch (final Exception ex) {
            PLog.errorLog(912031210, ex);
        }
    }

    private void addButton() {
        vBoxCont.getChildren().add(tilePane);
        tilePane.getChildren().addAll(btnInfo1, btnInfo2, btnClub);
        tilePane.setAlignment(Pos.CENTER);
        tilePane.setPadding(new Insets(10));
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        setButton(btnInfo1, State.INFO_1);
        setButton(btnInfo2, State.INFO_2);
        setButton(btnClub, State.CLUB);
        btnInfo1.getStyleClass().add("btnStartDialogSel");
    }

    private void setButton(Button btn, State state) {
        btn.getStyleClass().add("btnStartDialog");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(a -> {
            aktState = state;
            btnInfo1.getStyleClass().retainAll("btnStartDialog");
            btnInfo2.getStyleClass().retainAll("btnStartDialog");
            btnClub.getStyleClass().retainAll("btnStartDialog");
            btn.getStyleClass().add("btnStartDialogSel");
            setButtonStyle(btnInfo1);
            selectActPane();
        });
    }

    private void setButtonStyle(Button btnSel) {
        btnInfo1.getStyleClass().retainAll("btnStartDialog");
        btnInfo2.getStyleClass().retainAll("btnStartDialog");
        btnClub.getStyleClass().retainAll("btnStartDialog");
        btnSel.getStyleClass().add("btnStartDialogSel");
    }

    private void initStack() {
        stackpane = new StackPane();
        VBox.setVgrow(stackpane, Priority.ALWAYS);
        vBoxCont.getChildren().add(stackpane);

        // startPane 1
        infoPane_1 = new ScrollPane();
        infoPane_1.setFitToHeight(true);
        infoPane_1.setFitToWidth(true);

        TitledPane tStart1 = new StartDialogInfoPane(getStage()).makeStart1();
        tStart1.setMaxHeight(Double.MAX_VALUE);
        tStart1.setCollapsible(false);
        infoPane_1.setContent(tStart1);

        // startPane 2
        infoPane_2 = new ScrollPane();
        infoPane_2.setFitToHeight(true);
        infoPane_2.setFitToWidth(true);

        TitledPane tStart2 = new StartDialogInfoPane(getStage()).makeStart2();
        tStart2.setMaxHeight(Double.MAX_VALUE);
        tStart2.setCollapsible(false);
        infoPane_2.setContent(tStart2);

        // clubPane
        clubPane = new ScrollPane();
        clubPane.setFitToHeight(true);
        clubPane.setFitToWidth(true);

        TitledPane tClub = new StartDialogClubPane(getStage(), this).makePane();
        tClub.setMaxHeight(Double.MAX_VALUE);
        tClub.setCollapsible(false);
        clubPane.setContent(tClub);


        stackpane.getChildren().addAll(infoPane_1, infoPane_2, clubPane);
    }

    private void initButton() {
        btnCancel = new Button("Abbrechen");
        btnCancel.setOnAction(a -> closeDialog(false));

        btnNext = PButton.getButton(new ProgIcons().ICON_BUTTON_NEXT, "nächste Seite");
        btnNext.setOnAction(event -> {
            switch (aktState) {
                case INFO_1:
                    aktState = State.INFO_2;
                    break;
                case INFO_2:
                    aktState = State.CLUB;
                    break;
                case CLUB:
                    break;
            }
            selectActPane();
        });
        btnPrev = PButton.getButton(new ProgIcons().ICON_BUTTON_PREV, "vorherige Seite");
        btnPrev.setGraphic(new ProgIcons().ICON_BUTTON_PREV);
        btnPrev.setOnAction(event -> {
            switch (aktState) {
                case INFO_1:
                    break;
                case INFO_2:
                    aktState = State.INFO_1;
                    break;
                case CLUB:
                    aktState = State.INFO_2;
                    break;
            }
            selectActPane();
        });

        btnCancel.getStyleClass().add("btnStartDialog");
        btnNext.getStyleClass().add("btnStartDialog");
        btnPrev.getStyleClass().add("btnStartDialog");

        HBox hBox1 = new HBox();
        hBox1.setSpacing(10);
        hBox1.getChildren().addAll(btnPrev, btnNext);
        HBox.setHgrow(hBox1, Priority.ALWAYS);

        HBox hBox2 = new HBox();
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(hBox1, btnCancel);

        vBoxDialog.getChildren().add(hBox2);
    }

    private void selectActPane() {
        switch (aktState) {
            case INFO_1:
                btnPrev.setDisable(true);
                btnNext.setDisable(false);
                infoPane_1.toFront();
                setButtonStyle(btnInfo1);
                break;
            case INFO_2:
                btnPrev.setDisable(false);
                btnNext.setDisable(false);
                infoPane_2.toFront();
                setButtonStyle(btnInfo2);
                break;
            case CLUB:
                btnPrev.setDisable(false);
                btnNext.setDisable(true);
                clubPane.toFront();
                setButtonStyle(btnClub);
                break;
        }
    }

    private void initTooltip() {
        btnInfo1.setTooltip(new Tooltip("Infos über das Programm"));
        btnInfo2.setTooltip(new Tooltip("Infos über das Programm"));
        btnClub.setTooltip(new Tooltip("Vereinsname und Speicherort"));

        btnCancel.setTooltip(new Tooltip("Das Programm nicht einrichten\n" +
                "und starten sondern Dialog wieder beenden"));
        btnNext.setTooltip(new Tooltip("Nächste Einstellmöglichkeit"));
        btnPrev.setTooltip(new Tooltip("Vorherige Einstellmöglichkeit"));
    }

    void addNewClub() {
        AddNewClubDialogController addNewClubDialogController = new AddNewClubDialogController(getStage(), progData, knownClubData);

        if (addNewClubDialogController.isOk()) {
//            Platform.runLater(() -> {
//                ClubStartFactory.startNewClub(knownClubData);
//                closeDialog(true);
//            });

            ClubStartFactory.startNewClub(knownClubData);
            closeDialog(true);
        }
    }

    void importDirClub() {
        ImportDirClubDialogController importDirClubDialogController = new ImportDirClubDialogController(getStage());
        if (importDirClubDialogController.isOk()) {
            closeDialog(true);
        }
    }

    void importZipClub() {
        ImportZipClubDialogController importZipClubDialogController = new ImportZipClubDialogController(getStage());
        if (importZipClubDialogController.isOk()) {
            closeDialog(true);
        }
    }
}
