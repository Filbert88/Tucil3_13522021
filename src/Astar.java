import java.util.*;

public class Astar {
    public static List<String> findLadder(String start, String end, Map<String, List<String>> graph) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
        Map<String, Integer> costSoFar = new HashMap<>();
        int visitedNodes = 0;
        priorityQueue.add(new Node(start, null, 0, heuristic(start, end)));

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();
            visitedNodes++;

            if (current.word.equals(end)) {
                System.err.println("Visited nodes: " + visitedNodes);
                return getPath(current);
            }

            for (String neighbor : graph.get(current.word)) {
                int newCost = costSoFar.getOrDefault(current.word, 0) + 1;
                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)) {
                    costSoFar.put(neighbor, newCost);
                    int priority = newCost + heuristic(neighbor, end);
                    priorityQueue.add(new Node(neighbor, current, newCost, priority));
                }
            }
        }
        return Collections.emptyList();
    }

    private static List<String> getPath(Node endNode) {
        List<String> path = new ArrayList<>();
        for (Node node = endNode; node != null; node = node.parent) {
            path.add(node.word);
        }
        Collections.reverse(path);
        return path;
    }

    private static int heuristic(String word, String end) {
        int distance = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != end.charAt(i)) distance++;
        }
        return distance;
    }

    static class Node {
        String word;
        Node parent;
        int g; // Cost to reach this node from the start node
        int f; // Estimated total cost from start to end through this node

        Node(String word, Node parent, int g, int f) {
            this.word = word;
            this.parent = parent;
            this.g = g;
            this.f = f;
        }
    }
}
