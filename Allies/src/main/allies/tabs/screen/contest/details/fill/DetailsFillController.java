package main.allies.tabs.screen.contest.details.fill;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.function.Consumer;
import main.allies.utils.http.HttpClientUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

import static main.allies.utils.allies.Constants.*;

public class DetailsFillController {
    @FXML private TextField taskSizeTextField;
    @FXML private Button readyButton;
    @FXML private Label isReadyValueLabel;

    private Consumer<String> handleErrorConsumer;

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
    }
	
    @FXML
    void readyButtonAction(ActionEvent event) {
        String taskSize = taskSizeTextField.getText();

        if (Integer.parseInt(taskSize) > 0) {
            sendRequestToUpdateTaskSize(taskSize);
            sendRequestToUpdateReadyAlly();
        } else {
            handleErrorConsumer.accept("Error - Task size is not valid");
            taskSizeTextField.clear();
        }
    }

    private void sendRequestToUpdateReadyAlly() {
        String finalUrl = HttpUrl
                .parse(UPDATE_ALLY_IS_READY_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtils.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                handleErrorConsumer.accept(exception.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> isReadyValueLabel.setText("Ally Is Ready To Play!"));
                } else {
                    handleErrorConsumer.accept(response.body().string());
                }
                response.close();
            }
        });
    }

    private void sendRequestToUpdateTaskSize(String taskSize) {
        String finalUrl = HttpUrl
                .parse(UPDATE_TASK_SIZE_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtils.runAsyncPost(finalUrl, taskSize, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                handleErrorConsumer.accept(exception.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String errorMessage = response.body().string();
                    handleErrorConsumer.accept(errorMessage);
                }
                response.close();
            }
        });
    }

    public void reset() {
        isReadyValueLabel.setText("Not Yet!");
    }
}
