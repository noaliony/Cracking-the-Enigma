package decryption.manager;

import machine.details.ConfigurationDetails;
import machine.details.MachineSetting;

public class StringDecryptedCandidate {

    private final String stringDecrypted;
    private final ConfigurationDetails configurationDetails;
    private final int foundAgentID;

    public StringDecryptedCandidate(String stringDecrypted, ConfigurationDetails configurationDetails, int foundAgentID){
        this.stringDecrypted = stringDecrypted;
        this.configurationDetails = configurationDetails;
        this.foundAgentID = foundAgentID;
    }

    public String getStringDecrypted() {
        return stringDecrypted;
    }

    public ConfigurationDetails getConfigurationDetails() {
        return configurationDetails;
    }

    public int getFoundAgentID() {
        return foundAgentID;
    }
}
