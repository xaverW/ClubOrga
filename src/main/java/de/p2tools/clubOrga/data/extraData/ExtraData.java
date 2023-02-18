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


package de.p2tools.clubOrga.data.extraData;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.Config_boolProp;
import de.p2tools.p2Lib.configFile.config.Config_intProp;
import de.p2tools.p2Lib.configFile.config.Config_stringProp;
import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2Lib.tools.PIndex;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExtraData extends ExtraDataBase {

    public ExtraData() {
    }

    public ExtraData(String kind, String name, String initValue, String regEx) {
        // ein neues ExtraData wird im Dialog angelegt
        setKind(kind);
        setName(name);
        setInitValue(initValue);
        setRegEx(regEx);
        setId(PIndex.getIndex());
    }

    public ExtraDataProperty getProperty() {
        return new ExtraDataProperty(getInitValue());
    }

    public String getKindSave() {
        if (getKind().equals(ExtraKind.EXTRA_KIND.STRING.toString())) {
            return getKind();

        } else if (getKind().equals(ExtraKind.EXTRA_KIND.INTEGER.toString())) {
            return getKind();

        } else if (getKind().equals(ExtraKind.EXTRA_KIND.BOOLEAN.toString())) {
            return getKind();

        } else {
            setKind(ExtraKind.EXTRA_KIND.STRING.toString());
            PLog.errorLog(951241524, "no kind in extraData");
            return getKind();
        }
    }

    private SimpleStringProperty getPropertyString() {
        return new SimpleStringProperty(getInitValue());
    }

    private SimpleIntegerProperty getPropertyInt() {
        int initValue;
        try {
            initValue = Integer.parseInt(getInitValue());
        } catch (Exception ex) {
            initValue = 0;
        }

        return new SimpleIntegerProperty(initValue);

    }

    private SimpleBooleanProperty getPropertyBool() {
        return new SimpleBooleanProperty(Boolean.parseBoolean(getInitValue()));
    }


    public Config getConfig(ExtraDataProperty property) {
        Config config;

        if (getKindSave().equals(ExtraKind.EXTRA_KIND.STRING.toString())) {
            config = new Config_stringProp(getKey(), getName(), getRegEx(), property.getStringProp());

        } else if (getKindSave().equals(ExtraKind.EXTRA_KIND.INTEGER.toString())) {
            config = new Config_intProp(getKey(), getName(), property.getIntProp());

        } else if (getKindSave().equals(ExtraKind.EXTRA_KIND.BOOLEAN.toString())) {
            config = new Config_boolProp(getKey(), getName(), property.getBoolProp());

        } else {
            throw new PException("ExtraData getConfig");
        }

        return config;
    }

}
