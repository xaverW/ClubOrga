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
import de.p2tools.clubOrga.controller.SearchProgramUpdate;
import de.p2tools.clubOrga.gui.tools.HelpText;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class GeneralController extends AnchorPane {

    private final Accordion accordion = new Accordion();
    private final HBox hBox = new HBox(0);

    private final PToggleSwitch tglSearch = new PToggleSwitch("einmal am Tag nach einer neuen Programmversion suchen");
    private final PToggleSwitch tglSearchBeta = new PToggleSwitch("auch nach neuen Vorabversionen suchen");
    private final PToggleSwitch tglDarkTheme = new PToggleSwitch("Dunkles Erscheinungsbild der Programmoberfläche");
    private final Button btnNow = new Button("_Jetzt suchen");
    private Button btnHelpBeta;
    ScrollPane scrollPane = new ScrollPane();

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private final Stage stage;
    BooleanProperty propDarkTheme = ProgConfig.SYSTEM_DARK_THEME;

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

    public void close() {
        tglDarkTheme.selectedProperty().unbindBidirectional(propDarkTheme);
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


        tglDarkTheme.selectedProperty().bindBidirectional(propDarkTheme);
        final Button btnHelpTheme = PButton.helpButton(stage, "Erscheinungsbild der Programmoberfläche",
                HelpText.DARK_THEME);

        gridPane.add(tglDarkTheme, 0, 0);
        gridPane.add(btnHelpTheme, 1, 0);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize());
    }

    private void makeUpdate(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));

        TitledPane tpConfig = new TitledPane("Programmupdate", gridPane);
        result.add(tpConfig);

        //einmal am Tag Update suchen
        tglSearch.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_UPDATE_SEARCH);
        final Button btnHelp = PButton.helpButton(stage, "Programmupdate suchen",
                "Beim Programmstart wird geprüft, ob es eine neue Version des Programms gibt. " +
                        "Ist eine aktualisierte Version vorhanden, dann wird das gemeldet."
                        + P2LibConst.LINE_SEPARATOR +
                        "Das Programm wird aber nicht ungefragt ersetzt.");


        tglSearchBeta.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_UPDATE_BETA_SEARCH);
        btnHelpBeta = PButton.helpButton(stage, "Vorabversionen suchen",
                "Beim Programmstart wird geprüft, ob es eine neue Vorabversion des Programms gibt. " +
                        P2LibConst.LINE_SEPARATORx2 +
                        "Das sind \"Zwischenschritte\" auf dem Weg zur nächsten Version. Hier ist die " +
                        "Entwicklung noch nicht abgeschlossen und das Programm kann noch Fehler enthalten. Wer Lust hat " +
                        "einen Blick auf die nächste Version zu werfen, ist eingeladen, die Vorabversionen zu testen." +
                        P2LibConst.LINE_SEPARATORx2 +
                        "Ist eine aktualisierte Vorabversion vorhanden, dann wird das gemeldet."
                        + P2LibConst.LINE_SEPARATOR +
                        "Das Programm wird aber nicht ungefragt ersetzt.");

        //jetzt suchen
        btnNow.setOnAction(event -> new SearchProgramUpdate(clubConfig.getStage(), clubConfig).searchNewVersionInfos());


        checkBeta();
        tglSearch.selectedProperty().addListener((ob, ol, ne) -> checkBeta());

        PHyperlink hyperlink = new PHyperlink(ProgConst.WEBSITE_CLUB);
        HBox hBoxHyper = new HBox();
        hBoxHyper.setAlignment(Pos.CENTER_LEFT);
        hBoxHyper.setPadding(new Insets(10, 0, 0, 0));
        hBoxHyper.setSpacing(10);
        hBoxHyper.getChildren().addAll(new Label("Infos auch auf der Website:"), hyperlink);

        int row = 0;
        gridPane.add(tglSearch, 0, row);
        gridPane.add(btnHelp, 1, row);

        gridPane.add(tglSearchBeta, 0, ++row);
        gridPane.add(btnHelpBeta, 1, row);

        gridPane.add(btnNow, 0, ++row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(hBoxHyper, 0, ++row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());
        gridPane.getRowConstraints().addAll(PColumnConstraints.getRcPrefSize(), PColumnConstraints.getRcPrefSize(),
                PColumnConstraints.getRcPrefSize(), PColumnConstraints.getRcVgrow(), PColumnConstraints.getRcPrefSize());
    }

    private void checkBeta() {
        if (tglSearch.isSelected()) {
            tglSearchBeta.setDisable(false);
            btnHelpBeta.setDisable(false);
        } else {
            tglSearchBeta.setDisable(true);
            tglSearchBeta.setSelected(false);
            btnHelpBeta.setDisable(true);
        }
    }

}
