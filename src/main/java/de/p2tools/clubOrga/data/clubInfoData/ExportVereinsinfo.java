/*
 *    Copyright (C) 2007, 2008, 2009
 *
 *    Emma, Vereinsverwaltungsprorgamm
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.p2tools.clubOrga.data.clubInfoData;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.controller.ClubFactory;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.util.converter.NumberStringConverter;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.itextpdf.io.font.constants.StandardFontFamilies.COURIER;
import static com.itextpdf.styledxmlparser.css.CommonCssConstants.BOLD;

public class ExportVereinsinfo {

    PdfDocument pdfDocument = null;
    private Document document = null;
    private boolean newPage = false;
    private String destFile;
    final int spalten = 5;
    final Table table = new Table(spalten);
    private final ClubConfig clubConfig;

    private static final DecimalFormat DF;

    static {
        DF = new DecimalFormat("###,##0.00");
    }

    public ExportVereinsinfo(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
    }


    public boolean createPdf(String destPath, String destFileName, List<String> lineList) {

//        try {
//            this.destFile = ClubFactory.getDestinationPath(clubConfig.getStage(), destPath, destFileName);
//            PdfWriter writer = new PdfWriter(new FileOutputStream(destFile));
//            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
//            PdfDocument pdf = new PdfDocument(writer);
//            Document document = new Document(pdf);
//            Text text = new Text("Hello World with font and color")
//                    .setFont(font)
//                    .setFontColor(ColorConstants.BLUE);
//            document.add(new Paragraph(text));
//            document.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        this.destFile = ClubFactory.getDestinationPath(clubConfig.getStage(), destPath, destFileName);
        if (this.destFile.isEmpty()) {
            return false;
        }

        if (!openDocument()) {
            return false;
        }

        if (!writeFile(lineList)) {
            return false;
        }
        if (!closeDocument()) {
            return false;
        }

        return true;
    }

    private boolean writeFile(List<String> lineList) {
        try {
            boolean ret = false;
            final int actYear = LocalDate.now().getYear();
            int faktor = 3; //faktor 3 = 75 dpi / 2,54 Zoll *10mm
            com.itextpdf.kernel.colors.Color farbe0 = new DeviceRgb(255, 106, 0);
            com.itextpdf.kernel.colors.Color farbe1 = new DeviceRgb(255, 174, 0);
            com.itextpdf.kernel.colors.Color farbe2 = new DeviceRgb(255, 227, 167);

            //            Font f1 = new Font(Font.SANS_SERIF, 14, Font.BOLD);
//            Font f2 = new Font(Font.SANS_SERIF, 12, Font.PLAIN);
//            Font f3 = new Font(Font.SANS_SERIF, 10, Font.PLAIN);
            PdfFont pdfFont1 = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            PdfFont pdfFont2 = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            PdfFont pdfFont3 = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

            Style style1 = new Style();
            style1.setFont(pdfFont1).setFontSize(14);
            Style style2 = new Style();
            style2.setFont(pdfFont2).setFontSize(11);
            Style style3 = new Style();
            style3.setFont(pdfFont3).setFontSize(8);


            //----------------------------------
            //----------------------------------
            addText(table, "Vereinsinfo", spalten - 1, style1, farbe0, false);
            addText(table, PDateFactory.getTodayStr(), 1, style2, farbe0, true);
            //----------------------------------
            addText(table, clubConfig.clubData.getName(), spalten, style2, ColorConstants.WHITE, false);
            addText(table, clubConfig.clubData.getStrasse() + ", " +
                    clubConfig.clubData.getPlz() + " " +
                    clubConfig.clubData.getOrt(), spalten, style3, ColorConstants.WHITE, false);

            //----------------------------------
            //----------------------------------
            addText(table, "Mitglieder", spalten, style2, farbe1, false);
            addLeer(table, 1);
            addText(table, "Anzahl alle Mitglider", 3, style3, ColorConstants.WHITE, false);
            addText(table, clubConfig.memberDataList.size() + "", 1, style3, ColorConstants.WHITE, true);

            //----------------------------------
            addLeer(table, 1);
            addText(table, "Anzahl nach Status", spalten - 1, style3, ColorConstants.WHITE, false);
            clubConfig.stateDataList.stream().forEach(stateData -> {
                addLeer(table, 2);
                addText(table, stateData.getName(),
                        2, style3, ColorConstants.WHITE, false);
                addText(table, clubConfig.memberDataList.stream()
                                .filter(memberData -> memberData.getStateData().equals(stateData)).count() + "",
                        1, style3, ColorConstants.WHITE, true);
            });


            //----------------------------------
            addLeer(table, 1);
            addText(table, "Anzahl nach Beitragssatz",
                    spalten - 1, style3, ColorConstants.WHITE, false);
            clubConfig.feeRateDataList.stream().forEach(feeRateData -> {
                addLeer(table, 2);
                addText(table, feeRateData.getName(),
                        2, style3, ColorConstants.WHITE, false);
                addText(table, clubConfig.memberDataList.stream()
                                .filter(memberData -> memberData.getFeeRateData().equals(feeRateData)).count() + "",
                        1, style3, ColorConstants.WHITE, true);
            });


            //----------------------------------
            addLeer(table, 1);
            addText(table, "Anzahl nach Zahlarten",
                    spalten - 1, style3, ColorConstants.WHITE, false);
            clubConfig.paymentTypeDataList.stream().forEach(paymentTypeData -> {
                addLeer(table, 2);
                addText(table, paymentTypeData.getName(),
                        2, style3, ColorConstants.WHITE, false);
                addText(table, clubConfig.memberDataList.stream()
                                .filter(memberData -> memberData.getPaymentTypeData().equals(paymentTypeData)).count() + "",
                        1, style3, ColorConstants.WHITE, true);
            });


            //----------------------------------
            //----------------------------------
            addText(table, "Beiträge", spalten, style2, farbe1, false);

            //----------------------------------
            addLeer(table, 1);
            addText(table, "Summe Beiträge im aktuellen Jahr",
                    3, style3, ColorConstants.WHITE, false);
            addText(table,
                    new NumberStringConverter(DF).toString(0.01 * clubConfig.feeDataList.stream()
                            .filter(data -> data.getJahr() == LocalDate.now().getYear())
                            .collect(Collectors.summingLong(FeeData::getBetrag)))
                    , 1, style3, ColorConstants.WHITE, true);

            //----------------------------------
            addLeer(table, 1);
            addText(table, "Anzahl nicht bezahlter Beiträge",
                    3, style3, ColorConstants.WHITE, false);
            addText(table, clubConfig.feeDataList.stream()
                            .filter(data -> data.getBezahlt().isEqual(LocalDate.MIN)).count() + "",
                    1, style3, ColorConstants.WHITE, true);

            //----------------------------------
            addLeer(table, 1);
            addText(table, "Summe nicht bezahlter Beiträge im aktuellen Jahr",
                    3, style3, ColorConstants.WHITE, false);
            addText(table,
                    new NumberStringConverter(DF).toString(0.01 * clubConfig.feeDataList.stream()
                            .filter(data -> data.getJahr() == LocalDate.now().getYear())
                            .filter(data -> data.getBezahlt().isEqual(LocalDate.MIN))
                            .collect(Collectors.summingLong(FeeData::getBetrag)))
                    , 1, style3, ColorConstants.WHITE, true);

            //----------------------------------
            //----------------------------------
            addText(table, "Finanzen", spalten, style2, farbe1, false);
            addText(table, "Saldo über den gesamten Zeitraum", spalten - 1, style3, farbe2, false);
            addText(table, new NumberStringConverter(DF).toString(0.01 * clubConfig.financeDataList.getSaldo()),
                    1, style3, farbe2, true);

            //----------------------------------
            addLeer(table, 1);
            addText(table, "Summe nach Konten", spalten - 1, style3, ColorConstants.WHITE, false);
            TreeMap<String, Long> treeMapAccount = generateFinanceInfoAccount();
            treeMapAccount.entrySet().stream().forEach(tree -> {
                addLeer(table, 2);
                addText(table, tree.getKey()
                        , 2, style3, ColorConstants.WHITE, false);
                addText(table, new NumberStringConverter(DF).toString(0.01 * tree.getValue())
                        , 1, style3, ColorConstants.WHITE, true);
            });

            //----------------------------------
            addLeer(table, 1);
            addText(table, "Summe nach Kategorien", spalten - 1, style3, ColorConstants.WHITE, false);
            TreeMap<String, Long> treeMapCategory = generateFinanceInfoCategory();
            treeMapCategory.entrySet().stream().forEach(tree -> {
                addLeer(table, 2);
                addText(table, tree.getKey()
                        , 2, style3, ColorConstants.WHITE, false);
                addText(table, new NumberStringConverter(DF).toString(0.01 * tree.getValue())
                        , 1, style3, ColorConstants.WHITE, true);
            });

            //----------------------------------
            //----------------------------------
            addText(table, "Saldo im Jahr " + actYear, spalten - 1, style3, farbe2, false);
            addText(table, new NumberStringConverter(DF).toString(0.01 * clubConfig.financeDataList.getSaldo(actYear))
                    , 1, style3, farbe2, true);

            //----------------------------------
            addLeer(table, 1);
            addText(table, "Summe nach Konten " + actYear, spalten - 1, style3, ColorConstants.WHITE, false);
            TreeMap<String, Long> treeMapAccountAct = generateFinanceInfoActYearAccount();
            treeMapAccountAct.entrySet().stream().forEach(tree -> {
                addLeer(table, 2);
                addText(table, tree.getKey()
                        , 2, style3, ColorConstants.WHITE, false);
                addText(table, new NumberStringConverter(DF).toString(0.01 * tree.getValue())
                        , 1, style3, ColorConstants.WHITE, true);
            });

            //----------------------------------
            addLeer(table, 1);
            addText(table, "Summe nach Kategorien " + actYear, spalten - 1, style3, ColorConstants.WHITE, false);
            TreeMap<String, Long> treeMapCategoryAct = generateFinanceInfoActYearCategory();
            treeMapCategoryAct.entrySet().stream().forEach(tree -> {
                addLeer(table, 2);
                addText(table, tree.getKey()
                        , 2, style3, ColorConstants.WHITE, false);
                addText(table, new NumberStringConverter(DF).toString(0.01 * tree.getValue())
                        , 1, style3, ColorConstants.WHITE, true);
            });


            document.add(table);
        } catch (Exception ex) {
            PLog.errorLog(945120147, "create document");
            return false;
        }
        return true;
    }

    private boolean openDocument() {
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(destFile));
            pdfDocument = new PdfDocument(writer);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            document = new Document(pdfDocument);

        } catch (Exception ex) {
            PLog.errorLog(332587408, ex);
            PAlert.showErrorAlert(clubConfig.getStage(),
                    "Datei schreiben!",
                    "Die Zieldatei kann nicht geschrieben werden.");
            return false;
        }

        return true;
    }

    private boolean closeDocument() {
        if (document == null) {
            return false;
        }

        try {
            document.close();
        } catch (Exception ex) {
            PLog.errorLog(777412589, ex);
            return false;
        }

        return true;
    }

    private void addLeer(Table t, int anz) {
        Cell cell = new Cell(1, anz);
        cell.add(new Paragraph("").setFontSize(10));
        cell.setBackgroundColor(ColorConstants.WHITE);
        t.addCell(cell);
    }

    private void test(Table t, int anz) {
        try {
            Text title1 = new Text("The Strange Case of ").setFontColor(ColorConstants.BLUE);
            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont bold = PdfFontFactory.createFont(BOLD);
            Style code = new Style();
            PdfFont monospace = PdfFontFactory.createFont(COURIER);
            code.setFont(monospace).setFontColor(ColorConstants.BLUE)
                    .setBackgroundColor(ColorConstants.BLUE);
            Paragraph p = new Paragraph();
            p.add(new Text("Dr. Jekyll").addStyle(code));


            Text text = new Text("")
                    .setFontColor(ColorConstants.BLUE)
                    .setFont(font);
            Cell cell = new Cell(1, anz);
            cell.add(new Paragraph(""));
            cell.setBackgroundColor(ColorConstants.WHITE);
            t.addCell(cell);

        } catch (Exception ex) {
        }
    }

    private void addText(Table table, String txt, int colSpan,
                         Style style, com.itextpdf.kernel.colors.Color cellColor, boolean right) {

        Text text = new Text(txt).addStyle(style).setFontColor(ColorConstants.BLACK);
        Cell cell = new Cell(1, colSpan);
        cell.add(new Paragraph(text)).setBackgroundColor(cellColor);
        if (right) {
            cell.setTextAlignment(TextAlignment.RIGHT);
        }
        table.addCell(cell);
    }

    private TreeMap<String, Long> generateFinanceInfoAccount() {
        TreeMap<String, Long> treeMapAccount = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        clubConfig.financeDataList.stream()
                .forEach(financeData -> {
                    if (treeMapAccount.containsKey(financeData.getFinanceAccountData().getName())) {
                        long old = treeMapAccount.get(financeData.getFinanceAccountData().getName());
                        treeMapAccount.put(financeData.getFinanceAccountData().getName(),
                                old + financeData.financeDataGetSumBetrag());
                    } else {
                        treeMapAccount.put(financeData.getFinanceAccountData().getName(),
                                financeData.financeDataGetSumBetrag());
                    }
                });

        return treeMapAccount;
    }

    private TreeMap<String, Long> generateFinanceInfoCategory() {
        TreeMap<String, Long> treeMapCategory = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        clubConfig.financeDataList.stream().forEach(financeData -> financeData.getTransactionDataList()
                .stream()
                .forEach(transactionData -> {
                    if (treeMapCategory.containsKey(transactionData.getFinanceCategoryData().getCategory())) {
                        long old = treeMapCategory.get(transactionData.getFinanceCategoryData().getCategory());
                        treeMapCategory.put(transactionData.getFinanceCategoryData().getCategory(), old + transactionData.getBetrag());
                    } else {
                        treeMapCategory.put(transactionData.getFinanceCategoryData().getCategory(), transactionData.getBetrag());
                    }
                })
        );
        return treeMapCategory;
    }

    private TreeMap<String, Long> generateFinanceInfoActYearCategory() {
        final int actYear = LocalDate.now().getYear();
        TreeMap<String, Long> treeMapCategory = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        clubConfig.financeDataList.stream()
                .filter(financeData -> financeData.getGeschaeftsJahr() == actYear)
                .forEach(financeData -> financeData.getTransactionDataList()
                        .stream()
                        .forEach(transactionData -> {
                            if (treeMapCategory.containsKey(transactionData.getFinanceCategoryData().getCategory())) {
                                long old = treeMapCategory.get(transactionData.getFinanceCategoryData().getCategory());
                                treeMapCategory.put(transactionData.getFinanceCategoryData().getCategory(), old + transactionData.getBetrag());
                            } else {
                                treeMapCategory.put(transactionData.getFinanceCategoryData().getCategory(), transactionData.getBetrag());
                            }
                        })
                );

        return treeMapCategory;
    }

    private TreeMap<String, Long> generateFinanceInfoActYearAccount() {
        final int actYear = LocalDate.now().getYear();
        TreeMap<String, Long> treeMapAccount = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        clubConfig.financeDataList.stream()
                .filter(financeData -> financeData.getGeschaeftsJahr() == actYear)
                .forEach(financeData -> {
                    if (treeMapAccount.containsKey(financeData.getFinanceAccountData().getName())) {
                        long old = treeMapAccount.get(financeData.getFinanceAccountData().getName());
                        treeMapAccount.put(financeData.getFinanceAccountData().getName(),
                                old + financeData.financeDataGetSumBetrag());
                    } else {
                        treeMapAccount.put(financeData.getFinanceAccountData().getName(),
                                financeData.financeDataGetSumBetrag());
                    }
                });

        return treeMapAccount;
    }
}
