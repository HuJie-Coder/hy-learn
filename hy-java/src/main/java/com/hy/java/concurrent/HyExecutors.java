package com.hy.java.concurrent;

import javax.security.sasl.SaslServer;
import java.util.StringJoiner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * HyExecutors
 *
 * @author Jie.Hu
 * @date 8/10/21 6:09 PM
 */
public class HyExecutors {

    public static void main(String[] args) {
        // multi thread
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                StringJoiner joiner = new StringJoiner(",", "(", ")");
                joiner.add(Thread.currentThread().getName());
                joiner.add(String.valueOf(System.currentTimeMillis()));
                String str = joiner.toString();
                System.out.println(str);
            }
        };


        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                StringJoiner joiner = new StringJoiner(",", "(", ")");
                joiner.add(Thread.currentThread().getName());
                joiner.add(String.valueOf(System.currentTimeMillis()));
                String str = joiner.toString();
                System.out.println(str);
            }
        };

        scheduledThreadPool.scheduleAtFixedRate(task1, 0, 2, TimeUnit.SECONDS);
        scheduledThreadPool.scheduleAtFixedRate(task2, 0, 3, TimeUnit.SECONDS);

        // timer single thread
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                StringJoiner joiner = new StringJoiner(",", "(", ")");
                joiner.add(Thread.currentThread().getName());
                joiner.add(String.valueOf(System.currentTimeMillis()));
                String str = joiner.toString();
                System.out.println(str);
            }
        }, 1000, 5000);
    }
}
