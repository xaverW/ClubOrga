/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.clubOrga.controller.export.zip;

import de.p2tools.clubOrga.config.prog.ProgConst;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFactory {

    private static List<String> fileList = new ArrayList<>();

    private ZipFactory() {
    }

    public static boolean exportClub(Stage stage, Path pathClub, Path pathExportFile) {
        try {
            zipFolder(pathClub.toFile(), pathExportFile.toFile());
        } catch (Exception ex) {
            PAlert.showErrorAlert(stage, "Verein exportieren", "Das angegebene Speicherziel für den Export " +
                    "kann nicht erstellt werden.");
            PLog.errorLog(984512067, ex, "export club");
            return false;
        }

        return true;
    }

    private static void zipFolder(File srcFolder, File destZipFile) throws Exception {
        File tempFile = File.createTempFile(ProgConst.PROGRAMNAME + "-", "." + ProgConst.ZIP_SUFFIX);

        try (FileOutputStream fileWriter = new FileOutputStream(tempFile);
             ZipOutputStream zip = new ZipOutputStream(fileWriter)) {

            addFolderToZip(srcFolder, srcFolder, zip);
        }
        Files.move(tempFile.toPath(), destZipFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private static void addFileToZip(File rootPath, File srcFile, ZipOutputStream zip) throws Exception {

        if (srcFile.isDirectory()) {
            addFolderToZip(rootPath, srcFile, zip);
        } else {
            byte[] buf = new byte[1024];
            int len;
            try (FileInputStream in = new FileInputStream(srcFile)) {
                String name = srcFile.getPath();
                name = name.replace(rootPath.getPath(), "");
//                System.out.println("Zip " + srcFile + "\n to " + name);
                zip.putNextEntry(new ZipEntry(name));
                while ((len = in.read(buf)) > 0) {
                    zip.write(buf, 0, len);
                }
            }
        }
    }

    private static void addFolderToZip(File rootPath, File srcFolder, ZipOutputStream zip) throws Exception {
        for (File fileName : srcFolder.listFiles()) {
            addFileToZip(rootPath, fileName, zip);
        }
    }


    public static boolean unzipFileToFolder(Stage stage, String zipFile, String destFolder) {
        Path pathZipFile = Paths.get(zipFile);
        Path pathDestFolder = Paths.get(destFolder);

        // zip-file
        if (!pathZipFile.toFile().exists() || !pathZipFile.toFile().isFile()) {
            PAlert.showErrorAlert(stage, "ZIP-Datei", "Die angegebene ZIP-Datei existiert nicht " +
                    "oder ist ein Ordner.");
            return false;
        }

        // destDir
        if (pathDestFolder.toFile().exists() && !pathDestFolder.toFile().isDirectory()) {
            PAlert.showErrorAlert(stage, "Speicherort", "Der angegebene Speicherort für den Verein existiert " +
                    "bereits und ist kein Ordner.");
            return false;
        }

        if (pathDestFolder.toFile().exists() && pathDestFolder.toFile().listFiles().length > 0) {
            PAlert.showErrorAlert(stage, "Speicherort", "Der angegebene Speicherort für den Verein existiert " +
                    "bereits und ist nicht leer.");
            return false;
//            PAlert.BUTTON btn = PAlert.showAlert_yes_no(stage, "Verein importieren",
//                    "Speicherort",
//                    "Der angegebene Speicherort für den Verein existiert " +
//                            "bereits und ist nicht leer. Soll der Ordner gelöscht werden?");
//            if (!btn.equals(PAlert.BUTTON.YES)) {
//                return false;
//            }
        }

        if (!pathDestFolder.toFile().exists() && !pathDestFolder.toFile().mkdirs()) {
            PAlert.showErrorAlert(stage, "Speicherort", "Der angegebene Speicherort für den Verein " +
                    "kann nicht angelegt werden.");
            return false;
        }


        try {
            unzipFile(pathZipFile.toFile(), pathDestFolder.toFile());
        } catch (Exception ex) {
            PAlert.showErrorAlert(stage, "ZIP entpacken", "Die angegebene ZIP-Datei konnte nicht " +
                    "entpackt werden.");
            PLog.errorLog(951203037, ex, "import club");
            return false;
        }

        return true;
    }

    private static void unzipFile(File fileZip, File destDir) throws Exception {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
//            newFile.getParentFile().mkdirs();

            FileOutputStream fos = FileUtils.openOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

}