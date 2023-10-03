package tabs.encryptdecrypt.input.processor.fulltext.mode;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class FullTextModeController {

    @FXML private Button clearButton;
    @FXML private Button processButton;

    public Button getProcessButton() {
        return processButton;
    }
    public Button getClearButton() {
        return clearButton;
    }

    public void setFullTextModeClear(EventHandler<ActionEvent> fullTextModeClearEvent) {
        clearButton.setOnAction(fullTextModeClearEvent);
    }


}
