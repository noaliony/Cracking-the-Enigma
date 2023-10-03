package enigmaUI;

import components.Rotor;
import engine.Engine;
import exceptions.ReturnToMainMenuException;
import generated.CTERotor;

import java.time.Instant;
import java.util.*;

public class InitialDefinition {

    private final String newLine =  System.lineSeparator();
    private Scanner scanner = new Scanner(System.in);


    public void getFromUserManualSetting(List<Character> alphabet, int rotorsCount, List<Rotor> rotorsListInRepository, Engine engine) throws ReturnToMainMenuException {

        String rotorsID = getValidIDRotors(rotorsCount, rotorsListInRepository, engine);
        String rotorsNotch = createRotorsNotch(rotorsID, rotorsListInRepository);
        String startingPosition = getValidStartingPosition(alphabet, rotorsCount);
        String reflectorID = getValidReflectorID();
        String plugPairs = getValidPlugBoard();

        engine.setMachineSetting(rotorsID, rotorsNotch, startingPosition, reflectorID, plugPairs);
    }

    public String createRotorsNotch(String rotorsID, List<Rotor> rotorsListInRepository) {

        return Engine.createRotorsNotch(rotorsID, rotorsListInRepository);
    }

    private void printMessageGetPlugBoard() {

        System.out.println(newLine + "Please press an even number of characters for the plug board." +
                newLine + "Any two numbers will be a pair of plugs and finally press enter" +
                newLine + "(For example : AF@EL1ER)" +
                newLine + "If you don't want to use with plug board please press only enter");
    }

    private String getValidPlugBoard() throws ReturnToMainMenuException {

        String plugBoard = "";
        boolean validInput = false;

        printMessageGetPlugBoard();
        do {
            try {
                plugBoard = scanner.nextLine();
                plugBoard = plugBoard.toUpperCase();
                validInput = checkValidityPlugBoard(plugBoard);
                if (!validInput) {
                    askUserIfReturnToMainOrContinue("Please follow the instructions - note that you key in an even number and not the characters written above");
                    printMessageGetPlugBoard();
                }
            } catch (InputMismatchException exception) {
                askUserIfReturnToMainOrContinue("Please follow the instructions - note that you key in an even number and not the characters written above");
                printMessageGetPlugBoard();

            }

        } while (!validInput);

        return plugBoard;
    }

    public void askUserIfReturnToMainOrContinue(String messageValidity) throws ReturnToMainMenuException {

        String userSelection;
        boolean validInput;

        System.out.println(messageValidity);
        System.out.println("Would yoe like to try again or return to the main menu?");
        System.out.println("1. Try again" + newLine + "2. Return to the main menu");

        userSelection = scanner.nextLine();
        do{
            validInput = userSelection.equals("1") || userSelection.equals("2");

            if(!validInput){
                System.out.println("Please press only 1 or 2");
                userSelection = scanner.nextLine();
            }

        } while (!validInput);

        if (userSelection.equals("2"))
            throw new ReturnToMainMenuException();
    }

    private boolean checkValidityPlugBoard(String plugBoardOutput) {

        boolean checkValidity = true;
        Set<Character> plugBoardsSet = new HashSet<>();
        Character charInPlugBoard;

        for (int i = 0; i < plugBoardOutput.length(); i++) {
            charInPlugBoard = plugBoardOutput.charAt(i);
            plugBoardsSet.add(charInPlugBoard);
        }
        if(plugBoardsSet.size() != plugBoardOutput.length() || plugBoardOutput.length() % 2 !=0)
            checkValidity = false;

        return checkValidity;
    }

    private void printMessageGetReflectorID() {

        System.out.println(newLine + "Please press the number of reflector and finally press enter" +
                newLine + "1. I" + newLine + "2. II" + newLine + "3. III" + newLine + "4. IV" + newLine + "5. V" +
                newLine + "(For example : 2)");
    }

    private String getValidReflectorID() throws ReturnToMainMenuException {

        String reflectorIDOutput = "";
        boolean validInput = false;

        printMessageGetReflectorID();
        do {
            try {
                reflectorIDOutput = scanner.nextLine();
                validInput = checkValidityReflectorID(reflectorIDOutput);

                if (!validInput) {
                    askUserIfReturnToMainOrContinue("Please press only 1 - 5 !");
                    printMessageGetReflectorID();
                }
            } catch (InputMismatchException exception) {

                askUserIfReturnToMainOrContinue("Please press only 1 - 5 !");
                printMessageGetReflectorID();
            }

        } while (!validInput);

        return reflectorIDOutput;
    }

    private boolean checkValidityReflectorID(String userInput) {

        return Objects.equals(userInput, "1") || Objects.equals(userInput, "2") || Objects.equals(userInput, "3")
                || Objects.equals(userInput, "4") || Objects.equals(userInput, "5");
    }

