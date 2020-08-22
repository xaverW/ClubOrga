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


package de.p2tools.clubOrga.data.financeData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.data.extraData.ExtraData;
import de.p2tools.clubOrga.data.extraData.ExtraDataProperty;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.date.PLocalDateProperty;
import javafx.beans.property.*;

import java.util.ArrayList;

public class FinanceDataBase extends PDataSample<FinanceData> {
    public static final String TAG = "FinanceData";

    private final LongProperty id = new SimpleLongProperty(0);

    private final LongProperty no = new SimpleLongProperty(0);
    private final StringProperty receiptNo = new SimpleStringProperty("");

    private final LongProperty gesamtbetrag = new SimpleLongProperty(0);
    private final LongProperty konto = new SimpleLongProperty(ProgConst.STANDARD_FIELD);
    private final StringProperty category = new SimpleStringProperty("");
    private final IntegerProperty geschaeftsJahr = new SimpleIntegerProperty(PDateFactory.getAktYearInt());

    private final PLocalDateProperty buchungsDatum = new PLocalDateProperty();
    private final PLocalDate erstellDatum = new PLocalDate();
    private final StringProperty text = new SimpleStringProperty("");

    private ArrayList<ExtraDataProperty> extraDataPropertyList = new ArrayList<>();
    private final ObjectProperty<FinanceAccountData> financeAccountData = new SimpleObjectProperty<>();
    TransactionDataList transactionDataList;

    private boolean selected = false;
    private ClubConfig clubConfig;
//    private FeeData feeData = null; // todo->transaction

    @Override
    public String getTag() {
        return TAG;
    }

    void addExtra() {
        extraDataPropertyList.clear();
        for (ExtraData extraData : clubConfig.extraDataListFinance) {
            extraDataPropertyList.add(extraData.getProperty());
        }
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();

        if (getFinanceAccountData() != null) {
            konto.set(getFinanceAccountData().getId());
        } else {
            konto.set(ProgConst.STANDARD_FIELD);
        }

        list.add(new ConfigLongPropExtra("id", FinanceFieldNames.ID, id));
        list.add(new ConfigLongPropExtra("nr", FinanceFieldNames.NO, no));
        list.add(new ConfigStringPropExtra("belegNr", FinanceFieldNames.RECEIPT_NR, receiptNo));

        list.add(new ConfigMoneyPropExtra("gesamtbetrag", FinanceFieldNames.GESAMTBETRAG, gesamtbetrag, true));
        list.add(new ConfigLongPropExtra("konto", FinanceFieldNames.KONTO, konto));
//        list.add(new ConfigStringPropExtra("konto", FinanceFieldNames.KONTO, konto));
        list.add(new ConfigStringPropExtra("kategorie", FinanceFieldNames.CATEGORY, category));
        list.add(new ConfigIntPropExtra("geschaeftsJahr", FinanceFieldNames.GESCHAEFTSJAHR, geschaeftsJahr));

        list.add(new ConfigLocalDatePropExtra("buchungsDatum", FinanceFieldNames.BUCHUNGS_DATUM, buchungsDatum));
        list.add(new ConfigLocalDateExtra("erstellDatum", FinanceFieldNames.ERSTELLDATUM, erstellDatum));

        list.add(new ConfigStringPropExtra("text", FinanceFieldNames.TEXT, text));
        list.add(new ConfigPDataList(transactionDataList));

        for (int i = 0; i < clubConfig.extraDataListFinance.size(); ++i) {
            ExtraData extraData = clubConfig.extraDataListFinance.get(i);
            ExtraDataProperty extraDataProperty = extraDataPropertyList.get(i);
            list.add(extraData.getConfig(extraDataProperty));
        }

        return list.toArray(new Config[]{});
    }

    public ArrayList<ExtraDataProperty> getExtraDataPropertyList() {
        return extraDataPropertyList;
    }

    public ClubConfig getClubConfig() {
        return clubConfig;
    }

    public void setClubConfig(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
    }

//    public FeeData getFeeData() {
//        return feeData;
//    }
//
//    public void setFeeData(FeeData feeData) {
//        this.feeData = feeData;
//    }


    // =============================================================
    // Classes
    public FinanceAccountData getFinanceAccountData() {
        return financeAccountData.get();
    }

    public ObjectProperty<FinanceAccountData> financeAccountDataProperty() {
        return financeAccountData;
    }

    public void setFinanceAccountData(FinanceAccountData financeAccountData) {
        this.financeAccountData.set(financeAccountData);
    }

    public void initFinanceAccountData(ClubConfig clubConfig) {
        setFinanceAccountData(clubConfig.financeAccountDataList.getFinanceAccountDataOrStandard(konto.get()));
    }


    public TransactionDataList getTransactionDataList() {
        return transactionDataList;
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

    public long getNo() {
        return no.get();
    }

    public LongProperty noProperty() {
        return no;
    }

    public void setNo(long no) {
        this.no.set(no);
    }

    public String getReceiptNo() {
        return receiptNo.get();
    }

    public StringProperty receiptNoProperty() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo.set(receiptNo);
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

//    public String getKonto() {
//        return konto.get();
//    }
//
//    public StringProperty kontoProperty() {
//        return konto;
//    }
//
//    public void setKonto(String konto) {
//        this.konto.set(konto);
//    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
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

    public PLocalDate getErstellDatum() {
        return erstellDatum;
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
