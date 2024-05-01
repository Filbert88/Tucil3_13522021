package src.Algorithm;
import java.util.*;

public class UCS {
    public static List<String> findLadder(String start, String end, Map<String, List<String>> graph) {
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));
        Map<String, Integer> visited = new HashMap<>();
        frontier.add(new Node(start, null, 0));

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            // System.out.println("Dequeued: " + current.word + " with cost: " + current.cost); 

            if (current.word.equals(end)) {
                List<String> path = getPath(current);
                // System.out.println("Path found: " + path); 
                return path;
            }

            if (!visited.containsKey(current.word) || visited.get(current.word) > current.cost) {
                visited.put(current.word, current.cost);
                // System.out.println("Visiting: " + current.word + " at cost: " + current.cost); 

                for (String neighbor : graph.get(current.word)) {
                    frontier.add(new Node(neighbor, current, current.cost + 1));
                    // System.out.println("Added to queue: " + neighbor + " with cost: " + (current.cost + 1)); 
                }
            } else {
                // System.out.println("Already visited " + current.word + " with a lower or equal cost: " + visited.get(current.word)); 
            }
        }

        // System.out.println("No path found"); 
        return Collections.emptyList(); // return empty list if no path found
    }

    private static List<String> getPath(Node endNode) {
        List<String> path = new ArrayList<>();
        for (Node node = endNode; node != null; node = node.parent) {
            path.add(node.word);
        }
        Collections.reverse(path);
        return path;
    }

    static class Node {
        String word;
        Node parent;
        int cost;

        Node(String word, Node parent, int cost) {
            this.word = word;
            this.parent = parent;
            this.cost = cost;
        }
    }
}
