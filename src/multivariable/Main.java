import multivariable.FailedTaskRunner;

void main() {
    FailedTaskRunner failedTaskRunner = new FailedTaskRunner();
    failedTaskRunner.experiment(FailedTaskRunner.EXPERIMENT.CONTROLLED, FailedTaskRunner.DECAY.LINEAR);
}