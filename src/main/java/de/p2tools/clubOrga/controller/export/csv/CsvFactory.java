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


package de.p2tools.clubOrga.controller.export.csv;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.clubOrga.data.financeData.*;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberFactory;
import de.p2tools.clubOrga.data.memberData.MemberFieldNames;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.memberData.stateData.StateData;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.log.PLog;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CsvFactory {
    private static final DecimalFormat DF;
    private static final NumberFormat NF;
    private static final Locale locale = Locale.GERMAN;

    static {
        NF = NumberFormat.getNumberInstance(locale);
        DF = new DecimalFormat("###,##0.00");
    }

    private CsvFactory() {
    }

    public static boolean importMember(ClubConfig clubConfig, Path importPath) {

        try (Reader in = new InputStreamReader(new FileInputStream(importPath.toFile()))) {
//            Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withSkipHeaderRecord(false).parse(in);
//            Set<String> headers = records.iterator().next().toMap().keySet();

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                getMember(clubConfig, record);
            }

        } catch (Exception ex) {
            PLog.errorLog(978451201, ex, "import member");
            return false;
        }

        return true;
    }

    private static void getMember(ClubConfig clubConfig, CSVRecord record) {
        MemberData memberData = MemberFactory.getNewMemberData(clubConfig, "", true);
        Config[] confArr = memberData.getConfigsArr();
        boolean found = false;

        for (Config configExtra : confArr) {
            if (configExtra.getName().equals(MemberFieldNames.ID) ||
                    configExtra.getName().equals(MemberFieldNames.NR) ||
                    configExtra.getName().equals(MemberFieldNames.ERSTELLDATUM)) {
                continue;
            }

            final String name = configExtra.getName();
            try {
                final String csvValue = record.get(name).trim();
                if (csvValue != null && !csvValue.isEmpty()) {
                    found = true;

                    if (name.equals(MemberFieldNames.BEITRAG)) {
                        long l = 0;
                        try {
                            Double d = NF.parse(csvValue).doubleValue();
                            l = (long) (100 * d);
                        } catch (ParseException ignored) {
                            l = 0;
                            PLog.errorLog(641201478, "Kann Beitrag nicht parsen: " + csvValue);
                        }
                        configExtra.setActValue(l + "");

                    } else if (name.equals(MemberFieldNames.STATUS)) {
                        long id = clubConfig.stateDataList.get(0).getId();
                        for (StateData stateData : clubConfig.stateDataList) {
                            if (csvValue.equals(stateData.getName())) {
                                id = stateData.getId();
                            }
                        }
                        configExtra.setActValue(id + "");

                    } else if (name.equals(MemberFieldNames.BEITRAGSSATZ)) {
                        long id = clubConfig.feeRateDataList.get(0).getId();
                        for (FeeRateData feeRateData : clubConfig.feeRateDataList) {
                            if (csvValue.equals(feeRateData.getName())) {
                                id = feeRateData.getId();
                            }
                        }
                        configExtra.setActValue(id + "");

                    } else if (name.equals(MemberFieldNames.ZAHLART)) {
                        long id = clubConfig.paymentTypeDataList.get(0).getId();
                        for (PaymentTypeData paymentTypeData : clubConfig.paymentTypeDataList) {
                            if (csvValue.equals(paymentTypeData.getName())) {
                                id = paymentTypeData.getId();
                            }
                        }
                        configExtra.setActValue(id + "");

                    } else {
                        configExtra.setActValue(csvValue);
                    }
                }
            } catch (IllegalArgumentException ex) {
                PLog.errorLog(316497821, ex, "no coloum of header: " + name);
            }
        }
        if (found) {
            memberData.initAfterLoad();
            memberData.setNr(clubConfig.memberDataList.getNextNr());
            clubConfig.memberDataList.add(memberData);
        }
    }

    public static boolean exportMember(List<MemberData> memberDataList, Path exportPath) {
        ArrayList<String> list = new ArrayList<>();
        if (memberDataList.isEmpty()) {
            return true;
        }

        // header
        for (Config configExtra : memberDataList.get(0).getConfigsForNewsletter(true)) {
            if (configExtra.getName().equals(MemberFieldNames.ID)) {
                continue;
            }
            list.add(configExtra.getName());
        }
        String[] HEADERS = list.toArray(new String[]{});

        // data
        try (FileWriter out = new FileWriter(exportPath.toFile());
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {

            for (MemberData memberData : memberDataList) {
                list.clear();
                for (Config configExtra : memberData.getConfigsForNewsletter(true)) {
                    if (configExtra.getName().equals(MemberFieldNames.ID)) {
                        continue;
                    }
                    if (configExtra.getName().equals(MemberFieldNames.BEITRAG)) {
                        list.add(DF.format(1.0 * memberData.getBeitrag() / 100));
                    } else {
                        list.add(configExtra.getActValueString());
                    }
                }
                Object[] sArr = list.toArray(new String[]{});
                printer.printRecord(sArr);
            }

        } catch (Exception ex) {
            PLog.errorLog(945120145, ex, "export csv");
            return false;
        }

        return true;
    }

    public static boolean exportFinances(List<FinanceData> financeDataList, Path exportPath) {
        if (financeDataList == null || financeDataList.isEmpty()) {
            return false;
        }
        ArrayList<String> list = new ArrayList<>();
        String[] HEADERS = getFinanceDataHeaderArr(financeDataList.get(0));

        try (FileWriter out = new FileWriter(exportPath.toFile());
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {

            for (FinanceData financeData : financeDataList) {
//                ArrayList<String> line = financeData.getDataRow();
//                printer.printRecord(line);

                list.clear();
                for (Config configExtra : financeData.getConfigsArr()) {
                    if (configExtra.getName().equals(FinanceFieldNames.ID)) {
                        continue;
                    }
                    if (configExtra.getName().equals(FinanceFieldNames.GESAMTBETRAG)) {
                        list.add(DF.format(1.0 * financeData.getGesamtbetrag() / 100));
                    } else if (configExtra.getName().equals(FinanceFieldNames.KONTO)) {
                        list.add(financeData.getFinanceAccountData().getKonto());
                    } else {
                        list.add(configExtra.getActValueString());
                    }

                }
                Object[] sArr = list.toArray(new String[]{});
                printer.printRecord(sArr);
            }

        } catch (Exception ex) {
            PLog.errorLog(645120123, ex, "export csv");
            return false;
        }

        return true;
    }

    private static String[] getFinanceDataHeaderArr(FinanceData financeData) {
        ArrayList<String> headers = new ArrayList<>();

        for (Config configExtra : financeData.getConfigsArr()) {
            if (configExtra.getName().equals(FinanceFieldNames.ID)) {
                continue;
            }
            headers.add(configExtra.getName());
        }

        String[] HEADERS = headers.toArray(new String[]{});
        return HEADERS;
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
        headers.add(FinanceFieldNames.BUCHUNGS_DATUM);
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
        dataRow.add(financeReportData.getNr() + "");
        dataRow.add(financeReportData.getBelegNr());
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
