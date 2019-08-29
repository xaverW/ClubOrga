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

package de.p2tools.clubOrga.gui.table;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;

public class ClubTable {
    public enum TABLE {
        MEMBER, MEMBER_RATE, MEMBER_STATE, MEMBER_PAYMENT_TYPE, ADD_MISSING_FEE,
        FEE, PAY_FEE, FINANCE, FINANCE_ACCOUNT, FINANCE_CATEGORY, TRANSACTION,
        FINANCE_REPORT,
        CALCULATION_CATEGORY, CALCULATION_ACCOUNT
    }

    private static final String SORT_ASCENDING = "ASCENDING";
    private static final String SORT_DESCENDING = "DESCENDING";
    private String width = "";
    private String sort = "";
    private String upDown = "";
    private String vis = "";
    private String order = "";

    private int maxSpalten;
    private double[] breite;
    private boolean[] visAr;
    private TableColumn[] tArray;

    private StringProperty confWidth; //Spaltenbreite
    private StringProperty confSort; //"Sortieren"  der Tabelle nach Spalte
    private StringProperty confUpDown; //Sortierung UP oder Down
    private StringProperty confVis; //Spalte ist sichtbar
    private StringProperty confOrder; //"Reihenfolge" der Spalten

    private final ClubConfig clubConfig;

    public ClubTable(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
    }

