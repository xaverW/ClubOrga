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


package de.p2tools.clubOrga.controller;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.file.PFileUtils;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClubFactory {
    private ClubFactory() {

    }

    private static String getAbsolutFilePath(ClubConfig clubConfig, String src) {
        if (src.isEmpty()) {
            return "";
        }

        File s = new File(src);
        if (s.exists()) {
            return src;
        }

        Path path = Paths.get(ProgInfos.getClubDirectory(clubConfig.getClubPath()).toString(), src);
        if (path.toFile().exists()) {
            return path.toString();
        } else {
            return "";
        }
    }

    public static String getAbsolutFilePath(ClubConfig clubConfig, Stage stage, String s) {
        String sourceFile = s;

        if (sourceFile == null || sourceFile.isEmpty()) {
            PAlert.showErrorAlert(stage, "Vorlage fehlt", "Es wurde keine Vorlage angegeben!");
            return "";
        }

        sourceFile = ClubFactory.getAbsolutFilePath(clubConfig, sourceFile);

        if (!PFileUtils.fileExist(sourceFile)) {
            PAlert.showErrorAlert(stage, "Vorlage fehlt", "Die Vorlage existiert nicht!");
            return "";
        }

        return sourceFile;
    }

    public static String getDestinationPath(Stage stage, String destPath, String destFileName) {

        if (destPath == null || destPath.isEmpty()
                || destFileName == null || destFileName.isEmpty()) {
            PAlert.showErrorAlert(stage, "Speicherziel fehlt",
                    "Es wurde kein Speicherziel (Pfad und Datei) angeben!");
            return "";
        }
        Path dDir = Paths.get(destPath);
        Path dFile = Paths.get(destPath, destFileName);

        if (!dDir.toFile().exists() && !dDir.toFile().mkdirs()) {
            PAlert.showErrorAlert(stage, "Exportverzeichnis", "Das angegebene Exportverzeichnis existiert " +
                    "nicht und kann nicht angelegt werden.");
            return "";
        }

        if (dFile.toFile().exists() && !dFile.toFile().isFile()) {
            PAlert.showErrorAlert(stage, "Exportdatei", "Die angegebene Exportdatei existiert " +
                    "und ist ein Ordner.");
            return "";
        }

        if (!PFileUtils.checkFileToCreate(stage, dFile)) {
            return "";
        }

        return dFile.toString();
    }
}
