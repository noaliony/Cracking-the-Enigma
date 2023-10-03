package machine;
import components.*;
import components.Dictionary;
import enums.ReflectorID;
import exceptions.MessageToEncodeIsNotValidException;
import machine.details.ConfigurationDetails;
import machine.details.MachineDetailsObject;
import machine.details.MachineSetting;
import machineHistory.MachineHistory;
import machineHistory.ProcessedString;

import java.io.Serializable;
import java.util.*;

import static java.util.Arrays.asList;

public class Machine implements Serializable, Cloneable {

    private final KeyBoard keyBoard = new KeyBoard();
    private PlugBoard plugBoard;
    private List<Rotor> rotorsListInUse;
    private Reflector reflectorInUse = new Reflector();
    private Repository repository;
    private MachineSetting originalMachineSetting = new MachineSetting();
    private MachineSetting currentMachineSetting = new MachineSetting();
    private List<ProcessedString> currentIsEncodedStringList;
    private List<MachineHistory> machineHistoryList = new ArrayList<>();
    private MachineHistory currentMachineHistory;
    private final MachineDetailsObject machineDetailsObject = new MachineDetailsObject();
    private ProcessedString currentProcessedString = new ProcessedString();
    private Dictionary dictionary;
    private ConfigurationDetails configurationDetails;
    private long startTimeToEncodedString;
    private int amountOfIsEncodedMessages = 0;
    private final int rotorsCount;
    private boolean isCodeSet = false;
    private int maxValueAgentsCount;

    public Machine (List<Character> alphabet, PlugBoard plugBoard, int rotorsCount, Repository repository,
                    List<Character> excludeChars, Set<String> dictionary, int maxValueAgentsCount) {

        keyBoard.setAlphabet(alphabet);
        this.plugBoard = plugBoard;
        this.rotorsCount = rotorsCount;
        this.repository = repository;
        this.dictionary = new Dictionary(excludeChars, dictionary);
        this.maxValueAgentsCount = maxValueAgentsCount;
    }

    public int getMaxValueAgentsCount() {
        return maxValueAgentsCount;
    }

