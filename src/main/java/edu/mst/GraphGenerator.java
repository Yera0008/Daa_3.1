package edu.mst;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GraphGenerator {
    private static final ObjectMapper M = new ObjectMapper();
    private static final Random R = new Random(42);

    public static void main(String[] args) throws IOException {
        ObjectNode root = M.createObjectNode();
        ArrayNode graphs = M.createArrayNode();

        addCategory(graphs, "small", 5, 10, 30, 0.4);
        addCategory(graphs, "medium", 10, 100, 300, 0.3);
        addCategory(graphs, "large", 10, 300, 1000, 0.15);
        addCategory(graphs, "extra", 5, 1000, 3000, 0.05);

        root.set("graphs", graphs);
        M.writerWithDefaultPrettyPrinter().writeValue(new File("assign_3_input.json"), root);
        System.out.println("Generated assign_3_input.json with " + graphs.size() + " graphs.");
    }

    private static void addCategory(ArrayNode graphs, String name, int count,
                                    int minV, int maxV, double density) {
        for (int i = 1; i <= count; i++) {
            int V = minV + R.nextInt(maxV - minV);
            int maxPossibleEdges = V * (V - 1) / 2;
            int E = (int) Math.max(V - 1, maxPossibleEdges * density);

            ObjectNode graphNode = M.createObjectNode();
            graphNode.put("name", name + "-" + i);
            graphNode.put("V", V);
            ArrayNode edges = M.createArrayNode();

            for (int v = 0; v < V - 1; v++) {
                edges.add(arrayEdge(v, v + 1, randWeight()));
            }

            int added = V - 1;
            while (added < E) {
                int u = R.nextInt(V);
                int v = R.nextInt(V);
                if (u == v) continue;
                edges.add(arrayEdge(u, v, randWeight()));
                added++;
            }

            graphNode.set("edges", edges);
            graphs.add(graphNode);
        }
    }

    private static ArrayNode arrayEdge(int u, int v, double w) {
        ArrayNode arr = M.createArrayNode();
        arr.add(u);
        arr.add(v);
        arr.add(w);
        return arr;
    }

    private static double randWeight() {
        return 1.0 + R.nextDouble() * 99.0;
    }
}

