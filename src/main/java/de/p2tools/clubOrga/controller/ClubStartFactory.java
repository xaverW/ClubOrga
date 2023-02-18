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

import de.p2tools.clubOrga.Club;
import de.p2tools.clubOrga.ClubGuiController;
import de.p2tools.clubOrga.ClubSelector;
import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.club.ProgColorList;
import de.p2tools.clubOrga.config.prog.ProgConfig;
import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.clubOrga.config.prog.ProgData;
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.clubOrga.controller.newsletter.NewsletterFactory;
import de.p2tools.clubOrga.data.demoData.DemoData;
import de.p2tools.clubOrga.data.knownClubData.KnownClubData;
import de.p2tools.clubOrga.icon.GetIcon;
import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.date.DateFactory;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.file.PFileName;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClubStartFactory {

    private ClubStartFactory() {
    }

    public static void startClubSelector() {
        startClubSelector(null);
    }

    public static void startClubSelector(Stage stage) {
        ProgData progData = ProgData.getInstance();

        if (progData.clubSelector != null) {
            progData.clubSelector.getStage().show();
            progData.clubSelector.getStage().toFront();
        } else {
            progData.clubSelector = new ClubSelector(stage);
        }
    }

    /**
     * this ist the startpoint for a new club
     *
     * @param knownClubData
     */
    public static boolean startNewClub(KnownClubData knownClubData) {
        ProgData progData = ProgData.getInstance();
        progData.knownClubDataList.add(knownClubData);

        ClubConfig clubConfig = initClubConfig(null, knownClubData);
        clubConfig.setFirstStart(true);
        clubConfig.setAddDemoData(knownClubData.isAddDemoData());

        return startClubGui(clubConfig);
    }

    /**
     * this is the startpoint for a clubstart of a known club
     *
     * @param stage
     * @param knownClubData
     */
    public static boolean startClub(Stage stage, KnownClubData knownClubData) {
        initClubConfig(stage, knownClubData);
        return startClubGui(knownClubData.getClubConfig());
    }

    private static ClubConfig initClubConfig(Stage stage, KnownClubData knownClubData) {
        // macht das erste Init
        if (knownClubData.getClubConfig() == null) {
            ClubConfig clubConfig = new ClubConfig(knownClubData.getClubpath(), knownClubData.getClubname());
            knownClubData.setClubConfig(clubConfig);
        }

        ClubConfig clubConfig = knownClubData.getClubConfig();
        clubConfig.setClubIsStarting(true);
        ProgData.getInstance().knownClubDataList.setClubDataAutostart(clubConfig);
        knownClubData.clubnameProperty().unbind();
        knownClubData.clubnameProperty().bind(clubConfig.clubData.nameProperty());

        if (clubConfig.getStage() == null) {
            if (stage == null) {
                stage = new Stage();
            }
            clubConfig.setStage(stage);
        }

        return clubConfig;
    }

    private static boolean startClubGui(ClubConfig clubConfig) {
        // lädt die Daten/init der Daten beim ersten Start
        // und startet dann die GUI
        boolean ret = true;

        try {
            PDuration.onlyPing("startClub");

            if (clubConfig.isFirstStart()) {
                // der erste Start des Clubs
                // init und Ordner erstellen und befüllen, ....
                initFirstClubStart(clubConfig);

                ProgStartFactory.initListsAfterClubLoad(clubConfig);
                if (clubConfig.isAddDemoData()) {
                    // dann auch Demodaten einfügen
                    new DemoData().addDemoDataClubStart(clubConfig);
                }
                PDuration.onlyPing("startClub: ClubConfigData initialisiert");

            } else {
                // bereits erstellten Club laden
                ret = ProgStartFactory.loadClubConfigData(clubConfig);
                PDuration.onlyPing("startClub: ClubConfigData geladen");
            }

            // geladen und jetzt das ClubGUI starten
            clubConfig.setClubIsStarting(false);
            if (ret) {
                showClubGui(clubConfig);
                checkProgUpdate(ProgData.getInstance());
                PDuration.onlyPing("startClub: geladen");
            }

        } catch (final Exception e) {
            PLog.errorLog(945120125, "startClub");
            PAlert.showErrorAlert((Stage) null, "Fehler beim Start", e.getLocalizedMessage());
            ClubStartFactory.startClubSelector();
        }
        return ret;
    }

    private static void showClubGui(ClubConfig clubConfig) {
        try {
            if (clubConfig.clubGuiController == null) {

                clubConfig.clubGuiController = new ClubGuiController();
                clubConfig.clubGuiController.init(clubConfig);
                Scene scene = new Scene(clubConfig.clubGuiController,
                        PGuiSize.getWidth(clubConfig.GUI_CLUB_PANEL),
                        PGuiSize.getHeight(clubConfig.GUI_CLUB_PANEL));

                ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> {
                    addThemeCss(scene);
                    ProgColorList.setColorTheme();
                });
                addThemeCss(scene);
                ProgColorList.setColorTheme();

                clubConfig.getStage().setScene(scene);
                clubConfig.getStage().getIcons().add(GetIcon.getImage(ProgConst.P2_ICON_32, ProgConst.P2_ICON_PATH, 32, 32));
                clubConfig.getStage().setOnCloseRequest(e -> {
                    e.consume();
                    ProgQuitFactory.quitClub(ProgData.getInstance(), clubConfig);
                });
                setTitel(clubConfig);

            } else {
                //damit beim wiederholten starten des clubs die Größe stimmt
                clubConfig.getStage().setHeight(PGuiSize.getHeight(clubConfig.GUI_CLUB_PANEL));
                clubConfig.getStage().setWidth(PGuiSize.getWidth(clubConfig.GUI_CLUB_PANEL));
            }

            PGuiSize.setOnlyPos(clubConfig.GUI_CLUB_PANEL, clubConfig.getStage());

            clubConfig.getStage().show();
            clubConfig.getStage().toFront();
            PDuration.onlyPing("showClubGui: geladen");
        } catch (final Exception e) {
            PLog.errorLog(951747896, "showClubGui");
            PAlert.showErrorAlert((Stage) null, "Fehler beim Clubload", e.getLocalizedMessage());
            ClubStartFactory.startClubSelector();
        }
    }

    static void setTitel(ClubConfig clubConfig) {
        setTitel(clubConfig, "");
    }

    static void setTitel(ClubConfig clubConfig, String title) {
        //todo DEBUG anzeigen
        if (!title.isEmpty()) {
            clubConfig.getStage().titleProperty().unbind();
            clubConfig.getStage().titleProperty().setValue(title);
        } else {
            clubConfig.getStage().titleProperty().bind(
                    Bindings.when(clubConfig.clubData.nameProperty().isEmpty())
                            .then(ProgConst.PROGRAM_NAME)
                            .otherwise(
                                    Bindings.concat(ProgConst.PROGRAM_NAME,
                                            " <@> ",
                                            clubConfig.clubData.nameProperty()))
            );
        }
    }

    private static void checkProgUpdate(ProgData progData) {
        // Prüfen obs ein Programmupdate gibt
        PDuration.onlyPing("checkProgUpdate");


        if (ProgData.debug) {
            // damits bei jedem Start gemacht wird
            PLog.sysLog("DEBUG: Update-Check");
            runUpdateCheck(progData, true);

        } else if (ProgConfig.SYSTEM_UPDATE_SEARCH_ACT.get() &&
                !updateCheckTodayDone()) {
            // nach Updates suchen
            runUpdateCheck(progData, false);

        } else {
            // will der User nicht --oder-- wurde heute schon gemacht
            List list = new ArrayList(5);
            list.add("Kein Update-Check:");
            if (!ProgConfig.SYSTEM_UPDATE_SEARCH_ACT.get()) {
                list.add("  der User will nicht");
            }
            if (updateCheckTodayDone()) {
                list.add("  heute schon gemacht");
            }
            PLog.sysLog(list);
        }
    }

    private static boolean updateCheckTodayDone() {
        return ProgConfig.SYSTEM_UPDATE_DATE.get().equals(DateFactory.F_FORMAT_yyyy_MM_dd.format(new Date()));
    }


    private static void runUpdateCheck(ProgData progData, boolean showAlways) {
        //prüft auf neue Version, ProgVersion und auch (wenn gewünscht) BETA-Version, ..
        ProgConfig.SYSTEM_UPDATE_DATE.setValue(DateFactory.F_FORMAT_yyyy_MM_dd.format(new Date()));
        new SearchProgramUpdate(progData).searchNewProgramVersion(showAlways);
    }

    public static boolean noClubStageIsRunning() {
        ProgData progData = ProgData.getInstance();

        for (KnownClubData kc : progData.knownClubDataList) {
            if (kc.getClubConfig() != null &&
                    kc.getClubConfig().getStage() != null
                    && kc.getClubConfig().getStage().isShowing()) {
                return false;
            }
        }

        return true;
    }

    public static boolean onlyThisClubStageIsRunning(ClubConfig clubConfig) {
        ProgData progData = ProgData.getInstance();

        for (KnownClubData kc : progData.knownClubDataList) {
            if (kc.getClubConfig() != null && kc.getClubConfig().equals(clubConfig)) {
                continue;
            }

            if (kc.getClubConfig() != null &&
                    kc.getClubConfig().getStage() != null
                    && kc.getClubConfig().getStage().isShowing()) {
                return false;
            }
        }

        return true;
    }

