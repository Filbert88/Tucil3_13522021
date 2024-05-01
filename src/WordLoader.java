import java.io.*;
import java.util.*;

public class WordLoader {
    public static Set<String> loadWords(String filename) {
        Set<String> words = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.err.println("Error reading the word file: " + e.getMessage());
        }
        return words;
    }
}
