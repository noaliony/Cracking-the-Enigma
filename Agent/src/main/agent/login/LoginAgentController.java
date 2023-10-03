package main.agent.login;

import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.agent.utils.http.HttpClientUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import static main.agent.utils.agent.Constants.*;

public class LoginAgentController {
    @FXML private TextField usernameTextField;
    @FXML private ListView<String> alliesNamesListView;
    @FXML private Slider threadsCountSlider;
    @FXML private TextField tasksCountToPullTextField;
    @FXML private Button loginButton;
    @FXML private Label errorMessageLabel;
    @FXML private Label selectedTeamValueLabel;

    private ObservableList<String> alliesNamesObservableList = FXCollections.observableArrayList();
    private Consumer<String> loginSuccessConsumer;
    private Consumer<String> handleErrorConsumer;
    private StringProperty errorMessageProperty;
    private BooleanProperty isAllyNameExistProperty;
    private Stage primaryStage;
    private Timer getAllAlliesNamesTimer;

    public LoginAgentController(){
        errorMessageProperty = new SimpleStringProperty();
        isAllyNameExistProperty = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        loginButton.setOnAction(this::loginButtonClicked);
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        isAllyNameExistProperty.addListener((observable, oldValue, newValue) -> {
            alliesNamesListView.setItems(alliesNamesObservableList);
        });
        alliesNamesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTeamValueLabel.setText(newValue);
            }
        });
    }

    public void setUP(Consumer<String> loginSuccessConsumer, Consumer<String> handleErrorConsumer) {
        this.loginSuccessConsumer = loginSuccessConsumer;
        this.handleErrorConsumer = handleErrorConsumer;
        getAllAlliesNames();
    }

    public void getAllAlliesNames() {
        getAllAlliesNamesTimer = new Timer("getAllAlliesNamesTimer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sendRequestToGetAllAlliesNames();
            }
        };

        getAllAlliesNamesTimer.scheduleAtFixedRate(timerTask, ZERO, REFRESH_TWO_SECONDS);
    }

    private void sendRequestToGetAllAlliesNames() {
        String finalUrl = HttpUrl
                .parse(GET_ALL_ALLIES_NAMES_PAGE)
                .newBuilder()
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncGet(finalUrl);
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                Platform.runLater(() -> {
                    Type type = new TypeToken<List<String>>() {}.getType();
                    List<String> allAlliesNamesList = GSON_INSTANCE.fromJson(responseBody, type);

                    alliesNamesObservableList.clear();
                    alliesNamesObservableList.addAll(allAlliesNamesList);
                    isAllyNameExistProperty.setValue(true);
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

    @FXML
    void loginButtonClicked(ActionEvent actionEvent) {
        String userName = usernameTextField.getText();
        String allyName = selectedTeamValueLabel.getText();
        String threadsCount = String.valueOf((int) threadsCountSlider.getValue());
        String tasksCountToPull = tasksCountToPullTextField.getText();

        if (userName.isEmpty()) {
            setErrorMessageProperty("User name is empty." + System.lineSeparator() + "You can't login with empty user name");
            return;
        } else if (!isTasksCountValid()) {
            setErrorMessageProperty("Tasks count is invalid." + System.lineSeparator() + "You must enter only numbers");
            return;
        }

        String finalUrl = HttpUrl
                .parse(LOGIN_AGENT_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("allyName", allyName)
                .addQueryParameter("threadsCount", threadsCount)
                .addQueryParameter("tasksCountToPull", tasksCountToPull)
                .addQueryParameter("userType", "agent")
                .build()
                .toString();

        HttpClientUtils.runAsyncGet(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                Platform.runLater(() -> {
                    setErrorMessageProperty(exception.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        loginSuccessConsumer.accept(userName);
                    });

                    getAllAlliesNamesTimer.cancel();
                    getAllAlliesNamesTimer.purge();
                    getAllAlliesNamesTimer = null;
                } else {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                        setErrorMessageProperty(responseBody);
                    });
                }
                response.close();
            }
        });
    }

    private boolean isTasksCountValid() {
        try {
            Integer.parseInt(tasksCountToPullTextField.getText());

            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public void setErrorMessageProperty(String value){
        errorMessageProperty.set(value);
        usernameTextField.clear();
        threadsCountSlider.setValue(threadsCountSlider.getMin());
        tasksCountToPullTextField.clear();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public int getThreadsCount() {
        return (int) threadsCountSlider.getValue();
    }

    public Integer getTasksCount() {
        return Integer.parseInt(tasksCountToPullTextField.getText());
    }

    public String getAllyName() {
        return selectedTeamValueLabel.getText();
    }

    public String getAgentName() {
        return usernameTextField.getText();
    }
}
