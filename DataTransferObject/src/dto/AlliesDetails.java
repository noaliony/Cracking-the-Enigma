package dto;

public class AlliesDetails {
    private final String teamName;
    private int agentsCount;
    private int taskSize;
    private boolean isReady = false;

    public AlliesDetails(String teamName, int agentsCount, int taskSize, boolean isReady) {
        this.teamName = teamName;
        this.agentsCount = agentsCount;
        this.taskSize = taskSize;
        this.isReady = isReady;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getAgentsCount() {
        return agentsCount;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public boolean getIsReady() {
        return isReady;
    }
}
