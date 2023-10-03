package load.file;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class LoadFileController {

    @FXML private Button loadFileButton;
    @FXML private TextField currentLoadedFilePathTextField;

    private Stage primaryStage;
    private StringProperty selectedFilePath;

    public LoadFileController(){
        selectedFilePath = new SimpleStringProperty();
    }

    @FXML
    private void initialize() {
    }

    public void setTextCurrentLoadedFilePathTextField() {
        this.currentLoadedFilePathTextField.setText(selectedFilePath.get());
    }

    public StringProperty selectedFilePathProperty() {
        return selectedFilePath;
    }

    @FXML
    private void openFileButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        selectedFilePath.set(absolutePath);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


}
