package dto;

public class AgentDetails {
    private String agentName;
    private String allyName;
    private int threadsCount;
    private int tasksCountToPull;

    public AgentDetails(String agentName, String allyName, int threadsCount, int tasksCountToPull) {
        this.agentName = agentName;
        this.allyName = allyName;
        this.threadsCount = threadsCount;
        this.tasksCountToPull = tasksCountToPull;
    }

}
