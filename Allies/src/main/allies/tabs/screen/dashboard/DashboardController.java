package main.allies.tabs.screen.dashboard;

import dto.BattlefieldDetails;
import javafx.fxml.FXML;
import main.allies.tabs.screen.dashboard.teamsAgentData.TeamsAgentDataController;
import main.allies.tabs.screen.dashboard.contestsData.ContestDataJoinAbleController;

import java.util.function.Consumer;

public class DashboardController {
    @FXML private TeamsAgentDataController teamsAgentDataController;
    @FXML private ContestDataJoinAbleController contestDataJoinAbleController;

    private Consumer<String> handleErrorConsumer;

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer){
        this.handleErrorConsumer = handleErrorConsumer;
        teamsAgentDataController.setUpHandleErrorConsumer(handleErrorConsumer);
        contestDataJoinAbleController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    public void setUp(Consumer<BattlefieldDetails> updateContestDetailsConsumer, Runnable visibleContestTabRunnable, Runnable onJoinedContest) {
        contestDataJoinAbleController.setUp(updateContestDetailsConsumer, visibleContestTabRunnable, () -> {
            teamsAgentDataController.stopGettingTeamsAgentData();
            onJoinedContest.run();
        });
    }

    public void allyLoggedIn() {
        teamsAgentDataController.getTeamsAgentData();
        contestDataJoinAbleController.getContestsDataList();
    }

    public void reset() {
        contestDataJoinAbleController.reset();
    }
}
