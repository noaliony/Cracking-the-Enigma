package main.allies.tabs.screen.contest.details.display;

import dto.BattlefieldDetails;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.allies.utils.http.HttpClientUtils;
import okhttp3.HttpUrl;
import okhttp3.Response;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import static main.allies.utils.allies.Constants.*;

public class ContestDetailsController {

    @FXML private Label battlefieldNameValueLabel;
    @FXML private Label uBoatNameValueLabel;
    @FXML private Label gameStatusValueLabel;
    @FXML private Label gameLevelValueLabel;
    @FXML private Label registeredAlliesCountValueLabel;
    @FXML private Label totalAlliesCountValueLabel;

    private Consumer<String> handleErrorConsumer;
    private BattlefieldDetails battlefieldDetails = null;
    private Timer getContestDataTimer;

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
                .addQueryParameter("userType", "ally")
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    if (responseBody != null) {
                        BattlefieldDetails battlefieldDetails = GSON_INSTANCE.fromJson(responseBody, BattlefieldDetails.class);

                        updateData(battlefieldDetails);
                    }
                });
                response.close();
            } else {
                try {
                    handleErrorConsumer.accept(responseBody);
                    Thread.sleep(SLEEP_FIVE_SECONDS, ZERO);
                } catch (InterruptedException exception) {
                    handleErrorConsumer.accept(exception.getMessage());
                }
            }

        } catch (IOException exception) {
            handleErrorConsumer.accept(exception.getMessage());
        }
    }

    public void updateData(BattlefieldDetails battlefieldDetails) {
        this.battlefieldDetails = battlefieldDetails;
        battlefieldNameValueLabel.setText(battlefieldDetails.getBattlefieldName());
        uBoatNameValueLabel.setText(battlefieldDetails.getUBoatName());
        gameLevelValueLabel.setText(battlefieldDetails.getGameLevel().getDifficultyText());
        totalAlliesCountValueLabel.setText(battlefieldDetails.getTotalAlliesCount().toString());
        gameStatusValueLabel.setText(battlefieldDetails.getGameStatus().getGameStatusText());
        registeredAlliesCountValueLabel.setText(battlefieldDetails.getRegisteredAlliesCount().toString());
    }

    public BattlefieldDetails getBattlefieldDetails() {
        return battlefieldDetails;
    }

    public void stopGettingContestData() {
        getContestDataTimer.cancel();
        getContestDataTimer.purge();
        getContestDataTimer = null;
        sendRequestToGetContestData();
    }

    public void reset() {
        battlefieldDetails = null;
        battlefieldNameValueLabel.setText("");
        uBoatNameValueLabel.setText("");
        gameLevelValueLabel.setText("");
        totalAlliesCountValueLabel.setText("");
        gameStatusValueLabel.setText("");
        registeredAlliesCountValueLabel.setText("");
    }

    public void setBattlefieldDetails(BattlefieldDetails battlefieldDetails) {
        this.battlefieldDetails = battlefieldDetails;
    }
}