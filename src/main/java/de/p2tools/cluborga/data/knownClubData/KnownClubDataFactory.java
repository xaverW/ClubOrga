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


package de.p2tools.cluborga.data.knownClubData;

import de.p2tools.cluborga.config.prog.ProgData;
import de.p2tools.cluborga.data.demoData.data.DemoClubDataFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class KnownClubDataFactory {
    private KnownClubDataFactory() {

    }

    /**
     * liefert den "nächsten" neuen Club, also Name und Pfad werden vorbelegt
     * und "firstStart" wird gesetzt
     *
     * @return
     */
    public static KnownClubData getNextNewKnownClubData() {
        final KnownClubData knownClubData = new KnownClubData();
        int nr = 0;
        final String n = "Verein";
        String name = n;

        Path clubPath = Paths.get(System.getProperty("user.home"), name);
        while (clubPath.toFile().exists() && clubPath.toFile().listFiles().length != 0) {
            ++nr;
            name = n + "-" + nr;
            clubPath = Paths.get(System.getProperty("user.home"), name);
        }

        knownClubData.setClubname(DemoClubDataFactory.getName());
        knownClubData.setClubpath(clubPath.toString());

        return knownClubData;
    }

    /**
     * startet einen bereits angelegtn Club, der nicht mehr in der Liste ist
     * aber noch im Dateisystem liegt
     *
     * @param progData
     * @return
     */
    public static KnownClubData importKnownClubData(ProgData progData, String dir) {
        final KnownClubData knownClubData = new KnownClubData();

        knownClubData.setClubpath(dir);
        progData.knownClubDataList.add(knownClubData);
        return knownClubData;
    }
}
