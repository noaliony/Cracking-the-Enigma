package main.agent.operation.screen.data.contest.details;

import dto.BattlefieldDetails;
import enums.GameStatus;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.agent.utils.http.HttpClientUtils;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static main.agent.utils.agent.Constants.*;

public class ContestDetailsController {

    @FXML private Label battlefieldNameValueLabel;
    @FXML private Label uBoatNameValueLabel;
    @FXML private Label gameStatusValueLabel;
    @FXML private Label gameLevelValueLabel;
    @FXML private Label registeredAlliesCountValueLabel;
    @FXML private Label totalAlliesCountValueLabel;

    private Consumer<String> handleErrorConsumer;
    private BooleanProperty isDataInitializedProperty;
    private Timer getContestDataTimer;

    public ContestDetailsController() {
        isDataInitializedProperty = new SimpleBooleanProperty(false);
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
    }

    public void getContestData() {
        getContestDataTimer = new Timer("getContestDataTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetContestData();
            }
        };

        getContestDataTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_TWO_SECONDS);
    }

    private void sendRequestToGetContestData() {
        String finalUrl = HttpUrl
                .parse(GET_CONTEST_DATA_PAGE)
                .newBuilder()
                .addQueryParameter("userType", "agent")
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    BattlefieldDetails battlefieldDetails = GSON_INSTANCE.fromJson(responseBody, BattlefieldDetails.class);

                    updateData(battlefieldDetails);
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

    private void updateData(BattlefieldDetails battlefieldDetails) {
        battlefieldNameValueLabel.setText(battlefieldDetails.getBattlefieldName());
        uBoatNameValueLabel.setText(battlefieldDetails.getUBoatName());
        gameStatusValueLabel.setText(battlefieldDetails.getGameStatus().getGameStatusText());
        gameLevelValueLabel.setText(battlefieldDetails.getGameLevel().getDifficultyText());
        totalAlliesCountValueLabel.setText(battlefieldDetails.getTotalAlliesCount().toString());
        registeredAlliesCountValueLabel.setText(battlefieldDetails.getRegisteredAlliesCount().toString());
    }

    public void contestIsOver() {
        gameStatusValueLabel.setText(GameStatus.WAITING.getGameStatusText());
        getContestDataTimer.cancel();
        getContestDataTimer.purge();
        getContestDataTimer = null;
    }

    public void reset() {
        battlefieldNameValueLabel.setText("");
        uBoatNameValueLabel.setText("");
        gameStatusValueLabel.setText("");
        gameLevelValueLabel.setText("");
        totalAlliesCountValueLabel.setText("");
        registeredAlliesCountValueLabel.setText("");
    }
}