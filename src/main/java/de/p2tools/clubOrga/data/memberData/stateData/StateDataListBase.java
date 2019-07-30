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

package de.p2tools.clubOrga.data.memberData.stateData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.p2Lib.configFile.pData.PDataList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.function.Predicate;

public class StateDataListBase extends SimpleListProperty<StateData> implements PDataList<StateData> {

    public static final String TAG = "StateDataList";
    private BooleanProperty listChanged = new SimpleBooleanProperty(true);

    private FilteredList<StateData> filteredList = null;
    private SortedList<StateData> sortedList = null;

    final ClubConfig clubConfig;

    public StateDataListBase(ClubConfig clubConfig) {
        super(FXCollections.observableArrayList());
        this.clubConfig = clubConfig;
    }

    public SortedList<StateData> getSortedList() {
        if (sortedList == null) {
            sortedList = new SortedList<>(getFilteredList());
        }
        return sortedList;
    }

    public FilteredList<StateData> getFilteredList() {
        if (filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
        }
        return filteredList;
    }

    public synchronized void filterdListSetPred(Predicate<StateData> predicate) {
        filteredList.setPredicate(predicate);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste der Mitgliedsstati";
    }

    @Override
    public StateData getNewItem() {
        return new StateData(clubConfig);
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(StateData.class)) {
            add((StateData) obj);
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
        for (StateData data : this) {
            if (data.getNr() >= no) {
                no = data.getNr() + 1;
            }
        }
        return no;
    }
}
