package com.hy.java.concurrent.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CLHLock
 *
 * @author Jie.Hu
 * @date 7/31/21 8:22 AM
 */
public class CLHLock {

    private class CLHNode {
        // 锁状态，false 表示线程没有获取到锁，true 表示线程已经获取到锁
        // 为了保证 locked 在各个线程之间是可见的，使用 volatile 进行修饰
        volatile boolean locked = false;
    }

    /**
     * tail node
     */
    private final AtomicReference<CLHNode> tailNode;


    /**
     * current node
     */
    private final ThreadLocal<CLHNode> curNode;


    private CLHNode node;

    public CLHLock() {
        tailNode = new AtomicReference<>(new CLHNode());
        curNode = new ThreadLocal<CLHNode>() {
            @Override
            protected CLHNode initialValue() {
                return new CLHNode();
            }
        };

    }

    public void lock() {
        CLHNode own = curNode.get();
        own.locked = true;
        CLHNode pre = tailNode.getAndSet(own);
        while (pre.locked) {
        }

    }

    public void unlock() {
        curNode.get().locked = false;
    }


}
