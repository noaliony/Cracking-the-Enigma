package decryption.manager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import machine.Machine;
import machine.details.ConfigurationDetails;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class SingleTask implements Runnable {

    private final String messageToDecode;
    private final List<ConfigurationDetails> configurationDetailsList;
    private Machine machine;
    private List<StringDecryptedCandidate> stringDecryptedCandidateList = new ArrayList<>();
    private int foundIDAgent;
    private Map<Integer, Machine> machineForAnyAgent;
    private final int amountOfAgents;
    private BlockingQueue<TaskResult> tasksResultBlockingQueue;
    private Runnable pauseThreadRunnable;

    public SingleTask(List<ConfigurationDetails> configurationDetailsList, String messageToDecode,
                      Map<Integer, Machine> machineForAnyAgent, int amountOfAgents,
                      BlockingQueue<TaskResult> tasksResultBlockingQueue, Runnable pauseThreadRunnable){
        this.configurationDetailsList = new ArrayList<>(configurationDetailsList);
        this.messageToDecode = messageToDecode;
        this.machineForAnyAgent = machineForAnyAgent;
        this.amountOfAgents = amountOfAgents;
        this.tasksResultBlockingQueue = tasksResultBlockingQueue;
        this.pauseThreadRunnable = pauseThreadRunnable;
    }

    @Override
    public void run() {
        pauseThreadRunnable.run();
        foundIDAgent = (int) (Thread.currentThread().getId() % amountOfAgents + 1);
        machine = machineForAnyAgent.get(foundIDAgent);
        Set<String> dictionary = machine.getDictionary().getDictionary();
        long milliSecondTimeTookToDecrypt = System.currentTimeMillis();
        TaskResult taskResult;

        machine = machineForAnyAgent.get(foundIDAgent);
        configurationDetailsList.forEach(configuration -> {
            String outputString;

            machine.setCode(configuration);
            outputString = machine.encodeFullMessage(messageToDecode);
            if (dictionary.containsAll(Arrays.asList(outputString.split(" ")))){
                StringDecryptedCandidate stringDecryptedCandidate = new StringDecryptedCandidate(outputString, configuration, foundIDAgent);

                stringDecryptedCandidateList.add(stringDecryptedCandidate);
            }
        });

        milliSecondTimeTookToDecrypt = System.currentTimeMillis() - milliSecondTimeTookToDecrypt;
        taskResult = new TaskResult(stringDecryptedCandidateList, milliSecondTimeTookToDecrypt);
        tasksResultBlockingQueue.add(taskResult);
    }
}
