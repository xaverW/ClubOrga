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
import de.p2tools.p2Lib.configFile.config.ConfigExtra;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBoxObject;
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

public class TransactionPane extends VBox {

    private final FinanceData financeData;
    private final TransactionData transactionData;

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public TransactionPane(ClubConfig clubConfig, FinanceData financeData, TransactionData transactionData) {

        this.clubConfig = clubConfig;
        this.financeData = financeData;
        this.transactionData = transactionData;

        this.progData = ProgData.getInstance();
        make();
    }


    private void make() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        addTransactionData(gridPane);

        ScrollPane scrollPane = new ScrollPane(gridPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        getChildren().add(scrollPane);
    }

    private void addTransactionData(GridPane gridPane) {
        int row = 0;
        Config[] configs = transactionData.getConfigsArr();

        try {
            for (int i = 0; i < configs.length; ++i) {
                Control control;
                Config config = configs[i];

                if (config.getName().equals(FinanceFieldNames.ID) ||
                        config.getName().equals(FinanceFieldNames.FINANCE_ID) ||
                        config.getName().equals(FinanceFieldNames.FEED_ID) ||
                        config.getName().equals(TransactionDataList.TAG)) {
                    // spezielle Felder
                    continue;

                } else if (config.getName().equals(FinanceFieldNames.BETRAG)) {
                    control = ((ConfigExtra) config).getControl();

//                } else if (config.getName().equals(FinanceFieldNames.KONTO)) {
//                    control = getPComboObject(transactionData.financeAccountDataProperty(), clubConfig.financeAccountDataList);
                } else if (config.getName().equals(FinanceFieldNames.KATEGORIE)) {
                    control = getPComboObject(transactionData.financeCategoryDataProperty(), clubConfig.financeCategoryDataList);

                } else if (config instanceof ConfigExtra) {
                    // rest
                    control = ((ConfigExtra) config).getControl();

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

    private boolean check() {
        boolean ret = true;
        return ret;
    }

    public boolean isOk() {
        return check();
    }
}
