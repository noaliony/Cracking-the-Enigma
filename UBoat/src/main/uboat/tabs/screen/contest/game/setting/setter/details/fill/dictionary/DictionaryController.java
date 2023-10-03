package main.uboat.tabs.screen.contest.game.setting.setter.details.fill.dictionary;

import components.Dictionary;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import http.HttpClientUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Set;
import java.util.function.Consumer;

import static main.uboat.utils.uboat.Constants.*;
public class DictionaryController {

    @FXML private TextArea dictionaryTextArea;

    private Consumer<String> handleErrorConsumer;

    public void sendRequestToGetDictionary() {
        String finalUrl = HttpUrl
                .parse(GET_DICTIONARY_PAGE)
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
                    String responseBody = response.body() != null ? response.body().string() : null;

                    Platform.runLater(() -> {
                        Dictionary dictionary = GSON_INSTANCE.fromJson(responseBody, Dictionary.class);
                        Set<String> dictionarySet = dictionary.getDictionarySet();

                        StringBuilder dictionaryString = new StringBuilder();
                        int counter = 1;

                        for (String word : dictionarySet){
                            dictionaryString.append(counter + ". " + word + System.lineSeparator());
                            counter++;
                        }
                        dictionaryTextArea.setText(dictionaryString.toString());
                    });
                    response.close();
                }
//                else {
//                    String responseBody = response.body().string();
//                    Platform.runLater(() -> {
//                        handleErrorConsumer.accept("Something went wrong: " + responseBody);
//                    });
//                }
            }
        });
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
    }
}
