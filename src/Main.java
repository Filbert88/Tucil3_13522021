import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<String> words = WordLoader.loadWords("words.txt");
        Map<String, List<String>> graph = GraphBuilder.buildGraph(words);
    
        System.out.println("Enter start word:");
        String start = scanner.nextLine().toLowerCase();
        System.out.println("Enter end word:");
        String end = scanner.nextLine().toLowerCase();
    
        if (!words.contains(start) || !words.contains(end) || start.length() != end.length()) {
            System.out.println("Invalid input words or word lengths are not equal.");
            System.exit(0);
        }
    
        System.out.println("Select algorithm: UCS (Uniform Cost Search), GBFS (Greedy Best First Search), or A*:");
        String algorithm = scanner.nextLine().trim().toUpperCase();
    
        SearchResult result = null;
        long algStartTime = System.currentTimeMillis();
        switch (algorithm) {
            case "UCS":
                result = UCS.findLadder(start, end, graph);
                break;
            case "GBFS":
                result = GBFS.findLadder(start, end, graph); 
                break;
            case "A*":
                result = Astar.findLadder(start, end, graph); 
                break;
            default:
                System.out.println("Invalid algorithm choice");
                System.exit(0);
        }
        long algExecutionTime = System.currentTimeMillis() - algStartTime;
    
        if (result.path.isEmpty()) {
            System.out.println("No path found between " + start + " and " + end);
        } else {
            List<String> ladder = result.path.stream().map(String::toUpperCase).toList();
            System.out.println("Path found: " + String.join(" -> ", ladder));
            System.out.println("Visited nodes: " + result.visitedNodes);
        }
    
        System.out.println("Execution time: " + algExecutionTime + " milliseconds");
        scanner.close();
    }    
}