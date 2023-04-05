/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.cluborga.config.club;

import de.p2tools.cluborga.config.prog.ProgConst;
import de.p2tools.cluborga.config.prog.ProgInfos;
import de.p2tools.cluborga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.cluborga.data.feeData.paymentType.PaymentTypeData;
import de.p2tools.cluborga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.cluborga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.cluborga.data.memberData.MemberData;
import de.p2tools.cluborga.data.memberData.stateData.StateData;
import de.p2tools.p2lib.configfile.ConfigFile;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;

import java.nio.file.Path;

public class ClubConfigFactory {

    private ClubConfigFactory() {

    }

    public static void getClubConfigData(ClubConfig clubConfig, ConfigFile configFile) {
        // sind die Einstellungen der Clubfelder, Fenster, ..
        final Path xmlFilePath = ProgInfos.getClubConfigFile(clubConfig.getClubPath());
        configFile.setFilePath(xmlFilePath.toString());
//        ConfigFile configFile = new ConfigFile(xmlFilePath.toString(), true);

        configFile.addConfigs(clubConfig);
        configFile.addConfigs(ProgColorList.getConfigsData());
        configFile.addConfigs(clubConfig.extraDataListMember);
        configFile.addConfigs(clubConfig.extraDataListFee);
        configFile.addConfigs(clubConfig.extraDataListFinance);
    }

    public static void getClubData(ClubConfig clubConfig, ConfigFile configFile) {
        // sind die Clubdaten: Mitglieder, Beiträge, Finanzen
        final Path xmlFilePath = ProgInfos.getClubDataFile(clubConfig.getClubPath());
        configFile.setFilePath(xmlFilePath.toString());
//        ConfigFile configFile = new ConfigFile(xmlFilePath.toString(), false);

        configFile.addConfigs(clubConfig.clubData);
        configFile.addConfigs(clubConfig.memberDataList);
        configFile.addConfigs(clubConfig.feeRateDataList);
        configFile.addConfigs(clubConfig.stateDataList);
        configFile.addConfigs(clubConfig.paymentTypeDataList);
        configFile.addConfigs(clubConfig.feeDataList);
        configFile.addConfigs(clubConfig.financeDataList);
        configFile.addConfigs(clubConfig.financeAccountDataList);
        configFile.addConfigs(clubConfig.financeCategoryDataList);
    }

    public static void initFilter(ClubConfig clubConfig) {
        initFilterStateData(clubConfig);
        initFilterFeeRateData(clubConfig);
        initFilterPaymentTypeData(clubConfig);
        initFilterFeeMemberData(clubConfig);
        initFilterFinanceAccountData(clubConfig);
        initFilterFinanceCategoryData(clubConfig);
        initFilterFinanceReportAccountData(clubConfig);
        initFilterFinanceReportCategoryData(clubConfig);
    }

    private static void initFilterStateData(ClubConfig clubConfig) {
        final LongProperty propStatus = clubConfig.MEMBER_FILTER_STATUS_ID;
        final ObjectProperty<StateData> stateDataObjectProperty = clubConfig.MEMBER_FILTER_STATUS;
        stateDataObjectProperty.setValue(clubConfig.stateDataList.getById(propStatus.get()));
        stateDataObjectProperty.addListener((v, o, n) -> {
            if (n != null) {
                propStatus.setValue(n.getId());
            } else {
                propStatus.setValue(ProgConst.FILTER_ID_NOT_SELECTED);
            }
        });
    }

    private static void initFilterFeeRateData(ClubConfig clubConfig) {
        final LongProperty propBeitrag = clubConfig.MEMBER_FILTER_BEITRAG_ID;
        final ObjectProperty<FeeRateData> objectProperty = clubConfig.MEMBER_FILTER_BEITRAG;
        objectProperty.setValue(clubConfig.feeRateDataList.getById(propBeitrag.get()));
        objectProperty.addListener((v, o, n) -> {
            if (n != null) {
                propBeitrag.setValue(n.getId());
            } else {
                propBeitrag.setValue(ProgConst.FILTER_ID_NOT_SELECTED);
            }
        });
    }

