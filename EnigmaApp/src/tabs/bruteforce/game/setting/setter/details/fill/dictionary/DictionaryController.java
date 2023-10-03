package tabs.bruteforce.game.setting.setter.details.fill.dictionary;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.Set;

public class DictionaryController {

    @FXML private TextArea dictionaryTextArea;

    public void setUp(Set<String> dictionary) {
        StringBuilder dictionaryString = new StringBuilder();
        int counter = 1;

        for (String word : dictionary){
            dictionaryString.append(counter + ". " + word + System.lineSeparator());
            counter++;
        }
        dictionaryTextArea.setText(dictionaryString.toString());
    }
}
