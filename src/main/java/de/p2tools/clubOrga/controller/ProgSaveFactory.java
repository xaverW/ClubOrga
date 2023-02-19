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

package de.p2tools.clubOrga.controller;

import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.club.ClubConfigFactory;
import de.p2tools.clubOrga.config.prog.ProgConfig;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.ConfigWriteFile;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.ProgramToolsFactory;
import de.p2tools.p2Lib.tools.log.PLog;

import java.nio.file.Path;
import java.util.ArrayList;

public class ProgSaveFactory {

    private ProgSaveFactory() {
    }

    public static void saveAll() {
        PLog.sysLog("save all data");
        ProgData progData = ProgData.getInstance();
        saveProgConfig();
        progData.knownClubDataList.stream()
                .filter(knownClubData -> knownClubData.getClubConfig() != null)
                .forEach(knownClubData -> saveClub(knownClubData.getClubConfig()));
    }

    private static void saveProgConfig() {
        // ist die Clubauswahl und der Dialog dazu
        PLog.sysLog("Alle Programmeinstellungen sichern");
//        getProgConfigSizes();

        final Path xmlFilePath = ProgInfos.getSettingsFile();
        ConfigFile configFile = new ConfigFile(xmlFilePath.toString(), true);

        ProgConfig.SYSTEM_PROG_VERSION.set(ProgramToolsFactory.getProgVersion());
        ProgConfig.SYSTEM_PROG_BUILD_NO.set(ProgramToolsFactory.getBuild());
        ProgConfig.SYSTEM_PROG_BUILD_DATE.set(ProgramToolsFactory.getCompileDate());

        configFile.addConfigs(ProgConfig.getInstance());
        configFile.addConfigs(ProgData.getInstance().knownClubDataList);

        ConfigWriteFile.writeConfigFile(configFile);
    }

//    private static void getProgConfigSizes() {
//        ProgData progData = ProgData.getInstance();
//
//        // Hauptfenster Clubauswahl
//        if (progData.clubSelector != null) {
//            PGuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_CLUB_SELECTOR_GUI, progData.clubSelector.getStage());
//        }
//    }

    public static void saveClub(ClubConfig clubConfig) {
        PLog.sysLog("save club: " + clubConfig.clubData.getName());
        getClubSizes(clubConfig);

        final Path xmlFilePathZip = ProgInfos.getClubDataFileZip(clubConfig.getClubPath());
        ArrayList<ConfigFile> cFileList = new ArrayList<>();

        ConfigFile configFile = new ConfigFile("", false);
        ClubConfigFactory.getClubConfigData(clubConfig, configFile);
        cFileList.add(configFile);

        configFile = new ConfigFile("", false);
        ClubConfigFactory.getClubData(clubConfig, configFile);
        cFileList.add(configFile);

        ConfigWriteFile.writeConfigFileZip(xmlFilePathZip, cFileList);
    }

    private static void getClubSizes(ClubConfig clubConfig) {
        // Clubfenster
        PGuiSize.getSizeStage(clubConfig.GUI_CLUB_PANEL, clubConfig.getStage());

        if (clubConfig.guiMember != null) {
            clubConfig.guiMember.saveTable();
        }
        if (clubConfig.guiFeeRate != null) {
            clubConfig.guiFeeRate.saveTable();
        }
        if (clubConfig.guiMemberState != null) {
            clubConfig.guiMemberState.saveTable();
        }
        if (clubConfig.guiFeePaymentType != null) {
            clubConfig.guiFeePaymentType.saveTable();
        }
        if (clubConfig.guiFee != null) {
            clubConfig.guiFee.saveTable();
        }
        if (clubConfig.guiFinance != null) {
            clubConfig.guiFinance.saveTable();
        }
        if (clubConfig.guiFinanceAccount != null) {
            clubConfig.guiFinanceAccount.saveTable();
        }
        if (clubConfig.guiFinanceCategory != null) {
            clubConfig.guiFinanceCategory.saveTable();
        }
        if (clubConfig.guiFinanceReport != null) {
            clubConfig.guiFinanceReport.saveTable();
        }

    }
}
