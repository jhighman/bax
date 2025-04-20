package com.highman.exams;

/**
 * Abstract base class for exam questions, implementing ExamComponent.
 */
public abstract class Question implements ExamComponent {
    protected String questionText;
    protected String explanation;

    protected Question() {
    }

    public Question(String questionText, String explanation) {
        this.questionText = questionText;
        this.explanation = explanation != null ? explanation : "";
    }

    @Override
    public abstract String getCorrectAnswer();

    @Override
    public abstract void display();

    @Override
    public abstract String getComponentType();

    @Override
    public String getExplanation() {
        return explanation;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation != null ? explanation : "";
    }
}