    public List<Character> getExcludeChars() {
        return dictionary.getExcludeChars();
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void resetStartTimeToEncodedString(){
        startTimeToEncodedString = System.nanoTime();
    }

    public long getNanoSecondThatStringWasProcessed(){
        long endTime = System.nanoTime();

        return endTime - startTimeToEncodedString;
    }

    public int getRotorsCount() {
        return rotorsCount;
    }

    public ProcessedString getCurrentProcessedString() {
        return currentProcessedString;
    }

    public void setCode(ConfigurationDetails configurationDetails) {
        List<Integer> rotorIDsToUse = configurationDetails.getRotorIDs();
        List<Character> rotorStartPosition = configurationDetails.getStartPosition();
        ReflectorID reflectorID = configurationDetails.getReflectorID();
        Map<Character,Character> plugPairs = configurationDetails.getPlugPairs();

        this.configurationDetails = configurationDetails;
        rotorsListInUse  = new ArrayList<>();
        for(int i = rotorIDsToUse.size() - 1; i >= 0; i--) {

            int idRotor = rotorIDsToUse.get(i);
            rotorsListInUse.add(repository.getRotorByID(idRotor));
        }

        for (int i = 0; i < rotorIDsToUse.size(); i++) {
            Rotor currentRotor = rotorsListInUse.get(i);
            int newNotch;

            int index = currentRotor.getIndexInRightArrayByChar(rotorStartPosition.get(rotorIDsToUse.size() - i - 1));
            currentRotor.setStartPosition(index);
            currentRotor.setCurrPosition(index);
            newNotch = moduloOperationToIndexBetweenRotors(currentRotor.getStartingNotch() - currentRotor.getCurrPosition() + 1);
            currentRotor.setNotch(newNotch);
        }

        reflectorInUse = repository.getReflectorByID(reflectorID);

        plugBoard.SetPlugPairs(plugPairs);
        isCodeSet = true;
    }

    public char inputProcess(char charToEncodeFromUser){
        char newChar;
        int indexFromKeyBoard;
        int indexToKeyBoard;
        int newIndexReflector;
        int firstRotorPosition;
        int lastRotorPosition;

        advanceNotch();
        firstRotorPosition = rotorsListInUse.get(0).getCurrPosition();
        lastRotorPosition = rotorsListInUse.get(rotorsListInUse.size()-1).getCurrPosition();

        newChar = plugBoard.getValueByKey(charToEncodeFromUser);
        indexFromKeyBoard = keyBoard.getIndexByCharFromAlphabetList(newChar);
        indexFromKeyBoard += firstRotorPosition;
        indexFromKeyBoard = moduloOperationToIndexBetweenRotors(indexFromKeyBoard);
        newIndexReflector = passingRotorsFromRightToLeft(indexFromKeyBoard);
        newIndexReflector -= lastRotorPosition;
        newIndexReflector = moduloOperationToIndexForKeyBoardAndReflector(newIndexReflector);
        newIndexReflector = reflectorInUse.getDataByIndex(newIndexReflector);
        newIndexReflector += lastRotorPosition;
        newIndexReflector = moduloOperationToIndexBetweenRotors(newIndexReflector);
        indexToKeyBoard = passingRotorsFromLeftToRight(newIndexReflector);
        indexToKeyBoard -= firstRotorPosition;
        indexToKeyBoard = moduloOperationToIndexForKeyBoardAndReflector(indexToKeyBoard);
        newChar = keyBoard.getCharByIndexFromAlphabetList(indexToKeyBoard);

        return plugBoard.getValueByKey(newChar);
    }

    private int moduloOperationToIndexBetweenRotors(int index) {
        if (index == keyBoard.getLength())
            return index;
        if (index == 0)
            return keyBoard.getLength();

        int resultIndex = (index < 0 ? index + keyBoard.getLength() : index % keyBoard.getLength());

        return  resultIndex;
    }

    private int moduloOperationToIndexForKeyBoardAndReflector(int index) {
        if (index == keyBoard.getLength())
            return --index;

        int resultIndex = (index < 0 ? index + keyBoard.getLength() : index % keyBoard.getLength());

        return  resultIndex;
    }

    private int passingRotorsFromLeftToRight(int newIndexReflector) {
        char charToFind;
        int newIndex = newIndexReflector;

        if(rotorsListInUse.size() == 1) {
            charToFind = rotorsListInUse.get(0).getLeftArray().get(newIndex);
            newIndex = rotorsListInUse.get(0).getRightArray().indexOf(charToFind);
            return newIndex;
        }

        for(int i = rotorsListInUse.size() - 1; i > 0 ; i--) {
            Rotor currRotor = rotorsListInUse.get(i);
            Rotor nextRotor = rotorsListInUse.get(i - 1);

            charToFind = currRotor.getLeftArray().get(newIndex);
            newIndex = currRotor.getRightArray().indexOf(charToFind);
            newIndex = newIndex - currRotor.getCurrPosition() + nextRotor.getCurrPosition();
            newIndex = moduloOperationToIndexBetweenRotors(newIndex);
        }

        Rotor lastRotor = rotorsListInUse.get(0);
        charToFind = lastRotor.getLeftArray().get(newIndex);
        newIndex = lastRotor.getRightArray().indexOf(charToFind);

        return newIndex;
    }

    private int passingRotorsFromRightToLeft(int indexFromKeyBoard) {
        char charToFind;
        int newIndex = indexFromKeyBoard;

        if(rotorsListInUse.size() == 1) {
            charToFind = rotorsListInUse.get(0).getRightArray().get(newIndex);
            newIndex = rotorsListInUse.get(0).getLeftArray().indexOf(charToFind);
            return newIndex;
        }

        for (int i = 0; i < rotorsListInUse.size() - 1; i++) {
            Rotor currRotor = rotorsListInUse.get(i);
            Rotor nextRotor = rotorsListInUse.get(i + 1);

            charToFind = currRotor.getRightArray().get(newIndex);
            newIndex = currRotor.getLeftArray().indexOf(charToFind);
            newIndex = newIndex - currRotor.getCurrPosition() + nextRotor.getCurrPosition();
            newIndex = moduloOperationToIndexBetweenRotors(newIndex);
        }

        Rotor lastRotor = rotorsListInUse.get(rotorsListInUse.size() - 1);
        charToFind = lastRotor.getRightArray().get(newIndex);
        newIndex = lastRotor.getLeftArray().indexOf(charToFind);


        return newIndex;
    }

    private void advanceNotch() {
       updateNotchAndCurrPosition(0);

        for (int i = 0; i < rotorsListInUse.size() - 1 ; i++) {
            if (rotorsListInUse.get(i).getNotch() == 1)
                updateNotchAndCurrPosition(i+1);
            else
                break;
        }
    }

    private void updateNotchAndCurrPosition(int index){

        rotorsListInUse.get(index).advanceCurrPosition();
        rotorsListInUse.get(index).decreaseNotch();
    }

    public Map<Character, Character> getPlugPairs() {

        return plugBoard.getPlugPairs();
    }

    public KeyBoard getKeyBoard(){
        return keyBoard;
    }

    public List<Rotor> getRotorsListInUse (){
        return rotorsListInUse;
    }

    public List<Rotor> getRotorsListInRepository() {

        return repository.getRotorsList();
    }

    public void setMachineSetting(String rotorIDs, String rotorNotches, String startingPosition, String reflectorID, String plugPairs) {

        originalMachineSetting = new MachineSetting(rotorIDs, rotorNotches, startingPosition, reflectorID, plugPairs);
        currentMachineSetting = new MachineSetting(rotorIDs, rotorNotches, startingPosition, reflectorID, plugPairs);
    }

    public MachineSetting getOriginalMachineSetting() {

        return originalMachineSetting;
    }

    public void updateCurrentMachineSetting() {

        String currentPositionsString = "";
        String currentNotchesString = "";
        Character currentPosition;
        Integer notch;

        for (int i = rotorsCount - 1; i >= 0 ; i--) {

            currentPosition = rotorsListInUse.get(i).getRightArray().get(rotorsListInUse.get(i).getCurrPosition());
            currentPositionsString = currentPositionsString.concat(currentPosition.toString());
        }
        currentMachineSetting.setPosition(currentPositionsString);

        for (int i = rotorsCount - 1; i >= 0 ; i--) {

            notch = rotorsListInUse.get(i).getNotch();
            currentNotchesString = currentNotchesString.concat(notch.toString());

            if (i != 0)
                currentNotchesString = currentNotchesString.concat(",");

        }
        currentMachineSetting.setRotorNotches(currentNotchesString);
    }

    public int getAmountOfReflectorsInRepository() {

        return  repository.getAmountOfReflectorsInRepository();
    }

    public String displayOriginalMachineSetting() {

        return originalMachineSetting.toString();
    }

    public String displayCurrentMachineSetting() {

        return currentMachineSetting.toString();
    }

    public void setRotorsNotchInOriginalAndCurrentMachineSetting(String rotorsNotch) {

        originalMachineSetting.setRotorNotches(rotorsNotch);
        currentMachineSetting.setRotorNotches(rotorsNotch);
    }

    public String getNotchInRotorsInUseString() {

        StringBuilder rotorsNotch = new StringBuilder();

        for (int i = rotorsCount - 1; i >= 0; i--) {

            rotorsNotch.append(rotorsListInUse.get(i).getNotch());
            rotorsNotch.append(",");
        }

        return rotorsNotch.substring(0, rotorsNotch.length() - 1);
    }

    public void resetCurrentCode() {

        int newNotch;

        currentMachineSetting.setRotorNotches(originalMachineSetting.getRotorNotches());
        currentMachineSetting.setPosition(originalMachineSetting.getPosition());
        if (rotorsListInUse != null) {
            for (Rotor currentRotor : rotorsListInUse) {

                newNotch = moduloOperationToIndexBetweenRotors(currentRotor.getStartingNotch() - currentRotor.getStartPosition() + 1);
                currentRotor.setNotch(newNotch);
                currentRotor.setCurrPosition(currentRotor.getStartPosition());
            }
        }
    }

    public void resetCurrentNotchAndPosition() {

        resetCurrentCode();
        if (rotorsListInUse != null) {
            for (Rotor currentRotor : rotorsListInUse) {

                currentRotor.setNotch(currentRotor.getStartingNotch());
                currentRotor.setCurrPosition(currentRotor.getStartPosition());
            }
        }

        plugBoard = new PlugBoard(keyBoard.getAlphabet());
    }

    public void advanceAmountOfIsEncodedMessages() {
        amountOfIsEncodedMessages++;
    }

    public List<ProcessedString> getCurrentIsEncodedStringList() {
        return currentIsEncodedStringList;
    }

    public List<MachineHistory> getMachineHistoryList() {
        return machineHistoryList;
    }

    public int getAmountOfIsEncodedMessages() {
        return amountOfIsEncodedMessages;
    }

    public boolean isCodeSet (){

        return isCodeSet;
    }

    public void addMachineSetting() {

        MachineSetting machineSetting = getOriginalMachineSetting();
        MachineHistory machineHistory;

        currentIsEncodedStringList = new ArrayList<>();
        machineHistory = new MachineHistory(machineSetting, currentIsEncodedStringList);
        currentMachineHistory = machineHistory;
        getMachineHistoryList().add(machineHistory);
    }

    public void initializeMachineDetailsObject() {

        machineDetailsObject.initializeMachineDetailsObject(getRotorsCount(), getAmountOfRotorsInRepository(),
                getAmountOfReflectorsInRepository(), getAmountOfIsEncodedMessages());
    }

    public int getAmountOfRotorsInRepository() {
        return repository.getAmountOfRotors();
    }

    public MachineDetailsObject getMachineDetailsObject() {
        return machineDetailsObject;
    }

    public MachineSetting getCurrentMachineSetting() {
        return currentMachineSetting;
    }

    public void insertCurrentProcessedStringToMachineHistoryList() {
        currentIsEncodedStringList.add(currentProcessedString);
        updateCurrenProcessedStringInCurrentMachineHistory(currentProcessedString);
        currentProcessedString = new ProcessedString();
    }

    public String encodeFullMessage(String stringToEncodeFromUser) {
        Character outputChar;
        String outputString = "";

        for (int i = 0; i < stringToEncodeFromUser.length(); i++) {
            outputChar = inputProcess(stringToEncodeFromUser.charAt(i));
            outputString = outputString.concat(outputChar.toString());
        }

        return outputString;
    }

    public List<Character> getAlphabet() {
        return keyBoard.getAlphabet();
    }

    public List<ReflectorID> getReflectorIDsInRepositoryList() {
        return repository.getReflectorIDsList();
    }

    @Override
    public Machine clone() {
        try {
            Machine newMachine = (Machine)super.clone();

            newMachine.repository = repository.clone();
            newMachine.rotorsListInUse = new ArrayList<>();
            for (Rotor realRotor: rotorsListInUse) {
                for (Rotor cloneRotor : newMachine.repository.getRotorsList()) {
                    if (cloneRotor.getID() == realRotor.getID()) {
                        newMachine.rotorsListInUse.add(cloneRotor);
                    }
                }
            }
            newMachine.reflectorInUse = reflectorInUse.clone();
            newMachine.originalMachineSetting = originalMachineSetting.clone();
            newMachine.currentMachineSetting = currentMachineSetting.clone();
            newMachine.currentIsEncodedStringList = new ArrayList<>(this.currentIsEncodedStringList);
            newMachine.machineHistoryList = new ArrayList<>(this.machineHistoryList);

            return newMachine;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCurrenProcessedStringInCurrentMachineHistory(ProcessedString processedString) {
        currentMachineHistory.setCurrentProcessedString(processedString);
    }

    public MachineHistory getCurrentMachineHistory() {
        return currentMachineHistory;
    }

    public ConfigurationDetails getConfigurationDetails() {
        return configurationDetails;
    }

    public String validatorMessageToEncode(String messageToEncode) throws MessageToEncodeIsNotValidException {
        String messageToEncodeAfter = messageToEncode
                .chars()
                .filter(character -> !dictionary.getExcludeChars().contains((char) character))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        if (!dictionary.getDictionary().containsAll(asList(messageToEncodeAfter.split(" ")))){
            throw new MessageToEncodeIsNotValidException();
        } else {
            return messageToEncodeAfter;
        }
    }
}

