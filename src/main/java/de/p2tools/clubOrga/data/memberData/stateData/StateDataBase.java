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


package de.p2tools.clubOrga.data.memberData.stateData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.Config_longProp;
import de.p2tools.p2Lib.configFile.config.Config_stringProp;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Arrays;

public class StateDataBase extends PDataSample<StateData> {
    public static final String TAG = "StateData";

    private final LongProperty id = new SimpleLongProperty(0);
    private final LongProperty no = new SimpleLongProperty(0);
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty text = new SimpleStringProperty("");

    ClubConfig clubConfig;

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.addAll(Arrays.asList(getConfigs()));

        return list.toArray(new Config[]{});
    }

    private Config[] getConfigs() {
        return new Config[]{
                new Config_longProp("id", StateFieldNames.ID, id),
                new Config_longProp("nr", StateFieldNames.NO, no),
                new Config_stringProp("name", StateFieldNames.NAME, name),
                new Config_stringProp("text", StateFieldNames.DESCRIPTION, text),
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
