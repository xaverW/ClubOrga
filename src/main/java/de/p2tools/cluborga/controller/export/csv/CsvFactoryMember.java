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
import de.p2tools.cluborga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.cluborga.data.feeData.paymentType.PaymentTypeData;
import de.p2tools.cluborga.data.memberData.MemberData;
import de.p2tools.cluborga.data.memberData.MemberFactory;
import de.p2tools.cluborga.data.memberData.MemberFieldNames;
import de.p2tools.cluborga.data.memberData.stateData.StateData;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.tools.log.PLog;
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

public class CsvFactoryMember {
    private static final DecimalFormat DF;
    private static final NumberFormat NF;
    private static final Locale locale = Locale.GERMAN;

    static {
        NF = NumberFormat.getNumberInstance(locale);
        DF = new DecimalFormat("###,##0.00");
    }

    private CsvFactoryMember() {
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
            memberData.setNo(clubConfig.memberDataList.getNextNr());
            clubConfig.memberDataList.add(memberData);
        }
    }

    public static boolean exportMember(ClubConfig clubConfig, List<MemberData> memberDataList, Path exportPath) {
        ArrayList<String> list = new ArrayList<>();
        if (memberDataList.isEmpty()) {
            return true;
        }

        // header
        for (Config configExtra : memberDataList.get(0).getConfigsForNewsletter(true)) {
            if (configExtra.getName().equals(MemberFieldNames.ID)) {
                continue;
            }
            addMemberNameToList(clubConfig, list, configExtra);
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

                    addMemberToList(clubConfig, list, configExtra, memberData);
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

    private static void addMemberNameToList(ClubConfig clubConfig, ArrayList<String> list, Config configExtra) {
        if (!clubConfig.MEMBER_EXPORT_ALL.get()) {
            if (configExtra.getName().equals(MemberFieldNames.NR) && clubConfig.MEMBER_EXPORT_DATA_Nr.get() ||
                    configExtra.getName().equals(MemberFieldNames.NACHNAME) && clubConfig.MEMBER_EXPORT_DATA_Nachname.get() ||
                    configExtra.getName().equals(MemberFieldNames.VORNAME) && clubConfig.MEMBER_EXPORT_DATA_Vorname.get() ||
                    configExtra.getName().equals(MemberFieldNames.ANREDE) && clubConfig.MEMBER_EXPORT_DATA_Anrede.get() ||
                    configExtra.getName().equals(MemberFieldNames.EMAIL) && clubConfig.MEMBER_EXPORT_DATA_Email.get() ||
                    configExtra.getName().equals(MemberFieldNames.TELEFON) && clubConfig.MEMBER_EXPORT_DATA_Telefon.get() ||
                    configExtra.getName().equals(MemberFieldNames.STRASSE) && clubConfig.MEMBER_EXPORT_DATA_Strasse.get() ||
                    configExtra.getName().equals(MemberFieldNames.PLZ) && clubConfig.MEMBER_EXPORT_DATA_PLZ.get() ||
                    configExtra.getName().equals(MemberFieldNames.ORT) && clubConfig.MEMBER_EXPORT_DATA_Ort.get() ||
                    configExtra.getName().equals(MemberFieldNames.LAND) && clubConfig.MEMBER_EXPORT_DATA_Land.get() ||
                    configExtra.getName().equals(MemberFieldNames.STATUS) && clubConfig.MEMBER_EXPORT_DATA_Status.get() ||
                    configExtra.getName().equals(MemberFieldNames.BEITRAG) && clubConfig.MEMBER_EXPORT_DATA_Beitrag.get() ||
                    configExtra.getName().equals(MemberFieldNames.BEITRAGSSATZ) && clubConfig.MEMBER_EXPORT_DATA_Beitragssatz.get() ||
                    configExtra.getName().equals(MemberFieldNames.BANK) && clubConfig.MEMBER_EXPORT_DATA_Bank.get() ||
                    configExtra.getName().equals(MemberFieldNames.IBAN) && clubConfig.MEMBER_EXPORT_DATA_Iban.get() ||
                    configExtra.getName().equals(MemberFieldNames.BIC) && clubConfig.MEMBER_EXPORT_DATA_Bic.get() ||
                    configExtra.getName().equals(MemberFieldNames.KONTOINHABER) && clubConfig.MEMBER_EXPORT_DATA_Kontoinhaber.get() ||
                    configExtra.getName().equals(MemberFieldNames.ZAHLART) && clubConfig.MEMBER_EXPORT_DATA_Zahlart.get() ||
                    configExtra.getName().equals(MemberFieldNames.ZAHLUNGSBEGINN) && clubConfig.MEMBER_EXPORT_DATA_Zahlungsbeginn.get() ||
                    configExtra.getName().equals(MemberFieldNames.SEPABEGINN) && clubConfig.MEMBER_EXPORT_DATA_Sepabeginn.get() ||
                    configExtra.getName().equals(MemberFieldNames.BEITRITT) && clubConfig.MEMBER_EXPORT_DATA_Beitritt.get()) {
                list.add(configExtra.getName());
            }
        } else {
            //export all
            list.add(configExtra.getName());
        }
    }

    private static void addMemberToList(ClubConfig clubConfig, ArrayList<String> list, Config configExtra, MemberData memberData) {
        if (!clubConfig.MEMBER_EXPORT_ALL.get()) {
            if (configExtra.getName().equals(MemberFieldNames.BEITRAG) && clubConfig.MEMBER_EXPORT_DATA_Beitrag.get()) {
                list.add(DF.format(1.0 * memberData.getBeitrag() / 100));
            } else if (configExtra.getName().equals(MemberFieldNames.NR) && clubConfig.MEMBER_EXPORT_DATA_Nr.get() ||
                    configExtra.getName().equals(MemberFieldNames.NACHNAME) && clubConfig.MEMBER_EXPORT_DATA_Nachname.get() ||
                    configExtra.getName().equals(MemberFieldNames.VORNAME) && clubConfig.MEMBER_EXPORT_DATA_Vorname.get() ||
                    configExtra.getName().equals(MemberFieldNames.ANREDE) && clubConfig.MEMBER_EXPORT_DATA_Anrede.get() ||
                    configExtra.getName().equals(MemberFieldNames.EMAIL) && clubConfig.MEMBER_EXPORT_DATA_Email.get() ||
                    configExtra.getName().equals(MemberFieldNames.TELEFON) && clubConfig.MEMBER_EXPORT_DATA_Telefon.get() ||
                    configExtra.getName().equals(MemberFieldNames.STRASSE) && clubConfig.MEMBER_EXPORT_DATA_Strasse.get() ||
                    configExtra.getName().equals(MemberFieldNames.PLZ) && clubConfig.MEMBER_EXPORT_DATA_PLZ.get() ||
                    configExtra.getName().equals(MemberFieldNames.ORT) && clubConfig.MEMBER_EXPORT_DATA_Ort.get() ||
                    configExtra.getName().equals(MemberFieldNames.LAND) && clubConfig.MEMBER_EXPORT_DATA_Land.get() ||
                    configExtra.getName().equals(MemberFieldNames.STATUS) && clubConfig.MEMBER_EXPORT_DATA_Status.get() ||
                    configExtra.getName().equals(MemberFieldNames.BEITRAGSSATZ) && clubConfig.MEMBER_EXPORT_DATA_Beitragssatz.get() ||
                    configExtra.getName().equals(MemberFieldNames.BANK) && clubConfig.MEMBER_EXPORT_DATA_Bank.get() ||
                    configExtra.getName().equals(MemberFieldNames.IBAN) && clubConfig.MEMBER_EXPORT_DATA_Iban.get() ||
                    configExtra.getName().equals(MemberFieldNames.BIC) && clubConfig.MEMBER_EXPORT_DATA_Bic.get() ||
                    configExtra.getName().equals(MemberFieldNames.KONTOINHABER) && clubConfig.MEMBER_EXPORT_DATA_Kontoinhaber.get() ||
                    configExtra.getName().equals(MemberFieldNames.ZAHLART) && clubConfig.MEMBER_EXPORT_DATA_Zahlart.get() ||
                    configExtra.getName().equals(MemberFieldNames.ZAHLUNGSBEGINN) && clubConfig.MEMBER_EXPORT_DATA_Zahlungsbeginn.get() ||
                    configExtra.getName().equals(MemberFieldNames.SEPABEGINN) && clubConfig.MEMBER_EXPORT_DATA_Sepabeginn.get() ||
                    configExtra.getName().equals(MemberFieldNames.BEITRITT) && clubConfig.MEMBER_EXPORT_DATA_Beitritt.get()) {
                list.add(configExtra.getActValueString());
            }
        } else {
            //export all
            if (configExtra.getName().equals(MemberFieldNames.BEITRAG)) {
                list.add(DF.format(1.0 * memberData.getBeitrag() / 100));
            } else {
                list.add(configExtra.getActValueString());
            }
        }
    }
}
