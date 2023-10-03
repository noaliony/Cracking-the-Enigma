package main.allies.tabs.screen.dashboard.teamsAgentData;

import com.google.gson.reflect.TypeToken;
import dto.AgentDetails;
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

public class TeamsAgentDataController {

    @FXML private TableView<AgentDetails> agentDataTableView;
    @FXML private TableColumn<AgentDetails, String> agentNameCol;
    @FXML private TableColumn<AgentDetails, Integer> threadsCountCol;
    @FXML private TableColumn<AgentDetails, Integer> taskSizeCol;

    private final ObservableList<AgentDetails> AgentDetailsObservableList = FXCollections.observableArrayList();
    private BooleanProperty isTeamsAgentDataExist;
    private Consumer<String> handleErrorConsumer;
    private Timer getTeamsAgentDataTimer;

    public TeamsAgentDataController() {
        this.isTeamsAgentDataExist = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        agentNameCol.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        threadsCountCol.setCellValueFactory(new PropertyValueFactory<>("threadsCount"));
        taskSizeCol.setCellValueFactory(new PropertyValueFactory<>("tasksCountToPull"));
        isTeamsAgentDataExist.addListener((observable, oldValue, newValue) -> {
            agentDataTableView.setItems(AgentDetailsObservableList);
        });
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer){
        this.handleErrorConsumer = handleErrorConsumer;
    }

    public void getTeamsAgentData() {
        getTeamsAgentDataTimer = new Timer("getTeamsAgentDataTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetTeamsAgentData();
            }
        };

        getTeamsAgentDataTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_TWO_SECONDS);
    }

    private void sendRequestToGetTeamsAgentData() {
        String finalUrl = HttpUrl
                .parse(GET_TEAMS_AGENT_DATA_PAGE)
                .newBuilder()
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    Type type = new TypeToken<List<AgentDetails>>() {
                    }.getType();
                    List<AgentDetails> agentDetailsList = GSON_INSTANCE.fromJson(responseBody, type);

                    AgentDetailsObservableList.clear();
                    AgentDetailsObservableList.addAll(agentDetailsList);
                    isTeamsAgentDataExist.setValue(true);
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

    public void stopGettingTeamsAgentData() {
        getTeamsAgentDataTimer.cancel();
        getTeamsAgentDataTimer.purge();
        getTeamsAgentDataTimer = null;
    }
}
