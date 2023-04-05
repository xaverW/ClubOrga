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

package de.p2tools.cluborga.clubStart;

import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.config.prog.ProgIcons;
import de.p2tools.cluborga.controller.ClubStartFactory;
import de.p2tools.cluborga.data.knownClubData.KnownClubData;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.guitools.PButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;


public class StartDialogController extends PDialogExtra {

    private TilePane tilePane = new TilePane();
    private StackPane stackpane;
    private Button btnCancel;
    private Button btnPrev, btnNext;
    private Button btnInfo1 = new Button(STR_INFO_1), btnInfo2 = new Button(STR_INFO_2),
            btnClub = new Button(STR_CLUB);

    private static final String STR_INFO_1 = "Infos";
    private static final String STR_INFO_2 = "Infos";
    private static final String STR_CLUB = "Verein anlegen";

    private enum State {INFO_1, INFO_2, CLUB;}

    private State aktState = State.INFO_1;

    private TitledPane tStart1, tStart2, tClub;
    private StartDialogInfoPane startPane1;
    private StartDialogInfoPane startPane2;
    private StartDialogClubPane clubPane;

    private boolean ok = false;
    private final ProgData progData;
    private final KnownClubData knownClubData;

    public StartDialogController(KnownClubData knownClubData) {
        super(null, null, "Starteinstellungen", true, false);

        this.progData = ProgData.getInstance();
        this.knownClubData = knownClubData;

        init(true);
    }

    @Override
    public void make() {
        initTopButton();
        initStack();
        initButton();
        initTooltip();
        selectActPane();
    }

    private void closeDialog(boolean ok) {
        this.ok = ok;
        startPane1.close();
        startPane2.close();
        clubPane.close();
        super.close();
    }

    public boolean isOk() {
        return ok;
    }

    private void initTopButton() {
        getVBoxCont().getChildren().add(tilePane);
        tilePane.getChildren().addAll(btnInfo1, btnInfo2, btnClub);
        tilePane.setAlignment(Pos.CENTER);
        tilePane.setPadding(new Insets(10, 10, 20, 10));
        tilePane.setHgap(10);
        tilePane.setVgap(10);

        setButton(btnInfo1, State.INFO_1);
        setButton(btnInfo2, State.INFO_2);
        setButton(btnClub, State.CLUB);
    }

    private void setButton(Button btn, State state) {
        btn.getStyleClass().add("btnStartDialog");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(a -> {
            aktState = state;
            selectActPane();
        });
    }

    private void initStack() {
        stackpane = new StackPane();
        VBox.setVgrow(stackpane, Priority.ALWAYS);
        getVBoxCont().getChildren().add(stackpane);

        // startPane 1
        startPane1 = new StartDialogInfoPane(getStage());
        tStart1 = startPane1.makeStart1();
        tStart1.setMaxHeight(Double.MAX_VALUE);
        tStart1.setCollapsible(false);

        // startPane 2
        startPane2 = new StartDialogInfoPane(getStage());
        tStart2 = startPane2.makeStart2();
        tStart2.setMaxHeight(Double.MAX_VALUE);
        tStart2.setCollapsible(false);

        // clubPane
        clubPane = new StartDialogClubPane(getStage(), this);
        tClub = clubPane.makePane();
        tClub.setMaxHeight(Double.MAX_VALUE);
        tClub.setCollapsible(false);

        stackpane.getChildren().addAll(tStart1, tStart2, tClub);
    }

    private void initButton() {
        btnCancel = new Button("_Abbrechen");
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

        btnCancel.getStyleClass().setAll("btnStartDialog");
        btnNext.getStyleClass().setAll("btnStartDialog");
        btnPrev.getStyleClass().setAll("btnStartDialog");

        addCancelButton(btnCancel);
        ButtonBar.setButtonData(btnPrev, ButtonBar.ButtonData.BACK_PREVIOUS);
        ButtonBar.setButtonData(btnNext, ButtonBar.ButtonData.NEXT_FORWARD);
        addAnyButton(btnNext);
        addAnyButton(btnPrev);
        getButtonBar().setButtonOrder("BX+CO");
    }

    private void selectActPane() {
        switch (aktState) {
            case INFO_1:
                btnPrev.setDisable(true);
                btnNext.setDisable(false);
                tStart1.toFront();
                setButtonStyle(btnInfo1);
                break;
            case INFO_2:
                btnPrev.setDisable(false);
                btnNext.setDisable(false);
                tStart2.toFront();
                setButtonStyle(btnInfo2);
                break;
            case CLUB:
                btnPrev.setDisable(false);
                btnNext.setDisable(true);
                tClub.toFront();
                setButtonStyle(btnClub);
                break;
        }
    }

    private void setButtonStyle(Button btnSel) {
        btnInfo1.getStyleClass().retainAll("btnStartDialog");
        btnInfo2.getStyleClass().retainAll("btnStartDialog");
        btnClub.getStyleClass().retainAll("btnStartDialog");
        btnSel.getStyleClass().add("btnStartDialogSel");
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
