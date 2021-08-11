package com.hy.java.algorithm.tree;

/**
 * HyTree
 *
 * @author Jie.Hu
 * @date 8/10/21 5:26 PM
 */
public class HyTree {

    public static void main(String[] args) {

        BinaryTree<Integer> tree = new BinaryTree<>(10);

        tree.add(1);
        tree.add(20);
        tree.add(30);
        tree.add(40);
        tree.add(3);
        tree.add(100);

        tree.traverse();

        boolean contains = tree.contains(1);

    }
}
