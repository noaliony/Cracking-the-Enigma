//package main.uboat;
//
//import javafx.beans.property.BooleanProperty;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.control.TabPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.StackPane;
//import javafx.scene.layout.VBox;
//import main.uboat.login.LoginController;
//import main.uboat.load.file.LoadFileController;
//import main.uboat.tabs.screen.TabScreenController;
//import okhttp3.OkHttpClient;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class MainUBoatController {
//    @FXML private LoginController loginUBoatController;
//    @FXML private LoadFileController loadFileController;
//    @FXML private TabScreenController tabScreenController;
//    @FXML private VBox setterSettingVBox;
//    @FXML private VBox loginUBoat;
//    @FXML private VBox mainUBoat;
//    @FXML private HBox loadFile;
//    @FXML private StackPane mainUBoatPanel;
//    @FXML private TabPane tabScreen;
//
//    private BooleanProperty isFileLoaded;
//    private BooleanProperty isLoginSucceeded;
//    private BooleanProperty isConfigurationExist;
//    private OkHttpClient client = new OkHttpClient();
//
//    public MainUBoatController(){
//        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
//        isFileLoaded = new SimpleBooleanProperty(false);
//        isConfigurationExist = new SimpleBooleanProperty(false);
//        isLoginSucceeded = new SimpleBooleanProperty(false);
//    }
//
//    @FXML
//    private void initialize(){
//        loadFile.visibleProperty().bind(isLoginSucceeded);
//        tabScreen.visibleProperty().bind(isFileLoaded);
//        loginUBoatController.setUP(this::loginUBoat);
//        loadFileController.selectedFilePathProperty().addListener((observableValue, oldValue, newValue) -> {
//            loadXMLFileToMachine(newValue);
//        });
//        tabScreenController.isRandomCodeSelectedProperty().addListener((observable, oldValue, newValue) -> {
//            getRandomCode();
//        });
//        tabScreenController.setUpTabScreen(isConfigurationExist);
//
//    }
//
//    private void loadComponent(String URL, int indexInVBox) {
//        FXMLLoader loader = new FXMLLoader();
//        URL URL_FXML = LoginController.class.getResource(URL);
//        loader.setLocation(URL_FXML);
//        try {
//            Parent root = loader.load(loader.getLocation().openStream());
//
//            if (indexInVBox == -1)
//                mainUBoatPanel.getChildren().add(0, root);
//            else
//                mainUBoat.getChildren().add(indexInVBox, root);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void loginUBoat(String newValue) {
//        loginUBoatController.setErrorMessageProperty("");
//        loginUBoat.setVisible(false);
//        setterSettingVBox.setVisible(true);
//        isLoginSucceeded.set(true);
//    }
//
//    private void getRandomCode() {
//    }
//
//    private void loadXMLFileToMachine(String newValue) {
//
//        boolean isFileLoadedSuccess = loadFileController.sentRequestToLoadFile();
//
//        if (isFileLoadedSuccess) {
//            isFileLoaded.set(true);
//            loginUBoat.setVisible(false);
//            setterSettingVBox.setVisible(true);
//        }
//
//    }
//
//    public LoginController getLoginController() {
//        return loginUBoatController;
//    }
//
//}
package main.uboat;

import dto.ConfigurationDetails;
import dto.MachineSetting;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.uboat.load.file.LoadFileController;
import main.uboat.login.LoginUBoatController;
import main.uboat.tabs.screen.TabScreenController;
import http.HttpClientUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import static main.uboat.utils.uboat.Constants.*;

public class MainUBoatController {
    @FXML private LoginUBoatController loginUBoatController;
    @FXML private LoadFileController loadFileController;
    @FXML private TabScreenController tabScreenController;
    @FXML private HBox loadFile;
    @FXML private VBox loginUBoat;
    @FXML private TabPane tabScreen;
    @FXML private Label userName;

    private BooleanProperty isFileLoadedProperty;
    private BooleanProperty isConfigurationExistProperty;


    public MainUBoatController() {
        this.isFileLoadedProperty = new SimpleBooleanProperty(false);
        this.isConfigurationExistProperty = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize(){
        loginUBoat.setVisible(true);
        loadFile.setVisible(false);
        loginUBoatController.setUP(this::login);
        tabScreen.visibleProperty().bind(isFileLoadedProperty);
        loadFileController.selectedFilePathPropertyProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                loadXMLFileToMachine();
            }
        });
        tabScreenController.isRandomCodeSelectedProperty().addListener((observable, oldValue, newValue) -> {
            sendRequestToSetRandomCode();
        });
        loadFileController.setUpHandleErrorConsumer(this::handleError);
        tabScreenController.setUpHandleErrorConsumer(this::handleError);
        tabScreenController.setUpTabScreen(isConfigurationExistProperty);
        tabScreenController.setUp(this::setConfigurationRunnable, () -> {
            loginUBoat.setVisible(true);
            loadFile.setVisible(false);
            isFileLoadedProperty.set(false);
            isConfigurationExistProperty.set(false);
            loadFileController.selectedFilePathPropertyProperty().set("");
        });
        tabScreenController.setUpConfiguration(this::manualSetConfiguration, this::handleError, this::setConfigurationRunnable);
    }

    private void manualSetConfiguration(ConfigurationDetails configurationDetails){
        tabScreenController.sendRequestToSetCodeConfiguration(configurationDetails);
        setConfigurationRunnable();
    }


    private void setConfigurationRunnable(){
        isConfigurationExistProperty.set(true);
        updateOriginalAndCurrentMachineConfiguration();
    }

    private void updateOriginalAndCurrentMachineConfiguration() {
        String finalUrl = HttpUrl
                .parse(GET_CODE_CONFIGURATION_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtils.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                handleError(exception.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String bodyResponse = response.body().string();
                    Platform.runLater(() -> {
                        MachineSetting machineSetting = GSON_INSTANCE.fromJson(bodyResponse, MachineSetting.class);

                        tabScreenController.updateOriginalMachineConfiguration(machineSetting);
                        tabScreenController.updateCurrentMachineConfiguration(machineSetting);
                    });
                }
                response.close();
            }
        });
    }

    private void sendRequestToSetRandomCode() {
        tabScreenController.sendRequestToSetRandomCode();
    }

    private void login(String value){
        System.out.println(value);
        loadFile.setVisible(true);
        loginUBoat.setVisible(false);
        userName.setText("Hello " + value + "! Welcome To UBoat Application");
    }

    public LoginUBoatController getLoginController() {
        return loginUBoatController;
    }

    private void loadXMLFileToMachine() {
        boolean isFileLoadedSuccess = loadFileController.sendRequestToLoadFile();

        if (isFileLoadedSuccess) {
            isFileLoadedProperty.set(true);
            loadFile.setVisible(false);
            tabScreenController.sendRequestToGetMachineDetails();
            tabScreenController.sendRequestToGetDictionary();
            tabScreenController.getActiveTeamsDetails();
            //tabScreen.setVisible(true);
        }
    }

    private void handleError(String errorMessage) {
        if (!errorMessage.isEmpty() && !errorMessage.equals("interrupted")) {
            Platform.runLater(() -> {
                Alert errorPopUp = new Alert(Alert.AlertType.ERROR);

                errorPopUp.setTitle("Error");
                //errorPopUp.initStyle(StageStyle.UNDECORATED);
                errorPopUp.setHeaderText("Error");
                errorPopUp.setContentText(errorMessage);
                errorPopUp.showAndWait();
            });
        }
    }
}
