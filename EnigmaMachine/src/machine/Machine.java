package machine;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import components.*;
import enums.ReflectorID;
import machineHistory.MachineHistory;
import machineHistory.ProcessedString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Machine implements Serializable {

    private KeyBoard lightBulbKeyBoard;
    private KeyBoard keyBoard = new KeyBoard();
    private PlugBoard plugBoard;
    private List<Rotor> rotorsListInUse;
    private Reflector reflectorInUse = new Reflector();
    private int rotorsCount;
    private Repository repository;
    private MachineSetting originalMachineSetting = new MachineSetting();
    private MachineSetting currentMachineSetting = new MachineSetting();
    private List<ProcessedString> currentIsEncodedStringList;
    private List<MachineHistory> machineHistoryList = new ArrayList<>();
    private int amountOfIsEncodedMessages = 0;
    boolean isCodeSet = false;

    public Machine (List<Character> alphabet, PlugBoard plugBoard, int rotorsCount, Repository repository) {

        keyBoard.setAlphabet(alphabet);
        this.plugBoard = plugBoard;
        this.rotorsCount = rotorsCount;
        this.repository = repository;
    }

    public int getRotorsCount() {

        return rotorsCount;
    }

    public void setCode(List<Integer> rotorsIdToUse, List<Character> rotorStartPosition, ReflectorID reflectorID,
    Map<Character,Character> plugPairs) {

        rotorsListInUse  = new ArrayList<>();
        for(int i = rotorsIdToUse.size() - 1; i >= 0; i--) {

            int idRotor = rotorsIdToUse.get(i);
            rotorsListInUse.add(repository.getRotorByID(idRotor));
        }

        for (int i = 0; i < rotorsIdToUse.size(); i++) {
            Rotor currentRotor = rotorsListInUse.get(i);
            int newNotch;

            int index = currentRotor.getIndexInRightArrayByChar(rotorStartPosition.get(rotorsIdToUse.size() - i - 1));
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

    private int moduloOperationToIndexBetweenRotors(int index)
    {
        if (index == keyBoard.getLength())
            return index;
        if (index == 0)
            return keyBoard.getLength();

        int resultIndex = (index < 0 ? index + keyBoard.getLength() : index % keyBoard.getLength());

        return  resultIndex;
    }

    private int moduloOperationToIndexForKeyBoardAndReflector(int index)
    {
        if (index == keyBoard.getLength())
            return index--;

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
        }
    }

    private void updateNotchAndCurrPosition(int index){

        rotorsListInUse.get(index).advanceCurrPosition();
        rotorsListInUse.get(index).decreaseNotch();
    }

    public void print()
    {
        System.out.println(keyBoard.toString());
        System.out.println(repository.toString());
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

    public void setMachineSetting(String rotorsID, String rotorsNotch, String startingPosition, String reflectorID, String plugPairs) {

        originalMachineSetting = new MachineSetting();
        currentMachineSetting = new MachineSetting();

        originalMachineSetting.setRotorsID(rotorsID);
        originalMachineSetting.setRotorsNotch(rotorsNotch);
        originalMachineSetting.setPosition(startingPosition);
        originalMachineSetting.setReflectorID(reflectorID);
        originalMachineSetting.setPlugPairs(plugPairs);

        currentMachineSetting.setRotorsID(rotorsID);
        currentMachineSetting.setRotorsNotch(rotorsNotch);
        currentMachineSetting.setPosition(startingPosition);
        currentMachineSetting.setReflectorID(reflectorID);
        currentMachineSetting.setPlugPairs(plugPairs);
    }

    public MachineSetting getOriginalMachineSetting() {

        return originalMachineSetting;
    }

    public void updateCurrentMachineSetting() {

        String currentPositionsString = "";
        String currentNotchesString = "";
        Character currentPosition;
        Integer notch;
        int indexPosition;

        for (int i = rotorsCount - 1; i >= 0 ; i--) {

            indexPosition = rotorsListInUse.get(i).getCurrPosition() - 1;
            currentPosition = keyBoard.getAlphabet().get(indexPosition);
            currentPositionsString = currentPositionsString.concat(currentPosition.toString());
        }
        currentMachineSetting.setPosition(currentPositionsString);

        for (int i = rotorsCount - 1; i >= 0 ; i--) {

            notch = rotorsListInUse.get(i).getNotch();
            currentNotchesString = currentNotchesString.concat(notch.toString());

            if (i != 0)
                currentNotchesString = currentNotchesString.concat(",");

        }
        currentMachineSetting.setRotorsNotch(currentNotchesString);
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

        originalMachineSetting.setRotorsNotch(rotorsNotch);
        currentMachineSetting.setRotorsNotch(rotorsNotch);
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

        currentMachineSetting.setRotorsNotch(originalMachineSetting.getRotorsNotch());
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
        getMachineHistoryList().add(machineHistory);
    }
}

