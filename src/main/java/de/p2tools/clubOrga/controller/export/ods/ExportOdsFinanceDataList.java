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


package de.p2tools.clubOrga.controller.export.ods;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.p2Lib.tools.log.PLog;
import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.style.Border;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Column;
import org.odftoolkit.simple.table.Table;

import java.io.File;
import java.util.List;

public class ExportOdsFinanceDataList {

    private List<FinanceData> list;
    private final ClubConfig clubConfig;

    private final int tableHeaderRow = 1;
    private final int tableContentRow = tableHeaderRow + 1;

    private final int startCol = 1;
    private final int tableAccountCol = startCol + 4;
    private int tableCategoryCol;

    int countFinanceAccount;
    int countFinanceCategory;

    private Border border = new Border(Color.BLACK, 1, StyleTypeDefinitions.SupportedLinearMeasure.PT);

    public ExportOdsFinanceDataList(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
    }

    public void exportFinanceData(List<FinanceData> list) {
        this.list = list;
        countFinanceAccount = clubConfig.financeAccountDataList.size();
        countFinanceCategory = clubConfig.financeCategoryDataList.size();
        tableCategoryCol = tableAccountCol + 1 + countFinanceAccount;

        Thread th = new Thread(() -> {
            try {
                buildTableDocument();
            } catch (Exception ex) {
                PLog.errorLog(987451201, ex, "Thread: newsletterFinanceData");
            }
        });
        th.setName("newsletterFinanceData");
        th.start();
    }

    private void buildTableDocument() {
        try {

            SpreadsheetDocument document = SpreadsheetDocument.newSpreadsheetDocument();

            Table table = document.getTableList().get(0);
            table.setTableName("Finanzen");

            writeHeader(table);

            int row = tableContentRow;
            for (; row < list.size() + tableContentRow; ++row) {
                final int listRow = row - tableContentRow;
                boolean last = row == (list.size() + tableContentRow - 1);

                addInfoCell(table, row, startCol, list.get(listRow), last);
//                addCell(table, row, tableAccountCol, list.get(listRow), true, last);
                addCell(table, row, tableAccountCol + countFinanceAccount + 1, list.get(listRow), last);
            }

            setColumnWidth(table);
            writeBottom(table, row);

            document.save(new File("/home/emil/Desktop/club/HelloWorld.ods")); //todo
        } catch (Exception e) {
            System.err.println("buildTableDocument");
        }
    }

    private void setColumnWidth(Table table) {
        Column column;
        for (int i = 0; i < startCol; ++i) {
            // sind Spalten vor der Tabelle
            column = table.getColumnByIndex(i);
            column.setWidth(5);
        }
        // und das sind die 2 Leerspalten
        column = table.getColumnByIndex(tableAccountCol - 1);
        column.setWidth(5);
        column = table.getColumnByIndex(tableAccountCol + countFinanceAccount);
        column.setWidth(5);
    }

    private void writeBottom(Table table, int row) {
        // row ist bereits die Zeile (0 ... row) NACH der Tabelle
        Color color = Color.SILVER;
        Cell cell;

        final String startSumRow = tableContentRow + 1 + ""; // die zählen ab 1... !!!
        final String endSumRow = row + ""; // die zählen ab 1... !!

        final int sumRow = row + 3; // + eine Leerzeile
        final String sumRowStr = row + 2 + "";

        row = 1 + row;
        int start = tableAccountCol;
        int stop = tableAccountCol + countFinanceAccount;
        for (int col = start; col < stop; ++col) {
            cell = table.getCellByPosition(col, row);
            final String colStr = ExportFinanceFactory.getColumn(col);
            cell.setFormula("of:=SUM([." + colStr + startSumRow + ":." + colStr + endSumRow + "])");
            cell.setBorders(StyleTypeDefinitions.CellBordersType.ALL_FOUR, border);
            cell.setCellBackgroundColor(color);
        }

        cell = table.getCellByPosition(start, sumRow);
        String colStrStart = ExportFinanceFactory.getColumn(start);
        String colStrStop = ExportFinanceFactory.getColumn(stop - 1);

        cell.setFormula("of:=SUM([." + colStrStart + sumRowStr + ":." + colStrStop + sumRowStr + "])");
        cell.setBorders(StyleTypeDefinitions.CellBordersType.ALL_FOUR, border);
        cell.setCellBackgroundColor(color);

        start = tableCategoryCol;
        stop = tableCategoryCol + countFinanceCategory;
        for (int col = start; col < stop; ++col) {
            cell = table.getCellByPosition(col, row);
            final String colStr = ExportFinanceFactory.getColumn(col);
            cell.setFormula("of:=SUM([." + colStr + startSumRow + ":." + colStr + endSumRow + "])");
            cell.setBorders(StyleTypeDefinitions.CellBordersType.ALL_FOUR, border);
            cell.setCellBackgroundColor(color);
        }

        cell = table.getCellByPosition(start, sumRow);
        colStrStart = ExportFinanceFactory.getColumn(start);
        colStrStop = ExportFinanceFactory.getColumn(stop - 1);

        cell.setFormula("of:=SUM([." + colStrStart + sumRowStr + ":." + colStrStop + sumRowStr + "])");
        cell.setBorders(StyleTypeDefinitions.CellBordersType.ALL_FOUR, border);
        cell.setCellBackgroundColor(color);
    }

