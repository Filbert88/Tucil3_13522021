import java.util.*;

public class GBFS {
    public static SearchResult findLadder(String start, String end, NeighborGenerator generator) {
        PriorityQueue<Node> prioQueue = new PriorityQueue<>();
        Map<String, Boolean> visited = new HashMap<>();
        int visitedNodes = 0;

        prioQueue.add(new Node(start, null, calculateHammingDistance(start, end)));
        visited.put(start, true);

        while (!prioQueue.isEmpty()) {
            Node current = prioQueue.poll();
            visitedNodes++;

            if (current.word.equals(end)) {
                List<String> path = getPath(current);
                return new SearchResult(path, visitedNodes);
            }
        
            List<String> neighbors = generator.getNeighbors(current.word);
            for (String neighbor : neighbors) {
                if (!visited.containsKey(neighbor)) {
                    visited.put(neighbor, true);
                    prioQueue.add(new Node(neighbor, current, calculateHammingDistance(neighbor, end)));
                }
            }
        }
        return new SearchResult(Collections.emptyList(), visitedNodes);
    }

    private static List<String> getPath(Node endNode) {
        List<String> path = new ArrayList<>();
        for (Node node = endNode; node != null; node = node.parent) {
            path.add(node.word);
        }
        Collections.reverse(path);
        return path;
    }

    private static int calculateHammingDistance(String word, String end) {
        int difference = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != end.charAt(i)) {
                difference++;
            }
        }
        return difference; 
    }

    static class Node implements Comparable<Node> {
        String word;
        Node parent;
        int heuristic;
    
        Node(String word, Node parent, int heuristic) {
            this.word = word;
            this.parent = parent;
            this.heuristic = heuristic;
        }
    
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.heuristic, other.heuristic);
        }
    }
}