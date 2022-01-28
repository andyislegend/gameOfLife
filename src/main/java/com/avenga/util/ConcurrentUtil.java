package com.avenga.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcurrentUtil {

    public static <T extends Runnable> void execute(ExecutorService executor, List<T> tasks) {
        List<Future<?>> executions = new ArrayList<>(tasks.size());
        log.debug("Start tasks execution");
        for (T task : tasks) {
            var execution = executor.submit(task);
            executions.add(execution);
        }
        finishJobs(executions);
        log.debug("Finish tasks execution");
    }

    private static List<Object> finishJobs(List<Future<?>> jobs) {
        List<Object> results = new ArrayList<>(jobs.size());
        for (Future<?> job : jobs) {
            try {
                results.add(job.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Job interrupted", e);
            } catch (ExecutionException e) {
                log.error("Job execution failed", e);
            }
        }
        return results;
    }

    public static void tryToAwait(CyclicBarrier barrier, long timeout, TimeUnit timeUnit) {
        try {
            barrier.await(timeout, timeUnit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted:", e);
        } catch (BrokenBarrierException e) {
            log.error("Error:", e);
        } catch (TimeoutException e) {
            log.error("Timeout:", e);
        }
    }

    public static void tryToAwait(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted:", e);
        } catch (BrokenBarrierException e) {
            log.error("Error:", e);
        }
    }
}
