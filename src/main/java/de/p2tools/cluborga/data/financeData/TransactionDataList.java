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

package de.p2tools.cluborga.data.financeData;

import java.util.Collection;
import java.util.Collections;

public class TransactionDataList extends TransactionDataListBase {

    public TransactionDataList() {
        super();
    }

    @Override
    public synchronized boolean add(TransactionData transactionData) {
        addListener(transactionData);
        return super.add(transactionData);
    }


    @Override
    public synchronized boolean addAll(Collection<? extends TransactionData> transactionData) {
        transactionData.stream().forEach(tr -> addListener(tr));
        return super.addAll(transactionData);
    }

    public void sort() {
        Collections.sort(this);
    }

    private void addListener(TransactionData transactionData) {
        transactionData.changedProperty().addListener((observable, oldValue, newValue) -> {
            setListChanged();
        });
    }
}
