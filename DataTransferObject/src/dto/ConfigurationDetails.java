package dto;

import enums.ReflectorID;
import java.util.List;
import java.util.Map;

public class ConfigurationDetails {

    private List<Integer> rotorIDs;
    private List<Character> startPosition;
    private ReflectorID reflectorID;
    private Map<Character, Character> plugPairs;

    public ConfigurationDetails(){}

    public ConfigurationDetails(List<Integer> rotorIDs, List<Character> startPosition, ReflectorID reflectorID,
                                Map<Character, Character> plugPairs) {
        this.rotorIDs = rotorIDs;
        this.startPosition = startPosition;
        this.reflectorID = reflectorID;
        this.plugPairs = plugPairs;
    }

    public ConfigurationDetails(ConfigurationDetails startConfiguration) {
        this.rotorIDs = startConfiguration.getRotorIDs();
        this.startPosition = startConfiguration.getStartPosition();
        this.reflectorID = startConfiguration.getReflectorID();
        this.plugPairs = startConfiguration.getPlugPairs();
    }

    public List<Integer> getRotorIDs() {
        return rotorIDs;
    }

    public List<Character> getStartPosition() {
        return startPosition;
    }

    public ReflectorID getReflectorID() {
        return reflectorID;
    }

    public Map<Character, Character> getPlugPairs() {
        return plugPairs;
    }

    @Override
    public String toString(){
        String rotorIDsString = createStringIDRotors(rotorIDs);
        String startingPositionString = createStartingPosition(startPosition);
        String reflectorIDString = reflectorID.toString();
        String plugPairsString = createStringPlugPairs();
        MachineSetting machineSetting = new MachineSetting(rotorIDsString, startingPositionString, reflectorIDString, plugPairsString);

        return machineSetting.toString();
    }

    private String createStringPlugPairs() {
        String plugPairsString = "";

        if (plugPairs.isEmpty()){
            return plugPairsString;
        }

        for (Map.Entry<Character, Character> entry : plugPairs.entrySet()) {

            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            plugPairsString = plugPairsString.concat(key);
            plugPairsString = plugPairsString.concat(value);
        }

        return plugPairsString;
    }

    private String createStartingPosition(List<Character> rotorStartPosition) {

        String startingPositionString = "";

        for (Character currentChar : rotorStartPosition)
            startingPositionString = startingPositionString.concat(currentChar.toString());

        return startingPositionString;
    }

    private String createStringIDRotors(List<Integer> rotorsIDInUse) {

        StringBuilder rotorsIDString = new StringBuilder();

        for (Integer idRotor : rotorsIDInUse) {

            rotorsIDString.append(idRotor.toString());
            rotorsIDString.append(",");
        }

        return rotorsIDString.substring(0, rotorsIDString.length() - 1);
    }
}
