package com.hy.java.concurrent;

import com.hy.java.concurrent.lock.CLHLock;

/**
 * HyCASTest
 *
 * @author Jie.Hu
 * @date 8/11/21 8:18 AM
 */
public class HyCASTest {

    public static Integer index = 0;

    public static void main(String[] args) {
        CLHLock lock = new CLHLock();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println(System.currentTimeMillis() + " " + index++);
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
            }
        };

        Thread thread = new Thread(task);
//        Thread thread1 = new Thread(task);
//        Thread thread2 = new Thread(task);
//
        thread.start();
//        thread1.start();
//        thread2.start();


    }
}
