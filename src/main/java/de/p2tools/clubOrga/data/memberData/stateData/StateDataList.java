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

package de.p2tools.clubOrga.data.memberData.stateData;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.PException;

import java.util.Collection;

public class StateDataList extends StateDataListBase {

    public StateDataList(ClubConfig clubConfig) {
        super(clubConfig);
    }

    public void initListAfterClubLoad() {
        int idStateActive = StateDataFactory.STATE_TYPE.STATE_ACTIVE.getId();
        if (isEmpty() || get(idStateActive).getId() != idStateActive) {

            final StateData stateDataActive = new StateData();
            stateDataActive.setName(StateDataFactory.STATE_TYPE.STATE_ACTIVE.getName());
            stateDataActive.setText(StateDataFactory.STATE_TYPE.STATE_ACTIVE.getDescription());
            stateDataActive.setId(idStateActive);
            stateDataActive.setNo(StateDataFactory.STATE_TYPE.STATE_ACTIVE.getShownNo());

            add(idStateActive, stateDataActive);
        }

        final int idStatePassive = StateDataFactory.STATE_TYPE.STATE_PASSIVE.getId();
        if (size() <= idStatePassive || get(idStatePassive).getId() != idStatePassive) {

            final StateData stateDataPassive = new StateData();
            stateDataPassive.setName(StateDataFactory.STATE_TYPE.STATE_PASSIVE.getName());
            stateDataPassive.setText(StateDataFactory.STATE_TYPE.STATE_PASSIVE.getDescription());
            stateDataPassive.setId(idStatePassive);
            stateDataPassive.setNo(StateDataFactory.STATE_TYPE.STATE_PASSIVE.getShownNo());

            add(idStatePassive, stateDataPassive);
        }
    }

    public StateData getStateDataStandard(StateDataFactory.STATE_TYPE state) {
        StateData stateData;

        switch (state) {
            case STATE_PASSIVE:
                stateData = get(StateDataFactory.STATE_TYPE.STATE_PASSIVE.getId());
                break;

            case STATE_ACTIVE:
            default:
                stateData = get(StateDataFactory.STATE_TYPE.STATE_ACTIVE.getId());
        }

        return stateData;
    }

    public boolean removeState(StateData stateData) {
        if (stateData.getId() < StateDataFactory.STATE_TYPE_SIZE) {
            PAlert.showErrorAlert(clubConfig.getStage(), "Status löschen", "Das sind Standardstati die nicht gelöscht werden können.");
            return false;
        }

        if (checkStateBevoreRemoval(stateData)) {
            return super.remove(stateData);
        }

        return false;
    }

    @Override
    public boolean remove(Object obj) {
        if (StateData.class != obj.getClass()) {
            PException.throwPException(745102369, "Wrong Class");
            return false;
        }

        changeMember((StateData) obj);
        return super.remove(obj);
    }

    @Override
    public synchronized boolean add(StateData feeData) {
        boolean ret = super.add(feeData);
        setListChanged();
        return ret;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends StateData> feeData) {
        boolean ret = super.addAll(feeData);
        setListChanged();
        return ret;
    }

    public StateData getById(long id) {
        return this.stream().filter(data -> data.getId() == id).findFirst().orElse(null);
    }

    private boolean checkStateBevoreRemoval(StateData stateData) {
        //alle Mitglieder mit Status "stateData" auf "Standard" setzen
        boolean found = clubConfig.memberDataList.stream().filter(m -> m.getStateData().getId() == stateData.getId()).findAny().isPresent();
        if (found) {
            PAlert.BUTTON button = PAlert.showAlert_yes_no(clubConfig.getStage(), "Mitgliederstatus löschen",
                    "Mitgliederstatus löschen",
                    "Der Status der gelöscht werden soll, wird noch von Mitgliedern verwendet. Soll er trotzdem " +
                            "gelöscht werden? Die Mitglieder mit diesem Status werden dann auf \"aktiv\" gesetzt.");
            if (button != PAlert.BUTTON.YES) {
                return false;
            }

            changeMember(stateData);
        }

        return true;
    }

    private void changeMember(StateData stateData) {
        //alle Mitglieder mit Status "stateData" auf "Standard" setzen
        clubConfig.memberDataList.stream()
                .filter(m -> m.getStateData().getId() == stateData.getId())
                .forEach(m -> m.setStateData(clubConfig.stateDataList.getStateDataStandard(StateDataFactory.STATE_TYPE.STATE_ACTIVE)));
    }
}
