/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.clubOrga.data.financeData;

import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.date.PLocalDateProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.List;

public class FinanceReportDataBase {

    private final LongProperty nr = new SimpleLongProperty(0);
    private final StringProperty belegNr = new SimpleStringProperty("");

    private final StringProperty text = new SimpleStringProperty("");

    private final LongProperty gesamtbetrag = new SimpleLongProperty(0);
    private final IntegerProperty geschaeftsJahr = new SimpleIntegerProperty(PDateFactory.getAktYearInt());

    private final PLocalDateProperty buchungsDatum = new PLocalDateProperty();


    private final List<FinanceReportAccountData> accountList;
    private final List<FinanceReportCategoryData> categoryList;
    private final FinanceData financeData;
    private boolean selected = false;

    public FinanceReportDataBase(FinanceData financeData) {
        this.financeData = financeData;
        this.accountList = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.categoryList = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public FinanceData getFinanceData() {
        return financeData;
    }

    public List<FinanceReportAccountData> getAccountList() {
        return accountList;
    }

    public List<FinanceReportCategoryData> getCategoryList() {
        return categoryList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public String getBelegNr() {
        return belegNr.get();
    }

    public StringProperty belegNrProperty() {
        return belegNr;
    }

    public void setBelegNr(String belegNr) {
        this.belegNr.set(belegNr);
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

    public long getGesamtbetrag() {
        return gesamtbetrag.get();
    }

    public LongProperty gesamtbetragProperty() {
        return gesamtbetrag;
    }

    public void setGesamtbetrag(long gesamtbetrag) {
        this.gesamtbetrag.set(gesamtbetrag);
    }

    public int getGeschaeftsJahr() {
        return geschaeftsJahr.get();
    }

    public IntegerProperty geschaeftsJahrProperty() {
        return geschaeftsJahr;
    }

    public void setGeschaeftsJahr(int geschaeftsJahr) {
        this.geschaeftsJahr.set(geschaeftsJahr);
    }

    public PLocalDate getBuchungsDatum() {
        return buchungsDatum.get();
    }

    public PLocalDateProperty buchungsDatumProperty() {
        return buchungsDatum;
    }

    public void setBuchungsDatum(PLocalDate buchungsDatum) {
        this.buchungsDatum.set(buchungsDatum);
    }

}
