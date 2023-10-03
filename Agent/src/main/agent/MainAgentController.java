package main.agent;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.agent.login.LoginAgentController;
import main.agent.operation.screen.OperationScreenController;

public class MainAgentController {
    @FXML private LoginAgentController loginAgentController;
    @FXML private VBox loginAgent;
    @FXML private OperationScreenController operationScreenController;
    @FXML private ScrollPane operationScreen;
    @FXML private StackPane agentPanel;
    @FXML private Label userName;

    @FXML
    private void initialize() {
        loginAgent.setVisible(true);
        operationScreen.setVisible(false);
        loginAgentController.setUP(this::login, this::handleError);
        operationScreenController.setUpHandleErrorConsumer(this::handleError);
    }

    public LoginAgentController getLoginController() {
        return loginAgentController;
    }

    private void login(String value){
        System.out.println("Agent: " + value);
        loginAgent.setVisible(false);
        operationScreen.setVisible(true);
        userName.setText("Hello " + value + "! Welcome To Agent Application");
        operationScreenController.setUp(loginAgentController.getThreadsCount(),
                loginAgentController.getTasksCount(), loginAgentController.getAllyName(), loginAgentController.getAgentName());
        operationScreenController.agentLoggedIn();
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
