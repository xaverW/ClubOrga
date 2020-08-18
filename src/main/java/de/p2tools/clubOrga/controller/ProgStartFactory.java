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
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.ReadWriteConfigFile;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.LogMessage;
import de.p2tools.p2Lib.tools.log.PLog;

import java.nio.file.Path;
import java.util.ArrayList;

public class ProgStartFactory {

    private static final String backupHeader = "Die Einstellungen des Programms sind beschädigt" + P2LibConst.LINE_SEPARATOR +
            "und können nicht geladen werden.";
    private static final String backupText = "Soll versucht werden, mit gesicherten Einstellungen" + P2LibConst.LINE_SEPARATOR
            + "des Programms zu starten?" + P2LibConst.LINE_SEPARATORx2
            + "(ansonsten startet das Programm mit Standardeinstellungen)";

    private static final String backupHeaderClub = "Die Einstellungen des Vereins sind beschädigt" + P2LibConst.LINE_SEPARATOR +
            "und können nicht geladen werden.";
    private static final String backupTextClub = "Soll versucht werden, mit gesicherten" + P2LibConst.LINE_SEPARATOR
            + "Einstellungen des Vereins zu starten?" + P2LibConst.LINE_SEPARATORx2;

    private ProgStartFactory() {
    }

    public static void startMsg() {
//        if (ProgData.debug) {
//            PLogger.setFileHandler(ProgInfos.getLogDirectory_String());
//        }

        ArrayList<String> list = new ArrayList<>();
        list.add("Verzeichnisse:");
        list.add("Programmpfad: " + ProgInfos.getPathJar());
        list.add("Verzeichnis Einstellungen: " + ProgInfos.getSettingsDirectory_String());

        LogMessage.startMsg(ProgConst.PROGRAMNAME, list);
    }

    public static boolean loadProgConfigData() {
        PDuration.onlyPing("ProgStartFactory.loadProgConfigData");
        boolean found;
        startMsg();
        final Path path = ProgInfos.getSettingsFile();
        PLog.sysLog("Programmstart und Progconfig laden: " + path.toString());

        if (found = loadProgConfig(path) == false) {
            //todo? teils geladene Reste entfernen
            PLog.sysLog("->konnte nicht geladen werden!");
        }

        initP2Lib();
        return found;
    }

    private static boolean loadProgConfig(Path xmlFilePath) {
        ProgData progData = ProgData.getInstance();
        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, xmlFilePath);
        configFile.addConfigs(ProgConfig.getInstance());
        configFile.addConfigs(progData.knownClubDataList);

        PLog.sysLog("Konfig lesen: " + xmlFilePath.toString());

        ReadWriteConfigFile readWriteConfigFile = new ReadWriteConfigFile();
        readWriteConfigFile.addConfigFile(configFile);

        boolean ret = readWriteConfigFile.readConfigFile(backupHeader, backupText);

