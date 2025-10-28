# Minimum Spanning Tree Algorithms: Comprehensive Performance Analysis

A complete performance study and comparison of **Prim’s** and **Kruskal’s** algorithms for computing the **Minimum Spanning Tree (MST)** on graphs of varying size and density.

## Executive Summary

This report compares two classic MST algorithms — **Prim’s** and **Kruskal’s** — using both **theoretical analysis** and **empirical testing**.

Both algorithms were implemented from scratch in Java and instrumented to measure:
- Comparisons
- Union and Find operations
- Priority Queue insertions/removals
- Execution time in milliseconds

Although theoretical analysis suggests that **Prim’s algorithm** is better for *dense graphs*, actual results from this project show that **Kruskal’s algorithm consistently outperforms Prim’s** across almost all graph sizes, especially for larger datasets.

---

## Project Structure

- Daa_3.1/
- │
- ├── src/
- │ ├── main/java/edu/mst/
- │ │ ├── Graph.java
- │ │ ├── Edge.java
- │ │ ├── UnionFind.java
- │ │ ├── KruskalMST.java
- │ │ ├── PrimMST.java
- │ │ ├── MSTRunner.java
- │ │ └── GraphGenerator.java
- │ │
- │ └── test/java/edu/mst/
- │ └── MSTAlgorithmsTest.java
- │
- ├── assign_3_input.json
- ├── output.json
- ├── summary.csv
- │
- ├── pom.xml
- └── README.md

```markdown
# Minimum Spanning Tree Algorithms: Prim vs Kruskal

## Implemented Algorithms

### Prim's Algorithm
- **Approach**: Greedy vertex expansion using a priority queue
- **Implementation**: `PrimMST.java`
- **Core Code Logic**:
```java
while (!pq.isEmpty() && mst.size() < G.V() - 1) {
    Edge e = pq.poll();
    int u = e.either();
    int v = e.other(u);
    if (marked[u] && marked[v]) continue;
    mst.add(e);
    totalWeight += e.weight();
    if (!marked[u]) visit(u);
    if (!marked[v]) visit(v);
}
```
- **Complexity**: O(E log V)
- **Ideal for**: Dense graphs

### Kruskal's Algorithm
- **Approach**: Greedy edge selection with cycle detection using Union-Find
- **Implementation**: `KruskalMST.java`
- **Core Code Logic**:
```java
Collections.sort(edges);
for (Edge e : edges) {
    if (mst.size() == G.V() - 1) break;
    int u = e.either();
    int v = e.other(u);
    if (!uf.connected(u, v)) {
        uf.union(u, v);
        mst.add(e);
        totalWeight += e.weight();
    }
}
```
- **Complexity**: O(E log E)
- **Ideal for**: Sparse graphs

## Performance Summary

| Graph Size | Vertices | Dominant Algorithm | Time Advantage |
|------------|----------|-------------------|----------------|
| 🔵 Small   | < 50     | **Prim**          | +10-15% faster |
| 🟢 Medium  | 50-300   | **Kruskal**       | +8-10% faster  |
| 🟡 Large   | 300-1000 | **Kruskal**       | +30-40% faster |
| 🔴 X-Large | > 1000   | **Kruskal**       | +20-45% faster |

## Quick Selection Guide

```java
// Algorithm selection based on graph size
public MSTAlgorithm selectAlgorithm(Graph graph) {
    int V = graph.vertices();
    if (V < 50) {
        return new PrimMST(graph);  // Faster for small graphs
    } else {
        return new KruskalMST(graph); // Better for larger graphs
    }
}
```

## Key Findings

- **Both algorithms** produce identical MST weights (correctness verified)
- **Kruskal outperforms Prim** on graphs with ≥50 vertices
- **20-40% faster execution** for Kruskal on large datasets
- **Better scalability** demonstrated by Kruskal's algorithm

## Usage Recommendations

| Scenario | Recommended Algorithm | Reason |
|----------|---------------------|---------|
| Graphs < 50 vertices | **Prim** | Slightly faster, simpler |
| Graphs ≥ 50 vertices | **Kruskal** | 20-40% faster empirically |
| Dense graphs | **Kruskal** | Constant-factor efficiency |
| Time-critical systems | **Kruskal** | Predictable performance |

## Theoretical vs Empirical
Empirical scaling matches theoretical predictions

Kruskal's better constant factors outweigh higher operation counts

Prim's heap operations create significant overhead

Kruskal benefits from better memory locality with sequential processing

Conclusion: Kruskal's algorithm exhibits better scalability, faster execution, and lower real-world time cost on graphs beyond 50 vertices.
