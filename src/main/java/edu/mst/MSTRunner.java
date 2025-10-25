package edu.mst;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MSTRunner {
    private static final ObjectMapper M = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        String inputPath = "assign_3_input.json";
        JsonNode root = M.readTree(new File(inputPath));
        ArrayNode outputs = M.createArrayNode();
        List<String> csvLines = new ArrayList<>();
        csvLines.add("graphName,vertices,edges,primCost,kruskalCost,primTimeMs,kruskalTimeMs,primOps,kruskalOps");

        for (JsonNode graphNode : root.get("graphs")) {
            String name = graphNode.path("name").asText();
            int V = graphNode.path("V").asInt();
            ArrayNode edgesNode = (ArrayNode) graphNode.get("edges");
            Graph G = new Graph(V);
            for (JsonNode e : edgesNode) {
                int u = e.get(0).asInt();
                int v = e.get(1).asInt();
                double w = e.get(2).asDouble();
                G.addEdge(new Edge(u, v, w));
            }

            ObjectNode record = M.createObjectNode();
            record.put("name", name);
            record.put("V", G.V());
            record.put("E", G.E());

            PrimMST prim = new PrimMST(G);
            long t0 = System.nanoTime();
            prim.run(0); // start at 0
            long t1 = System.nanoTime();
            long primMs = (t1 - t0) / 1_000_000;
            ObjectNode primNode = M.createObjectNode();
            primNode.put("totalWeight", prim.weight());
            ArrayNode primEdges = M.createArrayNode();
            for (Edge e : prim.edges()) {
                ArrayNode ed = M.createArrayNode();
                ed.add(e.either()); ed.add(e.other(e.either())); ed.add(e.weight());
                primEdges.add(ed);
            }
            primNode.set("edges", primEdges);
            primNode.put("timeMs", primMs);
            primNode.put("edgeExaminations", prim.getEdgeExaminations());
            primNode.put("pqInsertions", prim.getPqInsertions());
            primNode.put("pqRemovals", prim.getPqRemovals());
            primNode.put("comparisons", prim.getComparisons());

            KruskalMST kr = new KruskalMST(G);
            long k0 = System.nanoTime();
            kr.run();
            long k1 = System.nanoTime();
            long kruskalMs = (k1 - k0) / 1_000_000;
            ObjectNode krNode = M.createObjectNode();
            krNode.put("totalWeight", kr.weight());
            ArrayNode krEdges = M.createArrayNode();
            for (Edge e : kr.edges()) {
                ArrayNode ed = M.createArrayNode();
                ed.add(e.either()); ed.add(e.other(e.either())); ed.add(e.weight());
                krEdges.add(ed);
            }
            krNode.set("edges", krEdges);
            krNode.put("timeMs", kruskalMs);
            krNode.put("comparisons", kr.getComparisons());
            krNode.put("unionOps", kr.getUnionOps());
            krNode.put("findOps", kr.getFindOps());

            record.set("prim", primNode);
            record.set("kruskal", krNode);

            outputs.add(record);

            csvLines.add(String.format("%s,%d,%d,%.2f,%.2f,%d,%d,%d,%d",
                    name, G.V(), G.E(),
                    prim.weight(), kr.weight(),
                    primMs, kruskalMs,
                    prim.getEdgeExaminations() + prim.getPqInsertions() + prim.getPqRemovals() + prim.getComparisons(),
                    kr.getComparisons() + kr.getUnionOps() + kr.getFindOps()
            ));
        }

        ObjectNode outRoot = M.createObjectNode();
        outRoot.set("results", outputs);
        M.writerWithDefaultPrettyPrinter().writeValue(new File("output.json"), outRoot);
        Files.write(Paths.get("summary.csv"), String.join("\n", csvLines).getBytes());
        System.out.println("Done");
    }
}

