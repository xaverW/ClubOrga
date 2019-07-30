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


package de.p2tools.clubOrga.data.demoData.data;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.p2Lib.tools.PRandom;

public class DemoClubDataFactory {

    private DemoClubDataFactory() {
    }

    public static String getName() {

        int i = PRandom.getRandom(0, 16);
        String name;
        switch (i) {
            case 0:
                name = "Stammtisch-Freunde";
                break;
            case 1:
                name = "Spielvereinigung";
                break;
            case 2:
                name = "Heimaturlauber";
                break;
            case 3:
                name = "Fußballfreunde";
                break;
            case 4:
                name = "Amateur-Club";
                break;
            case 5:
                name = "Athletik-Klub";
                break;
            case 6:
                name = "Fußballfreunde";
                break;
            case 7:
                name = "Erster Fußballclub";
                break;
            case 8:
                name = "Erster Turn- und Sportverein";
                break;
            case 9:
                name = "Freizeitverein";
                break;
            case 10:
                name = "Geselligkeitsverein";
                break;
            case 11:
                name = "Jugendspielverein";
                break;
            case 12:
                name = "Kulturgemeinschaft";
                break;
            case 13:
                name = "Kultur- und Schulsportverein";
                break;
            case 14:
                name = "Rasenspielverein";
                break;
            case 15:
                name = "Spiel- und Sportclub";
                break;
            case 16:
            default:
                name = "Dynamo Tresen";
                break;
        }
        return name;
    }

    public static String getOrt() {
        int i = PRandom.getRandom(0, 3);
        String ort;
        switch (i) {
            case 0:
                ort = "München";
                break;
            case 1:
                ort = "Hamburg";
                break;
            case 2:
                ort = "Berlin";
                break;
            case 3:
            default:
                ort = "Amberg";
                break;
        }
        return ort;
    }

    public static String getPlz() {
        return "" + PRandom.getRandom(10000, 99999);
    }

    public static String getStrasse() {
        int i = PRandom.getRandom(0, 3);
        String strasse;
        switch (i) {
            case 0:
                strasse = "Märchenweg " + PRandom.getRandom(1, 10);
                break;
            case 1:
                strasse = "Hauptstraße " + PRandom.getRandom(1, 10);
                break;
            case 2:
                strasse = "Waldweg " + PRandom.getRandom(1, 10);
                break;
            case 3:
            default:
                strasse = "Bahnhofstr. " + PRandom.getRandom(1, 10);
                break;
        }
        return strasse;
    }

    public static String getTelefon() {
        return "" + PRandom.getRandom(10000, 99999);
    }

    public static String getEmail() {
        return "mail@verein-" + PRandom.getRandom(100, 999);
    }

    public static String getWebsite() {
        return "www.verein-" + PRandom.getRandom(100, 999);
    }

    public static String getKontoNr() {
        return "" + PRandom.getRandom(10000, 99999);
    }

    public static String getBic() {
        return "VEREIN-BIC-" + PRandom.getRandom(100, 999);
    }

    public static String getIban() {
        return "VEREIN-IBAN-123456-" + PRandom.getRandom(100, 999);
    }

    public static String getBank() {
        return "Bank-" + PRandom.getRandom(100, 999);
    }

    public static String getGlaeubigerId() {
        return "GlaeubigerId-" + PRandom.getRandom(100, 999);
    }


    public static void randomizeDemoClubData(ClubConfig clubConfig) {
        clubConfig.DEMO_DATA_ADD_CLUB_NAME.setValue(getName());
        clubConfig.DEMO_DATA_ADD_CLUB_ORT.setValue(getOrt());
        clubConfig.DEMO_DATA_ADD_CLUB_PLZ.setValue(getPlz());
        clubConfig.DEMO_DATA_ADD_CLUB_STRASSE.setValue(getStrasse());
        clubConfig.DEMO_DATA_ADD_CLUB_TELEFON.setValue(getTelefon());
        clubConfig.DEMO_DATA_ADD_CLUB_EMAIL.setValue(getEmail());
        clubConfig.DEMO_DATA_ADD_CLUB_WEBSITE.setValue(getWebsite());

        clubConfig.DEMO_DATA_ADD_CLUB_KONTO_NR.setValue(getKontoNr());
        clubConfig.DEMO_DATA_ADD_CLUB_BIC.setValue(getBic());
        clubConfig.DEMO_DATA_ADD_CLUB_IBAN.setValue(getIban());
        clubConfig.DEMO_DATA_ADD_CLUB_BANK.setValue(getBank());
        clubConfig.DEMO_DATA_ADD_CLUB_GLAEUBIGER_ID.setValue(getGlaeubigerId());
    }

    public static void setDemoClubData(ClubConfig clubConfig) {
        if (!clubConfig.DEMO_DATA_CLUB.get()) {
            // dann sollen sie nicht geändert werden
            return;
        }

        // und noch eintragen
        clubConfig.clubData.setName(clubConfig.DEMO_DATA_ADD_CLUB_NAME.getValueSafe());
        clubConfig.clubData.setOrt(clubConfig.DEMO_DATA_ADD_CLUB_ORT.getValueSafe());
        clubConfig.clubData.setPlz(clubConfig.DEMO_DATA_ADD_CLUB_PLZ.getValueSafe());
        clubConfig.clubData.setStrasse(clubConfig.DEMO_DATA_ADD_CLUB_STRASSE.getValueSafe());
        clubConfig.clubData.setTelefon(clubConfig.DEMO_DATA_ADD_CLUB_TELEFON.getValueSafe());
        clubConfig.clubData.setEmail(clubConfig.DEMO_DATA_ADD_CLUB_EMAIL.getValueSafe());
        clubConfig.clubData.setWebsite(clubConfig.DEMO_DATA_ADD_CLUB_WEBSITE.getValueSafe());

        clubConfig.clubData.setKontoNr(clubConfig.DEMO_DATA_ADD_CLUB_KONTO_NR.getValueSafe());
        clubConfig.clubData.setBic(clubConfig.DEMO_DATA_ADD_CLUB_BIC.getValueSafe());
        clubConfig.clubData.setIban(clubConfig.DEMO_DATA_ADD_CLUB_IBAN.getValueSafe());
        clubConfig.clubData.setBank(clubConfig.DEMO_DATA_ADD_CLUB_BANK.getValueSafe());
        clubConfig.clubData.setGlaeubigerId(clubConfig.DEMO_DATA_ADD_CLUB_GLAEUBIGER_ID.getValueSafe());
    }
}
