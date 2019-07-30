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


package de.p2tools.clubOrga.data.clubData;

import java.text.DecimalFormat;

public class ClubInfoData {
    private String name = "";
    private String amount = "";
    private String text = "";
    private boolean group1 = false;
    private boolean group2 = false;
    private final DecimalFormat df = new DecimalFormat("###,##0.00");

    public ClubInfoData() {

    }

    public ClubInfoData(String name) {
        this.name = name;
    }

    public ClubInfoData(String name, String amount, String text) {
        this.name = name;
        this.amount = amount;
        this.text = text;
    }

    public ClubInfoData(String name, long amount, String text) {
        this.name = name;
        setAmount(amount);
        this.text = text;
    }

    public ClubInfoData(String name, double amount, String text) {
        this.name = name;
        setAmount(df.format(amount) + " €");
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = String.valueOf(amount);
    }

    public void setAmount(long amount) {
        this.amount = String.valueOf(amount);
    }

    public void setAmount(double amount) {
        this.amount = String.valueOf(amount);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isGroup1() {
        return group1;
    }

    public void setGroup1(boolean group1) {
        this.group1 = group1;
    }

    public boolean isGroup2() {
        return group2;
    }

    public void setGroup2(boolean group2) {
        this.group2 = group2;
    }
}
