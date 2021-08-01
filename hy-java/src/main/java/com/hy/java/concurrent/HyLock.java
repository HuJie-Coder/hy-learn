package com.hy.java.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * HyLock
 *
 * @author Jie.Hu
 * @date 7/31/21 5:35 PM
 */
public class HyLock {


    public static void main(String[] args) throws InterruptedException {

        Lock lock = new ReentrantLock();

        Condition condition = lock.newCondition();
        condition.signal();
        condition.await();


    }

}
