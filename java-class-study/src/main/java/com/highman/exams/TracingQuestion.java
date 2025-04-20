package com.highman.exams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a question where students trace algorithm execution and provide intermediate states.
 * Used for questions involving data structures (stacks, trees) or algorithms (sorting, balancing).
 */
public class TracingQuestion extends Question {
    private String codeBlock;
    private List<TracePoint> tracePoints;
    private String imagePath;

    /**
     * Represents a point in the code where the state needs to be traced.
     */
    public static class TracePoint {
        private String label;
        private String correctAnswer;

        public TracePoint(String label, String correctAnswer) {
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

    public TracingQuestion() {
        super();
        this.tracePoints = new ArrayList<>();
    }

    public TracingQuestion(String questionText, String codeBlock, List<TracePoint> tracePoints, String explanation) {
        super(questionText, explanation);
        this.codeBlock = codeBlock;
        this.tracePoints = tracePoints != null ? tracePoints : new ArrayList<>();
    }

    public String getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(String codeBlock) {
        this.codeBlock = codeBlock;
    }

    public List<TracePoint> getTracePoints() {
        return tracePoints;
    }

    public void setTracePoints(List<TracePoint> tracePoints) {
        this.tracePoints = tracePoints;
    }

    public void addTracePoint(String label, String correctAnswer) {
        tracePoints.add(new TracePoint(label, correctAnswer));
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String getComponentType() {
        return "Tracing";
    }

    /**
     * Normalizes a trace answer by removing extra spaces and standardizing format.
     * @param answer The answer to normalize
     * @return The normalized answer
     */
    private String normalizeAnswer(String answer) {
        if (answer == null) return null;
        
        // Remove all whitespace
        answer = answer.replaceAll("\\s+", "");
        
        // Remove outer brackets if present
        if (answer.startsWith("[") && answer.endsWith("]")) {
            answer = answer.substring(1, answer.length() - 1);
        }
        
        return answer;
    }

    /**
     * Checks if the given answers match the correct answers for all trace points.
     * @param userAnswers List of user answers in order of trace points
     * @return true if all answers match their corresponding trace points
     */
    public boolean isCorrect(List<String> userAnswers) {
        if (userAnswers == null || userAnswers.size() != tracePoints.size()) {
            return false;
        }

        for (int i = 0; i < tracePoints.size(); i++) {
            String normalizedUser = normalizeAnswer(userAnswers.get(i));
            String normalizedCorrect = normalizeAnswer(tracePoints.get(i).getCorrectAnswer());
            
            if (!Objects.equals(normalizedUser, normalizedCorrect)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public void display() {
        System.out.println(getQuestionText());
        if (codeBlock != null && !codeBlock.isEmpty()) {
            System.out.println("\nCode to trace:");
            System.out.println(codeBlock);
        }
        System.out.println("\nTrace points:");
        for (TracePoint point : tracePoints) {
            System.out.println(point.getLabel() + ": _____________");
        }
    }

    @Override
    public String getCorrectAnswer() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tracePoints.size(); i++) {
            if (i > 0) sb.append("; ");
            sb.append(tracePoints.get(i).getLabel())
              .append(": ")
              .append(tracePoints.get(i).getCorrectAnswer());
        }
        return sb.toString();
    }
} 