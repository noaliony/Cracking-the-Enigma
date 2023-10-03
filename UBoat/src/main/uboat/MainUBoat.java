package main.uboat;

import http.HttpClientUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.uboat.login.LoginUBoatController;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.uboat.utils.uboat.Constants.MAIN_UBOAT_FXML_RESOURCE_LOCATION;

public class MainUBoat extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
            FXMLLoader loader = new FXMLLoader();
            URL mainUBoatFXML = getClass().getResource(MAIN_UBOAT_FXML_RESOURCE_LOCATION);
            loader.setLocation(mainUBoatFXML);
            Parent root = loader.load(loader.getLocation().openStream());
            MainUBoatController mainUBoatController = loader.getController();
            LoginUBoatController loginUBoatController = mainUBoatController.getLoginController();
            loginUBoatController.setPrimaryStage(primaryStage);
            primaryStage.setTitle("Enigma UBoat");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(400);
            primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
                HttpClientUtils.sendLogoutRequest();
                HttpClientUtils.shutdown();
            });
            primaryStage.show();
        } catch (Exception exception) {

        }
    }

}
