package com.highman.exams;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a multiple select question where users can select multiple correct answers.
 */
public class MultipleSelectQuestion extends Question {
    private List<String> options;
    private List<String> correctAnswers;

    public MultipleSelectQuestion() {
        super();
        this.options = new ArrayList<>();
        this.correctAnswers = new ArrayList<>();
    }

    public MultipleSelectQuestion(String questionText, List<String> answerChoices, List<String> correctAnswers, String explanation) {
        super(questionText, explanation);
        this.options = new ArrayList<>(answerChoices);
        this.correctAnswers = new ArrayList<>();
        
        // Convert letter-based answers to actual text answers
        for (String answer : correctAnswers) {
            if (answer.length() == 1 && answer.charAt(0) >= 'A' && answer.charAt(0) <= 'Z') {
                int index = answer.charAt(0) - 'A';
                if (index < answerChoices.size()) {
                    this.correctAnswers.add(answerChoices.get(index));
                }
            } else {
                this.correctAnswers.add(answer);
            }
        }
    }

    public void addOption(String option) {
        options.add(option);
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = new ArrayList<>(correctAnswers);
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswers.toString();
    }

    @Override
    public void display() {
        System.out.println(questionText);
        System.out.println("Select all that apply:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
    }

    @Override
    public String getComponentType() {
        return "MultipleSelect";
    }

    public List<String> getOptions() {
        return new ArrayList<>(options);
    }

    public List<String> getCorrectAnswers() {
        return new ArrayList<>(correctAnswers);
    }

    // For backward compatibility
    public List<String> getAnswerChoices() {
        return getOptions();
    }
}