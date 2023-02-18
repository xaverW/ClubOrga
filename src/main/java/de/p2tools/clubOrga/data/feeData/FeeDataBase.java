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


package de.p2tools.clubOrga.data.feeData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.controller.export.ExportFactory;
import de.p2tools.clubOrga.data.extraData.ExtraData;
import de.p2tools.clubOrga.data.extraData.ExtraDataProperty;
import de.p2tools.clubOrga.data.feeData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2Lib.tools.date.PLDateProperty;
import javafx.beans.property.*;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class FeeDataBase extends PDataSample<FeeData> {
    private static final DecimalFormat DF;

    static {
        DF = new DecimalFormat("###,##0.00");
    }

    public static final String TAG = "FeeData";

    private final LongProperty id = new SimpleLongProperty(0);
    private final LongProperty memberId = new SimpleLongProperty(0);
    private final LongProperty no = new SimpleLongProperty(0);
    private final LongProperty memberNo = new SimpleLongProperty(0);
    private final StringProperty memberName = new SimpleStringProperty("");
    private final LongProperty betrag = new SimpleLongProperty(0);

    private final IntegerProperty jahr = new SimpleIntegerProperty(PDateFactory.getActYearInt());
    private final LongProperty zahlart = new SimpleLongProperty(0);

    private final PLDateProperty bezahlt = new PLDateProperty();
    private final PLDateProperty rechnung = new PLDateProperty();
    private final PLDateProperty spendenQ = new PLDateProperty();

    private LocalDate erstellDatum = LocalDate.now();

    private final StringProperty text = new SimpleStringProperty("");

    private ArrayList<ExtraDataProperty> extraDataPropertyList = new ArrayList<>();
    private final ObjectProperty<PaymentTypeData> paymentTypeData = new SimpleObjectProperty<>();

    private boolean selected = false;
    private MemberData memberData = null;
    ClubConfig clubConfig;

    public FeeDataBase() {
        paymentTypeData.addListener((obs, old, newV) -> {
            if (newV != null) {
                setZahlart(newV.getId());
            }
        });
    }

    @Override
    public String getTag() {
        return TAG;
    }

    void addExtra() {
        extraDataPropertyList.clear();
        for (ExtraData extraData : clubConfig.extraDataListFee) {
            extraDataPropertyList.add(extraData.getProperty());
        }
    }

    public Config[] getConfigsForNewsletter() {
        Config[] arr = getConfigsArr();
        ArrayList<Config> list = new ArrayList<>();

        for (Config config : arr) {
            if (config.getName().equals(FeeFieldNames.BETRAG)) {
                StringProperty st = new SimpleStringProperty();
                double d = 0.01 * getBetrag();
                st.setValue(new NumberStringConverter(DF).toString(d));
                Config_stringProp conf = new Config_stringProp("betrag",
                        FeeFieldNames.BETRAG, st);
                list.add(conf);

                // Betrag in Worten
                String inWords = ExportFactory.inWorten(st.get());
                StringProperty stWord = new SimpleStringProperty();
                stWord.setValue(inWords);
                Config_stringProp confInWords = new Config_stringProp("betragInWords",
                        FeeFieldNames.BETRAG_IN_WORDS, stWord);
                list.add(confInWords);

            } else if (config.getName().equals(FeeFieldNames.ZAHLART)) {
                StringProperty st = new SimpleStringProperty();
                st.setValue(paymentTypeData.get().getName());
                Config_stringProp conf = new Config_stringProp("zahlart",
                        FeeFieldNames.ZAHLART, st);
                list.add(conf);

            } else {
                list.add(config);
            }
        }

        return list.toArray(new Config[]{});
    }


    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new Config_longProp("id", FeeFieldNames.ID, id));
        list.add(new Config_longProp("memberId", FeeFieldNames.MEMBER_ID, memberId));
        list.add(new Config_longProp("nr", FeeFieldNames.NR, no));
        list.add(new Config_longProp("mitgliedNr", FeeFieldNames.MEMBER_NO, memberNo));
        list.add(new Config_stringProp("mitgliedName", FeeFieldNames.MEMBER_NAME, memberName));
        list.add(new Config_moneyProp("betrag", FeeFieldNames.BETRAG, betrag));
        list.add(new Config_intProp("jahr", FeeFieldNames.JAHR, jahr));

        list.add(new Config_longProp("zahlart", FeeFieldNames.ZAHLART, zahlart));
        list.add(new Config_lDateProp("bezahlt", FeeFieldNames.BEZAHLT, bezahlt));
        list.add(new Config_lDateProp("rechnung", FeeFieldNames.RECHNUNG, rechnung));
        list.add(new Config_lDateProp("spendenQ", FeeFieldNames.SPENDEN_Q, spendenQ));
        list.add(new Config_lDate("erstellDatum", FeeFieldNames.ERSTELLDATUM, erstellDatum) {
            @Override
            public void setUsedValue(LocalDate act) {
                erstellDatum = act;
            }
        });

        list.add(new Config_stringProp("text", FeeFieldNames.TEXT, text));

        for (int i = 0; i < clubConfig.extraDataListFee.size(); ++i) {
            ExtraData extraData = clubConfig.extraDataListFee.get(i);
            ExtraDataProperty extraDataProperty = extraDataPropertyList.get(i);
            list.add(extraData.getConfig(extraDataProperty));
        }

        return list.toArray(new Config[]{});
    }

    public ArrayList<ExtraDataProperty> getExtraDataPropertyList() {
        return extraDataPropertyList;
    }

    public MemberData getMemberData() {
        return memberData;
    }

    public void setMemberData(MemberData memberData) {
        this.memberData = memberData;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public long getMemberId() {
        return memberId.get();
    }

    public LongProperty memberIdProperty() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId.set(memberId);
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

    public long getMemberNo() {
        return memberNo.get();
    }

    public LongProperty memberNoProperty() {
        return memberNo;
    }

    public void setMemberNo(long memberNo) {
        this.memberNo.set(memberNo);
    }

    public void setMitgliedNr(String mitgliederNr) {
        int no = 0;
        try {
            no = Integer.parseInt(mitgliederNr);
        } catch (Exception ex) {
            no = 0;
        }
        this.memberNo.set(no);
    }

    public String getMemberName() {
        return memberName.get();
    }

    public StringProperty memberNameProperty() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName.set(memberName);
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

    public int getJahr() {
        return jahr.get();
    }

    public IntegerProperty jahrProperty() {
        return jahr;
    }

    public void setJahr(int jahr) {
        this.jahr.set(jahr);
    }

    public long getZahlart() {
        return zahlart.get();
    }

    public LongProperty zahlartProperty() {
        return zahlart;
    }

    public void setZahlart(long zahlart) {
        this.zahlart.set(zahlart);
    }

    public LocalDate getBezahlt() {
        return bezahlt.get();
    }

    public PLDateProperty bezahltProperty() {
        return bezahlt;
    }

    public void setBezahlt(LocalDate bezahlt) {
        this.bezahlt.set(bezahlt);
    }

    public LocalDate getRechnung() {
        return rechnung.get();
    }

    public PLDateProperty rechnungProperty() {
        return rechnung;
    }

    public void setRechnung(LocalDate rechnung) {
        this.rechnung.set(rechnung);
    }

    public LocalDate getSpendenQ() {
        return spendenQ.get();
    }

    public PLDateProperty spendenQProperty() {
        return spendenQ;
    }

    public void setSpendenQ(LocalDate spendenQ) {
        this.spendenQ.set(spendenQ);
    }

    public LocalDate getErstellDatum() {
        return erstellDatum;
    }

    public void setErstellDatum(LocalDate erstellDatum) {
        this.erstellDatum = erstellDatum;
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
