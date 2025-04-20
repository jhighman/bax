package com.highman.exams;

/**
 * Interface for components of the exam application.
 */
public interface ExamComponent {
    /**
     * Displays the component (e.g., a question or the entire exam UI).
     */
    void display();

    /**
     * Returns the correct answer for the component (if applicable).
     * @return The correct answer as a String, or null if not applicable.
     */
    String getCorrectAnswer();

    /**
     * Returns the type of the component (e.g., question type or exam type).
     * @return The type as a String.
     */
    String getComponentType();

    /**
     * Returns the explanation for the component (if applicable).
     * @return The explanation as a String, or null if not applicable.
     */
    String getExplanation();

    /**
     * Returns the question text for the component (if applicable).
     * @return The question text as a String, or null if not applicable.
     */
    String getQuestionText();
}