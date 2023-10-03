package enigmaUI;

import engine.Engine;

public class DisplayMachineSpecification {

    private Engine engine;

    public void showMachineSpecification(Engine engine, boolean isExistMachineSetting) {

        String newLine = System.lineSeparator();
        this.engine = engine;
        displayAmountOfRotors();
        displayAmountOfReflectors();
        displayAmountOfIsEncodedMessages();
        if (isExistMachineSetting)
            displayMachineSetting();
        else {
            System.out.println("4. There are no original code description");
            System.out.println("5. There are no current code description" + newLine);
        }
    }

    private void displayAmountOfIsEncodedMessages() {

        int amountOfIsEncodedMessages = engine.getAmountOfIsEncodedMessages();

        System.out.println("3. Amount of is encoded messages :" + amountOfIsEncodedMessages);
    }

    private void displayAmountOfReflectors() {

        int amountOfReflectors = engine.getAmountReflectorsInRepository();

        System.out.println("2. Amount of reflectors: " + amountOfReflectors);
    }

    private void displayAmountOfRotors() {

        int amountRotorsInUse = engine.getAmountRotorsInUse();
        int amountRotorsInRepository = engine.getAmountRotorsInRepository();

        System.out.println("1. Amount of rotors in use: " + amountRotorsInUse);
        System.out.println("   Possible amount of rotors: " + amountRotorsInRepository);
    }

    public void displayMachineSetting() {

        String originalMachineSettingString = engine.displayOriginalMachineSetting();
        String currentMachineSettingString = engine.displayCurrentMachineSetting();

        System.out.println("4. The original code description:" + originalMachineSettingString);
        System.out.println("5. The current code description:" + currentMachineSettingString);
        System.out.println(System.lineSeparator());
    }
}
