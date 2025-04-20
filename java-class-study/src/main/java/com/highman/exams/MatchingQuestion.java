package com.highman.exams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a matching question where users match items from two lists.
 */
public class MatchingQuestion extends Question {
    private List<String> leftItems;
    private List<String> rightItems;
    private Map<String, String> correctMatches;

    public MatchingQuestion() {
        super();
        this.leftItems = new ArrayList<>();
        this.rightItems = new ArrayList<>();
    }

    public MatchingQuestion(String questionText, List<String> leftItems, List<String> rightItems, Map<String, String> correctMatches, String explanation) {
        super(questionText, explanation);
        this.leftItems = new ArrayList<>(leftItems);
        this.rightItems = new ArrayList<>(rightItems);
        this.correctMatches = correctMatches;
    }

    @Override
    public String getCorrectAnswer() {
        return correctMatches.toString();
    }

    @Override
    public void display() {
        System.out.println(questionText);
        for (int i = 0; i < leftItems.size(); i++) {
            System.out.println((char)('A' + i) + ") " + leftItems.get(i));
        }
        System.out.println("\nMatch with:");
        for (int i = 0; i < rightItems.size(); i++) {
            System.out.println((i + 1) + ") " + rightItems.get(i));
        }
    }

    @Override
    public String getComponentType() {
        return "Matching";
    }

    public List<String> getLeftItems() {
        return new ArrayList<>(leftItems);
    }

    public List<String> getRightItems() {
        return new ArrayList<>(rightItems);
    }

    public Map<String, String> getCorrectMatches() {
        return correctMatches;
    }

    public void setLeftItems(List<String> leftItems) {
        this.leftItems = new ArrayList<>(leftItems);
    }

    public void setRightItems(List<String> rightItems) {
        this.rightItems = new ArrayList<>(rightItems);
    }

    public void setCorrectMatches(Map<String, String> correctMatches) {
        this.correctMatches = correctMatches;
    }
}