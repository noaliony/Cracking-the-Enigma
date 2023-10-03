package dto;

public class TasksResultsDetails {
    private String foundAgentName;
    private int pulledTasksCount = 0;
    private int completedTasksCount = 0;
    private int remainingTasksCount = 0;
    private int foundCandidatesCount = 0;

    public TasksResultsDetails(String foundAgentName, int pulledTasksCount, int completedTasksCount, int foundCandidatesCount) {
        this.foundAgentName = foundAgentName;
        this.pulledTasksCount = pulledTasksCount;
        this.completedTasksCount = completedTasksCount;
        this.foundCandidatesCount = foundCandidatesCount;
        remainingTasksCount = pulledTasksCount - completedTasksCount;
    }

    public String getFoundAgentName() {
        return foundAgentName;
    }

    public int getPulledTasksCount() {
        return pulledTasksCount;
    }

    public int getCompletedTasksCount() {
        return completedTasksCount;
    }

    public int getFoundCandidatesCount() {
        return foundCandidatesCount;
    }

    public int getRemainingTasksCount() {
        return remainingTasksCount;
    }
}