    private void printMessageGetStartingPosition(int rotorsCount) {

        System.out.println(newLine + "Please press " + rotorsCount + " characters to the starting position of the rotors (without spaces) and finally press enter" +
                newLine + "(For example : A!U@)");
    }

    private String getValidStartingPosition(List<Character> alphabet, int rotorsCount) throws ReturnToMainMenuException {

        String startingPositionOutput = "";
        boolean validInput = false;

        printMessageGetStartingPosition(rotorsCount);
        do {
            try {
                startingPositionOutput = scanner.nextLine();
                startingPositionOutput = startingPositionOutput.toUpperCase();
                validInput = checkValidityStartingPosition(startingPositionOutput, alphabet, rotorsCount);
                if (!validInput)
                    askUserIfReturnToMainOrContinue("Please follow the instructions - note that you are entering a char that appears in the alphabet");
                    printMessageGetStartingPosition(rotorsCount);

            } catch (InputMismatchException exception) {

                askUserIfReturnToMainOrContinue("Please follow the instructions - note that you are entering a char that appears in the alphabet");
                printMessageGetStartingPosition(rotorsCount);

            }

        } while (!validInput);

        return startingPositionOutput;
    }

    private boolean checkValidityStartingPosition(String startingPositionOutput, List<Character> alphabet, int rotorsCount) {
        boolean checkValidity = true;

        if(startingPositionOutput.length() != rotorsCount)
            return false;

        for (int i = 0; i < startingPositionOutput.length(); i++) {
            checkValidity &= alphabet.contains(startingPositionOutput.charAt(i));
        }
        return checkValidity;
    }

    private void printMessageGetIDRotors(int rotorsCount, List<Rotor> rotorsListInRepository, Engine engine) {

        System.out.println(newLine + "Please press " + rotorsCount + " numbers of rotors to use from those ID rotors: ");
        printAllIDRotorsInRepository(engine.createRotorsIDOptionalInRepository(rotorsListInRepository));
        System.out.println(newLine + "Write , between them to separate (without spaces) and finally press enter" +
                newLine + "(For example : 20,1,4,14)");
    }

    private String getValidIDRotors(int rotorsCount, List<Rotor> rotorsListInRepository, Engine engine) throws ReturnToMainMenuException {

        String IDRotorsOutput = "";
        boolean validInput = false;

        printMessageGetIDRotors(rotorsCount, rotorsListInRepository, engine);
        do {
            try {
                IDRotorsOutput = scanner.nextLine();
                validInput = checkValidityRotorsID(IDRotorsOutput, rotorsCount, rotorsListInRepository, engine);
                if (!validInput) {

                    askUserIfReturnToMainOrContinue("Please follow the instructions - note that you are entering a char that appears in the alphabet");
                    printMessageGetIDRotors(rotorsCount, rotorsListInRepository, engine);
                }

            } catch (InputMismatchException | NumberFormatException exception) {

                askUserIfReturnToMainOrContinue("Please follow the instructions - note that you are entering a char that appears in the alphabet");
                printMessageGetIDRotors(rotorsCount, rotorsListInRepository, engine);
            }

        } while (!validInput);

        return IDRotorsOutput;
    }

    private void printAllIDRotorsInRepository(List<Integer> rotorsIDOptionalInRepository) {

        for (Integer idRotor : rotorsIDOptionalInRepository)
            System.out.print(idRotor + " ");
    }

    private boolean checkValidityRotorsID(String IDRotorsOutput, int rotorsCount, List<Rotor> rotorsListInRepository,
                                          Engine engine) throws NumberFormatException {
        if (IDRotorsOutput.equals(""))
            return false;

        boolean checkValidity = true;
        String[] arrayOfStringsToRotorsID;
        Set<String> rotorsIDSet = new HashSet<>();
        List<Integer> newRotorsID;
        int countSeparator = calculateCountOfMaxRotors(IDRotorsOutput);

        if(countSeparator + 1 != rotorsCount)
            return false;

        newRotorsID = engine.createRotorsIDOptionalInRepository(rotorsListInRepository);
        arrayOfStringsToRotorsID = IDRotorsOutput.split(",", countSeparator + 1);

        for (String currentString : arrayOfStringsToRotorsID) {

            checkValidity &= newRotorsID.contains(Integer.parseInt(currentString));
            rotorsIDSet.add(currentString);
        }
        checkValidity &= rotorsIDSet.size() == arrayOfStringsToRotorsID.length;

        return checkValidity;
    }


    private int calculateCountOfMaxRotors(String IDRotors) {
        int count = 0;

        for (int i = 0; i < IDRotors.length(); i++) {
            if (IDRotors.charAt(i) == ',')
                count++;
        }

        return count;
    }

}
