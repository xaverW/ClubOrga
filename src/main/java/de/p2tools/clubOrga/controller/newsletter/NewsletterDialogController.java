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

package de.p2tools.clubOrga.controller.newsletter;


import de.p2tools.clubOrga.config.club.ClubConfig;
import de.p2tools.clubOrga.config.prog.ProgIcons;
import de.p2tools.clubOrga.config.prog.ProgInfos;
import de.p2tools.clubOrga.controller.ClubFactory;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBoxString;
import de.p2tools.p2Lib.tools.file.PFileName;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewsletterDialogController extends PDialogExtra {

    private final Button btnOk = new Button("Ok");
    private final Button btnCancel = new Button("Abbrechen");
    private Button btnHelp;

    private final VBox vBoxCont = new VBox(10);

    private final PComboBoxString pCboTemplate = new PComboBoxString();
    private final PComboBoxString pCboNewsletterPath = new PComboBoxString();
    private final PComboBoxString pCboNewsletterFile = new PComboBoxString();
    private final Button btnTemplate = new Button();
    private final Button btnNewsletterPath = new Button();
    private final Button btnProposeFileName = new Button();

    private boolean ok = false;
    private Stage stage;
    private final ClubConfig clubConfig;

    private NewsletterFactory.NEWSLETTER_TYPE newsletterType = NewsletterFactory.NEWSLETTER_TYPE.PDF;
    private String sourceFile = "";
    private String destPath = "";
    private String destFile = "";

    public NewsletterDialogController(ClubConfig clubConfig) {
        super(clubConfig.getStage(), clubConfig.NEWSLETTER_TO_ODF_DIALOG_SIZE,
                "Serienbrief erstellen", true, true);
        this.clubConfig = clubConfig;
        init(true);
    }

    public boolean isOk() {
        return ok;
    }

    public NewsletterFactory.NEWSLETTER_TYPE getNewsletterType() {
        return newsletterType;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public String getDestPath() {
        return destPath;
    }

    public String getDestFile() {
        return destFile;
    }

    @Override
    protected void make() {
        this.stage = clubConfig.getStage();
        btnHelp = PButton.helpButton(getStage(), "Serienbrief erstellen",
                "Hier kann aus einer Serienbrief-Vorlage mit den " +
                        "aktuellen Daten der Mitglieder ein Serienbrief erstellt " +
                        "werden.");

        addOkCancelButtons(btnOk, btnCancel);
        ButtonBar.setButtonData(btnHelp, ButtonBar.ButtonData.HELP);
        addAnyButton(btnHelp);

        btnOk.setOnAction(a -> {
            if (check()) {
                ok = true;
                close();
            }
        });
        btnCancel.setOnAction(a -> close());

        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        pCboTemplate.setMaxWidth(Double.MAX_VALUE);
        pCboNewsletterFile.setMaxWidth(Double.MAX_VALUE);
        pCboNewsletterPath.setMaxWidth(Double.MAX_VALUE);

        int row = 0;
        gridPane.add(new Label("Vorlge:"), 0, row);
        gridPane.add(pCboTemplate, 1, row);
        gridPane.add(btnTemplate, 2, row);

        gridPane.add(new Label(" "), 0, ++row);

        gridPane.add(new Label("Newsletter speichern"), 0, ++row, 2, 1);
        gridPane.add(new Label("Pfad:"), 0, ++row);
        gridPane.add(pCboNewsletterPath, 1, row);
        gridPane.add(btnNewsletterPath, 2, row);

        gridPane.add(new Label("Dateiname:"), 0, ++row);
        gridPane.add(pCboNewsletterFile, 1, row);
        gridPane.add(btnProposeFileName, 2, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());

        vBoxCont.getChildren().add(gridPane);
        getVBoxCont().getChildren().add(vBoxCont);

        initListener();
    }

    private void initListener() {
        pCboTemplate.init(clubConfig.CBO_LIST_NEWSLETTER_TEMPLATE, clubConfig.NEWSLETTER_TEMPLATE);
        pCboNewsletterPath.init(clubConfig.CBO_LIST_NEWSLETTER_DIR, clubConfig.NEWSLETTER_DIR);
        pCboNewsletterFile.init(clubConfig.CBO_LIST_NEWSLETTER_FILE, clubConfig.NEWSLETTER_FILE);

        btnTemplate.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnTemplate.setOnAction(event -> {
            PDirFileChooser.FileChooser(getStage(), pCboTemplate,
                    ProgInfos.getClubDirectory(clubConfig.getClubPath()).toString(),
                    ProgInfos.getClubDirectory(clubConfig.getClubPath()).toString());
        });

        btnNewsletterPath.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnNewsletterPath.setTooltip(new Tooltip("Pfad auswählen"));
        btnNewsletterPath.setOnAction(event -> PDirFileChooser.DirChooser(getStage(), pCboNewsletterPath,
                ProgInfos.getClubDataLetterFile(clubConfig.getClubPath()).toString()));

        btnProposeFileName.setGraphic(new ProgIcons().ICON_BUTTON_GUI_GEN_NAME);
        btnProposeFileName.setTooltip(new Tooltip("einen Dateinamen vorschlagen"));
        btnProposeFileName.setOnAction(event -> {
            String fileName = clubConfig.NEWSLETTER_FILE.getValueSafe();

            if (!setSourceFile()) {
                return;
            }

            if (fileName.isEmpty()) {
                fileName = "Serienbrief";
            }

            final String suffix = NewsletterFactory.getSuffix(newsletterType);
            final String destPath = pCboNewsletterPath.getSelValue();
            clubConfig.NEWSLETTER_FILE.setValue(PFileName.getNextFileNameWithDate(destPath, fileName, suffix));
        });

        btnOk.disableProperty().bind(pCboTemplate.getSelectionModel().selectedItemProperty().isNull()
                .or(pCboNewsletterPath.getSelectionModel().selectedItemProperty().isNull())
                .or(pCboNewsletterFile.getSelectionModel().selectedItemProperty().isNull())
        );

    }

    private boolean check() {
        if (!setSourceFile() || !setDestPath()) {
            return false;
        }

        return true;
    }

    private boolean setSourceFile() {
        final String src = pCboTemplate.getSelValue();
        sourceFile = ClubFactory.getAbsolutFilePath(clubConfig, getStage(), src);
        newsletterType = NewsletterFactory.getType(sourceFile);

        if (sourceFile.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean setDestPath() {
        destPath = pCboNewsletterPath.getSelValue();
        destFile = pCboNewsletterFile.getSelValue();

        if (ClubFactory.getDestinationPath(getStage(), destPath, destFile).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}
