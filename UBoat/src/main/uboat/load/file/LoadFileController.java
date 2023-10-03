package main.uboat.load.file;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import http.HttpClientUtils;
import main.uboat.utils.uboat.Constants;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import static main.uboat.utils.uboat.Constants.*;

public class LoadFileController {

    @FXML private Button loadFileButton;
    @FXML private TextField currentLoadedFilePathTextField;

    private Stage primaryStage;
    private StringProperty selectedFilePathProperty;
    private Consumer<String> handleErrorConsumer;

    public LoadFileController(){
        selectedFilePathProperty = new SimpleStringProperty();
    }

    @FXML
    private void initialize() {
        currentLoadedFilePathTextField.textProperty().bind(selectedFilePathProperty);
    }

    public StringProperty selectedFilePathPropertyProperty() {
        return selectedFilePathProperty;
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
        selectedFilePathProperty.set(absolutePath);
    }

    public boolean sendRequestToLoadFile() {
        try {
            String pathFile = currentLoadedFilePathTextField.getText();

            if (pathFile.isEmpty()) {
                handleErrorConsumer.accept("The path file is empty. You can't load file with empty path");
                return false;
            }

            File file = new File(pathFile);

            RequestBody body =
                    new MultipartBody.Builder()
                            .addFormDataPart("xml-file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
                            .build();

            Request request = new Request.Builder()
                    .url(LOAD_FILE_PAGE)
                    .post(body)
                    .build();

            Call call = HttpClientUtils.HTTP_CLIENT.newCall(request);

            Response response = call.execute();
            String responseBody = response.body().string();

            if (response.code() != STATE_OK) {
                handleErrorConsumer.accept(responseBody);

                return false;
            }
            System.out.println(responseBody);
            response.close();

            return true;
        } catch (IOException exception) {
            handleErrorConsumer.accept(exception.getMessage());

            return false;
        }
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
    }
}
