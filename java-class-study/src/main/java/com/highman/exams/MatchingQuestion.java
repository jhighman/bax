package com.highman.exams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Represents a matching question where users match items from two lists.
 */
public class MatchingQuestion extends Question {
    private List<String> leftItems;
    private List<String> rightItems;
    private Map<String, String> correctMatches;
    private Map<String, String> pairs;

    public MatchingQuestion() {
        super();
        this.leftItems = new ArrayList<>();
        this.rightItems = new ArrayList<>();
        this.pairs = new LinkedHashMap<>();
        this.correctMatches = new LinkedHashMap<>();
    }

    public MatchingQuestion(String questionText, List<String> leftItems, List<String> rightItems, Map<String, String> correctMatches, String explanation) {
        super(questionText, explanation);
        this.leftItems = new ArrayList<>(leftItems);
        this.rightItems = new ArrayList<>(rightItems);
        this.correctMatches = correctMatches;
        this.pairs = new LinkedHashMap<>(correctMatches);
    }

    public void addPair(String left, String right) {
        pairs.put(left, right);
        correctMatches.put(left, right);
    }

    @Override
    public String getCorrectAnswer() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : correctMatches.entrySet()) {
            if (sb.length() > 0) sb.append(";");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    @Override
    public void display() {
        System.out.println(questionText);
        System.out.println("\nLeft side:");
        int i = 1;
        for (String left : pairs.keySet()) {
            System.out.println(i + ". " + left);
            i++;
        }
        System.out.println("\nRight side:");
        i = 1;
        for (String right : pairs.values()) {
            System.out.println((char)('A' + i - 1) + ". " + right);
            i++;
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

    public Map<String, String> getPairs() {
        return new LinkedHashMap<>(pairs);
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