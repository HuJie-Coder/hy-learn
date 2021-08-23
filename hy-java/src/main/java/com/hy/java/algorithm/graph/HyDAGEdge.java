package com.hy.java.algorithm.graph;

/**
 * HyDAGEdge
 * 图边
 *
 * @author Jie.Hu
 * @date 8/16/21 2:01 PM
 */
public class HyDAGEdge {

    private HyDAGVertex start;

    private HyDAGVertex end;

    public HyDAGEdge(HyDAGVertex start, HyDAGVertex end) {
        this.start = start;
        this.end = end;
        this.start.addOutput(this);
        this.end.addInput(this);
    }

    public HyDAGVertex getStart() {
        return start;
    }

    public void setStart(HyDAGVertex start) {
        this.start = start;
    }

    public HyDAGVertex getEnd() {
        return end;
    }

    public void setEnd(HyDAGVertex end) {
        this.end = end;
    }
}
