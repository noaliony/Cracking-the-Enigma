package tabs.bruteforce.progress.display;

import decryption.manager.StringDecryptedCandidate;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tabs.bruteforce.task.BruteForceTask;

public class ProgressDisplayController {
    //private final String DECRYPTED_CANDIDATE_DISPLAY = "/tabs/bruteforce/progress/display/decrypted/candidate/display/DecryptedCandidateDisplay.fxml";
    @FXML private TableView<StringDecryptedCandidate> decryptedCandidatesTableView;
    @FXML private TableColumn<StringDecryptedCandidate, Integer> agentIDCol;
    @FXML private TableColumn<StringDecryptedCandidate, String> decryptedMessageCol;
    @FXML private TableColumn<StringDecryptedCandidate, String> settingsCol;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Button stopButton;
    @FXML private Label completedTasksCountValueLabel;
    @FXML private Label tasksCountValueLabel;
    @FXML private ProgressBar bruteForceProgressBar;

    private BooleanProperty isProgressBarComplete;

    @FXML
    private void initialize() {
        isProgressBarComplete = new SimpleBooleanProperty();
        isProgressBarComplete.bind(Bindings.equal(bruteForceProgressBar.progressProperty(), 1));
        agentIDCol.setCellValueFactory(new PropertyValueFactory<>("foundAgentID"));
        decryptedMessageCol.setCellValueFactory(new PropertyValueFactory<>("stringDecrypted"));
        settingsCol.setCellValueFactory(new PropertyValueFactory<>("configurationDetails"));
    }

    public void setUp(EventHandler<ActionEvent> pauseEvent, EventHandler<ActionEvent> resumeEvent, EventHandler<ActionEvent> stopEvent) {
        pauseButton.setOnAction(pauseEvent);
        resumeButton.setOnAction(resumeEvent);
        stopButton.setOnAction(stopEvent);
    }

    public BooleanProperty isProgressBarCompleteProperty() {
        return isProgressBarComplete;
    }

    public void setTasksCount(Integer tasksCount) {
        tasksCountValueLabel.setText(tasksCount.toString());
    }

    public void resetProgressDisplay() {
        tasksCountValueLabel.setText("");
    }

    public void setBruteForceTask(BruteForceTask bruteForceTask) {
        completedTasksCountValueLabel.textProperty().bind(bruteForceTask.readTasksCountProperty().asString());
        bruteForceProgressBar.progressProperty().bind(bruteForceTask.progressProperty());
        decryptedCandidatesTableView.setItems(bruteForceTask.getStringDecryptedCandidateList());
    }


    public void clearAllChanges() {
        for (int i = 0; i < decryptedCandidatesTableView.getItems().size(); i++) {
            decryptedCandidatesTableView.getItems().clear();
        } //OR decryptedCandidatesTableView.getItems().clear(); - NEED TO CHECK IT

        completedTasksCountValueLabel.textProperty().unbind();
        completedTasksCountValueLabel.setText("");
        tasksCountValueLabel.setText("");
        bruteForceProgressBar.progressProperty().unbind();
        bruteForceProgressBar.setProgress(0);
        //isProgressBarComplete.set(false);
    }
}

    /*private void createDecryptedCandidateDisplay(StringDecryptedCandidate stringDecryptedCandidate) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(DECRYPTED_CANDIDATE_DISPLAY));
            Node decryptedCandidateDisplay = loader.load();

            DecryptedCandidateDisplayController decryptedCandidateDisplayController = loader.getController();
            decryptedCandidateDisplayController.setDecryptedCandidate(stringDecryptedCandidate);

            decryptedCandidatesPanel.getChildren().add(decryptedCandidateDisplay);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

