package main.uboat.tabs.screen.machine.code.calibration.manual;

import dto.ConfigurationDetails;
import javafx.application.Platform;
import enums.ReflectorID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import machine.details.MachineDetailsObject;
import main.uboat.tabs.screen.machine.code.calibration.manual.plug.board.PlugBoardController;
import main.uboat.tabs.screen.machine.code.calibration.manual.reflector.id.ReflectorIDController;
import main.uboat.tabs.screen.machine.code.calibration.manual.rotor.id.RotorIDController;
import main.uboat.tabs.screen.machine.code.calibration.manual.start.position.StartPositionController;
import http.HttpClientUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static main.uboat.utils.uboat.Constants.*;

public class ManualCodeController {

    @FXML private Button rotorsIDButton;
    @FXML private Button startPositionButton;
    @FXML private Button reflectorIDButton;
    @FXML private Button plugBoardButton;
    @FXML private Button setConfigurationButton;
    @FXML private Button clearConfigurationButton;
    @FXML private ScrollPane rotorID;
    @FXML private RotorIDController rotorIDController;
    @FXML private ScrollPane startPosition;
    @FXML private StartPositionController startPositionController;
    @FXML private ScrollPane reflectorID;
    @FXML private ReflectorIDController reflectorIDController;
    @FXML private ScrollPane plugBoard;
    @FXML private PlugBoardController plugBoardController;
    @FXML private StackPane manualConfigurationPanel;

    private Runnable setConfigurationRunnable;
    private Consumer<String> handleErrorConsumer;

    @FXML
    void rotorsIDButtonAction(ActionEvent event) {
        turnOffVisibleToManualConfigurationPanel();
        rotorID.setVisible(true);
    }

    @FXML
    void startPositionButtonAction(ActionEvent event) {
        turnOffVisibleToManualConfigurationPanel();
        startPosition.setVisible(true);
    }

    @FXML
    void reflectorIDButtonAction(ActionEvent event) {
        turnOffVisibleToManualConfigurationPanel();
        reflectorID.setVisible(true);
    }

    @FXML
    void plugBoardButtonAction(ActionEvent event) {
        turnOffVisibleToManualConfigurationPanel();
        plugBoard.setVisible(true);
    }

    private void turnOffVisibleToManualConfigurationPanel(){
        manualConfigurationPanel.getChildren().forEach(child -> child.setVisible(false));
    }

    private ConfigurationDetails getConfigurationDetails() {
        List<Integer> rotorsID = createRotorIDsString();
        List<Character> startPosition = createStartPositionString();
        ReflectorID reflectorID = createReflectorIDString();
        Map<Character, Character> plugPairs = new HashMap<>();
        //Map<Character, Character> plugPairs = createPlugPairsString();

        return new ConfigurationDetails(rotorsID, startPosition, reflectorID, plugPairs);
    }

    private List<Integer> createRotorIDsString() {
        return rotorIDController.createRotorIDsString();
    }

    private List<Character> createStartPositionString() {
        return startPositionController.createStartPositionString();
    }

    private ReflectorID createReflectorIDString() {
        return reflectorIDController.createReflectorIDString();
    }

    private Map<Character, Character> createPlugPairsString() {
        return plugBoardController.createPlugPairsString();
    }

    public void setUpConfiguration(Consumer<ConfigurationDetails> configurationDetailsConsumer,
                                   Consumer<String> handleErrorConsumer,
                                   Runnable setConfigurationRunnable) {
        rotorsIDButtonAction(null);
        setConfigurationButton.setOnAction(event -> {
            try {
                configurationDetailsConsumer.accept(getConfigurationDetails());
            } catch (Exception exception) {
                handleErrorConsumer.accept(exception.getMessage());
            }
        });
        clearConfigurationButton.setOnAction(event -> clearAllConfigurations());
        this.setConfigurationRunnable = setConfigurationRunnable;
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
                String bodyResponse = response.body() != null ? response.body().string() : null;

                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        MachineDetailsObject machineDetailsObject = GSON_INSTANCE.fromJson(bodyResponse, MachineDetailsObject.class);

                        setUpManualCodeComponents(machineDetailsObject);
                    });
                    response.close();
                }
            }
        });
    }



    private void setUpManualCodeComponents(MachineDetailsObject machineDetailsObject) {
        Integer rotorsCount = machineDetailsObject.getAmountOfRotorsInUse();
        Integer rotorsCountOptionally = machineDetailsObject.getPossibleAmountOfRotors();
        Integer reflectorsCount = machineDetailsObject.getAmountOfReflectors();
        List<Character> alphabet = machineDetailsObject.getAlphabet();

        rotorIDController.setUp(rotorsCount, rotorsCountOptionally);
        startPositionController.setUp(rotorsCount, alphabet);
        reflectorIDController.setUp(reflectorsCount);
        plugBoardController.setUp(alphabet);
    }

    public void clearAllConfigurations() {
        rotorIDController.clearConfiguration();
        startPositionController.clearConfiguration();
        reflectorIDController.clearConfiguration();
        plugBoardController.clearConfiguration();
    }

    public void sendRequestToSetCodeConfiguration(ConfigurationDetails configurationDetails) {
        String body = GSON_INSTANCE.toJson(configurationDetails);

        Request request = new Request.Builder()
                .url(SET_MANUAL_CODE_PAGE)
                .post(RequestBody.create(body.getBytes()))
                .build();

        Call call = HttpClientUtils.HTTP_CLIENT.newCall(request);
        try (Response response = call.execute()) {
            if (response.isSuccessful()) {
                setConfigurationRunnable.run();
            } else {
                handleErrorConsumer.accept(response.body().string());
            }
        } catch (IOException e) {
            handleErrorConsumer.accept(e.getMessage());
        }
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
    }
}
