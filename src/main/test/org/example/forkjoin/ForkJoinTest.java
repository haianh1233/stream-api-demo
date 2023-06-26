package org.example.forkjoin;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinTest {
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool(2);

    @Test
    public void testWithCustomRecursiveAction() {
        String workload = "hello word";
        CustomRecursiveAction customRecursiveAction = new CustomRecursiveAction(workload);

        forkJoinPool.submit(customRecursiveAction);
    }

    @Test
    public void testWithCustomRecursiveActionAndFork() throws InterruptedException {
        String workload = "hello word";
        CustomRecursiveAction customRecursiveAction = new CustomRecursiveAction(workload);

        // method is used to submit a task to the ForkJoinPool for execution
        customRecursiveAction.fork();
        System.out.println("Trigger join");
        customRecursiveAction.join();
    }

    @Test
    public void testWithCustomerRecursiveTaskAndExecute() {
        List<Integer> testList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            testList.add(i);
        }

        int[] arr = testList.stream().mapToInt(testList::get).toArray();
        CustomRecursiveTask customRecursiveTask = new CustomRecursiveTask(arr);

        System.out.println("Test with execute ------------");
        // Execute the task asynchronously in the ForkJoinPool
        forkJoinPool.execute(customRecursiveTask);

        System.out.println("Trigger join");
        // Wait for the task to complete and retrieve the result
        int result = customRecursiveTask.join();
        System.out.println("Final result = " + result);
    }

    @Test
    public void testWithCustomRecursiveTaskAndInvoke() {
        List<Integer> testList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            testList.add(i);
        }

        int[] arr = testList.stream().mapToInt(testList::get).toArray();
        CustomRecursiveTask customRecursiveTask = new CustomRecursiveTask(arr);

        System.out.println("Test with invoke ------------");
        int result = forkJoinPool.invoke(customRecursiveTask);
        System.out.println("Final result = " + result);
    }
}
