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


package de.p2tools.cluborga.data.extraData;

import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.config.Config_boolProp;
import de.p2tools.p2lib.configfile.config.Config_longProp;
import de.p2tools.p2lib.configfile.config.Config_stringProp;
import de.p2tools.p2lib.configfile.pdata.PDataSample;
import javafx.beans.property.*;

public class ExtraDataBase extends PDataSample<ExtraData> {

    public static final String TAG = "ExtraData";

    private final LongProperty id = new SimpleLongProperty(0);
    private final BooleanProperty on = new SimpleBooleanProperty(false); // gibt an ob das Feld benutzt wird
    private final StringProperty kind = new SimpleStringProperty(""); // ob String, int, boolean
    private final StringProperty regEx = new SimpleStringProperty(""); // zum Prüfen der Eingabe
    private final StringProperty name = new SimpleStringProperty(""); // angezeigte Feldname
    private final StringProperty initValue = new SimpleStringProperty(""); // init-Wert

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        return new Config[]{
                new Config_longProp("id", id),
                new Config_boolProp("on", on),
                new Config_stringProp("kind", kind),
                new Config_stringProp("regEx", regEx),
                new Config_stringProp("name", name),
                new Config_stringProp("initValue", initValue)
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

    public boolean isOn() {
        return on.get();
    }

    public BooleanProperty onProperty() {
        return on;
    }

    public void setOn(boolean on) {
        this.on.set(on);
    }

    public String getKey() {
        // muss für XML mit einem String beginnen
        return getTag() + "-" + id.get();
    }

    public String getKind() {
        return kind.get();
    }

    public StringProperty kindProperty() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind.set(kind);
    }

    public String getRegEx() {
        return regEx.get();
    }

    public StringProperty regExProperty() {
        return regEx;
    }

    public void setRegEx(String regEx) {
        this.regEx.set(regEx);
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

    public String getInitValue() {
        return initValue.get();
    }

    public StringProperty initValueProperty() {
        return initValue;
    }

    public void setInitValue(String initValue) {
        this.initValue.set(initValue);
    }
}
