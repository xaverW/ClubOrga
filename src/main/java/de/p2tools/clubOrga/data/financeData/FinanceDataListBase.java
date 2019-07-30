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

package de.p2tools.clubOrga.data.financeData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.p2Lib.configFile.pData.PDataList;
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

public class FinanceDataListBase extends SimpleListProperty<FinanceData> implements PDataList<FinanceData> {

    public static final String TAG = "FinanceDataList";
    private BooleanProperty listChanged = new SimpleBooleanProperty(true);

    private FilteredList<FinanceData> filteredList = null;
    private SortedList<FinanceData> sortedList = null;
    ObservableList<Integer> geschaeftsJahrList = FXCollections.observableArrayList();

    final ClubConfig clubConfig;

    public FinanceDataListBase(ClubConfig clubConfig) {
        super(FXCollections.observableArrayList());
        this.clubConfig = clubConfig;
    }

    public SortedList<FinanceData> getSortedList() {
        if (sortedList == null) {
            sortedList = new SortedList<>(getFilteredList());
        }
        return sortedList;
    }

    public FilteredList<FinanceData> getFilteredList() {
        if (filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
        }
        return filteredList;
    }

    public synchronized void filterdListSetPred(Predicate<FinanceData> predicate) {
        filteredList.setPredicate(predicate);
    }

    public String getTag() {
        return TAG;
    }

    public String getComment() {
        return "Liste aller Finanzen";
    }

    public FinanceData getNewItem() {
        return new FinanceData(clubConfig);
    }

    public ObservableList<Integer> getGeschaeftsJahrList() {
        return geschaeftsJahrList;
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(FinanceData.class)) {
            super.add((FinanceData) obj);
        }
    }

    public void initAfterAdding() {
        makeJahrList();
//        sort();
        setListChanged();
    }

    public void initAfterRemoval() {
        makeJahrList();
        setListChanged();
    }

    @Override
    public boolean add(FinanceData financeData) {
        boolean ret = super.add(financeData);
        initAfterAdding();
        return ret;
    }

    @Override
    public boolean addAll(Collection<? extends FinanceData> elements) {
        boolean ret = super.addAll(elements);
        initAfterAdding();
        return ret;
    }

    @Override
    public boolean addAll(int i, Collection<? extends FinanceData> elements) {
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

    public void clearSelected() {
        this.stream().forEach(financeData -> financeData.setSelected(false));
    }

//    public void sort() {
//        Collections.sort(this);
//    }

    public long getNextNr() {
        long no = 1;
        for (FinanceData data : this) {
            if (data.getNr() >= no) {
                no = data.getNr() + 1;
            }
        }
        return no;
    }

    private void makeJahrList() {
        this.stream().forEach(feeData -> {
            final int jahr = feeData.getGeschaeftsJahr();
            Optional<Integer> jj = geschaeftsJahrList.stream().filter(j -> j.equals(jahr)).findAny();
            if (!jj.isPresent()) {
                geschaeftsJahrList.add(jahr);
            }
        });

        Collections.sort(geschaeftsJahrList);
        Collections.reverse(geschaeftsJahrList);
    }
}
