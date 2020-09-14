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
import de.p2tools.p2Lib.P2LibConst;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class StartDialogInfoPane {
    private final Stage stage;

    public StartDialogInfoPane(Stage stage) {
        this.stage = stage;
    }

    public void close() {
    }

    public TitledPane makeStart1() {

        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(25));

        ImageView iv = new ImageView();
        Image im = getHelpScreen1();
        iv.setSmooth(true);
        iv.setImage(im);

        hBox.getChildren().addAll(iv);
        Label lblText = new Label("1) Die unterschiedlichen Ansichten" + P2LibConst.LINE_SEPARATOR +
                "können hier ausgewählt werden:" + P2LibConst.LINE_SEPARATOR +
                "Mitglieder, Beiträge und Finanzen" +
                P2LibConst.LINE_SEPARATORx2 +
                "2) Die Daten sind dann hier gelistet" + P2LibConst.LINE_SEPARATOR +
                "(hier aktuell die Mitgliedsdaten)." +
                P2LibConst.LINE_SEPARATORx2 +
                "3) Unter der Tabelle können die" + P2LibConst.LINE_SEPARATOR +
                "angezeigten Daten gefiltert oder" + P2LibConst.LINE_SEPARATOR +
                "verändert werden." +
                P2LibConst.LINE_SEPARATORx2 +
                "4) Damit können Zeilen in der" + P2LibConst.LINE_SEPARATOR +
                "Tabelle markiert gefiltert, und der" + P2LibConst.LINE_SEPARATOR +
                "Filter auch wieder gelöscht werden." +
                P2LibConst.LINE_SEPARATORx2 +
                "5) Rechts neben der Tabelle können" + P2LibConst.LINE_SEPARATOR +
                "die ausgewählten Daten bearbeitet" + P2LibConst.LINE_SEPARATOR +
                "oder auch neue Einträge" + P2LibConst.LINE_SEPARATOR +
                "angelegt werden."
        );
        lblText.setMinWidth(Region.USE_PREF_SIZE);
        lblText.setPadding(new Insets(20));
        hBox.getChildren().add(lblText);

        TitledPane tpConfig = new TitledPane("Infos zur Programmoberfläche, Mitglieder (mit Demodaten)", hBox);
        return tpConfig;
    }

    public TitledPane makeStart2() {

        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(25));

        ImageView iv = new ImageView();
        Image im = getHelpScreen2();
        iv.setSmooth(true);
//        iv.setCache(true);
        iv.setImage(im);

        hBox.getChildren().addAll(iv);

        Label lblText = new Label("1) In der Tabelle hier sind die Finanzen" + P2LibConst.LINE_SEPARATOR +
                "gelistet." +
                P2LibConst.LINE_SEPARATORx2 +
                "2) Hier kann die Ansicht \"Filter\"," + P2LibConst.LINE_SEPARATOR +
                "\"Infos zum markierten Finanzeintrag\"" + P2LibConst.LINE_SEPARATOR +
                "und \"Infos zu allen Finanzen\"" + P2LibConst.LINE_SEPARATOR +
                "umgeschaltet werden" +
                P2LibConst.LINE_SEPARATORx2 +
                "3) Die Teilbuchungen zu dem in" + P2LibConst.LINE_SEPARATOR +
                "der Tabelle markierten Eintrag," + P2LibConst.LINE_SEPARATOR +
                "werden hier gelistet."
        );
        lblText.setMinWidth(Region.USE_PREF_SIZE);
        lblText.setPadding(new Insets(20));
        hBox.getChildren().add(lblText);

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
