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

package de.p2tools.cluborga.gui.guiFinanceReport;


import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.data.financeData.FinanceReportData;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

public class GuiFinanceReportContextMenu {

    private final ClubConfig clubConfig;
    private final TableView tableView;

    public GuiFinanceReportContextMenu(ClubConfig clubConfig, TableView tableView) {
        this.clubConfig = clubConfig;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(FinanceReportData financeReportData) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, financeReportData);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, FinanceReportData financeData) {
        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> clubConfig.guiFinanceReport.resetTable());
        contextMenu.getItems().add(resetTable);
    }

}
