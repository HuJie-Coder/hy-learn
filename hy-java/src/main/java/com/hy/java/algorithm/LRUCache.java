package com.hy.java.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LRUCache
 * LRU Latest Recently Use
 * implement: hashmap + linkedlist
 *
 * @author Jie.Hu
 * @date 8/12/21 4:54 PM
 */
public class LRUCache<K, V> {

    private final static int DEFAULT_CACHE_SIZE = 100;

    private Map<K, Node> cache = new HashMap();

    private Node first;

    private Node last;

    private int capacity = 0;

    private int size = 0;

    private Lock lock = new ReentrantLock();

    private class Node {
        Node pre;
        Node next;
        V data;
    }

    public LRUCache() {
        this(DEFAULT_CACHE_SIZE);
    }

    public LRUCache(int capacity) {
        if (capacity < 1) {
            throw new RuntimeException("capacity must be positive");
        }
        this.capacity = capacity;
        first = new Node();
        last = new Node();
        first.pre = last;
        last.next = first;
    }

    /**
     * add node to cache
     *
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        lock.lock();
        try {
            Node node = new Node();
            node.data = value;
            cache.put(key, node);
            if (size == capacity) {
                removeLast();
            }
            size++;
            moveToFirst(node);
        } finally {
            lock.unlock();
        }

    }

    /**
     * move node to first
     *
     * @param node
     */
    private void moveToFirst(Node node) {
        // initialize
        if (first.next == null) {
            first.next = node;
            last.pre = node;
            node.next = last;
            node.pre = first;
        } else {
            Node next = first.next;
            next.pre = node;
            node.next = next;
            node.pre = first;
            first.next = node;
        }
    }

    /**
     * remove last node
     */
    private void removeLast() {
        Node node = last.pre;
        Node pre = node.pre;
        pre.next = last;
        last.pre = node;
        node.pre = null;
        node.next = null;
        cache.remove(node);
        size--;
    }

    /**
     * get value from cache
     *
     * @param key
     * @return
     */
    public V get(K key) {
        lock.lock();
        try {
            Node node = cache.get(key);
            if (node == null) {
                return null;
            }
            moveToFirst(node);
            return node.data;
        } finally {
            lock.unlock();
        }

    }

    /**
     * remove node from cache
     *
     * @param key
     * @return
     */
    public V remove(K key) {
        lock.lock();
        try {
            Node node = cache.remove(key);
            if (node == null) {
                return null;
            }
            Node pre = node.pre;
            Node next = node.next;

            pre.next = next;
            next.pre = pre;

            node.next = null;
            node.pre = null;
            size--;
            return node.data;
        } finally {
            lock.unlock();
        }
    }
}
