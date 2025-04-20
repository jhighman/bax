package com.highman;

import com.highman.exams.*;
import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ExamAppSwing extends JFrame implements ExamComponent {
    private List<ExamComponent> components;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<ExamResult> results;

    private JLabel questionLabel;
    private JPanel choicesPanel;
    private ButtonGroup choicesGroup;
    private JButton skipButton;
    private JButton finishButton;
    private JLabel progressLabel;
    private JTextField fillInAnswerField;
    private List<JComboBox<String>> matchingComboBoxes;
    private List<JLabel> orderingLabels;
    private List<JButton> upButtons;
    private List<JButton> downButtons;
    private List<JCheckBox> multipleSelectCheckBoxes;

    public ExamAppSwing() {
        super("Java Exam Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
        loadQuestions();
        display();
    }

    private void initializeComponents() {
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
        
        // Bottom Panel with Progress and Buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        
        // Button Panel with border and grid layout
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton answerButton = new JButton("Submit Answer");
        skipButton = new JButton("Skip Question");
        finishButton = new JButton("Finish Exam");
        
        // Add buttons to button panel
        buttonPanel.add(answerButton);
        buttonPanel.add(skipButton);
        buttonPanel.add(finishButton);
        
        // Progress label with border
        progressLabel = new JLabel();
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        progressLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        // Add components to bottom panel
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(progressLabel, BorderLayout.SOUTH);
        
        // Add main panels to frame
        add(contentPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add button listeners
        answerButton.addActionListener(e -> handleAnswer());
        skipButton.addActionListener(e -> handleSkipQuestion());
        finishButton.addActionListener(e -> handleFinish());

        // Set size and position
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Add some padding around the entire window
        ((JPanel)getContentPane()).setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void loadQuestions() {
        try {
            ExamReader reader = new ExamReader("exams");
            File examDir = new File("exams");
            if (!examDir.exists() || !examDir.isDirectory()) {
                JOptionPane.showMessageDialog(this,
                    "Error: 'exams' directory not found.",
                    "Directory Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            File[] examFiles = examDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".yaml") || 
                name.toLowerCase().endsWith(".yml"));

            if (examFiles == null || examFiles.length == 0) {
                JOptionPane.showMessageDialog(this,
                    "No exam files found in the 'exams' directory.\n" +
                    "Please add YAML files to the 'exams' directory.",
                    "No Exams Found",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            // Sort exam files alphabetically
            Arrays.sort(examFiles, Comparator.comparing(File::getName));

            // Create a dialog to select the exam
            String[] examNames = Arrays.stream(examFiles)
                .map(file -> {
                    String name = file.getName().replaceFirst("[.][^.]+$", ""); // Remove extension
                    name = name.replace("_", " ").replace("-", " ");
                    // Capitalize first letter of each word
                    name = Arrays.stream(name.split("\\s+"))
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                        .collect(Collectors.joining(" "));
                    return name + " (" + file.getName() + ")";
                })
                .toArray(String[]::new);

            String selectedExam = (String) JOptionPane.showInputDialog(this,
                "Select an exam to take:",
                "Choose Exam",
                JOptionPane.QUESTION_MESSAGE,
                null,
                examNames,
                examNames[0]);

            if (selectedExam == null) {
                System.exit(0);
            }

            // Extract the filename from the selection
            String fileName = selectedExam.substring(selectedExam.indexOf("(") + 1, selectedExam.indexOf(")"));
            components = reader.loadQuestions(fileName);
            Collections.shuffle(components);
            results = new ArrayList<>();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading questions: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void displayCurrentQuestion() {
        if (currentQuestionIndex >= components.size()) {
            showResults();
            return;
        }

        ExamComponent component = components.get(currentQuestionIndex);
        Question question = (Question) component;
        questionLabel.setText("<html><body style='width: 400px'>" + 
            question.getQuestionText() + "</body></html>");

        choicesPanel.removeAll();
        choicesGroup = new ButtonGroup();
        fillInAnswerField = null;
        matchingComboBoxes = null;
        orderingLabels = null;
        upButtons = null;
        downButtons = null;
        multipleSelectCheckBoxes = null;

        String questionType = component.getComponentType();
        switch (questionType) {
            case "MultipleChoice":
                char choiceLetter = 'A';
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) component;
                List<String> choices = mcq.getAnswerChoices();
                if (mcq.getImagePath() != null) {
                    try {
                        ImageIcon imageIcon = new ImageIcon(mcq.getImagePath());
                        JLabel imageLabel = new JLabel(imageIcon);
                        choicesPanel.add(imageLabel);
                    } catch (Exception e) {
                        choicesPanel.add(new JLabel("Error loading image: " + mcq.getImagePath()));
                    }
                }
                if (mcq.getCodeSnippet() != null) {
                    JTextArea codeArea = new JTextArea(mcq.getCodeSnippet());
                    codeArea.setEditable(false);
                    codeArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                    choicesPanel.add(new JScrollPane(codeArea));
                }
                for (String choice : choices) {
                    JRadioButton radioButton = new JRadioButton(choiceLetter + ") " + choice);
                    radioButton.setActionCommand(String.valueOf(choiceLetter));
                    choicesGroup.add(radioButton);
                    choicesPanel.add(radioButton);
                    choiceLetter++;
                }
                break;

            case "TrueFalse":
                JRadioButton trueButton = new JRadioButton("A) True");
                trueButton.setActionCommand("A");
                JRadioButton falseButton = new JRadioButton("B) False");
                falseButton.setActionCommand("B");
                choicesGroup.add(trueButton);
                choicesGroup.add(falseButton);
                choicesPanel.add(trueButton);
                choicesPanel.add(falseButton);
                break;

            case "FillInTheBlank":
                fillInAnswerField = new JTextField(20);
                choicesPanel.add(new JLabel("Your answer:"));
                choicesPanel.add(fillInAnswerField);
                break;

            case "Matching":
                MatchingQuestion mq = (MatchingQuestion) component;
                matchingComboBoxes = new ArrayList<>();
                List<String> leftItems = mq.getLeftItems();
                List<String> rightItems = mq.getRightItems();
                for (int i = 0; i < leftItems.size(); i++) {
                    JPanel matchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    matchPanel.add(new JLabel((char)('A' + i) + ") " + leftItems.get(i)));
                    JComboBox<String> comboBox = new JComboBox<>(rightItems.toArray(new String[0]));
                    matchPanel.add(comboBox);
                    matchingComboBoxes.add(comboBox);
                    choicesPanel.add(matchPanel);
                }
                break;

            case "Ordering":
                OrderingQuestion oq = (OrderingQuestion) component;
                OrderedItemPanel orderPanel = new OrderedItemPanel(oq);
                choicesPanel.add(orderPanel);
                break;

            case "MultipleSelect":
                MultipleSelectQuestion msq = (MultipleSelectQuestion) component;
                multipleSelectCheckBoxes = new ArrayList<>();
                List<String> msChoices = msq.getAnswerChoices();
                for (int i = 0; i < msChoices.size(); i++) {
                    String choice = msChoices.get(i);
                    JCheckBox checkBox = new JCheckBox((char)('A' + i) + ") " + choice);
                    checkBox.setActionCommand(String.valueOf((char)('A' + i)));
                    multipleSelectCheckBoxes.add(checkBox);
                    choicesPanel.add(checkBox);
                }
                break;

            default:
                JOptionPane.showMessageDialog(this,
                    "Unsupported question type: " + questionType,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
        }

        progressLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + components.size());
        
        choicesPanel.revalidate();
        choicesPanel.repaint();
    }

    private void handleAnswer() {
        ExamComponent component = components.get(currentQuestionIndex);
        String questionType = component.getComponentType();
        String answer = null;
        boolean isCorrect = false;

        switch (questionType) {
            case "MultipleChoice":
            case "TrueFalse":
                ButtonModel selectedButton = choicesGroup.getSelection();
                if (selectedButton == null) {
                    JOptionPane.showMessageDialog(this,
                        "Please select an answer or click Skip.",
                        "No Answer Selected",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                answer = selectedButton.getActionCommand();
                isCorrect = answer.equals(component.getCorrectAnswer());
                break;

            case "FillInTheBlank":
                if (fillInAnswerField == null || fillInAnswerField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter an answer or click Skip.",
                        "No Answer Entered",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                answer = fillInAnswerField.getText().trim();
                isCorrect = answer.equalsIgnoreCase(component.getCorrectAnswer());
                break;

            case "Matching":
                MatchingQuestion mq = (MatchingQuestion) component;
                Map<String, String> correctMatches = mq.getCorrectMatches();
                List<String> leftItems = mq.getLeftItems();
                boolean allCorrect = true;
                StringBuilder userAnswer = new StringBuilder();
                for (int i = 0; i < leftItems.size(); i++) {
                    String leftItem = leftItems.get(i);
                    String selectedRight = (String) matchingComboBoxes.get(i).getSelectedItem();
                    userAnswer.append(leftItem).append(" -> ").append(selectedRight).append("; ");
                    if (!selectedRight.equals(correctMatches.get(leftItem))) {
                        allCorrect = false;
                    }
                }
                answer = userAnswer.toString();
                isCorrect = allCorrect;
                break;

            case "Ordering":
                OrderingQuestion oq = (OrderingQuestion) component;
                // Find the OrderedItemPanel in the choicesPanel
                OrderedItemPanel orderPanel = null;
                for (Component c : choicesPanel.getComponents()) {
                    if (c instanceof OrderedItemPanel) {
                        orderPanel = (OrderedItemPanel) c;
                        break;
                    }
                }
                if (orderPanel != null) {
                    List<String> userOrder = orderPanel.getItems();
                    answer = userOrder.toString();
                    isCorrect = userOrder.equals(oq.getCorrectOrder());
                }
                break;

            case "MultipleSelect":
                MultipleSelectQuestion msq = (MultipleSelectQuestion) component;
                List<String> userAnswers = new ArrayList<>();
                for (JCheckBox checkBox : multipleSelectCheckBoxes) {
                    if (checkBox.isSelected()) {
                        userAnswers.add(checkBox.getActionCommand());
                    }
                }
                if (userAnswers.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Please select at least one answer or click Skip.",
                        "No Answer Selected",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                answer = userAnswers.toString();
                isCorrect = userAnswers.size() == msq.getCorrectAnswers().size() &&
                            userAnswers.containsAll(msq.getCorrectAnswers());
                break;

            default:
                JOptionPane.showMessageDialog(this,
                    "Unsupported question type: " + questionType,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (isCorrect) {
            score++; // Increment score for correct answers
        }

        showExplanationDialog(component, answer, isCorrect);

        results.add(new ExamResult(component, answer, isCorrect));
        currentQuestionIndex++;
        displayCurrentQuestion();
    }

    private void handleSkipQuestion() {
        ExamComponent component = components.get(currentQuestionIndex);
        results.add(new ExamResult(component, null, false));
        showExplanationDialog(component, null, false);
        currentQuestionIndex++;
        displayCurrentQuestion();
    }

    private void handleFinish() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to finish the exam?",
            "Finish Exam",
            JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            showResults();
        }
    }

    private void showResults() {
        int attempted = (int) results.stream().filter(r -> r.getAnswer() != null).count();
        int skipped = (int) results.stream().filter(r -> r.getAnswer() == null).count();
        double percentage = (attempted == 0) ? 0.0 : (score * 100.0) / attempted;

        JTextPane resultPane = new JTextPane();
        resultPane.setEditorKit(new HTMLEditorKit());
        resultPane.setContentType("text/html");
        resultPane.setEditable(false);

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial; font-size: 14px; padding: 20px; background-color: #F9F9F9;'>");
        
        // Header with score percentage
        html.append("<h1 style='color: #2E86C1; text-align: center;'>Exam Results</h1>");
        html.append("<h2 style='color: #2E86C1; text-align: center;'>Final Score: ")
            .append(String.format("%.1f%%", percentage))
            .append("</h2>");

        // Statistics in a styled box
        html.append("<div style='background-color: #FFF; padding: 20px; border-radius: 5px; border: 1px solid #DDD; margin: 20px 0;'>");
        html.append("<h3 style='color: #2E86C1; margin-top: 0;'>Statistics</h3>");
        html.append("<table style='width: 100%; border-collapse: collapse;'>");
        html.append("<tr><td style='padding: 8px;'><b>Total Questions:</b></td><td>").append(components.size()).append("</td></tr>");
        html.append("<tr><td style='padding: 8px;'><b>Questions Attempted:</b></td><td>").append(attempted).append("</td></tr>");
        html.append("<tr><td style='padding: 8px;'><b>Correct Answers:</b></td><td>").append(score).append("</td></tr>");
        html.append("<tr><td style='padding: 8px;'><b>Incorrect Answers:</b></td><td>").append(attempted - score).append("</td></tr>");
        html.append("<tr><td style='padding: 8px;'><b>Skipped Questions:</b></td><td>").append(skipped).append("</td></tr>");
        html.append("</table>");
        html.append("</div>");

        html.append("</body></html>");
        resultPane.setText(html.toString());

        // Create dialog with A4 dimensions
        JDialog dialog = new JDialog(this, "Exam Results", true);
        dialog.setLayout(new BorderLayout());
        
        JScrollPane scrollPane = new JScrollPane(resultPane);
        scrollPane.setPreferredSize(new Dimension(794, 1122)); // A4 size
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton reviewButton = new JButton("Review Incorrect Answers");
        JButton newExamButton = new JButton("Take New Exam");
        JButton exitButton = new JButton("Exit");

        reviewButton.addActionListener(e -> {
            dialog.dispose();
            showIncorrectAnswers();
        });
        newExamButton.addActionListener(e -> {
            dialog.dispose();
            resetExam();
        });
        exitButton.addActionListener(e -> {
            dialog.dispose();
            dispose();
            System.exit(0);
        });

        buttonPanel.add(reviewButton);
        buttonPanel.add(newExamButton);
        buttonPanel.add(exitButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showIncorrectAnswers() {
        List<ExamResult> incorrectAnswers = results.stream()
            .filter(r -> r.getAnswer() != null && !r.isCorrect())
            .toList();

        if (incorrectAnswers.isEmpty()) {
            askForNewExam();
            return;
        }

        JTextPane reviewPane = new JTextPane();
        reviewPane.setEditorKit(new HTMLEditorKit());
        reviewPane.setContentType("text/html");
        reviewPane.setEditable(false);

        // Create dialog with A4 dimensions
        JDialog dialog = new JDialog(this, "Review Incorrect Answers", true);
        dialog.setLayout(new BorderLayout());
        
        // Track current answer index
        final int[] currentIndex = {0};
        
        // Function to update content
        Runnable updateContent = () -> {
            ExamResult result = incorrectAnswers.get(currentIndex[0]);
            ExamComponent component = result.getComponent();
            Question question = (Question) component;
            
            StringBuilder html = new StringBuilder();
            html.append("<html><body style='font-family: Arial; font-size: 14px; padding: 20px; background-color: #F9F9F9;'>");
            html.append("<h1 style='color: #2E86C1; text-align: center;'>Review Incorrect Answers</h1>");
            html.append("<h3 style='color: #666; text-align: center;'>Question ").append(currentIndex[0] + 1)
                .append(" of ").append(incorrectAnswers.size()).append("</h3>");

            html.append("<div style='background-color: #FFF; padding: 20px; border-radius: 5px; border: 1px solid #DDD; margin: 20px 0;'>");
            html.append("<h3 style='color: #2E86C1; margin-top: 0;'>Question</h3>");
            html.append("<p>").append(question.getQuestionText()).append("</p>");
            
            html.append("<div style='margin: 15px 0; padding: 15px; background-color: #F8F9FA; border-radius: 5px;'>");
            if (component instanceof MultipleChoiceQuestion mcq) {
                List<String> choices = mcq.getAnswerChoices();
                String userAnswer = result.getAnswer();
                String correctAnswer = component.getCorrectAnswer();
                
                int userIndex = userAnswer.charAt(0) - 'A';
                int correctIndex = correctAnswer.charAt(0) - 'A';
                
                String userAnswerText = choices.get(userIndex);
                String correctAnswerText = choices.get(correctIndex);
                
                html.append("<p><b>Your Answer:</b> ").append(userAnswer).append(" - ").append(userAnswerText).append("</p>");
                html.append("<p><b>Correct Answer:</b> ").append(correctAnswer).append(" - ").append(correctAnswerText).append("</p>");
            } else {
                html.append("<p><b>Your Answer:</b> ").append(result.getAnswer()).append("</p>");
                html.append("<p><b>Correct Answer:</b> ").append(component.getCorrectAnswer()).append("</p>");
            }
            html.append("</div>");

            // Add explanation if available
            String explanation = component.getExplanation();
            if (explanation != null && !explanation.trim().isEmpty()) {
                html.append("<h4 style='color: #2E86C1;'>Explanation</h4>");
                html.append("<p>").append(explanation).append("</p>");
            }
            
            html.append("</div>");
            html.append("</body></html>");
            reviewPane.setText(html.toString());
            reviewPane.setCaretPosition(0); // Scroll to top
        };

        JScrollPane scrollPane = new JScrollPane(reviewPane);
        scrollPane.setPreferredSize(new Dimension(794, 1122)); // A4 size
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Navigation and control buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton prevButton = new JButton("← Previous");
        JButton nextButton = new JButton("Next →");
        JButton finishButton = new JButton("Finish Review");

        // Previous button action
        prevButton.addActionListener(e -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                updateContent.run();
                nextButton.setEnabled(true);
                prevButton.setEnabled(currentIndex[0] > 0);
            }
        });

        // Next button action
        nextButton.addActionListener(e -> {
            if (currentIndex[0] < incorrectAnswers.size() - 1) {
                currentIndex[0]++;
                updateContent.run();
                prevButton.setEnabled(true);
                nextButton.setEnabled(currentIndex[0] < incorrectAnswers.size() - 1);
            }
        });

        // Finish button action
        finishButton.addActionListener(e -> {
            dialog.dispose();
            askForNewExam();
        });

        // Initial button states
        prevButton.setEnabled(false);
        nextButton.setEnabled(incorrectAnswers.size() > 1);

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(finishButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Show first answer
        updateContent.run();

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void askForNewExam() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Would you like to take another exam?",
            "New Exam",
            JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            resetExam();
        } else {
            dispose();
            System.exit(0);
        }
    }

    private void resetExam() {
        currentQuestionIndex = 0;
        score = 0;
        results.clear();
        loadQuestions(); // Reload questions instead of just shuffling
        displayCurrentQuestion();
    }

    @Override
    public void display() {
        setVisible(true);
        displayCurrentQuestion();
    }

    @Override
    public String getCorrectAnswer() {
        return null; // ExamAppSwing doesn't have a single correct answer
    }

    @Override
    public String getComponentType() {
        return "ExamApp";
    }

    @Override
    public String getExplanation() {
        return "<p><b>ExamAppSwing</b> is the main application class for the Java Exam Application.</p>" +
               "<p>It implements the <b>ExamComponent</b> interface to demonstrate how interfaces enable " +
               "abstraction and polymorphism. The application loads questions from YAML files, displays them " +
               "in a Swing GUI, and provides explanations for each answer.</p>";
    }

    private static class ExamResult {
        private final ExamComponent component;
        private final String answer;
        private final boolean correct;

        public ExamResult(ExamComponent component, String answer, boolean correct) {
            this.component = component;
            this.answer = answer;
            this.correct = correct;
        }

        public ExamComponent getComponent() { return component; }
        public String getAnswer() { return answer; }
        public boolean isCorrect() { return correct; }
    }

    private class OrderedItemPanel extends JPanel {
        private final List<String> items;
        private final List<JPanel> itemPanels;
        private final OrderingQuestion question;
        
        public OrderedItemPanel(OrderingQuestion question) {
            this.question = question;
            this.items = new ArrayList<>(question.getItems());
            this.itemPanels = new ArrayList<>();
            
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            createItemPanels();
        }
        
        private void createItemPanels() {
            removeAll();
            itemPanels.clear();
            
            for (int i = 0; i < items.size(); i++) {
                JPanel panel = createItemPanel(i);
                itemPanels.add(panel);
                add(panel);
            }
            
            revalidate();
            repaint();
        }
        
        private JPanel createItemPanel(int index) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel((index + 1) + ") " + items.get(index));
            
            JButton upButton = new JButton("↑");
            JButton downButton = new JButton("↓");
            
            upButton.setEnabled(index > 0);
            downButton.setEnabled(index < items.size() - 1);
            
            upButton.addActionListener(e -> moveItem(index, index - 1));
            downButton.addActionListener(e -> moveItem(index, index + 1));
            
            panel.add(label);
            panel.add(upButton);
            panel.add(downButton);
            
            return panel;
        }
        
        private void moveItem(int fromIndex, int toIndex) {
            if (toIndex < 0 || toIndex >= items.size()) {
                return;
            }
            
            // Update the data
            String movedItem = items.remove(fromIndex);
            items.add(toIndex, movedItem);
            
            // Rebuild the UI
            createItemPanels();
        }
        
        public List<String> getItems() {
            return new ArrayList<>(items);
        }
    }

    private void showExplanationDialog(ExamComponent component, String userAnswer, boolean isCorrect) {
        JTextPane explanationPane = new JTextPane();
        explanationPane.setEditorKit(new HTMLEditorKit());
        explanationPane.setContentType("text/html");
        explanationPane.setEditable(false);

        StringBuilder explanationHtml = new StringBuilder();
        explanationHtml.append("<html><body style='font-family: Arial; font-size: 14px; padding: 20px; background-color: #F9F9F9;'>");
        explanationHtml.append("<h1 style='color: ").append(isCorrect ? "green" : "red").append(";'>")
            .append(isCorrect ? "Correct!" : "Incorrect").append("</h1>");

        explanationHtml.append("<div style='margin: 20px 0;'>");
        explanationHtml.append("<p><b>Your Answer:</b> ").append(userAnswer != null ? userAnswer : "Skipped").append("</p>");
        explanationHtml.append("<p><b>Correct Answer:</b> ").append(component.getCorrectAnswer()).append("</p>");
        explanationHtml.append("</div>");

        explanationHtml.append("<div style='background-color: #FFF; padding: 20px; border-radius: 5px; border: 1px solid #DDD; margin-top: 20px;'>");
        explanationHtml.append("<h2 style='color: #2E86C1; margin-top: 0;'>Explanation</h2>");
        String explanationText = component.getExplanation();
        if (explanationText == null || explanationText.trim().isEmpty()) {
            explanationHtml.append("<p>No explanation available.</p>");
        } else {
            explanationHtml.append(explanationText);
        }
        explanationHtml.append("</div>");

        explanationHtml.append("</body></html>");

        explanationPane.setText(explanationHtml.toString());

        // Create a scrollPane with A4 paper dimensions (approximately)
        // A4 is roughly 8.27 × 11.69 inches, converting to pixels at 96 DPI
        JScrollPane scrollPane = new JScrollPane(explanationPane);
        scrollPane.setPreferredSize(new Dimension(794, 1122)); // A4 size in pixels
        
        // Create a custom dialog for better size control
        JDialog dialog = new JDialog(this, "Question Explanation", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        // Add a close button at the bottom
        JButton closeButton = new JButton("Continue");
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set dialog size and position
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ExamAppSwing();
        });
    }
}