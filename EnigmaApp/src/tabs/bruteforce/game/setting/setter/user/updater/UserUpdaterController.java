package tabs.bruteforce.game.setting.setter.user.updater;

import decryption.manager.DMInformationFromUser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class UserUpdaterController {

    @FXML private Label originalMessageLabel;
    @FXML private Label messageToEncodeLabel;
    @FXML private Label messageToDecodeLabel;
    @FXML private Label tasksCountValueLabel;
    @FXML private Button startButton;

    public void updateUserAboutMessagesAndTasksCount(DMInformationFromUser informationFromUser) {
        originalMessageLabel.setText(informationFromUser.getOriginalMessage());
        messageToEncodeLabel.setText(informationFromUser.getMessageToEncode());
        messageToDecodeLabel.setText(informationFromUser.getMessageToDecode());
        tasksCountValueLabel.setText(((Integer) informationFromUser.getTasksCount()).toString());
    }

    public void setUp(Runnable startTaskRunnable) {
        startButton.setOnAction(event -> startTaskRunnable.run());
    }

    public void clearAllChanges() {
        originalMessageLabel.setText("");
        messageToEncodeLabel.setText("");
        messageToDecodeLabel.setText("");
        tasksCountValueLabel.setText("");
    }
}