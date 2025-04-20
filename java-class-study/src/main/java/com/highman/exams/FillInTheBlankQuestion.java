package com.highman.exams;

/**
 * Represents a fill-in-the-blank question.
 */
public class FillInTheBlankQuestion extends Question {
    private String correctAnswer;

    public FillInTheBlankQuestion() {
        super();
    }

    public FillInTheBlankQuestion(String questionText, String correctAnswer, String explanation) {
        super(questionText, explanation);
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
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
        this.correctAnswer = correctAnswer;
    }
}