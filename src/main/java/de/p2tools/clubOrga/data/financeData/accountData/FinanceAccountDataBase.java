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


package de.p2tools.clubOrga.data.financeData.accountData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.pData.PDataId;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.Arrays;

public class FinanceAccountDataBase extends PDataSample<FinanceAccountData> implements PDataId {
    public static final String TAG = "FinanceAccountData";

    private final LongProperty id = new SimpleLongProperty(0);
    private final LongProperty no = new SimpleLongProperty(0);
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");

    private final BooleanProperty giro = new SimpleBooleanProperty(false);
    private final StringProperty bic = new SimpleStringProperty("");
    private final StringProperty iban = new SimpleStringProperty("");
    private final StringProperty bank = new SimpleStringProperty("");

    ClubConfig clubConfig;

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
                new ConfigLongPropExtra("id", FinanceAccountFieldNames.ID, id),
                new ConfigLongPropExtra("nr", FinanceAccountFieldNames.NO, no),
                new ConfigStringPropExtra("name", FinanceAccountFieldNames.NAME, name),
                new ConfigStringPropExtra("beschreibung", FinanceAccountFieldNames.DESCRIPTION, description),
                new ConfigBoolPropExtra("giro", FinanceAccountFieldNames.GIRO, giro),
                new ConfigStringPropExtra("bic", FinanceAccountFieldNames.BIC, bic),
                new ConfigStringPropExtra("iban", FinanceAccountFieldNames.IBAN, iban),
                new ConfigStringPropExtra("bank", FinanceAccountFieldNames.BANK, bank),
        };
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

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public boolean isGiro() {
        return giro.get();
    }

    public BooleanProperty giroProperty() {
        return giro;
    }

    public void setGiro(boolean giro) {
        this.giro.set(giro);
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
}
