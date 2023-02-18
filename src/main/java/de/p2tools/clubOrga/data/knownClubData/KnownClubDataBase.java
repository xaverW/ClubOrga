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


package de.p2tools.clubOrga.data.knownClubData;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.Config_boolProp;
import de.p2tools.p2Lib.configFile.config.Config_stringProp;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class KnownClubDataBase extends PDataSample<KnownClubData> {
    public static final String TAG = "KnownClubData";

    private final StringProperty clubname = new SimpleStringProperty("");
    private final StringProperty clubpath = new SimpleStringProperty("");
    private final BooleanProperty autostart = new SimpleBooleanProperty(false);

    // nur für den ersten Start interessant
    private BooleanProperty addDemoData = new SimpleBooleanProperty(false);


    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        return new Config[]{
                new Config_stringProp("clubname", clubname),
                new Config_stringProp("clubpath", clubpath),
                new Config_boolProp("autostart", autostart),
        };
    }

    // =========================
    // nur für den ersten Start interessant
    public boolean isAddDemoData() {
        return addDemoData.get();
    }

    public BooleanProperty addDemoDataProperty() {
        return addDemoData;
    }

    public void setAddDemoData(boolean addDemoData) {
        this.addDemoData.set(addDemoData);
    }
    // =========================

    public String getClubname() {
        return clubname.get();
    }

    public StringProperty clubnameProperty() {
        return clubname;
    }

    public void setClubname(String clubname) {
        this.clubname.set(clubname);
    }

    public String getClubpath() {
        return clubpath.get();
    }

    public StringProperty clubpathProperty() {
        return clubpath;
    }

    public void setClubpath(String clubpath) {
        this.clubpath.set(clubpath);
    }

    public boolean isAutostart() {
        return autostart.get();
    }

    public BooleanProperty autostartProperty() {
        return autostart;
    }

    public void setAutostart(boolean autostart) {
        this.autostart.set(autostart);
    }

}