    public void addResetMenue(TableView tableView, TABLE eTable) {
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final ContextMenu contextMenu = new ContextMenu();

                MenuItem miResetTable = new MenuItem("Tabelle zurücksetzen");
                miResetTable.setOnAction(a -> resetTable(tableView, eTable));
                contextMenu.getItems().add(miResetTable);

                tableView.setContextMenu(contextMenu);
            }
        });
    }

    private void initConf(TABLE eTable) {
        switch (eTable) {

            case MEMBER:
                confWidth = clubConfig.MEMBER_GUI_TABLE_WIDTH;
                confSort = clubConfig.MEMBER_GUI_TABLE_SORT;
                confUpDown = clubConfig.MEMBER_GUI_TABLE_UPDOWN;
                confVis = clubConfig.MEMBER_GUI_TABLE_VIS;
                confOrder = clubConfig.MEMBER_GUI_TABLE_ORDER;
                break;

            case MEMBER_RATE:
                confWidth = clubConfig.MEMBER_RATES_GUI_TABLE_WIDTH;
                confSort = clubConfig.MEMBER_RATES_GUI_TABLE_SORT;
                confUpDown = clubConfig.MEMBER_RATES_GUI_TABLE_UPDOWN;
                confVis = clubConfig.MEMBER_RATES_GUI_TABLE_VIS;
                confOrder = clubConfig.MEMBER_RATES_GUI_TABLE_ORDER;
                break;

            case MEMBER_STATE:
                confWidth = clubConfig.MEMBER_STATES_GUI_TABLE_WIDTH;
                confSort = clubConfig.MEMBER_STATES_GUI_TABLE_SORT;
                confUpDown = clubConfig.MEMBER_STATES_GUI_TABLE_UPDOWN;
                confVis = clubConfig.MEMBER_STATES_GUI_TABLE_VIS;
                confOrder = clubConfig.MEMBER_STATES_GUI_TABLE_ORDER;
                break;

            case MEMBER_PAYMENT_TYPE:
                confWidth = clubConfig.MEMBER_PAYMENT_TYPES_GUI_TABLE_WIDTH;
                confSort = clubConfig.MEMBER_PAYMENT_TYPES_GUI_TABLE_SORT;
                confUpDown = clubConfig.MEMBER_PAYMENT_TYPES_GUI_TABLE_UPDOWN;
                confVis = clubConfig.MEMBER_PAYMENT_TYPES_GUI_TABLE_VIS;
                confOrder = clubConfig.MEMBER_PAYMENT_TYPES_GUI_TABLE_ORDER;
                break;

            case ADD_MISSING_FEE:
                confWidth = clubConfig.ADD_MISSING_FEE_GUI_TABLE_WIDTH;
                confSort = clubConfig.ADD_MISSING_FEE_GUI_TABLE_SORT;
                confUpDown = clubConfig.ADD_MISSING_FEE_GUI_TABLE_UPDOWN;
                confVis = clubConfig.ADD_MISSING_FEE_GUI_TABLE_VIS;
                confOrder = clubConfig.ADD_MISSING_FEE_GUI_TABLE_ORDER;
                break;

            case FEE:
                confWidth = clubConfig.FEE_GUI_TABLE_WIDTH;
                confSort = clubConfig.FEE_GUI_TABLE_SORT;
                confUpDown = clubConfig.FEE_GUI_TABLE_UPDOWN;
                confVis = clubConfig.FEE_GUI_TABLE_VIS;
                confOrder = clubConfig.FEE_GUI_TABLE_ORDER;
                break;

            case PAY_FEE:
                confWidth = clubConfig.PAY_FEE_TABLE_WIDTH;
                confSort = clubConfig.PAY_FEE_TABLE_SORT;
                confUpDown = clubConfig.PAY_FEE_TABLE_UPDOWN;
                confVis = clubConfig.PAY_FEE_TABLE_VIS;
                confOrder = clubConfig.PAY_FEE_TABLE_ORDER;
                break;

            case FINANCE:
                confWidth = clubConfig.FINANCE_GUI_TABLE_WIDTH;
                confSort = clubConfig.FINANCE_GUI_TABLE_SORT;
                confUpDown = clubConfig.FINANCE_GUI_TABLE_UPDOWN;
                confVis = clubConfig.FINANCE_GUI_TABLE_VIS;
                confOrder = clubConfig.FINANCE_GUI_TABLE_ORDER;
                break;

            case FINANCE_ACCOUNT:
                confWidth = clubConfig.FINANCE_ACCOUNT_GUI_TABLE_WIDTH;
                confSort = clubConfig.FINANCE_ACCOUNT_GUI_TABLE_SORT;
                confUpDown = clubConfig.FINANCE_ACCOUNT_GUI_TABLE_UPDOWN;
                confVis = clubConfig.FINANCE_ACCOUNT_GUI_TABLE_VIS;
                confOrder = clubConfig.FINANCE_ACCOUNT_GUI_TABLE_ORDER;
                break;

            case FINANCE_CATEGORY:
                confWidth = clubConfig.FINANCE_CATEGORY_GUI_TABLE_WIDTH;
                confSort = clubConfig.FINANCE_CATEGORY_GUI_TABLE_SORT;
                confUpDown = clubConfig.FINANCE_CATEGORY_GUI_TABLE_UPDOWN;
                confVis = clubConfig.FINANCE_CATEGORY_GUI_TABLE_VIS;
                confOrder = clubConfig.FINANCE_CATEGORY_GUI_TABLE_ORDER;
                break;

            case TRANSACTION:
                confWidth = clubConfig.TRANSACTION_GUI_TABLE_WIDTH;
                confSort = clubConfig.TRANSACTION_GUI_TABLE_SORT;
                confUpDown = clubConfig.TRANSACTION_GUI_TABLE_UPDOWN;
                confVis = clubConfig.TRANSACTION_GUI_TABLE_VIS;
                confOrder = clubConfig.TRANSACTION_GUI_TABLE_ORDER;
                break;

            case FINANCE_REPORT:
                confWidth = clubConfig.FINANCE_REPORT_GUI_TABLE_WIDTH;
                confSort = clubConfig.FINANCE_REPORT_GUI_TABLE_SORT;
                confUpDown = clubConfig.FINANCE_REPORT_GUI_TABLE_UPDOWN;
                confVis = clubConfig.FINANCE_REPORT_GUI_TABLE_VIS;
                confOrder = clubConfig.FINANCE_REPORT_GUI_TABLE_ORDER;
                break;

            case CALCULATION_CATEGORY:
                confWidth = clubConfig.CALCULATION_CATEGORY_GUI_TABLE_WIDTH;
                confSort = clubConfig.CALCULATION_CATEGORY_GUI_TABLE_SORT;
                confUpDown = clubConfig.CALCULATION_CATEGORY_GUI_TABLE_UPDOWN;
                confVis = clubConfig.CALCULATION_CATEGORY_GUI_TABLE_VIS;
                confOrder = clubConfig.CALCULATION_CATEGORY_GUI_TABLE_ORDER;
                break;

            case CALCULATION_ACCOUNT:
                confWidth = clubConfig.CALCULATION_ACCOUNT_GUI_TABLE_WIDTH;
                confSort = clubConfig.CALCULATION_ACCOUNT_GUI_TABLE_SORT;
                confUpDown = clubConfig.CALCULATION_ACCOUNT_GUI_TABLE_UPDOWN;
                confVis = clubConfig.CALCULATION_ACCOUNT_GUI_TABLE_VIS;
                confOrder = clubConfig.CALCULATION_ACCOUNT_GUI_TABLE_ORDER;
                break;

        }
    }

    private void initColumn(TABLE eTable, TableView<PDataSample> table) {
        switch (eTable) {
            case MEMBER:
                tArray = new ClubTableMember(clubConfig).initColumn(table);
                break;

            case MEMBER_RATE:
                tArray = new ClubTableRateData(clubConfig).initColumn(table);
                break;

            case MEMBER_STATE:
                tArray = new ClubTableStateData(clubConfig).initColumn(table);
                break;

            case MEMBER_PAYMENT_TYPE:
                tArray = new ClubTablePaymentTypeData(clubConfig).initColumn(table);
                break;

            case FEE:
                tArray = new ClubTableFee(clubConfig).initColumn(table);
                break;

            case ADD_MISSING_FEE:
                tArray = new ClubTableAddMissingFee(clubConfig).initColumn(table);
                break;

            case PAY_FEE:
                tArray = new ClubTablePayFee(clubConfig).initColumn(table);
                break;

            case FINANCE:
                tArray = new ClubTableFinance(clubConfig).initColumn(table);
                break;

            case FINANCE_ACCOUNT:
                tArray = new ClubTableFinanceAccount(clubConfig).initColumn(table);
                break;

            case FINANCE_CATEGORY:
                tArray = new ClubTableFinanceCategory(clubConfig).initColumn(table);
                break;

            case TRANSACTION:
                tArray = new ClubTableTransaction(clubConfig).initColumn(table);
                break;

            case FINANCE_REPORT:
                tArray = new ClubTableFinanceReport(clubConfig).initColumn(table);
                break;

            case CALCULATION_CATEGORY:
                tArray = new ClubTableCalculation(clubConfig).initColumn(table, true);
                break;

            case CALCULATION_ACCOUNT:
                tArray = new ClubTableCalculation(clubConfig).initColumn(table, false);
                break;

        }
        String order = confOrder.get();
        String[] arOrder = order.split(",");
        if (confOrder.get().isEmpty() || arOrder.length != tArray.length) {
            // dann gibts keine Einstellungen oder die Anzahl der Spalten hat sich geändert
            for (TableColumn tc : tArray) {
                table.getColumns().add(tc);
            }
        } else {
            for (int i = 0; i < arOrder.length; ++i) {
                String s = arOrder[i];
                for (TableColumn tc : tArray) {
                    if (s.equals(tc.getText())) {
                        if (!table.getColumns().contains(tc)) {
                            //aus Fehlern wird man klug :(
                            table.getColumns().add(tc);
                        }
                    }
                }
            }
        }
        table.getColumns().stream().forEach(c -> c.setSortable(true));
        table.getColumns().stream().forEach(c -> c.setVisible(true));

    }

    public void saveTable(TableView ta, TABLE eTable) {
        // Tabellendaten sichern

        TableView<PDataSample> table = ta;

        initConf(eTable);
        maxSpalten = table.getColumns().size();

        table.getColumns().stream().forEach(c -> {
            width += c.getWidth() + ",";
            vis += String.valueOf(c.isVisible()) + ",";
        });

        table.getSortOrder().stream().forEach(so -> {
            sort += so.getText() + ",";
        });

        if (table.getSortOrder().size() > 0) {
            table.getSortOrder().stream().forEach(so -> {
                upDown += (so.getSortType().equals(TableColumn.SortType.ASCENDING) ? SORT_ASCENDING : SORT_DESCENDING) + ",";
            });
        }

        table.getColumns().stream().forEach(c -> {
            order += c.getText() + ",";
        });

        confWidth.set(width);
        confVis.set(vis);
        confSort.set(sort);
        confUpDown.set(upDown);
        confOrder.set(order);

    }


    public void resetTable(TableView ta, TABLE eTable) {
        reset(ta, eTable);
        setTable(ta, eTable);
    }

    private void reset(TableView ta, TABLE eTable) {
        initConf(eTable);
        maxSpalten = ta.getColumns().size();
        switch (eTable) {

            default:
                resetTable();

        }
    }


    public void setTable(TableView ta, TABLE eTable) {
        // Tabelle setzen
        TableView<PDataSample> table = ta;
        try {

            initConf(eTable);
            initColumn(eTable, table);


            maxSpalten = table.getColumns().size();

            breite = getDoubleArray(maxSpalten);
            visAr = getBoolArray(maxSpalten);

            if (!confWidth.get().isEmpty()) {
                width = confWidth.get();
                if (arrLesen(width, breite)) {
                    for (int i = 0; i < breite.length; ++i) {
                        table.getColumns().get(i).setPrefWidth(breite[i]);
                    }
                }
            }

            if (!confVis.get().isEmpty()) {
                vis = confVis.get();
                if (arrLesen(vis, visAr)) {
                    for (int i = 0; i < visAr.length; ++i) {
                        table.getColumns().get(i).setVisible(visAr[i]);
                    }
                }
            }

            if (!confSort.get().isEmpty()) {
                String sort = confSort.get();
                String[] arSort = sort.split(",");
                String sortUp = confUpDown.get();
                String[] arSortUp = sortUp.split(",");

                for (int i = 0; i < arSort.length; ++i) {
                    String s = arSort[i];
                    TableColumn co = table.getColumns().stream().filter(c -> c.getText().equals(s)).findFirst().get();
                    table.getSortOrder().add(co);

                    if (arSort.length == arSortUp.length) {
                        co.setSortType(arSortUp[i].equals(SORT_ASCENDING) ? TableColumn.SortType.ASCENDING : TableColumn.SortType.DESCENDING);
                    }
                }
            }
        } catch (final Exception ex) {
            PLog.errorLog(642103218, ex.getMessage());
            reset(ta, eTable);
        }

    }

    private boolean arrLesen(String s, double[] arr) {
        String sub;
        String[] sarr = s.split(",");
        if (maxSpalten != sarr.length) {
            // dann hat sich die Anzahl der Spalten der Tabelle geändert: Versionswechsel
            return false;
        } else {
            for (int i = 0; i < maxSpalten; i++) {
                try {
                    arr[i] = Double.parseDouble(sarr[i]);
                } catch (final Exception ex) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean arrLesen(String s, boolean[] arr) {
        String sub;
        String[] sarr = s.split(",");
        if (maxSpalten != sarr.length) {
            // dann hat sich die Anzahl der Spalten der Tabelle geändert: Versionswechsel
            return false;
        } else {
            for (int i = 0; i < maxSpalten; i++) {
                try {
                    arr[i] = Boolean.parseBoolean(sarr[i]);
                } catch (final Exception ex) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean arrLesen(String s, String[] arr) {
        arr = s.split(",");
        if (maxSpalten != arr.length) {
            // dann hat sich die Anzahl der Spalten der Tabelle geändert: Versionswechsel
            return false;
        }
        return true;
    }

    private int countString(String s) {
        int ret = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == ',') {
                ++ret;
            }
        }
        return ++ret;
    }

    private double[] getDoubleArray(int anzahl) {
        final double[] arr = new double[anzahl];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = -1;
        }
        return arr;
    }

    private boolean[] getBoolArray(int anzahl) {
        final boolean[] arr = new boolean[anzahl];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = true;
        }
        return arr;
    }

    private void resetTable() {
        String[] visArray = new String[maxSpalten];
        String set = "";

        for (int i = 0; i < maxSpalten; ++i) {
            visArray[i] = Boolean.TRUE.toString();
        }

        for (int i = 0; i < maxSpalten; ++i) {
            set += visArray[i] + ",";
        }

        confWidth.set("");
        confVis.set(set);
        confSort.set("");
        confUpDown.set("");
        confOrder.set("");
    }
}
