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


package de.p2tools.clubOrga.data.financeData.categoryData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.PIndex;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class FinanceCategoryData extends FinanceCategoryDataBase {

    public FinanceCategoryData() {
        setId(getNewId());
    }

    public FinanceCategoryData(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
        setId(getNewId());
        this.setNr(clubConfig.financeCategoryDataList.getNextNr());
    }

    private long getNewId() {
        return PIndex.getIndex();
    }

    /**
     * copy data FROM to the data TO
     *
     * @param dataFrom
     * @param dataTo
     */
    public static void copyData(FinanceCategoryData dataFrom, FinanceCategoryData dataTo) {
        if (dataFrom == null || dataTo == null) {
            return;
        }

        Config[] configs = dataFrom.getConfigsArr();
        Config[] configsCopy = dataTo.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }

    public ObjectProperty<FinanceCategoryData> getObjectProperty(LongProperty longProperty) {
        return getObjectProperty(longProperty, this);
    }

    public static ObjectProperty<FinanceCategoryData> getObjectProperty(LongProperty longProperty, FinanceCategoryData data) {
        ObjectProperty<FinanceCategoryData> stateDataObjectProperty = new SimpleObjectProperty<>(data);
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
        return getKategorie();
    }

    @Override
    public int compareTo(FinanceCategoryData o) {
        return o.getKategorie().compareTo(getKategorie());
    }
}
