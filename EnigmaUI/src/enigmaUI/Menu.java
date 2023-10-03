package enigmaUI;
import engine.Engine;
import machineHistory.MachineHistory;
import machineHistory.ProcessedString;
import enums.*;
import exceptions.*;
import machine.MachineSetting;
import xml.MachineBuilder;
import xml.ReadXML;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;


public class Menu {

    private Engine engine;
    private String newLine = System.lineSeparator();
    private boolean readFilePressed = false;
    private boolean manualSettingPressed = false;
    private boolean automaticSettingPressed = false;

    public void showOptions() {

        MenuOptions userSelection = MenuOptions.NOSELECTION;

        engine = new Engine();
        displayWelcome();
        while ((userSelection != MenuOptions.EXIT)) {
            displayMenu();
            userSelection = getValidUserSelection();
            String pathWithFileName;
            switch (userSelection){

                case READFILE:
                    try {
                        String pathXML = getPathFromUser();
                        ReadXML.checkIfValidXMLFile(pathXML);
                        MachineBuilder machineBuilder = new MachineBuilder();
                        engine.setMachine(machineBuilder.createMachineFromCTEEnigma(ReadXML.getCTEEnigma()));
                        System.out.println("The file was received successfully!" + newLine);
                        readFilePressed = true;
                        manualSettingPressed = automaticSettingPressed = false;

                    } catch (Exception exception) {

                        System.out.println(exception.getMessage());
                        System.out.println("You return to the main menu" + newLine);
                    }
                    break;

                case SHOWMACHINESPECIFICATION:
                    if (readFilePressed) {
                        DisplayMachineSpecification displayMachineSpecification = new DisplayMachineSpecification();

                        displayMachineSpecification.showMachineSpecification(engine, manualSettingPressed || automaticSettingPressed);
                    }
                    else {
                        needToLoadMachine();
                    }
                    break;

                case MANUALSETTING:
                    if (readFilePressed) {
                        InitialDefinition initialDefinition = new InitialDefinition();

                        engine.resetCurrentNotchAndPosition();
                        try {
                            initialDefinition.getFromUserManualSetting(engine.getAlphabet(), engine.getRotorsCount(), engine.getRotorsListInRepository(), engine);
                            engine.manualSetting();
                            engine.addMachineSetting();
                            manualSettingPressed = true;
                            System.out.println("The input was successful!" + newLine);
                        } catch (ReturnToMainMenuException exception) { }
                    }
                    else {
                        needToLoadMachine();
                    }
                    break;

                case AUTOMATICSETTING:
                    if (readFilePressed) {
                        String currentMachineSettingString;

                        engine.resetCurrentNotchAndPosition();
                        engine.getRandomSetting();
                        engine.addMachineSetting();
                        automaticSettingPressed = true;
                        System.out.println("The automatic input was successful!");
                        currentMachineSettingString = engine.displayCurrentMachineSetting();
                        System.out.println("The automatic code description: " + currentMachineSettingString + newLine);

                    }
                    else {
                        needToLoadMachine();
                    }
                    break;

                case INPUTPROCESSING:
                    if (readFilePressed && (manualSettingPressed || automaticSettingPressed)) {
                        try {
                            Instant start = Instant.now();
                            String stringToEncodeFromUser = getStringToEncodeFromUser();
                            String stringOutput = engine.encodingStringInput(stringToEncodeFromUser);
                            System.out.println("The string " + stringToEncodeFromUser + " is encoded to string " + stringOutput + newLine);
                            Duration totalTime = Duration.between(start, Instant.now());
                            engine.addProcessedStringToListInMachineHistory(stringToEncodeFromUser, stringOutput, totalTime.getNano());
                            engine.updateCurrentMachineSetting();
                        } catch (ReturnToMainMenuException exception) { }
                    }
                    else if (!readFilePressed) {
                         needToLoadMachine();
                    }
                    else {
                        needToSetCode();
                    }
                    break;

                case RESETCURRENTCODE:
                    if (readFilePressed && (manualSettingPressed || automaticSettingPressed)) {
                        engine.resetCurrentCode();
                        System.out.println("The reset successful!" + newLine);
                    }
                    else if (!readFilePressed) {
                        needToLoadMachine();
                    }
                    else {
                        needToSetCode();
                    }
                    break;

                case HISTORYANDSTATISTICS:
                    if (readFilePressed && (manualSettingPressed || automaticSettingPressed))
                        displayProcessedStringList();
                    else if (!readFilePressed) {
                        needToLoadMachine();
                    }
                    else {
                        needToSetCode();
                    }
                    break;

                case SAVETOFILE:
                    if (readFilePressed) {
                        pathWithFileName = engine.getPathWithFileNameFromUserForSaveAndLoad();
                        try {
                            engine.saveMachineToFile(pathWithFileName);
                            System.out.println("Machine save to file is successful!" + newLine);
                        }catch (RuntimeException exception){
                            System.out.println(exception.getMessage());
                        }
                    }
                    else {
                        needToLoadMachine();
                    }

                    break;

                case LOADFROMFILE:
                    pathWithFileName = engine.getPathWithFileNameFromUserForSaveAndLoad();
                    engine.loadMachineFromFile(pathWithFileName);
                    System.out.println("Machine load from file is successful!" + newLine);
                    manualSettingPressed = engine.isCodeSet();
                    readFilePressed = true;
                    break;

                case EXIT:
                    printGoodBye();
                    break;
            }
        }
    }

