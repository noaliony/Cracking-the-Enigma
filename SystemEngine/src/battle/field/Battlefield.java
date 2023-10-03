package battle.field;

import decryption.manager.DecryptionManager;
import dto.*;
import enums.GameLevel;
import enums.GameStatus;
import machine.Machine;
import users.Allies;
import users.UBoat;

import java.util.ArrayList;
import java.util.List;

public class Battlefield {
    private String battlefieldName;
    private String uBoatName;
    private GameLevel gameLevel;
    private GameStatus gameStatus = GameStatus.WAITING;
    private int registerAlliesCount = 0;
    private int totalAlliesCount = 0;
    private int taskSize;
    private UBoat uBoat;
    private Machine machine;
    private List<Allies> alliesList = new ArrayList<>();
    private List<StringDecryptedCandidate> stringDecryptedCandidateList = new ArrayList<>();
    private List<StringDecryptedCandidate> prevStringDecryptedCandidateList = new ArrayList<>();
    private List<TaskResult> taskResultList = new ArrayList<>();
    private String originalMessage;
    private String messageToEncode;
    private String messageToDecode;
    private String machineString;
    private ConfigurationDetails receivedConfigurationDetails;
    private StringDecryptedCandidate winnerString;

    public Battlefield(String battlefieldName, int totalAlliesCount, GameLevel gameLevel, Machine machine, String machineString, UBoat uBoat) {
        this.battlefieldName = battlefieldName;
        this.totalAlliesCount = totalAlliesCount;
        this.gameLevel = gameLevel;
        this.machine = machine;
        this.machineString = machineString;
        this.uBoat = uBoat;
        this.uBoatName = uBoat.getUserName();
        updateBattleNameInUBoat();
    }

    public void addAllyToAlliesList(Allies ally){
        alliesList.add(ally);
        advanceRegisterAlliesCount();
    }

    private void updateBattleNameInUBoat() {
        uBoat.setBattleName(battlefieldName);
    }

    public String getBattlefieldName() {
        return battlefieldName;
    }

    public List<StringDecryptedCandidate> getPrevStringDecryptedCandidateList() {
        return prevStringDecryptedCandidateList;
    }

    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    public void setMessageToEncode(String messageToEncode) {
        this.messageToEncode = messageToEncode;
    }

    public void setMessageToDecode(String messageToDecode) {
        this.messageToDecode = messageToDecode;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public List<Allies> getAlliesList() {
        return alliesList;
    }

    public int getRegisteredAlliesCount() {
        return alliesList.size();
    }

    public int getTotalAlliesCount() {
        return totalAlliesCount;
    }

    public String getMachineString() {
        return machineString;
    }

    public BattlefieldDetails getNewBattlefieldDetails() {
        return new BattlefieldDetails(battlefieldName, uBoatName, gameStatus, gameLevel, alliesList.size(), totalAlliesCount);
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void handleCaseIfAllUsersAreReady() {
        synchronized (this) {
            boolean isContestReady = checkIfAllUsersAreReady();

            if (isContestReady) {
                gameStatus = GameStatus.READY;
                createDMsToAllAllies();
                this.receivedConfigurationDetails = new ConfigurationDetails(machine.getConfigurationDetails());
            }
        }
    }

    private boolean checkIfAllUsersAreReady() {
        boolean isContestReady = uBoat.isReady() && alliesList.size() == totalAlliesCount && alliesList.size() > 0;

        if (isContestReady) {
            for (Allies currentAlly : alliesList) {
                if (!currentAlly.isReady()) {
                    isContestReady = false;
                    break;
                }
            }
        }

        return isContestReady;
    }

    private void createDMsToAllAllies() {
        alliesList.forEach(currentAlly -> {
            DMInformation information = new DMInformation(gameLevel, taskSize, originalMessage);
            DecryptionManager DM = new DecryptionManager(information, machine);

            currentAlly.setDM(DM);
            currentAlly.runDM();
        });
    }

    public UBoat getUBoat() {
        return uBoat;
    }

    public synchronized List<StringDecryptedCandidate> getStringDecryptedCandidateList() {
        return stringDecryptedCandidateList;
    }

    public synchronized void addTaskResultToList(TaskResult taskResult) {
        taskResultList.add(taskResult);
        stringDecryptedCandidateList.addAll(taskResult.getStringDecryptedCandidateList());
        handleIfTheResultContainsTheWinnerString(taskResult);
    }

    private void handleIfTheResultContainsTheWinnerString(TaskResult taskResult) {
        List<StringDecryptedCandidate> stringDecryptedCandidateListFromTaskResul = taskResult.getStringDecryptedCandidateList();

        stringDecryptedCandidateListFromTaskResul.forEach(currentString -> {
            if (currentString.getStringDecrypted().equals(messageToEncode) && currentString.getConfigurationDetails().toString().equals(receivedConfigurationDetails.toString())) {
                winnerString = currentString;
                gameStatus = GameStatus.WAITING;
                uBoat.setIsReady(false);
            }
        });
    }

    public StringDecryptedCandidate getWinnerString() {
        return winnerString;
    }

    public Integer getCompletedTasksCount() {
        return taskResultList.size();
    }

    public void removeAlly(Allies ally) {
        alliesList.remove(ally);
    }

    public void advanceRegisterAlliesCount() {
        registerAlliesCount++;
    }

    public String getMessageToDecode() {
        return messageToDecode;
    }
}