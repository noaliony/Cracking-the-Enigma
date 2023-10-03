package main.agent.task;

import dto.ConfigurationDetails;
import dto.StringDecryptedCandidate;
import dto.TaskDetails;
import dto.TaskResult;
import machine.Machine;
import main.agent.utils.http.HttpClientUtils;
import okhttp3.HttpUrl;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static main.agent.utils.agent.Constants.*;

public class SingleTask implements Runnable {

    private List<ConfigurationDetails> configurationDetailsList;
    private final String allyName;
    private final String agentName;
    private final String messageToDecode;
    private final TaskDetails taskDetails;
    private final CountDownLatch cdl;
    private final Consumer<String> handleErrorConsumer;
    private final Map<Integer, Machine> machinesForAgents;
    private Machine machine;
    private List<StringDecryptedCandidate> stringDecryptedCandidateList = new ArrayList<>();
    //private Runnable pauseThreadRunnable;

    public SingleTask(Map<Integer, Machine> machinesForAgents, TaskDetails taskDetails, String allyName, String agentName,
                      String messageToDecode, CountDownLatch cdl, Consumer<String> handleErrorConsumer) {
        this.machinesForAgents = machinesForAgents;
        this.taskDetails = taskDetails;
        this.allyName = allyName;
        this.agentName = agentName;
        this.messageToDecode = messageToDecode;
        this.cdl = cdl;
        this.handleErrorConsumer = handleErrorConsumer;
    }

    public List<ConfigurationDetails> getConfigurationDetailsList() {
        List<ConfigurationDetails> configurationDetailsList = new ArrayList<>();
        ConfigurationDetails startConfiguration = taskDetails.getStartConfiguration();
        ConfigurationDetails currentConfiguration = new ConfigurationDetails(startConfiguration);

        for (int i = 0; i < taskDetails.getTaskSize(); i++) {
            configurationDetailsList.add(currentConfiguration);
            currentConfiguration = getNextConfiguration(currentConfiguration);
        }

        return configurationDetailsList;
    }

    private ConfigurationDetails getNextConfiguration(ConfigurationDetails currentConfiguration) {
        List<Character> startPosition = new ArrayList<>(currentConfiguration.getStartPosition());
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

        ConfigurationDetails nextConfiguration = new ConfigurationDetails(currentConfiguration.getRotorIDs(), startPosition,
                currentConfiguration.getReflectorID(), currentConfiguration.getPlugPairs());

        return nextConfiguration;
    }

    private Character getNextKey(Character character) {
        List<Character> alphabet = machine.getAlphabet();

        return alphabet.indexOf(character) == alphabet.size() - 1 ? alphabet.get(0) : alphabet.get(alphabet.indexOf(character) + 1);
    }

    @Override
    public void run() {
        int currentThreadID = Math.toIntExact(Thread.currentThread().getId() % machinesForAgents.size() + 1);

        machine = machinesForAgents.get(currentThreadID);
        configurationDetailsList = getConfigurationDetailsList();

        Set<String> dictionary = machine.getDictionary().getDictionarySet();
        long milliSecondTimeTookToDecrypt = System.currentTimeMillis();
        TaskResult taskResult;

        configurationDetailsList.forEach(configuration -> {
            String outputString;

            machine.setCodeConfiguration(configuration);
            outputString = machine.encodeFullMessage(messageToDecode);
            if (dictionary.containsAll(Arrays.asList(outputString.split(" ")))){
                StringDecryptedCandidate stringDecryptedCandidate = new StringDecryptedCandidate(outputString, configuration,
                        allyName, agentName, taskDetails);

                stringDecryptedCandidateList.add(stringDecryptedCandidate);
            }
        });

        milliSecondTimeTookToDecrypt = System.currentTimeMillis() - milliSecondTimeTookToDecrypt;
        taskResult = new TaskResult(stringDecryptedCandidateList, milliSecondTimeTookToDecrypt);
        sendRequestToPostTaskResultToBattlefield(taskResult);
        cdl.countDown();
    }

    private void sendRequestToPostTaskResultToBattlefield(TaskResult taskResult) {
        String body = GSON_INSTANCE.toJson(taskResult);

        String finalUrl = HttpUrl
                .parse(POST_TASK_RESULT_TO_BATTLEFIELD_PAGE)
                .newBuilder()
                .build()
                .toString();

        try {
            Response response = HttpClientUtils.runSyncPost(finalUrl, body);
        } catch (IOException exception) {
            handleErrorConsumer.accept(exception.getMessage());
        }
    }
}
