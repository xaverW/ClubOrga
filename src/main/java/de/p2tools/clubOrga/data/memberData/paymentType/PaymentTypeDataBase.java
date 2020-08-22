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


package de.p2tools.clubOrga.data.memberData.paymentType;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.Arrays;

public class PaymentTypeDataBase extends PDataSample<PaymentTypeData> {
    public static final String TAG = "PaymentTypeData";

    private final LongProperty id = new SimpleLongProperty(0);
    private final LongProperty no = new SimpleLongProperty(0);
    private final StringProperty name = new SimpleStringProperty("");
    private final LongProperty account = new SimpleLongProperty(0);
    private final BooleanProperty directDebit = new SimpleBooleanProperty(false);
    private final StringProperty text = new SimpleStringProperty("");
    private final ObjectProperty<FinanceAccountData> financeAccountData = new SimpleObjectProperty<>();

    ClubConfig clubConfig;

    public PaymentTypeDataBase() {
        financeAccountData.addListener((obs, old, newV) -> {
            if (newV != null) {
                setAccount(newV.getId());
            }
        });
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public ConfigExtra[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.addAll(Arrays.asList(getConfigs()));

        return list.toArray(new ConfigExtra[]{});
    }

    private Config[] getConfigs() {
        return new Config[]{
                new ConfigLongPropExtra("id", PaymentTypeNames.ID, id),
                new ConfigLongPropExtra("no", PaymentTypeNames.NO, no),
                new ConfigStringPropExtra("name", PaymentTypeNames.NAME, name),
                new ConfigLongPropExtra("konto", PaymentTypeNames.ACCOUNT, account),
                new ConfigBoolPropExtra("directDebit", PaymentTypeNames.DIRECT_DEBIT, directDebit),
                new ConfigStringPropExtra("text", PaymentTypeNames.DESCRIPTION, text),
        };
    }

    public FinanceAccountData getFinanceAccountData() {
        return financeAccountData.get();
    }

    public ObjectProperty<FinanceAccountData> financeAccountDataProperty() {
        return financeAccountData;
    }

    public void setFinanceAccountData(FinanceAccountData financeAccountData) {
        this.financeAccountData.set(financeAccountData);
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

    public long getAccount() {
        return account.get();
    }

    public LongProperty accountProperty() {
        return account;
    }

    public void setAccount(long account) {
        this.account.set(account);
    }

    public boolean getDirectDebit() {
        return directDebit.get();
    }

    public BooleanProperty directDebitProperty() {
        return directDebit;
    }

    public void setDirectDebit(boolean directDebit) {
        this.directDebit.set(directDebit);
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

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
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
