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

package de.p2tools.cluborga.data.memberData;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.PAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;

public class MemberDataList extends MemberDataListWorker {

    public MemberDataList(ClubConfig clubConfig) {
        super(clubConfig);
    }

    public ObservableList<String> getListNames() {
        ObservableList<String> list = FXCollections.observableArrayList();
        this.stream().forEach(memberData -> list.add(memberData.getNachname()));
        return list;
    }

    public MemberData getById(long id) {
        if (id == 0) {
            return null;
        }
        return this.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
    }

    public boolean memberDataListRemoveAll(Collection<MemberData> list) {
        if (list.isEmpty()) {
            return true;
        }

        if (list.size() == 1) {
            MemberData memberData = list.toArray(new MemberData[]{})[0];

            if (PAlert.BUTTON.YES == PAlert.showAlert_yes_no(clubConfig.getStage(), "Löschen",
                    "1 Mitglieder löschen?",
                    "Soll das Mitglied:" + P2LibConst.LINE_SEPARATORx2 +
                            "    " + memberData.getVorname() + " " + memberData.getNachname() + P2LibConst.LINE_SEPARATORx2 +
                            "gelöscht werden?")) {

                return removeAll(list);
            }

        } else if (PAlert.BUTTON.YES == PAlert.showAlert_yes_no(clubConfig.getStage(), "Löschen",
                "Mitglieder löschen?",
                "Sollen " + list.size() + " Mitglieder gelöscht werden?")) {

            return removeAll(list);
        }

        return false;
    }

    public static Comparator<MemberData> getComperator() {
        return new MemberDataSorter();
    }

    private static class MemberDataSorter implements Comparator<MemberData> {

        private Collator collator;

        public MemberDataSorter() {
            super();
            // ignore lower/upper case, but accept special characters in localised alphabetical order
            collator = Collator.getInstance(Locale.GERMANY);
            collator.setStrength(Collator.SECONDARY);
        }

        @Override
        public int compare(MemberData o1, MemberData o2) {
            if (o1 == null || o2 == null) {
                return 0;
            }
            final int c;
            if ((c = collator.compare(o1.getNachname(), o2.getNachname())) != 0) {
                return c;
            }
            return collator.compare(o1.getVorname(), o2.getVorname());
        }
    }
}
