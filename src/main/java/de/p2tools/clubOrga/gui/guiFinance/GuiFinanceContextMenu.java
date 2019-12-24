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

package de.p2tools.clubOrga.gui.guiFinance;


import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.TransactionData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.gui.dialog.dataDialog.DataDialogController;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

public class GuiFinanceContextMenu {

    private final ClubConfig clubConfig;
    private final TableView tableView;

    public GuiFinanceContextMenu(ClubConfig clubConfig, TableView tableView) {
        this.clubConfig = clubConfig;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(FinanceData financeData) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, financeData);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, FinanceData financeData) {
        final MenuItem miFinancesInfo = new MenuItem("Buchungsinfos anzeigen");

        final FeeData feeData;
        final MemberData memberData;
        final TransactionData transactionData;
        if (financeData.getTransactionDataList().size() == 1) {
            transactionData = financeData.getTransactionDataList().get(0);
            feeData = transactionData.getFeeData();
            if (feeData != null) {
                memberData = feeData.getMemberData();
            } else {
                memberData = null;
            }
        } else {
            feeData = null;
            memberData = null;
            transactionData = null;
        }
        miFinancesInfo.setOnAction(a -> {
            if (new DataDialogController(clubConfig, DataDialogController.OPEN.FINANCE_PANE,
                    memberData, feeData, financeData, transactionData).isOk()) {
                tableView.refresh();
            }
        });
        contextMenu.getItems().add(miFinancesInfo);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> clubConfig.guiFinance.resetTable());
        contextMenu.getItems().add(resetTable);
    }

}
