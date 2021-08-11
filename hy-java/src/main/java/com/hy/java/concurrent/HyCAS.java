package com.hy.java.concurrent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * HyCAS
 *
 * @author Jie.Hu
 * @date 8/11/21 8:03 AM
 */
public class HyCAS {

    public static void main(String[] args) {
        String str1 = new String("a");
        String str2 = new String("b");
        // 返回内存值
        AtomicReference<String> atomic = new AtomicReference<>(str1);
        String result1 = atomic.getAndSet(str1);
        String result2 = atomic.getAndSet(str2);
        System.out.println(result1);
        System.out.println(result2);
    }
}
