package main.agent.operation.screen.winnerMessage;

import dto.StringDecryptedCandidate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WinnerMessageController {

    @FXML private VBox winnerStringDetailsVBox;
    @FXML private Label stringDecryptedValueLabel;
    @FXML private Label configurationDetailsValueLabel;
    @FXML private Label allyNameValueLabel;
    @FXML private Label agentNameValueLabel;

    public void updateLabels(StringDecryptedCandidate stringDecryptedCandidate) {
        stringDecryptedValueLabel.setText(stringDecryptedCandidate.getStringDecrypted());
        configurationDetailsValueLabel.setText(stringDecryptedCandidate.getConfigurationDetailsString());
        allyNameValueLabel.setText(stringDecryptedCandidate.getAllyName());
        agentNameValueLabel.setText(stringDecryptedCandidate.getAgentName());
    }

    public void updateVisibleWinnerMessage() {
        winnerStringDetailsVBox.setVisible(true);
    }

    public void reset() {
        stringDecryptedValueLabel.setText("");
        configurationDetailsValueLabel.setText("");
        allyNameValueLabel.setText("");
        agentNameValueLabel.setText("");
    }
}
