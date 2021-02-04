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


package de.p2tools.clubOrga.controller.sepa;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.feeData.FeeData;
import de.p2tools.p2Lib.tools.log.PLog;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ExportSepa {

    public static boolean createSepaFile(ClubConfig clubConfig, List<FeeData> feeDataList, String executeDate) {
        boolean ret = false;

        Path sepaFile = Paths.get(clubConfig.PAY_FEE_SEPA_DIR.getValue(), clubConfig.PAY_FEE_SEPA_FILE.getValue());
        Path sepaBegleit = Paths.get(clubConfig.PAY_FEE_SEPA_DIR.getValue(), clubConfig.PAY_FEE_SEPA_BEGLEIT_FILE.getValue());

        try {
            Sepa sepa = new Sepa(clubConfig, feeDataList, sepaFile, executeDate);

            if (sepa.write()) {
                ret = true;
                sepa.writeAddOnFile(sepaBegleit);
            }

        } catch (Exception ex) {
            PLog.errorLog(945120214, ex);
        }
        return ret;
    }
}
