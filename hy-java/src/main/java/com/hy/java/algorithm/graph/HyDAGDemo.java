package com.hy.java.algorithm.graph;

import java.util.List;

/**
 * HyDAGDemo
 *
 * @author Jie.Hu
 * @date 8/16/21 2:08 PM
 */
public class HyDAGDemo {

    public static void main(String[] args) {

        HyDAGVertex<String> vertex1 = new HyDAGVertex<>("test1");
        HyDAGVertex<String> vertex2 = new HyDAGVertex<>("test2");
        HyDAGVertex<String> vertex3 = new HyDAGVertex<>("test3");
        HyDAGVertex<String> vertex4 = new HyDAGVertex<>("test4");

        HyDAGEdge dagEdge1 = new HyDAGEdge(vertex1, vertex2);
        HyDAGEdge dagEdge2 = new HyDAGEdge(vertex1, vertex3);
        HyDAGEdge dagEdge3 = new HyDAGEdge(vertex1, vertex4);
        HyDAGEdge dagEdge4 = new HyDAGEdge(vertex2, vertex4);

        traverse(vertex1);
    }

    /**
     * 深度优先
     *
     * @param vertex
     */
    public static void traverse(HyDAGVertex vertex) {

        List<HyDAGEdge> output = vertex.getOutput();
        for (HyDAGEdge edge : output) {
            HyDAGVertex nextVertex = edge.getEnd();
            System.out.println(nextVertex.getData());
            traverse(nextVertex);
        }

    }
}
