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

package de.p2tools.cluborga.data.feeData;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.p2lib.configfile.pdata.PDataList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;

public class FeeDataListBase extends SimpleListProperty<FeeData> implements PDataList<FeeData> {

    public static final String TAG = "FeeDataList";
    BooleanProperty listChanged = new SimpleBooleanProperty(true);

    FilteredList<FeeData> filteredList = null;
    SortedList<FeeData> sortedList = null;
    ObservableList<Integer> jahrList = FXCollections.observableArrayList();

    final ClubConfig clubConfig;

    public FeeDataListBase(ClubConfig clubConfig) {
        super(FXCollections.observableArrayList());
        this.clubConfig = clubConfig;
    }

    public SortedList<FeeData> getSortedList() {
        if (sortedList == null) {
            sortedList = new SortedList<>(getFilteredList());
        }
        return sortedList;
    }

    public FilteredList<FeeData> getFilteredList() {
        if (filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
        }
        return filteredList;
    }

    public synchronized void filterdListSetPred(Predicate<FeeData> predicate) {
        filteredList.setPredicate(predicate);
    }

    public String getTag() {
        return TAG;
    }

    public String getComment() {
        return "Liste aller Beiträge";
    }

    public FeeData getNewItem() {
        return new FeeData(clubConfig);
    }

    public ObservableList<Integer> getJahrList() {
        return jahrList;
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(FeeData.class)) {
            super.add((FeeData) obj);
        }
    }

    public void initAfterAdding() {
        makeJahrList();
        setListChanged();
    }

    public void initAfterRemoval() {
        makeJahrList();
        setListChanged();
    }

    @Override
    public boolean add(FeeData feeData) {
        boolean ret = super.add(feeData);
        initAfterAdding();
        return ret;
    }

    @Override
    public boolean addAll(Collection<? extends FeeData> elements) {
        boolean ret = super.addAll(elements);
        initAfterAdding();
        return ret;
    }

    @Override
    public boolean addAll(int i, Collection<? extends FeeData> elements) {
        boolean ret = super.addAll(i, elements);
        initAfterAdding();
        return ret;
    }

    @Override
    public boolean remove(Object obj) {
        boolean ret = super.remove(obj);
        initAfterRemoval();
        return ret;
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        boolean ret = super.removeAll(objects);
        initAfterRemoval();
        return ret;
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
        long nr = 1;
        for (FeeData data : this) {
            if (data.getNo() >= nr) {
                nr = data.getNo() + 1;
            }
        }
        return nr;
    }

    private void makeJahrList() {
        this.stream().forEach(feeData -> {
            final int jahr = feeData.getJahr();
            Optional<Integer> jj = jahrList.stream().filter(j -> j.equals(jahr)).findAny();
            if (!jj.isPresent()) {
                jahrList.add(jahr);
            }
        });

        Collections.sort(jahrList);
        Collections.reverse(jahrList);
    }
}
