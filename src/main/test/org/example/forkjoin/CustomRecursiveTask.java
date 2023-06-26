package org.example.forkjoin;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CustomRecursiveTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 10;
    private int[] arr;

    public CustomRecursiveTask(int[] arr) {
        this.arr = arr;
    }

    @Override
    protected Integer compute() {
        if (arr.length > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .mapToInt(ForkJoinTask::join)
                    .sum();
        } else {
            return processing(arr);
        }
    }

    private Collection<CustomRecursiveTask> createSubtasks() {
        return List.of(
                new CustomRecursiveTask(Arrays.copyOfRange(arr, 0, arr.length / 2)),
                new CustomRecursiveTask(Arrays.copyOfRange(arr, arr.length / 2, arr.length))
        );
    }

    private Integer processing(int[] arr) {
        int result =  Arrays.stream(arr)
                .filter(a -> a > 10 && a < 27)
                .map(a -> a * 10)
                .sum();

        System.out.println("Processing by " + Thread.currentThread().getName() + " and get result = " + result);
        return result;
    }
}
