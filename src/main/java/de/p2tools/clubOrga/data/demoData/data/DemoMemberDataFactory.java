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
import de.p2tools.clubOrga.data.demoData.DemoDataFactory;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateFactory;
import de.p2tools.clubOrga.data.feeData.paymentType.PaymentTypeFactory;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberFactory;
import de.p2tools.clubOrga.data.memberData.stateData.StateDataFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class DemoMemberDataFactory {
    private DemoMemberDataFactory() {
    }

    private static final String[] arrVornamenW = {
            "Julia", "Sarah", "Katharina", "Jennifer", "Lisa", "Christina", "Anna", "Laura", "Melanie", "Sabrina", "Vanessa",
            "Nadine", "Annika", "Jana", "Katrin", "Jasmin", "Lena", "Johanna", "Alexandra", "Lea"};

    private static final String[] arrVornamenM = {"Jan", "Christian", "Patrick", "Marcel", "Fabian", "Tim", "Benjamin", "Maximilian",
            "Simon", "Marco", "Michael", "Markus", "Nils", "Julian", "Felix", "Florian", "Philipp", "Daniel", "Timo", "Matthias"};


    private static final String[] arrNnachname = {"Müller", "Schmidt", "Fischer", "Wagner", "Bauer", "Schröder", "Braun", "Schmitt",
            "Lange", "Krüger", "Huber", "Weiß", "Scholz", "Hahn", "Zimmermann", "Mayer", "Roth", "König", "Beck", "Kraus",
            "Graf", "Schulte", "Haas", "Pohl", "Sauer"};

    private static final String[] arrStrasse = {"Amselweg", "Ahornweg", "Bergstrasse", "Lindenstrasse", "Jahnstrasse", "Gartenstrasse",
            "Vulkanstrasse", "Ringstrasse", "Waldstrasse", "Oststrasse", "Mühlenweg", "Tannenstrasse", "Grabenstrasse",
            "Feldstrasse", "Dorfstrasse", "Erlenweg", "Oberstrasse", "Kirchstrasse", "Lindenweg", "Danziger Strasse",
            "Schulstrasse", "Poststrasse", "Quellenweg", "Neue Strasse", "Josefstrasse"};

    private static final String[][] arrPlz = {{"82008", "Unterhaching"}, {"82024", "Taufkirchen"}, {"82031", "Grünwald"},
            {"82041", "Oberhaching"}, {"82049", "Pullach im Isartal"}, {"82054", "Sauerlach"}, {"82131", "Gauting"},
            {"82140", "Olching"}, {"82152", "Planegg"}, {"97851", "Rothenfels"}, {"97852", "Schollbrunn"},
            {"97846", "Partenstein"}, {"97848", "Rechtenbach"}, {"97849", "Roden"}, {"97859", "Wiesthal"},
            {"97892", "Kreuzwertheim"}, {"97901", "Altenbuch"},
            {"97854", "Steinfeld"}, {"97855", "Triefenstein"}, {"97857", "Urspringen"}, {"97904", "Dorfprozelten"}};

    private static final String[] arrLaender = {"Deutschland", "Schweiz", "Niederlande", "Österreich", "Belgien"};

    public static ArrayList<MemberData> getDemoMember(ClubConfig clubConfig, int number) {

        if (!clubConfig.DEMO_DATA_MEMBER.get()) {
            // nichts anlegen
            return null;
        }

        if (number <= 0) {
            // nichts anlegen
            return null;
        }


        ArrayList<MemberData> newMemberList = new ArrayList<>(number);
        Random random = new Random();
        boolean mann = true;

        for (int i = 0; i < number; ++i) {
            MemberData memberData = MemberFactory.getNewMemberData(clubConfig,
                    arrNnachname[random.nextInt(arrNnachname.length)], false);


            // Vorname, Nachname, Anrede
            final String vor;
            if (mann) {
                vor = arrVornamenM[random.nextInt(arrVornamenM.length)];
            } else {
                vor = arrVornamenW[random.nextInt(arrVornamenW.length)];
            }
            memberData.setAnrede(mann ? "Herr" : "Frau");
            memberData.setVorname(vor);
            mann = !mann;


            // Telefon, Mail
            memberData.setTelefon(random.nextInt(10_000) + "");
            memberData.setEmail(random.nextInt(10_000) + ".mail@test.local");


            // Straße, PLZ, Ort, Land
            final String str = arrStrasse[random.nextInt(arrStrasse.length)] + " " + 1 + random.nextInt(25);
            memberData.setStrasse(str);

            final String[] p = arrPlz[random.nextInt(arrPlz.length)];
            memberData.setPlz(p[0]);
            memberData.setOrt(p[1]);

            final String land = arrLaender[random.nextInt(arrLaender.length)];
            memberData.setLand(land);


            // Status, Beitrag, Zahlart
            final int status = random.nextInt(5);
            if (status < 4) {
                memberData.setStateData(clubConfig.stateDataList.getStateDataStandard(StateDataFactory.STATE_TYPE.STATE_ACTIVE));
            } else {
                memberData.setStateData(clubConfig.stateDataList.getStateDataStandard(StateDataFactory.STATE_TYPE.STATE_PASSIVE));
            }

            final int beitrag = random.nextInt(10);
            if (beitrag < 2) {
                memberData.setFeeRateData(clubConfig.feeRateDataList.getRateDataStandard(FeeRateFactory.RATE_TYPE.RATE_WITHOUT));
                memberData.setBeitrag(0); // nix Beitrag
            } else if (beitrag < 8) {
                memberData.setFeeRateData(clubConfig.feeRateDataList.getRateDataStandard(FeeRateFactory.RATE_TYPE.RATE_STANDARD));
            } else {
                // frei
                memberData.setFeeRateData(clubConfig.feeRateDataList.getRateDataStandard(FeeRateFactory.RATE_TYPE.RATE_FREE));
                memberData.setBeitrag((1 + random.nextInt(10)) * 1000); // Beitrag von 10 .. 100€
            }

            final int zahlart = random.nextInt(PaymentTypeFactory.PAYMENT_TYPE.values().length);
            final PaymentTypeFactory.PAYMENT_TYPE type = PaymentTypeFactory.PAYMENT_TYPE.values()[zahlart];
            switch (type) {
                case PAYMENT_BAR:
                    memberData.setPaymentTypeData(clubConfig.paymentTypeDataList
                            .getStateDataStandard(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BAR));
                    break;

                case PAYMENT_BANKEINZUG:
                    memberData.setPaymentTypeData(clubConfig.paymentTypeDataList
                            .getStateDataStandard(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BANKEINZUG));
                    memberData.setBic("BIC-TEST-" + random.nextInt(4));
                    memberData.setIban("DE-TEST-IBAN" + random.nextInt(5));
                    break;

                case PAYMENT_UEBERWEISUNG:
                default:
                    memberData.setPaymentTypeData(clubConfig.paymentTypeDataList
                            .getStateDataStandard(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_UEBERWEISUNG));
                    break;
            }

            if (memberData.getFeeRateData().equals(
                    clubConfig.feeRateDataList.getRateDataStandard(FeeRateFactory.RATE_TYPE.RATE_WITHOUT))) {
                memberData.setPaymentTypeData(clubConfig.paymentTypeDataList
                        .getStateDataStandard(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BAR));
            }

            // Datum
            LocalDateTime dateTime = DemoDataFactory.getRandomDate();

            memberData.getBeitritt().setPLocalDate(dateTime.toLocalDate());
            memberData.getSepaBeginn().setPLocalDate(dateTime.toLocalDate());
            memberData.getErstellDatum().setPLocalDate(dateTime.toLocalDate());
            memberData.getZahlungsbeginn().setPLocalDate(dateTime.toLocalDate());

            newMemberList.add(memberData);
        }


        // nach Datum sortieren
        Collections.sort(newMemberList, Comparator.comparing(MemberData::getErstellDatum));

        // und noch die Nummer setzen
        long nr = clubConfig.memberDataList.getNextNr();
        for (MemberData memberData : newMemberList) {
            memberData.setNo(nr++);
        }

        return newMemberList;
    }

}