    private void needToLoadMachine() {
        System.out.println("You need to load an XML file! (by pressing option #1)" + newLine);
    }

    private void needToSetCode() {
        System.out.println("You need to initialize the machine settings! (by pressing option #3 or #4)" + newLine);
    }

    public String getPathFromUser() {

        String newLine = System.lineSeparator();
        System.out.println(newLine + "Please enter path to read from a file :");
        Scanner scanner = new Scanner(System.in);
        String pathFromUser = scanner.nextLine();

        return pathFromUser;
    }

    private void printGoodBye() {

        StringBuilder goodBye = new StringBuilder();

        goodBye.append("*********************************************").append(newLine);
        goodBye.append("*                                           *").append(newLine);
        goodBye.append("*                  Goodbye                  *").append(newLine);
        goodBye.append("*                                           *").append(newLine);
        goodBye.append("*********************************************").append(newLine);

        System.out.println(goodBye);
    }


    private void displayWelcome() {

        StringBuilder welcome = new StringBuilder();

        welcome.append("*********************************************").append(newLine);
        welcome.append("*                                           *").append(newLine);
        welcome.append("*       Welcome To The Enigma Machine       *").append(newLine);
        welcome.append("*                                           *").append(newLine);
        welcome.append("*********************************************").append(newLine);

        System.out.println(welcome);
    }

    private void displayMenu() {

        StringBuilder menu = new StringBuilder();

        menu.append("Main menu:").append(newLine);
        menu.append("----------").append(newLine);
        menu.append("1. Read XML file").append(newLine);
        menu.append("2. Show machine specification").append(newLine);
        menu.append("3. Manual setting").append(newLine);
        menu.append("4. Automatic setting").append(newLine);
        menu.append("5. Input processing").append(newLine);
        menu.append("6. Reset current code").append(newLine);
        menu.append("7. History and statistic").append(newLine);
        menu.append("8. Save to file").append(newLine);
        menu.append("9. Load from file").append(newLine);
        menu.append("10. Exit").append(newLine);
        menu.append("Please choose number of the relevant option and then press enter (For example : 1)");

        System.out.println(menu);
    }

    private void displayProcessedStringList() {

        List<MachineHistory> machineHistoryList = engine.getMachineHistory();

        for (MachineHistory currentMachineHistory : machineHistoryList) {

            System.out.println("Machine setting:");
            printMachineSetting(currentMachineHistory.getMachineSetting());
            if (currentMachineHistory.getProcessedStringList().size() != 0) {
                System.out.println("Processed string:");
                printProcessedStrings(currentMachineHistory.getProcessedStringList());
            }

            System.out.println();
        }
    }

    private void printProcessedStrings(List<ProcessedString> processedStringList) {

        for (int i = 0; i < processedStringList.size(); i++) {

            System.out.print(i+1 + ". ");
            System.out.println(processedStringList.get(i).toString());
        }
    }

    private void printMachineSetting(MachineSetting machineSetting) {

        System.out.println(machineSetting.toString());
    }

    private void printMessageGetStringToEncodedFromUser() {

        System.out.println("Please press string to encode from this list of characters :");
        printAlphabet();
        System.out.println(newLine + "And after press enter");
    }

    private String getStringToEncodeFromUser() throws ReturnToMainMenuException {

        InitialDefinition initialDefinition = new InitialDefinition();
        Scanner scanner = new Scanner(System.in);
        String stringToEncode = "";
        boolean checkValidity;

        printMessageGetStringToEncodedFromUser();

        do {
            stringToEncode = scanner.nextLine();
            stringToEncode = stringToEncode.toUpperCase();
            checkValidity = engine.checkIFTheInputValid(stringToEncode);

            if (!checkValidity) {
                initialDefinition.askUserIfReturnToMainOrContinue("The characters are not valid. Please press only characters from the list above and after press enter");
                printMessageGetStringToEncodedFromUser();
            }

        } while(!checkValidity);

        return  stringToEncode;

    }

    private void printAlphabet() {

        for (Character character : engine.getAlphabet())
            System.out.print(character + " ");
    }

    private MenuOptions getValidUserSelection() {

        MenuOptions userSelection;
        boolean validUserSelection = false;

        do {
            Scanner scanner = new Scanner(System.in);
            String userSelectionString = scanner.nextLine();
            userSelection = MenuOptions.convertFromStringToMenuOptions(userSelectionString);
            validUserSelection = userSelection != null;
            if (!validUserSelection)
                System.out.println("The selection was not valid - please choose only number between 1 - 8 and then press enter");

        } while (!validUserSelection);

        return userSelection;
    }


}
