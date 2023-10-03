package users;

import dto.AgentDetails;
import dto.TasksResultsDetails;

public class Agent {

    private String agentName;
    private String allyName;
    private String battlefieldName = null;
    private int threadsCount;
    private int tasksCountToPull;
    private boolean isReady = false;
    private int pulledTasksCount = 0;
    private int completedTasksCount = 0;
    private int foundCandidatesCount = 0;
    private boolean resetDataRequested = false;
        
    public Agent(String userName, String allyName, int threadsCount, int tasksCountToPull) {
        this.agentName = userName;
        this.allyName = allyName;
        this.threadsCount = threadsCount;
        this.tasksCountToPull = tasksCountToPull;
    }

    public AgentDetails getNewAgentDetails() {
        return new AgentDetails(agentName, allyName, threadsCount, tasksCountToPull);
    }
    
    public String getAllyName() {
        return allyName;
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public void setBattlefieldName(String battlefieldName) {
        this.battlefieldName = battlefieldName;
    }

    public void leaveBattlefield() {
        setBattlefieldName(null);
    }

    public void setPulledTasksCount(int pulledTasksCount) {
        this.pulledTasksCount += pulledTasksCount;
    }

    public void advanceCompletedTasksCount() {
        completedTasksCount++;
    }

    public void setFoundCandidatesCount(int foundCandidatesCount) {
        this.foundCandidatesCount += foundCandidatesCount;
    }

    public TasksResultsDetails getNewTasksResultsDetails() {
        return new TasksResultsDetails(agentName, pulledTasksCount, completedTasksCount, foundCandidatesCount);
    }

    public void setResetDataRequested(boolean resetDataRequested) {
        this.resetDataRequested = resetDataRequested;
    }

    public boolean isResetDataRequested() {
        return resetDataRequested;
    }

    public boolean checkIfAgentIsConnectedToContest() {
        return battlefieldName != null;
    }
}
