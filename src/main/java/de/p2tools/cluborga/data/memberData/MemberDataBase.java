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


package de.p2tools.cluborga.data.memberData;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.data.extraData.ExtraData;
import de.p2tools.cluborga.data.extraData.ExtraDataProperty;
import de.p2tools.cluborga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.cluborga.data.feeData.paymentType.PaymentTypeData;
import de.p2tools.cluborga.data.memberData.stateData.StateData;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.config.Config_lDate;
import de.p2tools.p2lib.configfile.config.Config_longProp;
import de.p2tools.p2lib.configfile.config.Config_stringProp;
import de.p2tools.p2lib.configfile.pdata.PDataSample;
import javafx.beans.property.*;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class MemberDataBase extends PDataSample<MemberData> {
    private static final DecimalFormat DF;

    static {
        DF = new DecimalFormat("###,##0.00");
    }

    public static final String TAG = "MemberData";

    private final LongProperty id = new SimpleLongProperty(0);
    private final LongProperty no = new SimpleLongProperty(0);
    private final StringProperty nachname = new SimpleStringProperty("");
    private final StringProperty vorname = new SimpleStringProperty("");

    private final StringProperty anrede = new SimpleStringProperty("");
    private final StringProperty text = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty telefon = new SimpleStringProperty("");

    private final StringProperty strasse = new SimpleStringProperty("");
    private final StringProperty plz = new SimpleStringProperty();
    private final StringProperty ort = new SimpleStringProperty("");
    private final StringProperty land = new SimpleStringProperty("");

    private final LongProperty status = new SimpleLongProperty(0);
    private final LongProperty beitragssatz = new SimpleLongProperty(0);
    private final LongProperty beitrag = new SimpleLongProperty(0);
    private final LongProperty paymentType = new SimpleLongProperty(0);
    private final StringProperty bank = new SimpleStringProperty("");
    private final StringProperty iban = new SimpleStringProperty("");
    private final StringProperty bic = new SimpleStringProperty("");
    private final StringProperty kontoinhaber = new SimpleStringProperty("");
    private LocalDate zahlungsbeginn = LocalDate.now();
    private LocalDate sepaBeginn = LocalDate.now();
    private LocalDate beitritt = LocalDate.now();
    private LocalDate erstellDatum = LocalDate.now();

    private ArrayList<ExtraDataProperty> extraDataPropertyList = new ArrayList<>();

    private final ObjectProperty<StateData> stateData = new SimpleObjectProperty<>();
    private final ObjectProperty<FeeRateData> feeRateData = new SimpleObjectProperty<>();
    private final ObjectProperty<PaymentTypeData> paymentTypeData = new SimpleObjectProperty<>();

    private boolean selected = false;
    ClubConfig clubConfig;

    public MemberDataBase() {
        stateData.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setStatus(newValue.getId());
            }
        });
        feeRateData.addListener((obs, old, newV) -> {
            if (newV != null) {
                setBeitragssatz(newV.getId());
            }
        });
        paymentTypeData.addListener((obs, old, newV) -> {
            if (newV != null) {
                setPaymentType(newV.getId());
            }
        });
    }

    @Override
    public String getTag() {
        return TAG;
    }

    void addExtra() {
        extraDataPropertyList.clear();
        for (ExtraData extraData : clubConfig.extraDataListMember) {
            extraDataPropertyList.add(extraData.getProperty());
        }
    }


    public Config[] getConfigsForNewsletter() {
        return getConfigsForNewsletter(false);
    }

    public Config[] getConfigsForNewsletter(boolean csvExport) {
        Config[] arr = getConfigsArr();
        ArrayList<Config> list = new ArrayList<>();

        if (!csvExport) {
            list.add(new Config_stringProp("iban", MemberFieldNames.IBAN_SHORT,
                    new SimpleStringProperty(MemberFactory.getShortIban(iban.getValueSafe()))));
        }

        for (Config config : arr) {
            if (config.getName().equals(MemberFieldNames.BEITRAG)) {
                StringProperty st = new SimpleStringProperty();
                double d = 0.01 * getBeitrag();
                st.setValue(new NumberStringConverter(DF).toString(d));
                Config_stringProp conf = new Config_stringProp("beitrag",
                        MemberFieldNames.BEITRAG, st);
                list.add(conf);

            } else if (config.getName().equals(MemberFieldNames.STATUS)) {
                StringProperty st = new SimpleStringProperty();
                st.setValue(stateData.get().getName());
                Config_stringProp conf = new Config_stringProp("status",
                        MemberFieldNames.STATUS, st);
                list.add(conf);

            } else if (config.getName().equals(MemberFieldNames.BEITRAGSSATZ)) {
                StringProperty st = new SimpleStringProperty();
                st.setValue(feeRateData.get().getName());
                Config_stringProp conf = new Config_stringProp("beitragssatz",
                        MemberFieldNames.BEITRAGSSATZ, st);
                list.add(conf);

            } else if (config.getName().equals(MemberFieldNames.ZAHLART)) {
                StringProperty st = new SimpleStringProperty();
                st.setValue(paymentTypeData.get().getName());
                Config_stringProp conf = new Config_stringProp("zahlart",
                        MemberFieldNames.ZAHLART, st);
                list.add(conf);

            } else {
                list.add(config);
            }
        }

        return list.toArray(new Config[]{});
    }

    @Override
    public Config[] getConfigsArr() {
        if (getStateData() != null) {
            setStatus(getStateData().getId());
        }

        if (getFeeRateData() != null) {
            setBeitragssatz(getFeeRateData().getId());
        }

        if (getPaymentTypeData() != null) {
            setPaymentType(getPaymentTypeData().getId());
        }

        ArrayList<Config> list = new ArrayList<>();
        list.add(new Config_longProp("id", MemberFieldNames.ID, id));
        list.add(new Config_longProp("nr", MemberFieldNames.NR, no));
        list.add(new Config_stringProp("nachname", MemberFieldNames.NACHNAME, nachname));
        list.add(new Config_stringProp("vorname", MemberFieldNames.VORNAME, vorname));

        list.add(new Config_stringProp("anrede", MemberFieldNames.ANREDE, anrede));
        list.add(new Config_stringProp("text", MemberFieldNames.TEXT, text));
        list.add(new Config_stringProp("email", MemberFieldNames.EMAIL, email));
        list.add(new Config_stringProp("telefon", MemberFieldNames.TELEFON, telefon));

        list.add(new Config_stringProp("strasse", MemberFieldNames.STRASSE, strasse));
        list.add(new Config_stringProp("plz", MemberFieldNames.PLZ, MemberFieldNames.PLZ_REGEX, plz));
        list.add(new Config_stringProp("ort", MemberFieldNames.ORT, ort));
        list.add(new Config_stringProp("land", MemberFieldNames.LAND, land));

        list.add(new Config_longProp("status", MemberFieldNames.STATUS, status));
        list.add(new Config_longProp("beitragssatz", MemberFieldNames.BEITRAGSSATZ, beitragssatz));
        list.add(new Config_longProp("beitrag", MemberFieldNames.BEITRAG, beitrag));
        list.add(new Config_stringProp("bank", MemberFieldNames.BANK, bank));
        list.add(new Config_stringProp("iban", MemberFieldNames.IBAN, iban));
        list.add(new Config_stringProp("bic", MemberFieldNames.BIC, bic));
        list.add(new Config_stringProp("kontoinhaber", MemberFieldNames.KONTOINHABER, kontoinhaber));
        list.add(new Config_longProp("zahlart", MemberFieldNames.ZAHLART, paymentType));
        list.add(new Config_lDate("zahlungsbeginn", MemberFieldNames.ZAHLUNGSBEGINN, zahlungsbeginn) {
            @Override
            public void setUsedValue(LocalDate act) {
                zahlungsbeginn = act;
            }
        });
        list.add(new Config_lDate("sepabeginn", MemberFieldNames.SEPABEGINN, sepaBeginn) {
            @Override
            public void setUsedValue(LocalDate act) {
                sepaBeginn = act;
            }
        });
        list.add(new Config_lDate("beitritt", MemberFieldNames.BEITRITT, beitritt) {
            @Override
            public void setUsedValue(LocalDate act) {
                beitritt = act;
            }
        });
        list.add(new Config_lDate("erstellDatum", MemberFieldNames.ERSTELLDATUM, erstellDatum) {
            @Override
            public void setUsedValue(LocalDate act) {
                erstellDatum = act;
            }
        });

        for (int i = 0; i < clubConfig.extraDataListMember.size(); ++i) {
            ExtraData extraData = clubConfig.extraDataListMember.get(i);
            ExtraDataProperty extraDataProperty = extraDataPropertyList.get(i);
            list.add(extraData.getConfig(extraDataProperty));
        }

        return list.toArray(new Config[]{});
    }

    public ArrayList<ExtraDataProperty> getExtraDataPropertyList() {
        return extraDataPropertyList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    // DataObjects
    public StateData getStateData() {
        return stateData.get();
    }

    public ObjectProperty<StateData> stateDataProperty() {
        return stateData;
    }

    public void setStateData(StateData stateData) {
        this.stateData.set(stateData);
    }

    public FeeRateData getFeeRateData() {
        return feeRateData.get();
    }

    public ObjectProperty<FeeRateData> feeRateDataProperty() {
        return feeRateData;
    }

    public void setFeeRateData(FeeRateData feeRateData) {
        this.feeRateData.set(feeRateData);
    }

    public PaymentTypeData getPaymentTypeData() {
        return paymentTypeData.get();
    }

    public ObjectProperty<PaymentTypeData> paymentTypeDataProperty() {
        return paymentTypeData;
    }

    public void setPaymentTypeData(PaymentTypeData paymentTypeData) {
        this.paymentTypeData.set(paymentTypeData);
    }

    // Data
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

    public String getNachname() {
        return nachname.get();
    }

    public StringProperty nachnameProperty() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname.set(nachname);
    }

    public String getVorname() {
        return vorname.get();
    }

    public StringProperty vornameProperty() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname.set(vorname);
    }

    public String getAnrede() {
        return anrede.get();
    }

    public StringProperty anredeProperty() {
        return anrede;
    }

    public void setAnrede(String anrede) {
        this.anrede.set(anrede);
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

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
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

    public String getStrasse() {
        return strasse.get();
    }

    public StringProperty strasseProperty() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse.set(strasse);
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

    public String getOrt() {
        return ort.get();
    }

    public StringProperty ortProperty() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort.set(ort);
    }

    public String getLand() {
        return land.get();
    }

    public StringProperty landProperty() {
        return land;
    }

    public void setLand(String land) {
        this.land.set(land);
    }

    public long getStatus() {
        return status.get();
    }

    public LongProperty statusProperty() {
        return status;
    }

    public void setStatus(long status) {
        this.status.set(status);
    }

    public long getBeitrag() {
        return beitrag.get();
    }

    public LongProperty beitragProperty() {
        return beitrag;
    }

    public void setBeitrag(long beitrag) {
        this.beitrag.set(beitrag);
    }

    public long getBeitragssatz() {
        return beitragssatz.get();
    }

    public LongProperty beitragssatzProperty() {
        return beitragssatz;
    }

    public void setBeitragssatz(long beitragssatz) {
        this.beitragssatz.set(beitragssatz);
    }

    public long getPaymentType() {
        return paymentType.get();
    }

    public LongProperty paymentTypeProperty() {
        return paymentType;
    }

    public void setPaymentType(long paymentType) {
        this.paymentType.set(paymentType);
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

    public String getIban() {
        return iban.get();
    }

    public StringProperty ibanProperty() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban.set(iban);
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

    public String getKontoinhaber() {
        return kontoinhaber.get();
    }

    public StringProperty kontoinhaberProperty() {
        return kontoinhaber;
    }

    public void setKontoinhaber(String kontoinhaber) {
        this.kontoinhaber.set(kontoinhaber);
    }

    public LocalDate getZahlungsbeginn() {
        return zahlungsbeginn;
    }

    public void setZahlungsbeginn(LocalDate zahlungsbeginn) {
        this.zahlungsbeginn = zahlungsbeginn;
    }

    public LocalDate getSepaBeginn() {
        return sepaBeginn;
    }

    public void setSepaBeginn(LocalDate sepaBeginn) {
        this.sepaBeginn = sepaBeginn;
    }

    public LocalDate getBeitritt() {
        return beitritt;
    }

    public void setBeitritt(LocalDate beitritt) {
        this.beitritt = beitritt;
    }

    public void setErstellDatum(LocalDate erstellDatum) {
        this.erstellDatum = erstellDatum;
    }

    public LocalDate getErstellDatum() {
        return erstellDatum;
    }
}
