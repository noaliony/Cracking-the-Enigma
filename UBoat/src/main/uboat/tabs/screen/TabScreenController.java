package main.uboat.tabs.screen;

import dto.ConfigurationDetails;
import dto.MachineSetting;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import main.uboat.tabs.screen.contest.ContestController;
import main.uboat.tabs.screen.machine.MachineController;
import http.HttpClientUtils;
import main.uboat.tabs.screen.machine.current.configuration.ConfigurationController;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

import static main.uboat.utils.uboat.Constants.GET_CODE_CONFIGURATION_PAGE;
import static main.uboat.utils.uboat.Constants.GSON_INSTANCE;

public class TabScreenController {
    @FXML private Tab machineTabButton;
    @FXML private TabPane tabPane;
    @FXML private ScrollPane machineTab;
    @FXML private ScrollPane contestTab;
    @FXML private MachineController machineTabController;
    @FXML private ContestController contestTabController;
    @FXML private ConfigurationController currentConfigurationController;

    private BooleanProperty isRandomCodeSelected;
    private Consumer<String> handleErrorConsumer;

    public TabScreenController(){
        isRandomCodeSelected = new SimpleBooleanProperty();
    }

    @FXML
    private void initialize(){

        machineTabController.isRandomCodeSelectedProperty().addListener((observableValue, oldValue, newValue) -> {
            isRandomCodeSelected.set(newValue);
        });
        currentConfigurationController = new ConfigurationController();
        currentConfigurationController.setUp();
        setCurrentConfigurationController();
        contestTabController.setAmountOfIsEncodedMessagesLabelRunnable(this::advanceAndSetTextAmountOfIsEncodedMessagesLabelRunnable);
    }

    private void setCurrentConfigurationController() {
        machineTabController.setCurrentConfigurationController(currentConfigurationController);
        contestTabController.setCurrentConfigurationController(currentConfigurationController);
    }

    public BooleanProperty isRandomCodeSelectedProperty() {
        return isRandomCodeSelected;
    }

    public void setUp(Runnable setRandomCodeRunnable, Runnable onLogoutRequest){
        machineTabController.setUp(setRandomCodeRunnable);
        contestTabController.setUp(this::updateCurrentMachineConfiguration, () -> {
            tabPane.getSelectionModel().select(machineTabButton);
            resetConfigurations();
            machineTabController.reset();
            onLogoutRequest.run();
        });
    }

    public void advanceAndSetTextAmountOfIsEncodedMessagesLabelRunnable() {
        machineTabController.advanceAndSetTextAmountOfIsEncodedMessagesLabel();
    }

    public void sendRequestToGetMachineDetails() {
        machineTabController.sendRequestToGetMachineDetails();
    }

    public void updateCurrentMachineConfiguration(MachineSetting machineSetting) {
        currentConfigurationController.updateCurrentMachineConfiguration(machineSetting);
    }

    private void updateCurrentMachineConfiguration() {
        String finalUrl = HttpUrl
                .parse(GET_CODE_CONFIGURATION_PAGE)
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
                    String bodyResponse = response.body().string();
                    Platform.runLater(() -> {
                        MachineSetting machineSetting = GSON_INSTANCE.fromJson(bodyResponse, MachineSetting.class);

                        updateCurrentMachineConfiguration(machineSetting);
                    });
                }
                response.close();
            }
        });
    }

    public void setUpConfiguration(Consumer<ConfigurationDetails> configurationDetailsConsumer,
                                   Consumer<String> errorMessageConsumer,
                                   Runnable setConfigurationRunnable) {
        machineTabController.setUpConfiguration(configurationDetailsConsumer, errorMessageConsumer,
                setConfigurationRunnable);
    }

    public void updateOriginalMachineConfiguration(MachineSetting machineSetting) {
        machineTabController.updateOriginalMachineConfiguration(machineSetting);
    }

    public void resetConfigurations() {
        machineTabController.resetOriginalConfiguration();
        machineTabController.clearAllConfigurations();
        currentConfigurationController.resetAllLabels();
    }

    public void setUpTabScreen(BooleanProperty isConfigurationExistProperty) {
        contestTab.disableProperty().bind(isConfigurationExistProperty.not());
        tabPane.getSelectionModel().select(0); // Select Machine Tab
    }

    public void sendRequestToSetRandomCode() {
        machineTabController.sendRequestToSetRandomCode();
    }

    public void sendRequestToSetCodeConfiguration(ConfigurationDetails configurationDetails) {
        machineTabController.sendRequestToSetCodeConfiguration(configurationDetails);
    }

    public void sendRequestToGetDictionary() {
        contestTabController.sendRequestToGetDictionary();
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
        machineTabController.setUpHandleErrorConsumer(handleErrorConsumer);
        contestTabController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    public void getActiveTeamsDetails() {
        contestTabController.getActiveTeamsDetails();
    }
}
