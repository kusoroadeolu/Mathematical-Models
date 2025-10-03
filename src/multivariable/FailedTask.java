package multivariable;

public class FailedTask {

    enum Status {
        CORRUPTED,
        SUCCESS,
        FAIL
    }

    private Priority priority;
    private int failureCount;
    private int retryDelay;
    private Status status = Status.FAIL;


    public FailedTask setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Status status() {
        return this.status;
    }

    ;

    public FailedTask(Priority priority, int failureCount, int retryDelay) {
        this.priority = priority;
        this.failureCount = failureCount;
        this.retryDelay = retryDelay;
    }

    public Priority priority() {
        return priority;
    }

    public FailedTask setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public int failureCount() {
        return failureCount;
    }

    public FailedTask setFailureCount(int failureCount) {
        this.failureCount = failureCount;
        return this;
    }

    public void incrementFailureCount() {
        failureCount++;
    }

    public int retryDelay() {
        return retryDelay;
    }

    public FailedTask setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
        return this;
    }

    @Override
    public String toString() {
        return "FailedTask{" +
                "priority=" + priority +
                ", failureCount=" + failureCount +
                ", retryDelay=" + this.retryDelay + "ms" +
                '}';
    }
}
