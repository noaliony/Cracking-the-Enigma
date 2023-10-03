package tabs.encryptdecrypt.input.processor;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.ToggleSwitch;
import tabs.encryptdecrypt.input.processor.fulltext.mode.FullTextModeController;
import tabs.encryptdecrypt.input.processor.keybykey.mode.KeyByKeyModeController;

import java.util.function.Consumer;

public class InputProcessorController {

    @FXML private ToggleSwitch selectionModeToggleSwitch;
    @FXML private StackPane encryptDecryptPane;
    @FXML private ScrollPane fullTextMode;
    @FXML private FullTextModeController fullTextModeController;
    @FXML private KeyByKeyModeController keyByKeyModeController;
    @FXML private ScrollPane keyByKeyMode;
    @FXML private TextField inputMessageTextField;
    @FXML private TextField isEncodedMessageTextField;

    @FXML
    private void initialize(){
        fullTextMode.visibleProperty().bind(selectionModeToggleSwitch.selectedProperty().not());
        keyByKeyMode.visibleProperty().bind(selectionModeToggleSwitch.selectedProperty());
        fullTextModeController.setFullTextModeClear(event-> cleanTextFieldAndMessageLabel());
       // keyByKeyModeController.setKeyByKeyModeDone(event -> cleanTextFieldAndMessageLabel());

        selectionModeToggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> cleanTextFieldAndMessageLabel());
    }

    public void cleanTextFieldAndMessageLabel() {
        inputMessageTextField.clear();
        isEncodedMessageTextField.setText("");
    }

    public void setUpFullTextModeController(Consumer<String> fullTextModeProcessEvent) {
        fullTextModeController.getProcessButton().setOnAction(event -> fullTextModeProcessEvent.accept(inputMessageTextField.getText()));
    }

    public void setUpKeyByKeyModeController(Consumer<Character> keyByKeyModeProcessEvent) {
        inputMessageTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectionModeToggleSwitch.isSelected() && newValue.length() > oldValue.length()){
                keyByKeyModeProcessEvent.accept(inputMessageTextField.getText().charAt(inputMessageTextField.getText().length() - 1));
            }
        });
    }

    public String getMessageToProcess() {
        return inputMessageTextField.getText();
    }

    public void setTextIsEncodedMessage(String processedMessage) {
        isEncodedMessageTextField.setText(processedMessage);
    }

    public String getTextIsEncodedMessage() {
        return isEncodedMessageTextField.getText();
    }

    public void setKeyByKeyModeDone(EventHandler<ActionEvent> actionEvent) {
        keyByKeyModeController.setKeyByKeyModeDone(actionEvent);
    }

}

