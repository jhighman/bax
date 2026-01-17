package com.highman.ui;

import com.highman.exams.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Displays questions and handles answer input.
 */
public class QuestionDisplay extends JPanel {
    private final JLabel questionLabel;
    private final JPanel choicesPanel;
    private ButtonGroup choicesGroup;
    private JTextArea fillInAnswerField;
    private List<JComboBox<String>> matchingComboBoxes;
    private OrderedItemPanel orderedItemPanel;
    private List<JCheckBox> multipleSelectCheckBoxes;
    private Consumer<String> answerUpdateCallback;

    /**
     * Creates a new QuestionDisplay.
     */
    public QuestionDisplay(Consumer<String> answerUpdateCallback) {
        this.answerUpdateCallback = answerUpdateCallback;
        setLayout(new BorderLayout(10, 10));
        
        // Question Panel
        JPanel questionPanel = new JPanel(new BorderLayout(5, 5));
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        questionPanel.add(questionLabel, BorderLayout.NORTH);

        // Choices Panel
        choicesPanel = new JPanel();
        choicesPanel.setLayout(new BoxLayout(choicesPanel, BoxLayout.Y_AXIS));
        
        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.add(questionPanel, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(choicesPanel), BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Displays a question.
     * @param component The exam component to display
     */
    public void displayQuestion(ExamComponent component) {
        // First, clear all existing components
        choicesPanel.removeAll();
        choicesGroup = null;
        fillInAnswerField = null;
        matchingComboBoxes = null;
        orderedItemPanel = null;
        multipleSelectCheckBoxes = null;

        // Set the question text
        questionLabel.setText("<html><body style='width: 400px'>" + 
            ((Question) component).getQuestionText() + "</body></html>");

        String questionType = component.getComponentType();
        switch (questionType) {
            case "MultipleChoice":
                displayMultipleChoice((MultipleChoiceQuestion) component);
                break;
            case "TrueFalse":
                displayTrueFalse();
                break;
            case "FillInTheBlank":
                displayFillInTheBlank();
                break;
            case "Matching":
                displayMatching((MatchingQuestion) component);
                break;
            case "Ordering":
                displayOrdering((OrderingQuestion) component);
                break;
            case "MultipleSelect":
                displayMultipleSelect((MultipleSelectQuestion) component);
                break;
            case "Code":
                displayCode((CodeQuestion) component);
                break;
            case "CodeCompletion":
                displayCodeCompletion((CodeCompletionQuestion) component);
                break;
            case "Tracing":
                displayTracing((TracingQuestion) component);
                break;
            case "Analysis":
                displayAnalysis((AnalysisQuestion) component);
                break;
        }

        // Ensure the UI is properly updated
        choicesPanel.revalidate();
        choicesPanel.repaint();
        
        // Make sure the scroll pane is at the top
        Container parent = choicesPanel.getParent();
        if (parent instanceof JViewport) {
            JViewport viewport = (JViewport) parent;
            viewport.setViewPosition(new Point(0, 0));
        }
    }

    /**
     * Gets the user's answer.
     * @return The user's answer
     */
    public String getAnswer() {
        if (multipleSelectCheckBoxes != null) {
            List<String> answers = new ArrayList<>();
            for (JCheckBox checkBox : multipleSelectCheckBoxes) {
                if (checkBox.isSelected()) {
                    answers.add(checkBox.getActionCommand());
                }
            }
            System.out.println("getAnswer() for multiple select: " + answers);
            return answers.isEmpty() ? null : answers.toString();
        } else if (choicesGroup != null) {
            ButtonModel selectedButton = choicesGroup.getSelection();
            return selectedButton != null ? selectedButton.getActionCommand() : null;
        } else if (fillInAnswerField != null) {
            return fillInAnswerField.getText().trim();
        } else if (matchingComboBoxes != null) {
            // Check if all selections have been made
            for (JComboBox<String> comboBox : matchingComboBoxes) {
                if (comboBox.getSelectedIndex() == -1) {
                    return null; // Not all items have been matched
                }
            }

            // Build the answer string in map format
            StringBuilder answer = new StringBuilder("{");
            MatchingQuestion question = (MatchingQuestion)getCurrentQuestion();
            List<String> leftItems = question.getLeftItems();
            
            for (int i = 0; i < matchingComboBoxes.size(); i++) {
                if (i > 0) {
                    answer.append(", ");
                }
                String leftItem = leftItems.get(i);
                String selectedItem = (String)matchingComboBoxes.get(i).getSelectedItem();
                answer.append("\"").append(leftItem).append("\": \"").append(selectedItem).append("\"");
            }
            answer.append("}");
            
            String result = answer.toString();
            System.out.println("Matching answer: " + result);
            return result;
        } else if (orderedItemPanel != null) {
            return orderedItemPanel.getItems().toString();
        }
        return null;
    }

    private Question getCurrentQuestion() {
        Container parent = getParent();
        while (parent != null && !(parent instanceof ExamAppFrame)) {
            parent = parent.getParent();
        }
        if (parent instanceof ExamAppFrame) {
            ExamAppFrame frame = (ExamAppFrame) parent;
            return (Question) frame.getCurrentQuestion();
        }
        return null;
    }

    private void displayMultipleChoice(MultipleChoiceQuestion question) {
        choicesGroup = new ButtonGroup();
        char choiceLetter = 'A';
        for (String choice : question.getAnswerChoices()) {
            JRadioButton radioButton = new JRadioButton(choiceLetter + ") " + choice);
            radioButton.setActionCommand(String.valueOf(choiceLetter));
            choicesGroup.add(radioButton);
            choicesPanel.add(radioButton);
            choiceLetter++;
        }
    }

    private void displayTrueFalse() {
        choicesGroup = new ButtonGroup();
        JRadioButton trueButton = new JRadioButton("A) True");
        trueButton.setActionCommand("A");
        JRadioButton falseButton = new JRadioButton("B) False");
        falseButton.setActionCommand("B");
        choicesGroup.add(trueButton);
        choicesGroup.add(falseButton);
        choicesPanel.add(trueButton);
        choicesPanel.add(falseButton);
    }

    private void displayFillInTheBlank() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Create and configure the text area
        fillInAnswerField = new JTextArea(3, 30);
        fillInAnswerField.setLineWrap(true);
        fillInAnswerField.setWrapStyleWord(true);
        fillInAnswerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    // Find the ExamAppFrame
                    Container parent = getParent();
                    while (parent != null && !(parent instanceof ExamAppFrame)) {
                        parent = parent.getParent();
                    }
                    if (parent instanceof ExamAppFrame) {
                        ExamAppFrame frame = (ExamAppFrame) parent;
                        frame.handleAnswer();
                    }
                } else {
                    if (answerUpdateCallback != null) {
                        answerUpdateCallback.accept(fillInAnswerField.getText().trim());
                    }
                }
            }
        });
        
        // Add a note about using Ctrl+Enter to submit
        JLabel noteLabel = new JLabel("Press Ctrl+Enter to submit your answer");
        noteLabel.setFont(noteLabel.getFont().deriveFont(Font.ITALIC));
        noteLabel.setForeground(Color.GRAY);
        noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JScrollPane scrollPane = new JScrollPane(fillInAnswerField);
        scrollPane.setMaximumSize(new Dimension(400, 100));
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(5));
        panel.add(noteLabel);
        panel.add(Box.createVerticalStrut(10));
        
        choicesPanel.add(panel);
        
        SwingUtilities.invokeLater(() -> fillInAnswerField.requestFocusInWindow());
    }

    private void displayMatching(MatchingQuestion question) {
        matchingComboBoxes = new ArrayList<>();
        List<String> leftItems = question.getLeftItems();
        List<String> rightItems = question.getRightItems();

        // Create a panel with GridBagLayout for better organization
        JPanel matchingPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add headers
        gbc.gridx = 0;
        gbc.gridy = 0;
        matchingPanel.add(new JLabel("<html><b>Items to Match:</b></html>"), gbc);
        
        gbc.gridx = 1;
        matchingPanel.add(new JLabel("<html><b>Choose Matching Item:</b></html>"), gbc);

        // Add each pair of items
        for (int i = 0; i < leftItems.size(); i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            String leftItem = leftItems.get(i);
            JLabel leftLabel = new JLabel((char)('A' + i) + ") " + leftItem);
            leftLabel.setPreferredSize(new Dimension(300, 25));
            matchingPanel.add(leftLabel, gbc);

            gbc.gridx = 1;
            JComboBox<String> comboBox = new JComboBox<>(rightItems.toArray(new String[0]));
            comboBox.setPreferredSize(new Dimension(300, 25));
            comboBox.setSelectedIndex(-1); // Ensure no initial selection
            // Add an action listener to debug the selection
            comboBox.addActionListener(e -> {
                System.out.println("Selection made for item '" + leftItem + "': " + comboBox.getSelectedItem());
                String currentAnswer = getAnswer();
                System.out.println("Current answer: " + (currentAnswer != null ? currentAnswer : "incomplete"));
            });
            matchingPanel.add(comboBox, gbc);
            matchingComboBoxes.add(comboBox);
        }

        // Add the matching panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(matchingPanel);
        scrollPane.setPreferredSize(new Dimension(650, Math.min(400, leftItems.size() * 35 + 50)));
        choicesPanel.add(scrollPane);
    }

    private void displayOrdering(OrderingQuestion question) {
        orderedItemPanel = new OrderedItemPanel(question.getItems());
        choicesPanel.add(orderedItemPanel);
    }

    private void displayMultipleSelect(MultipleSelectQuestion question) {
        multipleSelectCheckBoxes = new ArrayList<>();
        List<String> choices = question.getAnswerChoices();
        for (int i = 0; i < choices.size(); i++) {
            String choice = choices.get(i);
            JCheckBox checkBox = new JCheckBox((char)('A' + i) + ") " + choice);
            checkBox.setActionCommand(choice); // Store the actual option text
            // Add an action listener to debug checkbox state
            checkBox.addActionListener(e -> {
                JCheckBox source = (JCheckBox) e.getSource();
                System.out.println("Checkbox '" + source.getActionCommand() + "' selected: " + source.isSelected());
                String currentAnswer = getAnswer();
                System.out.println("Current answer: " + (currentAnswer != null ? currentAnswer : "incomplete"));
            });
            multipleSelectCheckBoxes.add(checkBox);
            choicesPanel.add(checkBox);
        }
    }

    private void displayCode(CodeQuestion question) {
        // Create a panel for the code
        JPanel codePanel = new JPanel(new BorderLayout());
        
        // Add the code block in a monospaced font
        JTextArea codeArea = new JTextArea(question.getCodeBlock());
        codeArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        codeArea.setEditable(false);
        codeArea.setBackground(new Color(245, 245, 245));
        codeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        codePanel.add(new JScrollPane(codeArea), BorderLayout.NORTH);
        
        // Add the answer field
        JPanel answerPanel = new JPanel(new BorderLayout());
        answerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JLabel answerLabel = new JLabel("Your Answer:");
        answerPanel.add(answerLabel, BorderLayout.NORTH);
        
        fillInAnswerField = new JTextArea(3, 40);
        fillInAnswerField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        fillInAnswerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    // Find the ExamAppFrame
                    Container parent = getParent();
                    while (parent != null && !(parent instanceof ExamAppFrame)) {
                        parent = parent.getParent();
                    }
                    if (parent instanceof ExamAppFrame) {
                        ExamAppFrame frame = (ExamAppFrame) parent;
                        frame.handleAnswer();
                    }
                } else {
                    if (answerUpdateCallback != null) {
                        answerUpdateCallback.accept(fillInAnswerField.getText().trim());
                    }
                }
            }
        });
        
        // Add a note about using Ctrl+Enter to submit
        JLabel noteLabel = new JLabel("Press Ctrl+Enter to submit your answer");
        noteLabel.setFont(noteLabel.getFont().deriveFont(Font.ITALIC));
        noteLabel.setForeground(Color.GRAY);
        
        answerPanel.add(new JScrollPane(fillInAnswerField), BorderLayout.CENTER);
        answerPanel.add(noteLabel, BorderLayout.SOUTH);
        codePanel.add(answerPanel, BorderLayout.CENTER);
        
        choicesPanel.add(codePanel);
    }

    private void displayCodeCompletion(CodeCompletionQuestion question) {
        // Create a panel for the code completion question
        JPanel codePanel = new JPanel(new BorderLayout());
        
        // Add the code block in a monospaced font
        JTextArea codeArea = new JTextArea(question.getCodeBlock());
        codeArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        codeArea.setEditable(false);
        codeArea.setBackground(new Color(245, 245, 245));
        codeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        codePanel.add(new JScrollPane(codeArea), BorderLayout.NORTH);
        
        // Add the answer field
        JPanel answerPanel = new JPanel(new BorderLayout());
        answerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JLabel answerLabel = new JLabel("Your Solution:");
        answerPanel.add(answerLabel, BorderLayout.NORTH);
        
        fillInAnswerField = new JTextArea(10, 40);
        fillInAnswerField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        fillInAnswerField.setLineWrap(true);
        fillInAnswerField.setWrapStyleWord(true);
        fillInAnswerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    // Find the ExamAppFrame
                    Container parent = getParent();
                    while (parent != null && !(parent instanceof ExamAppFrame)) {
                        parent = parent.getParent();
                    }
                    if (parent instanceof ExamAppFrame) {
                        ExamAppFrame frame = (ExamAppFrame) parent;
                        frame.handleAnswer();
                    }
                } else {
                    if (answerUpdateCallback != null) {
                        answerUpdateCallback.accept(fillInAnswerField.getText().trim());
                    }
                }
            }
        });
        
        // Add a note about using Ctrl+Enter to submit
        JLabel noteLabel = new JLabel("Press Ctrl+Enter to submit your answer");
        noteLabel.setFont(noteLabel.getFont().deriveFont(Font.ITALIC));
        noteLabel.setForeground(Color.GRAY);
        
        answerPanel.add(new JScrollPane(fillInAnswerField), BorderLayout.CENTER);
        answerPanel.add(noteLabel, BorderLayout.SOUTH);
        codePanel.add(answerPanel, BorderLayout.CENTER);
        
        // If there's an image, display it
        if (question.getImagePath() != null && !question.getImagePath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(question.getImagePath());
                JLabel imageLabel = new JLabel(icon);
                codePanel.add(imageLabel, BorderLayout.SOUTH);
            } catch (Exception e) {
                System.err.println("Failed to load image: " + question.getImagePath());
            }
        }
        
        choicesPanel.add(codePanel);
    }

    /**
     * Displays a tracing question with code and trace points.
     */
    private void displayTracing(TracingQuestion question) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Display code block in monospaced font
        JTextArea codeArea = new JTextArea(question.getCodeBlock());
        codeArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        codeArea.setEditable(false);
        codeArea.setBackground(new Color(245, 245, 245));
        codeArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane codeScrollPane = new JScrollPane(codeArea);
        codeScrollPane.setPreferredSize(new Dimension(600, 200));
        panel.add(codeScrollPane);
        panel.add(Box.createVerticalStrut(20));

        // Create input fields for trace points
        fillInAnswerField = new JTextArea(5, 40);
        fillInAnswerField.setLineWrap(true);
        fillInAnswerField.setWrapStyleWord(true);
        fillInAnswerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    Container parent = getParent();
                    while (parent != null && !(parent instanceof ExamAppFrame)) {
                        parent = parent.getParent();
                    }
                    if (parent instanceof ExamAppFrame) {
                        ExamAppFrame frame = (ExamAppFrame) parent;
                        frame.handleAnswer();
                    }
                } else {
                    if (answerUpdateCallback != null) {
                        answerUpdateCallback.accept(fillInAnswerField.getText().trim());
                    }
                }
            }
        });

        // Add trace points label and instructions
        JLabel traceLabel = new JLabel("Enter trace points (one per line):");
        traceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(traceLabel);
        panel.add(Box.createVerticalStrut(5));

        JScrollPane answerScrollPane = new JScrollPane(fillInAnswerField);
        answerScrollPane.setMaximumSize(new Dimension(600, 150));
        panel.add(answerScrollPane);
        panel.add(Box.createVerticalStrut(10));

        // Add note about format
        JLabel noteLabel = new JLabel("<html>Format: Enter each trace point on a new line.<br>" +
            "For variables, include name and value (e.g., 'x = 5').<br>" +
            "Press Ctrl+Enter to submit your answer.</html>");
        noteLabel.setFont(noteLabel.getFont().deriveFont(Font.ITALIC));
        noteLabel.setForeground(Color.GRAY);
        panel.add(noteLabel);

        choicesPanel.add(panel);
        SwingUtilities.invokeLater(() -> fillInAnswerField.requestFocusInWindow());
    }

    /**
     * Displays an analysis question with code and analysis points.
     */
    private void displayAnalysis(AnalysisQuestion question) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Display code block in monospaced font
        JTextArea codeArea = new JTextArea(question.getCodeBlock());
        codeArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        codeArea.setEditable(false);
        codeArea.setBackground(new Color(245, 245, 245));
        codeArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane codeScrollPane = new JScrollPane(codeArea);
        codeScrollPane.setPreferredSize(new Dimension(600, 200));
        panel.add(codeScrollPane);
        panel.add(Box.createVerticalStrut(20));

        // Create input field for analysis
        fillInAnswerField = new JTextArea(8, 40);
        fillInAnswerField.setLineWrap(true);
        fillInAnswerField.setWrapStyleWord(true);
        fillInAnswerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    Container parent = getParent();
                    while (parent != null && !(parent instanceof ExamAppFrame)) {
                        parent = parent.getParent();
                    }
                    if (parent instanceof ExamAppFrame) {
                        ExamAppFrame frame = (ExamAppFrame) parent;
                        frame.handleAnswer();
                    }
                } else {
                    if (answerUpdateCallback != null) {
                        answerUpdateCallback.accept(fillInAnswerField.getText().trim());
                    }
                }
            }
        });

        // Add analysis instructions
        JLabel analysisLabel = new JLabel("Enter your analysis:");
        analysisLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(analysisLabel);
        panel.add(Box.createVerticalStrut(5));

        JScrollPane answerScrollPane = new JScrollPane(fillInAnswerField);
        answerScrollPane.setMaximumSize(new Dimension(600, 200));
        panel.add(answerScrollPane);
        panel.add(Box.createVerticalStrut(10));

        // Add note about format
        JLabel noteLabel = new JLabel("<html>Analyze the code above and provide your detailed analysis.<br>" +
            "Consider: algorithm complexity, potential issues, and improvements.<br>" +
            "Press Ctrl+Enter to submit your answer.</html>");
        noteLabel.setFont(noteLabel.getFont().deriveFont(Font.ITALIC));
        noteLabel.setForeground(Color.GRAY);
        panel.add(noteLabel);

        choicesPanel.add(panel);
        SwingUtilities.invokeLater(() -> fillInAnswerField.requestFocusInWindow());
    }

    public boolean hasMultipleSelectCheckBoxes() {
        return multipleSelectCheckBoxes != null && !multipleSelectCheckBoxes.isEmpty();
    }

    public int getSelectedCheckBoxCount() {
        if (multipleSelectCheckBoxes == null) {
            return 0;
        }
        return (int) multipleSelectCheckBoxes.stream()
            .filter(JCheckBox::isSelected)
            .count();
    }

    public String getUserAnswer() {
        if (fillInAnswerField != null) {
            return fillInAnswerField.getText().trim();
        }
        return null;
    }
} 