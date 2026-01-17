package com.highman.exams;

/**
 * Represents a true/false question.
 */
public class TrueFalseQuestion extends Question {
    private String correctAnswer;

    public TrueFalseQuestion() {
        super();
    }

    public TrueFalseQuestion(String questionText, String correctAnswer, String explanation) {
        super(questionText, explanation);
        setCorrectAnswer(correctAnswer); // Use setter to ensure proper format
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Converts the answer to a standardized format.
     * Accepts either "A"/"B" or "True"/"False" and converts to "True"/"False".
     */
    public static String standardizeAnswer(String answer) {
        if (answer == null) return null;
        answer = answer.trim().toUpperCase();
        // First check for letter answers
        if (answer.equals("A")) return "True";
        if (answer.equals("B")) return "False";
        // Then check for word answers
        if (answer.equals("TRUE")) return "True";
        if (answer.equals("FALSE")) return "False";
        // If not recognized, return null to indicate invalid answer
        return null;
    }

    @Override
    public void display() {
        System.out.println(questionText);
        System.out.println("A) True");
        System.out.println("B) False");
    }

    @Override
    public String getComponentType() {
        return "TrueFalse";
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = standardizeAnswer(correctAnswer);
    }

    /**
     * Checks if the given answer is correct.
     * Accepts either "A"/"B" or "True"/"False" format.
     */
    public boolean isCorrect(String answer) {
        // Add debug logging
        System.out.println("Debug - Raw answer: [" + answer + "]");
        System.out.println("Debug - Raw correct answer: [" + correctAnswer + "]");
        
        String standardizedUserAnswer = standardizeAnswer(answer);
        String standardizedCorrectAnswer = standardizeAnswer(correctAnswer);
        
        System.out.println("Debug - Standardized user answer: [" + standardizedUserAnswer + "]");
        System.out.println("Debug - Standardized correct answer: [" + standardizedCorrectAnswer + "]");
        
        if (standardizedUserAnswer == null || standardizedCorrectAnswer == null) {
            System.out.println("Debug - Invalid answer format detected");
            return false;
        }
        
        boolean isCorrect = standardizedUserAnswer.equals(standardizedCorrectAnswer);
        System.out.println("Debug - Is correct? " + isCorrect);
        return isCorrect;
    }
}