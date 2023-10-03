package main.allies;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.allies.login.LoginAlliesController;
import main.allies.utils.http.HttpClientUtils;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.allies.utils.allies.Constants.*;

public class MainAllies extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
            FXMLLoader loader = new FXMLLoader();
            URL mainAlliesFXML = getClass().getResource(MAIN_ALLIES_FXML_RESOURCE_LOCATION);
            loader.setLocation(mainAlliesFXML);
            Parent root = loader.load(loader.getLocation().openStream());
            MainAlliesController mainAlliesController = loader.getController();
            LoginAlliesController loginAlliesController = mainAlliesController.getLoginController();
            loginAlliesController.setPrimaryStage(primaryStage);
            primaryStage.setTitle("Enigma Allies");
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


