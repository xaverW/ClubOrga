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


package de.p2tools.clubOrga.config.club;

import de.p2tools.clubOrga.ClubGuiController;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.controller.worker.Worker;
import de.p2tools.clubOrga.data.clubData.ClubData;
import de.p2tools.clubOrga.data.demoData.DemoConst;
import de.p2tools.clubOrga.data.demoData.data.DemoClubDataFactory;
import de.p2tools.clubOrga.data.extraData.ExtraDataListFee;
import de.p2tools.clubOrga.data.extraData.ExtraDataListFinance;
import de.p2tools.clubOrga.data.extraData.ExtraDataListMember;
import de.p2tools.clubOrga.data.feeData.FeeDataList;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateData;
import de.p2tools.clubOrga.data.feeData.feeRateData.FeeRateDataList;
import de.p2tools.clubOrga.data.feeData.paymentType.PaymentTypeData;
import de.p2tools.clubOrga.data.feeData.paymentType.PaymentTypeDataList;
import de.p2tools.clubOrga.data.financeData.FinanceDataList;
import de.p2tools.clubOrga.data.financeData.FinanceReportDataList;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountData;
import de.p2tools.clubOrga.data.financeData.accountData.FinanceAccountDataList;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryData;
import de.p2tools.clubOrga.data.financeData.categoryData.FinanceCategoryDataList;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberDataList;
import de.p2tools.clubOrga.data.memberData.stateData.StateData;
import de.p2tools.clubOrga.data.memberData.stateData.StateDataList;
import de.p2tools.clubOrga.gui.guiClub.GuiClub;
import de.p2tools.clubOrga.gui.guiClub.GuiClubInfo;
import de.p2tools.clubOrga.gui.guiFee.GuiFee;
import de.p2tools.clubOrga.gui.guiFee.guiConfig.GuiFeePaymentType;
import de.p2tools.clubOrga.gui.guiFee.guiConfig.GuiFeeRate;
import de.p2tools.clubOrga.gui.guiFinance.GuiFinance;
import de.p2tools.clubOrga.gui.guiFinance.guiConfig.GuiFinanceAccount;
import de.p2tools.clubOrga.gui.guiFinance.guiConfig.GuiFinanceCategory;
import de.p2tools.clubOrga.gui.guiFinanceReport.GuiFinanceReport;
import de.p2tools.clubOrga.gui.guiMember.GuiMember;
import de.p2tools.clubOrga.gui.guiMember.guiConfig.GuiMemberState;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.data.PDataProgConfig;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ClubConfig extends PDataProgConfig {
    private final ArrayList<Config> arrayList = new ArrayList<>();

    // Fenstereinstellungen
    public StringProperty SYSTEM_LOG_DIR = addStrProp("system-log-dir", "");


    public StringProperty GUI_CLUB_PANEL = addStrPropC("Fenstereinstellungen", "gui-club-panel", "1000:800");

    public DoubleProperty GUI_PANEL_MEMBER_DIVIDER_CONT = addDoubleProp("gui-panel-member-divider-cont", 0.7);
    public DoubleProperty GUI_PANEL_MEMBER_DIVIDER_INFO = addDoubleProp("gui-panel-member-divider-info", 0.5);

    public DoubleProperty GUI_PANEL_FEE_DIVIDER_CONT = addDoubleProp("gui-panel-fee-divider-cont", 0.7);
    public DoubleProperty GUI_PANEL_FEE_DIVIDER_INFO = addDoubleProp("gui-panel-fee-divider-info", 0.5);

    public DoubleProperty GUI_PANEL_FINANDES_DIVIDER_CONT = addDoubleProp("gui-panel-finances-divider-cont", 0.6);
    public DoubleProperty GUI_PANEL_FINANCES_DIVIDER_INFO_0 = addDoubleProp("gui-panel-finances-divider-info-0", 0.5);
    public DoubleProperty GUI_PANEL_FINANCES_DIVIDER_INFO_1 = addDoubleProp("gui-panel-finances-divider-info-1", 0.7);
    public DoubleProperty GUI_PANEL_FINANCES_DIVIDER_CALCULATION_0 = addDoubleProp("gui-panel-finances-divider-calculation-0", 0.4);
    public DoubleProperty GUI_PANEL_FINANCES_DIVIDER_CALCULATION_1 = addDoubleProp("gui-panel-finances-divider-calculation-1", 0.7);

    // Dialoge
    public StringProperty CLUB_CONFIG_DIALOG_SIZE = addStrProp("club-config-dialog-size", "800:600");
    public StringProperty CLUB_EXTRA_DATA_DIALOG_SIZE = addStrProp("club-extra-data-dialog-size", "500:700");
    public StringProperty DATA_DIALOG_SIZE = addStrProp("data-dialog-size", "500:700");
    public StringProperty FEE_RATE_DATA_DIALOG_SIZE = addStrProp("fee-rate-data-dialog-size", "500:700");
    public StringProperty STATE_DATA_DIALOG_SIZE = addStrProp("state-data-dialog-size", "500:700");
    public StringProperty FINANCE_ACCOUNT_DATA_DIALOG_SIZE = addStrProp("finance-account-data-dialog-size", "500:700");

    public StringProperty FINANCE_CATEGORY_DATA_DIALOG_SIZE = addStrProp("finance-category-data-dialog-size", "500:700");
    public StringProperty PAYMENT_TYPE_DATA_DIALOG_SIZE = addStrProp("payment-type-data-dialog-size", "500:700");
    public StringProperty ADD_DEMO_DATA_DIALOG_SIZE = addStrProp("add-demo-data-dialog-size", "500:700");
    public StringProperty EXPORT_CSV_DIALOG_SIZE = addStrProp("export-member-csv-dialog-size", "700:400");
    public StringProperty EXPORT_FINANCE_REPORT = addStrProp("export-finance-report", "700:400");
    public StringProperty IMPORT_MEMBER_CSV_DIALOG_SIZE = addStrProp("import-member-csv-dialog-size", "700:400");
    public StringProperty EXPORT_CLUB_ZIP_DIALOG_SIZE = addStrProp("export-club-zip-dialog-size", "700:400");
    public StringProperty EXPORT_CLUB_INFOS_DIALOG_SIZE = addStrProp("export-club-infos-dialog-size", "700:400");
    public StringProperty DELETE_ALL_DATA_DIALOG_SIZE = addStrProp("delete-all-data-dialog-size", "600:300");

    public DoubleProperty MISSING_FEE_DIALOG_DIVIDER = addDoubleProp("missing-fee-dialog-divider", 0.3);
    public StringProperty MISSING_FEE_DIALOG_SIZE = addStrPropC("Dialoge", "fee-dialog-size", "800:600");
    public DoubleProperty BILL_FOR_FEE_DIALOG_DIVIDER = addDoubleProp("bill-for-fee-dialog-divider", 0.3);
    public StringProperty BILL_FOR_FEE_DIALOG_SIZE = addStrProp("bill-for-fee-dialog-size", "700:700");
    public DoubleProperty PAY_FEE_DIALOG_DIVIDER = addDoubleProp("pay-fee-dialog-divider", 0.3);
    public StringProperty PAY_FEE_DIALOG_SIZE = addStrProp("pay-fee-dialog-size", "700:700");

    public BooleanProperty FEE_DIALOG_ADD_FINANCES = addBoolProp("fee-dialog-add-finance", Boolean.TRUE);
    public BooleanProperty FEE_DIALOG_ADD_TRANSACTIONS = addBoolProp("fee-dialog-add-transactions", Boolean.TRUE);
    public BooleanProperty FEE_DIALOG_ADD_DTAUS = addBoolProp("fee-dialog-add-dtaus", Boolean.FALSE);
    public StringProperty NEWSLETTER_TO_ODF_DIALOG_SIZE = addStrProp("newsletter-to-odf-dialog-size", "500:700");
    public BooleanProperty FEE_DIALOG_ADD_BILL = addBoolProp("fee-dialog-add-bill", Boolean.TRUE);
    public BooleanProperty FEE_DIALOG_ADD_SQ = addBoolProp("fee-dialog-add-sq", Boolean.TRUE);

    // GuiMember Filter
    public StringProperty MEMBER_FILTER_NACHNAME = addStrPropC("GuiMember Filter", "member-filter-nachname");
    public BooleanProperty MEMBER_FILTER_FEE_CREATED = addBoolProp("member-filter-fee", false);
    public BooleanProperty MEMBER_FILTER_FEE_CREATED_OFF = addBoolProp("member-filter-fee-off", true);
    public BooleanProperty MEMBER_FILTER_FEE_PAYED = addBoolProp("member-filter-fee-payed", false);
    public BooleanProperty MEMBER_FILTER_FEE_PAYED_OFF = addBoolProp("member-filter-fee-payed-off", true);
    public LongProperty MEMBER_FILTER_STATUS_ID = addLongProp("member-filter-status-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<StateData> MEMBER_FILTER_STATUS = new SimpleObjectProperty<>();
    public LongProperty MEMBER_FILTER_BEITRAG_ID = addLongProp("member-filter-beitrag-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<FeeRateData> MEMBER_FILTER_BEITRAG = new SimpleObjectProperty<>();
    public LongProperty MEMBER_FILTER_PAYMENT_TYPE_ID = addLongProp("member-filter-payment-type-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<PaymentTypeData> MEMBER_FILTER_PAYMENT_TYPE = new SimpleObjectProperty<>();


    // GuiFee Filter
    public LongProperty FEE_FILTER_MITGLIED_ID = addLongPropC("GuiFee Filter", "fee-filter-mitglied-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<MemberData> FEE_FILTER_MITGLIED = new SimpleObjectProperty<>();
    public LongProperty FEE_FILTER_PAYMENT_TYPE_ID = addLongProp("fee-filter-payment-type-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<PaymentTypeData> FEE_FILTER_PAYMENT_TYPE = new SimpleObjectProperty<>();

    public ObjectProperty<Integer> FEE_FILTER_JAHR = addObjIntProp("fee-filter-filter-jahr");
    public BooleanProperty FEE_FILTER_DEL_MEMBER = addBoolProp("fee-filter-deleted-member", false);
    public BooleanProperty getFEE_FILTER_DEL_MEMBER_OFF = addBoolProp("fee-filter-deleted-member-off", true);

    public BooleanProperty FEE_FILTER_BEZAHLT = addBoolProp("fee-filter-bezahlt", false);
    public BooleanProperty FEE_FILTER_BEZAHLT_OFF = addBoolProp("fee-filter-bezahlt-off", true);
    public BooleanProperty FEE_FILTER_RECHNUNG = addBoolProp("fee-filter-rechnung", false);
    public BooleanProperty FEE_FILTER_RECHNUNG_OFF = addBoolProp("fee-filter-rechnung-off", true);
    public BooleanProperty FEE_FILTER_SQ = addBoolProp("fee-filter-sq", false);
    public BooleanProperty FEE_FILTER_SQ_OFF = addBoolProp("fee-filter-sq-off", true);

    // GuiFinance Filter
    public StringProperty FINANCE_FILTER_BELEG_NR = addStrPropC("GuiFinance Filter", "finance-filter-belegnr");
    public ObjectProperty<Integer> FINANCE_FILTER_GESCHAEFTS_JAHR = addObjIntProp("finaces-filter-geschaefts-jahr");
    public LongProperty FINANCE_FILTER_ACCOUNT_ID = addLongProp("finance-filter-account-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<FinanceAccountData> FINANCE_FILTER_ACCOUNT = new SimpleObjectProperty<>();
    public LongProperty FINANCE_FILTER_CATEGORY_ID = addLongProp("finance-filter-category-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<FinanceCategoryData> FINANCE_FILTER_CATEGORY = new SimpleObjectProperty<>();

    // GuiFinanceReport Filter
    public StringProperty FINANCE_REPORT_FILTER_BELEG_NR = addStrPropC("GuiFinance Filter", "finance-report-filter-belegnr");
    public ObjectProperty<Integer> FINANCE_REPORT_FILTER_GESCHAEFTS_JAHR = addObjIntProp("finaces-report-filter-geschaefts-jahr");
    public LongProperty FINANCE_REPORT_FILTER_ACCOUNT_ID = addLongProp("finance-report-filter-account-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<FinanceAccountData> FINANCE_REPORT_FILTER_ACCOUNT = new SimpleObjectProperty<>();
    public LongProperty FINANCE_REPORT_FILTER_CATEGORY_ID = addLongProp("finance-report-filter-category-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<FinanceCategoryData> FINANCE_REPORT_FILTER_CATEGORY = new SimpleObjectProperty<>();

    // GuiMember
    public StringProperty MEMBER_GUI_TABLE_WIDTH = addStrPropC("GuiMember", "member-gui-table-width");
    public StringProperty MEMBER_GUI_TABLE_SORT = addStrProp("member-gui-table-sort");
    public StringProperty MEMBER_GUI_TABLE_UPDOWN = addStrProp("member-gui-table-upDown");
    public StringProperty MEMBER_GUI_TABLE_VIS = addStrProp("member-gui-table-vis");
    public StringProperty MEMBER_GUI_TABLE_ORDER = addStrProp("member-gui-table-order");

    // GuiMember-Rates
    public StringProperty MEMBER_RATES_GUI_TABLE_WIDTH = addStrPropC("GuiMember-Rates", "member-rates-gui-table-width");
    public StringProperty MEMBER_RATES_GUI_TABLE_SORT = addStrProp("member-rates-gui-table-sort");
    public StringProperty MEMBER_RATES_GUI_TABLE_UPDOWN = addStrProp("member-rates-gui-table-upDown");
    public StringProperty MEMBER_RATES_GUI_TABLE_VIS = addStrProp("member-rates-gui-table-vis");
    public StringProperty MEMBER_RATES_GUI_TABLE_ORDER = addStrProp("member-rates-gui-table-order");

    // GuiMember-states
    public StringProperty MEMBER_STATES_GUI_TABLE_WIDTH = addStrPropC("GuiMember-states", "member-states-gui-table-width");
    public StringProperty MEMBER_STATES_GUI_TABLE_SORT = addStrProp("member-states-gui-table-sort");
    public StringProperty MEMBER_STATES_GUI_TABLE_UPDOWN = addStrProp("member-states-gui-table-upDown");
    public StringProperty MEMBER_STATES_GUI_TABLE_VIS = addStrProp("member-states-gui-table-vis");
    public StringProperty MEMBER_STATES_GUI_TABLE_ORDER = addStrProp("member-states-gui-table-order");

    // GuiMember-PaymentTypes
    public StringProperty MEMBER_PAYMENT_TYPES_GUI_TABLE_WIDTH = addStrPropC("GuiMember-PaymentTypes", "member-payment-types-gui-table-width");
    public StringProperty MEMBER_PAYMENT_TYPES_GUI_TABLE_SORT = addStrProp("member-payment-types-gui-table-sort");
    public StringProperty MEMBER_PAYMENT_TYPES_GUI_TABLE_UPDOWN = addStrProp("member-payment-types-gui-table-upDown");
    public StringProperty MEMBER_PAYMENT_TYPES_GUI_TABLE_VIS = addStrProp("member-payment-types-gui-table-vis");
    public StringProperty MEMBER_PAYMENT_TYPES_GUI_TABLE_ORDER = addStrProp("member-payment-types-gui-table-order");

    // GuiFee
    public StringProperty FEE_GUI_TABLE_WIDTH = addStrPropC("GuiFee", "fee-gui-table-width");
    public StringProperty FEE_GUI_TABLE_SORT = addStrProp("fee-gui-table-sort");
    public StringProperty FEE_GUI_TABLE_UPDOWN = addStrProp("fee-gui-table-upDown");
    public StringProperty FEE_GUI_TABLE_VIS = addStrProp("fee-gui-table-vis");
    public StringProperty FEE_GUI_TABLE_ORDER = addStrProp("fee-gui-table-order");

    // DialogAddMissingFee
    public StringProperty ADD_MISSING_FEE_GUI_TABLE_WIDTH = addStrPropC("DialogAddMissingFee", "add-missing-fee-gui-table-width");
    public StringProperty ADD_MISSING_FEE_GUI_TABLE_SORT = addStrProp("add-missing-fee-gui-table-sort");
    public StringProperty ADD_MISSING_FEE_GUI_TABLE_UPDOWN = addStrProp("add-missing-fee-gui-table-upDown");
    public StringProperty ADD_MISSING_FEE_GUI_TABLE_VIS = addStrProp("add-missing-fee-gui-table-vis");
    public StringProperty ADD_MISSING_FEE_GUI_TABLE_ORDER = addStrProp("add-missing-fee-gui-table-order");

    // DialogPayFee
    public StringProperty PAY_FEE_TABLE_WIDTH = addStrPropC("DialogPayFee", "pay-fee-table-width");
    public StringProperty PAY_FEE_TABLE_SORT = addStrProp("pay-fee-table-sort");
    public StringProperty PAY_FEE_TABLE_UPDOWN = addStrProp("pay-fee-table-upDown");
    public StringProperty PAY_FEE_TABLE_VIS = addStrProp("pay-fee-table-vis");
    public StringProperty PAY_FEE_TABLE_ORDER = addStrProp("pay-fee-table-order");
    public LongProperty PAY_FEE_ACCOUNT_ID = addLongProp("pay-fee-account-id", 0);
    public ObjectProperty<FinanceAccountData> PAY_FEE_ACCOUNT = new SimpleObjectProperty<>();
    public LongProperty PAY_FEE_CATEGORY_ID = addLongProp("pay-fee-category-id", 0);
    public ObjectProperty<FinanceCategoryData> PAY_FEE_CATEGORY = new SimpleObjectProperty<>();

    public ObservableList<String> PAY_FEE_SEPA_DIR_LIST = addListProp("pay-fee-sepa-dir-list");
    public ObservableList<String> PAY_FEE_SEPA_FILE_LIST = addListProp("pay-fee-sepa-file-list");
    public ObservableList<String> PAY_FEE_SEPA_BEGLEIT_FILE_LIST = addListProp("pay-fee-sepa-begleit-file-list");

    public StringProperty PAY_FEE_SEPA_DIR = addStrProp("pay-fee-sepa-dir");
    public StringProperty PAY_FEE_SEPA_FILE = addStrProp("pay-fee-sepa-file");
    public StringProperty PAY_FEE_SEPA_BEGLEIT_FILE = addStrProp("pay-fee-sepa-begleit-file");

//    public StringProperty PAY_FEE_SEPA_DATE = addStrProp("pay-fee-sepa-date");

    // GuiFinance
    public StringProperty FINANCE_GUI_TABLE_WIDTH = addStrPropC("GuiFinance", "finance-gui-table-width");
    public StringProperty FINANCE_GUI_TABLE_SORT = addStrProp("finance-gui-table-sort");
    public StringProperty FINANCE_GUI_TABLE_UPDOWN = addStrProp("finance-gui-table-upDown");
    public StringProperty FINANCE_GUI_TABLE_VIS = addStrProp("finance-gui-table-vis");
    public StringProperty FINANCE_GUI_TABLE_ORDER = addStrProp("finance-gui-table-order");

    // GuiFinance-Account
    public StringProperty FINANCE_ACCOUNT_GUI_TABLE_WIDTH = addStrPropC("GuiFinance-Account", "finance-account-gui-table-width");
    public StringProperty FINANCE_ACCOUNT_GUI_TABLE_SORT = addStrProp("finance-account-gui-table-sort");
    public StringProperty FINANCE_ACCOUNT_GUI_TABLE_UPDOWN = addStrProp("finance-account-gui-table-upDown");
    public StringProperty FINANCE_ACCOUNT_GUI_TABLE_VIS = addStrProp("finance-account-gui-table-vis");
    public StringProperty FINANCE_ACCOUNT_GUI_TABLE_ORDER = addStrProp("finance-account-gui-table-order");

    // GuiFinance-Category
    public StringProperty FINANCE_CATEGORY_GUI_TABLE_WIDTH = addStrPropC("GuiFinance-Category", "finance-category-gui-table-width");
    public StringProperty FINANCE_CATEGORY_GUI_TABLE_SORT = addStrProp("finance-category-gui-table-sort");
    public StringProperty FINANCE_CATEGORY_GUI_TABLE_UPDOWN = addStrProp("finance-category-gui-table-upDown");
    public StringProperty FINANCE_CATEGORY_GUI_TABLE_VIS = addStrProp("finance-category-gui-table-vis");
    public StringProperty FINANCE_CATEGORY_GUI_TABLE_ORDER = addStrProp("finance-category-gui-table-order");

    // Transaction
    public StringProperty TRANSACTION_GUI_TABLE_WIDTH = addStrPropC("Transaction", "transaction-gui-table-width");
    public StringProperty TRANSACTION_GUI_TABLE_SORT = addStrProp("transaction-gui-table-sort");
    public StringProperty TRANSACTION_GUI_TABLE_UPDOWN = addStrProp("transaction-gui-table-upDown");
    public StringProperty TRANSACTION_GUI_TABLE_VIS = addStrProp("transaction-gui-table-vis");
    public StringProperty TRANSACTION_GUI_TABLE_ORDER = addStrProp("transaction-gui-table-order");

    // FinaceReport
    public StringProperty FINANCE_REPORT_GUI_TABLE_WIDTH = addStrPropC("FinanceReport", "finance-report-gui-table-width");
    public StringProperty FINANCE_REPORT_GUI_TABLE_SORT = addStrProp("finance-report-gui-table-sort");
    public StringProperty FINANCE_REPORT_GUI_TABLE_UPDOWN = addStrProp("finance-report-gui-table-upDown");
    public StringProperty FINANCE_REPORT_GUI_TABLE_VIS = addStrProp("finance-report-gui-table-vis");
    public StringProperty FINANCE_REPORT_GUI_TABLE_ORDER = addStrProp("finance-report-gui-table-order");

    // Calculation Category
    public StringProperty CALCULATION_CATEGORY_GUI_TABLE_WIDTH = addStrPropC("Calculation Category", "calculation-category-gui-table-width");
    public StringProperty CALCULATION_CATEGORY_GUI_TABLE_SORT = addStrProp("calculation-category-gui-table-sort");
    public StringProperty CALCULATION_CATEGORY_GUI_TABLE_UPDOWN = addStrProp("calculation-category-gui-table-upDown");
    public StringProperty CALCULATION_CATEGORY_GUI_TABLE_VIS = addStrProp("calculation-category-gui-table-vis");
    public StringProperty CALCULATION_CATEGORY_GUI_TABLE_ORDER = addStrProp("calculation-category-gui-table-order");

    // Calculation Account
    public StringProperty CALCULATION_ACCOUNT_GUI_TABLE_WIDTH = addStrPropC("Calculation Account", "calculation-account-gui-table-width");
    public StringProperty CALCULATION_ACCOUNT_GUI_TABLE_SORT = addStrProp("calculation-account-gui-table-sort");
    public StringProperty CALCULATION_ACCOUNT_GUI_TABLE_UPDOWN = addStrProp("calculation-account-gui-table-upDown");
    public StringProperty CALCULATION_ACCOUNT_GUI_TABLE_VIS = addStrProp("calculation-account-gui-table-vis");
    public StringProperty CALCULATION_ACCOUNT_GUI_TABLE_ORDER = addStrProp("calculation-account-gui-table-order");

    // Combobox-Listen
    public ObservableList<String> CBO_LIST_ANREDE = addListPropC("Combobox-Listen", "cbo-list-anrede");
    public ObservableList<String> CBO_LIST_PLZ = addListProp("cbo-list-plz");
    public ObservableList<String> CBO_LIST_ORT = addListProp("cbo-list-ort");
    public ObservableList<String> CBO_LIST_LAND = addListProp("cbo-list-land");

    // Serienbrief
    public ObservableList<String> CBO_LIST_NEWSLETTER_TEMPLATE = addListPropC("Serienbrief", "cbo-list-newsletter-template");
    public ObservableList<String> CBO_LIST_NEWSLETTER_DIR = addListProp("cbo-list-newsletter-dir");
    public ObservableList<String> CBO_LIST_NEWSLETTER_FILE = addListProp("cbo-list-newsletter-file");
    public StringProperty NEWSLETTER_TEMPLATE = addStrProp("newsletter-template");
    public StringProperty NEWSLETTER_DIR = addStrProp("newsletter-dir");
    public StringProperty NEWSLETTER_FILE = addStrProp("newsletter-file");

    // Finanzreport
    public ObservableList<String> CBO_LIST_DIR_FINANZREPORT = addListProp("cbo-list-finanzreport");
    public StringProperty EXPORT_DIR_FINANZREPORT = addStrProp("export-dir-finanzreport");
    public ObservableList<String> CBO_LIST_EXPORT_FILE_FINANZREPORT = addListProp("cbo-list-export-file-finanzreport");
    public StringProperty EXPORT_FILE_FINANZREPORT = addStrProp("export-file-finanzreport");
    public BooleanProperty EXPORT_TRANSACTION_SHORT = addBoolProp("export-transaction-short", false);

    // CSV
    public ObservableList<String> CBO_LIST_EXPORT_CSV_DIR = addListProp("cbo-list-export-csv-dir");
    public StringProperty EXPORT_CSV_DIR = addStrProp("export-csv-dir");
    public ObservableList<String> CBO_LIST_EXPORT_CSV_FILE = addListProp("cbo-list-export-csv-file");
    public StringProperty EXPORT_CSV_FILE = addStrProp("export-csv-file");
    public ObservableList<String> CBO_LIST_IMPORT_CSV_FILE = addListProp("cbo-list-import-csv-file");
    public StringProperty IMPORT_CSV_FILE = addStrProp("import-csv-file");
    // Infos
    public ObservableList<String> CBO_LIST_EXPORT_INFOS_DIR = addListProp("cbo-list-export-infos-dir");
    public StringProperty EXPORT_INFOS_DIR = addStrProp("export-infos-dir");
    public ObservableList<String> CBO_LIST_EXPORT_INFOS_FILE = addListProp("cbo-list-export-infos-file");
    public StringProperty EXPORT_INFOS_FILE = addStrProp("export-infos-file");
    // ZIP
    public ObservableList<String> CBO_LIST_EXPORT_ZIP_DIR = addListProp("cbo-list-export-zip-dir");
    public StringProperty EXPORT_ZIP_DIR = addStrProp("export-zip-dir");
    public ObservableList<String> CBO_LIST_EXPORT_ZIP_FILE = addListProp("cbo-list-export-zip-file");
    public StringProperty EXPORT_ZIP_FILE = addStrProp("export-zip-file");

    // Serienbrief Dialog Rechnung
    public StringProperty NEWSLETTER_TEMPLATE_BILL = addStrPropC("Serienbrief Dialog Rechnung", "newsletter-template-bill");
    public StringProperty NEWSLETTER_DIR_BILL = addStrProp("newsletter-dir-bill");
    public StringProperty NEWSLETTER_NAME_BILL = addStrProp("newsletter-name-bill");

    // Serienbrief Dialog SQ
    public StringProperty NEWSLETTER_TEMPLATE_SQ = addStrPropC("Serienbrief Dialog SQ", "newsletter-template-sq");
    public StringProperty NEWSLETTER_DIR_SQ = addStrProp("newsletter-dir-sq");
    public StringProperty NEWSLETTER_NAME_SQ = addStrProp("newsletter-name-sq");

    // Export Club
    public StringProperty EXPORT_CLUB_DIR = addStrProp("export-club-dir");

    // DemoData
    public BooleanProperty DEMO_DATA_REMOVE_OLD_DATA = addBoolPropC("DemoData", "demo-data-remove-old-data", false);
    public BooleanProperty DEMO_DATA_CLUB = addBoolProp("demo-data-club", true);
    public BooleanProperty DEMO_DATA_MEMBER = addBoolProp("demo-data-member", true);
    public BooleanProperty DEMO_DATA_FINANCES = addBoolProp("demo-data-finances", true);
    public IntegerProperty DEMO_DATA_ADD_MEMBER = addIntProp("demo-data-add-member", 100);
    public IntegerProperty DEMO_DATA_ADD_FEE = addIntProp("demo-data-add-fee", DemoConst.ADD_AMOUNT_FEE.FEE_ADD_SOME.getNo());
    public IntegerProperty DEMO_DATA_ADD_FEE_PAY = addIntProp("demo-data-add-fee-pay", DemoConst.ADD_AMOUNT_FEE.FEE_ADD_SOME.getNo());
    public IntegerProperty DEMO_DATA_ADD_FINANCE = addIntProp("demo-data-add-finance", 150);

    public StringProperty DEMO_DATA_ADD_CLUB_NAME = addStrProp("demo-data-add-club-name", DemoClubDataFactory.getName());
    public StringProperty DEMO_DATA_ADD_CLUB_ORT = addStrProp("demo-data-add-club-ort", DemoClubDataFactory.getOrt());
    public StringProperty DEMO_DATA_ADD_CLUB_PLZ = addStrProp("demo-data-add-club-plz", DemoClubDataFactory.getPlz());
    public StringProperty DEMO_DATA_ADD_CLUB_STRASSE = addStrProp("demo-data-add-club-strasse", DemoClubDataFactory.getStrasse());
    public StringProperty DEMO_DATA_ADD_CLUB_TELEFON = addStrProp("demo-data-add-club-telefon", DemoClubDataFactory.getTelefon());
    public StringProperty DEMO_DATA_ADD_CLUB_EMAIL = addStrProp("demo-data-add-club-email", DemoClubDataFactory.getEmail());
    public StringProperty DEMO_DATA_ADD_CLUB_WEBSITE = addStrProp("demo-data-add-club-website", DemoClubDataFactory.getWebsite());

    public StringProperty DEMO_DATA_ADD_CLUB_KONTO_NR = addStrProp("demo-data-add-club-konto-nr", DemoClubDataFactory.getKontoNr());
    public StringProperty DEMO_DATA_ADD_CLUB_BIC = addStrProp("demo-data-add-club-bic", DemoClubDataFactory.getBic());
    public StringProperty DEMO_DATA_ADD_CLUB_IBAN = addStrProp("demo-data-add-club-iban", DemoClubDataFactory.getIban());
    public StringProperty DEMO_DATA_ADD_CLUB_BANK = addStrProp("demo-data-add-club-bank", DemoClubDataFactory.getBank());
    public StringProperty DEMO_DATA_ADD_CLUB_GLAEUBIGER_ID = addStrProp("demo-data-add-club-glaeubiger-id", DemoClubDataFactory.getGlaeubigerId());

    // export member data
    public final BooleanProperty MEMBER_EXPORT_ALL = addBoolProp("member-export-all", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Nr = addBoolProp("member-export-data-nr", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Nachname = addBoolProp("member-export-data-nachname", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Vorname = addBoolProp("member-export-data-vorname", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Anrede = addBoolProp("member-export-data-anrede", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Email = addBoolProp("member-export-data-email", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Telefon = addBoolProp("member-export-data-telefon", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Strasse = addBoolProp("member-export-data-strasse", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_PLZ = addBoolProp("member-export-data-plz", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Ort = addBoolProp("member-export-data-ort", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Land = addBoolProp("member-export-data-land", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Status = addBoolProp("member-export-data-status", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Beitrag = addBoolProp("member-export-data-beitrag", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Beitragssatz = addBoolProp("member-export-data-beitragssatz", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Bank = addBoolProp("member-export-data-bank", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Iban = addBoolProp("member-export-data-iban", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Bic = addBoolProp("member-export-data-bic", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Kontoinhaber = addBoolProp("member-export-data-kontoinhaber", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Zahlart = addBoolProp("member-export-data-zahlart", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Zahlungsbeginn = addBoolProp("member-export-data-zahlungsbeginn", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Sepabeginn = addBoolProp("member-export-data-sepabeginn", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Beitritt = addBoolProp("member-export-data-beitritt", false);

    // export member data
    public final BooleanProperty FEE_EXPORT_ALL = addBoolProp("fee-export-all", false);
    public final BooleanProperty FEE_EXPORT_DATA_Nr = addBoolProp("fee-export-data-nr", true);
    public final BooleanProperty FEE_EXPORT_DATA_MemberNr = addBoolProp("fee-export-data-memberNr", true);
    public final BooleanProperty FEE_EXPORT_DATA_MemberName = addBoolProp("fee-export-data-memberName", true);
    public final BooleanProperty FEE_EXPORT_DATA_Betrag = addBoolProp("fee-export-data-betrag", true);
    public final BooleanProperty FEE_EXPORT_DATA_BetragInWords = addBoolProp("fee-export-data-betrag-in-words", true);
    public final BooleanProperty FEE_EXPORT_DATA_Jahr = addBoolProp("fee-export-data-jahr", true);
    public final BooleanProperty FEE_EXPORT_DATA_Zahlart = addBoolProp("fee-export-data-zahlart", true);
    public final BooleanProperty FEE_EXPORT_DATA_Bezahlt = addBoolProp("fee-export-data-bezahlt", true);
    public final BooleanProperty FEE_EXPORT_DATA_Rechnung = addBoolProp("fee-export-data-rechnung", true);
    public final BooleanProperty FEE_EXPORT_DATA_SpendenQ = addBoolProp("fee-export-data-spendenQ", true);
    public final BooleanProperty FEE_EXPORT_DATA_Erstelldatum = addBoolProp("fee-export-data-Erstelldatum", true);
    public final BooleanProperty FEE_EXPORT_DATA_Text = addBoolProp("fee-export-data-text", true);

    //expport finance data
    public final BooleanProperty FINANCE_EXPORT_ALL = addBoolProp("finance-export-all", false);
    public final BooleanProperty FINANCE_EXPORT_DATA_Nr = addBoolProp("finance-export-data-nr", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_BelegNr = addBoolProp("finance-export-data-belegNr", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Gesamtbetrag = addBoolProp("finance-export-data-gesamtbetrag", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Konto = addBoolProp("finance-export-data-konto", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Kategorie = addBoolProp("finance-export-data-kategorie", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Geschäftsjahr = addBoolProp("finance-export-data-geschäftsjahr", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Buchungsdatum = addBoolProp("finance-export-data-buchungsdatum", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Erstelldatum = addBoolProp("finance-export-data-erstelldatum", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Text = addBoolProp("finance-export-data-text", true);


    // Gui
    public ClubGuiController clubGuiController = null;
    public GuiClub guiClub = null;
    public GuiClubInfo guiClubInfo = null;
    public GuiMember guiMember = null;
    public GuiFeeRate guiFeeRate = null;
    public GuiMemberState guiMemberState = null;
    public GuiFeePaymentType guiFeePaymentType = null;
    public GuiFee guiFee = null;
    public GuiFinance guiFinance = null;
    public GuiFinanceReport guiFinanceReport = null;
    public GuiFinanceAccount guiFinanceAccount = null;
    public GuiFinanceCategory guiFinanceCategory = null;

    // clubdata
    public final ExtraDataListMember extraDataListMember;
    public final ExtraDataListFee extraDataListFee;
    public final ExtraDataListFinance extraDataListFinance;
    public final ClubData clubData;

    public final MemberDataList memberDataList;
    public final FeeDataList feeDataList;
    public final FinanceDataList financeDataList;
    public final FinanceReportDataList financeReportDataList;
    public final FinanceReportDataList financeReportDataListSum;

    public final StateDataList stateDataList;
    public final FeeRateDataList feeRateDataList;
    public final PaymentTypeDataList paymentTypeDataList;

    public final FinanceAccountDataList financeAccountDataList;
    public final FinanceCategoryDataList financeCategoryDataList;

    // filter changed
    public final BooleanProperty memberFilterChange = new SimpleBooleanProperty(false);
    public final BooleanProperty feeFilterChange = new SimpleBooleanProperty(false);
    public final BooleanProperty financesFilterChange = new SimpleBooleanProperty(false);
    public final BooleanProperty financesReportFilterChange = new SimpleBooleanProperty(false);

    private String clubPath;
    private Stage stage = null;
    public PMaskerPane pMaskerPane = null;
    public Worker worker = null;

    // nur für den ersten Start interessant
    private boolean firstStart = false;
    private boolean addDemoData = false;
    private boolean clubIsStarting = false;

    public ClubConfig(String clubPath, String clubName) {
        super.init("ClubConfig");

        this.clubPath = clubPath;

        worker = new Worker(this);
        extraDataListMember = new ExtraDataListMember(this);
        extraDataListFee = new ExtraDataListFee(this);
        extraDataListFinance = new ExtraDataListFinance(this);
        clubData = new ClubData(clubName);
        memberDataList = new MemberDataList(this);
        feeRateDataList = new FeeRateDataList(this);
        stateDataList = new StateDataList(this);
        paymentTypeDataList = new PaymentTypeDataList(this);
        feeDataList = new FeeDataList(this);
        financeDataList = new FinanceDataList(this);
        financeReportDataList = new FinanceReportDataList(this);
        financeReportDataListSum = new FinanceReportDataList(this);
        financeAccountDataList = new FinanceAccountDataList(this);
        financeCategoryDataList = new FinanceCategoryDataList(this);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isFirstStart() {
        if (firstStart) {
            firstStart = false;
            return true;
        }

        return false;
    }

    public void setFirstStart(boolean firstStart) {
        this.firstStart = firstStart;
    }

    public String getClubPath() {
        return clubPath;
    }

    public void setClubPath(String clubPath) {
        this.clubPath = clubPath;
    }

    public boolean isAddDemoData() {
        if (addDemoData) {
            addDemoData = false;
            return true;
        }

        return false;
    }

    public void setAddDemoData(boolean addDemoData) {
        this.addDemoData = addDemoData;
    }

    public boolean isClubIsStarting() {
        return clubIsStarting;
    }

    public void setClubIsStarting(boolean clubIsStarting) {
        this.clubIsStarting = clubIsStarting;
    }
}
