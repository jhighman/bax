package com.highman.exams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents a question requiring mathematical analysis, complexity analysis,
 * or step-by-step problem solving with intermediate calculations.
 */
public class AnalysisQuestion extends Question {
    private String codeBlock;
    private List<AnalysisStep> steps;
    private String imagePath;

    /**
     * Represents a step in the analysis process with its label and expected answer.
     */
    public static class AnalysisStep {
        private String label;
        private String correctAnswer;

        public AnalysisStep(String label, String correctAnswer) {
            this.label = label;
            this.correctAnswer = correctAnswer;
        }

        public String getLabel() {
            return label;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }
    }

    public AnalysisQuestion() {
        super();
        this.steps = new ArrayList<>();
    }

    public AnalysisQuestion(String questionText, String codeBlock, List<AnalysisStep> steps, String explanation) {
        super(questionText, explanation);
        this.codeBlock = codeBlock;
        this.steps = steps != null ? steps : new ArrayList<>();
    }

    public String getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(String codeBlock) {
        this.codeBlock = codeBlock;
    }

    public List<AnalysisStep> getSteps() {
        return steps;
    }

    public void setSteps(List<AnalysisStep> steps) {
        this.steps = steps;
    }

    public void addStep(String label, String correctAnswer) {
        steps.add(new AnalysisStep(label, correctAnswer));
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String getComponentType() {
        return "Analysis";
    }

    /**
     * Normalizes a mathematical expression by removing spaces and standardizing notation.
     * @param expression The expression to normalize
     * @return The normalized expression
     */
    private String normalizeExpression(String expression) {
        if (expression == null) return null;
        
        // Remove all whitespace
        expression = expression.replaceAll("\\s+", "");
        
        // Standardize multiplication symbols
        expression = expression.replaceAll("×", "*");
        expression = expression.replaceAll("⋅", "*");
        
        // Standardize exponentiation
        expression = expression.replaceAll("\\^", "^");
        
        // Standardize parentheses
        expression = expression.replaceAll("[\\[{]", "(");
        expression = expression.replaceAll("[\\]}]", ")");
        
        // Handle implicit multiplication (e.g., "2n" -> "2*n")
        expression = expression.replaceAll("(\\d)([a-zA-Z])", "$1*$2");
        
        return expression;
    }

    /**
     * Checks if two mathematical expressions are equivalent.
     * @param expr1 First expression
     * @param expr2 Second expression
     * @return true if the expressions are equivalent
     */
    private boolean areExpressionsEquivalent(String expr1, String expr2) {
        String norm1 = normalizeExpression(expr1);
        String norm2 = normalizeExpression(expr2);
        
        if (norm1 == null || norm2 == null) {
            return false;
        }
        
        // First try exact match after normalization
        if (norm1.equals(norm2)) {
            return true;
        }
        
        // For numeric answers, try parsing and comparing
        if (isNumeric(norm1) && isNumeric(norm2)) {
            try {
                double val1 = Double.parseDouble(norm1);
                double val2 = Double.parseDouble(norm2);
                return Math.abs(val1 - val2) < 1e-10; // Use small epsilon for floating-point comparison
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        // For more complex expressions, we could add a computer algebra system
        // or expression parser here in the future
        
        return false;
    }

    private boolean isNumeric(String str) {
        return Pattern.matches("-?\\d*\\.?\\d+", str);
    }

    /**
     * Checks if the given answers match the correct answers for all steps.
     * @param userAnswers List of user answers in order of steps
     * @return true if all answers match their corresponding steps
     */
    public boolean isCorrect(List<String> userAnswers) {
        if (userAnswers == null || userAnswers.size() != steps.size()) {
            return false;
        }

        for (int i = 0; i < steps.size(); i++) {
            if (!areExpressionsEquivalent(userAnswers.get(i), steps.get(i).getCorrectAnswer())) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public void display() {
        System.out.println(getQuestionText());
        if (codeBlock != null && !codeBlock.isEmpty()) {
            System.out.println("\nCode to analyze:");
            System.out.println(codeBlock);
        }
        System.out.println("\nAnalysis steps:");
        for (AnalysisStep step : steps) {
            System.out.println(step.getLabel() + ": _____________");
        }
    }

    @Override
    public String getCorrectAnswer() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < steps.size(); i++) {
            if (i > 0) sb.append("; ");
            sb.append(steps.get(i).getLabel())
              .append(": ")
              .append(steps.get(i).getCorrectAnswer());
        }
        return sb.toString();
    }
} 