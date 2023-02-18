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

import de.p2tools.clubOrga.data.clubData.ClubData;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.PException;

import java.util.ArrayList;
import java.util.List;

public class ReplaceFactory {

    public static void getAddressData(List<String> list, ClubData clubData, MemberData memberData) {
        list.clear();

        // clubdata
        if (clubData != null) {
            String sender = clubData.getName() + " - "
                    + clubData.getStrasse() + " - "
                    + clubData.getPlz() + " - " + clubData.getOrt();
            list.add(sender);

        } else {
            list.add("");
        }

        // membertags
        if (memberData != null) {
            // Anrede
            String addressee = memberData.getAnrede();
            if (!addressee.isEmpty()) {
                list.add(addressee);
            }

            // Name, Straße, PLZ, Ort
            addressee = memberData.getVorname() + " " + memberData.getNachname();
            list.add(addressee);
            addressee = memberData.getStrasse();
            list.add(addressee);
            addressee = memberData.getPlz() + " " + memberData.getOrt();
            list.add(addressee);

            // Land
            addressee = memberData.getLand();
            if (!addressee.isEmpty()) {
                list.add(addressee);
            }

        } else {
            list.add("");
        }

    }

    public static List<ReplaceData> getReplaceList(ClubData clubData) {
        return getReplaceList(clubData, null, null);
    }

    public static List<ReplaceData> getReplaceList(ClubData clubData, MemberData memberData) {
        return getReplaceList(clubData, memberData, null);
    }

    public static List<ReplaceData> getReplaceList(ClubData clubData, FeeData feeData) {
        MemberData memberData = feeData.getMemberData();
        return getReplaceList(clubData, memberData, feeData);
    }

    private static List<ReplaceData> getReplaceList(ClubData clubData,
                                                    MemberData memberData,
                                                    FeeData feeData) {
        List<ReplaceData> list = new ArrayList<>();
        Config[] configsArr;

        // systemtags
        for (NewsletterFactory.TAGS tag : NewsletterFactory.TAGS.values()) {
            list.add(new ReplaceData(tag.getFrom(), tag.getTo()));
        }

        // clubdata
        if (clubData != null) {
            configsArr = clubData.getConfigsArr();
            addData(configsArr, list, NewsletterFactory.NEWSLETTER_TAG_CLUB_DATA);
        }

        // membertags
        if (memberData != null) {
            configsArr = memberData.getConfigsForNewsletter();
            addData(configsArr, list, NewsletterFactory.NEWSLETTER_TAG_MEMBER_DATA);

        } else if (feeData != null && feeData.getMemberData() != null) {
            configsArr = feeData.getMemberData().getConfigsForNewsletter();
            addData(configsArr, list, NewsletterFactory.NEWSLETTER_TAG_MEMBER_DATA);
        }

        // feetags
        if (feeData != null) {
            configsArr = feeData.getConfigsForNewsletter();
            addData(configsArr, list, NewsletterFactory.NEWSLETTER_TAG_FEE_DATA);
        }

        return list;
    }

    private static void addData(Config[] configs, List<ReplaceData> list, String prefix) {
        for (Config config : configs) {
            if (!Config.class.isAssignableFrom(config.getClass())) {
                // nur die Config haben Namen
                PException.throwPException(874512036, "ReplayFactory: no Config " + config.getKey());
                continue;
            }

            if (config.getActValueString() == null || config.getName() == null) {
                PException.throwPException(874512036, "ReplayFactory: config-value||name is null: " + config.getKey());
            }


            final String from = getSearchString(config.getName(), prefix);
            final String to;

            switch (config.getActValueString()) {
                case "true":
                    to = "ja";
                    break;
                case "false":
                    to = "nein";
                    break;
                default:
                    to = config.getActValueString();
            }

            list.add(new ReplaceData(from, to));
        }
    }

    private static String getSearchString(String search, String prefix) {
        return NewsletterFactory.SEARCH_EXPRESSION_START + prefix + search + NewsletterFactory.SEARCH_EXPRESSION_STOP;
    }
}
