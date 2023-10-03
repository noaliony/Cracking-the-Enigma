package main.uboat.tabs.screen.contest.game.setting.setter.user.updater;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import http.HttpClientUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.function.Consumer;

import static main.uboat.utils.uboat.Constants.UPDATE_UBOAT_IS_READY_PAGE;

public class UserUpdaterController {

    @FXML private Label originalMessageLabel;
    @FXML private Label messageToEncodeLabel;
    @FXML private Label messageToDecodeLabel;
    @FXML private Button readyButton;
    @FXML private Label isReadyValueLabel;

    private Consumer<String> handleErrorMessage;
    private Runnable getContestStatusRunnable;


    public void setUp(Runnable getContestStatusRunnable) {
        this.getContestStatusRunnable = getContestStatusRunnable;
        readyButton.setOnAction(this::readyButtonAction);
    }

    private void readyButtonAction(ActionEvent actionEvent) {
        sendRequestToUpdateReadyUBoat();
        getContestStatusRunnable.run();
    }

    private void sendRequestToUpdateReadyUBoat() {
        String finalUrl = HttpUrl
                .parse(UPDATE_UBOAT_IS_READY_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtils.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                handleErrorMessage.accept(exception.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> isReadyValueLabel.setText("UBoat Is Ready To Play!"));
                }
                response.close();
            }
        });
    }

    public void clearAllChanges() {
        originalMessageLabel.setText("");
        messageToEncodeLabel.setText("");
        messageToDecodeLabel.setText("");
    }

    public void updateUserAboutMessages(String[] messages) {
        String originalMessage = messages[0];
        String messageToEncode = messages[1];
        String messageToDecode = messages[2];

        originalMessageLabel.setText(originalMessage);
        messageToEncodeLabel.setText(messageToEncode);
        messageToDecodeLabel.setText(messageToDecode);
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorMessage = handleErrorConsumer;
    }

    public void reset() {
        isReadyValueLabel.setText("Not Yet!");
    }
}