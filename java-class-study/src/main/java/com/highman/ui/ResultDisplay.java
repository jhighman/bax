package com.highman.ui;

import com.highman.exams.*;
import com.highman.logic.ExamState;
import com.highman.model.ExamResult;
import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Displays exam results and explanations.
 */
public class ResultDisplay {
    private final JFrame parent;
    private final ExamState examState;

    /**
     * Creates a new ResultDisplay.
     * @param parent The parent frame
     * @param examState The exam state
     */
    public ResultDisplay(JFrame parent, ExamState examState) {
        this.parent = parent;
        this.examState = examState;
    }

    /**
     * Shows the exam results.
     * @param results The list of exam results
     * @param totalQuestions The total number of questions
     */
    public void showResults(List<ExamResult> results, int totalQuestions) {
        int attempted = (int) results.stream().filter(r -> r.getAnswer() != null).count();
        int skipped = (int) results.stream().filter(r -> r.getAnswer() == null).count();
        int correct = (int) results.stream().filter(ExamResult::isCorrect).count();
        double percentage = (attempted == 0) ? 0.0 : (correct * 100.0) / attempted;

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
        html.append("<tr><td style='padding: 8px;'><b>Total Questions:</b></td><td>").append(totalQuestions).append("</td></tr>");
        html.append("<tr><td style='padding: 8px;'><b>Questions Attempted:</b></td><td>").append(attempted).append("</td></tr>");
        html.append("<tr><td style='padding: 8px;'><b>Correct Answers:</b></td><td>").append(correct).append("</td></tr>");
        html.append("<tr><td style='padding: 8px;'><b>Incorrect Answers:</b></td><td>").append(attempted - correct).append("</td></tr>");
        html.append("<tr><td style='padding: 8px;'><b>Skipped Questions:</b></td><td>").append(skipped).append("</td></tr>");
        html.append("</table>");
        html.append("</div>");

        html.append("</body></html>");
        resultPane.setText(html.toString());

        showDialog("Exam Results", resultPane, true);
    }

