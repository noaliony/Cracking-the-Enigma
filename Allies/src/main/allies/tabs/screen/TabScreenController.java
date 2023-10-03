package main.allies.tabs.screen;

import dto.BattlefieldDetails;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import main.allies.tabs.screen.contest.ContestController;
import main.allies.tabs.screen.dashboard.DashboardController;

import java.util.function.Consumer;

public class TabScreenController {
    @FXML private DashboardController dashboardController;
    @FXML private ScrollPane dashboard;
    @FXML private Tab dashboardTab;
    @FXML private ContestController contestController;
    @FXML private ScrollPane contest;
    @FXML private TabPane tabPane;
    @FXML private Tab contestTab;

    private Consumer<String> handleErrorConsumer;

    @FXML
    private void initialize() {
        contestTab.setDisable(true);
        dashboardTab.setDisable(false);
        dashboardController.setUp(this::updateContestDetailsConsumer, this::setVisibleContestTabRunnable, () -> {
            contestTab.setDisable(false);
            dashboardTab.setDisable(true);
            tabPane.getSelectionModel().select(contestTab);
            contestController.contestJoined();
        });
        contestController.setUp(() -> {
            contestTab.setDisable(true);
            dashboardTab.setDisable(false);
            dashboardController.reset();
            tabPane.getSelectionModel().select(dashboardTab);
            allyLoggedIn();
        });
    }

    private void updateContestDetailsConsumer(BattlefieldDetails battlefieldDetails) {
        contestController.updateContestDetails(battlefieldDetails);
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
        dashboardController.setUpHandleErrorConsumer(handleErrorConsumer);
        contestController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    private void setVisibleContestTabRunnable() {
        tabPane.getSelectionModel().select(contestTab);
    }

    public void allyLoggedIn() {
        dashboardController.allyLoggedIn();
    }
}
