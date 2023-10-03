package decryption.manager;

import enums.GameLevel;
import machine.Machine;
import machine.details.ConfigurationDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DecryptionManager {

    private int MAX_QUEUE_SIZE = 1000;
    private int amountOfAgents;
    private Machine machine;
    private final int taskSize;
    private List<StringDecryptedCandidate> stringDecryptedList;
    private BlockingQueue<Runnable> tasksBlockingQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
    private BlockingQueue<TaskResult> tasksResultBlockingQueue = new LinkedBlockingQueue<>();
    private TasksCreator tasksCreator;
    private Map<Integer, Machine> machineForAnyAgent;
    private final GameLevel gameLevel;
    private TaskResultReader taskResultReader;
    private ConfigurationDetails receivedConfigurationDetails;
    private String messageToEncode;
    private String messageToDecode;
    private double allTasksTimeAverage = 0;
    private int tasksCount;
    private ThreadPoolExecutor tasksPool;
    private boolean isPauseButtonClicked = false;
    private StringDecryptedCandidate winningStringDecryptedCandidate = null;

    public DecryptionManager(DMInformationFromUser informationFromUser, Machine machine){
        this.amountOfAgents = informationFromUser.getAmountOfAgents();
        this.gameLevel = informationFromUser.getGameLevel();
        this.taskSize = informationFromUser.getTaskSize();
        this.messageToDecode = informationFromUser.getMessageToDecode();
        this.messageToEncode = informationFromUser.getMessageToEncode();
        this.machine = machine;
        this.receivedConfigurationDetails = machine.getConfigurationDetails();
        tasksCount = calculateTasksCount();
        createMachineForAnyAgent();
        tasksPool = new ThreadPoolExecutor(amountOfAgents, amountOfAgents, Integer.MAX_VALUE, TimeUnit.SECONDS, tasksBlockingQueue);
        tasksCreator = new TasksCreator(gameLevel, receivedConfigurationDetails, taskSize,
                machine, tasksBlockingQueue, messageToDecode, tasksCount, machineForAnyAgent, amountOfAgents, tasksResultBlockingQueue, this::pauseAllThreads);
        taskResultReader = new TaskResultReader(tasksResultBlockingQueue, informationFromUser.getTaskResultConsumer(), tasksCount, tasksPool);
    }

    public int getTasksCount() {
        return tasksCount;
    }

    public void setPauseButtonClicked(boolean pauseButtonClicked) {
        isPauseButtonClicked = pauseButtonClicked;
    }

    public void runDM(){
        tasksPool.prestartAllCoreThreads();
        tasksCreator.start();
        taskResultReader.start();

        try {
            tasksPool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    public ConfigurationDetails getReceivedConfigurationDetails() {
        return receivedConfigurationDetails;
    }

    public String getMessageToEncode() {
        return messageToEncode;
    }

    public void createMachineForAnyAgent(){
        machineForAnyAgent = new HashMap<>();
        for (int i = 1; i <= amountOfAgents; i++) {
            machineForAnyAgent.put(i, machine.clone());
        }
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

    public void pauseAllThreads() {
        if (isPauseButtonClicked) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ignored){

                }
            }
        }
    }

    public void resumeAllThreads() {
        synchronized (this) {
            this.notifyAll();
        }
    }

    public void stopAllThreads() {
        tasksPool.shutdownNow();
    }

}
