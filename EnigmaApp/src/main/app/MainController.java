package main.app;

import decryption.manager.DMInformationFromUser;
import decryption.manager.TaskResult;
import machine.details.ConfigurationDetails;
import engine.Engine;
import exceptions.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import load.file.LoadFileController;
import machine.details.MachineDetailsObject;
import machine.details.MachineSetting;
import tabs.screen.TabScreenController;

import java.util.List;

public class MainController {

    @FXML private LoadFileController loadFileController;
    @FXML private TabScreenController tabScreenController;
    @FXML private TabPane tabScreen;

    private BooleanProperty isFileLoaded;
    private BooleanProperty isConfigurationExist;
    private Engine engine;

    public MainController(){
        isFileLoaded = new SimpleBooleanProperty(false);
        isConfigurationExist = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {

        engine = new Engine();
        tabScreen.disableProperty().bind(isFileLoaded.not());
        loadFileController.selectedFilePathProperty().addListener((observableValue, oldValue, newValue) -> {
           loadXMLFileToMachine(newValue);
        });
        tabScreenController.isRandomCodeSelectedProperty().addListener((observable, oldValue, newValue) -> {
            getRandomCode();
        });
        tabScreenController.setKeyByKeyModeDone(event -> doneButtonAction());
        tabScreenController.setUpFullTextModeController(this::getProcessedMessage);
        tabScreenController.setUpKeyByKeyModeController(this::getKeyProcessed);
        tabScreenController.setUpResetConfigurationButton(event -> resetConfiguration());
    }

    private void resetConfiguration() {
        if (engine.isCodeSet()) {
            engine.resetCurrentCode();
            updateCurrentMachineConfiguration();
        }
    }

    private void updateMachineDetails() {
        MachineDetailsObject machineDetails = engine.getMachineDetails();

        tabScreenController.updateMachineDetails(machineDetails);
    }

    private void loadXMLFileToMachine(String selectedFilePath){
        try {
            engine.loadXMLFileToMachine(selectedFilePath);
            loadFileController.setTextCurrentLoadedFilePathTextField();
            updateMachineDetails();
            if (isFileLoaded.get()) {
                resetConfigurationsAndHistory();
            }
            isFileLoaded.setValue(true);
            setUpConfiguration();
            isConfigurationExist.set(false);
            tabScreenController.setUpTabScreen(isConfigurationExist);
            tabScreenController.setUpBruteForce(engine, this::handleError, this::updateUserAboutMessagesAndTasksCount);
        } catch (EnigmaLogicException exception) {
            handleError(exception.getMessage());
        }
    }

    private void resetConfigurationsAndHistory() {
        tabScreenController.resetConfigurationsAndHistory();
    }

    private void setUpConfiguration() {
        int rotorsCount = engine.getRotorsCount();
        int rotorsCountOptionally = engine.getAmountRotorsInRepository();
        int reflectorsCount = engine.getAmountReflectorsInRepository();
        List<Character> alphabet = engine.getAlphabet();

        tabScreenController.setUpConfiguration(rotorsCount, rotorsCountOptionally, reflectorsCount, alphabet,
                this::manualSetConfiguration, this::handleError);
    }

    private void manualSetConfiguration(ConfigurationDetails configurationDetails){
        engine.setMachineSetting(configurationDetails.getRotorIDs(), configurationDetails.getStartPosition(),
                configurationDetails.getReflectorID(), configurationDetails.getPlugPairs());
        engine.updateManualSetting();
        isConfigurationExist.set(true);
        updateOriginalAndCurrentMachineSettingString();
    }

    private void updateCurrentMachineConfiguration(){
        MachineSetting machineSetting = engine.getCurrentMachineSetting();

        tabScreenController.updateCurrentMachineConfiguration(machineSetting);
    }

    private void getRandomCode(){
        engine.updateAutomaticSetting();
        isConfigurationExist.set(true);
        updateOriginalAndCurrentMachineSettingString();
    }

    private void updateOriginalAndCurrentMachineSettingString(){
        updateOriginalMachineConfiguration();
        updateCurrentMachineConfiguration();
    }

    private void updateOriginalMachineConfiguration() {
        MachineSetting machineSetting = engine.getCurrentMachineSetting();

        tabScreenController.updateOriginalMachineConfiguration(machineSetting);
    }

    private void getProcessedMessage(String messageToProcess) {
        try {
            String processedMessage = engine.encodingStringInput(messageToProcess, false);

            tabScreenController.setTextProcessedMessage(processedMessage);
            updateCurrentMachineConfiguration();
            addConfigurationHistoryViewToMachineHistory();
        } catch (Exception e) {
            handleError(e.getMessage());
            tabScreenController.cleanTextFieldAndMessageLabel();
        }
    }

    private void addConfigurationHistoryViewToMachineHistory() {
        tabScreenController.addConfigurationHistoryViewToMachineHistory(engine.getCurrentMachineHistory());
    }

    private void getKeyProcessed(Character keyToProcess) {
        try {
            String processedKey = engine.encodingStringInput(keyToProcess.toString(), true);

            tabScreenController.setTextProcessedMessage(tabScreenController.getTextIsEncodedMessageLabel().concat(processedKey));
            updateCurrentMachineConfiguration();
        } catch (Exception e) {
            handleError(e.getMessage());
        }
    }

    public LoadFileController getLoadFileController() {
        return loadFileController;
    }

    private void doneButtonAction(){
        engine.insertCurrentProcessedStringToMachineHistory();
        tabScreenController.cleanTextFieldAndMessageLabel();
        addConfigurationHistoryViewToMachineHistory();
    }

    private void updateUserAboutMessagesAndTasksCount(DMInformationFromUser informationFromUser){
        try {
            int tasksCount = engine.calculateTasksCount(informationFromUser);

            informationFromUser.setTasksCount(tasksCount);
            tabScreenController.updateUserAboutMessagesAndTasksCount(informationFromUser);
            updateCurrentMachineConfiguration();
        } catch (MessageToEncodeIsNotValidException exception) {
            handleError(exception.getMessage());
        }
    }

    private void handleError(String errorMessage) {
        Alert errorPopUp = new Alert(Alert.AlertType.ERROR);

        errorPopUp.setTitle("Error");
        //errorPopUp.initStyle(StageStyle.UNDECORATED);
        errorPopUp.setHeaderText("Error");
        errorPopUp.setContentText(errorMessage);
        errorPopUp.showAndWait();
    }
}