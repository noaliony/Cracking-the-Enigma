package decryption.manager;

import dto.TaskDetails;
import enums.GameLevel;
import enums.ReflectorID;
import machine.Machine;
import dto.ConfigurationDetails;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static enums.GameLevel.*;

public class TasksCreator extends Thread {

    private BlockingQueue<TaskDetails> taskDetailsBlockingQueue;
    private final GameLevel gameLevel;
    private final ConfigurationDetails receivedConfigurationDetails;
    private final int taskSize;
    private final Machine machine;
    private final String messageToDecode;
    private final int rotorsCount;
    private final int rotorsCountOptional;
    private final List<Character> alphabet;
    private final int tasksCount;
    private int tasksDetailsCreated = 0;

    public TasksCreator(GameLevel gameLevel, ConfigurationDetails receivedConfigurationDetails, int taskSize,
                        Machine machine, BlockingQueue<TaskDetails> taskDetailsBlockingQueue, String messageToDecode,
                        int tasksCount) {
        setName("Tasks Creator");
        setDaemon(true);
        this.gameLevel = gameLevel;
        this.receivedConfigurationDetails = receivedConfigurationDetails;
        this.taskSize = taskSize;
        this.machine = machine;
        this.taskDetailsBlockingQueue = taskDetailsBlockingQueue;
        this.messageToDecode = messageToDecode;
        this.rotorsCountOptional = machine.getAmountOfRotorsInRepository();
        this.rotorsCount = machine.getRotorsCount();
        this.alphabet = machine.getAlphabet();
        this.tasksCount = tasksCount;
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

        for (int i = 0; i < permutationsOfStartPosition.size(); i += taskSize){
            createTaskDetails(i, permutationsOfStartPosition, receivedConfigurationDetails.getRotorIDs(),
                    receivedConfigurationDetails.getReflectorID());
        }
    }

    private void mediumGameLevel(){
        List<List<Character>> permutationsOfStartPosition = getPermutationsOfStartPosition();
        List<ReflectorID> optionalReflectorIDsList = getOptionalReflectorIDsList();

        for (ReflectorID reflectorID : optionalReflectorIDsList){
            for (int i = 0; i < permutationsOfStartPosition.size(); i += taskSize){
                createTaskDetails(i, permutationsOfStartPosition, receivedConfigurationDetails.getRotorIDs(), reflectorID);
            }
        }
    }

    private void difficultOrImpossibleGameLevel() {
        List<List<Integer>> optionalRotorIDsList = getOptionalRotorIDsList();
        List<List<Character>> permutationsOfStartPosition = getPermutationsOfStartPosition();
        List<ReflectorID> optionalReflectorIDsList = getOptionalReflectorIDsList();

        for (List<Integer> currentRotorIDs : optionalRotorIDsList){
            for (ReflectorID reflectorID : optionalReflectorIDsList){
                for (int i = 0; i < permutationsOfStartPosition.size(); i += taskSize) {
                    createTaskDetails(i, permutationsOfStartPosition, currentRotorIDs, reflectorID);
                }
            }
        }
    }

    private void createTaskDetails(int index, List<List<Character>> permutationsOfStartPosition,
                                   List<Integer> rotorIDsList, ReflectorID reflectorID) {

        List<Character> currentListOfStartPosition = new ArrayList<>(permutationsOfStartPosition.get(index));
        ConfigurationDetails startConfiguration = new ConfigurationDetails(new ArrayList<>(rotorIDsList), new ArrayList<>(currentListOfStartPosition),
                reflectorID, new HashMap<>(receivedConfigurationDetails.getPlugPairs()));
        TaskDetails taskDetails = null;

        if (index + taskSize > permutationsOfStartPosition.size()) {
            int smallTaskSize = permutationsOfStartPosition.size() - index;

            taskDetails = new TaskDetails(startConfiguration, smallTaskSize);
        } else {
            taskDetails = new TaskDetails(startConfiguration, taskSize);
        }
        tasksDetailsCreated++;
        synchronized (taskDetailsBlockingQueue) {
            try {
                taskDetailsBlockingQueue.put(taskDetails);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private List<List<Character>> getPermutationsOfStartPosition(){
        int amountOfPermutations = (int) Generator
                .permutation(alphabet)
                .withRepetitions(rotorsCount)
                .stream().count();

        List<List<Character>> permutations = new ArrayList<>(new ArrayList<>());
        List<Character> currentPermutation = new ArrayList<>();

        for (int i = 0; i < rotorsCount; i++) {
            currentPermutation.add(alphabet.get(0));
        }

        for (int i = 0; i < amountOfPermutations; i++) {
            permutations.add(currentPermutation);
            currentPermutation = getNextPermutation(currentPermutation);
        }

        return permutations;
    }

    private List<Character> getNextPermutation(List<Character> currentPermutation) {
        List<Character> startPosition = new ArrayList<>(currentPermutation);
        int currentIndex = startPosition.size() - 1;

        while (currentIndex != -1) {
            Character nextKey = getNextKey(startPosition.get(currentIndex));

            startPosition.set(currentIndex, nextKey);
            if (nextKey.equals(machine.getAlphabet().get(0))) {
                currentIndex--;
            } else {
                currentIndex = -1;
            }
        }

        return startPosition;
    }

    private Character getNextKey(Character character) {
        List<Character> alphabet = machine.getAlphabet();

        return alphabet.indexOf(character) == alphabet.size() - 1 ? alphabet.get(0) : alphabet.get(alphabet.indexOf(character) + 1);
    }

    private List<ReflectorID> getOptionalReflectorIDsList(){
        return machine.getReflectorIDsInRepositoryList();
    }

    private List<List<Integer>> getOptionalRotorIDsList() {
        List<List<Integer>> optionalRotorIDsList = new ArrayList<>();

        if (gameLevel == DIFFICULT) {
            optionalRotorIDsList.addAll(Generator.permutation(receivedConfigurationDetails.getRotorIDs())
                    .simple()
                    .stream()
                    .collect(Collectors.toList()));
        } else if (gameLevel == IMPOSSIBLE) {
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

    public int getCreatedTasksCount() {
        return tasksDetailsCreated;
    }
}
