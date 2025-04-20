package com.highman.exams;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an ordering question where users arrange items in the correct order.
 */
public class OrderingQuestion extends Question {
    private List<String> items;
    private List<String> correctOrder;

    public OrderingQuestion() {
        super();
        this.items = new ArrayList<>();
        this.correctOrder = new ArrayList<>();
    }

    public OrderingQuestion(String questionText, List<String> items, List<String> correctOrder, String explanation) {
        super(questionText, explanation);
        this.items = new ArrayList<>(items);
        this.correctOrder = new ArrayList<>(correctOrder);
    }

    @Override
    public String getCorrectAnswer() {
        return correctOrder.toString();
    }

    @Override
    public void display() {
        System.out.println(questionText);
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ") " + items.get(i));
        }
    }

    @Override
    public String getComponentType() {
        return "Ordering";
    }

    public List<String> getItems() {
        return new ArrayList<>(items);
    }

    public List<String> getCorrectOrder() {
        return new ArrayList<>(correctOrder);
    }

    public void setItems(List<String> items) {
        this.items = new ArrayList<>(items);
    }

    public void setCorrectOrder(List<String> correctOrder) {
        this.correctOrder = new ArrayList<>(correctOrder);
    }
}