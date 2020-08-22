/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

package de.p2tools.clubOrga.gui.table;


import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.data.memberData.stateData.StateData;
import de.p2tools.clubOrga.data.memberData.stateData.StateFieldNames;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class ClubTableStateData {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    public ClubTableStateData(ClubConfig clubConfig) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
    }

    public TableColumn[] initColumn(TableView table) {
        ArrayList<TableColumn> tc = new ArrayList<>();

        table.getColumns().clear();

        final TableColumn<StateData, Long> nrColumn = new TableColumn<>(StateFieldNames.NO);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));

        final TableColumn<StateData, String> nameColumn = new TableColumn<>(StateFieldNames.NAME);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        final TableColumn<StateData, String> textColumn = new TableColumn<>(StateFieldNames.DESCRIPTION);
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));


        tc.add(nrColumn);
        tc.add(nameColumn);
        tc.add(textColumn);

        return tc.toArray(new TableColumn[]{});
    }

}
