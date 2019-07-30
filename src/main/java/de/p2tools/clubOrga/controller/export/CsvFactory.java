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


package de.p2tools.clubOrga.controller.export;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberFactory;
import de.p2tools.clubOrga.data.memberData.MemberFieldNames;
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
import java.util.ArrayList;
import java.util.List;

public class CsvFactory {

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

            final String key = configExtra.getName();
            try {
                final String s = record.get(key);
                if (s != null && !s.isEmpty()) {
                    found = true;
                    configExtra.setActValue(s);
                }
            } catch (IllegalArgumentException ex) {
                PLog.errorLog(316497821, ex, "no coloum of header: " + key);
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

        // headers
        MemberData header = memberDataList.get(0);
        for (Config configExtra : header.getConfigsArr()) {
            if (configExtra.getName().equals(MemberFieldNames.ID)) {
                continue;
            }
            list.add(configExtra.getName());
        }
        String[] HEADERS = list.toArray(new String[]{});


        try (FileWriter out = new FileWriter(exportPath.toFile());
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {

            for (MemberData memberData : memberDataList) {
                list.clear();
                for (Config configExtra : memberData.getConfigsArr()) {
                    if (configExtra.getName().equals(MemberFieldNames.ID)) {
                        continue;
                    }
                    list.add(configExtra.getActValueString());
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

}
