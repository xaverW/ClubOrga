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
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigLongPropExtra;
import de.p2tools.p2Lib.configFile.config.ConfigMoneyPropExtra;
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.*;

public class TransactionDataBase extends PDataSample<TransactionDataBase> {
    public static final String TAG = "TransactionData";

    private final LongProperty id = new SimpleLongProperty(0);
    private final LongProperty nr = new SimpleLongProperty(0);

    private final LongProperty feeId = new SimpleLongProperty(0);

    private final LongProperty betrag = new SimpleLongProperty(0);
    private final LongProperty kategorie = new SimpleLongProperty(ProgConst.STANDARD_FIELD);

    private final StringProperty text = new SimpleStringProperty("");
    private BooleanProperty changed = new SimpleBooleanProperty(true);

    private final ObjectProperty<FinanceCategoryData> financeCategoryData = new SimpleObjectProperty<>();
    private final ObjectProperty<FeeData> feeData = new SimpleObjectProperty<>(null);

    public TransactionDataBase() {
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        if (getFinanceCategoryData() != null) {
            kategorie.set(getFinanceCategoryData().getId());
        } else {
            kategorie.set(ProgConst.STANDARD_FIELD);
        }

        if (getFeeData() != null) {
            feeId.set(getFeeData().getId());
        }

        return new Config[]{
                new ConfigLongPropExtra("id", FinanceFieldNames.ID, id),
                new ConfigLongPropExtra("nr", FinanceFieldNames.NR, nr),
                new ConfigLongPropExtra("feeId", FinanceFieldNames.FEED_ID, feeId),

                new ConfigMoneyPropExtra("betrag", FinanceFieldNames.BETRAG, betrag),
                new ConfigLongPropExtra("kategorie", FinanceFieldNames.KATEGORIE, kategorie),

                new ConfigStringPropExtra("text", FinanceFieldNames.TEXT, text)
        };
    }

    public FinanceCategoryData getFinanceCategoryData() {
        return financeCategoryData.get();
    }

    public ObjectProperty<FinanceCategoryData> financeCategoryDataProperty() {
        return financeCategoryData;
    }

    public void setFinanceCategoryData(FinanceCategoryData financeCategoryData) {
        this.financeCategoryData.set(financeCategoryData);
    }

    public void initFinanceCategoryData(ClubConfig clubConfig) {
        setFinanceCategoryData(clubConfig.financeCategoryDataList.getFinanceCategoryDataOrStandard(kategorie.get()));
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

    public void initFeeData(ClubConfig clubConfig) {
        setFeeData(clubConfig.feeDataList.getFeeById(feeId.get()));
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

    public long getNr() {
        return nr.get();
    }

    public LongProperty nrProperty() {
        return nr;
    }

    public void setNr(long nr) {
        this.nr.set(nr);
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
