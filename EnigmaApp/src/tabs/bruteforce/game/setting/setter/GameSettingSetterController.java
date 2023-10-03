package tabs.bruteforce.game.setting.setter;

import decryption.manager.DMInformationFromUser;
import decryption.manager.TaskResult;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import tabs.bruteforce.game.setting.setter.details.fill.DetailsFillController;
import tabs.bruteforce.game.setting.setter.details.fill.dictionary.DictionaryController;
import tabs.bruteforce.game.setting.setter.user.updater.UserUpdaterController;

import java.awt.event.ActionEvent;
import java.util.Set;
import java.util.function.Consumer;

public class GameSettingSetterController {

    @FXML private HBox detailsFill;
    @FXML private DetailsFillController detailsFillController;
    @FXML private VBox userUpdater;
    @FXML private UserUpdaterController userUpdaterController;

    public void updateUserAboutMessagesAndTasksCount(DMInformationFromUser informationFromUser) {
        userUpdaterController.updateUserAboutMessagesAndTasksCount(informationFromUser);
        userUpdater.setVisible(true);
    }

    public void setUp(int maxValueAgentsCount, Set<String> dictionary, Consumer<String> handleErrorConsumer,
                      Consumer<DMInformationFromUser> informationConsumer, Consumer<TaskResult> taskResultConsumer,
                      Runnable startTaskRunnable) {
        detailsFillController.setUp(maxValueAgentsCount, dictionary, handleErrorConsumer,
                 informationConsumer, taskResultConsumer);
        userUpdaterController.setUp(startTaskRunnable);
        userUpdater.setVisible(false);
    }

    public void clearAllChanges() {
        detailsFillController.clearAllChanges();
        userUpdaterController.clearAllChanges();
        userUpdater.setVisible(false);
    }
}
