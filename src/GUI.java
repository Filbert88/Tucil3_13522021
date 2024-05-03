import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;

public class GUI extends JFrame {
    private JTextField startWordField;
    private JTextField endWordField;
    private JButton findButton;
    private JPanel resultPanel;
    private JComboBox<String> algorithmSelector;
    private JLabel algoLabel;
    private JLabel timeLabel;
    private JLabel visitedLabel;
    private Set<String> words;
    NeighborGenerator neighbor;

    public GUI() {
        words = WordLoader.loadWords("bin/words.txt");
        neighbor = new NeighborGenerator(words);
        // graph = GraphBuilder.buildGraph(words);
        createGUI();
        setupCloseKeyBinding();
    }

    private void createGUI() {
        setTitle("Word Ladder Game");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JPanel topPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        topPanel.add(new JLabel("Start Word:"));
        startWordField = new JTextField(15);
        topPanel.add(startWordField);
    
        topPanel.add(new JLabel("End Word:"));
        endWordField = new JTextField(10);
        topPanel.add(endWordField);
    
        topPanel.add(new JLabel("Select Algorithm:"));
        algorithmSelector = new JComboBox<>(new String[]{"UCS", "GBFS", "A*"});
        topPanel.add(algorithmSelector);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        findButton = new JButton("Solve");
        findButton.addActionListener(e -> executeSearch());
        buttonPanel.add(findButton);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(1, 3, 5, 5));
        algoLabel = new JLabel("Selected Algorithm: -", JLabel.CENTER);
        timeLabel = new JLabel("Execution Time: -", JLabel.CENTER);
        visitedLabel = new JLabel("Visited Nodes: -", JLabel.CENTER);
        statusPanel.add(algoLabel);
        statusPanel.add(timeLabel);
        statusPanel.add(visitedLabel);

        contentPanel.add(topPanel); 
        contentPanel.add(buttonPanel);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setPreferredSize(new Dimension(580, 300));

        contentPanel.add(scrollPane);
        contentPanel.add(statusPanel);
        add(contentPanel, BorderLayout.CENTER); 
        setLocationRelativeTo(null);
    }   

    private void setupCloseKeyBinding() {
        String closeKey = "CLOSE";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), closeKey);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(closeKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });
    }

    private void executeSearch() {
        resultPanel.removeAll();
        String start = startWordField.getText().toLowerCase();
        String end = endWordField.getText().toLowerCase();
        String algorithm = (String) algorithmSelector.getSelectedItem();
    
        algoLabel.setText("Selected Algorithm: " + algorithm);
    
        if (!words.contains(start) || !words.contains(end) || start.length() != end.length()) {
            JOptionPane.showMessageDialog(this, "Invalid input english words.");
            return;
        }
        
        if (start.length() != end.length()) {
            JOptionPane.showMessageDialog(this, "Word lengths are not equal.");
            return;
        }

        SwingWorker<SearchResult, Void> worker = new SwingWorker<>() {
            long startTime = System.nanoTime();
    
            @Override
            protected SearchResult doInBackground() throws Exception {
                switch (algorithm) {
                    case "UCS":
                        return UCS.findLadder(start, end, neighbor);
                    case "GBFS":
                        return GBFS.findLadder(start, end, neighbor);
                    case "A*":
                        return Astar.findLadder(start, end, neighbor);
                    default:
                        JOptionPane.showMessageDialog(GUI.this, "Invalid algorithm choice");
                        return null;
                }
            }
    
            @Override
            protected void done() {
                try {
                    SearchResult result = get();
                    long endTime = System.nanoTime();
                    long executionTime = (endTime - startTime) / 1_000_000;
                    timeLabel.setText("Execution Time: " + executionTime + " ms");
                    visitedLabel.setText("Visited Nodes: " + (result != null ? result.visitedNodes : 0));
    
                    if (result != null && result.path != null) {
                        displayLadder(result.path);
                    } else {
                        JOptionPane.showMessageDialog(GUI.this, "No path found.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    
        worker.execute();
    }
        
    private void displayLadder(List<String> ladder) {
        resultPanel.removeAll();
        if (ladder == null || ladder.isEmpty()) {
            resultPanel.setLayout(new BorderLayout()); 
            JLabel noPathLabel = new JLabel("No path found.");
            noPathLabel.setHorizontalAlignment(SwingConstants.CENTER); 
            resultPanel.add(noPathLabel, BorderLayout.CENTER);
        } else {
            resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
            String reference = ladder.get(0); 
            char[] lastState = reference.toCharArray(); 
    
            for (int i = 0; i < ladder.size(); i++) {
                JPanel wordPanel = new JPanel();
                wordPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                String current = ladder.get(i);
    
                JLabel numberLabel = new JLabel((i + 1) + ". ");
                wordPanel.add(numberLabel);
    
                for (int j = 0; j < current.length(); j++) {
                    JLabel label = new JLabel(String.valueOf(current.charAt(j)));
                    if (current.charAt(j) != lastState[j]) {
                        label.setForeground(Color.BLUE);  
                        lastState[j] = current.charAt(j); 
                    }
                    wordPanel.add(label);
                }
    
                resultPanel.add(wordPanel);
                if (i < ladder.size() - 1) {
                    resultPanel.add(new JSeparator());
                }
            }
        }
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
