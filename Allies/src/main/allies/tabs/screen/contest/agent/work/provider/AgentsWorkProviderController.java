package main.allies.tabs.screen.contest.agent.work.provider;

import com.google.gson.reflect.TypeToken;
import dto.TasksResultsDetails;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.allies.utils.http.HttpClientUtils;
import okhttp3.HttpUrl;
import okhttp3.Response;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static main.allies.utils.allies.Constants.*;

public class AgentsWorkProviderController {

    @FXML private TableView<TasksResultsDetails> tasksResultsDetailsTableView;
    @FXML private TableColumn<TasksResultsDetails, String> agentNameCol;
    @FXML private TableColumn<TasksResultsDetails, Integer> tasksCountRegisteredCol;
    @FXML private TableColumn<TasksResultsDetails, Integer> remainingTasksCol;
    @FXML private TableColumn<TasksResultsDetails, Integer> candidatesCountCol;

    private final ObservableList<TasksResultsDetails> tasksResultsDetailsObservableList = FXCollections.observableArrayList();
    private BooleanProperty isTasksResultsExistProperty;
    private Consumer<String> handleErrorConsumer;
    private Timer getDetailsOfActiveAgentsAndWorkProviderTimer;

    public AgentsWorkProviderController() {
        this.isTasksResultsExistProperty = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        agentNameCol.setCellValueFactory(new PropertyValueFactory<>("foundAgentName"));
        tasksCountRegisteredCol.setCellValueFactory(new PropertyValueFactory<>("pulledTasksCount"));
        remainingTasksCol.setCellValueFactory(new PropertyValueFactory<>("remainingTasksCount"));
        candidatesCountCol.setCellValueFactory(new PropertyValueFactory<>("foundCandidatesCount"));
        isTasksResultsExistProperty.addListener((observable, oldValue, newValue) -> {
            tasksResultsDetailsTableView.setItems(tasksResultsDetailsObservableList);
        });
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
    }

    public void getDetailsOfActiveAgentsAndWorkProvider() {
        getDetailsOfActiveAgentsAndWorkProviderTimer = new Timer("getDetailsOfActiveAgentsAndWorkProviderTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetDetailsOfActiveAgentsAndWorkProvider();
            }
        };

        getDetailsOfActiveAgentsAndWorkProviderTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_HALF_SECOND);
    }

    private void sendRequestToGetDetailsOfActiveAgentsAndWorkProvider() {
        String finalUrl = HttpUrl
                .parse(GET_DETAILS_OF_ACTIVE_AGENT_AND_WORK_PROVIDER_PAGE)
                .newBuilder()
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    Type type = new TypeToken<List<TasksResultsDetails>>() {}.getType();
                    List<TasksResultsDetails> tasksResultsDetailsList = GSON_INSTANCE.fromJson(responseBody, type);

                    tasksResultsDetailsObservableList.clear();
                    tasksResultsDetailsObservableList.addAll(tasksResultsDetailsList);
                    isTasksResultsExistProperty.setValue(true);
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

    public void stopGettingDetailsOfActiveAgentsAndWorkProvider() {
        getDetailsOfActiveAgentsAndWorkProviderTimer.cancel();
        getDetailsOfActiveAgentsAndWorkProviderTimer.purge();
        getDetailsOfActiveAgentsAndWorkProviderTimer = null;
        sendRequestToGetDetailsOfActiveAgentsAndWorkProvider();
    }
}
