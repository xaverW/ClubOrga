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


package de.p2tools.cluborga.controller.newsletter.document.pdfFile;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.controller.ClubFactory;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.tools.log.PLog;

import java.util.List;

public class CreateClubInfos {

    PdfDocument pdfDocument = null;
    private Document document = null;
    private boolean newPage = false;

    private String destFile;

    private final ClubConfig clubConfig;

    public CreateClubInfos(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
    }

    public boolean createPdf(String destPath, String destFileName, List<String> lineList) {
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

    private boolean openDocument() {
        try {
            PdfWriter writer = new PdfWriter(destFile);
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

    private boolean writeFile(List<String> lineList) {
        PdfPage pdfPage = pdfDocument.addNewPage();

        try {
            document.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN));
            PdfFactory.foldMark(pdfPage);
            for (String line : lineList) {
                document.add(new Paragraph(line));
            }
        } catch (Exception ex) {
            PLog.errorLog(333201459, ex);
            return false;
        }

        return true;
    }
}
