package com.hy.java.algorithm.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * HyDAGVertex
 * 图顶点
 *
 * @author Jie.Hu
 * @date 8/16/21 2:01 PM
 */
public class HyDAGVertex<T> {

    private T data;

    private List<HyDAGEdge> input = new ArrayList<>();

    private List<HyDAGEdge> output = new ArrayList<>();

    public HyDAGVertex(T data) {
        this.data = data;
    }

    protected void addInput(HyDAGEdge dagEdge) {
        if (dagEdge.getStart() != this) {
            return;
        }
        input.add(dagEdge);
    }

    protected void addOutput(HyDAGEdge dagEdge) {
        if (dagEdge.getStart() != this) {
            return;
        }
        output.add(dagEdge);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<HyDAGEdge> getInput() {
        return input;
    }

    public void setInput(List<HyDAGEdge> input) {
        this.input = input;
    }

    public List<HyDAGEdge> getOutput() {
        return output;
    }

    public void setOutput(List<HyDAGEdge> output) {
        this.output = output;
    }
}
