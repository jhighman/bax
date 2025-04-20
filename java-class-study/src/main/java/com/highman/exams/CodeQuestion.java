package com.highman.exams;

/**
 * Represents a question that requires analyzing, completing, or determining the output of code.
 */
public class CodeQuestion extends Question {
    private String codeBlock;
    private String correctAnswer;

    public CodeQuestion() {
        super();
    }

    public CodeQuestion(String questionText, String codeBlock, String correctAnswer, String explanation) {
        super(questionText, explanation);
        this.codeBlock = codeBlock;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(String codeBlock) {
        this.codeBlock = codeBlock;
    }

    @Override
    public String getComponentType() {
        return "Code";
    }

    /**
     * Checks if the given answer is correct.
     * Performs a case-sensitive exact match.
     */
    public boolean isCorrect(String answer) {
        if (answer == null || correctAnswer == null) return false;
        // Trim whitespace but preserve internal spaces
        return answer.trim().equals(correctAnswer.trim());
    }

    @Override
    public void display() {
        System.out.println(questionText);
        System.out.println("\nCode:");
        System.out.println(codeBlock);
    }
} 