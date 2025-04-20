package com.highman.logic;

import com.highman.exams.*;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Reads and parses exam questions from YAML files.
 */
public class ExamReader {
    private final String examDirectory;

    public ExamReader(String examDirectory) {
        this.examDirectory = examDirectory;
    }

    /**
     * Loads questions from a YAML file.
     * @param fileName The name of the YAML file
     * @return A list of exam components
     * @throws IOException If there is an error reading the file
     */
    public List<ExamComponent> loadQuestions(String fileName) throws IOException {
        Map<String, Object> yamlData = parseYaml(fileName);
        List<Map<String, Object>> questionList = getQuestionList(yamlData);
        List<ExamComponent> questions = new ArrayList<>();

        for (Map<String, Object> q : questionList) {
            ExamComponent component = createQuestion(q);
            if (component != null) {
                questions.add(component);
            }
        }

        return questions;
    }

    /**
     * Parses a YAML file into a Map.
     */
    private Map<String, Object> parseYaml(String fileName) throws IOException {
        File file = new File(examDirectory, fileName);
        if (!file.exists()) {
            throw new IOException("Exam file not found: " + fileName);
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Object yamlContent = yaml.load(fis);
            
            if (yamlContent instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> result = (Map<String, Object>) yamlContent;
                return result;
            }
            throw new IOException("Invalid YAML format: expected a map at root level");
        }
    }

    /**
     * Extracts the list of questions from the YAML data.
     */
    private List<Map<String, Object>> getQuestionList(Map<String, Object> yamlData) throws IOException {
        if (yamlData.containsKey("questions")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> questions = (List<Map<String, Object>>) yamlData.get("questions");
            return questions;
        }
        throw new IOException("Invalid YAML format: missing 'questions' key");
    }

    /**
     * Creates a question based on its type.
     */
    private ExamComponent createQuestion(Map<String, Object> q) throws IOException {
        String type = (String) q.get("type");
        if (type == null) {
            throw new IOException("Question missing required 'type' field");
        }

        String questionText = (String) q.getOrDefault("question", q.get("text"));
        String explanation = (String) q.get("explanation");
        String imagePath = (String) q.get("imagePath");
        String codeBlock = (String) q.get("codeBlock");

        switch (type.toLowerCase()) {
            case "codecompletion":
                return createCodeCompletionQuestion(questionText, codeBlock, q, explanation, imagePath);
                
            case "tracing":
                return createTracingQuestion(questionText, codeBlock, q, explanation, imagePath);
                
            case "analysis":
                return createAnalysisQuestion(questionText, codeBlock, q, explanation, imagePath);
                
            case "multiplechoice":
                return createMultipleChoiceQuestion(questionText, q, explanation);
                
            case "truefalse":
                return createTrueFalseQuestion(questionText, q, explanation);
                
            case "fillintheblank":
                return createFillInTheBlankQuestion(questionText, q, explanation);
                
            case "matching":
                return createMatchingQuestion(questionText, q, explanation);
                
            case "ordering":
                return createOrderingQuestion(questionText, q, explanation);
                
            case "multipleselect":
                return createMultipleSelectQuestion(questionText, q, explanation);
                
            default:
                throw new IOException("Unsupported question type: " + type);
        }
    }

    private CodeCompletionQuestion createCodeCompletionQuestion(
            String questionText, String codeBlock, Map<String, Object> data,
            String explanation, String imagePath) {
        String correctAnswer = (String) data.getOrDefault("correctAnswer", data.get("correct"));
        CodeCompletionQuestion question = new CodeCompletionQuestion(questionText, codeBlock, correctAnswer, explanation);
        if (imagePath != null) {
            question.setImagePath(imagePath);
        }
        return question;
    }

    private TracingQuestion createTracingQuestion(
            String questionText, String codeBlock, Map<String, Object> data,
            String explanation, String imagePath) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> tracePointsData = (List<Map<String, Object>>) data.get("tracePoints");
        List<TracingQuestion.TracePoint> tracePoints = new ArrayList<>();
        
        if (tracePointsData != null) {
            tracePoints = tracePointsData.stream()
                .map(tp -> new TracingQuestion.TracePoint(
                    (String) tp.get("label"),
                    (String) tp.get("correctAnswer")))
                .collect(Collectors.toList());
        }
        
