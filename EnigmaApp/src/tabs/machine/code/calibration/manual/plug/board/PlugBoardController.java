package tabs.machine.code.calibration.manual.plug.board;

import engine.Engine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tabs.machine.code.calibration.manual.plug.pair.SinglePlugPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlugBoardController {

    @FXML private ComboBox<String> plugOneComboBox;
    @FXML private ComboBox<String> plugTwoComboBox;
    @FXML private Button addPlugPairButton;
    @FXML private VBox addedPlugPairsVBox;
    @FXML private Label messagePlugPairsOverLabel;
    @FXML private Button clearConfigurationButton;

    private BooleanProperty isPlugPairsOver;
    private List<Character> alphabet;

    public PlugBoardController(){
        isPlugPairsOver = new SimpleBooleanProperty();
    }

    @FXML
    private void initialize(){
        messagePlugPairsOverLabel.visibleProperty().bind(isPlugPairsOver);
    }

    private void returnPlugPair(SinglePlugPair childToRemove, boolean removeFromVBoxChildren) {
        String plugOneCharacter = childToRemove.getPlugOneCharacter();
        String plugTwoCharacter = childToRemove.getPlugTwoCharacter();

        addCharacterToComboBox(plugOneCharacter, plugOneComboBox);
        addCharacterToComboBox(plugTwoCharacter, plugOneComboBox);
        addCharacterToComboBox(plugOneCharacter, plugTwoComboBox);
        addCharacterToComboBox(plugTwoCharacter, plugTwoComboBox);
        if (removeFromVBoxChildren) {
            addedPlugPairsVBox.getChildren().remove(childToRemove);
        }
        isPlugPairsOver.set(false);
    }

    public void setUp(List<Character> alphabet){
        this.alphabet = alphabet;

        plugOneComboBox.getItems().clear();
        plugTwoComboBox.getItems().clear();
        plugOneComboBox.getItems().clear();
        plugTwoComboBox.getItems().clear();
        alphabet.forEach(character -> {
            plugOneComboBox.getItems().add(character.toString());
            plugTwoComboBox.getItems().add(character.toString());
        });
        plugOneComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
                updateAllPlugPairs(oldValue, newValue, plugOneComboBox));
        plugTwoComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
                updateAllPlugPairs(oldValue, newValue, plugTwoComboBox));
        clearConfigurationButton.setOnAction(event -> clearConfiguration());
    }

    private SinglePlugPair createSinglePlugPair(String char1, String char2) {
        SinglePlugPair pairToCreate = new SinglePlugPair(char1, char2);

        pairToCreate.setDeletePlugPair(event -> returnPlugPair(pairToCreate, true));

        return pairToCreate;
    }

    private String getPlugOneComboBoxValue(){
        return plugOneComboBox.getValue();
    }

    private String getPlugTwoComboBoxValue(){
        return plugTwoComboBox.getValue();
    }

    private void updateAllPlugPairs(String oldValue, String newValue, ComboBox<String> relevantComboBox) {

        if (relevantComboBox != plugOneComboBox) {
            updateComboBox(oldValue, newValue, plugOneComboBox);
        }
        if (relevantComboBox != plugTwoComboBox) {
            updateComboBox(oldValue, newValue, plugTwoComboBox);
        }
    }

    private void updateComboBox(String oldValue, String newValue, ComboBox<String> comboBox) {
        if (oldValue == null && newValue != null) {
            removeCharacterFromComboBox(newValue, comboBox);
        } else if (oldValue != null && newValue != null) {
            addCharacterToComboBox(oldValue, comboBox);
            removeCharacterFromComboBox(newValue, comboBox);
        } else if (oldValue != null){
            removeCharacterFromComboBox(oldValue, comboBox);
        }
    }

    private void addCharacterToComboBox(String oldValue, ComboBox<String> comboBox) {
        comboBox.getItems().add(oldValue);
        FXCollections.sort(comboBox.getItems());
    }

    private void removeCharacterFromComboBox(String newValue, ComboBox<String> comboBox) {
        comboBox.getItems().remove(newValue);
    }

    @FXML
    void addPlugPairButtonAction(ActionEvent event) {
        if (getPlugOneComboBoxValue() != null && getPlugTwoComboBoxValue() != null) {
            String char1 = getPlugOneComboBoxValue();
            String char2 = getPlugTwoComboBoxValue();

            plugOneComboBox.setValue(null);
            plugTwoComboBox.setValue(null);
            addedPlugPairsVBox.getChildren().add(createSinglePlugPair(char1, char2));
            removeCharacterFromComboBox(char1, plugOneComboBox);
            removeCharacterFromComboBox(char2, plugTwoComboBox);
        }
        if (plugOneComboBox.getItems().isEmpty() && plugTwoComboBox.getItems().isEmpty())
        {
            isPlugPairsOver.set(true);
        }
    }

    public Map<Character, Character> createPlugPairsString() {

        Map<Character, Character> result = new HashMap<>();

        for (Character currentChar : alphabet) {
            result.put(currentChar, currentChar);
        }

        addedPlugPairsVBox.getChildren().forEach(singlePair -> {
            SinglePlugPair singlePairCast = (SinglePlugPair)singlePair;

            result.put(singlePairCast.getPlugOneCharacter().charAt(0), singlePairCast.getPlugTwoCharacter().charAt(0));
        });

        return result;
    }

    public void clearConfiguration() {
        addedPlugPairsVBox.getChildren().forEach(singlePair -> {
            returnPlugPair((SinglePlugPair) singlePair, false);
        });
        addedPlugPairsVBox.getChildren().clear();
        plugOneComboBox.setValue(null);
        plugTwoComboBox.setValue(null);
    }
}
