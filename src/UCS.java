import java.util.*;

public class UCS {
    public static SearchResult findLadder(String start, String end, NeighborGenerator generator) {
        if(start.equals(end)){
            return new SearchResult(Collections.singletonList(start), 0);
        }

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        Map<String, Integer> visited = new HashMap<>();
        int visitedNodes = 0;

        priorityQueue.add(new Node(start, null, 0));

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();
            
            if (current.word.equals(end)) {
                List<String> path = getPath(current);
                return new SearchResult(path, visitedNodes);
            }
            
            if (!visited.containsKey(current.word) || visited.get(current.word) > current.cost) {
                visitedNodes++;
                visited.put(current.word, current.cost);
                List<String> neighbors = generator.getNeighbors(current.word);

                for (String neighbor : neighbors) {
                    if(!visited.containsKey(neighbor) || visited.get(neighbor) > current.cost ){
                        priorityQueue.add(new Node(neighbor, current, current.cost + 1));
                    }
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

    static class Node implements Comparable<Node> {
        String word;
        Node parent;
        int cost;

        Node(String word, Node parent, int cost) {
            this.word = word;
            this.parent = parent;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }
}
