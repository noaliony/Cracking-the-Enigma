package main.uboat.tabs.screen.machine.code.calibration.random;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import http.HttpClientUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

import static main.uboat.utils.uboat.Constants.*;

public class RandomCodeController {

    @FXML private Button randomCode;

    private BooleanProperty isRandomCodeSelected;
    private Runnable setRandomCodeRunnable;
    private Consumer<String> handleErrorConsumer;

    public RandomCodeController(){
        isRandomCodeSelected = new SimpleBooleanProperty(false);
    }

    public BooleanProperty isRandomCodeSelectedProperty() {
        return isRandomCodeSelected;
    }

    public void setUp(Runnable setRandomCodeRunnable){
        this.setRandomCodeRunnable = setRandomCodeRunnable;
    }

    @FXML
    void randomCodeButtonAction(ActionEvent event) {
        isRandomCodeSelected.set(!isRandomCodeSelected.get());
    }

    public void sendRequestToSetRandomCode() {
        String finalUrl = HttpUrl
                .parse(SET_RANDOM_CODE_PAGE)
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
                    Platform.runLater(() -> {
                        setRandomCodeRunnable.run();
                    });
                    response.close();
                }
            }
        });
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
    }
}
