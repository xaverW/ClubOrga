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


package de.p2tools.clubOrga.controller.newsletter;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.p2Lib.tools.date.PDateFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NewsletterFactory {

    public static final String SEARCH_EXPRESSION_START = "<<";
    public static final String SEARCH_EXPRESSION_STOP = ">>";

    public static final String TAG_DATE_TODAY = "<<datum.heute>>";
    public static final String TAG_NEW_LINE = "<<##>>";

    public static final String TAG_NEW_SITE = "<<####>>";
    public static final String TAG_FOLD_MARK = "<<Faltmarke>>";
    public static final String TAG_ADDRESS_FIELD = "<<Adressfeld>>";
    public static final String TAG_PICTURE = "<<Bild>>"; // <<Bild url="", x="", y="", xx="", yy="">>
    public static final String TAG_PICTURE_ = "<<Bild";

    public static final String TAG_FONT_COURIER = "<<COURIER>>";
    public static final String TAG_FONT_HELVETICA = "<<HELVETICA>>";
    public static final String TAG_FONT_TIMES_ROMAN = "<<TIMES_ROMAN>>";

    public static final String ODF_FILE_SUFFIX = "odt";
    public static final String PDF_FILE_SUFFIX = "pdf";

    public static final String NEWSLETTER_TAG_CLUB_DATA = "club.";
    public static final String NEWSLETTER_TAG_MEMBER_DATA = "mitglied.";
    public static final String NEWSLETTER_TAG_FEE_DATA = "beitrag.";
    public static final String NEWSLETTER_TAG_FINANCE_DATA = "finanzen.";

    public enum NEWSLETTER_TYPE {ODF, PDF}

    public enum TAGS {
        DATE_TODAY_DD_MM_YYYY(TAG_DATE_TODAY, "") {
            public String getTo() {
                return PDateFactory.getTodayStr();
            }
        },
        NEW_LINE(TAG_NEW_LINE, "\n") {
        };

        private final String from;
        private final String to;

        TAGS(String from, String to) {
            this.from = from;
            this.to = to;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        @Override
        public String toString() {
            return to;
        }
    }

    public static NEWSLETTER_TYPE getType(String fileName) {
        final NEWSLETTER_TYPE newsletterType;

        if (fileName.endsWith(NewsletterFactory.ODF_FILE_SUFFIX)) {
            newsletterType = NEWSLETTER_TYPE.ODF;
        } else {
            // dann gehen wir mal davon aus, dass es eine lesbare TEXT-Datei ist
            newsletterType = NEWSLETTER_TYPE.PDF;
        }

        return newsletterType;
    }

    public static String getSuffix(NEWSLETTER_TYPE newsletterType) {
        final String suffix;

        switch (newsletterType) {
            case ODF:
                suffix = NewsletterFactory.ODF_FILE_SUFFIX;
                break;

            case PDF:
            default:
                suffix = NewsletterFactory.PDF_FILE_SUFFIX;
                break;
        }

        return suffix;
    }


    public static List<File> getTemplates(ClubConfig clubConfig) {
        List<File> results = new ArrayList<>();

        final Path templateFilePath = ProgInfos.getClubDataTemplateFile(clubConfig.getClubPath());
        File[] files = new File(templateFilePath.toString()).listFiles();
        if (files == null) {
            // dann gibts das Verzeichnis nicht mehr
            return results;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                results.add(file);
            }
        }

        return results;
    }


}
