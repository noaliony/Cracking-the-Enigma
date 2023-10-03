package decryption.manager;

import enums.GameLevel;
import enums.ReflectorID;
import machine.Machine;
import machine.details.ConfigurationDetails;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TasksCreator extends Thread {

    private BlockingQueue<Runnable> tasksBlockingQueue;
    private final GameLevel gameLevel;
    private final ConfigurationDetails receivedConfigurationDetails;
    private final int taskSize;
    private final Machine machine;
    private final String messageToDecode;
    private final int rotorsCount;
    private final int rotorsCountOptional;
    private final List<Character> alphabet;
    private final int tasksCount;
    private Map<Integer, Machine> machineForAnyAgent;
    private final int amountOfAgents;
    private BlockingQueue<TaskResult> tasksResultBlockingQueue;
    private int tasksCreated = 0;
    private Runnable pauseThreadRunnable;

    public TasksCreator(GameLevel gameLevel, ConfigurationDetails receivedConfigurationDetails, int taskSize,
                        Machine machine, BlockingQueue<Runnable> tasksBlockingQueue, String messageToDecode, int tasksCount,
                        Map<Integer, Machine> machineForAnyAgent, int amountOfAgents,
                        BlockingQueue<TaskResult> tasksResultBlockingQueue, Runnable pauseThreadRunnable) {
        setName("Tasks Creator");
        setDaemon(true);
        this.gameLevel = gameLevel;
        this.receivedConfigurationDetails = receivedConfigurationDetails;
        this.taskSize = taskSize;
        this.machine = machine;
        this.tasksBlockingQueue = tasksBlockingQueue;
        this.messageToDecode = messageToDecode;
        this.rotorsCountOptional = machine.getAmountOfRotorsInRepository();
        this.rotorsCount = machine.getRotorsCount();
        this.alphabet = machine.getAlphabet();
        this.tasksCount = tasksCount;
        this.machineForAnyAgent = machineForAnyAgent;
        this.amountOfAgents = amountOfAgents;
        this.tasksResultBlockingQueue = tasksResultBlockingQueue;
        this.pauseThreadRunnable = pauseThreadRunnable;
    }

    @Override
    public void run(){
        switch (gameLevel) {
            case EASY:
                easyGameLevel();
                break;
            case MEDIUM:
                mediumGameLevel();
                break;
            case DIFFICULT:
            case IMPOSSIBLE:
                difficultOrImpossibleGameLevel();
                break;
        }
    }

    private void easyGameLevel(){
        List<List<Character>> permutationsOfStartPosition = getPermutationsOfStartPosition();
        List<ConfigurationDetails> configurationDetailsList = new ArrayList<>();

        for (int i = 0; i < permutationsOfStartPosition.size(); i++){
            createTask(i, permutationsOfStartPosition, configurationDetailsList, receivedConfigurationDetails.getRotorIDs(),
                    receivedConfigurationDetails.getReflectorID());
        }
    }

    private void mediumGameLevel(){
        List<List<Character>> permutationsOfStartPosition = getPermutationsOfStartPosition();
        List<ReflectorID> optionalReflectorIDsList = getOptionalReflectorIDsList();
        List<ConfigurationDetails> configurationDetailsList = new ArrayList<>();

        for (ReflectorID reflectorID : optionalReflectorIDsList){
            for (int i = 0; i < permutationsOfStartPosition.size(); i++){
                createTask(i, permutationsOfStartPosition, configurationDetailsList, receivedConfigurationDetails.getRotorIDs(), reflectorID);
            }
        }
    }

    private void difficultOrImpossibleGameLevel() {
        List<List<Integer>> optionalRotorIDsList = getOptionalRotorIDsList();
        List<List<Character>> permutationsOfStartPosition = getPermutationsOfStartPosition();
        List<ReflectorID> optionalReflectorIDsList = getOptionalReflectorIDsList();
        List<ConfigurationDetails> configurationDetailsList = new ArrayList<>();

        for (List<Integer> currentRotorIDs : optionalRotorIDsList){
            for (ReflectorID reflectorID : optionalReflectorIDsList){
                for (int i = 0; i < permutationsOfStartPosition.size(); i++){
                    createTask(i, permutationsOfStartPosition, configurationDetailsList, currentRotorIDs, reflectorID);
                }
            }
        }
    }

    private void createTask(int index, List<List<Character>> permutationsOfStartPosition, List<ConfigurationDetails> configurationDetailsList, List<Integer> rotorIDsList, ReflectorID reflectorID){
        List<Character> currentListOfStartPosition = permutationsOfStartPosition.get(index);

        ConfigurationDetails configurationDetails = new ConfigurationDetails(rotorIDsList, currentListOfStartPosition,
                reflectorID, receivedConfigurationDetails.getPlugPairs());

        configurationDetailsList.add(configurationDetails);
        if (configurationDetailsList.size() == taskSize || index == permutationsOfStartPosition.size()) {
            SingleTask task = new SingleTask(configurationDetailsList, messageToDecode, machineForAnyAgent, amountOfAgents,
                    tasksResultBlockingQueue, pauseThreadRunnable);

            tasksCreated++;
            configurationDetailsList.clear();
            try {
                tasksBlockingQueue.put(task);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<List<Character>> getPermutationsOfStartPosition(){
        List<List<Character>> permutations = Generator
                .permutation(alphabet)
                .withRepetitions(rotorsCount)
                .stream()
                .collect(Collectors.<List<Character>>toList());

        return permutations;
    }

    private List<ReflectorID> getOptionalReflectorIDsList(){
        return machine.getReflectorIDsInRepositoryList();
    }

    private List<List<Integer>> getOptionalRotorIDsList() {
        List<List<Integer>> optionalRotorIDsList = new ArrayList<>();

        if (gameLevel == GameLevel.DIFFICULT) {
            optionalRotorIDsList.addAll(Generator.permutation(receivedConfigurationDetails.getRotorIDs())
                    .simple()
                    .stream()
                    .collect(Collectors.toList()));
        } else if (gameLevel == GameLevel.IMPOSSIBLE) {
            Generator.combination(IntStream.range(1, machine.getAmountOfRotorsInRepository() + 1).boxed().collect(Collectors.toList()))
                    .simple(machine.getRotorsCount())
                    .stream()
                    .forEach(rotorsOption -> {
                        optionalRotorIDsList.addAll(Generator.permutation(rotorsOption)
                                .simple()
                                .stream()
                                .collect(Collectors.toList()));
                    });
        }

        return optionalRotorIDsList;
    }
}
