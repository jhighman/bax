package com.highman.model;

import com.highman.exams.ExamComponent;

/**
 * Represents the result of answering a question in the exam.
 */
public class ExamResult {
    private final ExamComponent component;
    private final String answer;
    private boolean correct;

    /**
     * Creates a new exam result.
     * @param component The exam component (question) that was answered
     * @param answer The user's answer
     * @param correct Whether the answer was correct
     */
    public ExamResult(ExamComponent component, String answer, boolean correct) {
        this.component = component;
        this.answer = answer;
        this.correct = correct;
    }

    /**
     * Gets the exam component (question) that was answered
     * @return The exam component
     */
    public ExamComponent getComponent() {
        return component;
    }

    /**
     * Gets the user's answer
     * @return The answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Checks if the answer was correct
     * @return true if the answer was correct, false otherwise
     */
    public boolean isCorrect() {
        return correct;
    }

    /**
     * Sets whether this result is correct.
     * @param correct The new correctness value
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
} 