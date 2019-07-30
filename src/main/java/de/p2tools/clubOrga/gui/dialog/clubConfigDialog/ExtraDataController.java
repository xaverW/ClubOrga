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
import de.p2tools.clubOrga.config.prog.ProgData;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class ExtraDataController extends AnchorPane {

    private final Accordion accordion = new Accordion();
    private final HBox hBox = new HBox(0);

    ScrollPane scrollPane = new ScrollPane();

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private final Stage stage;

    public ExtraDataController(Stage stage, ClubConfig clubConfig) {
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
//        makeExtraName(result);

        ExtraDataPane expMember = new ExtraDataPane(stage, clubConfig);
        TitledPane tpMember = expMember.getExtraPane(clubConfig.extraDataListMember);
        tpMember.setText("Mitglieder");
        result.add(tpMember);

        ExtraDataPane expFee = new ExtraDataPane(stage, clubConfig);
        TitledPane tpFee = expFee.getExtraPane(clubConfig.extraDataListFee);
        tpFee.setText("Beiträge");
        result.add(tpFee);

        ExtraDataPane expFinance = new ExtraDataPane(stage, clubConfig);
        TitledPane tpFinance = expFinance.getExtraPane(clubConfig.extraDataListFinance);
        tpFinance.setText("Finanzen");
        result.add(tpFinance);

        return result;
    }

//    private void makeExtraName(Collection<TitledPane> result) {
//        final GridPane gridPane = new GridPane();
//        gridPane.setHgap(15);
//        gridPane.setVgap(15);
//        gridPane.setPadding(new Insets(20, 20, 20, 20));
//
//        TitledPane tpConfig = new TitledPane("Namen", gridPane);
//        result.add(tpConfig);
//
//        gridPane.add(new Label("Mitglieder"), 0, 0);
//        gridPane.add(new Label("Beiträge"), 1, 0);
//        gridPane.add(new Label("Finanzen"), 2, 0);
//
//        int row = 1;
//        for (int i = 0; i < ProgConst.MAX_EXTRA_DATA_MAX_MEMBER; ++i) {
//            TextField txt = new TextField();
//            ExtraData ex = clubConfig.extraDataListMember.get(i);
//            txt.textProperty().bindBidirectional(ex.nameProperty());
//            gridPane.add(txt, 0, row++);
//        }
//
//        row = 1;
//        for (int i = 0; i < ProgConst.MAX_EXTRA_DATA_MAX_FEE; ++i) {
//            TextField txt = new TextField();
//            ExtraData ex = clubConfig.extraDataListFee.get(i);
//            txt.textProperty().bindBidirectional(ex.nameProperty());
//            gridPane.add(txt, 1, row++);
//        }
//
//        row = 1;
//        for (int i = 0; i < ProgConst.MAX_EXTRA_DATA_MAX_FINANCE; ++i) {
//            TextField txt = new TextField();
//            ExtraData ex = clubConfig.extraDataListFinance.get(i);
//            txt.textProperty().bindBidirectional(ex.nameProperty());
//            gridPane.add(txt, 2, row++);
//        }
//
//
//        gridPane.add(new Label(" "), 0, row++);
//
//        final Button btnHelp = new Button("");
//        btnHelp.setTooltip(new Tooltip("Hilfe anzeigen."));
//        btnHelp.setGraphic(new ProgIcons().ICON_BUTTON_HELP);
//        btnHelp.setOnAction(a -> PAlert.showHelpAlert(stage, "Extrafelder",
//                "Die Mitgliedsdaten, Beitragsdaten und Finanzdaten enthalten je ein paar Felder " +
//                        "die das Programm nicht nutzt. Diese sind gedacht, für Infos die für den Verein wichtig sind und " +
//                        "so erfasst werden können. Die Titel der Felder können hier angepasst werden."));
//        GridPane.setHalignment(btnHelp, HPos.RIGHT);
//        gridPane.add(btnHelp, 2, row);
//
//        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow(),
//                PColumnConstraints.getCcComputedSizeAndHgrow(),
//                PColumnConstraints.getCcComputedSizeAndHgrow());
//    }


}
