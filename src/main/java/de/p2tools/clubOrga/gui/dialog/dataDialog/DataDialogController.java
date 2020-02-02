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

package de.p2tools.clubOrga.gui.dialog.dataDialog;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.feeData.FeeFactory;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFactory;
import de.p2tools.clubOrga.data.financeData.TransactionData;
import de.p2tools.clubOrga.data.financeData.TransactionFactory;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberFactory;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class DataDialogController extends PDialogExtra {

    private TabPane tabPane = new TabPane();
    private Button btnOk = new Button("Ok");
    private Button btnCancel = new Button("Abbrechen");

    private MemberData memberDataOrg = null;
    private MemberData memberDataCopy = null;
    private FeeData feeDataOrg = null;
    private FeeData feeDataCopy = null;
    private FinanceData financeDataOrg = null;
    private FinanceData financeDataCopy = null;
    private TransactionData transactionDataOrg = null;
    private TransactionData transactionDataCopy = null;

    private MemberPane memberPane = null;
    private FeePane feePane = null;
    private FinancePane financePane = null;
    private TransactionPane transactionPane = null;

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private boolean ok = false;
    private OPEN open;

    public static enum OPEN {MEMBER_PANE, FEE_PANE, FINANCE_PANE, TRANSACTION_PANE}

    public DataDialogController(ClubConfig clubConfig, OPEN open,
                                MemberData memberData, FeeData feeData,
                                FinanceData financeData, TransactionData transactionData) {

        super(clubConfig.getStage(), clubConfig.DATA_DIALOG_SIZE, "Daten ändern",
                true, true, DECO.BORDER);

        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.open = open;

        this.memberDataOrg = memberData;
        this.feeDataOrg = feeData;
        this.financeDataOrg = financeData;
        this.transactionDataOrg = transactionData;

        init(true);
    }

    @Override
    public void make() {
        getvBoxCont().setPadding(new Insets(0));
        getvBoxCont().getChildren().add(tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        tabPane.setPadding(new Insets(15));
        addOkCancelButtons(btnOk, btnCancel);

        initPanel();
        btnOk.setOnAction(a -> {
            if (check()) {
                ok = true;
                close();
            }
        });
        btnCancel.setOnAction(a -> close());
    }

    public boolean isOk() {
        return ok;
    }

    public void close() {
        super.close();
    }


    private void initPanel() {
        if (memberDataOrg != null) {
            memberDataCopy = MemberFactory.getNewMemberData(clubConfig, "", false);
            MemberFactory.copyMemberData(memberDataOrg, memberDataCopy);

            memberPane = new MemberPane(clubConfig, memberDataCopy);
            Tab tab = new Tab("Mitglied");
            tab.setClosable(false);
            tab.setContent(memberPane);
            tabPane.getTabs().add(tab);
            if (open.equals(OPEN.MEMBER_PANE)) {
                tabPane.getSelectionModel().select(tab);
            }
        }
        if (feeDataOrg != null) {
            feeDataCopy = FeeFactory.getNewFeeWithNo(clubConfig);
            FeeFactory.copyFeeData(feeDataOrg, feeDataCopy);

            feePane = new FeePane(clubConfig, feeDataCopy, memberDataCopy);
            Tab tab = new Tab("Beitrag");
            tab.setClosable(false);
            tab.setContent(feePane);
            tabPane.getTabs().add(tab);
            if (open.equals(OPEN.FEE_PANE)) {
                tabPane.getSelectionModel().select(tab);
            }
        }
        if (financeDataOrg != null) {
            financeDataCopy = FinanceFactory.getNewFinanceWithId(clubConfig);
            FinanceFactory.copyFinanceData(financeDataOrg, financeDataCopy);

            financePane = new FinancePane(clubConfig, financeDataCopy);
            Tab tab = new Tab("Finanzen");
            tab.setClosable(false);
            tab.setContent(financePane);
            tabPane.getTabs().add(tab);
            if (open.equals(OPEN.FINANCE_PANE)) {
                tabPane.getSelectionModel().select(tab);
            }
        }
        if (transactionDataOrg != null) {
            transactionDataCopy = new TransactionData();
            TransactionFactory.copyTransactionData(transactionDataOrg, transactionDataCopy);


            transactionPane = new TransactionPane(clubConfig, financeDataCopy, transactionDataCopy);
            Tab tab = new Tab("Transaktion");
            tab.setClosable(false);
            tab.setContent(transactionPane);
            tabPane.getTabs().add(tab);
            if (open.equals(OPEN.TRANSACTION_PANE)) {
                tabPane.getSelectionModel().select(tab);
            }
        }
    }

    private boolean check() {
        boolean ret = true;
        if (memberPane != null && !memberPane.isOk()) {
            ret = false;
        }
        if (feePane != null && !feePane.isOk()) {
            ret = false;
        }
        if (financePane != null && !financePane.isOk()) {
            ret = false;
        }

        if (transactionPane != null && !transactionPane.isOk()) {
            ret = false;
        }

        if (ret) {
            // geänderte Daten wieder auf ORG zurück kopieren
            MemberFactory.copyMemberData(memberDataCopy, memberDataOrg);
            FeeFactory.copyFeeData(feeDataCopy, feeDataOrg);
            FinanceFactory.copyFinanceData(financeDataCopy, financeDataOrg);
            TransactionFactory.copyTransactionData(transactionDataCopy, transactionDataOrg);
        }

        return ret;
    }

}
