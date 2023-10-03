package main.allies.tabs.screen.contest.total.progress;

import com.google.gson.Gson;
import dto.StringDecryptedCandidate;
import enums.GameStatus;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.allies.tabs.screen.contest.total.progress.winnerMessage.WinnerMessageController;
import main.allies.utils.http.HttpClientUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static main.allies.utils.allies.Constants.*;

public class TotalProgressController {
    @FXML private WinnerMessageController winnerMessageController;

    @FXML private Label tasksCountValueLabel;
    @FXML private Label createdTasksCountValueLabel;
    @FXML private Label completedTasksCountValueLabel;

    private StringProperty gameStatusProperty;
    private Consumer<String> handleErrorConsumer;
    private Timer getCreatedTasksCountTimer;
    private Timer getCompletedTasksCountTimer;

    public TotalProgressController(){
        gameStatusProperty = new SimpleStringProperty(GameStatus.WAITING.getGameStatusText());
    }

    public void contestIsReady() {
        sendRequestToGetTasksCount();
        getCreatedTasksCount();
        getCompletedTasksCount();
    }

    public void getCreatedTasksCount() {
        getCreatedTasksCountTimer = new Timer("getCreatedTasksCountTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetCreatedTasksCount();
            }
        };

        getCreatedTasksCountTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_HALF_SECOND);
    }

    private void sendRequestToGetCreatedTasksCount() {
        String finalUrl = HttpUrl
                .parse(GET_CREATED_TASKS_COUNT_PAGE)
                .newBuilder()
                .build()
                .toString();
        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    int createdTasksCount = new Gson().fromJson(responseBody, Integer.class);

                    createdTasksCountValueLabel.setText(Integer.toString(createdTasksCount));
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

    public void getCompletedTasksCount() {
        getCompletedTasksCountTimer = new Timer("getCompletedTasksCountTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetCompletedTasksCount();
            }
        };

        getCompletedTasksCountTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_HALF_SECOND);
    }

    private void sendRequestToGetCompletedTasksCount() {
        String finalUrl = HttpUrl
                .parse(GET_COMPLETED_TASKS_COUNT_PAGE)
                .newBuilder()
                .build()
                .toString();
        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    int completedTasksCount = new Gson().fromJson(responseBody, Integer.class);

                    completedTasksCountValueLabel.setText(Integer.toString(completedTasksCount));
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


    private void sendRequestToGetTasksCount() {
        String finalUrl = HttpUrl
                .parse(GET_TASKS_COUNT_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtils.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                handleErrorConsumer.accept(exception.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();

                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        Integer tasksCount = GSON_INSTANCE.fromJson(responseBody, Integer.class);

                        tasksCountValueLabel.setText(tasksCount.toString());
                    });
                }
                response.close();
            }
        });
    }

    public void contestIsOver() {
        //handle end contest, show winner string in specific agent (with others need be empty screen)
        sendRequestToGetWinnerString();
        winnerMessageController.updateVisibleWinnerMessage();
        getCompletedTasksCountTimer.cancel();
        getCompletedTasksCountTimer.purge();
        getCompletedTasksCountTimer = null;
        getCreatedTasksCountTimer.cancel();
        getCreatedTasksCountTimer.purge();
        getCreatedTasksCountTimer = null;
    }
    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
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
}
