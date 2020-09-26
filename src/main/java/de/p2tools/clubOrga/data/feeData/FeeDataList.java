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

package de.p2tools.clubOrga.data.feeData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.clubInfoData.ClubInfoData;
import de.p2tools.p2Lib.alert.PAlert;

import java.util.ArrayList;
import java.util.Collection;

public class FeeDataList extends FeeDataListWorker {

    public FeeDataList(ClubConfig clubConfig) {
        super(clubConfig);
    }

    public void clearSelected() {
        this.stream().forEach(feeData -> feeData.setSelected(false));
    }

    public FeeData getFeeById(long id) {
        if (id == 0) {
            return null;
        }
        return this.stream().filter(feeData -> feeData.getId() == id).findFirst().orElse(null);
    }


    public boolean feeDataListRemoveAll(Collection<FeeData> list) {
        if (PAlert.BUTTON.YES == PAlert.showAlert_yes_no(clubConfig.getStage(),
                "Löschen",
                "Beiträge löschen?", list.size() == 1 ?
                        "Soll 1 Beitrag gelöscht werden?" :
                        "Sollen " + list.size() + " Beiträge gelöscht werden?")) {

            return removeAll(list);
        }

        return false;
    }

    public ArrayList<ClubInfoData> generateClubDataInfo() {
        ArrayList<ClubInfoData> list = new ArrayList<>();

        ClubInfoData ci = new ClubInfoData();
        ci.setText("Beiträge");
        ci.setAmount(this.size());
        ci.setGroup1(true);
        list.add(ci);

        ClubInfoData ciRechnungOk = new ClubInfoData();
        ciRechnungOk.setName("Rechnung");
        ciRechnungOk.setText("Rechnung wurde erstellt");
        this.stream().forEach(feeData -> {
            ciRechnungOk.setAmount((int) this.stream()
                    .filter(data -> !data.getRechnung().isEmpty()).count());
        });
        list.add(ciRechnungOk);
        ClubInfoData ciRechnung = new ClubInfoData();
        ciRechnung.setText("Rechnung wurde nicht erstellt");
        this.stream().forEach(feeData -> {
            ciRechnung.setAmount((int) this.stream()
                    .filter(data -> data.getRechnung().isEmpty()).count());
        });
        list.add(ciRechnung);

        ClubInfoData ciBezahlt = new ClubInfoData();
        ciBezahlt.setName("Beitrag");
        ciBezahlt.setText("Beitrag ist bezahlt");
        this.stream().forEach(feeData -> {
            ciBezahlt.setAmount((int) this.stream()
                    .filter(data -> !data.getBezahlt().isEmpty()).count());
        });
        list.add(ciBezahlt);
        ClubInfoData ciNichtBezahlt = new ClubInfoData();
        ciNichtBezahlt.setName("Beitrag ist nicht bezahlt");
        this.stream().forEach(feeData -> {
            ciNichtBezahlt.setAmount((int) this.stream()
                    .filter(data -> data.getBezahlt().isEmpty()).count());
        });
        list.add(ciNichtBezahlt);

        ClubInfoData ciSqOk = new ClubInfoData();
        ciSqOk.setName("Spendenquittung");
        ciSqOk.setText("Spendenquittung wurde erstellt");
        this.stream().forEach(feeData -> {
            ciSqOk.setAmount((int) this.stream()
                    .filter(data -> !data.getSpendenQ().isEmpty()).count());
        });
        list.add(ciSqOk);
        ClubInfoData ciSq = new ClubInfoData();
        ciSq.setText("Spendenquittung nicht erstellt");
        this.stream().forEach(feeData -> {
            ciSq.setAmount((int) this.stream()
                    .filter(data -> data.getSpendenQ().isEmpty()).count());
        });
        list.add(ciSq);

        return list;
    }

}
