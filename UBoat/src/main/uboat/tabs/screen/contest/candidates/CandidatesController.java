package main.uboat.tabs.screen.contest.candidates;

import com.google.gson.reflect.TypeToken;
import dto.StringDecryptedCandidate;
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

public class CandidatesController {
    @FXML private TableView<StringDecryptedCandidate> decryptedCandidatesTableView;
    @FXML private TableColumn<StringDecryptedCandidate, String> decryptedMessageCol;
    @FXML private TableColumn<StringDecryptedCandidate, String> allyNameCol;
    @FXML private TableColumn<StringDecryptedCandidate, String> agentNameCol;
    @FXML private TableColumn<StringDecryptedCandidate, String> configurationCol;

    private final ObservableList<StringDecryptedCandidate> stringDecryptedCandidateObservableList = FXCollections.observableArrayList();
    private BooleanProperty isStringDecryptedCandidateExistProperty;
    private Consumer<String> handleErrorConsumer;
    private Timer getStringDecryptedCandidateTimer;

    public CandidatesController() {
        isStringDecryptedCandidateExistProperty = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        decryptedMessageCol.setCellValueFactory(new PropertyValueFactory<>("stringDecrypted"));
        allyNameCol.setCellValueFactory(new PropertyValueFactory<>("allyName"));
        agentNameCol.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        configurationCol.setCellValueFactory(new PropertyValueFactory<>("configurationDetails"));
        isStringDecryptedCandidateExistProperty.addListener((observable, oldValue, newValue) -> {
            decryptedCandidatesTableView.setItems(stringDecryptedCandidateObservableList);
        });
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
    }

    public void getStringDecryptedCandidate() {
        getStringDecryptedCandidateTimer = new Timer("getStringDecryptedCandidateTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetStringDecryptedCandidate();
            }
        };

        getStringDecryptedCandidateTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_HALF_SECOND);
    }

    public void sendRequestToGetStringDecryptedCandidate(){
        String finalUrl = HttpUrl
                .parse(GET_STRING_DECRYPTED_CANDIDATE_PAGE)
                .newBuilder()
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    Type type = new TypeToken<List<StringDecryptedCandidate>>() {}.getType();
                    List<StringDecryptedCandidate> deltaStringDecryptedCandidateList;

                    deltaStringDecryptedCandidateList = GSON_INSTANCE.fromJson(responseBody, type);

                    deltaStringDecryptedCandidateList.forEach(candidate -> {
                        if (!stringDecryptedCandidateObservableList.contains(candidate)) {
                            stringDecryptedCandidateObservableList.add(candidate);
                        }
                    });
                    if (!deltaStringDecryptedCandidateList.isEmpty()) {
                        isStringDecryptedCandidateExistProperty.setValue(true);
                    }
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

    public void stopGettingStringDecryptedCandidate() {
        if (getStringDecryptedCandidateTimer != null) {
            getStringDecryptedCandidateTimer.cancel();
            getStringDecryptedCandidateTimer.purge();
            getStringDecryptedCandidateTimer = null;
            sendRequestToGetStringDecryptedCandidate();
        }
    }

    public void reset() {
        stringDecryptedCandidateObservableList.clear();
    }
}
