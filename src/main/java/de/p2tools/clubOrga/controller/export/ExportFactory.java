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

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ExportFactory {
    private static List<String> fileList = new ArrayList<>();

    private ExportFactory() {
    }

    public static boolean check(Stage stage, String destDir, String destFile) {

        if (destDir.isEmpty()) {
            PAlert.showErrorAlert(stage, "Pfad fehlt", "Es wurde kein Pfad für den " +
                    "Export angegeben!");
            return false;
        }

        if (destFile.isEmpty()) {
            PAlert.showErrorAlert(stage, "Datei fehlt", "Es wurde keine Datei für den " +
                    "Export angegeben!");
            return false;
        }

        Path dDir = Paths.get(destDir);
        Path dFile = Paths.get(destDir, destFile);

        if (!dDir.toFile().exists() && !dDir.toFile().mkdirs()) {
            PAlert.showErrorAlert(stage, "Exportverzeichnis", "Das angegebene Exportverzeichnis existiert " +
                    "nicht und kann nicht angelegt werden.");
            return false;
        }

        if (dFile.toFile().exists() && !dFile.toFile().isFile()) {
            PAlert.showErrorAlert(stage, "Exportdatei", "Die angegebene Exportdatei existiert " +
                    "und ist ein Ordner.");
            return false;
        }

        if (dFile.toFile().exists()) {
            PAlert.BUTTON btn = PAlert.showAlert_yes_no(stage,
                    "Datei erstellen",
                    "Exportdatei",
                    "Die angegebene Exportdatei existiert bereits. Soll sie überschrieben werden?");

            if (btn != PAlert.BUTTON.YES) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param in
     * @return
     */
    public static String inWorten(String in) {
        String out;
        String str = in;
        String tmp;
        int nr = 0;
        out = "*";
        try {
            while (str.length() > 0) {
                tmp = str.substring(0, 1);
                str = str.substring(1);
                if (tmp.equals(".") || tmp.equals(",")) {
                    break;
                }
                nr = Integer.parseInt(tmp);
                switch (nr) {
                    case 0:
                        out += "Null*";
                        break;
                    case 1:
                        out += "Eins*";
                        break;
                    case 2:
                        out += "Zwei*";
                        break;
                    case 3:
                        out += "Drei*";
                        break;
                    case 4:
                        out += "Vier*";
                        break;
                    case 5:
                        out += "Fünf*";
                        break;
                    case 6:
                        out += "Sechs*";
                        break;
                    case 7:
                        out += "Sieben*";
                        break;
                    case 8:
                        out += "Acht*";
                        break;
                    case 9:
                        out += "Neun*";
                        break;
                }
            }
        } catch (NumberFormatException ex) {
            PLog.errorLog(945152102, "wrong number: " + in);
        }
        return out;
    }

}
