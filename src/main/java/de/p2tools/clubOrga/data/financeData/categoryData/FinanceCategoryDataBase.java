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
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigExtra;
import de.p2tools.p2Lib.configFile.config.ConfigLongPropExtra;
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import de.p2tools.p2Lib.configFile.pData.PDataId;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Arrays;

public class FinanceCategoryDataBase extends PDataSample<FinanceCategoryData> implements PDataId {
    public static final String TAG = "FinanceCategoryData";

    private final LongProperty id = new SimpleLongProperty(0);
    private final LongProperty no = new SimpleLongProperty(0);
    private final StringProperty category = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");

    ClubConfig clubConfig;

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public ConfigExtra[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.addAll(Arrays.asList(getConfigs()));

        return list.toArray(new ConfigExtra[]{});
    }

    private Config[] getConfigs() {
        return new Config[]{
                new ConfigLongPropExtra("id", FinanceCategoryFieldNames.ID, id),
                new ConfigLongPropExtra("nr", FinanceCategoryFieldNames.NO, no),
                new ConfigStringPropExtra("kategorie", FinanceCategoryFieldNames.CATEGORY, category),
                new ConfigStringPropExtra("beschreibung", FinanceCategoryFieldNames.DESCRIPTION, description),
        };
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public long getNo() {
        return no.get();
    }

    public LongProperty noProperty() {
        return no;
    }

    public void setNo(long no) {
        this.no.set(no);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
