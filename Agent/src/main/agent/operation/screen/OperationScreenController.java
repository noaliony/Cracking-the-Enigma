package main.agent.operation.screen;

import com.google.gson.reflect.TypeToken;
import dto.StringDecryptedCandidate;
import enums.GameStatus;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import main.agent.operation.screen.data.DataController;
import main.agent.task.SingleTask;
import dto.TaskDetails;
import javafx.application.Platform;
import javafx.fxml.FXML;
import machine.Machine;
import main.agent.operation.screen.xml.MachineBuilder;
import main.agent.utils.http.HttpClientUtils;
import main.agent.operation.screen.winnerMessage.WinnerMessageController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static main.agent.utils.agent.Constants.*;

public class OperationScreenController {

    @FXML private DataController dataController;
    @FXML private WinnerMessageController winnerMessageController;
    @FXML private Label isConnectedToContestValueLabel;
    @FXML private Label statusContestValueLabel;
    @FXML private HBox contestStatusHBox;

    private StringProperty gameStatusProperty;
    private Consumer<String> handleErrorConsumer;
    private ThreadPoolExecutor tasksThreadPool;
    private BlockingQueue<Runnable> tasksBlockingQueue = new LinkedBlockingQueue<>();
    private int threadsCount;
    private Integer tasksCount;
    private String allyName;
    private String agentName;
    private String messageToDecode;
    private Thread pullTasksThread;
    private CountDownLatch cdl;
    private Machine machine;
    private Timer getContestStatusTimer;
    private Timer isResetDataRequestedTimer;
    private Map<Integer, Machine> machineClonesMapForAgents;
    private final BooleanProperty resetDataRequested;
    private Timer checkIfAgentIsConnectedToContestTimer;
    private BooleanProperty isAgentConnectedToContestProperty;

