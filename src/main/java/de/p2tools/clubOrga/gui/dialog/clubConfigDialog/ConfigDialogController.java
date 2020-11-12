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
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class ConfigDialogController extends PDialogExtra {

    private TabPane tabPane = new TabPane();
    private Button btnOk = new Button("Ok");

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private ButtonBar buttonBar = new ButtonBar();

    GeneralController generalController;
    ExtraDataController extraDataController;

    //    StringProperty sizeProperty = ProgConfig.CONFIG_DIALOG_SIZE;


    public ConfigDialogController(ClubConfig clubConfig) {
        super(clubConfig.getStage(), clubConfig.CLUB_CONFIG_DIALOG_SIZE, "Einstellungen", true, false, DECO.NONE);
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;

        VBox.setVgrow(tabPane, Priority.ALWAYS);
        getvBoxCont().getChildren().add(tabPane);

        addOkButton(btnOk);
        init(true);
    }

    @Override
    public void make() {
        initPanel();
        ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> updateCss());
        btnOk.setOnAction(a -> close());
    }

    public void close() {
//        Platform.runLater(() -> {
        clubConfig.guiMember.refreshTable();
        clubConfig.guiFee.refreshTable();
        clubConfig.guiFinance.refreshTable();
//        });

        super.close();
    }


    private void initPanel() {
        try {
            generalController = new GeneralController(getStage(), clubConfig);
            Tab tab = new Tab("Allgemein");
            tab.setClosable(false);
            tab.setContent(generalController);
            tabPane.getTabs().add(tab);

            extraDataController = new ExtraDataController(getStage(), clubConfig);
            tab = new Tab("Extra Felder");
            tab.setClosable(false);
            tab.setContent(extraDataController);
            tabPane.getTabs().add(tab);

        } catch (final Exception ex) {
            PLog.errorLog(987010102, ex);
        }
    }

}
