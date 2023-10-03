package machine.details;

import java.util.List;

public class MachineDetailsObject {

    private Integer amountOfRotorsInUse;
    private Integer possibleAmountOfRotors;
    private Integer amountOfReflectors;
    private Integer amountOfIsEncodedMessages; //must set function!!!
    private List<Character> alphabet;

    public void initializeMachineDetailsObject(int rotorsCount, int amountOfRotorsInRepository,
                                               int amountOfReflectorsInRepository, int amountOfIsEncodedMessages,
                                               List<Character> alphabet) {
        amountOfRotorsInUse = rotorsCount;
        possibleAmountOfRotors = amountOfRotorsInRepository;
        amountOfReflectors = amountOfReflectorsInRepository;
        this.amountOfIsEncodedMessages = amountOfIsEncodedMessages;
        this.alphabet = alphabet;
    }

    public Integer getAmountOfRotorsInUse() { return amountOfRotorsInUse; }
    public Integer getPossibleAmountOfRotors() {
        return possibleAmountOfRotors;
    }
    public Integer getAmountOfReflectors() { return amountOfReflectors; }
    public Integer getAmountOfIsEncodedMessages() {
        return amountOfIsEncodedMessages;
    }
    public List<Character> getAlphabet() { return alphabet; }
}
