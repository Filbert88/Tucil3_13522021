import java.util.*;

public class SearchResult {
    public final List<String> path;
    public final int visitedNodes;

    public SearchResult(List<String> path, int visitedNodes) {
        this.path = path;
        this.visitedNodes = visitedNodes;
    }
}
