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

package de.p2tools.clubOrga.data.financeData.accountData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.memberData.paymentType.PaymentTypeData;
import de.p2tools.p2Lib.alert.PAlert;

import java.util.Collection;

public class FinanceAccountDataList extends FinanceAccountDataListBase {

    public FinanceAccountDataList(ClubConfig clubConfig) {
        super(clubConfig);
    }

    public void initListAfterClubLoad() {
        final int idBar = FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_BAR.getId();
        if (isEmpty() || get(idBar).getId() != idBar) {
            final FinanceAccountData financeBar = new FinanceAccountData();
            financeBar.setName(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_BAR.getName());
            financeBar.setDescription(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_BAR.getDescription());
            financeBar.setId(idBar);
            financeBar.setNo(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_BAR.getShownNo());

            add(idBar, financeBar);
        }

        final int idGiro = FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_GIRO.getId();
        if (size() <= idGiro || get(idGiro).getId() != idGiro) {
            final FinanceAccountData financeGiro = new FinanceAccountData();
            financeGiro.setName(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_GIRO.getName());
            financeGiro.setDescription(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_GIRO.getDescription());
            financeGiro.setGiro(true);
            financeGiro.setId(idGiro);
            financeGiro.setNo(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_GIRO.getShownNo());

            add(idGiro, financeGiro);
        }
    }

    public FinanceAccountData getAccountDataStandard(FinanceAccountFactory.ACCOUNT_TYPE accountType) {
        switch (accountType) {
            case ACCOUNT_BAR:
                return get(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_BAR.getId());

            case ACCOUNT_GIRO:
            default:
                return get(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_GIRO.getId());
        }
    }


    @Override
    public boolean remove(Object obj) {
        if (!checkRemove((FinanceAccountData) obj)) {
            return false;
        }

        return super.remove(obj);
    }

    @Override
    public synchronized boolean add(FinanceAccountData feeData) {
        boolean ret = super.add(feeData);
//        sort();
        setListChanged();
        return ret;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends FinanceAccountData> feeData) {
        boolean ret = super.addAll(feeData);
//        sort();
        setListChanged();
        return ret;
    }

    public FinanceAccountData getById(long id) {
        return this.stream().filter(data -> data.getId() == id).findFirst().orElse(null);
    }

    public boolean checkRemove(FinanceAccountData financeAccountData) {
        if (financeAccountData.getId() < FinanceAccountFactory.ACCOUNT_TYPE_SIZE) {
            PAlert.showErrorAlert(clubConfig.getStage(), "Konto löschen",
                    "Das ist ein Standardkonto das nicht gelöscht werden kann.");
            return false;
        }

        for (PaymentTypeData paymentTypeData : clubConfig.paymentTypeDataList) {
            if (paymentTypeData.getAccount() == financeAccountData.getId()) {
                PAlert.showErrorAlert(clubConfig.getStage(), "Konto löschen",
                        "Das Konto wird bei den Zahlarten verwendet und kann nicht " +
                                "gelöscht werden.");
                return false;
            }
        }

        return true;
    }
}
