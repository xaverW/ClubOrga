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

package de.p2tools.clubOrga.data.feeData.feeRateData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.p2Lib.alert.PAlert;

import java.util.Collection;
import java.util.Optional;

public class FeeRateDataList extends FeeRateDataListBase {

    public FeeRateDataList(ClubConfig clubConfig) {
        super(clubConfig);
    }

    public void initListAfterClubLoad() {
        final int rateStandard = FeeRateFactory.RATE_TYPE.RATE_STANDARD.getId();
        if (isEmpty() || get(rateStandard).getId() != rateStandard) {
            final FeeRateData feeRateDataStandard = new FeeRateData();
            feeRateDataStandard.setName(FeeRateFactory.RATE_TYPE.RATE_STANDARD.getName());
            feeRateDataStandard.setText(FeeRateFactory.RATE_TYPE.RATE_STANDARD.getDescription());
            feeRateDataStandard.setBetrag(FeeRateFactory.RATE_TYPE.RATE_STANDARD.getBetrag());
            feeRateDataStandard.setId(rateStandard);
            feeRateDataStandard.setNo(FeeRateFactory.RATE_TYPE.RATE_STANDARD.getShownNo());

            add(rateStandard, feeRateDataStandard);
        }

        final int rateFree = FeeRateFactory.RATE_TYPE.RATE_FREE.getId();
        if (size() <= rateFree || get(rateFree).getId() != rateFree) {
            final FeeRateData feeRateDataFrei = new FeeRateData();
            feeRateDataFrei.setName(FeeRateFactory.RATE_TYPE.RATE_FREE.getName());
            feeRateDataFrei.setText(FeeRateFactory.RATE_TYPE.RATE_FREE.getDescription());
            feeRateDataFrei.setBetrag(FeeRateFactory.RATE_TYPE.RATE_FREE.getBetrag());
            feeRateDataFrei.setId(rateFree);
            feeRateDataFrei.setNo(FeeRateFactory.RATE_TYPE.RATE_FREE.getShownNo());

            add(rateFree, feeRateDataFrei);
        }

        final int rateWithout = FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getId();
        if (size() <= rateWithout || get(rateWithout).getId() != rateWithout) {
            final FeeRateData feeRateDataOhne = new FeeRateData();
            feeRateDataOhne.setName(FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getName());
            feeRateDataOhne.setText(FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getDescription());
            feeRateDataOhne.setBetrag(FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getBetrag());
            feeRateDataOhne.setId(rateWithout);
            feeRateDataOhne.setNo(FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getShownNo());

            add(rateWithout, feeRateDataOhne);
        }
    }

    public FeeRateData getRateDataStandard(FeeRateFactory.RATE_TYPE RATE) {
        switch (RATE) {
            case RATE_FREE:
                return get(FeeRateFactory.RATE_TYPE.RATE_FREE.getId());

            case RATE_WITHOUT:
                return get(FeeRateFactory.RATE_TYPE.RATE_WITHOUT.getId());

            case RATE_STANDARD:
            default:
                return get(FeeRateFactory.RATE_TYPE.RATE_STANDARD.getId());
        }
    }

    @Override
    public boolean remove(Object obj) {
        if (!checkRemove(obj)) {
            return false;
        }

        return super.remove(obj);
    }

    @Override
    public synchronized boolean add(FeeRateData feeData) {
        boolean ret = super.add(feeData);
//        sort();
        setListChanged();
        return ret;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends FeeRateData> feeData) {
        boolean ret = super.addAll(feeData);
//        sort();
        setListChanged();
        return ret;
    }

    public FeeRateData getById(long id) {
        return this.stream().filter(data -> data.getId() == id).findFirst().orElse(null);
    }

    private boolean checkRemove(Object obj) {
        if (((FeeRateData) obj).getId() < FeeRateFactory.RATE_TYPE_SIZE) {
            PAlert.showErrorAlert(clubConfig.getStage(), "Beitragssatz löschen",
                    "Das sind Standardbeitragssätze die nicht gelöscht werden können.");
            return false;
        }

        // Prüfen obs noch Mitglieder mit der Beitragssatz gibt
        long id = ((FeeRateData) obj).getId();
        Optional<MemberData> optionalMemberData = clubConfig.memberDataList.stream().filter(m -> m.getFeeRateData().getId() == id).findAny();
        if (optionalMemberData.isPresent()) {
            PAlert.showErrorAlert(clubConfig.getStage(), "Beitragssatz löschen",
                    "Der Beitragssatz kann nicht gelöscht werden.\n" +
                            "Es gibt noch Mitglieder mit diesem Beitragssatz!");
            return false;
        }

        return true;
    }
}
