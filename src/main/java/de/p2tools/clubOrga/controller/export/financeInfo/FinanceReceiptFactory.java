/*
 * P2tools Copyright (C) 2020 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.clubOrga.controller.export.financeInfo;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import de.p2tools.clubOrga.data.financeData.FinanceData;
import de.p2tools.clubOrga.data.financeData.FinanceFieldNames;
import de.p2tools.clubOrga.data.financeData.TransactionData;
import de.p2tools.p2Lib.tools.log.PLog;

import java.text.DecimalFormat;
import java.util.List;

public class FinanceReceiptFactory {
    private static final DecimalFormat DF;

    static {
        DF = new DecimalFormat("###,##0.00");
    }

    private void FinanceReceipt() {
    }

    public static boolean writeBeleg(String destFile, List<FinanceData> liste, boolean transactionShort) {
        final int GREY = 230;
        try {
            PdfWriter writer = new PdfWriter(destFile);

            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            boolean second = false;
            for (FinanceData data : liste) {
                if (second) {
                    if (!transactionShort) {
                        // nur eine schmale Leerzeile
                        document.add(new Paragraph(" "));
                        document.add(new Paragraph(" "));
                    }
                    document.add(new Paragraph(" "));
                }
                second = true;

                Table table = new Table(4, true);
                PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

                Paragraph paragraph = new Paragraph(FinanceFieldNames.RECEIPT_NO + ": " + data.getReceiptNo());
                paragraph.setFont(bold);
                Cell cell = new Cell(1, 4).add(paragraph);
                cell.setBackgroundColor(new DeviceRgb(250, 104, 0));
                table.addCell(cell);

                cell = new Cell(1, 2).add(new Paragraph(FinanceFieldNames.GESAMTBETRAG + ": " + DF.format(1.0 * data.getGesamtbetrag() / 100)));
                cell.setBackgroundColor(new DeviceRgb(GREY, GREY, GREY));
                table.addCell(cell);

                if (data.getFinanceAccountData() != null) {
                    cell = new Cell(1, 2).add(new Paragraph(FinanceFieldNames.KONTO + ": " + data.getFinanceAccountData().getName()));
                    cell.setBackgroundColor(new DeviceRgb(GREY, GREY, GREY));
                    table.addCell(cell);
                } else {
                    cell = new Cell(1, 2).add(new Paragraph(FinanceFieldNames.KONTO));
                    cell.setBackgroundColor(new DeviceRgb(GREY, GREY, GREY));
                    table.addCell(cell);
                    table.addCell(new Cell().setBackgroundColor(new DeviceRgb(GREY, GREY, GREY)));
                }

                cell = new Cell(1, 2).add(new Paragraph(FinanceFieldNames.GESCHAEFTSJAHR + ": " + data.getGeschaeftsJahr()));
                table.addCell(cell);

                cell = new Cell(1, 2).add(new Paragraph(FinanceFieldNames.BUCHUNGS_DATUM + ": " + data.getBuchungsDatum().toString()));
                table.addCell(cell);

                if (!data.getText().isEmpty()) {
                    cell = new Cell(1, 4).add(new Paragraph(FinanceFieldNames.TEXT + ": " + data.getText()));
                    table.addCell(cell);
                }

                if (!transactionShort) {
                    cell = new Cell(1, 4);
                    table.addCell(cell);
                }
                cell = new Cell(1, 4).add(new Paragraph("Transaktionen").setFont(bold));
                cell.setBackgroundColor(new DeviceRgb(255, 227, 167));
                table.addCell(cell);

                boolean secondTr = false;
                for (TransactionData transactionData : data.getTransactionDataList()) {
                    if (transactionShort) {
                        Paragraph paragraphTr = new Paragraph(FinanceFieldNames.TRANSACTION_NO + ": " + transactionData.getNo());
                        paragraphTr.setFont(bold);
                        cell = new Cell().add(paragraphTr);
                        table.addCell(cell);

                        cell = new Cell().add(new Paragraph(FinanceFieldNames.BETRAG + ": " + DF.format(1.0 * transactionData.getBetrag() / 100)));
                        table.addCell(cell);

                        if (transactionData.getFinanceCategoryData() != null) {
                            cell = new Cell().add(new Paragraph(FinanceFieldNames.CATEGORY + ": " + transactionData.getFinanceCategoryData().getCategory()));
                            table.addCell(cell);
                        } else {
                            cell = new Cell().add(new Paragraph(FinanceFieldNames.CATEGORY));
                            table.addCell(cell);
                        }

                        if (transactionData.getFeeData() != null) {
                            cell = new Cell().add(new Paragraph("BeitragsNr: " + transactionData.getFeeData().getNo()));
                            table.addCell(cell);
                        } else {
                            table.addCell(new Cell());
                        }

                    } else {
                        if (secondTr) {
                            cell = new Cell(1, 4);
                            table.addCell(cell);
                        }
                        secondTr = true;

                        Paragraph paragraphTr = new Paragraph(FinanceFieldNames.TRANSACTION_NO + ": " + transactionData.getNo());
                        paragraphTr.setFont(bold);
                        cell = new Cell(1, 4).add(paragraphTr);
                        cell.setBackgroundColor(new DeviceRgb(242, 242, 242));
                        table.addCell(cell);

                        cell = new Cell(1, 2).add(new Paragraph(FinanceFieldNames.BETRAG + ": " + DF.format(1.0 * transactionData.getBetrag() / 100)));
                        table.addCell(cell);

                        if (transactionData.getFinanceCategoryData() != null) {
                            cell = new Cell(1, 2).add(new Paragraph(FinanceFieldNames.CATEGORY + ": " + transactionData.getFinanceCategoryData().getCategory()));
                            table.addCell(cell);
                        } else {
                            cell = new Cell().add(new Paragraph(FinanceFieldNames.CATEGORY));
                            table.addCell(cell);
                            table.addCell(new Cell());
                        }

                        if (transactionData.getFeeData() != null) {
                            table.addCell(new Cell(1, 2));
                            cell = new Cell(1, 2).add(new Paragraph("BeitragsNr: " + transactionData.getFeeData().getNo()));
                            table.addCell(cell);
                        }

                        if (!transactionData.getText().isEmpty()) {
                            cell = new Cell(1, 4).add(new Paragraph(FinanceFieldNames.TEXT + ": " + transactionData.getText()));
                            table.addCell(cell);
                        }
                    }
                }
                document.add(table);
                table.complete();
            }
            document.close();
        } catch (Exception ex) {
            PLog.errorLog(515124789, ex);
            return false;
        }
        return true;
    }
}
