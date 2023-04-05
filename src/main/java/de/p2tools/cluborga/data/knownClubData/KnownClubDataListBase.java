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

package de.p2tools.cluborga.data.knownClubData;

import de.p2tools.p2lib.configfile.pdata.PDataList;
import javafx.beans.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.Collections;

public class KnownClubDataListBase extends SimpleListProperty<KnownClubData> implements PDataList<KnownClubData> {

    public static final String TAG = "KnownClubDataList";


    public KnownClubDataListBase() {
        super(FXCollections.observableArrayList(callback ->
                new Observable[]{callback.clubnameProperty()}));
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "list of the clubs in the program";
    }


    @Override
    public KnownClubData getNewItem() {
        return new KnownClubData();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(KnownClubData.class)) {
            add((KnownClubData) obj);
        }
    }

    public void sort() {
        Collections.sort(this);
    }

}
