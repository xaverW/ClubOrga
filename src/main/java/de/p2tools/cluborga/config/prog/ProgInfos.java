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

package de.p2tools.cluborga.config.prog;

import de.p2tools.cluborga.Main;
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.configfile.SettingsDirectory;
import de.p2tools.p2lib.tools.PException;
import de.p2tools.p2lib.tools.log.PLog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;

public class ProgInfos {

    public static String getUserAgent() {
        return "";
    }


    /**
     * Retrieve the path to the program jar file.
     *
     * @return The program jar file path with a separator added.
     */
    public static String getPathJar() {
        // macht Probleme bei Win und Netzwerkpfaden, liefert dann Absolute Pfade zB. \\VBOXSVR\share\Mediathek\...
        final String pFilePath = "pFile";
        File propFile = new File(pFilePath);
        if (!propFile.exists()) {
            try {
                final CodeSource cS = Main.class.getProtectionDomain().getCodeSource();
                final File jarFile = new File(cS.getLocation().toURI().getPath());
                final String jarDir = jarFile.getParentFile().getPath();
                propFile = new File(jarDir + File.separator + pFilePath);
            } catch (final Exception ignored) {
            }
        }
        String s = propFile.getAbsolutePath().replace(pFilePath, "");
        if (!s.endsWith(File.separator)) {
            s = s + File.separator;
        }
        return s;
    }


    /**
     * Return the path to the club config file
     *
     * @return Path object to club config file
     */
    public static Path getClubConfigFile(String clubPath) {
        Path path = ProgInfos.getClubDirectory(clubPath).resolve(ProgConst.CLUB_CONFIG_DIR);
        makeDirs(path);

        return path.resolve(ProgConst.CLUB_CONFIG_FILE);

    }

    /**
     * Return the path to the club data file
     *
     * @return Path object to club data file
     */
    public static Path getClubDataFile(String clubPath) {
        Path path = ProgInfos.getClubDirectory(clubPath).resolve(ProgConst.CLUB_CONFIG_DIR);
        makeDirs(path);

        return path.resolve(ProgConst.CLUB_DATA_FILE);
    }

    /**
     * Return the path to the club data template file
     *
     * @return Path object to club data template file
     */
    public static Path getClubDataTemplateFile(String clubPath) {
        Path path = ProgInfos.getClubDirectory(clubPath).resolve(ProgConst.CLUB_TEMPLATE_DIR);
        makeDirs(path);

        return path;
    }

    /**
     * Return the path to the club data letter file
     *
     * @return Path object to club data letter file
     */
    public static Path getClubDataLetterFile(String clubPath) {
        Path path = ProgInfos.getClubDirectory(clubPath).resolve(ProgConst.CLUB_LETTER_DIR);
        makeDirs(path);

        return path;
    }

    /**
     * Return the path to the club exports file
     *
     * @return Path object to club exports file
     */
    public static Path getClubExportsDir(String clubPath) {
        Path path = ProgInfos.getClubDirectory(clubPath).resolve(ProgConst.CLUB_EXPORT_DIR);
        makeDirs(path);

        return path;
    }

    /**
     * Return the path to the club data file
     *
     * @return Path object to club data file
     */
    public static Path getClubDataFileZip(String clubPath) {
        Path path = ProgInfos.getClubDirectory(clubPath).resolve(ProgConst.CLUB_CONFIG_DIR);
        makeDirs(path);

        return path.resolve(ProgConst.CLUB_DATA_FILE_ZIP);
    }

    /**
     * Returns the path to the club data
     *
     * @param clubPath
     * @return
     */
    public static Path getClubDirectory(String clubPath) {
        final Path baseDirectoryPath;

        if (clubPath.isEmpty()) {
            baseDirectoryPath = Paths.get(System.getProperty("user.home"), ProgConst.CLUB_DIRECTORY);
        } else {
            baseDirectoryPath = Paths.get(clubPath);
        }

        return baseDirectoryPath;
    }

    private static void makeDirs(Path path) {
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (final IOException ioException) {
                PLog.errorLog(987102036, ioException, "Ordner anlegen: " + path);
                PAlert.showErrorAlert("Ordner anlegen", " Der Ordner " + path +
                        " konnte nicht angelegt werden." + P2LibConst.LINE_SEPARATOR +
                        "Bitte prüfen Sie die Dateirechte.");

                PException.throwPException("Der Ordner " + path + " " +
                        "konnte nicht angelegt werden." + P2LibConst.LINE_SEPARATOR +
                        "Bitte prüfen Sie die Dateirechte.", ioException);
            }
        }
    }

    public static String getLogDirectory_String() {
        final String logDir;
        if (ProgConfig.SYSTEM_LOG_DIR.get().isEmpty()) {
            logDir = Paths.get(getSettingsDirectory_String(), ProgConst.LOG_DIR).toString();
        } else {
            logDir = ProgConfig.SYSTEM_LOG_DIR.get();
        }
        return logDir;
    }

    /**
     * Return the path to "p2tools.xml"
     *
     * @return Path object to p2tools.xml file
     */
    public static Path getSettingsFile() {
        return SettingsDirectory.getSettingsFile(ProgData.configDir,
                ProgConst.CONFIG_DIRECTORY,
                ProgConst.CONFIG_FILE);
    }

    /**
     * Return the location of the settings directory. If it does not exist, create one.
     *
     * @return Path to the settings directory
     * @throws IllegalStateException Will be thrown if settings directory don't exist and if there is
     *                               an error on creating it.
     */
    public static Path getSettingsDirectory() throws IllegalStateException {
        return SettingsDirectory.getSettingsDirectory(ProgData.configDir,
                ProgConst.CONFIG_DIRECTORY);
    }

    public static String getSettingsDirectory_String() {
        return getSettingsDirectory().toString();
    }

}
