package tabs.bruteforce.task;

import decryption.manager.StringDecryptedCandidate;
import decryption.manager.TaskResult;
import engine.Engine;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import machine.details.ConfigurationDetails;

import java.time.Duration;
import java.time.Instant;

public class BruteForceTask extends Task<Boolean> {
    private Engine engine;
    private final ObservableList<StringDecryptedCandidate> stringDecryptedCandidateList = FXCollections.observableArrayList();
    private final IntegerProperty readTasksCount;
    private final LongProperty allTasksTime;
    private long timeTakenForProcess;
    private Instant startTimeForProcess;
    private long timeTakenForPause = 0;
    private Instant startTimeForPause;

    private double averageDurationTimeToEachTask;
    private int tasksCount;

    public BruteForceTask(int tasksCount){
        readTasksCount = new SimpleIntegerProperty();
        allTasksTime = new SimpleLongProperty();
        this.tasksCount = tasksCount;
    }

    public long getTimeTakenForProcess() {
        return timeTakenForProcess;
    }

    public IntegerProperty readTasksCountProperty() {
        return readTasksCount;
    }

    public double getAverageDurationTimeToEachTask() {
        return averageDurationTimeToEachTask;
    }

    @Override
    protected Boolean call(){
        startTimeForProcess = Instant.now();
        engine.runDM();

        timeTakenForProcess = Duration.between(startTimeForProcess, Instant.now()).getSeconds() - timeTakenForPause;
        return true;
    }

    public void taskResultAchieved(TaskResult taskResult) {
        readTasksCount.setValue(readTasksCount.getValue() + 1);
        stringDecryptedCandidateList.addAll(taskResult.getStringDecryptedCandidateList());
        allTasksTime.setValue(allTasksTime.getValue() + taskResult.getMilliSecondTimeTookToDecrypt());
    }

    public ObservableList<StringDecryptedCandidate> getStringDecryptedCandidateList() {
        return stringDecryptedCandidateList;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void updateTasksCountListeners(int tasksCount) {
        readTasksCount.addListener((observable, oldValue, newValue) -> updateProgress(newValue.doubleValue(), tasksCount));
    }

    public void calculateAverageDurationTimeToEachTask() {
        averageDurationTimeToEachTask = allTasksTime.doubleValue() / tasksCount;
    }

    public void pauseAllThreads() {
        startTimeForPause = Instant.now();
        engine.pauseAllThreads();
    }

    public void resumeAllThreads(){
        timeTakenForPause += Duration.between(startTimeForPause, Instant.now()).getSeconds();
        engine.resumeAllThreads();
    }

    public void stopAllThreads(){
        engine.stopAllThreads();
        timeTakenForProcess = Duration.between(startTimeForProcess, Instant.now()).getSeconds() - timeTakenForPause;
    }

    public String getWinnerAgent(BruteForceTask bruteForceTask) {
        String winnerAgent = "No agent found the right configuration!";
        String messageToEncode = engine.getMessageToEncodeFromDM();
        ConfigurationDetails configurationDetails = engine.getReceivedConfigurationDetailsFromDM();

        for (StringDecryptedCandidate stringDecryptedCandidate : stringDecryptedCandidateList) {
            if (stringDecryptedCandidate.getStringDecrypted().equals(messageToEncode)
                    && stringDecryptedCandidate.getConfigurationDetails().toString().equals(configurationDetails.toString())) {
                winnerAgent = String.format("Agent %d found the right configuration!", stringDecryptedCandidate.getFoundAgentID());
            }
        }

        return winnerAgent;
    }
}
