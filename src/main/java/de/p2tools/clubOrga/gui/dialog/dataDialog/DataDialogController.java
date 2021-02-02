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
import de.p2tools.clubOrga.data.financeData.TransactionDataList;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberFactory;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class DataDialogController extends PDialogExtra implements ActionInterface {

    private TabPane tabPane = new TabPane();
    private Button btnOk = new Button("Ok");
    private Button btnCancel = new Button("Abbrechen");

    private MemberData memberDataOrg = null;
    private MemberData memberDataCopy = null;
    private FeeData feeDataOrg = null;
    private FeeData feeDataCopy = null;
    private FinanceData financeDataOrg = null;
    private FinanceData financeDataCopy = null;
    private int transactionDataNo = 0;

    private MemberPane memberPane = null;
    private FeePane feePane = null;
    private FinancePane financePane = null;
    private TransactionPane transactionPane = null;

    private Tab memberTab = null;
    private Tab feeTab = null;
    private Tab financeTab = null;
    private Tab transactionTab = null;

    private final ProgData progData;
    private final ClubConfig clubConfig;
    private boolean ok = false;
    private OPEN open;

    public enum OPEN {MEMBER_PANE, FEE_PANE, FINANCE_PANE, TRANSACTION_PANE}

    public DataDialogController(ClubConfig clubConfig, MemberData memberData) {

        super(clubConfig.getStage(), clubConfig.DATA_DIALOG_SIZE, "Daten ändern",
                true, true, DECO.BORDER);

        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.open = OPEN.MEMBER_PANE;
        this.memberDataOrg = memberData;

        init(true);
    }

    public DataDialogController(ClubConfig clubConfig, FeeData feeData) {

        super(clubConfig.getStage(), clubConfig.DATA_DIALOG_SIZE, "Daten ändern",
                true, true, DECO.BORDER);

        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.open = OPEN.FEE_PANE;
        this.memberDataOrg = feeData.getMemberData();
        this.feeDataOrg = feeData;

        init(true);
    }

    public DataDialogController(ClubConfig clubConfig, OPEN open,
                                FinanceData financeData, int transactionDataNo) {

        super(clubConfig.getStage(), clubConfig.DATA_DIALOG_SIZE, "Daten ändern",
                true, true, DECO.BORDER);

        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.open = open;

        this.memberDataOrg = null;
        this.feeDataOrg = null;
        this.financeDataOrg = financeData;
        this.transactionDataNo = transactionDataNo;

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

    @Override
    public void reportAction(int sel) {
        transactionDataNo = sel;
        open = OPEN.TRANSACTION_PANE;
        initPanel();
    }

    public boolean isOk() {
        return ok;
    }

    public void close() {
        super.close();
    }

    private void initPanel() {
        memberTab = null;
        feeTab = null;
        financeTab = null;
        transactionTab = null;

        copyData();
        addMemberTab();
        addFeeTab();
        addFinanceTab();

        tabPane.getTabs().clear();
        if (memberTab != null) {
            tabPane.getTabs().add(memberTab);
        }
        if (feeTab != null) {
            tabPane.getTabs().add(feeTab);
        }
        if (financeTab != null) {
            tabPane.getTabs().add(financeTab);
        }
        if (transactionTab != null) {
            tabPane.getTabs().add(transactionTab);
        }

        if (transactionDataNo >= 0 && transactionTab != null) {
            tabPane.getSelectionModel().select(transactionTab);
        }
        switch (open) {
            case MEMBER_PANE:
                tabPane.getSelectionModel().select(memberTab);
                break;
            case FEE_PANE:
                tabPane.getSelectionModel().select(feeTab);
                break;
            case FINANCE_PANE:
                tabPane.getSelectionModel().select(financeTab);
                break;
            case TRANSACTION_PANE:
                tabPane.getSelectionModel().select(transactionTab);
                break;
        }
    }

    private void copyData() {
        switch (open) {
            case MEMBER_PANE:
                memberDataCopy = MemberFactory.copyMemberData(clubConfig, memberDataOrg, memberDataCopy);
                break;
            case FEE_PANE:
                feeDataCopy = FeeFactory.copyFeeData(clubConfig, feeDataOrg, feeDataCopy);
                memberDataCopy = feeDataCopy.getMemberData();
                break;
            case FINANCE_PANE:
            case TRANSACTION_PANE:
                if (financeDataCopy == null) {
                    // sonst hammers schon mal gemacht und kann mehrmals aufgerufen werden
                    financeDataCopy = FinanceFactory.copyFinanceData(clubConfig, financeDataOrg, financeDataCopy);

                    final TransactionDataList trlFrom, trlTo;
                    trlFrom = financeDataOrg.getTransactionDataList();
                    trlTo = financeDataCopy.getTransactionDataList();

                    for (int i = 0; i < trlFrom.size(); ++i) {

                        int ii;
                        for (ii = 0; ii < i; ++ii) {
                            final FeeData feeFrom = trlFrom.get(ii).getFeeData();
                            final FeeData feeTo = trlFrom.get(i).getFeeData();
                            if (feeFrom != null && feeTo != null) {
                                if (feeFrom.equals(feeTo)) {
                                    // dann sind sie gleich
                                    trlTo.get(i).setFeeData(trlTo.get(ii).getFeeData());
                                }
                            }
                        }

                        for (ii = 0; ii < i; ++ii) {
                            if (trlFrom.get(ii).getFeeData() != null &&
                                    trlFrom.get(i).getFeeData() != null &&
                                    trlFrom.get(ii).getFeeData().getMemberData() != null &&
                                    trlFrom.get(i).getFeeData().getMemberData() != null) {

                                if (trlFrom.get(ii).getFeeData().getMemberData().equals(trlFrom.get(i).getFeeData().getMemberData())) {
                                    // dann sind sie gleich
                                    trlTo.get(i).getFeeData().setMemberData(trlTo.get(ii).getFeeData().getMemberData());
                                }
                            }
                        }


                    }

                }

                if (financeDataOrg != null && transactionDataNo >= 0) {
                    // nur dann passt FeeDate und MemberDate auch immer
                    this.feeDataOrg = financeDataOrg.getTransactionDataList().get(transactionDataNo).getFeeData();
                    this.feeDataCopy = financeDataCopy.getTransactionDataList().get(transactionDataNo).getFeeData();

                    this.memberDataOrg = this.feeDataOrg == null ? null : this.feeDataOrg.getMemberData();
                    this.memberDataCopy = this.feeDataCopy == null ? null : this.feeDataCopy.getMemberData();
                }


                break;
        }
    }

    private void addMemberTab() {
        if (memberDataOrg != null) {
            memberPane = new MemberPane(clubConfig, memberDataCopy);
            memberTab = new Tab("Mitglied");
            memberTab.setClosable(false);
            memberTab.setContent(memberPane);
        }
    }

    private void addFeeTab() {
        if (feeDataOrg != null) {
            switch (open) {
                case FEE_PANE:
                    feePane = new FeePane(clubConfig, feeDataCopy, memberDataCopy, true);
                    break;
                default:
                    feePane = new FeePane(clubConfig, feeDataCopy, memberDataCopy, false);
            }
            feeTab = new Tab("Beitrag");
            feeTab.setClosable(false);
            feeTab.setContent(feePane);
        }
    }

    private void addFinanceTab() {
        if (financeDataOrg != null) {
            financePane = new FinancePane(clubConfig, financeDataCopy);
            financeTab = new Tab("Finanzen");
            financeTab.setClosable(false);
            financeTab.setContent(financePane);

            transactionPane = new TransactionPane(clubConfig, financeDataCopy, transactionDataNo);
            transactionPane.addListener(this);
            /*Tab*/
            transactionTab = new Tab("Transaktion");
            transactionTab.setClosable(false);
            transactionTab.setContent(transactionPane);
        }
    }

    private boolean check() {
        if (memberPane != null && !memberPane.isOk()) {
            return false;
        }
        if (feePane != null && !feePane.isOk()) {
            return false;
        }
        if (financePane != null && !financePane.isOk()) {
            return false;
        }
        if (transactionPane != null && !transactionPane.isOk()) {
            return false;
        }

        if (feePane != null) {
            feePane.payFee();
        }

        // geänderte Daten wieder auf ORG zurück kopieren
        if (financeDataOrg == null) {
            // dann gibts keine Finanzen und nix zum Umschalten im Dialog
            MemberFactory.copyMemberData(clubConfig, memberDataCopy, memberDataOrg);
            FeeFactory.copyFeeData(clubConfig, feeDataCopy, feeDataOrg);

        } else {
            // dann können verschiedene Member/Fee geändert worden sein
            FinanceFactory.copyFinanceData(clubConfig, financeDataCopy, financeDataOrg);
        }

        if (feeDataOrg != null) {
            // falls sich MittgliedDaten geändert haben
            clubConfig.feeDataList.stream().forEach(feeData -> feeData.initMemberName());
        }

        return true;
    }
}