//    private static void addCss(Scene scene) {
//        ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> setTheme(scene));
//        setTheme(scene);
//    }

    private static void addThemeCss(Scene scene) {
        if (ProgConfig.SYSTEM_DARK_THEME.get()) {
            P2LibInit.addCssFile(ProgConst.CSS_FILE_DARK_THEME);
        } else {
            P2LibInit.removeCssFile(ProgConst.CSS_FILE_DARK_THEME);
        }
        P2LibInit.addP2CssToScene(scene);
    }

    private static boolean initFirstClubStart(ClubConfig clubConfig) {
        boolean ret = true;

        // Newsletter-Vorlagen und Pfade anlegen
        clubConfig.NEWSLETTER_DIR.set(ProgInfos.getClubDataLetterFile(clubConfig.getClubPath()).toString());
        try {
            final Path templateFilePath = ProgInfos.getClubDataTemplateFile(clubConfig.getClubPath());

            File srcDir = new File("./" + ProgConst.CLUB_TEMPLATE_DIR);
            if (srcDir.exists()) {
                // wenn im ProgDir Vorlagen vorhanden sind, dann die nehmen
                PLog.sysLog("Vorlagen kopieren: " + srcDir.toString());
                File destDir = templateFilePath.toFile();
                FileUtils.copyDirectory(srcDir, destDir);

            } else {
                // ansonsten gibts die aus dem SrcDir
                PLog.sysLog("Vorlagen kopieren: " + "source");
                InputStream src = Club.class.getResourceAsStream(ProgConst.TEMPLATE_PATH + ProgConst.TEMPLATE_1);
                Files.copy(src, templateFilePath.resolve(ProgConst.TEMPLATE_1), StandardCopyOption.REPLACE_EXISTING);

                src = Club.class.getResourceAsStream(ProgConst.TEMPLATE_PATH + ProgConst.TEMPLATE_2);
                Files.copy(src, templateFilePath.resolve(ProgConst.TEMPLATE_2), StandardCopyOption.REPLACE_EXISTING);

                src = Club.class.getResourceAsStream(ProgConst.TEMPLATE_PATH + ProgConst.TEMPLATE_3);
                Files.copy(src, templateFilePath.resolve(ProgConst.TEMPLATE_3), StandardCopyOption.REPLACE_EXISTING);

                src = Club.class.getResourceAsStream(ProgConst.TEMPLATE_PATH + ProgConst.TEMPLATE_4);
                Files.copy(src, templateFilePath.resolve(ProgConst.TEMPLATE_4), StandardCopyOption.REPLACE_EXISTING);
            }

            List<File> list = NewsletterFactory.getTemplates(clubConfig);
            for (File file : list) {
                String f = PFileName.getFilenameRelative(file, ProgInfos.getClubDirectory(clubConfig.getClubPath()).toString());
                clubConfig.CBO_LIST_NEWSLETTER_TEMPLATE.addAll(f);
            }
        } catch (Exception ex) {
            // todo -> windows
            PLog.errorLog(951203214, ex, "cant copy templates");
            ret = false;
        }

        // Export-Pfade anlegen
        clubConfig.CBO_LIST_EXPORT_CSV_DIR.add(ProgInfos.getClubExportsDir(clubConfig.getClubPath()).toString());
        clubConfig.EXPORT_CSV_DIR.set(ProgInfos.getClubExportsDir(clubConfig.getClubPath()).toString());

        clubConfig.CBO_LIST_EXPORT_ZIP_DIR.add(ProgInfos.getClubExportsDir(clubConfig.getClubPath()).toString());
        clubConfig.EXPORT_ZIP_DIR.set(ProgInfos.getClubExportsDir(clubConfig.getClubPath()).toString());

        return ret;
    }
}
