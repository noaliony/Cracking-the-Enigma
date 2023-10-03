package dto;

import java.util.Objects;

public class StringDecryptedCandidate {

    private final String stringDecrypted;
    private final ConfigurationDetails configurationDetails;
    private final String configurationDetailsString;
    private final String allyName;
    private final String agentName;
    private final TaskDetails taskDetails;

    public StringDecryptedCandidate(String stringDecrypted, ConfigurationDetails configurationDetails, String allyName,
                                    String agentName, TaskDetails taskDetails){
        this.stringDecrypted = stringDecrypted;
        this.configurationDetails = configurationDetails;
        configurationDetailsString = configurationDetails.toString();
        this.allyName = allyName;
        this.agentName = agentName;
        this.taskDetails = taskDetails;
    }

    public String getStringDecrypted() {
        return stringDecrypted;
    }

    public ConfigurationDetails getConfigurationDetails() {
        return configurationDetails;
    }

    public String getAllyName() {
        return allyName;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getConfigurationDetailsString() {
        return configurationDetailsString;
    }

    public TaskDetails getTaskDetails() {
        return taskDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringDecryptedCandidate that = (StringDecryptedCandidate) o;
        return Objects.equals(stringDecrypted, that.stringDecrypted) && Objects.equals(configurationDetailsString, that.configurationDetailsString) && Objects.equals(allyName, that.allyName) && Objects.equals(agentName, that.agentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringDecrypted, configurationDetailsString, allyName, agentName);
    }
}
