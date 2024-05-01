package src.utils;
import java.util.*;
import java.util.stream.Collectors;

public class GraphBuilder {
    public static Map<String, List<String>> buildGraph(Set<String> words) {
        return words.parallelStream().collect(Collectors.toConcurrentMap(
            word -> word,
            word -> {
                List<String> neighbors = new ArrayList<>();
                char[] wordArray = word.toCharArray();
                for (int i = 0; i < wordArray.length; i++) {
                    char originalChar = wordArray[i];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c != originalChar) {
                            wordArray[i] = c;
                            String newWord = new String(wordArray);
                            if (words.contains(newWord)) {
                                neighbors.add(newWord);
                            }
                        }
                    }
                    wordArray[i] = originalChar;
                }
                return neighbors;
            }
        ));
    }
}
