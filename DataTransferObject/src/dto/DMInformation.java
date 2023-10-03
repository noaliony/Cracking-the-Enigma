package dto;

import enums.GameLevel;

import java.util.function.Consumer;

public class DMInformation {
    private GameLevel gameLevel;
    private int taskSize;
    private int tasksCount;
    private final String originalMessage;
    private String messageToEncode;
    private String messageToDecode;

    public DMInformation(GameLevel gameLevel, int taskSize, String originalMessage) {
        this.gameLevel = gameLevel;
        this.taskSize = taskSize;
        this.originalMessage = originalMessage;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public String getMessageToEncode(){
        return messageToEncode;
    }

    public String getMessageToDecode() {
        return messageToDecode;
    }
}
