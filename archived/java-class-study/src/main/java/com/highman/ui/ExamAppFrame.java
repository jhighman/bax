package com.highman.ui;

import com.highman.exams.ExamComponent;
import com.highman.exams.MultipleSelectQuestion;
import com.highman.exams.MultipleChoiceQuestion;
import com.highman.exams.FillInTheBlankQuestion;
import com.highman.exams.MatchingQuestion;
import com.highman.exams.TrueFalseQuestion;
import com.highman.exams.Question;
import com.highman.exams.TracingQuestion;
import com.highman.model.ExamResult;
import com.highman.logic.ExamState;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * The main application window for the exam application.
 */
public class ExamAppFrame extends JFrame {
    private final QuestionDisplay questionDisplay;
    private final ResultDisplay resultDisplay;
    private final ExamState examState;
    private final JLabel progressLabel;
    private final JButton skipButton;
    private final JButton finishButton;

    /**
     * Creates a new ExamAppFrame.
     */
    public ExamAppFrame() {
        super("Java Exam Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        examState = new ExamState();
        questionDisplay = new QuestionDisplay(answer -> {
            // This callback will be called whenever the answer changes
            System.out.println("Answer updated: " + answer);
        });
        resultDisplay = new ResultDisplay(this, examState);

        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(5, 5));
        contentPanel.add(questionDisplay, BorderLayout.CENTER);

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

    /**
     * Sets the exam components (questions).
     * @param components The list of exam components
     */
    public void setComponents(List<ExamComponent> components) {
        examState.setComponents(components);
        displayCurrentQuestion();
    }

    /**
     * Displays the current question.
     */
    private void displayCurrentQuestion() {
        if (examState.isComplete()) {
            showResults();
            return;
        }

        ExamComponent component = examState.getCurrentQuestion();
        questionDisplay.displayQuestion(component);
        progressLabel.setText("Question " + (examState.getCurrentQuestionIndex() + 1) + 
            " of " + examState.getComponents().size());
    }

    /**
     * Handles the submission of an answer.
     */
    public void handleAnswer() {
        String answer = questionDisplay.getAnswer();
        
        // Check if any answer is provided
        if (answer == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an answer or click Skip.",
                "No Answer Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        ExamComponent component = examState.getCurrentQuestion();
        boolean isCorrect;
        
        // Add debug logging
        System.out.println("Debug - Component type: " + component.getComponentType());
        System.out.println("Debug - Raw user answer: [" + answer + "]");
        System.out.println("Debug - Raw correct answer: [" + component.getCorrectAnswer() + "]");

        if (component instanceof TrueFalseQuestion) {
            TrueFalseQuestion tfq = (TrueFalseQuestion) component;
            isCorrect = tfq.isCorrect(answer);
        } else if (component instanceof MultipleSelectQuestion) {
            MultipleSelectQuestion msq = (MultipleSelectQuestion) component;
            List<String> options = msq.getOptions();
            
            // Parse user answer string "[answer1, answer2]" into list
            List<String> userAnswers = new ArrayList<>();
            String userAnswerStr = answer.substring(1, answer.length() - 1);
            if (!userAnswerStr.isEmpty()) {
                for (String s : userAnswerStr.split(", ")) {
                    // Remove quotes if present
                    s = s.trim();
                    if (s.startsWith("\"") && s.endsWith("\"")) {
                        s = s.substring(1, s.length() - 1);
                    }
                    userAnswers.add(s);
                }
            }
            
            // Get correct answers and convert to actual text if they're letters
            List<String> correctAnswers = msq.getCorrectAnswers();
            List<String> standardizedCorrectAnswers = new ArrayList<>();
            for (String correctAnswer : correctAnswers) {
                if (correctAnswer.length() == 1 && correctAnswer.charAt(0) >= 'A' && correctAnswer.charAt(0) <= 'Z') {
                    int index = correctAnswer.charAt(0) - 'A';
                    if (index >= 0 && index < options.size()) {
                        standardizedCorrectAnswers.add(options.get(index));
                    }
                } else {
                    standardizedCorrectAnswers.add(correctAnswer);
                }
            }
            
            // Compare the lists using sets to ignore order
            isCorrect = userAnswers.size() == standardizedCorrectAnswers.size() && 
                       new HashSet<>(userAnswers).equals(new HashSet<>(standardizedCorrectAnswers));
            
        } else if (component instanceof FillInTheBlankQuestion) {
            FillInTheBlankQuestion fibq = (FillInTheBlankQuestion) component;
            // Add logging to debug the answer comparison
            System.out.println("Fill in the blank - User answer: [" + answer + "]");
            System.out.println("Fill in the blank - Correct answer: [" + fibq.getCorrectAnswer() + "]");
            isCorrect = fibq.isCorrectAnswer(answer);
            System.out.println("Fill in the blank - Is correct: " + isCorrect);
        } else if (component instanceof MatchingQuestion) {
            Map<String, String> userMatches = parseMatchingAnswer(answer);
            String correctAnswer = component.getCorrectAnswer().trim();
            
            // Parse correct answer into a map
            Map<String, String> correctMatches = new LinkedHashMap<>();
            for (String pair : correctAnswer.split(";")) {
                String[] parts = pair.split("=", 2);
                if (parts.length == 2) {
                    correctMatches.put(parts[0].trim(), parts[1].trim());
                }
            }
            
            // Convert both maps to sorted lists of key-value pairs for comparison
            List<String> userPairs = new ArrayList<>();
            List<String> correctPairs = new ArrayList<>();
            
            for (Map.Entry<String, String> entry : userMatches.entrySet()) {
                userPairs.add(entry.getKey() + "=" + entry.getValue());
            }
            
            for (Map.Entry<String, String> entry : correctMatches.entrySet()) {
                correctPairs.add(entry.getKey() + "=" + entry.getValue());
            }
            
            // Sort both lists
            Collections.sort(userPairs);
            Collections.sort(correctPairs);
            
            // Compare the sorted lists
            isCorrect = userPairs.equals(correctPairs);
            
        } else if (component instanceof TracingQuestion) {
            TracingQuestion tq = (TracingQuestion) component;
            
            // Split user's answer into lines
            List<String> userAnswers = Arrays.asList(answer.split("\n"));
            
            // Debug logging
            System.out.println("Debug - Tracing Question - User answers:");
            for (int i = 0; i < userAnswers.size(); i++) {
                System.out.println("  " + i + ": [" + userAnswers.get(i) + "]");
            }
            System.out.println("Debug - Tracing Question - Expected answers:");
            for (TracingQuestion.TracePoint tp : tq.getTracePoints()) {
                System.out.println("  " + tp.getLabel() + ": [" + tp.getCorrectAnswer() + "]");
            }
            
            isCorrect = tq.isCorrect(userAnswers);
            
        } else {
            // For single-choice questions (MultipleChoice, TrueFalse)
            String correctAnswer = component.getCorrectAnswer();
            
            // Convert letter answer to actual option text for multiple choice questions
            if (component instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) component;
                List<String> options = mcq.getAnswerChoices();
                // If answer is a single letter A-Z, convert it to the corresponding option
                if (answer.length() == 1 && answer.charAt(0) >= 'A' && answer.charAt(0) <= 'Z') {
                    int index = answer.charAt(0) - 'A';
                    if (index >= 0 && index < options.size()) {
                        answer = options.get(index);
                    }
                }
            }
            
            // Trim both answers to handle any whitespace issues
            answer = answer.trim();
            correctAnswer = correctAnswer.trim();
            isCorrect = answer.equals(correctAnswer);
        }
        
        examState.recordAnswer(answer, isCorrect);
        resultDisplay.showExplanation(component, answer, isCorrect);
        displayCurrentQuestion();
    }

    /**
     * Handles skipping a question.
     */
    private void handleSkipQuestion() {
        examState.skipQuestion();
        displayCurrentQuestion();
    }

    /**
     * Handles finishing the exam.
     */
    private void handleFinish() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to finish the exam?",
            "Finish Exam",
            JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            showResults();
        }
    }

    /**
     * Shows the exam results.
     */
    private void showResults() {
        resultDisplay.showResults(examState.getResults(), examState.getComponents().size());
    }

    /**
     * Gets the current question.
     * @return The current question
     */
    public ExamComponent getCurrentQuestion() {
        return examState.getCurrentQuestion();
    }

    /**
     * Parses a string representation of a matching answer into a Map<String, String>.
     */
    private Map<String, String> parseMatchingAnswer(String matchStr) {
        if (matchStr == null) return null;
        Map<String, String> result = new LinkedHashMap<>();
        
        try {
            // Remove outer curly braces and parse JSON format
            String content = matchStr.substring(1, matchStr.length() - 1).trim();
            if (content.isEmpty()) return result;

            // Keep track of current key and value
            StringBuilder currentPart = new StringBuilder();
            String currentKey = null;
            boolean inQuotes = false;
            boolean afterColon = false;

            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                
                if (c == '"') {
                    inQuotes = !inQuotes;
                    continue;
                }
                
                if (!inQuotes) {
                    if (c == ':' && currentKey == null) {
                        currentKey = currentPart.toString().trim();
                        if (currentKey.startsWith("\"") && currentKey.endsWith("\"")) {
                            currentKey = currentKey.substring(1, currentKey.length() - 1);
                        }
                        currentPart.setLength(0);
                        afterColon = true;
                        continue;
                    }
                    
                    if (c == ',' && currentKey != null) {
                        String value = currentPart.toString().trim();
                        if (value.startsWith("\"") && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 1);
                        }
                        result.put(currentKey, value);
                        currentKey = null;
                        currentPart.setLength(0);
                        afterColon = false;
                        continue;
                    }
                }
                
                if (!afterColon || c != ' ' || currentPart.length() > 0) {
                    currentPart.append(c);
                }
            }
            
            // Handle the last pair
            if (currentKey != null && currentPart.length() > 0) {
                String value = currentPart.toString().trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                result.put(currentKey, value);
            }
        } catch (Exception e) {
            System.err.println("Error parsing matching answer: " + e.getMessage());
            return null;
        }
        
        return result;
    }

    /**
     * Converts a map of matches to the standardized string format (key=value;key=value;...)
     */
    private String convertMatchesToString(Map<String, String> matches) {
        if (matches == null || matches.isEmpty()) return "";
        List<String> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : matches.entrySet()) {
            pairs.add(entry.getKey() + "=" + entry.getValue());
        }
        Collections.sort(pairs); // Sort pairs for consistent ordering
        return String.join(";", pairs);
    }
} 