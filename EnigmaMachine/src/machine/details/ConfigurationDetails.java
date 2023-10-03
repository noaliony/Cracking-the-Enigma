package machine.details;

import components.Rotor;
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
        MachineSetting machineSetting = new MachineSetting(rotorIDsString, startingPositionString, reflectorIDString);

        return machineSetting.toString();
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
