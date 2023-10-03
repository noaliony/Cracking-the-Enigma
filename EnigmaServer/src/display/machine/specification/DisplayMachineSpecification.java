package display.machine.specification;

import engine.Engine;

public class DisplayMachineSpecification {
    private Engine engine;
    private String newLine = System.lineSeparator();

    public DisplayMachineSpecification(Engine engine) {
        this.engine = engine;
    }

    public StringBuilder buildMachineSpecificationString() {
        StringBuilder machineDetailsObjectString = new StringBuilder();
        String newLine = System.lineSeparator();

        machineDetailsObjectString.append(buildAmountOfRotorsString());
        machineDetailsObjectString.append(buildAmountOfReflectorsString());
        machineDetailsObjectString.append(buildAmountOfIsEncodedMessagesString());
        if (engine.isCodeSet())
            machineDetailsObjectString.append(buildMachineSettingString());
        else {
            machineDetailsObjectString
                    .append("4. There are no original code description because no code has been configured on the machine")
                    .append(newLine);
            machineDetailsObjectString
                    .append("5. There are no current code description because no code has been configured on the machine")
                    .append(newLine);
        }

        return machineDetailsObjectString;
    }

    private String buildAmountOfIsEncodedMessagesString() {

        int amountOfIsEncodedMessages = engine.getAmountOfIsEncodedMessages();

        return "3. Amount of is encoded messages :" + amountOfIsEncodedMessages + newLine;
    }

    private String buildAmountOfReflectorsString() {

        int amountOfReflectors = engine.getAmountReflectorsInRepository();

        return "2. Amount of reflectors: " + amountOfReflectors + newLine;
    }

    private String buildAmountOfRotorsString() {

        int amountRotorsInUse = engine.getAmountRotorsInUse();
        int amountRotorsInRepository = engine.getAmountRotorsInRepository();

        return "1. Amount of rotors: " + amountRotorsInUse + "/" + amountRotorsInRepository + newLine;
    }

    private StringBuilder buildMachineSettingString() {

        StringBuilder originalAndCurrentCodeConfiguration = new StringBuilder();
        String originalMachineSettingString = engine.displayOriginalMachineSetting();
        String currentMachineSettingString = engine.displayCurrentMachineSetting();

        originalAndCurrentCodeConfiguration
                .append("4. The original code description: ")
                .append(originalMachineSettingString)
                .append(newLine);
        originalAndCurrentCodeConfiguration
                .append("5. The current code description: ")
                .append(currentMachineSettingString)
                .append(newLine);

        return originalAndCurrentCodeConfiguration;
    }
}
