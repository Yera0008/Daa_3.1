package edu.mst;

import java.util.*;

public class PrimMST {
    private final Graph G;
    private final boolean[] marked;
    private final PriorityQueue<Edge> pq;
    private final List<Edge> mst;
    private double totalWeight;

    private long edgeExaminations = 0;
    private long pqInsertions = 0;
    private long pqRemovals = 0;
    private long comparisons = 0;

    public PrimMST(Graph G) {
        this.G = G;
        this.marked = new boolean[G.V()];
        this.pq = new PriorityQueue<>();
        this.mst = new ArrayList<>();
    }

    public void run(int start) {
        if (G.V() == 0) return;
        Arrays.fill(marked, false);
        totalWeight = 0;
        pq.clear();
        mst.clear();

        visit(start);

        while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            Edge e = pq.poll(); pqRemovals++;
            int u = e.either();
            int v = e.other(u);
            comparisons++;
            if (marked[u] && marked[v]) continue;
            mst.add(e);
            totalWeight += e.weight();
            if (!marked[u]) visit(u);
            if (!marked[v]) visit(v);
        }
    }

    private void visit(int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            edgeExaminations++;
            int w = e.other(v);
            if (!marked[w]) {
                pq.add(e); pqInsertions++;
            } else {
                comparisons++;
            }
        }
    }

    public List<Edge> edges() { return Collections.unmodifiableList(mst); }
    public double weight() { return totalWeight; }
    public long getEdgeExaminations() { return edgeExaminations; }
    public long getPqInsertions() { return pqInsertions; }
    public long getPqRemovals() { return pqRemovals; }
    public long getComparisons() { return comparisons; }
}

