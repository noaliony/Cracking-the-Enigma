package decryption.manager;

import dto.*;
import enums.GameLevel;
import machine.Machine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static com.sun.webkit.graphics.WCRenderQueue.MAX_QUEUE_SIZE;

public class DecryptionManager {

    private Machine machine;
    private TasksCreator tasksCreator;
    private BlockingQueue<TaskDetails> taskDetailsBlockingQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
    private GameLevel gameLevel;
    private ConfigurationDetails receivedConfigurationDetails;
    private String messageToEncode;
    private String messageToDecode;
    private int taskSize;
    private int tasksCount;

    public DecryptionManager(DMInformation information, Machine machine) {
        setDataMembers(information, machine);
        tasksCount = calculateTasksCount();
        tasksCreator = new TasksCreator(gameLevel, receivedConfigurationDetails, taskSize,
                machine, taskDetailsBlockingQueue, messageToDecode, tasksCount);
    }

    private void setDataMembers(DMInformation information, Machine machine) {
        this.gameLevel = information.getGameLevel();
        this.taskSize = information.getTaskSize();
        this.messageToDecode = information.getMessageToDecode();
        this.messageToEncode = information.getMessageToEncode();
        this.machine = machine;
        this.receivedConfigurationDetails = machine.getConfigurationDetails();
    }

    public int getTasksCount() {
        return tasksCount;
    }

    public int getCreatedTasksCount() {
        return tasksCreator.getCreatedTasksCount();
    }

    private int calculateTasksCount() {
        int rotorsCount = machine.getRotorsCount();
        int amountOfTasks = (int) Math.pow(machine.getAlphabet().size(), rotorsCount);

        switch (gameLevel){
            case EASY:
                break;
            case MEDIUM:
                amountOfTasks *= machine.getAmountOfReflectorsInRepository();
                break;
            case DIFFICULT:
                amountOfTasks *= machine.getAmountOfReflectorsInRepository();
                amountOfTasks *= factorial(rotorsCount);
                break;
            case IMPOSSIBLE:
                amountOfTasks *= machine.getAmountOfReflectorsInRepository();
                amountOfTasks *= factorial(rotorsCount);
                amountOfTasks *= binomial(machine.getAmountOfRotorsInRepository(), rotorsCount);
                break;
        }
        amountOfTasks /= taskSize;

        return amountOfTasks;
    }

    private int binomial(final int n, final int k) {
        return factorial(n) / (factorial(n - k) * factorial(k));
    }

    private int factorial(int number){
        int result = 1;

        for (int i = 1; i <= number; i++) {
            result *= i;
        }

        return result;
    }

    public List<TaskDetails> getListOfTaskDetails(int tasksCount) {
        List<TaskDetails> taskDetailsList = new ArrayList<>();

        for (int i = 0; i < tasksCount && !taskDetailsBlockingQueue.isEmpty(); i++) {
            try {
                TaskDetails taskDetails = taskDetailsBlockingQueue.take();

                taskDetailsList.add(taskDetails);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }

        return taskDetailsList;
    }

    public void runDM() {
        tasksCreator.start();
    }
}
