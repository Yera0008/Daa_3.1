package edu.mst;

import java.util.*;

public class Graph {
    private final int V;
    private final List<Edge> edges;
    private final List<List<Edge>> adj;

    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.edges = new ArrayList<>();
        this.adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    public int V() { return V; }
    public int E() { return edges.size(); }

    public void addEdge(Edge e) {
        int u = e.either();
        int v = e.other(u);
        if (u >= V || v >= V) throw new IllegalArgumentException("Vertex id exceeds limit");
        edges.add(e);
        adj.get(u).add(e);
        adj.get(v).add(e);
    }

    public Iterable<Edge> edges() { return Collections.unmodifiableList(edges); }

    public Iterable<Edge> adj(int v) {
        return Collections.unmodifiableList(adj.get(v));
    }
}

