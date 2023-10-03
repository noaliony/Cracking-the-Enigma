package decryption.manager;

import enums.GameLevel;

import java.util.function.Consumer;

public class DMInformationFromUser {
    private final int amountOfAgents;
    private final GameLevel gameLevel;
    private final int taskSize;
    private int tasksCount;
    private final String originalMessage;
    private String messageToEncode;
    private String messageToDecode;
    private Consumer<TaskResult> taskResultConsumer;

    public DMInformationFromUser(int amountOfAgents, GameLevel gameLevel, int taskSize, String originalMessage,
                                 Consumer<TaskResult> taskResultConsumer){
        this.amountOfAgents = amountOfAgents;
        this.gameLevel = gameLevel;
        this.taskSize = taskSize;
        this.originalMessage = originalMessage;
        this.taskResultConsumer = taskResultConsumer;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public int getAmountOfAgents() {
        return amountOfAgents;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public Consumer<TaskResult> getTaskResultConsumer() {
        return taskResultConsumer;
    }

    public String getMessageToEncode(){
        return messageToEncode;
    }

    public String getMessageToDecode() {
        return messageToDecode;
    }

    public int getTasksCount(){
        return tasksCount;
    }

    public void setMessageToEncode(String messageToEncode){
        this.messageToEncode = messageToEncode;
    }

    public void setMessageToDecode(String messageToDecode) {
        this.messageToDecode = messageToDecode;
    }

    public void setTaskResultConsumer(Consumer<TaskResult> taskResultConsumer) {
        this.taskResultConsumer = taskResultConsumer;
    }

    public void setTasksCount(int tasksCount) {
        this.tasksCount = tasksCount;
    }
}