    private void writeHeader(Table table) {

        Color color = Color.SILVER;

        int countFinanceAccount = clubConfig.financeAccountDataList.size();
        int countFinanceCategory = clubConfig.financeCategoryDataList.size();
        Border border = new Border(Color.BLACK, 1, StyleTypeDefinitions.SupportedLinearMeasure.PT);

        Column column = table.getColumnByIndex(startCol);
        column.setWidth(30);
        column = table.getColumnByIndex(startCol + 1);
        column.setWidth(30);
        column = table.getColumnByIndex(startCol + 2);
        column.setWidth(30);


        // Spalte BelegNr, Geschäftsjahr, Datum
        Cell cellHeader = table.getCellByPosition(startCol, tableHeaderRow);
        cellHeader.setStringValue(FinanceFieldNames.BELEG_NR);
        cellHeader.setBorders(StyleTypeDefinitions.CellBordersType.ALL_FOUR, border);
        cellHeader.setCellBackgroundColor(color);

        cellHeader = table.getCellByPosition(startCol + 1, tableHeaderRow);
        cellHeader.setStringValue(FinanceFieldNames.GESCHAEFTSJAHR);
        cellHeader.setBorders(StyleTypeDefinitions.CellBordersType.ALL_FOUR, border);
        cellHeader.setCellBackgroundColor(color);

        cellHeader = table.getCellByPosition(startCol + 2, tableHeaderRow);
        cellHeader.setStringValue(FinanceFieldNames.BUCHUNGS_DATUM);
        cellHeader.setBorders(StyleTypeDefinitions.CellBordersType.ALL_FOUR, border);
        cellHeader.setCellBackgroundColor(color);

        int start = tableAccountCol, stop = tableAccountCol + countFinanceAccount;
        for (int col = start; col < stop; ++col) {
            Cell cell = table.getCellByPosition(col, tableHeaderRow);
            cell.setStringValue(clubConfig.financeAccountDataList.get(col - start).getKonto());
            cell.setBorders(StyleTypeDefinitions.CellBordersType.ALL_FOUR, border);
            cell.setCellBackgroundColor(color);
        }

        start = tableCategoryCol;
        stop = tableCategoryCol + countFinanceCategory;
        for (int col = start; col < stop; ++col) {
            Cell cell = table.getCellByPosition(col, tableHeaderRow);
            cell.setStringValue(clubConfig.financeCategoryDataList.get(col - start).getKategorie());
            cell.setBorders(StyleTypeDefinitions.CellBordersType.ALL_FOUR, border);
            cell.setCellBackgroundColor(color);
        }

    }

    private void addInfoCell(Table table, int row, int startCol, FinanceData financeData, boolean last) {

        Cell cell = table.getCellByPosition(startCol, row);
        cell.setStringValue(financeData.getBelegNr());
        cell.setBorders(StyleTypeDefinitions.CellBordersType.LEFT_RIGHT, border);
        if (last) {
            cell.setBorders(StyleTypeDefinitions.CellBordersType.BOTTOM, border);
        }

        cell = table.getCellByPosition(startCol + 1, row);
        cell.setStringValue(financeData.getGeschaeftsJahr() + "");
        cell.setBorders(StyleTypeDefinitions.CellBordersType.LEFT_RIGHT, border);
        if (last) {
            cell.setBorders(StyleTypeDefinitions.CellBordersType.BOTTOM, border);
        }

        cell = table.getCellByPosition(startCol + 2, row);
        cell.setStringValue(financeData.getBuchungsDatum().toString());
        cell.setBorders(StyleTypeDefinitions.CellBordersType.LEFT_RIGHT, border);
        if (last) {
            cell.setBorders(StyleTypeDefinitions.CellBordersType.BOTTOM, border);
        }
    }

    private void addCell(Table table, int row, int startCol, FinanceData financeData, boolean last) {

        double[] arr;
//        if (account) {
//            arr = financeData.getSumKontoKategorieArray(true);
//        } else {
        arr = financeData.getSumKategorieArray();
//        }

        int i = 0;
        for (; i < arr.length; ++i) {
            Cell cell = table.getCellByPosition(startCol + i, row);
            cell.setBorders(StyleTypeDefinitions.CellBordersType.LEFT_RIGHT, border);
            if (last) {
                cell.setBorders(StyleTypeDefinitions.CellBordersType.BOTTOM, border);
            }
            if (arr[i] != 0) {
                cell.setDoubleValue(arr[i]);
            }
        }
    }
}
