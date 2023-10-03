package main.allies.tabs.screen.contest;

import dto.BattlefieldDetails;
import enums.GameStatus;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.allies.tabs.screen.contest.active.teams.details.ActiveTeamsDetailsController;
import main.allies.tabs.screen.contest.agent.work.provider.AgentsWorkProviderController;
import main.allies.tabs.screen.contest.candidates.CandidatesController;
import main.allies.tabs.screen.contest.details.fill.DetailsFillController;
import main.allies.tabs.screen.contest.details.display.ContestDetailsController;
import main.allies.tabs.screen.contest.total.progress.TotalProgressController;
import main.allies.utils.http.HttpClientUtils;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static main.allies.utils.allies.Constants.*;

public class ContestController {
    @FXML private Button leaveContestButton;
    @FXML private DetailsFillController detailsFillController;
    @FXML private ContestDetailsController contestDetailsController;
    @FXML private Label messageToDecodeLabel;
    @FXML private ActiveTeamsDetailsController activeTeamsDetailsController;
    @FXML private CandidatesController candidatesController;
    @FXML private AgentsWorkProviderController agentsWorkProviderController;
    @FXML private TotalProgressController totalProgressController;

    private Consumer<String> handleErrorConsumer;
    private StringProperty gameStatusProperty;
    private Timer getContestStatusTimer;
    private Runnable onLeaveContest;

    public ContestController(){
        gameStatusProperty = new SimpleStringProperty(GameStatus.WAITING.getGameStatusText());
    }

    @FXML
    private void initialize() {
        gameStatusProperty.addListener((observable, oldValue, newValue) -> {
            contestStatusHasChanged(newValue);
        });

        leaveContestButton.setDisable(true);
    }

    private void contestStatusHasChanged(String newValue) {
        if (newValue.equals(GameStatus.READY.getGameStatusText())) {
            contestIsReady();
        } else if (newValue.equals(GameStatus.WAITING.getGameStatusText())) {
            contestIsOver();
        }
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
        detailsFillController.setUpHandleErrorConsumer(handleErrorConsumer);
        contestDetailsController.setUpHandleErrorConsumer(handleErrorConsumer);
        activeTeamsDetailsController.setUpHandleErrorConsumer(handleErrorConsumer);
        agentsWorkProviderController.setUpHandleErrorConsumer(handleErrorConsumer);
        candidatesController.setUpHandleErrorConsumer(handleErrorConsumer);
        totalProgressController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    public void contestJoined() {
        contestDetailsController.getContestData();
        activeTeamsDetailsController.getActiveTeamsDetails();
        getContestStatus();
    }

    public void contestIsReady() {
        sendRequestToGetMessageToDecode();
        totalProgressController.contestIsReady();
        agentsWorkProviderController.getDetailsOfActiveAgentsAndWorkProvider();
        candidatesController.getStringDecryptedCandidate();
    }

    private void sendRequestToGetMessageToDecode() {
        String finalUrl = HttpUrl
                .parse(GET_MESSAGE_TO_DECODE)
                .newBuilder()
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                messageToDecodeLabel.setText(GSON_INSTANCE.fromJson(responseBody, String.class));
            } else {
                handleErrorConsumer.accept(responseBody);
            }
        } catch (IOException exception) {
            handleErrorConsumer.accept(exception.getMessage());
        }
    }

    private void contestIsOver() {
        //sendRequestToStopDMWork();
        leaveContestButton.setDisable(false);
        totalProgressController.contestIsOver();
        getContestStatusTimer.cancel();
        getContestStatusTimer.purge();
        getContestStatusTimer = null;
        contestDetailsController.stopGettingContestData();
        activeTeamsDetailsController.stopGettingActiveTeamsDetails();
        agentsWorkProviderController.stopGettingDetailsOfActiveAgentsAndWorkProvider();
        candidatesController.stopGettingStringDecryptedCandidate();
        detailsFillController.reset();
    }

    public void getContestStatus() {
        getContestStatusTimer = new Timer("getContestStatusTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetContestStatus();
            }
        };

        getContestStatusTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_HALF_SECOND);
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
                GameStatus gameStatus = GSON_INSTANCE.fromJson(responseBody, GameStatus.class);

                Platform.runLater(() -> updateContestStatus(gameStatus));
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

    private void updateContestStatus(GameStatus gameStatus) {
        gameStatusProperty.setValue(gameStatus.getGameStatusText());
    }

    public void updateContestDetails(BattlefieldDetails battlefieldDetails) {
        contestDetailsController.updateData(battlefieldDetails);
    }

    @FXML
    public void leaveContestButtonClicked() {
        sendResetContestRequest();
        onLeaveContest.run();
        leaveContestButton.setDisable(true);
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

    public void setUp(Runnable onLeaveContest) {
        this.onLeaveContest = onLeaveContest;
    }
}
