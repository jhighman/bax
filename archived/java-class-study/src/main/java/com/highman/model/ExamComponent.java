package com.highman.model;

/**
 * Interface that defines the contract for all exam components.
 * This includes questions, answers, and other exam elements.
 */
public interface ExamComponent {
    /**
     * Gets the type of the component (e.g., "MultipleChoice", "TrueFalse", etc.)
     * @return The component type
     */
    String getComponentType();

    /**
     * Gets the correct answer for this component
     * @return The correct answer
     */
    String getCorrectAnswer();

    /**
     * Gets the explanation for this component
     * @return The explanation text
     */
    String getExplanation();
} 