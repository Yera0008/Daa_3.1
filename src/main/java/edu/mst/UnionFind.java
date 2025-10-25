package edu.mst;

public class UnionFind {
    private final int[] parent;
    private final int[] size;
    private int count;
    private long unionCount = 0;
    private long findCount = 0;
    private long comparisonCount = 0;

    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int p) {
        findCount++;
        while (p != parent[p]) {
            comparisonCount++;
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    public boolean connected(int p, int q) {
        comparisonCount++;
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        unionCount++;
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        } else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        count--;
    }

    public int count() { return count; }
    public long getUnionCount() { return unionCount; }
    public long getFindCount() { return findCount; }
    public long getComparisonCount() { return comparisonCount; }
}


