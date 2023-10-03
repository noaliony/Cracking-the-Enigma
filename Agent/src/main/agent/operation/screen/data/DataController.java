package main.agent.operation.screen.data;

import dto.TasksResultsDetails;
import enums.GameStatus;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.agent.operation.screen.data.contest.details.ContestDetailsController;
import main.agent.operation.screen.data.candidates.CandidatesController;
import main.agent.utils.http.HttpClientUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

import static main.agent.utils.agent.Constants.*;

public class DataController {
    @FXML private Label allyNameValueLabel;
    @FXML private ContestDetailsController contestDetailsController;
    @FXML private Label tasksCountInBlockingQueueValueLabel;
    @FXML private Label pulledTasksCountValueLabel;
    @FXML private Label completedTasksCountValueLabel;
    @FXML private Label foundStringsDecryptedCountValueLabel;
    @FXML private CandidatesController candidatesController;

    private Consumer<String> handleErrorConsumer;
    private IntSupplier getTasksCountInBlockingQueueSupplier;
    private Timer getTasksCountInBlockingQueueTimer;
    private Timer getTasksResultsDetailsTimer;

    public void setUp(String allyName, IntSupplier getTasksCountInBlockingQueueSupplier) {
        allyNameValueLabel.setText(allyName);
        this.getTasksCountInBlockingQueueSupplier = getTasksCountInBlockingQueueSupplier;
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
        contestDetailsController.setUpHandleErrorConsumer(handleErrorConsumer);
        candidatesController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    public void setTextTasksCountInBlockingQueueValueLabel(int tasksCountInBlockingQueue) {
        Platform.runLater(() -> tasksCountInBlockingQueueValueLabel.setText(String.valueOf(tasksCountInBlockingQueue)));
    }

    public void getTasksCountInBlockingQueue() {
        getTasksCountInBlockingQueueTimer = new Timer("getTasksCountInBlockingQueueTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                setTextTasksCountInBlockingQueueValueLabel(getTasksCountInBlockingQueueSupplier.getAsInt());
            }
        };

        getTasksCountInBlockingQueueTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_HALF_SECOND);
    }

    public void getTasksResultsDetails() {
        getTasksResultsDetailsTimer = new Timer("getTasksResultsDetailsTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetTasksResultsDetails();
            }
        };

        getTasksResultsDetailsTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_HALF_SECOND);
    }

    private void sendRequestToGetTasksResultsDetails() {
        String finalUrl = HttpUrl
                .parse(GET_TASKS_RESULTS_DETAILS_PAGE)
                .newBuilder()
                .build()
                .toString();
        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    TasksResultsDetails tasksResultsDetails = GSON_INSTANCE.fromJson(responseBody, TasksResultsDetails.class);

                    updateData(tasksResultsDetails);
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

    private void updateData(TasksResultsDetails tasksResultsDetails) {
        pulledTasksCountValueLabel.setText(String.valueOf(tasksResultsDetails.getPulledTasksCount()));
        completedTasksCountValueLabel.setText(String.valueOf(tasksResultsDetails.getCompletedTasksCount()));
        foundStringsDecryptedCountValueLabel.setText(String.valueOf(tasksResultsDetails.getFoundCandidatesCount()));
    }

    public void contestIsActive() {
        contestDetailsController.getContestData();
        getTasksCountInBlockingQueue();
        getTasksResultsDetails();
        candidatesController.getStringDecryptedCandidate();
    }

    public void contestIsOver() {
        contestDetailsController.contestIsOver();
        getTasksCountInBlockingQueueTimer.cancel();
        getTasksCountInBlockingQueueTimer.purge();
        getTasksCountInBlockingQueueTimer = null;
        getTasksResultsDetailsTimer.cancel();
        getTasksResultsDetailsTimer.purge();
        getTasksResultsDetailsTimer = null;
        candidatesController.stopGettingStringDecryptedCandidate();
    }

    public void reset() {
        candidatesController.reset();
        contestDetailsController.reset();
    }
}
