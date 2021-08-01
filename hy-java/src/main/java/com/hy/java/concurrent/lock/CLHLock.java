package com.hy.java.concurrent.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CLHLock
 *
 * @author Jie.Hu
 * @date 7/31/21 8:22 AM
 */
public class CLHLock {

    private static class CLHNode {
        // 锁状态，false 表示线程没有获取到锁，true 表示线程已经获取到锁
        // 为了保证 locked 在各个线程之间是可见的，使用 volatile 进行修饰
        volatile boolean locked = false;
    }

    /**
     * tail node
     */
    private final AtomicReference<CLHNode> tailNode;

    /**
     * prenode
     */
    private final ThreadLocal<CLHNode> predNode;

    /**
     * current node
     */
    private final ThreadLocal<CLHNode> curNode;

    public CLHLock() {
        tailNode = new AtomicReference<>(new CLHNode());

        predNode = new ThreadLocal<>();

        curNode = new ThreadLocal<CLHNode>() {
            @Override
            protected CLHNode initialValue() {
                return new CLHNode();
            }
        };
    }

    /**
     * lock
     */
    public void lock() {
        CLHNode node = curNode.get();
        node.locked = true;
        CLHNode preNode = tailNode.getAndSet(node);
        predNode.set(preNode);
        while (preNode.locked) {
            System.out.println(Thread.currentThread().getName() + "自旋中");
        }
        System.out.println(Thread.currentThread().getName() + " get lock");
    }

    /**
     * unlock
     */
    public void unlock() {
        CLHNode node = curNode.get();
        node.locked = false;
        System.out.println(Thread.currentThread().getName() + " release lock");
        // 防止死锁，一直自旋
        CLHNode newNode = new CLHNode();
        curNode.set(newNode);
        //
        curNode.set(predNode.get());
    }
}
