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

import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.p2Lib.PConst;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StartDialogInfoPane {
    private final Stage stage;

    public StartDialogInfoPane(Stage stage) {
        this.stage = stage;
    }

    public TitledPane makeStart1() {

        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(10));

        ImageView iv = new ImageView();
        Image im = getHelpScreen1();
        iv.setSmooth(true);
        iv.setImage(im);

        hBox.getChildren().addAll(iv);
        Label text = new Label("1) Die unterschiedlichen Ansichten können" + PConst.LINE_SEPARATOR +
                "hier ausgewählt werden: Mitglieder," + PConst.LINE_SEPARATOR +
                "Beiträge und Finanzen" +
                PConst.LINE_SEPARATORx2 +
                "2) Die Daten sind dann hier gelistet" + PConst.LINE_SEPARATOR +
                "(aktuell sind es die Mitgliederdaten)." +
                PConst.LINE_SEPARATORx2 +
                "3) Unter der Tabelle können die angezeigten" + PConst.LINE_SEPARATOR +
                "Daten gefiltert oder verändert werden." +
                PConst.LINE_SEPARATORx2 +
                "4) Damit können Zeilen in der Tabelle markiert" + PConst.LINE_SEPARATOR +
                "gefiltert, und der Filter auch wieder" + PConst.LINE_SEPARATOR +
                "gelöscht werden." +
                PConst.LINE_SEPARATORx2 +
                "5) Rechts neben der Tabelle kann die" + PConst.LINE_SEPARATOR +
                "ausgewählte Tabellenzeile bearbeitet oder" + PConst.LINE_SEPARATOR +
                "auch neue Einträge angelegt werden."
        );
        text.setPadding(new Insets(20));
        hBox.getChildren().add(text);

        TitledPane tpConfig = new TitledPane("Infos zur Programmoberfläche, Mitglieder (mit Demodaten)", hBox);
        return tpConfig;
    }

    public TitledPane makeStart2() {

        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(10));

        ImageView iv = new ImageView();
        Image im = getHelpScreen2();
        iv.setSmooth(true);
//        iv.setCache(true);
        iv.setImage(im);

        hBox.getChildren().addAll(iv);

        Label text = new Label("1) In der Tabelle sind die Finanzen gelistet." +
                PConst.LINE_SEPARATORx2 +
                "2) Hier kann die Ansicht \"Filter\"," + PConst.LINE_SEPARATOR +
                "\"Infos zum markierten Finanzeintrag\" und" + PConst.LINE_SEPARATOR +
                "\"Infos zu allen Finanzen\" umgeschaltet werden" +
                PConst.LINE_SEPARATORx2 +
                "3) Die Teilbuchungen zu dem in der Tabelle" + PConst.LINE_SEPARATOR +
                "markierten Eintrag werden hier gelistet."
        );
        text.setPadding(new Insets(20));
        hBox.getChildren().add(text);

        TitledPane tpConfig = new TitledPane("Infos zur Programmoberfläche, Finanzen (mit Demodaten)", hBox);
        return tpConfig;
    }

    private Image getHelpScreen1() {
        final String path = ProgConst.P2_ICON_PATH + "startpage-1.png";
        return new Image(path, 600,
                600,
                true, true);
    }

    private Image getHelpScreen2() {
        final String path = ProgConst.P2_ICON_PATH + "startpage-2.png";
        return new Image(path, 600,
                600,
                true, true);
    }

}
