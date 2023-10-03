package machine.details;

public class MachineDetailsObject {

    private Integer amountOfRotorsInUse;
    private Integer possibleAmountOfRotors;
    private Integer amountOfReflectors;
    private Integer amountOfIsEncodedMessages;

    public void initializeMachineDetailsObject(int rotorsCount, int amountOfRotorsInRepository,
                                               int amountOfReflectorsInRepository, int amountOfIsEncodedMessages) {
        amountOfRotorsInUse = rotorsCount;
        possibleAmountOfRotors = amountOfRotorsInRepository;
        amountOfReflectors = amountOfReflectorsInRepository;
        this.amountOfIsEncodedMessages = amountOfIsEncodedMessages;
    }

    public Integer getAmountOfRotorsInUse() {
        return amountOfRotorsInUse;
    }

    public Integer getPossibleAmountOfRotors() {
        return possibleAmountOfRotors;
    }

    public Integer getAmountOfReflectors() {
        return amountOfReflectors;
    }

    public Integer getAmountOfIsEncodedMessages() {
        return amountOfIsEncodedMessages;
    }
}
