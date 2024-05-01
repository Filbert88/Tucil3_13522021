import java.util.*;

public class GBFS {
    public static List<String> findLadder(String start, String end, Map<String, List<String>> graph) {
        PriorityQueue<Node> prioQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.heuristic));
        Map<String, Boolean> visited = new HashMap<>();
        int visitedNodes = 0;

        prioQueue.add(new Node(start, null, heuristic(start, end)));
        
        while (!prioQueue.isEmpty()) {
            Node current = prioQueue.poll();
            visitedNodes++;

            if (current.word.equals(end)) {
                System.out.println("Visited nodes: " + visitedNodes);
                return getPath(current);
            }

            if (!visited.containsKey(current.word)) {
                visited.put(current.word, true);
                for (String neighbor : graph.get(current.word)) {
                    if (!visited.containsKey(neighbor)) {
                        prioQueue.add(new Node(neighbor, current, heuristic(neighbor, end)));
                    }
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
        int matches = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == end.charAt(i)) {
                matches++;
            }
        }
        return -matches; 
    }

    static class Node {
        String word;
        Node parent;
        int heuristic;

        Node(String word, Node parent, int heuristic) {
            this.word = word;
            this.parent = parent;
            this.heuristic = heuristic;
        }
    }
}