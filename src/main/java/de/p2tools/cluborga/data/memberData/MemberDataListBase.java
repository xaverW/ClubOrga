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
import de.p2tools.p2lib.configfile.pdata.PDataList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.Collection;
import java.util.function.Predicate;

public class MemberDataListBase extends SimpleListProperty<MemberData> implements PDataList<MemberData> {

    public static final String TAG = "MemberDataList";
    private BooleanProperty listChanged = new SimpleBooleanProperty(true);

    private FilteredList<MemberData> filteredList = null;
    private SortedList<MemberData> sortedList = null;

    final ClubConfig clubConfig;

    public MemberDataListBase(ClubConfig clubConfig) {
        super(FXCollections.observableArrayList());
        this.clubConfig = clubConfig;
    }

    public SortedList<MemberData> getSortedList() {
        if (sortedList == null) {
            sortedList = new SortedList<>(getFilteredList());
        }
        return sortedList;
    }

    public FilteredList<MemberData> getFilteredList() {
        if (filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
        }
        return filteredList;
    }

    public synchronized void filterdListSetPred(Predicate<MemberData> predicate) {
        filteredList.setPredicate(predicate);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste aller Mitglieder";
    }

    @Override
    public MemberData getNewItem() {
        return new MemberData(clubConfig);
    }


    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(MemberData.class)) {
            super.add((MemberData) obj);
        }
    }

    public void initAfterAdding() {
//        sort();
        setListChanged();
    }

    public void initAfterRemoval() {
        setListChanged();
    }

    @Override
    public boolean add(MemberData elements) {
        boolean ret = super.add(elements);
        initAfterAdding();
        return ret;
    }

    @Override
    public boolean addAll(Collection<? extends MemberData> elements) {
        boolean ret = super.addAll(elements);
        initAfterAdding();
        return ret;
    }

    @Override
    public boolean addAll(int i, Collection<? extends MemberData> elements) {
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
        this.stream().forEach(memberData -> memberData.setSelected(false));
    }

//    public void sort() {
//        Collections.sort(this);
//    }

    public long getNextNr() {
        long nr = 1;
        for (MemberData memberData : this) {
            if (memberData.getNo() >= nr) {
                nr = memberData.getNo() + 1;
            }
        }
        return nr;
    }

}
