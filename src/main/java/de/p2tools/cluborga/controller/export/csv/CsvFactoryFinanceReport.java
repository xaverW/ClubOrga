/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.cluborga.controller.export.csv;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.data.financeData.FinanceFieldNames;
import de.p2tools.cluborga.data.financeData.FinanceReportData;
import de.p2tools.cluborga.data.financeData.FinanceReportDataList;
import de.p2tools.cluborga.data.financeData.FinanceReportFactory;
import de.p2tools.p2lib.tools.log.PLog;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CsvFactoryFinanceReport {
    private static final DecimalFormat DF;
    private static final NumberFormat NF;
    private static final Locale locale = Locale.GERMAN;

    static {
        NF = NumberFormat.getNumberInstance(locale);
        DF = new DecimalFormat("###,##0.00");
    }

    private CsvFactoryFinanceReport() {
    }

    public static boolean exportFinancesReport(ClubConfig clubConfig, List<FinanceReportData> financeReportDataList,
                                               Path exportPath) {

        if (financeReportDataList == null || financeReportDataList.isEmpty()) {
            return false;
        }

        String[] HEADERS = getFinanceReportDataHeaderArr(clubConfig);

        try (FileWriter out = new FileWriter(exportPath.toFile());
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {

            for (FinanceReportData financeReportData : financeReportDataList) {
                final ArrayList<String> line = getDataRow(financeReportData);
                printer.printRecord(line);
            }

        } catch (Exception ex) {
            PLog.errorLog(945120145, ex, "export csv");
            return false;
        }

        return true;
    }

    private static String[] getFinanceReportDataHeaderArr(ClubConfig clubConfig) {
        ArrayList<String> headers = new ArrayList<>();
        headers.add(FinanceFieldNames.NR);
        headers.add(FinanceFieldNames.BELEG_NR);
        headers.add(FinanceFieldNames.GESAMTBETRAG);
        headers.add(FinanceFieldNames.GESCHAEFTSJAHR);
        headers.add(FinanceFieldNames.BUCHUNGSDATUM);
        headers.add(FinanceFieldNames.TEXT);

        FinanceReportDataList financeReportDataList = clubConfig.financeReportDataList;
        headers.addAll(financeReportDataList.getAccounts());
        headers.addAll(financeReportDataList.getCategories());

        String[] HEADERS = headers.toArray(new String[]{});
        for (int i = 0; i < headers.size(); ++i) {
            HEADERS[i] = HEADERS[i].replace(FinanceReportFactory.KONTO, "");
            HEADERS[i] = HEADERS[i].replace(FinanceReportFactory.KATEGORIE, "");
        }
        return HEADERS;
    }

    private static ArrayList<String> getDataRow(FinanceReportData financeReportData) {
        ArrayList<String> dataRow = new ArrayList<>();
        dataRow.add(financeReportData.getNo() + "");
        dataRow.add(financeReportData.getReceiptNo());
        dataRow.add(DF.format(1.0 * financeReportData.getGesamtbetrag() / 100));
        dataRow.add(financeReportData.getGeschaeftsJahr() + "");
        dataRow.add(financeReportData.getBuchungsDatum().toString());
        dataRow.add(financeReportData.getText());

        for (int i = 0; i < financeReportData.getAccountList().size(); i++) {
            final long l = financeReportData.getAccountList().get(i).getBetrag();
            if (l == 0) {
                dataRow.add("");
            } else {
                dataRow.add(DF.format(1.0 * l / 100));
            }
        }

        for (int i = 0; i < financeReportData.getCategoryList().size(); i++) {
            final long l = financeReportData.getCategoryList().get(i).getBetrag();
            if (l == 0) {
                dataRow.add("");
            } else {
                dataRow.add(DF.format(1.0 * l / 100));
            }
        }

        return dataRow;
    }
}
