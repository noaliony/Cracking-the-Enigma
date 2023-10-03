package tabs.machine.code.calibration.manual.start.position;

import exceptions.StartPositionAreNotValidException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class StartPositionController {

    @FXML private HBox startPositionHBox;
    @FXML private Button clearConfigurationButton;

    List<Character> alphabet;

    public void setUp(int rotorsCount, List<Character> alphabet){
        this.alphabet = alphabet;

        startPositionHBox.getChildren().clear();
        for (int i = 1; i <= rotorsCount; i++) {
            startPositionHBox.getChildren().add(createComboBox());
        }
        clearConfigurationButton.setOnAction(event -> clearConfiguration());
    }

    private ComboBox<Character> createComboBox() {

        ComboBox<Character> optionalCharacters = new ComboBox<>();
        ObservableList<Character> observableList = optionalCharacters.getItems();

        alphabet.forEach(character -> observableList.add(character));

        return optionalCharacters;
    }

    public List<Character> createStartPositionString() {

        List<Character> result = new ArrayList<>();

        startPositionHBox.getChildren().forEach(comboBox -> {
            ComboBox<Character> comboBoxCast = (ComboBox<Character>)comboBox;

            if (comboBoxCast.getValue() == null)
                throw new StartPositionAreNotValidException();
            result.add(comboBoxCast.getValue());
        });

        return result;
    }

    public void clearConfiguration() {
        startPositionHBox.getChildren().forEach(comboBox -> {
            ComboBox<Character> comboBoxCast = (ComboBox<Character>) comboBox;

            comboBoxCast.getItems().clear();
            alphabet.forEach(character -> comboBoxCast.getItems().add(character));
        });
    }
}
