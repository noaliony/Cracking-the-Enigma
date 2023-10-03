package main.uboat.tabs.screen.contest.game.setting.setter.details.fill;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.uboat.tabs.screen.contest.game.setting.setter.details.fill.dictionary.DictionaryController;

import java.util.function.Consumer;

public class DetailsFillController {

    @FXML private TextField messageToEncodeTextField;
    @FXML private Button sendDataButton;
    @FXML private DictionaryController dictionaryController;

    private Consumer<String> handleErrorConsumer;
    private Consumer<String> updateUserAboutMessagesConsumer;
    private Runnable amountOfIsEncodedMessagesLabelRunnable;

    @FXML
    private void initialize(){
        sendDataButton.setOnAction(event -> updateUserAboutMessages());
    }

    @FXML
    public void setUp(Consumer<String> updateUserAboutMessagesConsumer){
        this.updateUserAboutMessagesConsumer = updateUserAboutMessagesConsumer;
    }

    public void sendRequestToGetDictionary(){
        dictionaryController.sendRequestToGetDictionary();
    }

    private void updateUserAboutMessages(){
        String originalMessage = messageToEncodeTextField.getText().toUpperCase();
        
        amountOfIsEncodedMessagesLabelRunnable.run();
        if (isOriginalMessageValid(originalMessage)){
            updateUserAboutMessagesConsumer.accept(originalMessage);
        }
    }

    private boolean isOriginalMessageValid(String originalMessage) {
        boolean result = true;

        if (originalMessage.isEmpty()){
            handleErrorConsumer.accept("Can not send data because the message to encode is empty");
            result = false;
        }

        return result;
    }

    public void clearAllChanges() {
        messageToEncodeTextField.clear();
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
        dictionaryController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    public void setAmountOfIsEncodedMessagesLabelRunnable(Runnable amountOfIsEncodedMessagesLabelRunnable) {
        this.amountOfIsEncodedMessagesLabelRunnable = amountOfIsEncodedMessagesLabelRunnable;
    }
}
