package com.highman.logic;

import com.highman.exams.ExamComponent;
import com.highman.model.ExamResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the state of the exam, including current question, score, and results.
 */
public class ExamState {
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<ExamResult> results;
    private List<ExamComponent> components;

    /**
     * Creates a new exam state with empty results.
     */
    public ExamState() {
        this.results = new ArrayList<>();
    }

    /**
     * Sets the exam components (questions)
     * @param components The list of exam components
     */
    public void setComponents(List<ExamComponent> components) {
        this.components = components;
        reset(); // Reset the exam state when new components are set
    }

    /**
     * Gets the list of exam components
     * @return The list of exam components
     */
    public List<ExamComponent> getComponents() {
        return components;
    }

    /**
     * Gets the current question index
     * @return The current question index
     */
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    /**
     * Gets the current score
     * @return The current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the list of results
     * @return The list of results
     */
    public List<ExamResult> getResults() {
        return results;
    }

    /**
     * Gets the current question
     * @return The current question
     */
    public ExamComponent getCurrentQuestion() {
        return components.get(currentQuestionIndex);
    }

    /**
     * Records an answer and moves to the next question
     * @param answer The user's answer
     * @param isCorrect Whether the answer was correct
     */
    public void recordAnswer(String answer, boolean isCorrect) {
        results.add(new ExamResult(getCurrentQuestion(), answer, isCorrect));
        if (isCorrect) {
            score++;
        }
        currentQuestionIndex++;
    }

    /**
     * Records a skipped question
     */
    public void skipQuestion() {
        results.add(new ExamResult(getCurrentQuestion(), null, false));
        currentQuestionIndex++;
    }

    /**
     * Resets the exam state
     */
    public void reset() {
        currentQuestionIndex = 0;
        score = 0;
        results.clear();
    }

    /**
     * Checks if the exam is complete
     * @return true if the exam is complete, false otherwise
     */
    public boolean isComplete() {
        return currentQuestionIndex >= components.size();
    }
} 