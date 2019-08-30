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


package de.p2tools.clubOrga.data.deleteData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.controller.ProgStartFactory;

public class DeleteDataFactory {

    public static void deleteAllData(ClubConfig clubConfig) {
        if (new DeleteAllDialogController(clubConfig).isOk()) {
            ProgStartFactory.resetClubData(clubConfig);
            clubConfig.guiFinanceReport.isShown();
        }
    }

}
