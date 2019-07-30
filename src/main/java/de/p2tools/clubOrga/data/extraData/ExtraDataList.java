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

package de.p2tools.clubOrga.data.extraData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;

public class ExtraDataList extends ExtraDataListBase {

    private final int max;

    public ExtraDataList(ClubConfig clubConfig, int max) {
        super(clubConfig);
        this.max = max;
    }

    /**
     * Init nach dem Laden der ClubConfig
     */

    public void initListAfterClubLoad() {
        while (size() < max) {
            add(new ExtraData(ExtraKind.EXTRA_KIND.STRING.toString(),
                    ProgConst.EXTRA_DATA_NAME + size(), "", ""));
        }
        while (size() > max) {
            remove(size() - 1);
        }
    }

    @Override
    public ExtraData get(int i) {
        if (this.size() <= i) {
            initListAfterClubLoad();
        }
        ExtraData ex = super.get(i);

        return ex;
    }


}
