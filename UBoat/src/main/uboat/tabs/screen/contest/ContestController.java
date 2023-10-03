package main.uboat.tabs.screen.contest;

import dto.StringDecryptedCandidate;
import enums.GameStatus;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.uboat.tabs.screen.contest.active.teams.details.ActiveTeamsDetailsController;
import main.uboat.tabs.screen.contest.candidates.CandidatesController;
import main.uboat.tabs.screen.contest.game.setting.setter.GameSettingSetterController;
import http.HttpClientUtils;
import main.uboat.tabs.screen.contest.winnerMessage.WinnerMessageController;
import main.uboat.tabs.screen.machine.current.configuration.ConfigurationController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static http.HttpClientUtils.*;
import static main.uboat.utils.uboat.Constants.*;

public class ContestController {
    @FXML private Button logoutButton;
    @FXML private Button resetContestButton;
    @FXML private ConfigurationController currentConfigurationController;
    @FXML private GameSettingSetterController gameSettingSetterController;
    @FXML private ActiveTeamsDetailsController activeTeamsDetailsController;
    @FXML private CandidatesController candidatesController;
    @FXML private WinnerMessageController winnerMessageController;

    private Consumer<String> handleErrorConsumer;
    private StringProperty gameStatusProperty;
    private Timer getContestStatusTimer;
    private Runnable onLogoutRequest;

    public ContestController() {
        gameStatusProperty = new SimpleStringProperty(GameStatus.WAITING.getGameStatusText());
    }

    @FXML
    private void initialize() {
        gameStatusProperty.addListener((observable, oldValue, newValue) -> {
            statusContestHasChanged(newValue);
        });
        logoutButton.disableProperty().bind(Bindings.equal(gameStatusProperty, GameStatus.READY.getGameStatusText()));
        resetContestButton.disableProperty().bind(Bindings.equal(gameStatusProperty, GameStatus.READY.getGameStatusText()));
    }

    private void updateStatusContest(GameStatus gameStatus) {
        gameStatusProperty.setValue(gameStatus.getGameStatusText());
    }

    private void statusContestHasChanged(String newValue) {
        if (newValue.equals(GameStatus.READY.getGameStatusText())) {
            contestIsReady();
        } else if (newValue.equals(GameStatus.WAITING.getGameStatusText())) {
            contestIsOver();
        }
    }

    private void contestIsReady() {
        candidatesController.getStringDecryptedCandidate();
    }

    private void contestIsOver() {
        //Maybe - display logout button
        sendRequestToGetWinnerString();
        winnerMessageController.updateVisibleWinnerMessage();
        candidatesController.stopGettingStringDecryptedCandidate();
        gameSettingSetterController.reset();
    }

    public void setCurrentConfigurationController(ConfigurationController configurationController) {
        this.currentConfigurationController.bindToConfigurationController(configurationController);
    }

    public void sendRequestToGetDictionary() {
        gameSettingSetterController.sendRequestToGetDictionary();
    }

    public void setUp(Runnable updateConfigurationRunnable, Runnable onLogoutRequest) {
        gameSettingSetterController.setUp(updateConfigurationRunnable, this::getContestStatus);
        this.onLogoutRequest = () -> {
            onLogoutRequest.run();
            if (getContestStatusTimer != null) {
                getContestStatusTimer.cancel();
                getContestStatusTimer.purge();
                getContestStatusTimer = null;
            }
            activeTeamsDetailsController.stopGettingActiveTeamsDetails();
            candidatesController.stopGettingStringDecryptedCandidate();
        };
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
        gameSettingSetterController.setUpHandleErrorConsumer(handleErrorConsumer);
        activeTeamsDetailsController.setUpHandleErrorConsumer(handleErrorConsumer);
        candidatesController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    public void getActiveTeamsDetails() {
        activeTeamsDetailsController.getActiveTeamsDetails();
    }

    public void getContestStatus() {
        getContestStatusTimer = new Timer("getContestStatusTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetContestStatus();
            }
        };

        getContestStatusTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_TWO_SECONDS);
    }

    public void sendRequestToGetContestStatus() {
        String finalUrl = HttpUrl
                .parse(GET_CONTEST_STATUS_PAGE)
                .newBuilder()
                .build()
                .toString();
        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    GameStatus gameStatus = GSON_INSTANCE.fromJson(responseBody, GameStatus.class);

                    updateStatusContest(gameStatus);
                });
            } else {
                try {
                    handleErrorConsumer.accept(responseBody);
                    Thread.sleep(SLEEP_FIVE_SECONDS, ZERO);
                } catch (InterruptedException exception) {
                    handleErrorConsumer.accept(exception.getMessage());
                }
            }
            response.close();

        } catch (IOException exception) {
            handleErrorConsumer.accept(exception.getMessage());
        }
    }

    public void setAmountOfIsEncodedMessagesLabelRunnable(Runnable amountOfIsEncodedMessagesLabelRunnable) {
        gameSettingSetterController.setAmountOfIsEncodedMessagesLabelRunnable(amountOfIsEncodedMessagesLabelRunnable);
    }

    public void sendRequestToGetWinnerString() {
        String finalUrl = HttpUrl
                .parse(GET_WINNER_STRING_PAGE)
                .newBuilder()
                .build()
                .toString();
        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    StringDecryptedCandidate stringDecryptedCandidate = GSON_INSTANCE.fromJson(responseBody, StringDecryptedCandidate.class);

                    updateWinnerDetails(stringDecryptedCandidate);
                });
            } else {
                try {
                    handleErrorConsumer.accept(responseBody);
                    Thread.sleep(SLEEP_FIVE_SECONDS, ZERO);
                } catch (InterruptedException exception) {
                    handleErrorConsumer.accept(exception.getMessage());
                }
            }
            response.close();

        } catch (IOException exception) {
            handleErrorConsumer.accept(exception.getMessage());
        }
    }

    private void updateWinnerDetails(StringDecryptedCandidate stringDecryptedCandidate) {
        winnerMessageController.updateLabels(stringDecryptedCandidate);
    }

    @FXML
    public void resetContestButtonClicked() {
        sendResetContestRequest();
        candidatesController.reset();
        winnerMessageController.reset();
    }

    private void sendResetContestRequest() {
        String finalUrl = HttpUrl
                .parse(RESET_CONTEST)
                .newBuilder()
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                handleErrorConsumer.accept(responseBody);
            }
            response.close();

        } catch (IOException exception) {
            handleErrorConsumer.accept(exception.getMessage());
        }
    }

    @FXML
    public void logoutButtonClicked() {
        HttpClientUtils.sendLogoutRequest();
        onLogoutRequest.run();
    }
}
