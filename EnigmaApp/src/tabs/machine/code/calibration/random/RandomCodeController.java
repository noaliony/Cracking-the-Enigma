package tabs.machine.code.calibration.random;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RandomCodeController {

    @FXML private Button randomCode;

    private BooleanProperty isRandomCodeSelected;

    public RandomCodeController(){
        isRandomCodeSelected = new SimpleBooleanProperty();
    }

    public BooleanProperty isRandomCodeSelectedProperty() {
        return isRandomCodeSelected;
    }

    @FXML
    void randomCodeButtonAction(ActionEvent event) {
        isRandomCodeSelected.set(!isRandomCodeSelected.get());
    }
}
