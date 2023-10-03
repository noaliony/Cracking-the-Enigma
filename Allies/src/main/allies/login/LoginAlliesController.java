package main.allies.login;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.allies.utils.http.HttpClientUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

import static main.allies.utils.allies.Constants.LOGIN_PAGE;


public class LoginAlliesController {

    @FXML private TextField usernameTextField;
    @FXML private Button loginButton;
    @FXML private Label errorMessageLabel;

    private Stage primaryStage;
    private StringProperty errorMessageProperty;
    private Consumer<String> loginSuccessConsumer;

    public LoginAlliesController(){
        errorMessageProperty = new SimpleStringProperty();
    }

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }

    public void setUP(Consumer<String> loginSuccessConsumer) {
        this.loginSuccessConsumer = loginSuccessConsumer;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setErrorMessageProperty(String value){
        errorMessageProperty.set(value);
        usernameTextField.clear();
    }

    @FXML
    void loginButtonClicked(ActionEvent actionEvent) {

        String userName = usernameTextField.getText();

        if (userName.isEmpty()) {
            setErrorMessageProperty("User name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("userType", "allies")
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

    public StringProperty errorMessageProperty() {
        return errorMessageProperty;
    }

    public Label getUserMessageLabel() {
        return errorMessageLabel;
    }
}