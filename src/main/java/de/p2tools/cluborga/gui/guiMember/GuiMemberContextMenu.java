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

package de.p2tools.cluborga.gui.guiMember;


import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.data.feeData.FeeFactory;
import de.p2tools.cluborga.data.memberData.MemberData;
import de.p2tools.cluborga.gui.dialog.dataDialog.DataDialogController;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

public class GuiMemberContextMenu {

    private final ClubConfig clubConfig;
    private final TableView tableView;

    public GuiMemberContextMenu(ClubConfig clubConfig, TableView tableView) {
        this.clubConfig = clubConfig;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(MemberData memberData) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, memberData);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, MemberData memberData) {
        MenuItem miMemberFee = new MenuItem("fehlende Beiträge anlegen");
        miMemberFee.setOnAction(a -> {
            FeeFactory.generateMissingFeesForMembers(clubConfig, memberData);
        });
        contextMenu.getItems().add(miMemberFee);

        MenuItem miMemberInfo = new MenuItem("Mitgliederinfos anzeigen");
        miMemberInfo.setOnAction(a -> {
            if (new DataDialogController(clubConfig, memberData).isOk()) {
                tableView.refresh();
            }
        });
        contextMenu.getItems().add(miMemberInfo);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> clubConfig.guiMember.resetTable());
        contextMenu.getItems().add(resetTable);
    }

}
