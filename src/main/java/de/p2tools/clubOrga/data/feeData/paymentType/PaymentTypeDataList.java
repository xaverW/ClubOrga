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

package de.p2tools.clubOrga.data.feeData.paymentType;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountFactory;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.log.PLog;

import java.util.Collection;
import java.util.Optional;

public class PaymentTypeDataList extends PaymentTypeDataListBase {

    public PaymentTypeDataList(ClubConfig clubConfig) {
        super(clubConfig);
    }

    public void initListAfterClubLoad() {
        final int idBar = PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BAR.getId();
        if (isEmpty() || get(idBar).getId() != idBar) {

            final PaymentTypeData paymentTypeDataStandard = new PaymentTypeData();
            paymentTypeDataStandard.setName(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BAR.getName());
            paymentTypeDataStandard.setText(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BAR.getDescription());
            paymentTypeDataStandard.setId(idBar);
            paymentTypeDataStandard.setNo(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BAR.getShownNo());
            paymentTypeDataStandard.setAccount(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_BAR.getId());
            paymentTypeDataStandard.setDirectDebit(false);

            add(idBar, paymentTypeDataStandard);
        }

        final int idUeberweisung = PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_UEBERWEISUNG.getId();
        if (size() <= idUeberweisung || get(idUeberweisung).getId() != idUeberweisung) {

            final PaymentTypeData paymentTypeDataStandard = new PaymentTypeData();
            paymentTypeDataStandard.setName(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_UEBERWEISUNG.getName());
            paymentTypeDataStandard.setText(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_UEBERWEISUNG.getDescription());
            paymentTypeDataStandard.setId(idUeberweisung);
            paymentTypeDataStandard.setNo(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_UEBERWEISUNG.getShownNo());
            paymentTypeDataStandard.setAccount(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_GIRO.getId());
            paymentTypeDataStandard.setDirectDebit(false);

            add(idUeberweisung, paymentTypeDataStandard);
        }

        final int idBankeinzug = PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BANKEINZUG.getId();
        if (size() <= idBankeinzug || get(idBankeinzug).getId() != idBankeinzug) {

            final PaymentTypeData paymentTypeDataStandard = new PaymentTypeData();
            paymentTypeDataStandard.setName(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BANKEINZUG.getName());
            paymentTypeDataStandard.setText(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BANKEINZUG.getDescription());
            paymentTypeDataStandard.setId(idBankeinzug);
            paymentTypeDataStandard.setNo(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BANKEINZUG.getShownNo());
            paymentTypeDataStandard.setAccount(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_GIRO.getId());
            paymentTypeDataStandard.setDirectDebit(true);

            add(idBankeinzug, paymentTypeDataStandard);
        }

        this.stream().forEach(paymentTypeData -> {

            // Konto
            Optional<FinanceAccountData> accountData = clubConfig.financeAccountDataList.stream()
                    .filter(data -> paymentTypeData.getAccount() == data.getId()).findAny();

            if (accountData.isPresent()) {
                paymentTypeData.setFinanceAccountData(accountData.get());
            } else {
                PLog.errorLog(102025489, "no FinanceAccountData for the PaymentTypeDate: " + paymentTypeData.getName());
                paymentTypeData.setFinanceAccountData(clubConfig.financeAccountDataList.
                        getAccountDataStandard(FinanceAccountFactory.ACCOUNT_TYPE.ACCOUNT_GIRO));
            }

        });
    }

    public PaymentTypeData getStateDataStandard(PaymentTypeFactory.PAYMENT_TYPE type) {
        switch (type) {
            case PAYMENT_BAR:
                return get(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BAR.getId());

            case PAYMENT_BANKEINZUG:
                return get(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BANKEINZUG.getId());

            case PAYMENT_UEBERWEISUNG:
            default:
                return get(PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_UEBERWEISUNG.getId());
        }
    }

    @Override
    public boolean remove(Object obj) {
        if (((PaymentTypeData) obj).getId() <= PaymentTypeFactory.PAYMENT_TYPE.PAYMENT_BANKEINZUG.getId()) {
            PAlert.showErrorAlert(clubConfig.getStage(), "Zahlart löschen",
                    "Das sind Standardeinstellungen die nicht gelöscht werden können.");
            return false;
        }

        for (MemberData md : clubConfig.memberDataList) {
            if (md.getPaymentType() == ((PaymentTypeData) obj).getId()) {
                PAlert.showErrorAlert(clubConfig.getStage(), "Zahlart löschen",
                        "Die Zahlart wird noch bei einem Mitglied verwendet und kann nicht gelöscht werden. " +
                                "Bitte zuerst die Zahlart ändern.");
                return false;
            }
        }
        return super.remove(obj);
    }

    @Override
    public synchronized boolean add(PaymentTypeData data) {
        boolean ret = super.add(data);
//        sort();
        setListChanged();
        return ret;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends PaymentTypeData> data) {
        boolean ret = super.addAll(data);
//        sort();
        setListChanged();
        return ret;
    }

    public PaymentTypeData getById(long id) {
        return this.stream().filter(data -> data.getId() == id).findFirst().orElse(null);
    }

}
