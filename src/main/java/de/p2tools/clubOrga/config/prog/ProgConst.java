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

package de.p2tools.clubOrga.config.prog;

public class ProgConst {

    public static final String P2_PROGRAMNAME = "P2Tools - ClubOrga";
    public static final String P2_PROGRAMMNAME_SELECTOR = "P2Tools - Vereinsauswahl";
    public static final String PROGRAMNAME = "ClubOrga";

    // Config dir/file
    public static final String CONFIG_FILE = "cluborga.xml";
    public static final String CONFIG_DIRECTORY = "p2ClubOrga"; // im Homeverzeichnis
    public static final String XML_START = "ClubOrga";


    // standard path for clubdata
    public static final String CLUB_DIRECTORY = "ClubOrga"; // im Homeverzeichnis
    public static final String CLUB_TEMPLATE_DIR = "Vorlagen";
    public static final String CLUB_EXPORT_DIR = "Exporte";
    public static final String CLUB_LETTER_DIR = "Briefe";
    public static final String CLUB_CONFIG_DIR = "Vereinsdaten";
    public static final String CLUB_CONFIG_FILE = "club-config.xml";
    public static final String CLUB_DATA_FILE = "club-data.xml";
    public static final String CLUB_DATA_FILE_ZIP = "club-data.zip";

    public static final String TEMPLATE_PATH = "/de/p2tools/clubOrga/templates/";
    public static final String TEMPLATE_1 = "Vorlage-Einladung.txt";
    public static final String TEMPLATE_2 = "Vorlage-Rechnung.txt";
    public static final String TEMPLATE_3 = "Parameter-Test.txt";
    public static final String TEMPLATE_4 = "P2-Logo.png";


    public static final String CSS_FILE = "/de/p2tools/clubOrga/club.css";
    public static final String CSS_FILE_DARK_THEME = "/de/p2tools/clubOrga/club-dark.css";
    public static final String LOG_DIR = "Log";

    public static final String WEBSITE_P2 = "https://www.p2tools.de";
    public static final String WEBSITE_CLUB = "https://www.p2tools.de/cluborga/";
    public static final String WEBSITE_CLUB_HELP = "https://www.p2tools.de/cluborga/manual/";

    public static final String URL_PROG_UPDATE = "https://www.p2tools.de/extra/cluborga-info.xml";
    public static final String URL_PROG_BETA_UPDATE = "https://www.p2tools.de/extra/cluborga-beta-info.xml";


    public static final String P2_ICON_850 = "P2.png";
    public static final String P2_ICON_32 = "P2_32.png";
    public static final String P2_ICON_PATH = "/de/p2tools/clubOrga/icon/";

    public static final long FILTER_ID_NOT_SELECTED = -1;
    public static final int FIELD_NOT_USED = -1;
    public static final int STANDARD_FIELD = 0;

    public static final int MAX_EXTRA_DATA_MAX_MEMBER = 5;
    public static final int MAX_EXTRA_DATA_MAX_FEE = 5;
    public static final int MAX_EXTRA_DATA_MAX_FINANCE = 5;
    public static final String EXTRA_DATA_NAME = "Info-";

    public final static String CSV_SUFFIX = "csv";
    public final static String ZIP_SUFFIX = "zip";
    public final static String PDF_SUFFIX = "pdf";

    public static final int MIN_TABLE_HEIGHT = 200;
    
}