    private static void initFilterPaymentTypeData(ClubConfig clubConfig) {
        final LongProperty prop = clubConfig.MEMBER_FILTER_PAYMENT_TYPE_ID;
        final ObjectProperty<PaymentTypeData> objectProperty = clubConfig.MEMBER_FILTER_PAYMENT_TYPE;
        objectProperty.setValue(clubConfig.paymentTypeDataList.getById(prop.get()));
        objectProperty.addListener((v, o, n) -> {
            if (n != null) {
                prop.setValue(n.getId());
            } else {
                prop.setValue(ProgConst.FILTER_ID_NOT_SELECTED);
            }
        });
    }

    private static void initFilterFeeMemberData(ClubConfig clubConfig) {
        final LongProperty prop = clubConfig.FEE_FILTER_MITGLIED_ID;
        final ObjectProperty<MemberData> objectProperty = clubConfig.FEE_FILTER_MITGLIED;
        objectProperty.setValue(clubConfig.memberDataList.getById(prop.get()));
        objectProperty.addListener((v, o, n) -> {
            if (n != null) {
                prop.setValue(n.getId());
            } else {
                prop.setValue(ProgConst.FILTER_ID_NOT_SELECTED);
            }
        });
    }

    private static void initFilterFinanceAccountData(ClubConfig clubConfig) {
        final LongProperty prop = clubConfig.FINANCE_FILTER_ACCOUNT_ID;
        final ObjectProperty<FinanceAccountData> objectProperty = clubConfig.FINANCE_FILTER_ACCOUNT;
        objectProperty.setValue(clubConfig.financeAccountDataList.getById(prop.get()));
        objectProperty.addListener((v, o, n) -> {
            if (n != null) {
                prop.setValue(n.getId());
            } else {
                prop.setValue(ProgConst.FILTER_ID_NOT_SELECTED);
            }
        });
    }

    private static void initFilterFinanceCategoryData(ClubConfig clubConfig) {
        final LongProperty prop = clubConfig.FINANCE_FILTER_CATEGORY_ID;
        final ObjectProperty<FinanceCategoryData> objectProperty = clubConfig.FINANCE_FILTER_CATEGORY;
        objectProperty.setValue(clubConfig.financeCategoryDataList.getById(prop.get()));
        objectProperty.addListener((v, o, n) -> {
            if (n != null) {
                prop.setValue(n.getId());
            } else {
                prop.setValue(ProgConst.FILTER_ID_NOT_SELECTED);
            }
        });
    }

    private static void initFilterFinanceReportAccountData(ClubConfig clubConfig) {
        final LongProperty prop = clubConfig.FINANCE_REPORT_FILTER_ACCOUNT_ID;
        final ObjectProperty<FinanceAccountData> objectProperty = clubConfig.FINANCE_REPORT_FILTER_ACCOUNT;
        objectProperty.setValue(clubConfig.financeAccountDataList.getById(prop.get()));
        objectProperty.addListener((v, o, n) -> {
            if (n != null) {
                prop.setValue(n.getId());
            } else {
                prop.setValue(ProgConst.FILTER_ID_NOT_SELECTED);
            }
        });
    }

    private static void initFilterFinanceReportCategoryData(ClubConfig clubConfig) {
        final LongProperty prop = clubConfig.FINANCE_REPORT_FILTER_CATEGORY_ID;
        final ObjectProperty<FinanceCategoryData> objectProperty = clubConfig.FINANCE_REPORT_FILTER_CATEGORY;
        objectProperty.setValue(clubConfig.financeCategoryDataList.getById(prop.get()));
        objectProperty.addListener((v, o, n) -> {
            if (n != null) {
                prop.setValue(n.getId());
            } else {
                prop.setValue(ProgConst.FILTER_ID_NOT_SELECTED);
            }
        });
    }
}
