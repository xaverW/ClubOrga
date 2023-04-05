/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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


package de.p2tools.cluborga.data.memberData.stateData;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgConst;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.tools.PIndex;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class StateData extends StateDataBase {

    public StateData() {
        setId(getNewId());
    }

    public StateData(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
        setId(getNewId());
        this.setNo(clubConfig.stateDataList.getNextNr());
    }

    private long getNewId() {
        return PIndex.getIndex();
    }


    /**
     * copy stateData FROM to the stateData TO
     *
     * @param dataFrom
     * @param dataTo
     */
    public static void copyData(StateData dataFrom, StateData dataTo) {
        if (dataFrom == null || dataTo == null) {
            return;
        }

        Config[] configs = dataFrom.getConfigsArr();
        Config[] configsCopy = dataTo.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }

    public ObjectProperty<StateData> getObjectProperty(LongProperty longProperty) {
        return getObjectProperty(longProperty, this);
    }

    public static ObjectProperty<StateData> getObjectProperty(LongProperty longProperty, StateData stateData) {
        ObjectProperty<StateData> stateDataObjectProperty = new SimpleObjectProperty<>(stateData);
        stateDataObjectProperty.addListener((v, o, n) -> {
            if (n != null) {
                longProperty.setValue(n.getId());
            } else {
                longProperty.setValue(ProgConst.FILTER_ID_NOT_SELECTED);
            }
        });
        return stateDataObjectProperty;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(StateData o) {
        return o.getName().compareTo(getName());
    }
}
