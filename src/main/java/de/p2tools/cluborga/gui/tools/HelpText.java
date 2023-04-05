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


package de.p2tools.cluborga.gui.tools;

public class HelpText {
    private HelpText() {

    }

    public static final String DARK_THEME = "Das Programm wird damit mit einer dunklen " +
            "Programmoberfläche angezeigt. Damit alle Elemente der Programmoberfläche " +
            "geändert werden, kann ein Programmneustart notwendig sein.";

    public static final String HELP_CLUB_SELECTOR = "Dieser Dialog dient zum Verwalten der bekannten Vereine." +
            "\n" +
            "Es können Vereine gestartet, in die Liste aufgenommen und aus ihr auch wieder gelöscht werden." +
            "\n\n" +
            "Die Liste enthält die Vereine die dem Programm bereits bekannt sind. Mit dem \"Pfeil\" " +
            "kann man einen davon starten. Das \"X\" löscht ihn aus der Liste. (Die Vereinsdaten selbst werden " +
            "aber damit nicht gelöscht, können also auch wieder in die Liste aufgenommen werden.)" +
            "\n\n" +
            "\"neuen Verein anlegen\" macht genau das, es wird ein neuer Verein angelegt, im nächsten Dialog " +
            "wird dann Speicherort und Vereinsname angegeben." +
            "\n\n" +
            "Ein erstellter Verein kann in eine ZIP-Datei gesichert werden. " +
            "Es werden damit alle Infos zum Verein gesichert. Mit \"ZIP-Datei importieren\" " +
            "kann dann ein so gesicherter Verein wieder gestartet werden." +
            "\n\n" +
            "Vereinsdaten die vorhanden, aber nicht mehr in der Liste enthalten " +
            "sind, können mit \"Verzeichnis importeren\" dem Programm wieder bekannt gemacht " +
            "und der Verein wieder gestartet werden.";
}
