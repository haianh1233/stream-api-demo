package org.example.executorservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;

public class ExecutorServiceTest {
    private ExecutorService executorService = getExecutorService();
    private Runnable runnableTask = getRunnableTask();
    private Callable<String> callableTask = getCallableTask();
    private List<Callable<String>> callableTasks = List.of(
            callableTask,
            callableTask,
            callableTask
    );;

    @Test
    public void testExecute() throws ExecutionException, InterruptedException {
        // execute -> fire and forget
        executorService.execute(runnableTask);
    }

    @Test
    public void testSubmit() throws ExecutionException, InterruptedException {
        // submit -> return Future
        Future<String> submitFuture = executorService.submit(callableTask);
        String submitResult = submitFuture.get();
        System.out.println("Result: " + submitResult);
    }

    @Test
    public void testSubmitWithTimeout() throws ExecutionException, InterruptedException {
        // submit -> return Future
        Future<String> submitFuture = executorService.submit(callableTask);
        Exception actualException = null;
        String submitResult = null;
        try {
            // If the execution period is longer than specified (in this case, 200 milliseconds),
            // a TimeoutException will be thrown.
            submitResult = submitFuture.get(200, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            actualException = e;
        }

        Assertions.assertEquals(TimeoutException.class, actualException.getClass());
    }

    @Test
    public void testFutureIsDone() throws InterruptedException {
        // submit -> return Future
        Future<String> submitFuture = executorService.submit(callableTask);
        // permanent checking task execution -> not complete
        boolean isDone1 = submitFuture.isDone();

        // wait for the task execution complete
        TimeUnit.MILLISECONDS.sleep(400);

        boolean isDone2 = submitFuture.isDone();
        Assertions.assertEquals(Boolean.FALSE, isDone1);
        Assertions.assertEquals(Boolean.TRUE, isDone2);
    }

    @Test
    public void testInvokeAny() throws ExecutionException, InterruptedException {
        // invoke any -> return result one of them
        String invokeAnyResult = executorService.invokeAny(callableTasks);
        System.out.println("Invoke any result: " + invokeAnyResult);
    }

    @Test
    public void testInvokeAll() throws InterruptedException {
        // invoke all -> return result all of them
        List<Future<String>> futures = executorService.invokeAll(callableTasks);
        futures.forEach(t -> {
            try {
                System.out.println("Execute result: " + t.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Callable<String> getCallableTask() {
        return () -> {
            System.out.println("Callable task start, working thread " + Thread.currentThread().getName());
            TimeUnit.MILLISECONDS.sleep(300);

            return "Callable task's execution";
        };
    }

    private static Runnable getRunnableTask() {
        return () -> {
            try {
                System.out.println("Runnable task start, working thread " + Thread.currentThread().getName());
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    private static ThreadPoolExecutor getExecutorService() {
        return new ThreadPoolExecutor(
                1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
        );
    }
}
