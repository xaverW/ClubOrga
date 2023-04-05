/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.cluborga.gui.guiFee;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GuiFeeWorkerPane extends AnchorPane {

    private final VBox vbLeft = new VBox();

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public GuiFeeWorkerPane(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
        progData = ProgData.getInstance();

        ScrollPane scrollPaneLeft = new ScrollPane();
        scrollPaneLeft.setFitToHeight(true);
        scrollPaneLeft.setFitToWidth(true);

        getChildren().addAll(vbLeft);

        AnchorPane.setLeftAnchor(vbLeft, 0.0);
        AnchorPane.setBottomAnchor(vbLeft, 0.0);
        AnchorPane.setRightAnchor(vbLeft, 0.0);
        AnchorPane.setTopAnchor(vbLeft, 0.0);

        vbLeft.setPadding(new Insets(15, 15, 15, 15));
        vbLeft.setSpacing(20);
        vbLeft.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        vbLeft.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        scrollPaneLeft.setContent(vbLeft);

        initInfoLeft();
    }

    private void initInfoLeft() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        Button btnPayFee = new Button("Test");
        gridPane.add(btnPayFee, 0, row);

        vbLeft.getChildren().add(gridPane);
    }

}
