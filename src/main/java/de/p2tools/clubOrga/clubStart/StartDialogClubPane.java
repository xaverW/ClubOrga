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

import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.p2Lib.guiTools.BigButton;
import de.p2tools.p2Lib.guiTools.PButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StartDialogClubPane {

    private final Stage stage;
    private final StartDialogController startDialogController;

    public StartDialogClubPane(Stage stage, StartDialogController startDialogController) {
        this.stage = stage;
        this.startDialogController = startDialogController;
    }

    public TitledPane makePane() {
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(25, 20, 20, 20));

        initPane(vBox);

        final Button btnHelp = PButton.helpButton(stage,
                "Verein starten",
                "In diesem Dialog kann ein Verein gestartet werden.\n\n" +
                        "\"Verein neu anlegen\" macht genau das, es wird ein neuer Verein angelegt. " +
                        "Im Anschluss kann man dann Speicherort und Vereinsname angegeben " +
                        "und der Verein wird gestartet.\n\n" +

                        "Ein erstellter Verein kann in eine ZIP-Datei gesichert werden. " +
                        "Es werden damit alle Infos zum Verein gesichert. Mit \"Verein aus Datei importieren\" " +
                        "kann dann ein so gesichertet Verein wieder gestartet werden.\n\n" +

                        "Hat man das Programm früher schon mal benutzt und nur die Programmeinstellungen gelöscht, " +
                        "die Vereinsdaten selbst liegen aber noch auf dem Rechner, dann kann mit \"Verein aus Verzeichnis " +
                        "importeren\" der Verein dieser vorhandenen Vereinsdaten gestartet werden.");

        HBox hBox = new HBox();
        VBox.setVgrow(hBox, Priority.ALWAYS);

        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(btnHelp);
        vBox.getChildren().add(hBox);

        TitledPane tpConfig = new TitledPane("Verein starten", vBox);
        return tpConfig;
    }


    private void initPane(VBox vBox) {

        BigButton btnNewClub = new BigButton(new ProgIcons().ICON_BUTTON_QUIT,
                "Verein neu anlegen", "einen neuen Verein erstellen und starten");
        btnNewClub.setOnAction(e -> startDialogController.addNewClub());

        BigButton btnImportZipClub = new BigButton(new ProgIcons().ICON_BUTTON_QUIT,
                "Verein aus Datei importieren", "einen in eine ZIP-Datei exportierten Verein (z.B. Verein.zip) starten");
        btnImportZipClub.setOnAction(e -> startDialogController.importZipClub());

        BigButton btnImpportDirClub = new BigButton(new ProgIcons().ICON_BUTTON_QUIT,
                "Verein aus Verzeichnis importieren", "einen bereits erstellten Verein aus einem Verzeichnis starten");
        btnImpportDirClub.setOnAction(e -> startDialogController.importDirClub());

        int row = 0;
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setVgap(20);

        gridPane.add(btnNewClub, 0, row, 2, 1);
        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(btnImportZipClub, 1, ++row);
        gridPane.add(btnImpportDirClub, 1, ++row);


        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(10);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(90);
        column2.setFillWidth(true);
        column2.setMinWidth(Region.USE_COMPUTED_SIZE);
        column2.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(column1, column2);

        vBox.getChildren().add(gridPane);
    }


}
