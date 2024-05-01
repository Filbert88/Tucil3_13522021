import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long startTime = System.currentTimeMillis();
        Set<String> words = WordLoader.loadWords("words.txt");
        Map<String, List<String>> graph = GraphBuilder.buildGraph(words);
        long loadTime = System.currentTimeMillis() - startTime;

        System.out.println("Enter start word: ");
        String start = scanner.nextLine().toLowerCase();
        System.out.println("Enter end word: ");
        String end = scanner.nextLine().toLowerCase();

        if (!words.contains(start) || !words.contains(end) || start.length() != end.length()) {
            System.out.println("Invalid input words or word lengths are not equal.");
            System.exit(0);
        }

        System.out.println("Select algorithm: UCS (Uniform Cost Search), GBFS (Greedy Best First Search), or A* : ");
        String algorithm = scanner.nextLine().trim().toUpperCase();

        List<String> ladder = new ArrayList<>();
        long algStartTime = System.currentTimeMillis();
        switch (algorithm) {
            case "UCS":
                ladder = UCS.findLadder(start, end, graph);
                break;
            case "GBFS":
                ladder = GBFS.findLadder(start, end, graph);
                break;
            case "A*":
                ladder = Astar.findLadder(start, end, graph);
                break;
            default:
                System.out.println("Invalid algorithm choice");
                System.exit(0);
        }
        long algExecutionTime = System.currentTimeMillis() - algStartTime;

        if (ladder.isEmpty()) {
            System.out.println("No path found between " + start + " and " + end);
        } else {
            ladder = ladder.stream().map(String::toUpperCase).toList();
            System.out.println("Path found: " + String.join(" -> ", ladder));
            System.out.println(ladder.size());
        }

        System.out.println("Load time: " + loadTime + " milliseconds");
        System.out.println("Execution time: " + algExecutionTime + " milliseconds");
        scanner.close();
    }
}