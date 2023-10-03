package main.uboat.tabs.screen.machine.details.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import machine.details.MachineDetailsObject;
import http.HttpClientUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

import static java.lang.Integer.parseInt;
import static main.uboat.utils.uboat.Constants.*;

public class MachineDetailsViewController {
    @FXML private Label amountOfRotorsInUseValueLabel;
    @FXML private Label possibleAmountOfRotorsValueLabel;
    @FXML private Label amountOfReflectorsValueLabel;
    @FXML public Label amountOfIsEncodedMessagesValueLabel;

    private Consumer<String> handleErrorConsumer;


    public void setTextAmountOfRotorsInUseLabel(String amountOfRotorsInUseLabel) {
        this.amountOfRotorsInUseValueLabel.setText(amountOfRotorsInUseLabel);
    }

    public void setTextPossibleAmountOfRotorsLabel(String possibleAmountOfRotorsLabel) {
        this.possibleAmountOfRotorsValueLabel.setText(possibleAmountOfRotorsLabel);
    }

    public void setTextAmountOfReflectorsLabel(String amountOfReflectorsLabel) {
        this.amountOfReflectorsValueLabel.setText(amountOfReflectorsLabel);
    }

    public void setTextAmountOfIsEncodedMessagesLabel(String amountOfIsEncodedMessagesValueLabel) {
        this.amountOfIsEncodedMessagesValueLabel.setText(amountOfIsEncodedMessagesValueLabel);
    }

    public void advanceAndSetTextAmountOfIsEncodedMessagesLabelRunnable() {
        Integer amountOfIsEncodedMessages = Integer.parseInt(amountOfIsEncodedMessagesValueLabel.getText());

        amountOfIsEncodedMessages++;
        setTextAmountOfIsEncodedMessagesLabel(amountOfIsEncodedMessages.toString());
    }


    public void updateMachineDetails(MachineDetailsObject machineDetails) {
        setTextAmountOfRotorsInUseLabel(machineDetails.getAmountOfRotorsInUse().toString());
        setTextPossibleAmountOfRotorsLabel(machineDetails.getPossibleAmountOfRotors().toString());
        setTextAmountOfReflectorsLabel(machineDetails.getAmountOfReflectors().toString());
        setTextAmountOfIsEncodedMessagesLabel(machineDetails.getAmountOfIsEncodedMessages().toString());
    }

    public void sendRequestToGetMachineDetails() {
        String finalUrl = HttpUrl
                .parse(GET_MACHINE_DETAILS_PAGE)
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
                String responseBody = response.body() != null ? response.body().string() : null;

                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        MachineDetailsObject machineDetailsObject = GSON_INSTANCE.fromJson(responseBody, MachineDetailsObject.class);

                        updateMachineDetails(machineDetailsObject);
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
