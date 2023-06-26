package org.example.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class CustomRecursiveAction extends RecursiveAction {
    private static final int THRESHOLD = 4;
    private String workload;

    public CustomRecursiveAction(String workload) {
        this.workload = workload;
    }

    @Override
    protected void compute() {
        if (workload.length() > THRESHOLD) {
            ForkJoinTask.invokeAll(createSubTasks());
        } else {
            processing(workload);
        }
    }

    private List<CustomRecursiveAction> createSubTasks() {
        String partOne = workload.substring(0, workload.length() / 2);
        String partTwo = workload.substring(workload.length() / 2);

        return List.of(
                new CustomRecursiveAction(partOne),
                new CustomRecursiveAction(partTwo)
        );
    }

    private void processing(String work) {
        String result = work.toUpperCase();

        System.out.println("This result (" + result + ") was processed by " + Thread.currentThread().getName());
    }
}
