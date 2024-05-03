import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<String> words = WordLoader.loadWords("bin/words.txt");
        NeighborGenerator neighbor = new NeighborGenerator(words);
        
        System.out.println("Welcome to Word Ladder Solver!");
        System.out.println("Available algorithms: ");
        System.out.println("1. UCS (Uniform Cost Search)");
        System.out.println("2. GBFS (Greedy Best First Search)");
        System.out.println("3. Astar (A*)");
        
        String start, end;
        do {
            System.out.print("\nEnter start word: ");
            start = scanner.nextLine().toLowerCase();
            if (!words.contains(start)) {
                System.out.println("The word '" + start + "' is not a valid English word. Please try again.");
            }
        } while (!words.contains(start));
        
        do {
            System.out.print("Enter end word: ");
            end = scanner.nextLine().toLowerCase();
            if (!words.contains(end)) {
                System.out.println("The word '" + end + "' is not a valid English word. Please try again.");
            } else if (start.length() != end.length()) {
                System.out.println("The length of start and end words should be the same. Please try again.");
            }
        } while (!words.contains(end) || start.length() != end.length());
        
        int choice;
        do {
            System.out.print("\nSelect algorithm (1 for UCS, 2 for GBFS, 3 for Astar): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > 3) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number corresponding to the algorithm.");
                choice = 0;
            }
        } while (choice < 1 || choice > 3);
        
        String algorithm;
        switch (choice) {
            case 1:
                algorithm = "UCS";
                break;
            case 2:
                algorithm = "GBFS";
                break;
            case 3:
                algorithm = "ASTAR";
                break;
            default:
                algorithm = "";
                break;
        }
        
        SearchResult result = null;
        long startTime = System.nanoTime();
        switch (algorithm) {
            case "UCS":
                result = UCS.findLadder(start, end, neighbor);
                break;
            case "GBFS":
                result = GBFS.findLadder(start, end, neighbor); 
                break;
            case "ASTAR":
                result = Astar.findLadder(start, end, neighbor); 
                break;
            default:
                System.out.println("Invalid algorithm choice");
                System.exit(0);
        }
        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1_000_000;
        
        if (result.path.isEmpty()) {
            System.out.println("No path found between " + start + " and " + end);
        } else {
            List<String> ladder = result.path.stream().map(String::toUpperCase).toList();
            System.out.println("\nSelected Algorithm: " + algorithm);
            System.out.println("Path found: " + String.join(" -> ", ladder));
            System.out.println("Visited nodes: " + result.visitedNodes);
            System.out.println("Total Path found: " + ladder.size());
        }
        
        System.out.println("Execution time: " + executionTime + " ms");
        scanner.close();
    }    
}
