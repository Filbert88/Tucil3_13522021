import java.util.*;

public class Astar {
    public static SearchResult findLadder(String start, String end,NeighborGenerator generator) {
        if(start.equals(end)){
            return new SearchResult(Collections.singletonList(start), 0);
        }

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        Map<String, Integer> costSoFar = new HashMap<>();
        int visitedNodes = 0;
        priorityQueue.add(new Node(start, null, 0, calculateHammingDistance(start, end)));
        costSoFar.put(start,0);

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();
            visitedNodes++;

            if (current.word.equals(end)) {
                List<String> path = getPath(current);
                return new SearchResult(path, visitedNodes);
            }

            List<String> neighbors = generator.getNeighbors(current.word);
            for (String neighbor : neighbors) {
                int newCost = costSoFar.getOrDefault(current.word, 0) + 1;
                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)) {
                    costSoFar.put(neighbor, newCost);
                    int priority = newCost + calculateHammingDistance(neighbor, end);
                    priorityQueue.add(new Node(neighbor, current, newCost, priority));
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
        int g; // Cost to reach this node from the start node
        int f; // total cost = g + h(dari calculatehammingdistance)

        Node(String word, Node parent, int g, int f) {
            this.word = word;
            this.parent = parent;
            this.g = g;
            this.f = f;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.f, other.f);
        }
    }
}
