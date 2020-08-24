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

package de.p2tools.clubOrga.data.deleteData;


import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DeleteAllDialogController extends PDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnCancel = new Button("Abbrechen");
    private Button btnHelp;

    private boolean ok = false;
    private final ClubConfig clubConfig;

    public DeleteAllDialogController(ClubConfig clubConfig) {
        super(clubConfig.getStage(), clubConfig.DELETE_ALL_DATA_DIALOG_SIZE,
                "Vereinsdaten löschen", true, true);
        this.clubConfig = clubConfig;

        init(true);
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    protected void make() {
        btnHelp = PButton.helpButton(getStage(), "Alle Daten löschen",
                "Es werden alle Vereinsdaten gelöscht: Mitglieder, Beiträge und Finanzen. Das Löschen " +
                        "kann nicht rückgänig gemacht werden!");

//        HBox hBoxHelp = new HBox();
//        hBoxHelp.setAlignment(Pos.CENTER_LEFT);
//        hBoxHelp.getChildren().addAll(btnHelp);
//
//        HBox hBox = new HBox();
//        HBox.setHgrow(hBox, Priority.ALWAYS);
//        getHboxOk().getChildren().addAll(btnHelp, hBox, btnOk, btnCancel);

        addOkCancelButtons(btnOk, btnCancel);
        ButtonBar.setButtonData(btnHelp, ButtonBar.ButtonData.HELP);
        addAnyButton(btnHelp);

        btnOk.setOnAction(a -> {
            if (check()) {
                ok = true;
                close();
            }
        });
        btnCancel.setOnAction(a -> close());

        getHBoxTitle().getChildren().add(new Label("Alle Vereinsdaten löschen"));
//        HBox hBoxTitle = GuiFactory.getDialogTitle("Alle Vereinsdaten löschen");
        final int member = clubConfig.memberDataList.size();
        final int fee = clubConfig.feeDataList.size();
        final int finances = clubConfig.financeDataList.size();


        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        int row = 0;
        gridPane.add(new Label("Mitglieder:"), 0, ++row);
        gridPane.add(new Label(member + ""), 1, row);

        gridPane.add(new Label("Beiträge:"), 0, ++row);
        gridPane.add(new Label(fee + ""), 1, row);

        gridPane.add(new Label("Finanzen:"), 0, ++row);
        gridPane.add(new Label(finances + ""), 1, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        VBox.setVgrow(gridPane, Priority.ALWAYS);
        getvBoxCont().getChildren().addAll(/*hBoxTitle,*/ gridPane);
    }

    private boolean check() {
        boolean ret = false;

        final int member = clubConfig.memberDataList.size();
        final int fee = clubConfig.feeDataList.size();
        final int finances = clubConfig.financeDataList.size();

        if (member == 0 && fee == 0 && finances == 0) {
            ret = true;

        } else {
            PAlert.BUTTON button = PAlert.showAlert_yes_no(getStage(), "Löschen",
                    "Vereinsdaten löschen",
                    "Sollen wirklich alle bestehenden Vereinsdaten gelöscht werden?" +
                            "\n\n" +
                            "Bestehende Vereinsdaten:\n" +
                            member + " Mitglieder\n" +
                            fee + " Beiträge\n" +
                            finances + " Finanzen");
            if (button == PAlert.BUTTON.YES) {
                ret = true;
            }
        }

        return ret;
    }

}
