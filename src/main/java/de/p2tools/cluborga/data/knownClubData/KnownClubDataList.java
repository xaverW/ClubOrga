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

package de.p2tools.cluborga.data.knownClubData;

import de.p2tools.cluborga.config.club.ClubConfig;

import java.util.Collection;
import java.util.Collections;

public class KnownClubDataList extends KnownClubDataListBase {


    public KnownClubDataList() {
        super();
    }

    @Override
    public synchronized boolean add(KnownClubData projectData) {
        boolean ret = super.add(projectData);
        sort();
        return ret;
    }


    @Override
    public synchronized boolean addAll(Collection<? extends KnownClubData> projectData) {
        boolean ret = super.addAll(projectData);
        sort();
        return ret;
    }

    public void setClubDataAutostart(ClubConfig clubConfig) {
        this.stream().forEach(knownCD -> {
            if (knownCD.getClubConfig() != null && knownCD.getClubConfig().equals(clubConfig)) {
                knownCD.setAutostart(true);
            } else {
                knownCD.setAutostart(false);
            }
        });
    }

    public KnownClubData getKnownClubForStart() {
        return this.stream().filter(c -> c.isAutostart() == true).findAny().orElse(null);
    }

    public void sort() {
        Collections.sort(this);
    }
}
