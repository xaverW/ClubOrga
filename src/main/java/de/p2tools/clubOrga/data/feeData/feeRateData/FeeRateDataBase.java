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


package de.p2tools.clubOrga.data.feeData.feeRateData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigExtra;
import de.p2tools.p2Lib.configFile.config.ConfigLongPropExtra;
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Arrays;

public class FeeRateDataBase extends PDataSample<FeeRateData> {
    public static final String TAG = "FeeRateData";

    private final LongProperty id = new SimpleLongProperty(0);
    private final LongProperty nr = new SimpleLongProperty(0);
    private final LongProperty betrag = new SimpleLongProperty(0);
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty text = new SimpleStringProperty("");

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
                new ConfigLongPropExtra("id", FeeRateFieldNames.ID, id),
                new ConfigLongPropExtra("nr", FeeRateFieldNames.NR, nr),
                new ConfigStringPropExtra("name", FeeRateFieldNames.NAME, name),
                new ConfigLongPropExtra("betrag", FeeRateFieldNames.BETRAG, betrag),
                new ConfigStringPropExtra("text", FeeRateFieldNames.DESCRIPTION, text),
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

    public long getNr() {
        return nr.get();
    }

    public LongProperty nrProperty() {
        return nr;
    }

    public void setNr(long nr) {
        this.nr.set(nr);
    }

    public long getBetrag() {
        return betrag.get();
    }

    public LongProperty betragProperty() {
        return betrag;
    }

    public void setBetrag(long betrag) {
        this.betrag.set(betrag);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }
}
