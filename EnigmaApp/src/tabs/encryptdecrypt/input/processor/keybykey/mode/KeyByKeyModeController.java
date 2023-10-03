package tabs.encryptdecrypt.input.processor.keybykey.mode;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class KeyByKeyModeController {

    @FXML private Button doneButton;

    public Button getDoneButton() {
        return doneButton;
    }


    public void setKeyByKeyModeDone(EventHandler<ActionEvent> fullTextModeClearEvent) {
        getDoneButton().setOnAction(fullTextModeClearEvent);
    }

}