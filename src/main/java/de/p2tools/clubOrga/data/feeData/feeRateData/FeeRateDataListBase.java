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

package de.p2tools.clubOrga.data.feeData.feeRateData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.p2Lib.configFile.pData.PDataList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.function.Predicate;

public class FeeRateDataListBase extends SimpleListProperty<FeeRateData> implements PDataList<FeeRateData> {

    public static final String TAG = "FeeRateDataList";
    private BooleanProperty listChanged = new SimpleBooleanProperty(true);

    private FilteredList<FeeRateData> filteredList = null;
    private SortedList<FeeRateData> sortedList = null;

    final ClubConfig clubConfig;

    public FeeRateDataListBase(ClubConfig clubConfig) {
        super(FXCollections.observableArrayList());
        this.clubConfig = clubConfig;
    }

    public SortedList<FeeRateData> getSortedList() {
        if (sortedList == null) {
            sortedList = new SortedList<>(getFilteredList());
        }
        return sortedList;
    }

    public FilteredList<FeeRateData> getFilteredList() {
        if (filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
        }
        return filteredList;
    }

    public synchronized void filterdListSetPred(Predicate<FeeRateData> predicate) {
        filteredList.setPredicate(predicate);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste Beitragssätze";
    }

    @Override
    public FeeRateData getNewItem() {
        return new FeeRateData(clubConfig);
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(FeeRateData.class)) {
            add((FeeRateData) obj);
        }
    }

    public boolean isListChanged() {
        return listChanged.get();
    }

    public BooleanProperty listChangedProperty() {
        return listChanged;
    }

    public void setListChanged() {
        this.listChanged.set(!listChanged.get());
    }

//    public void sort() {
//        Collections.sort(this);
//    }

    public long getNextNr() {
        long no = 1;
        for (FeeRateData data : this) {
            if (data.getNo() >= no) {
                no = data.getNo() + 1;
            }
        }
        return no;
    }
}
