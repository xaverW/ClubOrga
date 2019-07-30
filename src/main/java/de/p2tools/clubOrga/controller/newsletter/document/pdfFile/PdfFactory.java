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


package de.p2tools.clubOrga.controller.newsletter.document.pdfFile;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2Lib.tools.log.PLog;

import java.util.List;

public class PdfFactory {

    private PdfFactory() {
    }

    public static void foldMark(PdfPage pdfPage) {
        try {
            Rectangle rect = new Rectangle(20, 565, 10, 1);
            PdfCanvas canvas = new PdfCanvas(pdfPage);
            canvas.setStrokeColor(ColorConstants.BLACK)
                    .setLineWidth(0.5f)
                    .rectangle(rect)
                    .stroke();
        } catch (Exception ex) {
            PLog.errorLog(951202014, ex, "foldMark");
        }
    }

    public static void addressBlock(PdfPage pdfPage, Document document, List<String> addressList) {
        if (addressList == null || addressList.isEmpty()) {
            throw new PException("addresslist is null or empty");
        }

        try {
            Table table = new Table(1, true);

            // Absender
            Paragraph p1 = new Paragraph(addressList.get(0)).setFontSize(8);
            Cell cell1 = new Cell()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(new DeviceRgb(245, 245, 245))
                    .add(p1);
            table.addCell(cell1);

            // Empfänger
            String txt = "";
            for (int i = 1; i < addressList.size(); ++i) {
                txt += addressList.get(i);
                if (i < addressList.size() - 1) {
                    txt += "\n";
                }
            }
            Paragraph p2 = new Paragraph(txt).setFontSize(12);
            Cell cell2 = new Cell()
                    .add(p2)
                    .setTextAlignment(TextAlignment.LEFT);
            table.addCell(cell2);


            Rectangle maxRect = new Rectangle(48, 610, 240, 120);
            PdfCanvas canvas = new PdfCanvas(pdfPage);
            new Canvas(canvas, pdfPage.getDocument(), maxRect).add(table);
            canvas.rectangle(maxRect);
            canvas.stroke();

        } catch (Exception ex) {
            PLog.errorLog(201010889, ex, "addressBlock");
        }

    }

}