    public OperationScreenController() {
        gameStatusProperty = new SimpleStringProperty(GameStatus.WAITING.getGameStatusText());
        isAgentConnectedToContestProperty = new SimpleBooleanProperty(false);
        resetDataRequested = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        gameStatusProperty.addListener((observable, oldValue, newValue) -> {
            statusContestHasChanged(newValue);
        });
        resetDataRequested.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isResetDataRequestedTimer.cancel();
                isResetDataRequestedTimer.purge();
                isResetDataRequestedTimer = null;
                agentLoggedIn();
                dataController.reset();
                winnerMessageController.reset();
                isConnectedToContestValueLabel.setText("Not Yet!");
                statusContestValueLabel.setText("The Contest Is Not Active");
            }
        });

        isAgentConnectedToContestProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                agentIsConnectedToContest();
            }
        });

        pullTasksThread = new Thread(this::sendRequestToPullTasks, "pullTasksThread");
        pullTasksThread.setDaemon(true);
    }

    public void setUp(int threadsCount, Integer tasksCount, String allyName, String agentName) {
        this.threadsCount = threadsCount;
        this.tasksCount = tasksCount;
        this.allyName = allyName;
        this.agentName = agentName;
        dataController.setUp(allyName, this::getTasksCountInBlockingQueueSupplier);
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
        dataController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    private int getTasksCountInBlockingQueueSupplier() {
        return tasksBlockingQueue.size();
    }

    private void agentIsConnectedToContest() {
        checkIfAgentIsConnectedToContestTimer.cancel();
        checkIfAgentIsConnectedToContestTimer.purge();
        checkIfAgentIsConnectedToContestTimer = null;
        isConnectedToContestValueLabel.setText("Yes!");
        contestStatusHBox.setVisible(true);
        getContestStatus();
    }

    private void sendRequestToGetMachineString() {
        String finalUrl = HttpUrl
                .parse(GET_MACHINE_STRING_PAGE)
                .newBuilder()
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);

            if (response.isSuccessful()) {
                MachineBuilder machineBuilder = new MachineBuilder();

                machine = machineBuilder.createMachineFromInputStream(response.body().byteStream());
                machineClonesMapForAgents = createMachineClonesMapForAgents();
            } else {
                handleErrorConsumer.accept(response.body().string());
            }
        } catch (IOException e) {
            handleErrorConsumer.accept(e.getMessage());
        }
    }

    private Map<Integer, Machine> createMachineClonesMapForAgents() {
        Map<Integer, Machine> result = new HashMap<>();

        for (int i = 1; i <= threadsCount; i++) {
            result.put(i, machine.clone());
        }

        return result;
    }

    private void statusContestHasChanged(String newValue) {
        if (newValue.equals(GameStatus.READY.getGameStatusText())) {
            contestIsReady();
        } else if (newValue.equals(GameStatus.WAITING.getGameStatusText())) {
            contestIsOver();
        }
    }

    private void contestIsReady() {
        contestIsActive();
    }

    private void contestIsActive() {
        statusContestValueLabel.setText("The Contest Is Active");
        sendRequestToGetMachineString();
        sendRequestToGetMessageToDecode();
        dataController.contestIsActive();
        executeTheContest();
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
                messageToDecode = GSON_INSTANCE.fromJson(responseBody, String.class);
            } else {
                handleErrorConsumer.accept(responseBody);
            }
        } catch (IOException exception) {
            handleErrorConsumer.accept(exception.getMessage());
        }
    }

    private void executeTheContest() {
        tasksThreadPool.prestartAllCoreThreads();
        pullTasksThread.start();
    }

    private void sendRequestToPullTasks() {
        while (gameStatusProperty.getValue().equals(GameStatus.READY.getGameStatusText())) {
            String tasksCountString = String.valueOf(tasksCount);
            String finalUrl = HttpUrl
                    .parse(GET_TASKS_DETAILS_PAGE)
                    .newBuilder()
                    .addQueryParameter("tasksCount", tasksCountString)
                    .build()
                    .toString();
            try {
                Response response = HttpClientUtils.runSyncGet(finalUrl);

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Type type = new TypeToken<List<TaskDetails>>() {}.getType();
                    List<TaskDetails> taskDetailsList = GSON_INSTANCE.fromJson(responseBody, type);

                    cdl = new CountDownLatch(taskDetailsList.size()); //for cases tasksCount is smaller than usual
                    taskDetailsList.forEach(taskDetails -> {
                        SingleTask singleTask = new SingleTask(machineClonesMapForAgents, taskDetails, allyName, agentName, messageToDecode,
                                cdl, handleErrorConsumer);

                        tasksBlockingQueue.add(singleTask);
                    });
                    try {
                        cdl.await();
                    } catch (InterruptedException ignored) {

                    }
                } else {
                    try {
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
    }

    private void contestIsOver() {
        sendRequestToGetWinnerString();
        winnerMessageController.updateVisibleWinnerMessage();
        dataController.contestIsOver();
        getContestStatusTimer.cancel();
        getContestStatusTimer.purge();
        getContestStatusTimer = null;
        if (!pullTasksThread.isInterrupted()) {
            pullTasksThread.interrupt();
        }
        tasksThreadPool.shutdown();
        tasksThreadPool.shutdownNow();
        getIsResetDataRequested();
        pullTasksThread = new Thread(this::sendRequestToPullTasks, "pullTasksThread");
        pullTasksThread.setDaemon(true);
    }

    private void getIsResetDataRequested() {
        isResetDataRequestedTimer = new Timer("isResetDataRequestedTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetIsResetDataRequested();
            }
        };

        isResetDataRequestedTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_HALF_SECOND);
    }

    private void sendRequestToGetIsResetDataRequested() {
        String finalUrl = HttpUrl
                .parse(IS_RESET_DATA_REQUESTED)
                .newBuilder()
                .build()
                .toString();
        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    resetDataRequested.set(GSON_INSTANCE.fromJson(responseBody, boolean.class));
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
                Platform.runLater(() -> {
                    GameStatus gameStatus;

                    gameStatus = GSON_INSTANCE.fromJson(responseBody, GameStatus.class);
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

    private void updateStatusContest(GameStatus gameStatus) {
        gameStatusProperty.setValue(gameStatus.getGameStatusText());
    }

    public void agentLoggedIn() {
        tasksThreadPool = new ThreadPoolExecutor(threadsCount, threadsCount, Integer.MAX_VALUE, TimeUnit.SECONDS, tasksBlockingQueue);
        resetDataRequested.set(false);
        checkIfAgentIsConnectedToContest();
    }

    private void checkIfAgentIsConnectedToContest() {
        checkIfAgentIsConnectedToContestTimer = new Timer("checkIfAgentIsConnectedToContestTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToCheckIfAgentIsConnectedToContest();
            }
        };

        checkIfAgentIsConnectedToContestTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_HALF_SECOND);
    }

    public void sendRequestToCheckIfAgentIsConnectedToContest() {
        String finalUrl = HttpUrl
                .parse(CHECK_IF_AGENT_IS_CONNECTED_TO_CONTEST_PAGE)
                .newBuilder()
                .build()
                .toString();
        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    Boolean isAgentConnectedToContest = GSON_INSTANCE.fromJson(responseBody, Boolean.class);

                    isAgentConnectedToContestProperty.setValue(isAgentConnectedToContest);
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
}
