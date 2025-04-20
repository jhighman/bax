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
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
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
        this.correctAnswer = correctAnswer;
    }
}