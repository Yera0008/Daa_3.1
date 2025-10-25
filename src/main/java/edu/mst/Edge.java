package edu.mst;

import java.util.Objects;

public class Edge implements Comparable<Edge> {
    private final int u;
    private final int v;
    private final double weight;

    public Edge(int u, int v, double weight) {
        if (u < 0 || v < 0) throw new IllegalArgumentException("Vertex ids must be non-negative");
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public int either() { return u; }
    public int other(int vertex) {
        if (vertex == u) return v;
        if (vertex == v) return u;
        throw new IllegalArgumentException("Vertex not part of edge");
    }
    public double weight() { return weight; }

    @Override
    public int compareTo(Edge o) {
        return Double.compare(this.weight, o.weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge e = (Edge) o;
        return Double.compare(e.weight, weight) == 0 &&
                ((e.u == u && e.v == v) || (e.u == v && e.v == u));
    }

    @Override
    public int hashCode() {
        int a = Math.min(u, v);
        int b = Math.max(u, v);
        return Objects.hash(a, b, weight);
    }

    @Override
    public String toString() {
        return String.format("(%d - %d : %.2f)", u, v, weight);
    }
}


