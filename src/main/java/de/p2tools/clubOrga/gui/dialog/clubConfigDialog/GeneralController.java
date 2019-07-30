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

package de.p2tools.clubOrga.gui.dialog.clubConfigDialog;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.POpen;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class GeneralController extends AnchorPane {

    private final Accordion accordion = new Accordion();
    private final HBox hBox = new HBox(0);

    ScrollPane scrollPane = new ScrollPane();

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private final Stage stage;

    public GeneralController(Stage stage, ClubConfig clubConfig) {
        progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.stage = stage;

        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        hBox.getChildren().addAll(scrollPane);
        getChildren().addAll(hBox);

        accordion.setPadding(new Insets(1));

        AnchorPane.setLeftAnchor(hBox, 10.0);
        AnchorPane.setBottomAnchor(hBox, 10.0);
        AnchorPane.setRightAnchor(hBox, 10.0);
        AnchorPane.setTopAnchor(hBox, 10.0);

        accordion.getPanes().addAll(createPanes());
        scrollPane.setContent(accordion);

    }

    private Collection<TitledPane> createPanes() {
        Collection<TitledPane> result = new ArrayList<TitledPane>();
        makeConfig(result);
        makeUpdate(result);
//        accordion.setExpandedPane(result.stream().findFirst().get());
        return result;
    }

    private void makeConfig(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        TitledPane tpConfig = new TitledPane("Allgemein", gridPane);
        result.add(tpConfig);


        final ColumnConstraints ccTxt = new ColumnConstraints();
        ccTxt.setFillWidth(true);
        ccTxt.setMinWidth(Region.USE_COMPUTED_SIZE);
        ccTxt.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(new ColumnConstraints(), ccTxt);
    }

    private void makeUpdate(Collection<TitledPane> result) {
        final VBox vBox = new VBox();
        vBox.setFillWidth(true);
        TitledPane tpConfig = new TitledPane("Programmupdate", vBox);
        result.add(tpConfig);

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        vBox.getChildren().add(gridPane);

        //einmal am Tag Update suchen
        final CheckBox tglSearch = new CheckBox("einmal am Tag nach einer neuen Programmversion suchen");
        tglSearch.selectedProperty().bindBidirectional(ProgConfig.CLUB_CONFIG_SEARCH_PROG_UPDATE);
        gridPane.add(tglSearch, 0, 0);

        final Button btnHelp = new Button("");
        btnHelp.setTooltip(new Tooltip("Hilfe anzeigen."));
        btnHelp.setGraphic(new ProgIcons().ICON_BUTTON_HELP);
        btnHelp.setOnAction(a -> PAlert.showHelpAlert(stage, "Programmupdate suchen",
                "Beim Programmstart wird geprüft, ob es eine neue Version des Programms gibt. " +
                        "Ist eine aktualisierte Version vorhanden, wird das dann gemeldet." + PConst.LINE_SEPARATOR +
                        "Das Programm wird aber nicht ungefragt ersetzt."));
        GridPane.setHalignment(btnHelp, HPos.RIGHT);
        gridPane.add(btnHelp, 1, 0);

        final ColumnConstraints ccTxt = new ColumnConstraints();
        ccTxt.setFillWidth(true);
        ccTxt.setMinWidth(Region.USE_COMPUTED_SIZE);
        ccTxt.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(new ColumnConstraints(), ccTxt);

        //jetzt suchen
        Button btnNow = new Button("Jetzt suchen");
        btnNow.setMaxWidth(Double.MAX_VALUE);
//        btnNow.setOnAction(event -> new SearchProgramUpdate().checkVersion(true, true /* anzeigen */));

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 0, 0, 0));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(btnNow);
        gridPane.add(hBox, 0, 1);


        Hyperlink hyperlink = new Hyperlink(ProgConst.WEBSITE_CLUB);
        hyperlink.setOnAction(a -> {
            try {
                POpen.openURL(ProgConst.WEBSITE_CLUB);
            } catch (Exception e) {
                PLog.errorLog(932012478, e);
            }
        });
        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(10, 0, 0, 0));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(new Label("Infos auch auf der Website:"), hyperlink);
        gridPane.add(hBox, 0, 2);


    }

}
