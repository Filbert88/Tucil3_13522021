import java.util.*;

public class NeighborGenerator {
    private Set<String> dictionary;

    public NeighborGenerator(Set<String> dictionary) {
        this.dictionary = dictionary;
    }

    public List<String> getNeighbors(String word) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != originalChar) {
                    chars[i] = c;
                    String newWord = new String(chars);
                    if (dictionary.contains(newWord)) {
                        neighbors.add(newWord);
                    }
                }
            }
            chars[i] = originalChar;  
        }
        return neighbors;
    }
}
