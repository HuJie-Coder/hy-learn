package com.hy.java.algorithm;

import java.util.HashMap;

/**
 * Cache
 *
 * 以空间换时间
 *
 * @author Jie.Hu
 * @date 8/12/21 11:27 AM
 */
public class Cache<K, V> {

    private HashMap<K, Node> caches = new HashMap<K, Node>();

    private Node first = new Node();

    private Node last = new Node();

    private class Node {
        Node pre;
        Node next;
        V data;
    }

    public Cache() {
        first.pre = last;
        last.next = first;
    }

    /**
     * add value into cache
     *
     * @param key
     * @param value
     */
    public synchronized void put(K key, V value) {
        Node node = new Node();
        node.data = value;

        if (first.next == null) {
            first.next = node;
            last.pre = node;
            node.pre = first;
            node.next = last;
        } else {
            Node pre = last.pre;
            pre.next = node;
            node.next = last;
            node.pre = pre;
            last.pre = node;
        }

        caches.put(key, node);
    }

    /**
     * get cache from cache
     *
     * @param key
     * @return
     */
    public V get(K key) {
        Node node = caches.get(key);
        return node == null ? null : node.data;
    }

    /**
     * remove key from cache
     *
     * @param key
     */
    public synchronized V remove(K key) {
        Node node = caches.remove(key);
        if (node == null) {
            return null;
        }
        Node pre = node.pre;
        Node next = node.next;
        pre.next = next;
        next.pre = pre;
        return node.data;
    }

}
