package multivariable;

import java.util.Random;

public class FailedTaskGenerator {

    private static final double FAILURE_COUNT_PROB = 0.1;
    private static final double CORRUPTED_FAILURE_COUNT_PROB = 0.2;
    private static final Random RANDOM = new Random(12345);

    public static FailedTask generateFailedTaskWithControlledProbability(){
        int i = (int)(RANDOM.nextDouble() * 3) + 1;

        Priority p = switch (i) {
            case 1 -> Priority.LOW;
            case 2 -> Priority.MEDIUM;
            case 3 -> Priority.HIGH;
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };

        //In ms
        int retryDelay  = switch (p) {
            case LOW -> 500;
            case MEDIUM -> 300;
            case HIGH -> 100;
            default -> throw new IllegalStateException("Unexpected value: " + p);
        };

        //Intentionally set to 15 to create bugs
        int failureCount = 0;

        //Random no to assert against probability
        double roll = RANDOM.nextDouble();

        if(roll < FAILURE_COUNT_PROB){
            failureCount = (int) (RANDOM.nextDouble() * 15) + 1;

            roll = RANDOM.nextDouble();

            if(roll < CORRUPTED_FAILURE_COUNT_PROB && failureCount > 0){
                failureCount*=-1;
            }

        }

        return new FailedTask(p, failureCount,  retryDelay);
    }

    public static FailedTask generateFailedTaskWithUnControlledProbability(){
        int i = (int)(Math.random() * 3) + 1;

        Priority p = switch (i) {
            case 1 -> Priority.LOW;
            case 2 -> Priority.MEDIUM;
            case 3 -> Priority.HIGH;
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };

        //In ms
        int retryDelay  = switch (p) {
            case LOW -> 500;
            case MEDIUM -> 300;
            case HIGH -> 100;
            default -> throw new IllegalStateException("Unexpected value: " + p);
        };

        //Intentionally set to 15 to create bugs
        int failureCount = 0;

        //Random no to assert against probability
        double roll = Math.random();

        if(roll < FAILURE_COUNT_PROB){
            failureCount = (int) (Math.random() * 15) + 1;

            roll = Math.random();

            if(roll < CORRUPTED_FAILURE_COUNT_PROB && failureCount > 0){
                failureCount*=-1;
            }

        }

        return new FailedTask(p, failureCount,  retryDelay);
    }

}
