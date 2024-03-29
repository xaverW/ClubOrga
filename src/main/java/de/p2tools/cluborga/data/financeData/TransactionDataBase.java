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


package de.p2tools.cluborga.data.financeData;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgConst;
import de.p2tools.cluborga.data.feeData.FeeData;
import de.p2tools.cluborga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.config.Config_longProp;
import de.p2tools.p2lib.configfile.config.Config_moneyProp;
import de.p2tools.p2lib.configfile.config.Config_stringProp;
import de.p2tools.p2lib.configfile.pdata.PDataSample;
import javafx.beans.property.*;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;

public class TransactionDataBase extends PDataSample<TransactionDataBase> {
    public static final String TAG = "TransactionData";

    private final LongProperty id = new SimpleLongProperty(0);
    private final LongProperty no = new SimpleLongProperty(0);

    private final LongProperty feeId = new SimpleLongProperty(0);
    private final LongProperty feeNo = new SimpleLongProperty(0);

    private final LongProperty betrag = new SimpleLongProperty(0);
    private final LongProperty category = new SimpleLongProperty(ProgConst.STANDARD_FIELD);

    private final StringProperty text = new SimpleStringProperty("");
    private BooleanProperty changed = new SimpleBooleanProperty(true);

    private final ObjectProperty<FinanceCategoryData> financeCategoryData = new SimpleObjectProperty<>();
    private final ObjectProperty<FeeData> feeData = new SimpleObjectProperty<>(null);

    static final DecimalFormat DF;
    static final NumberStringConverter NSC;

    static {
        DF = new DecimalFormat("###,##0.00 €");
        NSC = new NumberStringConverter(DF);
    }

    public TransactionDataBase() {
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        if (getFinanceCategoryData() != null) {
            category.set(getFinanceCategoryData().getId());
        } else {
            category.set(ProgConst.STANDARD_FIELD);
        }

        initFeeData();

        return new Config[]{
                new Config_longProp("id", FinanceFieldNames.ID, id),
                new Config_longProp("nr", FinanceFieldNames.NR, no),
                new Config_longProp("feeId", FinanceFieldNames.FEED_ID, feeId),

                new Config_moneyProp("betrag", FinanceFieldNames.BETRAG, betrag),
                new Config_longProp("kategorie", FinanceFieldNames.KATEGORIE, category),

                new Config_stringProp("text", FinanceFieldNames.TEXT, text)
        };
    }

    void initData(ClubConfig clubConfig) {
        setFinanceCategoryData(clubConfig.financeCategoryDataList.getFinanceCategoryDataOrStandard(category.get()));
        setFeeData(clubConfig.feeDataList.getFeeById(feeId.get()));
    }

    // =============================================================
    // ==== FinanceCategoryData ====
    public FinanceCategoryData getFinanceCategoryData() {
        return financeCategoryData.get();
    }

    public ObjectProperty<FinanceCategoryData> financeCategoryDataProperty() {
        return financeCategoryData;
    }

    public void setFinanceCategoryData(FinanceCategoryData financeCategoryData) {
        this.financeCategoryData.set(financeCategoryData);
    }

    // =============================================================
    // ==== FeeData ====
    private void initFeeData() {
        if (getFeeData() != null) {
            feeId.set(getFeeData().getId());
            feeNo.set(getFeeData().getNo());
        } else {
            feeId.set(0);
            feeNo.set(0);
        }
    }

    public long getFeeNo() {
        initFeeData();
        return feeNo.get();
    }

    public FeeData getFeeData() {
        return feeData.get();
    }

    public ObjectProperty<FeeData> feeDataProperty() {
        return feeData;
    }

    public void setFeeData(FeeData feeData) {
        this.feeData.set(feeData);
    }
    // =============================================================

    // =============================================================
    // changed
    void noticeChange() {
        changed.setValue(!changed.get());
    }

    public boolean getChanged() {
        return changed.get();
    }

    public BooleanProperty changedProperty() {
        return changed;
    }
    // =============================================================


    // =============================================================
    // fields
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

    public long getBetrag() {
        return betrag.get();
    }

    public LongProperty betragProperty() {
        return betrag;
    }

    public void setBetrag(long betrag) {
        this.betrag.set(betrag);
    }

    public void addBetrag(long betrag) {
        this.betrag.set(this.betrag.getValue() + betrag);
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
