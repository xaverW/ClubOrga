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

package de.p2tools.cluborga.gui.dialog;


import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgConfig;
import de.p2tools.cluborga.config.prog.ProgConst;
import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.config.prog.ProgInfos;
import de.p2tools.cluborga.controller.SearchProgramUpdate;
import de.p2tools.cluborga.icon.GetIcon;
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.PHyperlink;
import de.p2tools.p2lib.tools.ProgramToolsFactory;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class AboutDialogController extends PDialogExtra {

    private Button btnOk = new Button("_Ok");
    private Button btnCheck = new Button("_Programmupdate prüfen");
    private final Color GRAY;

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public AboutDialogController(ClubConfig clubConfig) {
        super(clubConfig.getStage(), null, "Über das Programm", true, true);

        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;

        if (ProgConfig.SYSTEM_DARK_THEME.get()) {
            this.GRAY = Color.LAVENDER;
        } else {
            this.GRAY = Color.DARKBLUE;
        }
        addOkButton(btnOk);
        getHboxLeft().getChildren().add(btnCheck);
        init(true);
    }

    public void make() {
        btnOk.setOnAction(a -> close());
        btnCheck.setOnAction(a -> new SearchProgramUpdate(progData, progData.primaryStage).searchNewProgramVersion(true));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(0, 10, 0, 10));
//        gridPane.setGridLinesVisible(true);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        HBox hBox = new HBox();
        hBox.getChildren().add(gridPane);
        hBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(gridPane, Priority.ALWAYS);
        hBox.getStyleClass().add("dialog-about");
        getVBoxCont().getChildren().add(hBox);


        int row = 0;

        ImageView iv = new ImageView();
        Image im = getImage();
        iv.setSmooth(true);
        iv.setCache(true);
        iv.setImage(im);
        gridPane.add(iv, 0, row, 1, 2);

        // top
        Text text1 = new Text(ProgConst.PROGRAM_NAME);
        text1.setFont(Font.font(null, FontWeight.BOLD, 40));
        gridPane.add(text1, 1, row);
        GridPane.setValignment(text1, VPos.TOP);
        GridPane.setHalignment(text1, HPos.CENTER);

        Text text2 = new Text(P2LibConst.LINE_SEPARATOR + "Version: " + ProgramToolsFactory.getProgVersion());
        text2.setFont(new Font(18));
        Text text3 = new Text("[ Build: " + ProgramToolsFactory.getBuild() + " vom " + ProgramToolsFactory.getCompileDate() + " ]");

        VBox vBox = new VBox(5);
        vBox.getChildren().addAll(text2, text3);
        vBox.setAlignment(Pos.BOTTOM_CENTER);
        GridPane.setValignment(vBox, VPos.BOTTOM);
        GridPane.setHalignment(vBox, HPos.CENTER);
        gridPane.add(vBox, 1, ++row);


        //=======================
        gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        getVBoxCont().getChildren().add(gridPane);

        row = 0;
        final int c = 0;

        Text text = new Text(P2LibConst.LINE_SEPARATORx2 + "Autor");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, c, ++row, 2, 1);

        text = new Text("Xaver W. (xaverW)");
        text.setFont(new Font(15));
        gridPane.add(text, c, ++row, 2, 1);


        // Pfade
        text = new Text(P2LibConst.LINE_SEPARATORx2 + "Programm Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, c, ++row, 2, 1);

        PHyperlink hyperlinkWeb = new PHyperlink(ProgConst.URL_WEBSITE_CLUB);
        hyperlinkWeb.setStyle("-fx-font-size: 15px;");

        PHyperlink hyperlinkHelp = new PHyperlink(ProgConst.URL_WEBSITE_HELP);
        hyperlinkHelp.setStyle("-fx-font-size: 15px;");

        text = new Text("Website:");
        text.setFont(new Font(15));
//        text.setFill(GRAY);
        gridPane.add(text, c, ++row);
        gridPane.add(hyperlinkWeb, c + 1, row);

        text = new Text("Anleitung:");
        text.setFont(new Font(15));
//        text.setFill(GRAY);
        gridPane.add(text, c, ++row);
        gridPane.add(hyperlinkHelp, c + 1, row);


        // Pfade
        text = new Text(P2LibConst.LINE_SEPARATORx2 + "Pfade der Einstellungen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, c, ++row, 2, 1);

        text = new Text("Programm:");
        text.setFont(new Font(15));
//        text.setFill(GRAY);
        gridPane.add(text, c, ++row);

        text = new Text(new ProgInfos().getSettingsFile().toAbsolutePath().toString());
        text.setFont(new Font(15));
//        text.setFill(GRAY);
        gridPane.add(text, c + 1, row);

        text = new Text("Verein:");
        text.setFont(new Font(15));
//        text.setFill(GRAY);
        gridPane.add(text, c, ++row);

        text = new Text(clubConfig.getClubPath());
        text.setFont(new Font(15));
//        text.setFill(GRAY);
        gridPane.add(text, c + 1, row);


        // Java
        text = new Text(P2LibConst.LINE_SEPARATORx2 + "Java Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, c, ++row, 2, 1);

        text = new Text("Version:");
        text.setFont(new Font(15));
//        text.setFill(GRAY);
        gridPane.add(text, c, ++row);

        text = new Text(System.getProperty("java.version"));
        text.setFont(new Font(15));
//        text.setFill(GRAY);
        gridPane.add(text, c + 1, row);

        text = new Text("Type:");
        text.setFont(new Font(15));
//        text.setFill(GRAY);
        gridPane.add(text, c, ++row);

        String strVmType = System.getProperty("java.vm.name");
        strVmType += " (" + System.getProperty("java.vendor") + ")";
        text = new Text(strVmType);
        text.setFont(new Font(15));
//        text.setFill(GRAY);
        gridPane.add(text, c + 1, row);

        text = new Text(P2LibConst.LINE_SEPARATORx2 + "Ein Dankeschön an alle," + P2LibConst.LINE_SEPARATOR +
                "die mit Vorschlägen oder Quelltext" + P2LibConst.LINE_SEPARATOR +
                "zu diesem Programm beigetragen haben.");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, c, ++row, 2, 1);
    }

    private javafx.scene.image.Image getImage() {
        Image img = null;
        try {
            img = GetIcon.getImage(ProgConst.P2_ICON_850, ProgConst.P2_ICON_PATH, 150, 150);
        } catch (Exception ex) {
            PLog.errorLog(975421305, ex);
        }
        return img;
    }

}
