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
import de.p2tools.p2Lib.configFile.pData.PDataProgConfig;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ClubConfig extends PDataProgConfig {
    private final ArrayList<Config> arrayList = new ArrayList<>();

    // Fenstereinstellungen
    public StringProperty SYSTEM_LOG_DIR = addStr("system-log-dir", "");


    public StringProperty GUI_CLUB_PANEL = addStrC("Fenstereinstellungen", "gui-club-panel", "1000:800");

    public DoubleProperty GUI_PANEL_MEMBER_DIVIDER_CONT = addDouble("gui-panel-member-divider-cont", 0.7);
    public DoubleProperty GUI_PANEL_MEMBER_DIVIDER_INFO = addDouble("gui-panel-member-divider-info", 0.5);

    public DoubleProperty GUI_PANEL_FEE_DIVIDER_CONT = addDouble("gui-panel-fee-divider-cont", 0.7);
    public DoubleProperty GUI_PANEL_FEE_DIVIDER_INFO = addDouble("gui-panel-fee-divider-info", 0.5);

    public DoubleProperty GUI_PANEL_FINANDES_DIVIDER_CONT = addDouble("gui-panel-finances-divider-cont", 0.6);
    public DoubleProperty GUI_PANEL_FINANCES_DIVIDER_INFO_0 = addDouble("gui-panel-finances-divider-info-0", 0.5);
    public DoubleProperty GUI_PANEL_FINANCES_DIVIDER_INFO_1 = addDouble("gui-panel-finances-divider-info-1", 0.7);
    public DoubleProperty GUI_PANEL_FINANCES_DIVIDER_CALCULATION_0 = addDouble("gui-panel-finances-divider-calculation-0", 0.4);
    public DoubleProperty GUI_PANEL_FINANCES_DIVIDER_CALCULATION_1 = addDouble("gui-panel-finances-divider-calculation-1", 0.7);

    // Dialoge
    public StringProperty CLUB_CONFIG_DIALOG_SIZE = addStr("club-config-dialog-size", "800:600");
    public StringProperty CLUB_EXTRA_DATA_DIALOG_SIZE = addStr("club-extra-data-dialog-size", "500:700");
    public StringProperty DATA_DIALOG_SIZE = addStr("data-dialog-size");
    public StringProperty FEE_RATE_DATA_DIALOG_SIZE = addStr("fee-rate-data-dialog-size");
    public StringProperty STATE_DATA_DIALOG_SIZE = addStr("state-data-dialog-size");
    public StringProperty FINANCE_ACCOUNT_DATA_DIALOG_SIZE = addStr("finance-account-data-dialog-size");

    public StringProperty FINANCE_CATEGORY_DATA_DIALOG_SIZE = addStr("finance-category-data-dialog-size");
    public StringProperty PAYMENT_TYPE_DATA_DIALOG_SIZE = addStr("payment-type-data-dialog-size");
    public StringProperty ADD_DEMO_DATA_DIALOG_SIZE = addStr("add-demo-data-dialog-size");
    public StringProperty EXPORT_CSV_DIALOG_SIZE = addStr("export-member-csv-dialog-size", "700:400");
    public StringProperty EXPORT_FINANCE_REPORT = addStr("export-finance-report", "700:400");
    public StringProperty IMPORT_MEMBER_CSV_DIALOG_SIZE = addStr("import-member-csv-dialog-size", "700:400");
    public StringProperty EXPORT_CLUB_ZIP_DIALOG_SIZE = addStr("export-club-zip-dialog-size", "700:400");
    public StringProperty EXPORT_CLUB_INFOS_DIALOG_SIZE = addStr("export-club-infos-dialog-size", "700:400");
    public StringProperty DELETE_ALL_DATA_DIALOG_SIZE = addStr("delete-all-data-dialog-size", "600:300");

    public DoubleProperty MISSING_FEE_DIALOG_DIVIDER = addDouble("missing-fee-dialog-divider", 0.3);
    public StringProperty MISSING_FEE_DIALOG_SIZE = addStrC("Dialoge", "fee-dialog-size", "800:600");
    public DoubleProperty BILL_FOR_FEE_DIALOG_DIVIDER = addDouble("bill-for-fee-dialog-divider", 0.3);
    public StringProperty BILL_FOR_FEE_DIALOG_SIZE = addStr("bill-for-fee-dialog-size");
    public DoubleProperty PAY_FEE_DIALOG_DIVIDER = addDouble("pay-fee-dialog-divider", 0.3);
    public StringProperty PAY_FEE_DIALOG_SIZE = addStr("pay-fee-dialog-size");

    public BooleanProperty FEE_DIALOG_ADD_FINANCES = addBool("fee-dialog-add-finance", Boolean.TRUE);
    public BooleanProperty FEE_DIALOG_ADD_TRANSACTIONS = addBool("fee-dialog-add-transactions", Boolean.TRUE);
    public BooleanProperty FEE_DIALOG_ADD_DTAUS = addBool("fee-dialog-add-dtaus", Boolean.TRUE);
    public StringProperty NEWSLETTER_TO_ODF_DIALOG_SIZE = addStr("newsletter-to-odf-dialog-size", "");
    public BooleanProperty FEE_DIALOG_ADD_BILL = addBool("fee-dialog-add-bill", Boolean.TRUE);
    public BooleanProperty FEE_DIALOG_ADD_SQ = addBool("fee-dialog-add-sq", Boolean.TRUE);

    // GuiMember Filter
    public StringProperty MEMBER_FILTER_NACHNAME = addStrC("GuiMember Filter", "member-filter-nachname");
    public BooleanProperty MEMBER_FILTER_FEE_CREATED = addBool("member-filter-fee", false);
    public BooleanProperty MEMBER_FILTER_FEE_CREATED_OFF = addBool("member-filter-fee-off", true);
    public BooleanProperty MEMBER_FILTER_FEE_PAYED = addBool("member-filter-fee-payed", false);
    public BooleanProperty MEMBER_FILTER_FEE_PAYED_OFF = addBool("member-filter-fee-payed-off", true);
    public LongProperty MEMBER_FILTER_STATUS_ID = addLong("member-filter-status-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<StateData> MEMBER_FILTER_STATUS = new SimpleObjectProperty<>();
    public LongProperty MEMBER_FILTER_BEITRAG_ID = addLong("member-filter-beitrag-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<FeeRateData> MEMBER_FILTER_BEITRAG = new SimpleObjectProperty<>();
    public LongProperty MEMBER_FILTER_PAYMENT_TYPE_ID = addLong("member-filter-payment-type-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<PaymentTypeData> MEMBER_FILTER_PAYMENT_TYPE = new SimpleObjectProperty<>();


    // GuiFee Filter
    public LongProperty FEE_FILTER_MITGLIED_ID = addLongC("GuiFee Filter", "fee-filter-mitglied-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<MemberData> FEE_FILTER_MITGLIED = new SimpleObjectProperty<>();
    public LongProperty FEE_FILTER_PAYMENT_TYPE_ID = addLong("fee-filter-payment-type-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<PaymentTypeData> FEE_FILTER_PAYMENT_TYPE = new SimpleObjectProperty<>();

    public ObjectProperty<Integer> FEE_FILTER_JAHR = addObjIntProp(arrayList, "fee-filter-filter-jahr");
    public BooleanProperty FEE_FILTER_DEL_MEMBER = addBool("fee-filter-deleted-member", false);
    public BooleanProperty getFEE_FILTER_DEL_MEMBER_OFF = addBool("fee-filter-deleted-member-off", true);

    public BooleanProperty FEE_FILTER_BEZAHLT = addBool("fee-filter-bezahlt", false);
    public BooleanProperty FEE_FILTER_BEZAHLT_OFF = addBool("fee-filter-bezahlt-off", true);
    public BooleanProperty FEE_FILTER_RECHNUNG = addBool("fee-filter-rechnung", false);
    public BooleanProperty FEE_FILTER_RECHNUNG_OFF = addBool("fee-filter-rechnung-off", true);
    public BooleanProperty FEE_FILTER_SQ = addBool("fee-filter-sq", false);
    public BooleanProperty FEE_FILTER_SQ_OFF = addBool("fee-filter-sq-off", true);

    // GuiFinance Filter
    public StringProperty FINANCE_FILTER_BELEG_NR = addStrC("GuiFinance Filter", "finance-filter-belegnr");
    public ObjectProperty<Integer> FINANCE_FILTER_GESCHAEFTS_JAHR = addObjIntProp(arrayList, "finaces-filter-geschaefts-jahr");
    public LongProperty FINANCE_FILTER_ACCOUNT_ID = addLong("finance-filter-account-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<FinanceAccountData> FINANCE_FILTER_ACCOUNT = new SimpleObjectProperty<>();
    public LongProperty FINANCE_FILTER_CATEGORY_ID = addLong("finance-filter-category-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<FinanceCategoryData> FINANCE_FILTER_CATEGORY = new SimpleObjectProperty<>();

    // GuiFinanceReport Filter
    public StringProperty FINANCE_REPORT_FILTER_BELEG_NR = addStrC("GuiFinance Filter", "finance-report-filter-belegnr");
    public ObjectProperty<Integer> FINANCE_REPORT_FILTER_GESCHAEFTS_JAHR = addObjIntProp(arrayList, "finaces-report-filter-geschaefts-jahr");
    public LongProperty FINANCE_REPORT_FILTER_ACCOUNT_ID = addLong("finance-report-filter-account-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<FinanceAccountData> FINANCE_REPORT_FILTER_ACCOUNT = new SimpleObjectProperty<>();
    public LongProperty FINANCE_REPORT_FILTER_CATEGORY_ID = addLong("finance-report-filter-category-id", ProgConst.FILTER_ID_NOT_SELECTED);
    public ObjectProperty<FinanceCategoryData> FINANCE_REPORT_FILTER_CATEGORY = new SimpleObjectProperty<>();

    // GuiMember
    public StringProperty MEMBER_GUI_TABLE_WIDTH = addStrC("GuiMember", "member-gui-table-width");
    public StringProperty MEMBER_GUI_TABLE_SORT = addStr("member-gui-table-sort");
    public StringProperty MEMBER_GUI_TABLE_UPDOWN = addStr("member-gui-table-upDown");
    public StringProperty MEMBER_GUI_TABLE_VIS = addStr("member-gui-table-vis");
    public StringProperty MEMBER_GUI_TABLE_ORDER = addStr("member-gui-table-order");

    // GuiMember-Rates
    public StringProperty MEMBER_RATES_GUI_TABLE_WIDTH = addStrC("GuiMember-Rates", "member-rates-gui-table-width");
    public StringProperty MEMBER_RATES_GUI_TABLE_SORT = addStr("member-rates-gui-table-sort");
    public StringProperty MEMBER_RATES_GUI_TABLE_UPDOWN = addStr("member-rates-gui-table-upDown");
    public StringProperty MEMBER_RATES_GUI_TABLE_VIS = addStr("member-rates-gui-table-vis");
    public StringProperty MEMBER_RATES_GUI_TABLE_ORDER = addStr("member-rates-gui-table-order");

    // GuiMember-states
    public StringProperty MEMBER_STATES_GUI_TABLE_WIDTH = addStrC("GuiMember-states", "member-states-gui-table-width");
    public StringProperty MEMBER_STATES_GUI_TABLE_SORT = addStr("member-states-gui-table-sort");
    public StringProperty MEMBER_STATES_GUI_TABLE_UPDOWN = addStr("member-states-gui-table-upDown");
    public StringProperty MEMBER_STATES_GUI_TABLE_VIS = addStr("member-states-gui-table-vis");
    public StringProperty MEMBER_STATES_GUI_TABLE_ORDER = addStr("member-states-gui-table-order");

    // GuiMember-PaymentTypes
    public StringProperty MEMBER_PAYMENT_TYPES_GUI_TABLE_WIDTH = addStrC("GuiMember-PaymentTypes", "member-payment-types-gui-table-width");
    public StringProperty MEMBER_PAYMENT_TYPES_GUI_TABLE_SORT = addStr("member-payment-types-gui-table-sort");
    public StringProperty MEMBER_PAYMENT_TYPES_GUI_TABLE_UPDOWN = addStr("member-payment-types-gui-table-upDown");
    public StringProperty MEMBER_PAYMENT_TYPES_GUI_TABLE_VIS = addStr("member-payment-types-gui-table-vis");
    public StringProperty MEMBER_PAYMENT_TYPES_GUI_TABLE_ORDER = addStr("member-payment-types-gui-table-order");

    // GuiFee
    public StringProperty FEE_GUI_TABLE_WIDTH = addStrC("GuiFee", "fee-gui-table-width");
    public StringProperty FEE_GUI_TABLE_SORT = addStr("fee-gui-table-sort");
    public StringProperty FEE_GUI_TABLE_UPDOWN = addStr("fee-gui-table-upDown");
    public StringProperty FEE_GUI_TABLE_VIS = addStr("fee-gui-table-vis");
    public StringProperty FEE_GUI_TABLE_ORDER = addStr("fee-gui-table-order");

    // DialogAddMissingFee
    public StringProperty ADD_MISSING_FEE_GUI_TABLE_WIDTH = addStrC("DialogAddMissingFee", "add-missing-fee-gui-table-width");
    public StringProperty ADD_MISSING_FEE_GUI_TABLE_SORT = addStr("add-missing-fee-gui-table-sort");
    public StringProperty ADD_MISSING_FEE_GUI_TABLE_UPDOWN = addStr("add-missing-fee-gui-table-upDown");
    public StringProperty ADD_MISSING_FEE_GUI_TABLE_VIS = addStr("add-missing-fee-gui-table-vis");
    public StringProperty ADD_MISSING_FEE_GUI_TABLE_ORDER = addStr("add-missing-fee-gui-table-order");

    // DialogPayFee
    public StringProperty PAY_FEE_TABLE_WIDTH = addStrC("DialogPayFee", "pay-fee-table-width");
    public StringProperty PAY_FEE_TABLE_SORT = addStr("pay-fee-table-sort");
    public StringProperty PAY_FEE_TABLE_UPDOWN = addStr("pay-fee-table-upDown");
    public StringProperty PAY_FEE_TABLE_VIS = addStr("pay-fee-table-vis");
    public StringProperty PAY_FEE_TABLE_ORDER = addStr("pay-fee-table-order");
    public LongProperty PAY_FEE_ACCOUNT_ID = addLong("pay-fee-account-id", 0);
    public ObjectProperty<FinanceAccountData> PAY_FEE_ACCOUNT = new SimpleObjectProperty<>();
    public LongProperty PAY_FEE_CATEGORY_ID = addLong("pay-fee-category-id", 0);
    public ObjectProperty<FinanceCategoryData> PAY_FEE_CATEGORY = new SimpleObjectProperty<>();

    public ObservableList<String> PAY_FEE_SEPA_DIR_LIST = addListProp(arrayList, "pay-fee-sepa-dir-list");
    public ObservableList<String> PAY_FEE_SEPA_FILE_LIST = addListProp(arrayList, "pay-fee-sepa-file-list");
    public ObservableList<String> PAY_FEE_SEPA_BEGLEIT_FILE_LIST = addListProp(arrayList, "pay-fee-sepa-begleit-file-list");

    public StringProperty PAY_FEE_SEPA_DIR = addStr("pay-fee-sepa-dir");
    public StringProperty PAY_FEE_SEPA_FILE = addStr("pay-fee-sepa-file");
    public StringProperty PAY_FEE_SEPA_BEGLEIT_FILE = addStr("pay-fee-sepa-begleit-file");

    public StringProperty PAY_FEE_SEPA_DATE = addStr("pay-fee-sepa-date");

    // GuiFinance
    public StringProperty FINANCE_GUI_TABLE_WIDTH = addStrC("GuiFinance", "finance-gui-table-width");
    public StringProperty FINANCE_GUI_TABLE_SORT = addStr("finance-gui-table-sort");
    public StringProperty FINANCE_GUI_TABLE_UPDOWN = addStr("finance-gui-table-upDown");
    public StringProperty FINANCE_GUI_TABLE_VIS = addStr("finance-gui-table-vis");
    public StringProperty FINANCE_GUI_TABLE_ORDER = addStr("finance-gui-table-order");

    // GuiFinance-Account
    public StringProperty FINANCE_ACCOUNT_GUI_TABLE_WIDTH = addStrC("GuiFinance-Account", "finance-account-gui-table-width");
    public StringProperty FINANCE_ACCOUNT_GUI_TABLE_SORT = addStr("finance-account-gui-table-sort");
    public StringProperty FINANCE_ACCOUNT_GUI_TABLE_UPDOWN = addStr("finance-account-gui-table-upDown");
    public StringProperty FINANCE_ACCOUNT_GUI_TABLE_VIS = addStr("finance-account-gui-table-vis");
    public StringProperty FINANCE_ACCOUNT_GUI_TABLE_ORDER = addStr("finance-account-gui-table-order");

    // GuiFinance-Category
    public StringProperty FINANCE_CATEGORY_GUI_TABLE_WIDTH = addStrC("GuiFinance-Category", "finance-category-gui-table-width");
    public StringProperty FINANCE_CATEGORY_GUI_TABLE_SORT = addStr("finance-category-gui-table-sort");
    public StringProperty FINANCE_CATEGORY_GUI_TABLE_UPDOWN = addStr("finance-category-gui-table-upDown");
    public StringProperty FINANCE_CATEGORY_GUI_TABLE_VIS = addStr("finance-category-gui-table-vis");
    public StringProperty FINANCE_CATEGORY_GUI_TABLE_ORDER = addStr("finance-category-gui-table-order");

    // Transaction
    public StringProperty TRANSACTION_GUI_TABLE_WIDTH = addStrC("Transaction", "transaction-gui-table-width");
    public StringProperty TRANSACTION_GUI_TABLE_SORT = addStr("transaction-gui-table-sort");
    public StringProperty TRANSACTION_GUI_TABLE_UPDOWN = addStr("transaction-gui-table-upDown");
    public StringProperty TRANSACTION_GUI_TABLE_VIS = addStr("transaction-gui-table-vis");
    public StringProperty TRANSACTION_GUI_TABLE_ORDER = addStr("transaction-gui-table-order");

    // FinaceReport
    public StringProperty FINANCE_REPORT_GUI_TABLE_WIDTH = addStrC("FinanceReport", "finance-report-gui-table-width");
    public StringProperty FINANCE_REPORT_GUI_TABLE_SORT = addStr("finance-report-gui-table-sort");
    public StringProperty FINANCE_REPORT_GUI_TABLE_UPDOWN = addStr("finance-report-gui-table-upDown");
    public StringProperty FINANCE_REPORT_GUI_TABLE_VIS = addStr("finance-report-gui-table-vis");
    public StringProperty FINANCE_REPORT_GUI_TABLE_ORDER = addStr("finance-report-gui-table-order");

    // Calculation Category
    public StringProperty CALCULATION_CATEGORY_GUI_TABLE_WIDTH = addStrC("Calculation Category", "calculation-category-gui-table-width");
    public StringProperty CALCULATION_CATEGORY_GUI_TABLE_SORT = addStr("calculation-category-gui-table-sort");
    public StringProperty CALCULATION_CATEGORY_GUI_TABLE_UPDOWN = addStr("calculation-category-gui-table-upDown");
    public StringProperty CALCULATION_CATEGORY_GUI_TABLE_VIS = addStr("calculation-category-gui-table-vis");
    public StringProperty CALCULATION_CATEGORY_GUI_TABLE_ORDER = addStr("calculation-category-gui-table-order");

    // Calculation Account
    public StringProperty CALCULATION_ACCOUNT_GUI_TABLE_WIDTH = addStrC("Calculation Account", "calculation-account-gui-table-width");
    public StringProperty CALCULATION_ACCOUNT_GUI_TABLE_SORT = addStr("calculation-account-gui-table-sort");
    public StringProperty CALCULATION_ACCOUNT_GUI_TABLE_UPDOWN = addStr("calculation-account-gui-table-upDown");
    public StringProperty CALCULATION_ACCOUNT_GUI_TABLE_VIS = addStr("calculation-account-gui-table-vis");
    public StringProperty CALCULATION_ACCOUNT_GUI_TABLE_ORDER = addStr("calculation-account-gui-table-order");

    // Combobox-Listen
    public ObservableList<String> CBO_LIST_ANREDE = addListPropC("Combobox-Listen", arrayList, "cbo-list-anrede");
    public ObservableList<String> CBO_LIST_PLZ = addListProp(arrayList, "cbo-list-plz");
    public ObservableList<String> CBO_LIST_ORT = addListProp(arrayList, "cbo-list-ort");
    public ObservableList<String> CBO_LIST_LAND = addListProp(arrayList, "cbo-list-land");

    // Serienbrief
    public ObservableList<String> CBO_LIST_NEWSLETTER_TEMPLATE = addListPropC("Serienbrief", arrayList, "cbo-list-newsletter-template");
    public ObservableList<String> CBO_LIST_NEWSLETTER_DIR = addListProp(arrayList, "cbo-list-newsletter-dir");
    public ObservableList<String> CBO_LIST_NEWSLETTER_FILE = addListProp(arrayList, "cbo-list-newsletter-file");
    public StringProperty NEWSLETTER_TEMPLATE = addStr("newsletter-template");
    public StringProperty NEWSLETTER_DIR = addStr("newsletter-dir");
    public StringProperty NEWSLETTER_FILE = addStr("newsletter-file");

    // Finanzreport
    public ObservableList<String> CBO_LIST_DIR_FINANZREPORT = addListProp(arrayList, "cbo-list-finanzreport");
    public StringProperty EXPORT_DIR_FINANZREPORT = addStr("export-dir-finanzreport");
    public ObservableList<String> CBO_LIST_EXPORT_FILE_FINANZREPORT = addListProp(arrayList, "cbo-list-export-file-finanzreport");
    public StringProperty EXPORT_FILE_FINANZREPORT = addStr("export-file-finanzreport");
    public BooleanProperty EXPORT_TRANSACTION_SHORT = addBool("export-transaction-short", false);

    // CSV
    public ObservableList<String> CBO_LIST_EXPORT_CSV_DIR = addListProp(arrayList, "cbo-list-export-csv-dir");
    public StringProperty EXPORT_CSV_DIR = addStr("export-csv-dir");
    public ObservableList<String> CBO_LIST_EXPORT_CSV_FILE = addListProp(arrayList, "cbo-list-export-csv-file");
    public StringProperty EXPORT_CSV_FILE = addStr("export-csv-file");
    public ObservableList<String> CBO_LIST_IMPORT_CSV_FILE = addListProp(arrayList, "cbo-list-import-csv-file");
    public StringProperty IMPORT_CSV_FILE = addStr("import-csv-file");
    // Infos
    public ObservableList<String> CBO_LIST_EXPORT_INFOS_DIR = addListProp(arrayList, "cbo-list-export-infos-dir");
    public StringProperty EXPORT_INFOS_DIR = addStr("export-infos-dir");
    public ObservableList<String> CBO_LIST_EXPORT_INFOS_FILE = addListProp(arrayList, "cbo-list-export-infos-file");
    public StringProperty EXPORT_INFOS_FILE = addStr("export-infos-file");
    // ZIP
    public ObservableList<String> CBO_LIST_EXPORT_ZIP_DIR = addListProp(arrayList, "cbo-list-export-zip-dir");
    public StringProperty EXPORT_ZIP_DIR = addStr("export-zip-dir");
    public ObservableList<String> CBO_LIST_EXPORT_ZIP_FILE = addListProp(arrayList, "cbo-list-export-zip-file");
    public StringProperty EXPORT_ZIP_FILE = addStr("export-zip-file");

    // Serienbrief Dialog Rechnung
    public StringProperty NEWSLETTER_TEMPLATE_BILL = addStrC("Serienbrief Dialog Rechnung", "newsletter-template-bill");
    public StringProperty NEWSLETTER_DIR_BILL = addStr("newsletter-dir-bill");
    public StringProperty NEWSLETTER_NAME_BILL = addStr("newsletter-name-bill");

    // Serienbrief Dialog SQ
    public StringProperty NEWSLETTER_TEMPLATE_SQ = addStrC("Serienbrief Dialog SQ", "newsletter-template-sq");
    public StringProperty NEWSLETTER_DIR_SQ = addStr("newsletter-dir-sq");
    public StringProperty NEWSLETTER_NAME_SQ = addStr("newsletter-name-sq");

    // Export Club
    public StringProperty EXPORT_CLUB_DIR = addStr("export-club-dir");

    // DemoData
    public BooleanProperty DEMO_DATA_REMOVE_OLD_DATA = addBoolC("DemoData", "demo-data-remove-old-data", false);
    public BooleanProperty DEMO_DATA_CLUB = addBool("demo-data-club", true);
    public BooleanProperty DEMO_DATA_MEMBER = addBool("demo-data-member", true);
    public BooleanProperty DEMO_DATA_FINANCES = addBool("demo-data-finances", true);
    public IntegerProperty DEMO_DATA_ADD_MEMBER = addInt("demo-data-add-member", 100);
    public IntegerProperty DEMO_DATA_ADD_FEE = addInt("demo-data-add-fee", DemoConst.ADD_AMOUNT_FEE.FEE_ADD_SOME.getNo());
    public IntegerProperty DEMO_DATA_ADD_FEE_PAY = addInt("demo-data-add-fee-pay", DemoConst.ADD_AMOUNT_FEE.FEE_ADD_SOME.getNo());
    public IntegerProperty DEMO_DATA_ADD_FINANCE = addInt("demo-data-add-finance", 150);

    public StringProperty DEMO_DATA_ADD_CLUB_NAME = addStr("demo-data-add-club-name", DemoClubDataFactory.getName());
    public StringProperty DEMO_DATA_ADD_CLUB_ORT = addStr("demo-data-add-club-ort", DemoClubDataFactory.getOrt());
    public StringProperty DEMO_DATA_ADD_CLUB_PLZ = addStr("demo-data-add-club-plz", DemoClubDataFactory.getPlz());
    public StringProperty DEMO_DATA_ADD_CLUB_STRASSE = addStr("demo-data-add-club-strasse", DemoClubDataFactory.getStrasse());
    public StringProperty DEMO_DATA_ADD_CLUB_TELEFON = addStr("demo-data-add-club-telefon", DemoClubDataFactory.getTelefon());
    public StringProperty DEMO_DATA_ADD_CLUB_EMAIL = addStr("demo-data-add-club-email", DemoClubDataFactory.getEmail());
    public StringProperty DEMO_DATA_ADD_CLUB_WEBSITE = addStr("demo-data-add-club-website", DemoClubDataFactory.getWebsite());

    public StringProperty DEMO_DATA_ADD_CLUB_KONTO_NR = addStr("demo-data-add-club-konto-nr", DemoClubDataFactory.getKontoNr());
    public StringProperty DEMO_DATA_ADD_CLUB_BIC = addStr("demo-data-add-club-bic", DemoClubDataFactory.getBic());
    public StringProperty DEMO_DATA_ADD_CLUB_IBAN = addStr("demo-data-add-club-iban", DemoClubDataFactory.getIban());
    public StringProperty DEMO_DATA_ADD_CLUB_BANK = addStr("demo-data-add-club-bank", DemoClubDataFactory.getBank());
    public StringProperty DEMO_DATA_ADD_CLUB_GLAEUBIGER_ID = addStr("demo-data-add-club-glaeubiger-id", DemoClubDataFactory.getGlaeubigerId());

    // export member data
    public final BooleanProperty MEMBER_EXPORT_ALL = addBool("member-export-all", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Nr = addBool("member-export-data-nr", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Nachname = addBool("member-export-data-nachname", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Vorname = addBool("member-export-data-vorname", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Anrede = addBool("member-export-data-anrede", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Email = addBool("member-export-data-email", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Telefon = addBool("member-export-data-telefon", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Strasse = addBool("member-export-data-strasse", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_PLZ = addBool("member-export-data-plz", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Ort = addBool("member-export-data-ort", true);
    public final BooleanProperty MEMBER_EXPORT_DATA_Land = addBool("member-export-data-land", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Status = addBool("member-export-data-status", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Beitrag = addBool("member-export-data-beitrag", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Beitragssatz = addBool("member-export-data-beitragssatz", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Bank = addBool("member-export-data-bank", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Iban = addBool("member-export-data-iban", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Bic = addBool("member-export-data-bic", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Kontoinhaber = addBool("member-export-data-kontoinhaber", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Zahlart = addBool("member-export-data-zahlart", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Zahlungsbeginn = addBool("member-export-data-zahlungsbeginn", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Sepabeginn = addBool("member-export-data-sepabeginn", false);
    public final BooleanProperty MEMBER_EXPORT_DATA_Beitritt = addBool("member-export-data-beitritt", false);

    // export member data
    public final BooleanProperty FEE_EXPORT_ALL = addBool("fee-export-all", false);
    public final BooleanProperty FEE_EXPORT_DATA_Nr = addBool("fee-export-data-nr", true);
    public final BooleanProperty FEE_EXPORT_DATA_MemberNr = addBool("fee-export-data-memberNr", true);
    public final BooleanProperty FEE_EXPORT_DATA_MemberName = addBool("fee-export-data-memberName", true);
    public final BooleanProperty FEE_EXPORT_DATA_Betrag = addBool("fee-export-data-betrag", true);
    public final BooleanProperty FEE_EXPORT_DATA_BetragInWords = addBool("fee-export-data-betrag-in-words", true);
    public final BooleanProperty FEE_EXPORT_DATA_Jahr = addBool("fee-export-data-jahr", true);
    public final BooleanProperty FEE_EXPORT_DATA_Zahlart = addBool("fee-export-data-zahlart", true);
    public final BooleanProperty FEE_EXPORT_DATA_Bezahlt = addBool("fee-export-data-bezahlt", true);
    public final BooleanProperty FEE_EXPORT_DATA_Rechnung = addBool("fee-export-data-rechnung", true);
    public final BooleanProperty FEE_EXPORT_DATA_SpendenQ = addBool("fee-export-data-spendenQ", true);
    public final BooleanProperty FEE_EXPORT_DATA_Erstelldatum = addBool("fee-export-data-Erstelldatum", true);
    public final BooleanProperty FEE_EXPORT_DATA_Text = addBool("fee-export-data-text", true);

    //expport finance data
    public final BooleanProperty FINANCE_EXPORT_ALL = addBool("finance-export-all", false);
    public final BooleanProperty FINANCE_EXPORT_DATA_Nr = addBool("finance-export-data-nr", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_BelegNr = addBool("finance-export-data-belegNr", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Gesamtbetrag = addBool("finance-export-data-gesamtbetrag", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Konto = addBool("finance-export-data-konto", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Kategorie = addBool("finance-export-data-kategorie", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Geschäftsjahr = addBool("finance-export-data-geschäftsjahr", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Buchungsdatum = addBool("finance-export-data-buchungsdatum", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Erstelldatum = addBool("finance-export-data-erstelldatum", true);
    public final BooleanProperty FINANCE_EXPORT_DATA_Text = addBool("finance-export-data-text", true);


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
        super.init(arrayList, "ClubConfig");

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

    private StringProperty addStr(String key) {
        return addStrProp(arrayList, key);
    }

    private StringProperty addStrC(String comment, String key) {
        return addStrPropC(comment, arrayList, key);
    }

    private StringProperty addStr(String key, String init) {
        return addStrProp(arrayList, key, init);
    }

    private StringProperty addStrC(String comment, String key, String init) {
        return addStrPropC(comment, arrayList, key, init);
    }

    private DoubleProperty addDouble(String key, double init) {
        return addDoubleProp(arrayList, key, init);
    }

    private DoubleProperty addDoubleC(String comment, String key, double init) {
        return addDoublePropC(comment, arrayList, key, init);
    }

    private IntegerProperty addInt(String key, int init) {
        return addIntProp(arrayList, key, init);
    }

    private IntegerProperty addIntC(String comment, String key, int init) {
        return addIntPropC(comment, arrayList, key, init);
    }

    private LongProperty addLong(String key, long init) {
        return addLongProp(arrayList, key, init);
    }

    private LongProperty addLongC(String comment, String key, long init) {
        return addLongPropC(comment, arrayList, key, init);
    }

    private BooleanProperty addBool(String key, boolean init) {
        return addBoolProp(arrayList, key, init);
    }

    private BooleanProperty addBoolC(String comment, String key, boolean init) {
        return addBoolPropC(comment, arrayList, key, init);
    }
}
