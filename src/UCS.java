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
        visited.put(start, 0); 

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();

            if (current.cost <= visited.get(current.word)) {
                visitedNodes++;
                
                if (current.word.equals(end)) {
                    List<String> path = getPath(current);
                    return new SearchResult(path, visitedNodes);
                }

                List<String> neighbors = generator.getNeighbors(current.word);
                for (String neighbor : neighbors) {
                    int newCost = current.cost + 1;
                    if (!visited.containsKey(neighbor) || newCost < visited.get(neighbor)) {
                        visited.put(neighbor, newCost);
                        priorityQueue.add(new Node(neighbor, current, newCost));
                    }
                }
            }
        }
        return new SearchResult(Collections.emptyList(), visitedNodes);
    }

    private static List<String> getPath(Node endNode) {
        List<String> path = new ArrayList<>();
        Node current = endNode;
        while (current != null) {
            path.add(current.word);
            current = current.parent;
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

