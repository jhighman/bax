package com.highman.exams;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a multiple choice question with a list of possible answers.
 */
public class MultipleChoiceQuestion extends Question {
    private List<String> answerChoices;
    private String correctAnswer;
    private String codeSnippet;
    private String imagePath;

    public MultipleChoiceQuestion() {
        super();
        this.answerChoices = new ArrayList<>();
        this.codeSnippet = null;
        this.imagePath = null;
    }

    public MultipleChoiceQuestion(String questionText, List<String> answerChoices, String correctAnswer, String explanation) {
        super(questionText, explanation);
        this.answerChoices = new ArrayList<>(answerChoices);
        this.correctAnswer = correctAnswer;
        this.codeSnippet = null;
        this.imagePath = null;
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public void display() {
        System.out.println(questionText);
        if (codeSnippet != null) {
            System.out.println("Code Snippet:");
            System.out.println(codeSnippet);
        }
        for (int i = 0; i < answerChoices.size(); i++) {
            char choiceLetter = (char) ('A' + i);
            System.out.println(choiceLetter + ") " + answerChoices.get(i));
        }
    }

    @Override
    public String getComponentType() {
        return "MultipleChoice";
    }

    public List<String> getAnswerChoices() {
        return new ArrayList<>(answerChoices);
    }

    public String getCodeSnippet() {
        return codeSnippet;
    }

    public void setCodeSnippet(String codeSnippet) {
        this.codeSnippet = codeSnippet;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setAnswerChoices(List<String> answerChoices) {
        this.answerChoices = new ArrayList<>(answerChoices);
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}