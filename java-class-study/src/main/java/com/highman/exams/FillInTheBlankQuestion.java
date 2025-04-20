package com.highman.exams;

/**
 * Represents a fill-in-the-blank question.
 */
public class FillInTheBlankQuestion extends Question {
    private String correctAnswer;

    public FillInTheBlankQuestion() {
        super();
        this.correctAnswer = "";
    }

    public FillInTheBlankQuestion(String questionText, String correctAnswer, String explanation) {
        super(questionText, explanation);
        this.correctAnswer = correctAnswer != null ? correctAnswer.trim() : "";
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer != null ? correctAnswer : "";
    }

    /**
     * Checks if the given answer is correct, ignoring case and extra whitespace.
     * @param userAnswer The user's answer
     * @return true if the answer is correct, false otherwise
     */
    public boolean isCorrectAnswer(String userAnswer) {
        if (correctAnswer == null || correctAnswer.trim().isEmpty() || userAnswer == null) {
            return false;
        }
        return correctAnswer.trim().equalsIgnoreCase(userAnswer.trim());
    }

    @Override
    public void display() {
        System.out.println(questionText);
        System.out.println("Enter your answer: ________");
    }

    @Override
    public String getComponentType() {
        return "FillInTheBlank";
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer != null ? correctAnswer.trim() : "";
    }
}