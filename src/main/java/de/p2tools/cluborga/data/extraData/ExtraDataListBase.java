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

package de.p2tools.cluborga.data.extraData;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.p2lib.configfile.pdata.PDataList;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.Collections;

public class ExtraDataListBase extends SimpleListProperty<ExtraData> implements PDataList<ExtraData> {

    public static final String TAG = "ExtraDataList";
    final ClubConfig clubConfig;

    public ExtraDataListBase(ClubConfig clubConfig) {
        super(FXCollections.observableArrayList());
        this.clubConfig = clubConfig;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "list of extra data";
    }

    @Override
    public ExtraData getNewItem() {
        return new ExtraData();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(ExtraData.class)) {
            add((ExtraData) obj);
        }
    }

    public void sort() {
        Collections.sort(this);
    }

}