    /**
     * Shows the explanation for a question.
     * @param component The exam component
     * @param userAnswer The user's answer
     * @param isCorrect Whether the answer was correct
     */
    public void showExplanation(ExamComponent component, String userAnswer, boolean isCorrect) {
        JTextPane explanationPane = new JTextPane();
        explanationPane.setContentType("text/html");
        
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial, sans-serif; padding: 20px; background-color: #F9F9F9;'>");
        
        // Header with result
        html.append("<div style='text-align: center; margin-bottom: 20px;'>");
        if (isCorrect) {
            html.append("<h2 style='color: #28a745;'>✓ Correct!</h2>");
        } else {
            html.append("<h2 style='color: #dc3545;'>✗ Incorrect</h2>");
        }
        html.append("</div>");

        // Question section
        html.append("<div style='background-color: #FFF; padding: 20px; border-radius: 5px; border: 1px solid #DDD; margin-bottom: 20px;'>");
        html.append("<h3 style='color: #2E86C1; margin-top: 0;'>Question</h3>");
        html.append("<p>").append(((Question) component).getQuestionText()).append("</p>");

        // Code block section for code-related questions
        if (component instanceof CodeQuestion || 
            component instanceof CodeCompletionQuestion || 
            component instanceof TracingQuestion || 
            component instanceof AnalysisQuestion) {
            
            String codeBlock = null;
            if (component instanceof CodeQuestion) {
                codeBlock = ((CodeQuestion) component).getCodeBlock();
            } else if (component instanceof CodeCompletionQuestion) {
                codeBlock = ((CodeCompletionQuestion) component).getCodeBlock();
            } else if (component instanceof TracingQuestion) {
                codeBlock = ((TracingQuestion) component).getCodeBlock();
            } else if (component instanceof AnalysisQuestion) {
                codeBlock = ((AnalysisQuestion) component).getCodeBlock();
            }
            
            if (codeBlock != null && !codeBlock.isEmpty()) {
                html.append("<div style='margin: 15px 0;'>");
                html.append("<h4 style='color: #2E86C1; margin-bottom: 10px;'>Code</h4>");
                html.append("<pre style='background-color: #f8f9fa; padding: 15px; border-radius: 5px; border: 1px solid #e9ecef; font-family: monospace; white-space: pre-wrap;'>");
                html.append(escapeHtml(codeBlock));
                html.append("</pre>");
                html.append("</div>");
            }
        }
        html.append("</div>");

        // Answer section
        html.append("<div style='background-color: #FFF; padding: 20px; border-radius: 5px; border: 1px solid #DDD; margin-bottom: 20px;'>");
        html.append("<h3 style='color: #2E86C1; margin-top: 0;'>Answers</h3>");
        
        // Your answer
        html.append("<div style='margin-bottom: 10px;'>");
        html.append("<p><strong>Your Answer:</strong> ");
        if (userAnswer != null) {
            html.append("<span style='background-color: ").append(isCorrect ? "#e8f5e9" : "#ffebee").append("; padding: 2px 5px; border-radius: 3px;'>");
            html.append("<code>").append(escapeHtml(userAnswer)).append("</code>");
            html.append("</span>");
        } else {
            html.append("<span style='color: #666; font-style: italic;'>Skipped</span>");
        }
        html.append("</p>");
        html.append("</div>");

        // Correct answer
        html.append("<div style='margin-bottom: 10px;'>");
        html.append("<p><strong>Correct Answer:</strong> ");
        html.append("<span style='background-color: #e8f5e9; padding: 2px 5px; border-radius: 3px;'>");
        html.append("<code>").append(escapeHtml(component.getCorrectAnswer())).append("</code>");
        html.append("</span></p>");
        html.append("</div>");
        html.append("</div>");

        // Explanation section
        String explanation = component.getExplanation();
        if (explanation != null && !explanation.trim().isEmpty()) {
            html.append("<div style='background-color: #FFF; padding: 20px; border-radius: 5px; border: 1px solid #DDD;'>");
            html.append("<h3 style='color: #2E86C1; margin-top: 0;'>Explanation</h3>");
            html.append(explanation); // Don't escape HTML in explanation as it may contain formatted content
            html.append("</div>");
        }

        html.append("</body></html>");
        explanationPane.setText(html.toString());
        
        // Create dialog with override button
        JDialog dialog = new JDialog(parent, "Question Explanation", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Set content properties
        explanationPane.setEditable(false);
        
        // Create a panel with proper layout and borders
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Configure scroll pane with proper size constraints
        JScrollPane scrollPane = new JScrollPane(explanationPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // Set minimum and preferred sizes
        Dimension preferredSize = new Dimension(800, 600);
        Dimension minimumSize = new Dimension(400, 300);
        
        scrollPane.setPreferredSize(preferredSize);
        scrollPane.setMinimumSize(minimumSize);
        explanationPane.setPreferredSize(preferredSize);
        explanationPane.setMinimumSize(minimumSize);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Create button panel with override option
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Add override button if the answer is incorrect
        if (!isCorrect && userAnswer != null) {
            JButton overrideButton = new JButton("Override as Correct");
            overrideButton.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(dialog,
                    "Are you sure you want to mark your answer as correct?\n" +
                    "This will update your score and record this answer as correct.",
                    "Confirm Override",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                
                if (choice == JOptionPane.YES_OPTION) {
                    examState.updateLastResult(true);
                    dialog.dispose();
                }
            });
            buttonPanel.add(overrideButton);
        }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setContentPane(contentPanel);
        
        // Pack and show dialog
        dialog.pack();
        
        // Ensure the dialog fits on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(dialog.getWidth(), screenSize.width - 100);
        int height = Math.min(dialog.getHeight(), screenSize.height - 100);
        
        // Ensure minimum dimensions
        width = Math.max(width, minimumSize.width);
        height = Math.max(height, minimumSize.height);
        
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Escapes HTML special characters in a string.
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;")
                  .replace("\n", "<br>");
    }

    /**
     * Parses a string representation of an ordered list into a List<String>.
     */
    private List<String> parseOrderList(String orderStr) {
        if (orderStr == null) return null;
        List<String> result = new ArrayList<>();
        // Remove brackets and split by comma
        String[] items = orderStr.substring(1, orderStr.length() - 1).split(",");
        for (String item : items) {
            result.add(item.trim());
        }
        return result;
    }

    /**
     * Parses a string representation of a matching answer into a Map<String, String>.
     */
    private Map<String, String> parseMatchingAnswer(String matchStr) {
        if (matchStr == null) return null;
        Map<String, String> result = new LinkedHashMap<>();
        
        try {
            // Remove outer curly braces
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
     * Shows incorrect answers for review.
     * @param incorrectAnswers The list of incorrect answers
     */
    public void showIncorrectAnswers(List<ExamResult> incorrectAnswers) {
        if (incorrectAnswers.isEmpty()) {
            return;
        }

        JTextPane reviewPane = new JTextPane();
        reviewPane.setEditorKit(new HTMLEditorKit());
        reviewPane.setContentType("text/html");
        reviewPane.setEditable(false);

        // Track current answer index
        final int[] currentIndex = {0};
        
        // Function to update content
        Runnable updateContent = () -> {
            ExamResult result = incorrectAnswers.get(currentIndex[0]);
            ExamComponent component = result.getComponent();
            
            StringBuilder html = new StringBuilder();
            html.append("<html><body style='font-family: Arial; font-size: 14px; padding: 20px; background-color: #F9F9F9;'>");
            html.append("<h1 style='color: #2E86C1; text-align: center;'>Review Incorrect Answers</h1>");
            html.append("<h3 style='color: #666; text-align: center;'>Question ").append(currentIndex[0] + 1)
                .append(" of ").append(incorrectAnswers.size()).append("</h3>");

            html.append("<div style='background-color: #FFF; padding: 20px; border-radius: 5px; border: 1px solid #DDD; margin: 20px 0;'>");
            html.append("<h3 style='color: #2E86C1; margin-top: 0;'>Question</h3>");
            html.append("<p>").append(((Question) component).getQuestionText()).append("</p>");
            
            html.append("<div style='margin: 15px 0; padding: 15px; background-color: #F8F9FA; border-radius: 5px;'>");
            html.append("<p><b>Your Answer:</b> ").append(result.getAnswer()).append("</p>");
            html.append("<p><b>Correct Answer:</b> ").append(component.getCorrectAnswer()).append("</p>");
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

        JDialog dialog = new JDialog(parent, "Review Incorrect Answers", true);
        dialog.setLayout(new BorderLayout());
        
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
        finishButton.addActionListener(e -> dialog.dispose());

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
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * Shows a dialog with the given content.
     * @param title The dialog title
     * @param content The content pane
     * @param showButtons Whether to show navigation buttons
     */
    private void showDialog(String title, JTextPane content, boolean showButtons) {
        JDialog dialog = new JDialog(parent, title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Set content properties
        content.setEditable(false);
        
        // Create a panel with proper layout and borders
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Configure scroll pane with proper size constraints
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // Set minimum and preferred sizes
        Dimension preferredSize = new Dimension(800, 600);
        Dimension minimumSize = new Dimension(400, 300);
        
        scrollPane.setPreferredSize(preferredSize);
        scrollPane.setMinimumSize(minimumSize);
        content.setPreferredSize(preferredSize);
        content.setMinimumSize(minimumSize);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        if (showButtons) {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dialog.dispose());
            buttonPanel.add(closeButton);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        dialog.setContentPane(contentPanel);
        
        // Pack the dialog to calculate initial size
        dialog.pack();
        
        // Ensure the dialog fits on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(dialog.getWidth(), screenSize.width - 100);
        int height = Math.min(dialog.getHeight(), screenSize.height - 100);
        
        // Ensure minimum dimensions
        width = Math.max(width, minimumSize.width);
        height = Math.max(height, minimumSize.height);
        
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private void showExplanation(Question question, String userAnswer) {
        StringBuilder content = new StringBuilder();
        content.append("<html><body style='width: 400px; font-family: Arial, sans-serif;'>");
        content.append("<h2>Question Explanation</h2>");
        content.append("<p><b>Question:</b> ").append(question.getQuestionText()).append("</p>");

        if (question instanceof CodeCompletionQuestion) {
            CodeCompletionQuestion ccq = (CodeCompletionQuestion) question;
            content.append("<p><b>Code Template:</b></p>");
            content.append("<pre style='background-color: #f5f5f5; padding: 10px; border-radius: 5px;'>");
            content.append(ccq.getCodeBlock());
            content.append("</pre>");
            
            if (ccq.getImagePath() != null && !ccq.getImagePath().isEmpty()) {
                content.append("<p><img src='").append(ccq.getImagePath()).append("' alt='Question Image' style='max-width: 100%;'/></p>");
            }
        }

        content.append("<p><b>Your Answer:</b> ").append(userAnswer).append("</p>");
        content.append("<p><b>Correct Answer:</b> ").append(question.getCorrectAnswer()).append("</p>");
        
        if (question.getExplanation() != null && !question.getExplanation().isEmpty()) {
            content.append("<p><b>Explanation:</b> ").append(question.getExplanation()).append("</p>");
        }
        
        content.append("</body></html>");
        
        JTextPane explanationPane = new JTextPane();
        explanationPane.setContentType("text/html");
        explanationPane.setText(content.toString());
        explanationPane.setEditable(false);
        
        showDialog("Explanation", explanationPane, true);
    }
} 