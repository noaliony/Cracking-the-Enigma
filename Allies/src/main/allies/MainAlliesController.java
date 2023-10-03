package main.allies;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import main.allies.login.LoginAlliesController;
import main.allies.tabs.screen.TabScreenController;

public class MainAlliesController {

    @FXML private LoginAlliesController loginAlliesController;
    @FXML private VBox loginAllies;
    @FXML private TabScreenController tabScreenController;
    @FXML private TabPane tabScreen;
    @FXML private Label userName;

    @FXML
    private void initialize() {
        loginAllies.setVisible(true);
        tabScreen.setVisible(false);
        loginAlliesController.setUP(this::login);
        tabScreenController.setUpHandleErrorConsumer(this::handleError);
    }

    private void login(String value){
        System.out.println(value);
        loginAllies.setVisible(false);
        tabScreen.setVisible(true);
        userName.setText("Hello " + value + "! Welcome To Allies Application");
        tabScreenController.allyLoggedIn();
    }

    public LoginAlliesController getLoginController() {
        return loginAlliesController;
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
