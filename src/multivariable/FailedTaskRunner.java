package multivariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class FailedTaskRunner {
    private static final int LOW_MAX_RETRY_COUNT = 3;
    private static final int MED_MAX_RETRY_COUNT = 6;
    private static final int HIGH_MAX_RETRY_COUNT = 9;
    private static final double LOW_BASE_PROBABILITY = 0.35;  //Prob of a low prio task to succeed
    private static final double MED_BASE_PROBABILITY = 0.45;  //Prob of a medium prio task to succeed
    private static final double HIGH_BASE_PROBABILITY = 0.55; //Prob of a high prio task to succeed
    protected static final Random RANDOM = new Random(12345);

    public enum DECAY{
        LINEAR,
        EXPONENTIAL,
    }

    public enum EXPERIMENT {
        CONTROLLED,
        UNCONTROLLED
    }

    /**
     * The controlled experiment
     * */
    public void experiment(EXPERIMENT experiment, DECAY decayType) {
        FailedTask task = null;
        List<FailedTask> corruptedTasks = new ArrayList<>();
        List<FailedTask> successTasks = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        List<Long> avgTimes = new ArrayList<>();

        for(int i = 0; i < 100; i++){
            long startAvgTime = System.currentTimeMillis();
            long avgTime = 0; //Calc the avg time taken for each task
            task = FailedTaskGenerator.generateFailedTaskWithControlledProbability();

            while(adheresToDomain(task)){
                //Randomness for a task to succeed
                int rand = (int) (RANDOM.nextDouble() * 100) + 1;

                boolean probToSucceed;

                if(decayType == DECAY.EXPONENTIAL){
                    probToSucceed = succeedsWithExponentialProbability(experiment, task);
                }else{
                    probToSucceed = succeedsWithLinearProbability(experiment, task);
                }

                if(probToSucceed){
                    task.setStatus(FailedTask.Status.SUCCESS);
                    successTasks.add(task);
                    avgTime = System.currentTimeMillis() - startAvgTime;
                    IO.println(String.format("Success: %s. Time taken: %d ms", task, avgTime));
                    break;
                }

                sleep(task.retryDelay());
                task.incrementFailureCount();
            }

            if(task.status() == FailedTask.Status.FAIL){
                //If the task fails
                task.setStatus(FailedTask.Status.CORRUPTED);
                corruptedTasks.add(task);
                avgTime = System.currentTimeMillis() - startAvgTime;
                IO.println(String.format("Corrupted: %s. Time taken: %d ms", task, avgTime));
            }

            avgTimes.add(avgTime);

        }

        printResult(corruptedTasks, successTasks, startTime, avgTimes);
    }

    /**
     * My domain explicitly checks if a failed task has a negative retry count
     * The set of inputs that my domain requires depends on the priority {@link Priority} and failure count of a task
     * A LOW(1) priority task can only fail a max of 3 times before it is dropped with a retry delay of 5 seconds
     * A MED(2) priority task can only fail a max of 6 times before it is dropped with a retry delay of 3 seconds
     * A HIGH(3) priority task can only fail a max of 9 times before it is dropped with a retry delay of 1 seconds
     * My domain given f(x, y) where x is the priority of the task and y is the failure count
     * The domain reads that x >= 1 && x <= 3 AND y >= 0 (is true for all priorities)
     * when x = 1, y >= 0 && y <= 3
     * when x = 2, y >= 0 && y <= 6
     * when x = 3, y >= 0 && y <= 9
     * Range of possible outputs (SUCCESS or CORRUPTED)
     *
     */
    boolean adheresToDomain(FailedTask failedTask){
        int failureCount = failedTask.failureCount();

        if(failureCount < 0){
            return false;
        }

        Priority prio = failedTask.priority();
        return switch (prio) {
            case LOW -> {
                if(failureCount <= LOW_MAX_RETRY_COUNT){
                    yield true;
                }else {
                    yield false;
                }
            }

            case MEDIUM -> {
                if(failureCount <= MED_MAX_RETRY_COUNT){
                    yield true;
                }else {
                    yield false;
                }
            }

            case HIGH -> {
                if(failureCount <= HIGH_MAX_RETRY_COUNT){
                    yield true;
                }else {
                    yield false;
                }
            }

            default -> false;
        };

    }


    //Probability to indicate if a failed task succeeds.
// The probability of success reduces the more a tasks fails but with an exponential impl
    boolean succeedsWithExponentialProbability(EXPERIMENT experiment, FailedTask task){
        Priority prio = task.priority();
        int failureCount = task.failureCount();
        double roll = experiment == EXPERIMENT.CONTROLLED ? RANDOM.nextDouble() : Math.random();

        //Probability of success reduces exponentially the more times the tasks fails
        double baseProbability = switch (prio){
            case LOW -> {
                yield LOW_BASE_PROBABILITY;
            }
            case MEDIUM -> {
                yield MED_BASE_PROBABILITY;
            }
            case HIGH -> {
                yield HIGH_BASE_PROBABILITY;
            }

            default -> 0.0;
        };

        baseProbability = baseProbability * Math.pow(0.8, failureCount);

        return roll <= baseProbability;

    }

    //Probability to indicate if a failed task succeeds.
// The probability of success reduces the more a tasks fails but with a very linear/harsh impl
    boolean succeedsWithLinearProbability(EXPERIMENT experiment, FailedTask task){
        Priority prio = task.priority();
        int failureCount = task.failureCount();
        double roll = experiment == EXPERIMENT.CONTROLLED ? RANDOM.nextDouble() : Math.random();

        //Probability of success reduces exponentially the more times the tasks fails
        double baseProbability = switch (prio){
            case LOW -> {
                yield LOW_BASE_PROBABILITY / (failureCount + 1);
            }
            case MEDIUM -> {
                yield MED_BASE_PROBABILITY / (failureCount + 1);
            }
            case HIGH -> {
                yield HIGH_BASE_PROBABILITY / (failureCount + 1);
            }

            default -> 0.0;
        };

        return roll <= baseProbability;

    }

     void printResult(List<FailedTask> corruptedTasks, List<FailedTask> successTasks, long startTime, List<Long> avgTimes) {
        long endTime = System.currentTimeMillis();
        long totalAvg = avgTimes.stream().reduce(0L, Long::sum);
        IO.println("Total time taken: " + (endTime - startTime));
        IO.println("Avg time per task: " +  (totalAvg / 100));
        IO.println("Success Tasks: " + successTasks.size());
        IO.println("Corrupted Tasks Count: " + corruptedTasks.size());
    }

    void sleep(int ms){
        try {
            Thread.sleep(ms);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }


}
