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

package de.p2tools.clubOrga.gui.guiFinanceReport;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.controller.export.ExportCsvDialogController;
import de.p2tools.clubOrga.data.financeData.FinanceReportDataList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

public class GuiFinanceReportMenu extends VBox {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private final GuiFinanceReport guiFinanceReport;

    public GuiFinanceReportMenu(ClubConfig clubConfig, GuiFinanceReport guiFinanceReport) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.guiFinanceReport = guiFinanceReport;

        initCont();
    }

    private void initCont() {
        this.setSpacing(10);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_CENTER);

        final MenuButton mb = new MenuButton("");
        mb.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU);
        mb.getStyleClass().add("btnFunction");


        MenuItem miExport = new MenuItem("Finanzen exportieren");
        miExport.setOnAction(a -> {
            FinanceReportDataList dataArr = clubConfig.financeReportDataList;
            if (dataArr == null || dataArr.isEmpty()) {
                return;
            }
            new ExportCsvDialogController(clubConfig.getStage(), clubConfig, null, dataArr);
        });

        mb.getItems().addAll(miExport);
        getChildren().addAll(mb);
    }

}
