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

package de.p2tools.clubOrga.gui.dialog;


import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.clubOrga.icon.GetIcon;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.guiTools.POpen;
import de.p2tools.p2Lib.tools.ProgramTools;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.nio.file.Path;

public class AboutDialogController extends PDialogExtra {

    Button btnOk = new Button("Ok");
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

//        getHboxOk().getChildren().addAll(btnOk);
        addOkButton(btnOk);

        init(true);
    }


    @Override
    protected void make() {
        btnOk.setOnAction(a -> close());
        HBox hBox = new HBox(10);
        getvBoxCont().getChildren().add(hBox);

        ImageView iv = new ImageView();
        Image im = getImage();
        if (im != null) {
            iv.setSmooth(true);
            iv.setCache(true);
            iv.setImage(im);
            iv.setFitWidth(150);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);

            hBox.getChildren().add(iv);
        }

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        int row = 0;

        // top
        Text text = new Text(ProgConst.PROGRAMMNAME);
        text.setFont(Font.font(null, FontWeight.BOLD, 40));
        gridPane.add(text, 0, row, 2, 1);

        text = new Text(P2LibConst.LINE_SEPARATOR + "Version: " + ProgramTools.getProgVersion());
        text.setFont(new Font(18));
        gridPane.add(text, 0, ++row, 2, 1);

        text = new Text("[ Build: " + ProgramTools.getBuild() + " vom " + ProgramTools.getCompileDate() + " ]");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row, 2, 1);

        text = new Text(P2LibConst.LINE_SEPARATORx2 + "Autor");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, ++row, 2, 1);

        text = new Text("Xaver W. (xaverW)");
        text.setFont(new Font(15));
        gridPane.add(text, 0, ++row, 2, 1);


        // Pfade
        text = new Text(P2LibConst.LINE_SEPARATORx2 + "Programm Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, ++row, 2, 1);

        Hyperlink hyperlink = new PHyperlink(ProgConst.WEBSITE_P2);
        hyperlink.setStyle("-fx-font-size: 15px;");
        hyperlink.setOnAction(a -> {
            try {
                POpen.openURL(ProgConst.WEBSITE_P2);
            } catch (Exception e) {
                PLog.errorLog(974125469, e);
            }
        });

        text = new Text("Website:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row);
        gridPane.add(hyperlink, 1, row);


        text = new Text("Programmeinstellungen:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row);

        final Path xmlFilePath = new ProgInfos().getSettingsFile();
        text = new Text(xmlFilePath.toAbsolutePath().toString());
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 1, row);


        text = new Text("Vereinsdaten:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row);

        final Path xmlClubPath = ProgInfos.getClubDirectory(clubConfig.getClubPath());
        text = new Text(xmlClubPath.toAbsolutePath().toString());
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 1, row);


        // Java
        text = new Text(P2LibConst.LINE_SEPARATORx2 + "Java Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, ++row, 2, 1);

        text = new Text("Version:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row);

        text = new Text(System.getProperty("java.version"));
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 1, row);

        text = new Text("Type:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row);

        String strVmType = System.getProperty("java.vm.name");
        strVmType += " (" + System.getProperty("java.vendor") + ")";
        text = new Text(strVmType);
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 1, row);

        text = new Text(P2LibConst.LINE_SEPARATORx2 + "Ein Dankeschön an alle," + P2LibConst.LINE_SEPARATOR + "die zu dieser Software beigetragen haben.");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, ++row, 2, 1);


        hBox.getChildren().add(gridPane);

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
