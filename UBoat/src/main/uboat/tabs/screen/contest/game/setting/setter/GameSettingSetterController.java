package main.uboat.tabs.screen.contest.game.setting.setter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import main.uboat.tabs.screen.contest.game.setting.setter.details.fill.DetailsFillController;
import main.uboat.tabs.screen.contest.game.setting.setter.user.updater.UserUpdaterController;
import http.HttpClientUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

import static main.uboat.utils.uboat.Constants.*;

public class GameSettingSetterController {

    @FXML private HBox detailsFill;
    @FXML private DetailsFillController detailsFillController;
    @FXML private HBox userUpdater;
    @FXML private UserUpdaterController userUpdaterController;

    private Consumer<String> handleErrorConsumer;
    private Runnable updateConfigurationRunnable;

    @FXML
    private void initialize() {
        detailsFillController.setUp(this::updateUserAboutMessages);
    }

    public void setUp(Runnable updateConfigurationRunnable, Runnable getContestStatusRunnable) {
        this.updateConfigurationRunnable = updateConfigurationRunnable;
        userUpdaterController.setUp(getContestStatusRunnable);
    }

    public void sendRequestToGetDictionary() {
        detailsFillController.sendRequestToGetDictionary();
        userUpdater.setVisible(false);
    }

    public void clearAllChanges() {
        detailsFillController.clearAllChanges();
        userUpdaterController.clearAllChanges();
        userUpdater.setVisible(false);
    }

    public void updateUserAboutMessages(String originalMessage) {
        sendRequestToValidatorAndEncodingMessage(originalMessage, userUpdaterController::updateUserAboutMessages);
        updateConfigurationRunnable.run();
        userUpdater.setVisible(true);

    }

    private void sendRequestToValidatorAndEncodingMessage(String originalMessage,
                                                          Consumer<String[]> updateMessagesConsumer) {
        String finalUrl = HttpUrl
                .parse(VALIDATOR_AND_ENCODING_MESSAGE_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtils.runAsyncPost(finalUrl, originalMessage, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                Platform.runLater(()-> handleErrorConsumer.accept(exception.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String responseBodyString = responseBody.string();

                    Platform.runLater(() -> {
                        String[] messages = responseBodyString.split(System.lineSeparator());

                        updateMessagesConsumer.accept(messages);
                    });
                } else {
                    String errorMessage = response.body().string();

                    handleErrorConsumer.accept(errorMessage);
                }
                response.close();
            }
        });

    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
        detailsFillController.setUpHandleErrorConsumer(handleErrorConsumer);
        userUpdaterController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    public void setAmountOfIsEncodedMessagesLabelRunnable(Runnable amountOfIsEncodedMessagesLabelRunnable) {
        detailsFillController.setAmountOfIsEncodedMessagesLabelRunnable(amountOfIsEncodedMessagesLabelRunnable);
    }

    public void reset() {
        userUpdaterController.reset();
    }
}
