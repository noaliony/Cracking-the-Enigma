package decryption.manager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TaskResultReader extends Thread{

    private BlockingQueue<TaskResult> tasksResultBlockingQueue;
    private Consumer<TaskResult> taskResultAchieved;
    private final int tasksCount;
    private int readTasksCount = 0;
    private ThreadPoolExecutor tasksPool;

    public TaskResultReader(BlockingQueue<TaskResult> tasksResultBlockingQueue, Consumer<TaskResult> taskResultAchieved,
                            int tasksCount, ThreadPoolExecutor tasksPool) {
        setName("Task Result Reader");
        setDaemon(true);
        this.tasksResultBlockingQueue = tasksResultBlockingQueue;
        this.taskResultAchieved = taskResultAchieved;
        this.tasksCount = tasksCount;
        this.tasksPool = tasksPool;
    }

    @Override
    public void run(){
        try {
            while (readTasksCount < tasksCount) {
                TaskResult taskResult = tasksResultBlockingQueue.take();

                taskResultAchieved.accept(taskResult);
                readTasksCount++;
            }
            tasksPool.shutdown();
            tasksPool.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }
}
