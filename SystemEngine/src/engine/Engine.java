package engine;

import components.Rotor;
import decryption.manager.DMInformationFromUser;
import decryption.manager.DecryptionManager;
import enums.ReflectorID;
import exceptions.*;
import machine.Machine;
import machine.details.ConfigurationDetails;
import machine.details.MachineDetailsObject;
import machine.details.MachineSetting;
import machineHistory.MachineHistory;
import machineHistory.ProcessedString;
import xml.MachineBuilder;
import xml.ReadXML;
import generated.CTEEnigma;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public class Engine {

    private int MAX_NUMBERS_OF_ROTORS_IN_USE = 99;
    private Machine machine = null;
    private DecryptionManager DM;

    public void loadXMLFileToMachine(String pathXML) throws EnigmaLogicException {
        machine = readSystemDetailsFile(pathXML);
    }

    public void updateManualSetting(){
        resetCurrentNotchAndPosition();
        manualSetting();
        addMachineSetting();
    }

    public void updateAutomaticSetting(){
        resetCurrentNotchAndPosition();
        getRandomSetting();
        addMachineSetting();
    }

    public Machine readSystemDetailsFile(String pathXML) throws EnigmaLogicException {

        boolean isValidFile =  ReadXML.checkIfValidXMLFile(pathXML);
        MachineBuilder machineBuilder = new MachineBuilder();
        CTEEnigma cteEnigma = ReadXML.getCTEEnigma();
        Machine machine = machineBuilder.createMachineFromCTEEnigma(cteEnigma);

        return machine;
    }

    public void manualSetting() {

        MachineSetting machineSetting = machine.getOriginalMachineSetting();
        String rotorsID = machineSetting.getRotorIDs();
        String startingPosition = machineSetting.getPosition();
        String reflectorID = machineSetting.getReflectorID();
        String plugPairs = machineSetting.getPlugPairs();
        List<Integer> rotorsIdToUse;
        String[] arrayOfStringsToRotorsID;
        List<Character> rotorStartPosition;
        ReflectorID reflectorIdToUse;
        Map<Character,Character> plugPairsToUse;
        ConfigurationDetails configurationDetails;
        String rotorsNotch;

        rotorsIdToUse = new ArrayList<>();
        arrayOfStringsToRotorsID = rotorsID.split("," , MAX_NUMBERS_OF_ROTORS_IN_USE);

        for (String stringRotorID : arrayOfStringsToRotorsID)
            rotorsIdToUse.add(Integer.parseInt(stringRotorID));

        rotorStartPosition = new ArrayList<>();
        for (int i = 0; i < startingPosition.length(); i++)
            rotorStartPosition.add(startingPosition.charAt(i));

        reflectorIdToUse =  ReflectorID.convertStringToReflectorID(reflectorID);

        plugPairsToUse = machine.getPlugPairs();
        for (int i = 0; i < plugPairs.length(); i+=2) {

            char key = plugPairs.charAt(i);
            char value = plugPairs.charAt(i+1);

            plugPairsToUse.replace(key, value);
            plugPairsToUse.replace(value, key);
        }

        configurationDetails = new ConfigurationDetails(rotorsIdToUse, rotorStartPosition, reflectorIdToUse, plugPairsToUse);
        machine.setCode(configurationDetails);

        rotorsNotch = updateNotchInRotorsInUse();
        machine.setRotorsNotchInOriginalAndCurrentMachineSetting(rotorsNotch);

    }

    private String updateNotchInRotorsInUse() {

        return machine.getNotchInRotorsInUseString();
    }

    public void getRandomSetting() {

        List<Integer> rotorsIdToUse = getRandomIDRotors();
        List<Character> rotorStartPosition = getRandomStartingPosition();
        ReflectorID reflectorIdToUse = getRandomReflectorID();
        Map<Character, Character> plugPairsToUse = getRandomPlugBoard();
        ConfigurationDetails configurationDetails = new ConfigurationDetails(rotorsIdToUse, rotorStartPosition, reflectorIdToUse, plugPairsToUse);

        machine.setCode(configurationDetails);

        setMachineSetting(rotorsIdToUse, rotorStartPosition, reflectorIdToUse, plugPairsToUse);
    }

    public void setMachineSetting(List<Integer> rotorsIdToUse, List<Character> rotorStartPosition,
                                   ReflectorID reflectorIdToUse, Map<Character, Character> plugPairsToUse) {

        String rotorsID = createStringIDRotors(rotorsIdToUse);
        String rotorsNotch = createRotorsNotch(rotorsID, getRotorsListInRepository());
        String startingPosition = createStartingPosition(rotorStartPosition);
        String reflectorID = reflectorIdToUse.toString();
        String plugPairs = createPlugPairs(plugPairsToUse);

        setMachineSettingStringsParameters(rotorsID, rotorsNotch, startingPosition, reflectorID, plugPairs);
    }

    private String createPlugPairs(Map<Character, Character> plugPairsToUse) {

        String plugPairsString = "";

        for (Map.Entry<Character, Character> entry : plugPairsToUse.entrySet()) {

            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            if (plugPairsString.contains(key) == false && plugPairsString.contains(value) == false && !key.equals(value)) {

                plugPairsString = plugPairsString.concat(key);
                plugPairsString = plugPairsString.concat(value);
            }
        }

        return plugPairsString;
    }

    private String createStartingPosition(List<Character> rotorStartPosition) {

        String startingPositionString = "";

        for (Character currentChar : rotorStartPosition)
            startingPositionString = startingPositionString.concat(currentChar.toString());

        return startingPositionString;
    }

    public static String createRotorsNotch(String rotorsID, List<Rotor> rotorsListInRepository) {

        String rotorsNotch = "";
        String[] rotorsIDArray = rotorsID.split(",");
        int idRotor;
        Integer notch;

        for(String rotorID : rotorsIDArray) {

            idRotor = Integer.parseInt(rotorID);
            for (Rotor currentRotor : rotorsListInRepository) {
                if (currentRotor.getID() == idRotor) {
                    notch = currentRotor.getNotch();
                    rotorsNotch = rotorsNotch.concat(notch.toString() + ",");
                }
            }
        }
        rotorsNotch = rotorsNotch.substring(0, rotorsNotch.length() -1);

        return  rotorsNotch;
    }

    private String createStringIDRotors(List<Integer> rotorsIDInUse) {

        StringBuilder rotorsIDString = new StringBuilder();

        for (Integer idRotor : rotorsIDInUse) {

            rotorsIDString.append(idRotor.toString());
            rotorsIDString.append(",");
        }

        return rotorsIDString.substring(0, rotorsIDString.length() - 1);
    }

    private Map<Character, Character> getRandomPlugBoard() {
        Random random = new Random();
        Map<Character, Character> plugPairsToUse;
        List<Character> charactersInUseInPlugBoard = new ArrayList<>();
        int amountOfPairs = random.nextInt(getAlphabet().size() / 2 + 1);
        int randomIndex;
        List<Character> alphabet = getAlphabet();
        boolean validRandomChar;
        char key, value;

        plugPairsToUse = machine.getPlugPairs();
        for (int i = 0; i < amountOfPairs; i++) {

            do {
                randomIndex = random.nextInt(alphabet.size());
                key = alphabet.get(randomIndex);
                validRandomChar = charactersInUseInPlugBoard.contains(key);
            } while (validRandomChar);
            charactersInUseInPlugBoard.add(key);

            do {
                randomIndex = random.nextInt(alphabet.size());
                value = alphabet.get(randomIndex);
                validRandomChar = charactersInUseInPlugBoard.contains(value);
            } while (validRandomChar);
            charactersInUseInPlugBoard.add(value);

            plugPairsToUse.replace(key, value);
            plugPairsToUse.replace(value, key);
        }

        return plugPairsToUse;
    }

    public List<Character> getAlphabet(){
        return machine.getKeyBoard().getAlphabet();
    }

    public List<Rotor> getRotorsListInRepository(){
        return machine.getRotorsListInRepository();
    }

    public int getRotorsCount() {
        return machine.getRotorsCount();
    }

    private ReflectorID getRandomReflectorID() {

        ReflectorID reflectorID;
        String randomReflectorID;
        Random random = new Random();
        Integer randomIndex;

        randomIndex = 1 + random.nextInt(machine.getAmountOfReflectorsInRepository());
        randomReflectorID = randomIndex.toString();
        reflectorID = ReflectorID.convertStringToReflectorID(randomReflectorID);

        return reflectorID;
    }

    private List<Character> getRandomStartingPosition() {

        List<Character> startingPositionList = new ArrayList<>();
        List<Character> alphabet = getAlphabet();
        int rotorsCount = machine.getRotorsCount();
        Random random = new Random();
        char randomChar;
        int randomIndex;

        for (int i = 0; i < rotorsCount; i++) {

            randomIndex = random.nextInt(alphabet.size());
            randomChar = alphabet.get(randomIndex);
            startingPositionList.add(randomChar);
        }

        return startingPositionList;
    }

    private List<Integer> getRandomIDRotors() {

        List<Integer> rotorsIDToUse = new ArrayList<>();
        List<Integer> rotorsIDOptionalInRepository;
        List<Rotor> rotorsListInRepository = machine.getRotorsListInRepository();
        int rotorsCount = machine.getRotorsCount();
        int rotorID;
        Random random = new Random();
        int randomIndex;
        boolean validRandomIndex;

        rotorsIDOptionalInRepository = createRotorsIDOptionalInRepository(rotorsListInRepository);
        for (int i = 0; i < rotorsCount; i++) {

            do {
                randomIndex = random.nextInt(rotorsIDOptionalInRepository.size());
                rotorID = rotorsIDOptionalInRepository.get(randomIndex);
                validRandomIndex = rotorsIDToUse.contains(rotorID);
            } while (validRandomIndex);

            rotorsIDToUse.add(rotorID);
        }

        return rotorsIDToUse;
    }

    public List<Integer> createRotorsIDOptionalInRepository(List<Rotor> rotorsListInRepository) {
        List<Integer> rotorsIDOptionalInRepository = new ArrayList<>();

        for (Rotor currentRotor : rotorsListInRepository)
            rotorsIDOptionalInRepository.add(currentRotor.getID());

        return rotorsIDOptionalInRepository;
    }

    public List<Integer> createRotorsIDInUse(List<Rotor> rotorsIDInUse) {
        List<Integer> rotorsIDInUseList = new ArrayList<>();

        for (Rotor currentRotor : rotorsIDInUse)
            rotorsIDInUseList.add(currentRotor.getID());

        return rotorsIDInUseList;
    }

    public void setMachine(Machine machineFromCTEEnigma) {
        this.machine = machineFromCTEEnigma;
    }

    public String encodingStringInput(String stringToEncodeFromUser, boolean inKeyByKeyMode) {
        if (!checkIfTheInputValid(stringToEncodeFromUser.toUpperCase())){
            throw new MessageForCodingNotValidException(getAlphabet());
        }

        Duration totalTime;
        Character isEncoded;
        String stringOutput = "";
        Long nanoSecondTimeTookToProcessedString;

        stringToEncodeFromUser = stringToEncodeFromUser.toUpperCase();
        machine.resetStartTimeToEncodedString();
        stringOutput = machine.encodeFullMessage(stringToEncodeFromUser);
        nanoSecondTimeTookToProcessedString = getNanoSecondThatStringWasProcessed();

        if (!inKeyByKeyMode) {
            machine.advanceAmountOfIsEncodedMessages();
            addProcessedStringToListInMachineHistory(stringToEncodeFromUser, stringOutput, getNanoSecondThatStringWasProcessed());
        } else {
            ProcessedString processedString = machine.getCurrentProcessedString();

            processedString.setInput(processedString.getInput().concat(stringToEncodeFromUser));
            processedString.setOutput(processedString.getOutput().concat(stringOutput));
            processedString.setNanoSeconds(nanoSecondTimeTookToProcessedString + processedString.getNanoSeconds());
        }
        updateCurrentMachineSetting();

        return stringOutput;
    }

    public void insertCurrentProcessedStringToMachineHistory(){
        machine.insertCurrentProcessedStringToMachineHistoryList();
    }

    public long getNanoSecondThatStringWasProcessed(){
        return machine.getNanoSecondThatStringWasProcessed();
    }

    public void setMachineSettingStringsParameters(String rotorsID, String rotorsNotch, String startingPosition, String reflectorID, String plugPairs) {

        machine.setMachineSetting(rotorsID, rotorsNotch, startingPosition, reflectorID, plugPairs);
    }

    public void addMachineSetting() {

        machine.addMachineSetting();
    }

    public void addProcessedStringToListInMachineHistory(String stringToEncodeFromUser, String stringOutput, Long nano) {

        ProcessedString processedString = new ProcessedString(stringToEncodeFromUser, stringOutput, nano);

        machine.getCurrentIsEncodedStringList().add(processedString);
        machine.updateCurrenProcessedStringInCurrentMachineHistory(processedString);
    }

    public void updateCurrentMachineSetting() {

        machine.updateCurrentMachineSetting();
    }

    public int getAmountRotorsInUse() {

        return machine.getRotorsCount();
    }

    public int getAmountRotorsInRepository() {

        return machine.getRotorsListInRepository().size();
    }

    public int getAmountReflectorsInRepository() {

        return machine.getAmountOfReflectorsInRepository();
    }

    public int getAmountOfIsEncodedMessages() {

        return machine.getAmountOfIsEncodedMessages();
    }

    public String displayOriginalMachineSetting() {

        return machine.displayOriginalMachineSetting();
    }

    public String displayCurrentMachineSetting() {

        return machine.displayCurrentMachineSetting();
    }

    public List<MachineHistory> getMachineHistory() {

        return machine.getMachineHistoryList();
    }

    public void resetCurrentCode() {

        machine.resetCurrentCode();
    }

    public void resetCurrentNotchAndPosition() {

        machine.resetCurrentNotchAndPosition();
    }

    public boolean checkIfTheInputValid(String stringToEncode) {

        boolean checkValidity = stringToEncode.length() > 0;

        for (int i = 0; i < stringToEncode.length(); i++) {
            checkValidity &= getAlphabet().contains(stringToEncode.charAt(i));
        }

        return checkValidity;
    }

    public void checkFilePath(String xmlFilePath, String requiredFileExtension) throws RuntimeException {
        Path filePath;

        try {
            filePath = Paths.get(xmlFilePath);
        } catch (InvalidPathException e) {
            throw new RuntimeException();
        }

        if (!Files.isRegularFile(filePath)) {
            throw new RuntimeException();
        }

        if (!filePath.toString().endsWith(requiredFileExtension)) {
            throw new RuntimeException();
        }
    }

    public boolean isCodeSet(){
        return machine.isCodeSet();
    }

    public MachineDetailsObject getMachineDetails() {
        return machine.getMachineDetailsObject();
    }

    public MachineSetting getCurrentMachineSetting(){
        return machine.getCurrentMachineSetting();
    }

    public MachineHistory getCurrentMachineHistory() {
        return machine.getCurrentMachineHistory();
    }

    public Machine getMachine() {
        return machine;
    }

    public int calculateTasksCount(DMInformationFromUser informationFromUser) throws MessageToEncodeIsNotValidException {
        try {
            String messageToEncode = validatorMessageToEncode(informationFromUser.getOriginalMessage());
            String messageToDecode = encodingStringInput(messageToEncode, false);

            informationFromUser.setMessageToEncode(messageToEncode);
            informationFromUser.setMessageToDecode(messageToDecode);
            DM = createDM(informationFromUser);

            return DM.getTasksCount();
        } catch (MessageToEncodeIsNotValidException exception) {
            throw exception;
        }
    }

    public DecryptionManager createDM(DMInformationFromUser informationFromUser){
        return new DecryptionManager(informationFromUser, machine);
    }

    public String validatorMessageToEncode(String messageToEncode) throws MessageToEncodeIsNotValidException {
        return machine.validatorMessageToEncode(messageToEncode);
    }

    public void runDM() {
        DM.runDM();
    }

    public String getMessageToEncodeFromDM() {
        return DM.getMessageToEncode();
    }

    public ConfigurationDetails getReceivedConfigurationDetailsFromDM() {
        return DM.getReceivedConfigurationDetails();
    }

    public Set<String> getDictionary() {
        return machine.getDictionary().getDictionary();
    }

    public int getMaxValueAgentsCount() {
        return machine.getMaxValueAgentsCount();
    }

    public void resumeAllThreads() {
        DM.setPauseButtonClicked(false);
        DM.resumeAllThreads();
    }

    public void pauseAllThreads() {
        DM.setPauseButtonClicked(true);
    }

    public void stopAllThreads(){
        DM.stopAllThreads();
    }
}
