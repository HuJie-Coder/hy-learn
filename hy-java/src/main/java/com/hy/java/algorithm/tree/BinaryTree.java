package com.hy.java.algorithm.tree;

import java.util.Objects;

/**
 * BinaryTree
 *
 * @author Jie.Hu
 * @date 8/10/21 4:29 PM
 */
public class BinaryTree<T extends Comparable> {

    private Node root = null;

    private int size = 0;

    private class Node {

        Node leftNode;

        T data;

        Node rightNode;

        Node(T data) {
            this.leftNode = null;
            this.data = data;
            this.rightNode = null;
        }

        void preTraverse(Node node) {
            if (node != null) {
                preTraverse(node.leftNode);
                System.out.println(node.data);
                preTraverse(node.rightNode);
            }
        }

        void midTraverse(Node node) {
            if (node != null) {
                System.out.println(node.data);
                preTraverse(node.leftNode);
                preTraverse(node.rightNode);
            }
        }

        void oldTraverse(Node node) {
            if (node != null) {
                preTraverse(node.leftNode);
                preTraverse(node.rightNode);
                System.out.println(node.data);
            }
        }

        Node find(Node node, T data) {
            if (node == null) {
                return null;
            }

            int value = node.data.compareTo(data);
            if (value == 0) {
                return node;
            } else if (value <= 0) {
                return find(node.leftNode, data);
            } else {
                return find(node.rightNode, data);
            }
        }

    }

    public BinaryTree(T data) {
        this.root = new Node(data);
    }


    /**
     * @param data
     */
    public void add(T data) {
        Objects.requireNonNull(data);
        int value = root.data.compareTo(data);
        if (value <= 0) {
            root.leftNode = addNode(root.leftNode, data);
        } else {
            root.rightNode = addNode(root.rightNode, data);
        }
    }


    /**
     * add data to binary tree
     *
     * @param node
     * @param data
     * @return
     */
    private Node addNode(Node node, T data) {
        if (node == null) {
            return new Node(data);
        }
        int value = node.data.compareTo(data);
        if (value <= 0) {
            node.leftNode = addNode(node.leftNode, data);
        } else {
            node.rightNode = addNode(node.rightNode, data);
        }
        return node;
    }

    /**
     * is binary tree contains data
     *
     * @param data
     * @return
     */
    public boolean contains(T data) {
        return root.find(root, data) == null ? false : true;
    }

    public void traverse() {
        root.preTraverse(root);
    }


}
