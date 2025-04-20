package com.highman.exams;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a multiple select question where users can select multiple correct answers.
 */
public class MultipleSelectQuestion extends Question {
    private List<String> answerChoices;
    private List<String> correctAnswers;

    public MultipleSelectQuestion() {
        super();
        this.answerChoices = new ArrayList<>();
        this.correctAnswers = new ArrayList<>();
    }

    public MultipleSelectQuestion(String questionText, List<String> answerChoices, List<String> correctAnswers, String explanation) {
        super(questionText, explanation);
        this.answerChoices = new ArrayList<>(answerChoices);
        this.correctAnswers = new ArrayList<>(correctAnswers);
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswers.toString();
    }

    @Override
    public void display() {
        System.out.println(questionText);
        for (int i = 0; i < answerChoices.size(); i++) {
            char choiceLetter = (char) ('A' + i);
            System.out.println(choiceLetter + ") " + answerChoices.get(i));
        }
    }

    @Override
    public String getComponentType() {
        return "MultipleSelect";
    }

    public List<String> getAnswerChoices() {
        return new ArrayList<>(answerChoices);
    }

    public List<String> getCorrectAnswers() {
        return new ArrayList<>(correctAnswers);
    }

    public void setAnswerChoices(List<String> answerChoices) {
        this.answerChoices = new ArrayList<>(answerChoices);
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = new ArrayList<>(correctAnswers);
    }
}