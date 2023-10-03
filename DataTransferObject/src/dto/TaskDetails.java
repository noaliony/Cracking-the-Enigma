package dto;

public class TaskDetails {
    private ConfigurationDetails startConfiguration;
    private int taskSize;

    public TaskDetails(ConfigurationDetails startConfiguration, int taskSize) {
        this.startConfiguration = startConfiguration;
        this.taskSize = taskSize;
    }

    public ConfigurationDetails getStartConfiguration() {
        return startConfiguration;
    }

    public int getTaskSize() {
        return taskSize;
    }

    @Override
    public String toString() {
        return "Start - " + startConfiguration +
                " Length - " + taskSize;
    }
}
