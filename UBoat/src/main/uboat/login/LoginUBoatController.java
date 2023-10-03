package main.uboat.login;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import http.HttpClientUtils;

import java.io.IOException;
import java.util.function.Consumer;

import static main.uboat.utils.uboat.Constants.*;

public class LoginUBoatController {

    @FXML private TextField userNameTextField;
    @FXML private Button loginButton;
    @FXML private Label errorMessageLabel;

    private Stage primaryStage;
    private StringProperty errorMessageProperty;
    private Consumer<String> loginSuccessConsumer;

    public LoginUBoatController(){
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
        userNameTextField.clear();
    }

    @FXML
    void loginButtonClicked(ActionEvent actionEvent) {

        String userName = userNameTextField.getText();

        if (userName.isEmpty()) {
            setErrorMessageProperty("User name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("userType", "uBoat")
                .build()
                .toString();

        HttpClientUtils.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                Platform.runLater(() -> {
                    setErrorMessageProperty("Something went wrong: " + exception.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        loginSuccessConsumer.accept(userName);
                        userNameTextField.clear();
                    });
                } else {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                        setErrorMessageProperty("Something went wrong: " + responseBody);
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