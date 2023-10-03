package main.agent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.agent.login.LoginAgentController;
import main.agent.utils.http.HttpClientUtils;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.agent.utils.agent.Constants.*;

public class MainAgent extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
            FXMLLoader loader = new FXMLLoader();
            URL mainAgentFXML = getClass().getResource(MAIN_AGENT_FXML_RESOURCE_LOCATION);
            loader.setLocation(mainAgentFXML);
            Parent root = loader.load(loader.getLocation().openStream());
            MainAgentController mainAgentController = loader.getController();
            LoginAgentController loginController = mainAgentController.getLoginController();
            loginController.setPrimaryStage(primaryStage);
            primaryStage.setTitle("Enigma Agent");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(400);
            primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
                HttpClientUtils.sendLogoutRequest();
                HttpClientUtils.shutdown();
            });
            primaryStage.show();
        } catch (Exception exception) {}
    }
}
