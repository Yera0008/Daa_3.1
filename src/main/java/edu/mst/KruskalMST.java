package edu.mst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KruskalMST {
    private final Graph G;
    private final List<Edge> mst;
    private double totalWeight;

    private long comparisons = 0;
    private long unionOps = 0;
    private long findOps = 0;

    public KruskalMST(Graph G) {
        this.G = G;
        this.mst = new ArrayList<>();
    }

    public void run() {
        List<Edge> edges = new ArrayList<>();
        for (Edge e : G.edges()) edges.add(e);
        Collections.sort(edges);
        UnionFind uf = new UnionFind(G.V());

        for (Edge e : edges) {
            if (mst.size() == G.V() - 1) break;
            comparisons++;
            int u = e.either();
            int v = e.other(u);
            findOps += 2;
            if (!uf.connected(u, v)) {
                uf.union(u, v);
                unionOps++;
                mst.add(e);
                totalWeight += e.weight();
            }
        }

        findOps += uf.getFindCount();
        comparisons += uf.getComparisonCount();
    }

    public List<Edge> edges() { return Collections.unmodifiableList(mst); }
    public double weight() { return totalWeight; }
    public long getComparisons() { return comparisons; }
    public long getUnionOps() { return unionOps; }
    public long getFindOps() { return findOps; }
}