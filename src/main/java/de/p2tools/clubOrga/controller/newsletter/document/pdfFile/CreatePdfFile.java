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


package de.p2tools.clubOrga.controller.newsletter.document.pdfFile;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.controller.ClubFactory;
import de.p2tools.clubOrga.controller.newsletter.NewsletterFactory;
import de.p2tools.clubOrga.controller.newsletter.ReplaceData;
import de.p2tools.clubOrga.controller.newsletter.ReplaceFactory;
import de.p2tools.clubOrga.controller.newsletter.document.CreateDocumentFactory;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.log.PLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreatePdfFile {

    PdfDocument pdfDocument = null;
    private Document document = null;
    private boolean newPage = false;
    private List<String> addressList = new ArrayList<>();

    String srcFile;
    String destFile;

    private final ClubConfig clubConfig;

    public CreatePdfFile(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;
    }

    public void createPdfNewsletter(List<MemberData> memberDataList,
                                    List<FeeData> feeDataList,
                                    String srcFile,
                                    String destPath,
                                    String destFileName) {

        this.srcFile = ClubFactory.getAbsolutFilePath(clubConfig, clubConfig.getStage(), srcFile);
        this.destFile = ClubFactory.getDestinationPath(clubConfig.getStage(), destPath, destFileName);

        if (this.srcFile.isEmpty() || this.destFile.isEmpty()) {
            return;
        }

        if (!openDocument()) {
            return;
        }

        if (memberDataList != null) {
            // Mitgliedsdaten
            memberDataList.stream().forEach(memberData -> {

                ReplaceFactory.getAddressData(addressList, clubConfig.clubData, memberData);
                final List<ReplaceData> replaceDataList =
                        ReplaceFactory.getReplaceList(clubConfig.clubData, memberData);
                replaceFile(replaceDataList, this.srcFile);

            });

        } else if (feeDataList != null) {
            // Beitragsdaten
            feeDataList.stream().forEach(feeData -> {

                ReplaceFactory.getAddressData(addressList, clubConfig.clubData, feeData.getMemberData());
                final List<ReplaceData> replaceDataList =
                        ReplaceFactory.getReplaceList(clubConfig.clubData, feeData);
                replaceFile(replaceDataList, this.srcFile);

            });

        } else {
            // nur Vereinsdaten
            final List<ReplaceData> replaceDataList =
                    ReplaceFactory.getReplaceList(clubConfig.clubData);
            replaceFile(replaceDataList, this.srcFile);
        }

        closeDocument();
    }

    private boolean openDocument() {
        try {
            PdfWriter writer = new PdfWriter(destFile);
            pdfDocument = new PdfDocument(writer);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            document = new Document(pdfDocument);

        } catch (Exception ex) {
            PLog.errorLog(451201010, ex);
            PAlert.showErrorAlert(clubConfig.getStage(),
                    "Datei schreiben!",
                    "Die Zieldatei kann nicht geschrieben werden.");
            return false;
        }

        return true;
    }

    private void closeDocument() {
        if (document == null) {
            return;
        }

        try {
            document.close();
        } catch (Exception ex) {
            PLog.errorLog(121214541, ex);
        }
    }

    private void replaceFile(List<ReplaceData> replaceDataList, String srcFileName) {
        List<String> lineList = CreateDocumentFactory.replaceFile(replaceDataList, srcFileName);
        PdfPage pdfPage = pdfDocument.addNewPage();
        Paragraph paragraph = new Paragraph().setFontSize(9);
        Text text = new Text("");

        if (newPage) {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        }
        newPage = true;


        try {
            for (String line : lineList) {

                line = firstCheck(line, pdfPage);
//                if (line.isEmpty()) {
//                    // dann ists erledigt
//                    continue;
//                }

                if (line.isEmpty()) {
                    // leere Zeile
//                    Text title = new Text("\n");
//                    paragraph.add(title);
                    text.setText(text.getText() + "\n");
                    continue;
                }

                // Paragraph p = new Paragraph(line);
                // paragraph.add(line);

//                Text title = new Text(line + "\n");
//                title.getChildren().add(line);
//                paragraph.add(title);

                text.setText(text.getText() + "\n" + line);
            }

            paragraph.add(text);
            document.add(paragraph);
        } catch (
                Exception ex) {
            PLog.errorLog(987451202, ex);
        }
    }

    private String firstCheck(String line, PdfPage pdfPage) throws IOException {

//        if (line.equals("")) {
//            // leere Zeile
//            document.add(new Paragraph(" "));
//        }

        // Fonts
        if (line.contains(NewsletterFactory.TAG_FONT_COURIER)) {
            line = line.replaceAll(NewsletterFactory.TAG_FONT_COURIER, "");
            document.setFont(PdfFontFactory.createFont(StandardFonts.COURIER));

        } else if (line.contains(NewsletterFactory.TAG_FONT_HELVETICA)) {
            line = line.replaceAll(NewsletterFactory.TAG_FONT_HELVETICA, "");
            document.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));

        } else if (line.contains(NewsletterFactory.TAG_FONT_TIMES_ROMAN)) {
            line = line.replaceAll(NewsletterFactory.TAG_FONT_TIMES_ROMAN, "");
            document.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN));
        }

        // restliche tags
        if (line.contains(NewsletterFactory.TAG_FOLD_MARK)) {
            line = line.replaceAll(NewsletterFactory.TAG_FOLD_MARK, "");
            PdfFactory.foldMark(pdfPage);
        }

        if (line.contains(NewsletterFactory.TAG_ADDRESS_FIELD)) {
            line = line.replaceAll(NewsletterFactory.TAG_ADDRESS_FIELD, "");
            PdfFactory.addressBlock(pdfPage, document, addressList);
        }

        if (line.contains(NewsletterFactory.TAG_NEW_SITE)) {
            line = line.replaceAll(NewsletterFactory.TAG_NEW_SITE, "");
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        }

        if (line.contains(NewsletterFactory.TAG_PICTURE_)) {
            line = getLogoTag(line, pdfPage);
        }
        return line;
    }

    private String getLogoTag(String line, PdfPage pdfPage) {
        int i = line.indexOf(NewsletterFactory.TAG_PICTURE_);
        if (i < 0) {
            return line;
        }

        int o = line.indexOf(">>", i);
        if (o < 0) {
            return line;
        }

        String tag = line.substring(i, o + ">>".length());
        line = line.replaceAll(tag, "");

        PdfFactory.addLogo(pdfPage, document, tag, srcFile);

        return line;
    }
}
