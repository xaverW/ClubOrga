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

package de.p2tools.clubOrga.gui.guiMember;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.controller.export.ExportCsvDialogController;
import de.p2tools.clubOrga.controller.export.ImportCsvMemberDialogController;
import de.p2tools.clubOrga.controller.newsletter.Newsletter;
import de.p2tools.clubOrga.data.feeData.FeeFactory;
import de.p2tools.clubOrga.data.memberData.MemberData;
import de.p2tools.clubOrga.data.memberData.MemberFactory;
import de.p2tools.clubOrga.gui.dialog.dataDialog.DataDialogController;
import de.p2tools.p2Lib.guiTools.PButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class GuiMemberMenu extends VBox {

    private final ProgData progData;
    private final ClubConfig clubConfig;

    private final GuiMember guiMember;


    public GuiMemberMenu(ClubConfig clubConfig, GuiMember guiMember) {
        this.progData = ProgData.getInstance();
        this.clubConfig = clubConfig;
        this.guiMember = guiMember;

        initCont();
    }

    private void initCont() {
        this.setSpacing(10);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_CENTER);

        final MenuButton mb = new MenuButton("");
        mb.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU);
        mb.getStyleClass().add("btnFunction");

        // Mitglied
        MenuItem miChangeMember = new MenuItem("ausgewähltes Mitglied ändern");
        miChangeMember.setOnAction(a -> guiMember.changeMember());

        MenuItem miAddMember = new MenuItem("ein neues Mitglied anlegen");
        miAddMember.setOnAction(a -> addNewMember());
        MenuItem miRemoveMember = new MenuItem("ausgewählte Mitglieder löschen");
        miRemoveMember.setOnAction(a -> removeMember());

        // Beitrag
        MenuItem miAddFee = new MenuItem("einen Beitrag anlegen");
        miAddFee.setOnAction(a -> FeeFactory.generateFeeForMember(clubConfig, clubConfig.guiMember.getSel()));

        MenuItem miMemberFee = new MenuItem("fehlende Beiträge für Auswahl anlegen");
        miMemberFee.setOnAction(a -> {
            ArrayList<MemberData> memberDataList = clubConfig.guiMember.getSelList();
            if (memberDataList.isEmpty()) {
                return;
            }
            FeeFactory.generateMissingFeesForMembers(clubConfig, memberDataList);
        });

        MenuItem miAllMemberFee = new MenuItem("alle fehlende Beiträge anlegen");
        miAllMemberFee.setOnAction(a -> {
            ArrayList<MemberData> memberDataList = clubConfig.guiMember.getSelList();
            if (memberDataList.isEmpty()) {
                return;
            }
            FeeFactory.generateMissingFeesForMembers(clubConfig, clubConfig.memberDataList);
        });

        // Serienbriefe
        MenuItem miNewsletter = new MenuItem("Serienbrief für Auswahl erstellen");
        miNewsletter.setOnAction(event -> createNewsletter());

//        // Export
//        MenuItem miExportSelMember = new MenuItem("Auswahl in CSV-Datei exportieren");
//        miExportSelMember.setOnAction(event -> exportSelMember());
//
//        MenuItem miExportMember = new MenuItem("alle in CSV-Datei exportieren");
//        miExportMember.setOnAction(event -> exportMember());
//
//        MenuItem miImportMember = new MenuItem("aus CSV-Datei importieren");
//        miImportMember.setOnAction(event -> importMember());
//
//
//        // Menü
//        Menu menuExport = new Menu("Mitglieder exoprtieren");
//        menuExport.getItems().addAll(miExportSelMember, miExportMember, miImportMember);

        mb.getItems().addAll(miChangeMember,
                new SeparatorMenuItem(), miAddMember, miRemoveMember,
                new SeparatorMenuItem(), miAddFee, miMemberFee, miAllMemberFee,
                new SeparatorMenuItem(), miNewsletter);

        // extra Buttons
        Button btnNew = PButton.getButton(new ProgIcons().ICON_BUTTON_ADD, "neues Mitglied anlegen");
        btnNew.setOnAction(a -> {
            addNewMember();
        });

        Button btnDel = PButton.getButton(new ProgIcons().ICON_BUTTON_REMOVE, "ausgewähltes Mitglied löschen");
        btnDel.setOnAction(a -> removeMember());

        Button btnChange = PButton.getButton(new ProgIcons().ICON_BUTTON_MEMBER_CHANGE, "Mitglied ändern");
        btnChange.setOnAction(a -> guiMember.changeMember());
        getChildren().addAll(mb, btnNew, btnDel, btnChange);


    }

    private void createNewsletter() {
        List<MemberData> memberData = clubConfig.guiMember.getSelList();
        if (!memberData.isEmpty()) {
            Newsletter.memberNewsletter(clubConfig, memberData);
        }
    }

    private void createNewsletterAll() {
        Newsletter.memberNewsletter(clubConfig, clubConfig.memberDataList);
    }

    private void exportSelMember() {
        List<MemberData> memberData = clubConfig.guiMember.getSelList();
        if (!memberData.isEmpty()) {
            new ExportCsvDialogController(clubConfig.getStage(), clubConfig, memberData, null);
        }
    }

    private void exportMember() {
        new ExportCsvDialogController(clubConfig.getStage(), clubConfig, clubConfig.memberDataList, null);
    }

    private void importMember() {
        new ImportCsvMemberDialogController(clubConfig.getStage(), clubConfig);
    }

    private void addNewMember() {
        MemberData memberData = MemberFactory.getNewMemberData(clubConfig, "", true);
        if (new DataDialogController(clubConfig, DataDialogController.OPEN.MEMBER_PANE,
                memberData, null, null, null).isOk()) {
            clubConfig.memberDataList.add(memberData);
        }
    }

    private void removeMember() {
        List<MemberData> data = guiMember.getSelList();
        if (!data.isEmpty()) {
            clubConfig.memberDataList.memberDataListRemoveAll(data);
        }
    }
}
