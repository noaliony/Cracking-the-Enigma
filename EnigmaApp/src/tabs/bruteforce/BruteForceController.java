package tabs.bruteforce;

import decryption.manager.DMInformationFromUser;
import decryption.manager.TaskResult;
import engine.Engine;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import tabs.bruteforce.game.setting.setter.GameSettingSetterController;
import tabs.bruteforce.progress.display.ProgressDisplayController;
import tabs.bruteforce.result.display.ResultDisplayController;
import tabs.bruteforce.task.BruteForceTask;
import tabs.machine.current.configuration.ConfigurationController;

import java.util.function.Consumer;

public class BruteForceController {

    @FXML private ConfigurationController currentConfigurationController;
    @FXML private Button resetConfigurationButton;
    @FXML private Button gameSettingSetterButton;
    @FXML private Button progressDisplayButton;
    @FXML private Button resultDisplayButton;
    @FXML private ScrollPane gameSettingSetter;
    @FXML private GameSettingSetterController gameSettingSetterController;
    @FXML private ScrollPane progressDisplay;
    @FXML private ProgressDisplayController progressDisplayController;
    @FXML private ScrollPane resultDisplay;
    @FXML private ResultDisplayController resultDisplayController;
    @FXML private StackPane bruteForcePanel;

    private BruteForceTask bruteForceTask;
    private Thread bruteForceTaskRunner;
    private DMInformationFromUser informationFromUser;
    private Engine engine;
    private int tasksCount;
    private String gameState = "The game stopped in the middle";
    private FadeTransition fadeTransitionToResultDisplayButton;

    @FXML
    private void initialize() {
        gameSettingSetterButtonAction(null);
        progressDisplayController.isProgressBarCompleteProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                flashingResultDisplayButton();
                gameState = "The game ended successfully";
                gameStopped();
            }
        });
    }

    private void flashingResultDisplayButton(){
        fadeTransitionToResultDisplayButton = new FadeTransition(Duration.seconds(0.8), resultDisplayButton);
        fadeTransitionToResultDisplayButton.setFromValue(1.0);
        fadeTransitionToResultDisplayButton.setToValue(0.0);
        fadeTransitionToResultDisplayButton.setCycleCount(Animation.INDEFINITE);
        fadeTransitionToResultDisplayButton.play();
    }

    private void gameStopped() {
        bruteForceTask.calculateAverageDurationTimeToEachTask();
        resultDisplayController.setUp(gameState, bruteForceTask);
    }

    @FXML
    void gameSettingSetterButtonAction(ActionEvent event) {
        turnOffVisibleToBruteForcePanel();
        gameSettingSetter.setVisible(true);
    }

    @FXML
    void progressDisplayButtonAction(ActionEvent event) {
        turnOffVisibleToBruteForcePanel();
        progressDisplay.setVisible(true);
    }

    @FXML
    void resultDisplayButtonAction(ActionEvent event) {
        turnOffVisibleToBruteForcePanel();
        resultDisplay.setVisible(true);
        if (fadeTransitionToResultDisplayButton != null) {
            fadeTransitionToResultDisplayButton.stop();
            resultDisplayButton.setOpacity(1);
        }
    }

    private void turnOffVisibleToBruteForcePanel(){
        bruteForcePanel.getChildren().forEach(child -> child.setVisible(false));
    }

    public void setCurrentConfigurationController(ConfigurationController configurationController){
        this.currentConfigurationController.bindToConfigurationController(configurationController);
    }

    public void taskResultAchieved(TaskResult taskResult) {
        Platform.runLater(() -> bruteForceTask.taskResultAchieved(taskResult));
    }

    public void createTask() {
        progressDisplayButtonAction(null);
        bruteForceTask = new BruteForceTask(tasksCount);
        bruteForceTask.setEngine(engine);
        bruteForceTask.updateTasksCountListeners(tasksCount);
        progressDisplayController.setBruteForceTask(bruteForceTask);
        bruteForceTaskRunner = new Thread(bruteForceTask, "Brute Force Task Runner");
        bruteForceTaskRunner.setDaemon(true);
        bruteForceTaskRunner.start();
    }

    public void updateUserAboutMessagesAndTasksCount(DMInformationFromUser informationFromUser) {
        this.tasksCount = informationFromUser.getTasksCount();
        this.informationFromUser = informationFromUser;
        informationFromUser.setTaskResultConsumer(this::taskResultAchieved);
        gameSettingSetterController.updateUserAboutMessagesAndTasksCount(informationFromUser);
        progressDisplayController.setTasksCount(informationFromUser.getTasksCount());
    }

    public void setUp(Engine engine, Consumer<String> handleErrorConsumer,
                      Consumer<DMInformationFromUser> informationConsumer) {
        this.engine = engine;
        gameSettingSetterController.setUp(engine.getMaxValueAgentsCount(), engine.getDictionary(), handleErrorConsumer,
                informationConsumer, this::taskResultAchieved, this::createTask);
        progressDisplayController.setUp(event -> pauseAllThreads(), event -> resumeAllThreads(), event -> stopAllThreads());
        resultDisplayController.setPlayAgainButtonAction(event -> setPlayAgainButtonAction());
    }

    private void pauseAllThreads() {
        bruteForceTask.pauseAllThreads();
    }

    private void resumeAllThreads(){
        bruteForceTask.resumeAllThreads();
    }

    private void stopAllThreads() {
        bruteForceTask.stopAllThreads();
        gameStopped();
        resultDisplayButtonAction(null);
    }

    private void setPlayAgainButtonAction(){
        clearAllScreensChanges();
        gameSettingSetterButtonAction(null);
    }

    private void clearAllScreensChanges() {
        gameSettingSetterController.clearAllChanges();
        progressDisplayController.clearAllChanges();
        resultDisplayController.clearAllChanges();
    }

    public void setUpResetConfigurationButton(EventHandler<ActionEvent> resetConfigurationEvent) {
        resetConfigurationButton.setOnAction(resetConfigurationEvent);
    }
}