        TracingQuestion question = new TracingQuestion(questionText, codeBlock, tracePoints, explanation);
        if (imagePath != null) {
            question.setImagePath(imagePath);
        }
        return question;
    }

    private AnalysisQuestion createAnalysisQuestion(
            String questionText, String codeBlock, Map<String, Object> data,
            String explanation, String imagePath) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> stepsData = (List<Map<String, Object>>) data.get("steps");
        List<AnalysisQuestion.AnalysisStep> steps = new ArrayList<>();
        
        if (stepsData != null) {
            steps = stepsData.stream()
                .map(step -> new AnalysisQuestion.AnalysisStep(
                    (String) step.get("label"),
                    (String) step.get("correctAnswer")))
                .collect(Collectors.toList());
        }
        
        AnalysisQuestion question = new AnalysisQuestion(questionText, codeBlock, steps, explanation);
        if (imagePath != null) {
            question.setImagePath(imagePath);
        }
        return question;
    }

    private MultipleChoiceQuestion createMultipleChoiceQuestion(
            String questionText, Map<String, Object> data, String explanation) {
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        question.setQuestionText(questionText);
        question.setExplanation(explanation);
        
        @SuppressWarnings("unchecked")
        List<String> choices = (List<String>) data.get("choices");
        if (choices != null) {
            for (String choice : choices) {
                question.addChoice(choice);
            }
        }
        
        String correct = (String) data.getOrDefault("correct", data.get("correctAnswer"));
        if (correct != null && !correct.isEmpty() && choices != null) {
            int index = correct.charAt(0) - 'A';
            if (index >= 0 && index < choices.size()) {
                question.setCorrectAnswer(choices.get(index));
            }
        }
        
        return question;
    }

    private TrueFalseQuestion createTrueFalseQuestion(
            String questionText, Map<String, Object> data, String explanation) {
        String correct = (String) data.getOrDefault("correct", data.get("correctAnswer"));
        return new TrueFalseQuestion(questionText, correct, explanation);
    }

    private FillInTheBlankQuestion createFillInTheBlankQuestion(
            String questionText, Map<String, Object> data, String explanation) {
        FillInTheBlankQuestion question = new FillInTheBlankQuestion();
        question.setQuestionText(questionText);
        question.setExplanation(explanation);
        String correctAnswer = (String) data.getOrDefault("correct", data.get("correctAnswer"));
        question.setCorrectAnswer(correctAnswer);
        return question;
    }

    private MatchingQuestion createMatchingQuestion(
            String questionText, Map<String, Object> data, String explanation) {
        MatchingQuestion question = new MatchingQuestion();
        question.setQuestionText(questionText);
        question.setExplanation(explanation);
        
        @SuppressWarnings("unchecked")
        List<String> leftItems = (List<String>) data.get("leftItems");
        @SuppressWarnings("unchecked")
        List<String> rightItems = (List<String>) data.get("rightItems");
        @SuppressWarnings("unchecked")
        Map<String, String> correctMatches = (Map<String, String>) data.get("correctMatches");
        
        if (leftItems != null) question.setLeftItems(leftItems);
        if (rightItems != null) question.setRightItems(rightItems);
        if (correctMatches != null) question.setCorrectMatches(correctMatches);
        
        return question;
    }

    private OrderingQuestion createOrderingQuestion(
            String questionText, Map<String, Object> data, String explanation) {
        OrderingQuestion question = new OrderingQuestion();
        question.setQuestionText(questionText);
        question.setExplanation(explanation);
        
        @SuppressWarnings("unchecked")
        List<String> items = (List<String>) data.get("items");
        if (items != null) {
            for (String item : items) {
                question.addItem(item);
            }
        }
        
        @SuppressWarnings("unchecked")
        List<String> correctOrder = (List<String>) data.get("correctOrder");
        if (correctOrder != null) {
            question.setCorrectOrder(correctOrder);
        }
        
        return question;
    }

    private MultipleSelectQuestion createMultipleSelectQuestion(
            String questionText, Map<String, Object> data, String explanation) {
        MultipleSelectQuestion question = new MultipleSelectQuestion();
        question.setQuestionText(questionText);
        question.setExplanation(explanation);
        
        @SuppressWarnings("unchecked")
        List<String> options = (List<String>) data.get("choices");
        if (options != null) {
            for (String option : options) {
                question.addOption(option);
            }
        }
        
        @SuppressWarnings("unchecked")
        List<String> correctAnswers = (List<String>) data.get("correctAnswers");
        if (correctAnswers != null && options != null) {
            List<String> selectedAnswers = new ArrayList<>();
            for (String answer : correctAnswers) {
                int index = answer.charAt(0) - 'A';
                if (index >= 0 && index < options.size()) {
                    selectedAnswers.add(options.get(index));
                }
            }
            question.setCorrectAnswers(selectedAnswers);
        }
        
        return question;
    }
} 