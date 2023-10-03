package main.uboat.tabs.screen.contest.active.teams.details;

import com.google.gson.reflect.TypeToken;
import dto.AlliesDetails;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import http.HttpClientUtils;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static main.uboat.utils.uboat.Constants.*;

public class ActiveTeamsDetailsController {

    @FXML private TableView<AlliesDetails> teamsDetailsTableView;
    @FXML private TableColumn<AlliesDetails, String> userNameCol;
    @FXML private TableColumn<AlliesDetails, Integer> agentsCountCol;
    @FXML private TableColumn<AlliesDetails, Integer> taskSizeCol;
    @FXML private TableColumn<AlliesDetails, Boolean> isReadyCol;

    private final ObservableList<AlliesDetails> alliesDetailsObservableList = FXCollections.observableArrayList();
    private BooleanProperty isTeamDetailsExistProperty;
    private Consumer<String> handleErrorConsumer;
    private Timer getActiveTeamsDetailsTimer;

    public ActiveTeamsDetailsController() {
        this.isTeamDetailsExistProperty = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        agentsCountCol.setCellValueFactory(new PropertyValueFactory<>("agentsCount"));
        taskSizeCol.setCellValueFactory(new PropertyValueFactory<>("taskSize"));
        isReadyCol.setCellValueFactory(new PropertyValueFactory<>("isReady"));
        isTeamDetailsExistProperty.addListener((observable, oldValue, newValue) -> {
            teamsDetailsTableView.setItems(alliesDetailsObservableList);
        });
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer){
        this.handleErrorConsumer = handleErrorConsumer;
    }

    public void getActiveTeamsDetails() {
        getActiveTeamsDetailsTimer = new Timer("getActiveTeamsDetailsTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetActiveTeamsDetails();
            }
        };

        getActiveTeamsDetailsTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_TWO_SECONDS);
    }

    private void sendRequestToGetActiveTeamsDetails() {
        String finalUrl = HttpUrl
                .parse(GET_ACTIVE_TEAMS_DETAILS_PAGE)
                .newBuilder()
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    Type type = new TypeToken<List<AlliesDetails>>() {}.getType();
                    List<AlliesDetails> alliesDetailsList = GSON_INSTANCE.fromJson(responseBody, type);

                    alliesDetailsObservableList.clear();
                    alliesDetailsObservableList.addAll(alliesDetailsList);
                    isTeamDetailsExistProperty.setValue(true);
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

    public void stopGettingActiveTeamsDetails() {
        if (getActiveTeamsDetailsTimer != null) {
            getActiveTeamsDetailsTimer.cancel();
            getActiveTeamsDetailsTimer.purge();
            getActiveTeamsDetailsTimer = null;
        }
    }
}

