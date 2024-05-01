import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GUI extends JFrame {
    private JTextField startWordField;
    private JTextField endWordField;
    private JButton findButton;
    private JPanel resultPanel;
    private JComboBox<String> algorithmSelector;
    private Set<String> words;
    private Map<String, List<String>> graph;

    public GUI() {
        words = WordLoader.loadWords("words_alpha.txt");
        graph = GraphBuilder.buildGraph(words);
        createGUI();
    }

    private void createGUI() {
        setTitle("Word Ladder Game");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2));
        
        formPanel.add(new JLabel("Start Word:"));
        startWordField = new JTextField(15);
        formPanel.add(startWordField);
        
        formPanel.add(new JLabel("End Word:"));
        endWordField = new JTextField(10);
        formPanel.add(endWordField);
        
        formPanel.add(new JLabel("Select Algorithm:"));
        String[] algorithms = {"UCS", "GBFS", "A*"};
        algorithmSelector = new JComboBox<>(algorithms);
        formPanel.add(algorithmSelector);

        add(formPanel, BorderLayout.NORTH);
        
        JPanel button = new JPanel();
        findButton = new JButton("Solve");
        findButton.addActionListener(e -> executeSearch(words,graph));
        findButton.setFocusable(false);
        button.add(findButton);

        add(button, BorderLayout.SOUTH);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void executeSearch(Set<String> words, Map<String, List<String>> graph) {
        // Clear the result panel before displaying the new result
        resultPanel.removeAll();
        resultPanel.revalidate();
        resultPanel.repaint();
    
        String start = startWordField.getText().toLowerCase();
        String end = endWordField.getText().toLowerCase();
        String algorithm = (String) algorithmSelector.getSelectedItem();
    
        if (!words.contains(start) || !words.contains(end) || start.length() != end.length()) {
            JOptionPane.showMessageDialog(this, "Invalid input words or word lengths are not equal.");
            return;
        }
    
        SwingWorker<List<String>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<String> doInBackground() throws Exception {
                List<String> ladder = null;
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
                        JOptionPane.showMessageDialog(GUI.this, "Invalid algorithm choice");
                }
                return ladder;
            }
    
            @Override
            protected void done() {
                try {
                    displayLadder(get());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    
        worker.execute();
    }       

    private void displayLadder(List<String> ladder) {
        for (int i = 0; i < ladder.size() - 1; i++) {
            JPanel wordPanel = new JPanel();
            String current = ladder.get(i);
            String next = ladder.get(i + 1);
            for (int j = 0; j < current.length(); j++) {
                JLabel label = new JLabel(String.valueOf(current.charAt(j)));
                if (current.charAt(j) != next.charAt(j)) {
                    label.setForeground(Color.BLUE); // Highlight the changed character
                }
                wordPanel.add(label);
            }
            resultPanel.add(wordPanel);
        }
        // Add the last word
        String lastWord = ladder.get(ladder.size() - 1);
        JPanel lastWordPanel = new JPanel();
        for (char c : lastWord.toCharArray()) {
            lastWordPanel.add(new JLabel(String.valueOf(c)));
        }
        resultPanel.add(lastWordPanel);
    
        // Revalidate and repaint the resultPanel
        resultPanel.revalidate();
        resultPanel.repaint();
    }    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setVisible(true);
        });
    }
}
