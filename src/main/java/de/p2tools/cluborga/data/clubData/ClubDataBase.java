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


package de.p2tools.cluborga.data.clubData;

import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.config.Config_lDate;
import de.p2tools.p2lib.configfile.config.Config_stringProp;
import de.p2tools.p2lib.configfile.pdata.PDataSample;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class ClubDataBase extends PDataSample<ClubData> {

    public static final String TAG = "ClubData";

    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty ort = new SimpleStringProperty("");

    private final StringProperty plz = new SimpleStringProperty("");
    private final StringProperty strasse = new SimpleStringProperty("");
    private final StringProperty telefon = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty website = new SimpleStringProperty("");

    private final StringProperty kontoNr = new SimpleStringProperty("");
    private final StringProperty bic = new SimpleStringProperty("");
    private final StringProperty iban = new SimpleStringProperty("");
    private final StringProperty bank = new SimpleStringProperty("");

    private final StringProperty glaeubigerId = new SimpleStringProperty("");
    private LocalDate erstellDatum = LocalDate.now();

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        return new Config[]{
                new Config_stringProp("name", ClubFieldNames.NAME, name),
                new Config_stringProp("ort", ClubFieldNames.ORT, ort),
                new Config_stringProp("plz", ClubFieldNames.PLZ, plz),
                new Config_stringProp("strasse", ClubFieldNames.STRASSE, strasse),
                new Config_stringProp("telefon", ClubFieldNames.TELEFON, telefon),
                new Config_stringProp("email", ClubFieldNames.EMAIL, email),
                new Config_stringProp("website", ClubFieldNames.WEBSITE, website),
                new Config_stringProp("kontoNr", ClubFieldNames.KONTONR, kontoNr),
                new Config_stringProp("Bic", ClubFieldNames.BIC, bic),
                new Config_stringProp("Iban", ClubFieldNames.IBAN, iban),
                new Config_stringProp("Bank", ClubFieldNames.BANK, bank),
                new Config_stringProp("glaeubigerId", ClubFieldNames.GLAEUBIGER_ID, glaeubigerId),
                new Config_lDate("erstellDatum", ClubFieldNames.ERSTELLDATUM, erstellDatum) {
                    @Override
                    public void setUsedValue(LocalDate act) {
                        erstellDatum = act;
                    }
                },
        };
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

    public String getOrt() {
        return ort.get();
    }

    public StringProperty ortProperty() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort.set(ort);
    }

    public String getPlz() {
        return plz.get();
    }

    public StringProperty plzProperty() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz.set(plz);
    }

    public String getStrasse() {
        return strasse.get();
    }

    public StringProperty strasseProperty() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse.set(strasse);
    }

    public String getTelefon() {
        return telefon.get();
    }

    public StringProperty telefonProperty() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon.set(telefon);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getWebsite() {
        return website.get();
    }

    public StringProperty websiteProperty() {
        return website;
    }

    public void setWebsite(String website) {
        this.website.set(website);
    }

    public String getKontoNr() {
        return kontoNr.get();
    }

    public StringProperty kontoNrProperty() {
        return kontoNr;
    }

    public void setKontoNr(String kontoNr) {
        this.kontoNr.set(kontoNr);
    }

    public String getBic() {
        return bic.get();
    }

    public StringProperty bicProperty() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic.set(bic);
    }

    public String getIban() {
        return iban.get();
    }

    public StringProperty ibanProperty() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban.set(iban);
    }

    public String getBank() {
        return bank.get();
    }

    public StringProperty bankProperty() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank.set(bank);
    }

    public String getGlaeubigerId() {
        return glaeubigerId.get();
    }

    public StringProperty glaeubigerIdProperty() {
        return glaeubigerId;
    }

    public void setGlaeubigerId(String glaeubigerId) {
        this.glaeubigerId.set(glaeubigerId);
    }

    public LocalDate getErstellDatum() {
        return erstellDatum;
    }
}
