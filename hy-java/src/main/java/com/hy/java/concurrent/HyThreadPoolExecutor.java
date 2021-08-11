package com.hy.java.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HyExecutors
 *
 * @author Jie.Hu
 * @date 8/8/21 9:59 AM
 */
public class HyThreadPoolExecutor {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue(10);
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                2,
                10,
                1,
                TimeUnit.MINUTES,
                blockingQueue,
                new ThreadFactory() {
                    private AtomicInteger count = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable task) {
                        return new Thread(task, "customer-thread-" + count.getAndIncrement());
                    }
                });

        Future<String> future = poolExecutor.submit(() -> {
            int count = 0;
            while (count++ < 50) {
                System.out.println(Thread.currentThread().getName() + " waiting callable " + count);
                Thread.sleep(500);
            }
            return "result";
        });

        Future<?> submit = poolExecutor.submit(() -> {
            int count = 0;
            while (count++ < 50) {
                System.out.println(Thread.currentThread().getName() + " waiting runnable " + count);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
            }
        });

        while (true) {
            if (future.isDone()) {
                System.out.println("done");
                String s = future.get();
                System.out.println(s);
                System.exit(0);
            }
        }
    }

}
