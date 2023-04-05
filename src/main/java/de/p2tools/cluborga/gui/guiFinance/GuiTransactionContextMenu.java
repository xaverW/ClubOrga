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

package de.p2tools.cluborga.gui.guiFinance;


import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.data.financeData.TransactionData;
import de.p2tools.cluborga.gui.table.ClubTable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

public class GuiTransactionContextMenu {

    private final ClubConfig clubConfig;
    private final TableView tableView;

    public GuiTransactionContextMenu(ClubConfig clubConfig, TableView tableView) {
        this.clubConfig = clubConfig;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(TransactionData memberData) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, memberData);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, TransactionData memberData) {
//        MenuItem miMemberInfo = new MenuItem("Mitgliederinfos anzeigen");
//        miMemberInfo.setOnAction(a -> {
//            if (new DataDialogController(clubConfig, DataDialogController.OPEN.MEMBER_PANE,
//                    memberData, null, null).isOk()) {
//                tableView.refresh();
//            }
//        });
//        contextMenu.getItems().add(miMemberInfo);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new ClubTable(clubConfig).resetTable(tableView, ClubTable.TABLE.TRANSACTION));
        contextMenu.getItems().add(resetTable);
    }

}
