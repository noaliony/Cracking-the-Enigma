package machine;

import enums.ReflectorID;

import java.io.Serializable;

public class MachineSetting implements Serializable {

    private String rotorsID; //45,27,94
    private String position;
    private String reflectorID;
    private String plugPairs;
    private String rotorsNotch; //2,5,20

    public String getRotorsID() {

        return rotorsID;
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

    public void setRotorsID(String rotorsID) {

        this.rotorsID = rotorsID;
    }

    public void setPosition(String position) {

        this.position = position;
    }

    public void setReflectorID(String reflectorID) {

        this.reflectorID = reflectorID;
    }

    public void setPlugPairs(String plugPairs) {

        this.plugPairs = plugPairs;
    }

    public void setRotorsNotch(String rotorsNotch) {

        this.rotorsNotch = rotorsNotch;
    }

    //<45(2),27(5),94(20)><AO!><III><A|Z,D|E>
    @Override
    public String toString()
    {
        String result = "";
        StringBuilder settingString = new StringBuilder();

        settingString.append(createStringRotors());
        settingString.append(createStartingPosition());
        settingString.append(createReflectorID());
        settingString.append(createPlugPairs());

        return settingString.toString();
    }

    //<45(2),27(5),94(20)>
    private StringBuilder createStringRotors() {

        StringBuilder rotorsString = new StringBuilder();

        rotorsString.append("<");
        String[] rotorsIDArray = rotorsID.split(",");
        String[] rotorsNotchArray = rotorsNotch.split(",");

        for (int i = 0; i < rotorsIDArray.length; i++) {

            String currentRotor = rotorsIDArray[i];

            rotorsString.append(currentRotor);
            rotorsString.append("(");
            rotorsString.append(rotorsNotchArray[i]);
            rotorsString.append(")");

            if (i != rotorsIDArray.length - 1)
                rotorsString.append(",");

        }
        rotorsString.append(">");

        return rotorsString;
    }

    //<AO!>
    private StringBuilder createStartingPosition() {

        StringBuilder startingPositionString = new StringBuilder();

        startingPositionString.append("<");
        startingPositionString.append(position);
        startingPositionString.append(">");

        return startingPositionString;
    }

    //<III> , check if return III or 3
    private StringBuilder createReflectorID() {

        StringBuilder reflectorIDString = new StringBuilder();

        reflectorIDString.append("<");
        reflectorIDString.append(ReflectorID.convertFromStringToReflectorID(reflectorID).toString());
        reflectorIDString.append(">");

        return reflectorIDString;

    }

    //<A|Z,D|E>
    private StringBuilder createPlugPairs() {

        StringBuilder plugPairsString = new StringBuilder();

        if(plugPairs.equals(""))
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

    public String getRotorsNotch() {

        return rotorsNotch;
    }
}


