package main.allies.tabs.screen.dashboard.contestsData;

import com.google.gson.reflect.TypeToken;
import dto.AgentDetails;
import dto.BattlefieldDetails;
import enums.GameLevel;
import enums.GameStatus;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import main.allies.tabs.screen.contest.details.display.ContestDetailsController;
import main.allies.utils.http.HttpClientUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static main.allies.utils.allies.Constants.*;

public class ContestDataJoinAbleController {

    @FXML private TableView<BattlefieldDetails> contestsDataTableView;
    @FXML private TableColumn<BattlefieldDetails, String> battlefieldNameCol;
    @FXML private TableColumn<BattlefieldDetails, String> uBoatNameCol;
    @FXML private TableColumn<BattlefieldDetails, GameStatus> gameStatusCol;
    @FXML private TableColumn<BattlefieldDetails, GameLevel> gameLevelCol;
    @FXML private TableColumn<BattlefieldDetails, Integer> alliesCountRegisteredCol;
    @FXML private TableColumn<BattlefieldDetails, Integer> totalAlliesCountCol;
    @FXML private VBox selectedContest;
    @FXML private ContestDetailsController selectedContestController;

    private final ObservableList<BattlefieldDetails> battlefieldDetailsObservableList = FXCollections.observableArrayList();
    private final ObjectProperty<BattlefieldDetails> selectedContestObject = new SimpleObjectProperty<>(null);
    private BooleanProperty isCurrentBattlefieldDetailsExist;
    private Consumer<String> handleErrorConsumer;
    private Runnable visibleContestTabRunnable;
    private Consumer<BattlefieldDetails> updateContestDetailsConsumer;
    private Timer getContestsDataListTimer;
    private Runnable onJoinedContest;

    public ContestDataJoinAbleController() {
        this.isCurrentBattlefieldDetailsExist = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        battlefieldNameCol.setCellValueFactory(new PropertyValueFactory<>("battlefieldName"));
        uBoatNameCol.setCellValueFactory(new PropertyValueFactory<>("uBoatName"));
        gameStatusCol.setCellValueFactory(new PropertyValueFactory<>("gameStatus"));
        gameLevelCol.setCellValueFactory(new PropertyValueFactory<>("gameLevel"));
        alliesCountRegisteredCol.setCellValueFactory(new PropertyValueFactory<>("registeredAlliesCount"));
        totalAlliesCountCol.setCellValueFactory(new PropertyValueFactory<>("totalAlliesCount"));

        isCurrentBattlefieldDetailsExist.addListener((observable, oldValue, newValue) -> {
            contestsDataTableView.setItems(battlefieldDetailsObservableList);
        });

        selectedContest.visibleProperty().bind(Bindings.isNotNull(selectedContestObject));

        contestsDataTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedContestController.updateData(newValue);
                selectedContestObject.set(newValue);
            }
        });
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
        selectedContestController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    public void setUp(Consumer<BattlefieldDetails> updateContestDetailsConsumer, Runnable visibleContestTabRunnable, Runnable onJoinedContest) {
        this.updateContestDetailsConsumer = updateContestDetailsConsumer;
        this.visibleContestTabRunnable = visibleContestTabRunnable;
        this.onJoinedContest = onJoinedContest;
    }

    private void sendRequestToJoinContest(String battlefieldName) {
        String finalUrl = HttpUrl
                .parse(JOIN_CONTEST_PAGE)
                .newBuilder()
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(battlefieldName.getBytes()))
                .build();

        Call call = HttpClientUtils.HTTP_CLIENT.newCall(request);

        try (Response response = call.execute()) {
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    BattlefieldDetails battlefieldDetails = GSON_INSTANCE.fromJson(responseBody, BattlefieldDetails.class);

                    updateContestDetailsConsumer.accept(battlefieldDetails);
                });
            } else {
                handleErrorConsumer.accept(responseBody);
            }

        } catch (IOException exception) {
            handleErrorConsumer.accept(exception.getMessage());
        }
    }

    @FXML
    void joinButtonClicked(ActionEvent event) {
        BattlefieldDetails battlefieldDetails = selectedContestController.getBattlefieldDetails();

        if (battlefieldDetails != null) {
            if(!battlefieldDetails.getRegisteredAlliesCount().equals(battlefieldDetails.getTotalAlliesCount())) {
                sendRequestToJoinContest(battlefieldDetails.getBattlefieldName());
                visibleContestTabRunnable.run();
                updateContestDetailsConsumer.accept(battlefieldDetails);
                getContestsDataListTimer.cancel();
                getContestsDataListTimer.purge();
                getContestsDataListTimer = null;
                onJoinedContest.run();
            } else {
                selectedContestController.setBattlefieldDetails(null);
                handleErrorConsumer.accept("Error - The contest is full, please choose another contest");
            }
        } else {
            handleErrorConsumer.accept("Error - There is no selection to battlefield");
        }
    }

    public void getContestsDataList() {
        getContestsDataListTimer = new Timer("getContestsDataListTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetContestsDataList();
            }
        };

        getContestsDataListTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_TWO_SECONDS);
    }

    private void sendRequestToGetContestsDataList() {
        String finalUrl = HttpUrl
                .parse(GET_CONTESTS_DATA_LIST_PAGE)
                .newBuilder()
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    Type type = new TypeToken<List<BattlefieldDetails>>() {}.getType();
                    List<BattlefieldDetails> battlefieldDetailsList = GSON_INSTANCE.fromJson(responseBody, type);

                    battlefieldDetailsObservableList.clear();
                    battlefieldDetailsObservableList.addAll(battlefieldDetailsList);
                    isCurrentBattlefieldDetailsExist.setValue(true);
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

    public void reset() {
        selectedContestController.reset();
    }
}
