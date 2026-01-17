package com.highman.exams;

/**
 * Represents a question where students must complete a code fragment.
 * Typically used for DSN questions involving recursive logic, pointer manipulation,
 * or data structure operations.
 */
public class CodeCompletionQuestion extends Question {
    private String codeBlock;
    private String correctAnswer;
    private String imagePath;

    public CodeCompletionQuestion() {
        super();
    }

    public CodeCompletionQuestion(String questionText, String codeBlock, String correctAnswer, String explanation) {
        super(questionText, explanation);
        this.codeBlock = codeBlock;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCodeBlock() {
        return codeBlock;
    }

    public void setCodeBlock(String codeBlock) {
        this.codeBlock = codeBlock;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String getComponentType() {
        return "CodeCompletion";
    }

    /**
     * Checks if the given answer is correct.
     * Performs a normalized comparison by removing extra whitespace.
     */
    public boolean isCorrect(String answer) {
        if (answer == null || correctAnswer == null) return false;
        
        // Normalize whitespace in both strings
        String normalizedAnswer = normalizeCode(answer);
        String normalizedCorrect = normalizeCode(correctAnswer);
        
        return normalizedAnswer.equals(normalizedCorrect);
    }

    /**
     * Normalizes code by removing extra whitespace and standardizing line endings.
     */
    private String normalizeCode(String code) {
        // Remove leading/trailing whitespace
        code = code.trim();
        
        // Normalize line endings to \n
        code = code.replaceAll("\r\n", "\n");
        
        // Remove extra spaces between words
        code = code.replaceAll("\\s+", " ");
        
        // Remove spaces after certain characters
        code = code.replaceAll("\\s*([{}\\[\\](),.;])\\s*", "$1");
        
        return code;
    }

    @Override
    public void display() {
        System.out.println(questionText);
        System.out.println("\nCode Template:");
        System.out.println(codeBlock);
    }
} 