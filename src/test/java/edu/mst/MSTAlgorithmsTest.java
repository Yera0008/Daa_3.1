package edu.mst;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class MSTAlgorithmsTest {
    @Test
    public void smallGraphCorrectness() {
        Graph G = new Graph(4);
        G.addEdge(new Edge(0,1,1));
        G.addEdge(new Edge(0,2,4));
        G.addEdge(new Edge(0,3,3));
        G.addEdge(new Edge(1,2,2));
        G.addEdge(new Edge(2,3,5));

        PrimMST prim = new PrimMST(G);
        prim.run(0);

        KruskalMST kr = new KruskalMST(G);
        kr.run();

        assertThat(prim.weight()).isEqualTo(kr.weight());
        assertThat(prim.edges().size()).isEqualTo(G.V() - 1);
        assertThat(kr.edges().size()).isEqualTo(G.V() - 1);

        boolean[] seen = new boolean[G.V()];
        for (Edge e : kr.edges()) {
            seen[e.either()] = true;
            seen[e.other(e.either())] = true;
        }
        for (boolean b : seen) assertThat(b).isTrue();
    }

    @Test
    public void disconnectedGraphHandled() {
        Graph G = new Graph(4);
        G.addEdge(new Edge(0,1,1));
        PrimMST prim = new PrimMST(G);
        prim.run(0);
        assertThat(prim.edges().size()).isLessThan(3);
        KruskalMST kr = new KruskalMST(G);
        kr.run();
        assertThat(kr.edges().size()).isLessThan(3);
    }

    @Test
    public void operationCountsNonNegative() {
        Graph G = new Graph(6);
        G.addEdge(new Edge(0,1,7));
        G.addEdge(new Edge(0,2,5));
        G.addEdge(new Edge(1,2,8));
        G.addEdge(new Edge(1,3,9));
        G.addEdge(new Edge(2,4,7));
        G.addEdge(new Edge(3,4,5));
        G.addEdge(new Edge(3,5,6));
        PrimMST prim = new PrimMST(G);
        prim.run(0);
        assertThat(prim.getEdgeExaminations()).isGreaterThanOrEqualTo(0);
        assertThat(prim.getPqInsertions()).isGreaterThanOrEqualTo(0);
        assertThat(prim.getPqRemovals()).isGreaterThanOrEqualTo(0);
        KruskalMST kr = new KruskalMST(G);
        kr.run();
        assertThat(kr.getComparisons()).isGreaterThanOrEqualTo(0);
        assertThat(kr.getUnionOps()).isGreaterThanOrEqualTo(0);
        assertThat(kr.getFindOps()).isGreaterThanOrEqualTo(0);
    }
}
