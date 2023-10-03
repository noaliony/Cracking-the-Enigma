package main.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import load.file.LoadFileController;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private final String MAIN_APP_FXML = "/main/app/MainApp.fxml";
    private final String LOAD_FILE_FXML = "/load/file/LoadFile.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL mainAppFXML = getClass().getResource(MAIN_APP_FXML);
        loader.setLocation(mainAppFXML);
        Parent root = loader.load(loader.getLocation().openStream());
        MainController mainController = loader.getController();
        LoadFileController loadFileController = mainController.getLoadFileController();
        loadFileController.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Enigma App");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
        primaryStage.show();
    }
}
