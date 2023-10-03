package tabs.bruteforce.progress.display.decrypted.candidate.display;

import decryption.manager.StringDecryptedCandidate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DecryptedCandidateDisplayController {
    @FXML private Label messageValueLabel;
    @FXML private Label agentIDValueLabel;
    @FXML private Label settingsValueLabel;

    public void setDecryptedCandidate(StringDecryptedCandidate stringDecryptedCandidate) {
        messageValueLabel.setText(stringDecryptedCandidate.getStringDecrypted());
        agentIDValueLabel.setText(String.valueOf(stringDecryptedCandidate.getFoundAgentID()));
        settingsValueLabel.setText(stringDecryptedCandidate.getConfigurationDetails().toString());
    }
}
