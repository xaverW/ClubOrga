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

package de.p2tools.cluborga.gui;

import de.p2tools.cluborga.config.club.ClubConfig;
import de.p2tools.cluborga.config.prog.ProgIcons;
import de.p2tools.cluborga.controller.RunEvent;
import de.p2tools.cluborga.controller.RunListener;
import de.p2tools.p2lib.guitools.Listener;
import de.p2tools.p2lib.tools.PStringUtils;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class StatusBarController extends AnchorPane {

    StackPane stackPane = new StackPane();

    // textPane
    private final AnchorPane textPane = new AnchorPane();
    private final Label lblLeft = new Label();
    private final Label lblRight = new Label();

    // workerPane
    private final AnchorPane workerPane = new AnchorPane();
    private final Label lblProgress = new Label();
    private final ProgressBar progressBar = new ProgressBar(1);
    private final Button btnStop = new Button("");

    private boolean running = false;
    private final ClubConfig clubConfig;

    private GuiFactory.PANE selPane = GuiFactory.PANE.CLUB;

    public StatusBarController(ClubConfig clubConfig) {
        this.clubConfig = clubConfig;

        getChildren().addAll(stackPane);
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane, 0.0);

        // textPane
        HBox hBox = getHbox();
        lblLeft.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblLeft, Priority.ALWAYS);

        hBox.getChildren().addAll(lblLeft, lblRight);
        textPane.getChildren().add(hBox);

        // workerPane
        hBox = getHbox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        progressBar.setPrefWidth(200);
        hBox.getChildren().addAll(lblProgress, progressBar, btnStop);
        workerPane.getChildren().add(hBox);

        textPane.getStyleClass().add("statusbar");
        workerPane.getStyleClass().add("statusbar");
        stackPane.getChildren().addAll(textPane, workerPane);
        textPane.toFront();

        make();
    }

    private HBox getHbox() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(2, 5, 2, 5));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        AnchorPane.setLeftAnchor(hBox, 0.0);
        AnchorPane.setBottomAnchor(hBox, 0.0);
        AnchorPane.setRightAnchor(hBox, 0.0);
        AnchorPane.setTopAnchor(hBox, 0.0);
        return hBox;
    }


    private void make() {
        btnStop.setGraphic(new ProgIcons().ICON_BUTTON_STOP);
        btnStop.setOnAction(e -> clubConfig.worker.setStop());
        clubConfig.worker.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                if (runEvent.nixLos()) {
                    running = false;
                } else {
                    running = true;
                }
                updateProgressBar(runEvent);
                setStatusbar();
            }
        });

        Listener.addListener(new Listener(Listener.EVENT_TIMER, StatusBarController.class.getSimpleName()) {
            @Override
            public void ping() {
                try {
                    if (!running) {
                        setStatusbar();
                    }
                } catch (final Exception ex) {
                    PLog.errorLog(936251087, ex);
                }
            }
        });
    }

    private synchronized void updateProgressBar(RunEvent event) {
        int max = event.getMax();
        int progress = event.getProgress();
        double prog = 1.0;
        if (max > 0) {
            prog = (1.0 * progress) / max;
        }

        lblProgress.setText(event.getText());
        progressBar.setProgress(prog);
    }

    private void setStatusbar() {
        setSelPane(selPane);
    }

    public void setSelPane(GuiFactory.PANE selPane) {
        this.selPane = selPane;
        if (running) {
            workerPane.toFront();
            return;
        }

        textPane.toFront();
        switch (selPane) {
            case MEMBER:
            case FEE_RATE:
            case FEE_PAYMENT_TYPE:
            case MEMBER_STATE:
                lblLeft.setText(getMemberText());
                lblRight.setText("");
                break;
            case FEE:
                lblLeft.setText(getFeeText());
                lblRight.setText("");
                break;
            case FINANCE:
            case FINANCE_CALCULATION:
            case FINANCE_ACCOUNT:
            case FINANCE_CATEGORY:
                lblLeft.setText(getFinanceText());
                lblRight.setText("");
                break;
            case CLUB:
            case CLUB_INFO:
            default:
                lblLeft.setText("");
                lblRight.setText("");
                break;
        }
    }

    private String getMemberText() {
        final int all = clubConfig.memberDataList.size();
        final int sel = clubConfig.memberDataList.getFilteredList().size();
        final String text;
        if (sel == all) {
            text = PStringUtils.getFormattedString(all) + " Mitglieder";
        } else {
            text = PStringUtils.getFormattedString(sel) + " von " + PStringUtils.getFormattedString(all) + " Mitglieder";
        }
        return text;
    }

    private String getFeeText() {
        final int all = clubConfig.feeDataList.size();
        final int sel = clubConfig.feeDataList.getFilteredList().size();
        final String text;
        if (sel == all) {
            text = PStringUtils.getFormattedString(all) + " Beiträge";
        } else {
            text = PStringUtils.getFormattedString(sel) + " von " + PStringUtils.getFormattedString(all) + " Beiträge";
        }
        return text;
    }

    private String getFinanceText() {
        final int all = clubConfig.financeDataList.size();
        final int sel = clubConfig.financeDataList.getFilteredList().size();
        final String text;
        if (sel == all) {
            text = PStringUtils.getFormattedString(all) + " Finanzen";
        } else {
            text = PStringUtils.getFormattedString(sel) + " von " + PStringUtils.getFormattedString(all) + " Finanzen";
        }
        return text;
    }
}
