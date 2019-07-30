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

import de.p2tools.p2Lib.configFile.pData.PDataList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.Collection;
import java.util.Collections;

public class TransactionDataListBase extends SimpleListProperty<TransactionData> implements PDataList<TransactionData> {

    public static final String TAG = "TransactionDataList";
    private BooleanProperty listChanged = new SimpleBooleanProperty(true);

    public TransactionDataListBase() {
        super(FXCollections.observableArrayList());
    }

    public String getTag() {
        return TAG;
    }

    public String getComment() {
        return "Liste der Teilbuchungen";
    }


    public TransactionData getNewItem() {
        return new TransactionData();
    }


    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(TransactionData.class)) {
            add((TransactionData) obj);
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

    @Override
    public synchronized boolean add(TransactionData transactionData) {
        addListener(transactionData);
        boolean ret = super.add(transactionData);
        setListChanged();
        return ret;
    }


    @Override
    public synchronized boolean addAll(Collection<? extends TransactionData> transactionData) {
        transactionData.stream().forEach(tr -> addListener(tr));
        boolean ret = super.addAll(transactionData);
        setListChanged();
        return ret;
    }

    public void sort() {
        Collections.sort(this);
    }

    private void addListener(TransactionData transactionData) {
        transactionData.changedProperty().addListener((observable, oldValue, newValue) -> {
            setListChanged();
        });
    }

    public long getNextNr() {
        long no = 1;
        for (TransactionData data : this) {
            if (data.getNr() >= no) {
                no = data.getNr() + 1;
            }
        }
        return no;
    }

    public TransactionData getLast() {
        if (this.isEmpty()) {
            return null;
        } else {
            return this.get(this.getSize() - 1);
        }
    }
}
