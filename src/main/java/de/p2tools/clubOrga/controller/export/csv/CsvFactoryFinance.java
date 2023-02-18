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
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.clubOrga.data.financeData.TransactionDataListBase;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.log.PLog;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CsvFactoryFinance {
    private static final DecimalFormat DF;
    private static final NumberFormat NF;
    private static final Locale locale = Locale.GERMAN;

    static {
        NF = NumberFormat.getNumberInstance(locale);
        DF = new DecimalFormat("###,##0.00");
    }

    private CsvFactoryFinance() {
    }

    public static boolean exportFinances(ClubConfig clubConfig, List<FinanceData> financeDataList, Path exportPath) {
        if (financeDataList == null || financeDataList.isEmpty()) {
            return false;
        }
        String[] headerArr = getFinanceDataHeaderArr(clubConfig, financeDataList.get(0));


        ArrayList<String> list = new ArrayList<>();
        try (FileWriter out = new FileWriter(exportPath.toFile());
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headerArr))) {

            for (FinanceData financeData : financeDataList) {
                list.clear();
                for (Config Config : financeData.getConfigsArr()) {
                    if (Config.getName().equals(FinanceFieldNames.ID)) {
                        continue;
                    }
                    addFinanceToList(clubConfig, list, Config, financeData);
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

    private static String[] getFinanceDataHeaderArr(ClubConfig clubConfig, FinanceData financeData) {
        ArrayList<String> list = new ArrayList<>();

        for (Config config : financeData.getConfigsArr()) {
            if (config.getName().equals(FinanceFieldNames.ID)) {
                continue;
            }
            if (config.getName().equals(TransactionDataListBase.TAG)) {
                continue;
            }

            if (!clubConfig.FINANCE_EXPORT_ALL.get()) {
                if (config.getName().equals(FinanceFieldNames.NR) && clubConfig.FINANCE_EXPORT_DATA_Nr.get() ||
                        config.getName().equals(FinanceFieldNames.BELEG_NR) && clubConfig.FINANCE_EXPORT_DATA_BelegNr.get() ||
                        config.getName().equals(FinanceFieldNames.GESAMTBETRAG) && clubConfig.FINANCE_EXPORT_DATA_Gesamtbetrag.get() ||
                        config.getName().equals(FinanceFieldNames.KONTO) && clubConfig.FINANCE_EXPORT_DATA_Konto.get() ||
                        config.getName().equals(FinanceFieldNames.KATEGORIE) && clubConfig.FINANCE_EXPORT_DATA_Kategorie.get() ||
                        config.getName().equals(FinanceFieldNames.GESCHAEFTSJAHR) && clubConfig.FINANCE_EXPORT_DATA_Geschäftsjahr.get() ||
                        config.getName().equals(FinanceFieldNames.BUCHUNGSDATUM) && clubConfig.FINANCE_EXPORT_DATA_Buchungsdatum.get() ||
                        config.getName().equals(FinanceFieldNames.ERSTELLDATUM) && clubConfig.FINANCE_EXPORT_DATA_Erstelldatum.get() ||
                        config.getName().equals(FinanceFieldNames.TEXT) && clubConfig.FINANCE_EXPORT_DATA_Text.get()) {
                    list.add(config.getName());
                }
            } else {
                //export all
                list.add(config.getName());
            }
        }

        String[] HEADERS = list.toArray(new String[]{});
        return HEADERS;
    }

    private static void addFinanceToList(ClubConfig clubConfig, ArrayList<String> list, Config configExtra, FinanceData financeData) {
        if (!clubConfig.FINANCE_EXPORT_ALL.get()) {
            if (configExtra.getName().equals(FinanceFieldNames.GESAMTBETRAG) && clubConfig.FINANCE_EXPORT_DATA_Gesamtbetrag.get()) {
                list.add(DF.format(1.0 * financeData.getGesamtbetrag() / 100));
            } else if (configExtra.getName().equals(FinanceFieldNames.KONTO) && clubConfig.FINANCE_EXPORT_DATA_Konto.get()) {
                list.add(financeData.getFinanceAccountData().getName());

            } else if (configExtra.getName().equals(FinanceFieldNames.NR) && clubConfig.FINANCE_EXPORT_DATA_Nr.get() ||
                    configExtra.getName().equals(FinanceFieldNames.BELEG_NR) && clubConfig.FINANCE_EXPORT_DATA_BelegNr.get() ||
                    configExtra.getName().equals(FinanceFieldNames.KATEGORIE) && clubConfig.FINANCE_EXPORT_DATA_Kategorie.get() ||
                    configExtra.getName().equals(FinanceFieldNames.GESCHAEFTSJAHR) && clubConfig.FINANCE_EXPORT_DATA_Geschäftsjahr.get() ||
                    configExtra.getName().equals(FinanceFieldNames.BUCHUNGSDATUM) && clubConfig.FINANCE_EXPORT_DATA_Buchungsdatum.get() ||
                    configExtra.getName().equals(FinanceFieldNames.ERSTELLDATUM) && clubConfig.FINANCE_EXPORT_DATA_Erstelldatum.get() ||
                    configExtra.getName().equals(FinanceFieldNames.TEXT) && clubConfig.FINANCE_EXPORT_DATA_Text.get()) {
                list.add(configExtra.getActValueString());
            }
        } else {
            //export all
            if (configExtra.getName().equals(FinanceFieldNames.GESAMTBETRAG)) {
                list.add(DF.format(1.0 * financeData.getGesamtbetrag() / 100));
            } else if (configExtra.getName().equals(FinanceFieldNames.KONTO)) {
                list.add(financeData.getFinanceAccountData().getName());
            } else {
                list.add(configExtra.getActValueString());
            }
        }
    }
}
