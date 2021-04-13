/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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

import com.itextpdf.layout.element.Image;

public class PdfImage {
    private final String url;
    private final Image image;

    public PdfImage(String url, Image image) {
        this.url = url;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public Image getImage() {
        return image;
    }
}
