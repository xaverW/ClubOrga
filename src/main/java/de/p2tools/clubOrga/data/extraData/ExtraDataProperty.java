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

package de.p2tools.clubOrga.data.extraData;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.util.StringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class ExtraDataProperty {

    private final StringProperty sp = new SimpleStringProperty();

    public ExtraDataProperty(String s) {
        sp.setValue(s);
    }

    public IntegerProperty getIntProp() {
        StringConverter<Number> intConverter = new NumberStringConverter(new DecimalFormat("##")) {

            @Override
            public Number fromString(String value) {
                try {
                    if (value == null) {
                        return 0;
                    }

                    value = value.trim();
                    if (value.length() < 1) {
                        return 0;
                    }

                    NumberFormat parser = getNumberFormat();
                    return parser.parse(value);
                } catch (ParseException ex) {
                    return 0;
                }
            }

            @Override
            public String toString(Number value) {
                if (value == null) {
                    return "";
                }

                NumberFormat formatter = getNumberFormat();
                return formatter.format(value);
            }

        };

        IntegerProperty ip = new SimpleIntegerProperty();
        try {
            ip = new SimpleIntegerProperty();
            ip.set(Integer.parseInt(sp.getValueSafe()));
        } catch (NumberFormatException ex) {
            sp.setValue("0");
            ip.set(0);
        }
        Bindings.bindBidirectional(sp, ip, intConverter);

        return ip;
    }

    public BooleanProperty getBoolProp() {
        StringConverter<Boolean> boolConverter = new BooleanStringConverter() {
            @Override
            public Boolean fromString(String value) {
                // If the specified value is null or zero-length, return null
                if (value == null) {
                    return false;
                }

                value = value.trim();

                if (value.length() < 1) {
                    return false;
                }

                return Boolean.valueOf(value);
            }

            @Override
            public String toString(Boolean value) {
                // If the specified value is null, return a zero-length String
                if (value == null) {
                    return "";
                }

                return value.toString();
            }
        };

        BooleanProperty bp = new SimpleBooleanProperty();
        bp.setValue(Boolean.parseBoolean(sp.getValue()));
        Bindings.bindBidirectional(sp, bp, boolConverter);

        return bp;
    }

    public StringProperty getStringProp() {
        return sp;
    }

    @Override
    public String toString() {
        return sp.getValueSafe();
    }

}
