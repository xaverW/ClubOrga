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
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.controller.ClubFactory;
import de.p2tools.cluborga.controller.newsletter.NewsletterFactory;
import de.p2tools.cluborga.controller.newsletter.ReplaceData;
import de.p2tools.cluborga.controller.newsletter.ReplaceFactory;
import de.p2tools.cluborga.controller.newsletter.document.CreateDocumentFactory;
import de.p2tools.cluborga.data.feeData.FeeData;
import de.p2tools.cluborga.data.memberData.MemberData;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.tools.log.PLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            List<PdfImage> images = new ArrayList<>();
            Optional<MemberData> optionalMemberData = memberDataList.stream().findAny();
            if (optionalMemberData.get() != null) {
                final List<ReplaceData> replaceDataList =
                        ReplaceFactory.getReplaceList(clubConfig.clubData, optionalMemberData.get());
                getImages(images, replaceDataList, this.srcFile);
            }

            memberDataList.stream().forEach(memberData -> {
                ReplaceFactory.getAddressData(addressList, clubConfig.clubData, memberData);
                final List<ReplaceData> replaceDataList =
                        ReplaceFactory.getReplaceList(clubConfig.clubData, memberData);
                replaceFile(images, replaceDataList, this.srcFile);
            });

        } else if (feeDataList != null) {
            // Beitragsdaten
            List<PdfImage> images = new ArrayList<>();
            Optional<FeeData> optionalFeeData = feeDataList.stream().findAny();
            if (optionalFeeData.get() != null) {
                final List<ReplaceData> replaceDataList =
                        ReplaceFactory.getReplaceList(clubConfig.clubData, optionalFeeData.get());
                getImages(images, replaceDataList, this.srcFile);
            }

            feeDataList.stream().forEach(feeData -> {
                ReplaceFactory.getAddressData(addressList, clubConfig.clubData, feeData.getMemberData());
                final List<ReplaceData> replaceDataList =
                        ReplaceFactory.getReplaceList(clubConfig.clubData, feeData);
                replaceFile(images, replaceDataList, this.srcFile);
            });

        } else {
            // nur Vereinsdaten
            List<PdfImage> images = new ArrayList<>();
            final List<ReplaceData> replaceDataList =
                    ReplaceFactory.getReplaceList(clubConfig.clubData);
            getImages(images, replaceDataList, this.srcFile);
            replaceFile(images, replaceDataList, this.srcFile);
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

    private void getImages(List<PdfImage> imageList, List<ReplaceData> replaceDataList, String srcFileName) {
        List<String> lineList = CreateDocumentFactory.replaceFile(replaceDataList, srcFileName);

        try {
            for (String line : lineList) {
                if (line.contains(NewsletterFactory.TAG_PICTURE)) {
                    int i = line.indexOf(NewsletterFactory.TAG_PICTURE);
                    if (i < 0) {
                        continue;
                    }

                    int o = line.indexOf(">>", i);
                    if (o < 0) {
                        continue;
                    }

                    String tag = line.substring(i, o + ">>".length());
                    PdfImage image = PdfFactory.getLogo(tag, srcFile);
                    imageList.add(image);
                }
            }
        } catch (Exception ex) {
            PLog.errorLog(201450304, ex);
        }
    }

    private void replaceFile(List<PdfImage> images, List<ReplaceData> replaceDataList, String srcFileName) {
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
                line = getLine(images, line, pdfPage);
                if (line.isEmpty()) {
                    // leere Zeile
                    text.setText(text.getText() + "\n");
                    continue;
                }

                text.setText(text.getText() + "\n" + line);
            }

            paragraph.add(text);
            document.add(paragraph);
        } catch (Exception ex) {
            PLog.errorLog(987451202, ex);
        }
    }

    private String getLine(List<PdfImage> image, String line, PdfPage pdfPage) throws IOException {
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

        if (line.contains(NewsletterFactory.TAG_PICTURE)) {
            line = getLogoTag(image, line, pdfPage);
//            line = getLogoTag(line, pdfPage);
        }
        return line;
    }

    private String getLogoTag(List<PdfImage> imageList, String line, PdfPage pdfPage) {
        int i = line.indexOf(NewsletterFactory.TAG_PICTURE);
        if (i < 0) {
            return line;
        }

        int o = line.indexOf(">>", i);
        if (o < 0) {
            return line;
        }

        String tag = line.substring(i, o + ">>".length());
        line = line.replaceAll(tag, "");

        String url = PdfFactory.getUrl(tag, srcFile);
        Optional<PdfImage> pdfImage = imageList.stream().filter(image -> image.getUrl().equals(url)).findFirst();
        if (pdfImage.isPresent()) {
            PdfFactory.addLogo(pdfImage.get().getImage(), pdfPage, document, tag, srcFile);
        }

        return line;
    }

    private String getLogoTag(String line, PdfPage pdfPage) {
        int i = line.indexOf(NewsletterFactory.TAG_PICTURE);
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
