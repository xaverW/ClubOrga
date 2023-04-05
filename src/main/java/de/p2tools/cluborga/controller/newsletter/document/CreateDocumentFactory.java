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


package de.p2tools.cluborga.controller.newsletter.document;

import de.p2tools.cluborga.controller.newsletter.ReplaceData;
import de.p2tools.p2lib.tools.log.PLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CreateDocumentFactory {

    private CreateDocumentFactory() {
    }

    public static List<String> replaceFile(List<ReplaceData> replaceDataList, String srcFileName) {
        final List<String> lineList = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(srcFileName))) {

            br.lines().forEach(line -> {
                line = replaceLine(line, replaceDataList);
                lineList.add(line);
            });

        } catch (IOException ex) {
            PLog.errorLog(987451203, ex);
        }

        return lineList;
    }

    private static String replaceLine(String line, List<ReplaceData> list) {
        String ret = line;

        for (ReplaceData replaceData : list) {
            ret = replaceString(replaceData, ret);
        }

        return ret;
    }

    private static String replaceString(ReplaceData replaceData, String line) {
        StringBuilder source = new StringBuilder(line);
        StringBuilder sourceLower = new StringBuilder(line.toLowerCase());
        String replaceDataFrom = replaceData.getFrom();

        int idx = 0;
        while ((idx = sourceLower.indexOf(replaceDataFrom, idx)) != -1) {
            source.replace(idx, idx + replaceDataFrom.length(), replaceData.getTo());
            sourceLower.replace(idx, idx + replaceDataFrom.length(), replaceData.getTo());
            idx += replaceDataFrom.length();
        }

        return source.toString();
    }
}