        return ret;
    }

    private static void initP2Lib() {
        P2LibInit.initLib(null, ProgConst.PROGRAMNAME, ProgInfos.getUserAgent(),
                ProgData.debug, ProgData.duration);
        P2LibInit.addCssFile(ProgConst.CSS_FILE);
//        PButton.setHlpImage(GetIcon.getImage("button-help.png", 16, 16));
    }

    /**
     * loads thd club-data and makes the first init on the datta
     *
     * @param clubConfig
     * @return
     */
    public static boolean loadClubConfigData(ClubConfig clubConfig) {
        PDuration.onlyPing("ProgStartFactory.loadClubConfigData");
        PLog.sysLog("Club starten und Clubconfig laden");

        boolean ret = loadAndInitClubConfig(clubConfig);

        if (!ret) {
            PLog.sysLog("-> konnte nicht geladen werden");
            PAlert.showErrorAlert("Die Einstellungen sind beschädigt",
                    "Die Einstellungen des Vereins sind beschädigt" + P2LibConst.LINE_SEPARATOR +
                            "und können nicht geladen werden.");
        }
        return ret;
    }

    private static boolean loadAndInitClubConfig(ClubConfig clubConfig) {
        PDuration.onlyPing("ProgStartFactory.oadClub");
        PDuration.counterStart("readConfig");

        // beim Vereinswechsel müssen die Daten erst gelöscht werden
        clearClubData(clubConfig);


        // ClubConfig und ClubData lesen
        PLog.sysLog("Konfig lesen: " + ProgInfos.getClubDataFileZip(clubConfig.getClubPath()));
        ReadWriteConfigFile readWriteConfigFile = new ReadWriteConfigFile();

        final ConfigFile configFileConf = getConfigClubConnfig(clubConfig);
        readWriteConfigFile.addConfigFile(configFileConf);
        PLog.sysLog("Konfig lesen: " + configFileConf.getConfigFile().toString());

        ConfigFile configFileData = ClubConfigFactory.getClubData(clubConfig);
        readWriteConfigFile.addConfigFile(configFileData);
        PLog.sysLog("Konfig lesen: " + configFileData.getConfigFile().toString());

        final Path xmlFilePathZip = ProgInfos.getClubDataFileZip(clubConfig.getClubPath());
        boolean ret = readWriteConfigFile.readConfigFileZip(xmlFilePathZip, backupHeaderClub, backupTextClub);


        // Clubinit
        PDuration.onlyPing("ClubLoadFactory.initListsAfterClubLoad: initListsAfterClubLoad");
        initListsAfterClubLoad(clubConfig);
        ClubConfigFactory.initFilter(clubConfig);

        PDuration.counterStop("readConfig");
        return ret;
    }

    private static ConfigFile getConfigClubConnfig(ClubConfig clubConfig) {
        // beim Vereinswechsel müssen die Daten erst gelöscht werden
        clubConfig.extraDataListMember.clear();
        clubConfig.extraDataListFee.clear();
        clubConfig.extraDataListFinance.clear();

        ConfigFile configFile = ClubConfigFactory.getClubConfigData(clubConfig);
        PLog.sysLog("Konfig lesen: " + configFile.getConfigFile().toString());

        return configFile;
    }

    public static synchronized void resetClubData(ClubConfig clubConfig) {
        clearClubData(clubConfig);
        initListsAfterClubLoad(clubConfig);
    }

    private static synchronized void clearClubData(ClubConfig clubConfig) {
        clubConfig.memberDataList.clear();
        clubConfig.feeDataList.clear();
        clubConfig.financeDataList.clear();

        clubConfig.feeRateDataList.clear();
        clubConfig.stateDataList.clear();
        clubConfig.paymentTypeDataList.clear();
        clubConfig.financeAccountDataList.clear();
        clubConfig.financeCategoryDataList.clear();
    }

    //#############################################
    // init lists
    public static synchronized void initListsAfterClubLoad(ClubConfig clubConfig) {
        PDuration.counterStart("initListsAfterClubLoad");
        clubConfig.extraDataListMember.initListAfterClubLoad();
        clubConfig.extraDataListFee.initListAfterClubLoad();
        clubConfig.extraDataListFinance.initListAfterClubLoad();

        clubConfig.feeRateDataList.initListAfterClubLoad();
        clubConfig.stateDataList.initListAfterClubLoad();
        clubConfig.financeAccountDataList.initListAfterClubLoad();
        clubConfig.financeCategoryDataList.initListAfterClubLoad();
        clubConfig.paymentTypeDataList.initListAfterClubLoad(); // braucht die Konten

        clubConfig.memberDataList.initListAfterClubLoad();
        clubConfig.feeDataList.initListAfterClubLoad(clubConfig);
        clubConfig.financeDataList.initListAfterClubLoad(clubConfig);

        PDuration.counterStop("initListsAfterClubLoad");
    }
}


