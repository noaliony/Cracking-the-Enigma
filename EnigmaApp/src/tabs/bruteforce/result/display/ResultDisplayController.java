package tabs.bruteforce.result.display;

import decryption.manager.StringDecryptedCandidate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import tabs.bruteforce.task.BruteForceTask;

public class ResultDisplayController {

    @FXML private Label gameStatusValueLabel;
    @FXML private Label winnerAgentValueLabel;
    @FXML private Label gameDurationTimeValueLabel;
    @FXML private Label averageDurationTimeToEachTaskValueLabel;
    @FXML private Button playAgainButton;

    public void setUp(String successFinishGameState, BruteForceTask bruteForceTask){
        long timeTakenForProcess = bruteForceTask.getTimeTakenForProcess();

        gameStatusValueLabel.setText(successFinishGameState);
        winnerAgentValueLabel.setText(bruteForceTask.getWinnerAgent(bruteForceTask));
        gameDurationTimeValueLabel.setText(String.format("%d Seconds", timeTakenForProcess));
        averageDurationTimeToEachTaskValueLabel.setText(String.format("%03f Milli-Seconds/Task", bruteForceTask.getAverageDurationTimeToEachTask()));
    }

    public void setPlayAgainButtonAction(EventHandler<ActionEvent> playAgainEvent) {
        playAgainButton.setOnAction(playAgainEvent);
    }

    public void clearAllChanges() {
        gameStatusValueLabel.setText("");
        winnerAgentValueLabel.setText("");
        gameDurationTimeValueLabel.setText("");
        averageDurationTimeToEachTaskValueLabel.setText("");
    }
}