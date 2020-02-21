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


package de.p2tools.clubOrga.controller.newsletter;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.controller.newsletter.document.pdfFile.CreatePdfFile;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.clubOrga.data.memberData.MemberData;

import java.util.List;

public class Newsletter {


    private Newsletter() {
    }

    public static void createDocument(ClubConfig clubConfig, NewsletterFactory.NEWSLETTER_TYPE newsletterType,
                                      List<MemberData> memberDataList,
                                      List<FeeData> feeDataList,
                                      String sourceFile,
                                      String destPath,
                                      String destFile) {

        create(clubConfig, newsletterType,
                memberDataList, feeDataList,
                sourceFile, destPath, destFile);
    }

    public static void memberNewsletter(ClubConfig clubConfig, List<MemberData> memberDataList) {
        createNewsletter(clubConfig, memberDataList, null);
    }

    public static void feeNewsletter(ClubConfig clubConfig, List<FeeData> feeDataList) {
        createNewsletter(clubConfig, null, feeDataList);
    }

    private static void createNewsletter(ClubConfig clubConfig,
                                         List<MemberData> memberDataList,
                                         List<FeeData> feeDataList) {

        NewsletterDialogController newsletterDialogController = new NewsletterDialogController(clubConfig);
        if (newsletterDialogController.isOk()) {
            create(clubConfig, newsletterDialogController.getNewsletterType(),
                    memberDataList, feeDataList,
                    newsletterDialogController.getSourceFile(),
                    newsletterDialogController.getDestPath(),
                    newsletterDialogController.getDestFile());
        }
    }

    private static void create(ClubConfig clubConfig, NewsletterFactory.NEWSLETTER_TYPE exportType,
                               List<MemberData> memberDataList,
                               List<FeeData> feeDataList,
                               String sourceFile,
                               String destPath,
                               String destFile) {

        new CreatePdfFile(clubConfig).createPdfNewsletter(memberDataList, feeDataList,
                sourceFile, destPath, destFile);
    }

}
