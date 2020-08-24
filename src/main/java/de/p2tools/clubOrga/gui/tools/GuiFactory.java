/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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


package de.p2tools.clubOrga.gui.tools;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class GuiFactory {

    private GuiFactory() {
    }

    public static void setColor(Control tf, boolean set) {
        if (set) {
            tf.getStyleClass().add("txtIsEmpty");
        } else {
            tf.getStyleClass().removeAll("txtIsEmpty");
        }
    }

    public static void setPaneTitle(BorderPane borderPane, String title) {
        Label lblTitle = new Label(title);
        lblTitle.setMaxWidth(Double.MAX_VALUE);
        lblTitle.getStyleClass().add("label-tile-title");

        HBox hBox = new HBox();
        HBox.setHgrow(lblTitle, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(lblTitle);
        borderPane.setTop(hBox);
    }

    public static HBox getTitle(String title) {
        return getTitle(null, title);
    }

    public static HBox getTitle(HBox hBox, String title) {
        Label lblTitle = new Label(title);
        lblTitle.setMaxWidth(Double.MAX_VALUE);
        lblTitle.getStyleClass().add("label-tile-title");

        if (hBox == null) {
            hBox = new HBox();
        }
        HBox.setHgrow(lblTitle, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(lblTitle);
        return hBox;
    }

    public static HBox getDialogTitle(String title) {
        return getDialogTitle(null, title);
    }

    public static HBox getDialogTitle(HBox hBox, String title) {
        Label lblTitle = new Label(title);
        lblTitle.setMaxWidth(Double.MAX_VALUE);
        lblTitle.getStyleClass().add("label-dialog-title");

        if (hBox == null) {
            hBox = new HBox();
        }
        HBox.setHgrow(lblTitle, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(5, 0, 15, 0));
        hBox.getChildren().add(lblTitle);
        return hBox;
    }

}
