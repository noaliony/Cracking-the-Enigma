package dto;

import enums.ReflectorID;

import java.io.Serializable;

public class MachineSetting implements Serializable, Cloneable {

    private String rotorIDs; //45,27,94
    private String position;
    private String reflectorID;
    private String plugPairs;
    private String rotorNotches; //2,5,20

    public MachineSetting() {
    }

    public MachineSetting(String rotorIDs, String position, String reflectorID) {
        this.rotorIDs = rotorIDs;
        this.position = position;
        this.reflectorID = reflectorID;
        plugPairs = "";
        rotorNotches = "";
    }

    public MachineSetting(String rotorIDs, String position,
                          String reflectorID, String plugPairs) {
        this.rotorIDs = rotorIDs;
        this.position = position;
        this.reflectorID = reflectorID;
        this.plugPairs = plugPairs;
        rotorNotches = "";
    }

    public MachineSetting(String rotorIDs, String rotorNotches, String position,
                          String reflectorID, String plugPairs) {
        this.rotorIDs = rotorIDs;
        this.rotorNotches = rotorNotches;
        this.position = position;
        this.reflectorID = reflectorID;
        this.plugPairs = plugPairs;
    }

    public String getRotorIDs() {

        return rotorIDs;
    }

    public String getPosition() {

        return position;
    }

    public String getReflectorID() {

        return reflectorID;
    }

    public String getPlugPairs() {

        return plugPairs;
    }

    public void setPosition(String position) {

        this.position = position;
    }

    public void setRotorNotches(String rotorNotches) {

        this.rotorNotches = rotorNotches;
    }

    //<45,27,94><A(2)O(5)!(20)><III><A|Z,D|E>
    @Override
    public String toString()
    {
        String result = "";
        StringBuilder settingString = new StringBuilder();

        settingString.append(createStringRotors());
        settingString.append(createStartingPosition());
        settingString.append(createReflectorID());
        //settingString.append(createPlugPairs());

        return settingString.toString();
    }

    //<45,27,94>
    public StringBuilder createStringRotors() {

        StringBuilder rotorsString = new StringBuilder();
        String[] rotorsIDArray = rotorIDs.split(",");

        rotorsString.append("<");
        for (int i = 0; i < rotorsIDArray.length; i++) {
            String currentRotor = rotorsIDArray[i];

            rotorsString.append(currentRotor);
            if (i != rotorsIDArray.length - 1)
                rotorsString.append(",");
        }
        rotorsString.append(">");

        return rotorsString;
    }

    //<A(2)O(5)!(20)>
    public StringBuilder createStartingPosition() {

        StringBuilder startingPositionString = new StringBuilder();
        String[] rotorsNotchArray = null;
        if (!rotorNotches.equals("")) {
            rotorsNotchArray = rotorNotches.split(",");
        }

        startingPositionString.append("<");
        for (int i = 0; i < position.length(); i++) {
            startingPositionString.append(position.charAt(i));

            if (!rotorNotches.equals("")) {
                startingPositionString.append("(");
                startingPositionString.append(rotorsNotchArray[i]);
                startingPositionString.append(")");
            }

            if (i != position.length() - 1)
                startingPositionString.append(",");
        }
        startingPositionString.append(">");

        return startingPositionString;
    }

    //<III>
    public StringBuilder createReflectorID() {

        StringBuilder reflectorIDString = new StringBuilder();

        reflectorIDString.append("<");
        reflectorIDString.append(ReflectorID.convertStringToReflectorID(reflectorID).toString());
        reflectorIDString.append(">");

        return reflectorIDString;

    }

    //<A|Z,D|E>
    public StringBuilder createPlugPairs() {

        StringBuilder plugPairsString = new StringBuilder();

        if (plugPairs.equals(""))
        {
            plugPairsString.append("");

            return plugPairsString;
        }

        plugPairsString.append("<");

        for (int i = 0; i < plugPairs.length(); i+=2) {

            Character char1 = plugPairs.charAt(i);
            Character char2 = plugPairs.charAt(i + 1);

            plugPairsString.append(char1);
            plugPairsString.append("|");
            plugPairsString.append(char2);

            if (i != plugPairs.length() - 2)
                plugPairsString.append(",");
        }
        plugPairsString.append(">");

        return plugPairsString;
    }

    public String getRotorNotches() {

        return rotorNotches;
    }

    @Override
    public MachineSetting clone() {
        try {
            MachineSetting clone = (MachineSetting) super.clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}


