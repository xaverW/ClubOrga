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


package de.p2tools.clubOrga.gui.dialog.dataDialog;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.clubOrga.data.financeData.TransactionData;
import de.p2tools.clubOrga.data.financeData.TransactionDataList;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBoxObject;
import de.p2tools.p2Lib.guiTools.PTextField;
import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class TransactionPane extends VBox {

    private final FinanceData financeData;
    private int transactionDataNo;
    private final TransactionDataList transactionDataList;
    private final PComboBoxObject<TransactionData> cboTransaction;

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private List<ActionInterface> listeners = new ArrayList<>();

    public TransactionPane(ClubConfig clubConfig, FinanceData financeData, int transactionDataNO) {
        this.clubConfig = clubConfig;
        this.financeData = financeData;
        this.transactionDataNo = transactionDataNO;
        this.transactionDataList = financeData.getTransactionDataList();
        this.progData = ProgData.getInstance();

        cboTransaction = new PComboBoxObject<>();
        make();
    }

    public void addListener(ActionInterface actionInterface) {
        listeners.add(actionInterface);
    }

    public void reportAction() {
        for (ActionInterface ai : listeners)
            ai.reportAction(cboTransaction.getSelectionModel().getSelectedIndex());
    }

    private void make() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        ScrollPane scrollPane = new ScrollPane();
        if (transactionDataNo < 0) {
            // dann wird die erste angezeigt
            transactionDataNo = 0;
        }
        if (transactionDataList.size() > 1) {
            cboTransaction.init(transactionDataList, null);
            cboTransaction.setMaxWidth(Double.MAX_VALUE);
            cboTransaction.setOnAction(a -> {
                int tr = cboTransaction.getSelectionModel().getSelectedIndex();
                if (tr < 0) {
                    return;
                }
                this.transactionDataNo = tr;
                addTransactionData(gridPane);
                reportAction();
            });
            cboTransaction.getSelectionModel().select(transactionDataNo);

            final VBox vBox = new VBox(10);
            vBox.setPadding(new Insets(10));
            vBox.getChildren().addAll(cboTransaction, gridPane);
            scrollPane.setContent(vBox);

        } else {
            scrollPane.setContent(gridPane);
        }

        addTransactionData(gridPane);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        getChildren().add(scrollPane);
    }

    private void addTransactionData(GridPane gridPane) {
        int row = 0;
        TransactionData transactionData = transactionDataList.get(transactionDataNo);
        Config[] configs = transactionData.getConfigsArr();

        try {
            for (int i = 0; i < configs.length; ++i) {
                Control control;
                Config config = configs[i];

                if (config.getName().equals(FinanceFieldNames.ID) ||
                        config.getName().equals(FinanceFieldNames.FINANCE_ID) ||
                        config.getName().equals(TransactionDataList.TAG)) {
                    // spezielle Felder
                    continue;

                } else if (config.getName().equals(FinanceFieldNames.FEED_ID) &&
                        transactionData.getFeeData() != null) {
                    control = new PTextField(transactionData.getFeeNo() + "", true);
                    gridPane.add(new Label(FinanceFieldNames.FEED_NO + ":"), 0, row);
                    gridPane.add(control, 1, row);
                    ++row;
                    continue;

                } else if (config.getName().equals(FinanceFieldNames.BETRAG)) {
                    control = ((Config) config).getControl();

                } else if (config.getName().equals(FinanceFieldNames.KATEGORIE)) {
                    control = getPComboObject(transactionData.financeCategoryDataProperty(), clubConfig.financeCategoryDataList);

                } else if (config instanceof Config) {
                    // rest
                    control = ((Config) config).getControl();

                } else {
                    throw new PException("FinancePane.addTransactionData");
                }

                control.setMaxWidth(Double.MAX_VALUE);
                gridPane.add(new Label(config.getName() + ":"), 0, row);
                gridPane.add(control, 1, row);
                ++row;

            }
        } catch (PException ex) {
            PLog.errorLog(951203647, ex);
        }
    }

    private PComboBoxObject getPComboObject(ObjectProperty objectProperty, ObservableList cbo_list) {
        final PComboBoxObject cboStatus = new PComboBoxObject<>();
        cboStatus.setMaxWidth(Double.MAX_VALUE);
        cboStatus.init(cbo_list, objectProperty);

        return cboStatus;
    }

    public boolean isOk() {
        return true;
    }
}
