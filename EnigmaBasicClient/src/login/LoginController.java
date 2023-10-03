package login;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.http.HttpClientUtils;
import java.io.IOException;
import java.util.function.Consumer;
import static utils.uboat.Constants.*;

public class LoginController {

    @FXML private TextField userNameTextField;
    @FXML private Button loginButton;
    @FXML private Label errorMessageLabel;

    private Stage primaryStage;
    private StringProperty errorMessageProperty;
    private Consumer<String> loginSuccessConsumer;

    public LoginController(){
        errorMessageProperty = new SimpleStringProperty();
    }

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        loginButton.setOnAction(this::loginButtonClicked);
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
    private void loginButtonClicked(ActionEvent event) {

        String userName = userNameTextField.getText();

        if (userName.isEmpty()) {
            setErrorMessageProperty("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();


        HttpClientUtils.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                Platform.runLater(() -> {
                    setErrorMessageProperty(exception.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != STATE_OK) {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                        setErrorMessageProperty(responseBody);
                    });
                } else {
                    Platform.runLater(() -> {
                        loginSuccessConsumer.accept(userName);
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