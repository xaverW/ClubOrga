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

package de.p2tools.clubOrga.data.financeData.categoryData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.p2Lib.alert.PAlert;

import java.util.Collection;

public class FinanceCategoryDataList extends FinanceCategoryDataListBase {

    public FinanceCategoryDataList(ClubConfig clubConfig) {
        super(clubConfig);
    }

    public void initListAfterClubLoad() {
        final int idBeitrag = FinanceCategoryFactory.CATEGORY_TYPE.CATEGORY_BEITRAG.getId();
        if (isEmpty() || get(idBeitrag).getId() != idBeitrag) {
            final FinanceCategoryData data = new FinanceCategoryData();
            data.setKategorie(FinanceCategoryFactory.CATEGORY_TYPE.CATEGORY_BEITRAG.getName());
            data.setDescription(FinanceCategoryFactory.CATEGORY_TYPE.CATEGORY_BEITRAG.getDescription());
            data.setId(idBeitrag);
            data.setNr(FinanceCategoryFactory.CATEGORY_TYPE.CATEGORY_BEITRAG.getShowNo());

            add(idBeitrag, data);
        }
    }

    public FinanceCategoryData getCategoryDataStandard(FinanceCategoryFactory.CATEGORY_TYPE categoryType) {
        switch (categoryType) {
            case CATEGORY_BEITRAG:
            default:
                return get(FinanceCategoryFactory.CATEGORY_TYPE.CATEGORY_BEITRAG.getId());
        }
    }

    @Override
    public boolean remove(Object obj) {
        if (!checkRemove((FinanceCategoryData) obj)) {
            return false;
        }

        return super.remove(obj);
    }

    @Override
    public synchronized boolean add(FinanceCategoryData data) {
        boolean ret = super.add(data);
//        sort();
        setListChanged();
        return ret;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends FinanceCategoryData> data) {
        boolean ret = super.addAll(data);
//        sort();
        setListChanged();
        return ret;
    }

    public FinanceCategoryData getById(long id) {
        return this.stream().filter(data -> data.getId() == id).findFirst().orElse(null);
    }

    public boolean checkRemove(FinanceCategoryData financeCategoryData) {
        if (financeCategoryData.getId() < FinanceCategoryFactory.CATEGORY_TYPE_SIZE) {
            PAlert.showErrorAlert(clubConfig.getStage(), "Kategorie löschen", "Das ist eine Standardkategorie die nicht gelöscht werden kann.");
            return false;
        }

        return true;
    }
}
