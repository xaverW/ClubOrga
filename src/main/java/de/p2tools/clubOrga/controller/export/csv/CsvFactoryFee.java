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
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.feeData.FeeFieldNames;
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

public class CsvFactoryFee {
    private static final DecimalFormat DF;
    private static final NumberFormat NF;
    private static final Locale locale = Locale.GERMAN;

    static {
        NF = NumberFormat.getNumberInstance(locale);
        DF = new DecimalFormat("###,##0.00");
    }

    private CsvFactoryFee() {
    }

    public static boolean exportFee(ClubConfig clubConfig, List<FeeData> feeDataList, Path exportPath) {
        ArrayList<String> list = new ArrayList<>();
        if (feeDataList.isEmpty()) {
            return true;
        }

        // header
        for (Config configExtra : feeDataList.get(0).getConfigsForNewsletter()) {
            if (configExtra.getName().equals(FeeFieldNames.ID)) {
                continue;
            }
            addFeeNameToList(clubConfig, list, configExtra);
        }
        String[] HEADERS = list.toArray(new String[]{});

        // data
        try (FileWriter out = new FileWriter(exportPath.toFile());
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {

            for (FeeData feeData : feeDataList) {
                list.clear();
                for (Config configExtra : feeData.getConfigsForNewsletter()) {
                    if (configExtra.getName().equals(FeeFieldNames.ID)) {
                        continue;
                    }

                    addFeeToList(clubConfig, list, configExtra, feeData);
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

    private static void addFeeNameToList(ClubConfig clubConfig, ArrayList<String> list, Config configExtra) {
        if (!clubConfig.FEE_EXPORT_ALL.get()) {
            if (configExtra.getName().equals(FeeFieldNames.NR) && clubConfig.FEE_EXPORT_DATA_Nr.get() ||
                    configExtra.getName().equals(FeeFieldNames.MEMBER_NO) && clubConfig.FEE_EXPORT_DATA_MemberNr.get() ||
                    configExtra.getName().equals(FeeFieldNames.MEMBER_NAME) && clubConfig.FEE_EXPORT_DATA_MemberName.get() ||
                    configExtra.getName().equals(FeeFieldNames.BETRAG) && clubConfig.FEE_EXPORT_DATA_Betrag.get() ||
                    configExtra.getName().equals(FeeFieldNames.BETRAG_IN_WORDS) && clubConfig.FEE_EXPORT_DATA_BetragInWords.get() ||
                    configExtra.getName().equals(FeeFieldNames.JAHR) && clubConfig.FEE_EXPORT_DATA_Jahr.get() ||
                    configExtra.getName().equals(FeeFieldNames.ZAHLART) && clubConfig.FEE_EXPORT_DATA_Zahlart.get() ||
                    configExtra.getName().equals(FeeFieldNames.BEZAHLT) && clubConfig.FEE_EXPORT_DATA_Bezahlt.get() ||
                    configExtra.getName().equals(FeeFieldNames.RECHNUNG) && clubConfig.FEE_EXPORT_DATA_Rechnung.get() ||
                    configExtra.getName().equals(FeeFieldNames.SPENDEN_Q) && clubConfig.FEE_EXPORT_DATA_SpendenQ.get() ||
                    configExtra.getName().equals(FeeFieldNames.ERSTELLDATUM) && clubConfig.FEE_EXPORT_DATA_Erstelldatum.get() ||
                    configExtra.getName().equals(FeeFieldNames.TEXT) && clubConfig.FEE_EXPORT_DATA_Text.get()) {
                list.add(configExtra.getName());
            }
        } else {
            //export all
            list.add(configExtra.getName());
        }
    }


    private static void addFeeToList(ClubConfig clubConfig, ArrayList<String> list, Config configExtra, FeeData feeData) {
        if (!clubConfig.FEE_EXPORT_ALL.get()) {
            if (configExtra.getName().equals(FeeFieldNames.BETRAG) && clubConfig.FEE_EXPORT_DATA_Betrag.get()) {
                list.add(DF.format(1.0 * feeData.getBetrag() / 100));
            } else if (configExtra.getName().equals(FeeFieldNames.NR) && clubConfig.FEE_EXPORT_DATA_Nr.get() ||
                    configExtra.getName().equals(FeeFieldNames.MEMBER_NO) && clubConfig.FEE_EXPORT_DATA_MemberNr.get() ||
                    configExtra.getName().equals(FeeFieldNames.MEMBER_NAME) && clubConfig.FEE_EXPORT_DATA_MemberName.get() ||
                    configExtra.getName().equals(FeeFieldNames.BETRAG_IN_WORDS) && clubConfig.FEE_EXPORT_DATA_BetragInWords.get() ||
                    configExtra.getName().equals(FeeFieldNames.JAHR) && clubConfig.FEE_EXPORT_DATA_Jahr.get() ||
                    configExtra.getName().equals(FeeFieldNames.ZAHLART) && clubConfig.FEE_EXPORT_DATA_Zahlart.get() ||
                    configExtra.getName().equals(FeeFieldNames.BEZAHLT) && clubConfig.FEE_EXPORT_DATA_Bezahlt.get() ||
                    configExtra.getName().equals(FeeFieldNames.RECHNUNG) && clubConfig.FEE_EXPORT_DATA_Rechnung.get() ||
                    configExtra.getName().equals(FeeFieldNames.SPENDEN_Q) && clubConfig.FEE_EXPORT_DATA_SpendenQ.get() ||
                    configExtra.getName().equals(FeeFieldNames.ERSTELLDATUM) && clubConfig.FEE_EXPORT_DATA_Erstelldatum.get() ||
                    configExtra.getName().equals(FeeFieldNames.TEXT) && clubConfig.FEE_EXPORT_DATA_Text.get()) {
                list.add(configExtra.getActValueString());
            }
        } else {
            //export all
            if (configExtra.getName().equals(FeeFieldNames.BETRAG)) {
                list.add(DF.format(1.0 * feeData.getBetrag() / 100));
            } else {
                list.add(configExtra.getActValueString());
            }
        }
    }
}
