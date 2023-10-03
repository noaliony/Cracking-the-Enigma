package tabs.bruteforce.game.setting.setter.details.fill;

import decryption.manager.DMInformationFromUser;
import decryption.manager.TaskResult;
import enums.GameLevel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;
import tabs.bruteforce.game.setting.setter.details.fill.dictionary.DictionaryController;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static com.sun.org.apache.xml.internal.dtm.Axis.getNames;

public class DetailsFillController {

    @FXML private Slider agentsCountSlider;
    @FXML private ComboBox<String> gameLevelComboBox;
    @FXML private TextField taskSizeValueTextField;
    @FXML private TextField messageToEncodeTextField;
    @FXML private Button sendDataButton;
    @FXML private DictionaryController dictionaryController;

    private Consumer<String> handleErrorConsumer;
    private DMInformationFromUser informationFromUser;
    private Consumer<DMInformationFromUser> informationConsumer;
    private Consumer<TaskResult> taskResultConsumer;

    @FXML
    private void initialize(){
        sendDataButton.setOnAction(event -> getInformationFromUserAndSendData());
    }

    public void setUp(int maxValueAgentsCount, Set<String> dictionary, Consumer<String> handleErrorConsumer,
                      Consumer<DMInformationFromUser> informationConsumer, Consumer<TaskResult> taskResultConsumer){
        agentsCountSlider.setMax(maxValueAgentsCount);
        agentsCountSlider.setShowTickLabels(true);
        agentsCountSlider.setShowTickMarks(true);
        agentsCountSlider.setMajorTickUnit(1);
        agentsCountSlider.setMinorTickCount(0);
        agentsCountSlider.setSnapToTicks(true);
        dictionaryController.setUp(dictionary);
        this.handleErrorConsumer = handleErrorConsumer;
        this.informationConsumer = informationConsumer;
        this.taskResultConsumer = taskResultConsumer;
        gameLevelComboBox.getItems().clear();
        for (GameLevel gameLevel : GameLevel.values()){
            gameLevelComboBox.getItems().add(gameLevel.name());
        }
    }

    private void getInformationFromUserAndSendData(){
        informationFromUser = getInformationFromUser();
        if (informationFromUser != null) {
            informationConsumer.accept(informationFromUser);
        }
    }

    private DMInformationFromUser getInformationFromUser() {

        int agentsCount = (int) agentsCountSlider.getValue();
        String gameLevel = gameLevelComboBox.getValue();
        String originalMessage = messageToEncodeTextField.getText().toUpperCase();

        if (isDMInformationFromUserValid(gameLevel, originalMessage)){
            int taskSize = Integer.parseInt(taskSizeValueTextField.getText());

            return new DMInformationFromUser(agentsCount, GameLevel.convertStringToGameLevel(gameLevel), taskSize, originalMessage, taskResultConsumer);
        }

        return null;
    }

    private boolean isDMInformationFromUserValid(String gameLevel, String originalMessage) {

        boolean result = true;

        try {
            if (gameLevel == null) {
                handleErrorConsumer.accept("Can not send data because the game level id not valid");
                result = false;
            } else if (taskSizeValueTextField.getText().isEmpty()) {
                handleErrorConsumer.accept("Can not send data because the task size is empty");
                result = false;
            } else if (Integer.parseInt(taskSizeValueTextField.getText()) <= 0){
                handleErrorConsumer.accept("Can not send data because the task size in not positive");
                result = false;
            } else if (originalMessage.isEmpty()){
                handleErrorConsumer.accept("Can not send data because the message to encode is empty");
                result = false;
            }

        } catch (NumberFormatException exception){
            handleErrorConsumer.accept("Can not send data because the task size is not an integer number");
        }

        return result;
    }

    public void clearAllChanges() {
        agentsCountSlider.setValue(agentsCountSlider.getMin());
        gameLevelComboBox.setValue(null);
        taskSizeValueTextField.clear();
        messageToEncodeTextField.clear();
    }
}
