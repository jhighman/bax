package com.highman;

import com.highman.exams.*;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExamReader {
    private String examDirectory;

    public ExamReader(String examDirectory) {
        this.examDirectory = examDirectory;
    }

    public List<ExamComponent> loadQuestions() throws IOException {
        List<ExamComponent> components = new ArrayList<>();
        File dir = new File(examDirectory);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IOException("Exam directory not found: " + examDirectory);
        }

        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".yaml") || 
                                                 name.toLowerCase().endsWith(".yml"));
        if (files == null || files.length == 0) {
            throw new IOException("No exam files found in directory: " + examDirectory);
        }

        // Load the first YAML file found
        File yamlFile = files[0];
        return loadQuestions(yamlFile.getName());
    }

    public List<ExamComponent> loadQuestions(String fileName) throws IOException {
        List<ExamComponent> components = new ArrayList<>();
        File file = new File(examDirectory, fileName);
        
        try (FileInputStream input = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            List<Map<String, Object>> questions = yaml.load(input);
            
            for (Map<String, Object> questionData : questions) {
                String type = (String) questionData.get("type");
                ExamComponent component = null;
                
                switch (type) {
                    case "MultipleChoice":
                        component = createMultipleChoiceQuestion(questionData);
                        break;
                    case "TrueFalse":
                        component = createTrueFalseQuestion(questionData);
                        break;
                    case "FillInTheBlank":
                        component = createFillInTheBlankQuestion(questionData);
                        break;
                    case "Matching":
                        component = createMatchingQuestion(questionData);
                        break;
                    case "Ordering":
                        component = createOrderingQuestion(questionData);
                        break;
                    case "MultipleSelect":
                        component = createMultipleSelectQuestion(questionData);
                        break;
                }
                
                if (component != null) {
                    components.add(component);
                }
            }
        }
        
        return components;
    }

    private MultipleChoiceQuestion createMultipleChoiceQuestion(Map<String, Object> data) {
        String question = (String) data.get("question");
        @SuppressWarnings("unchecked")
        List<String> choices = (List<String>) data.get("choices");
        String correctAnswer = (String) data.get("correctAnswer");
        String explanation = (String) data.get("explanation");
        
        MultipleChoiceQuestion mcq = new MultipleChoiceQuestion(question, choices, correctAnswer, explanation);
        
        // Set optional fields after construction
        String imagePath = (String) data.get("imagePath");
        String codeSnippet = (String) data.get("codeSnippet");
        if (imagePath != null) {
            mcq.setImagePath(imagePath);
        }
        if (codeSnippet != null) {
            mcq.setCodeSnippet(codeSnippet);
        }
        
        return mcq;
    }

    private TrueFalseQuestion createTrueFalseQuestion(Map<String, Object> data) {
        String question = (String) data.get("question");
        String correctAnswer = (String) data.get("correctAnswer");
        String explanation = (String) data.get("explanation");
        
        return new TrueFalseQuestion(question, correctAnswer, explanation);
    }

    private FillInTheBlankQuestion createFillInTheBlankQuestion(Map<String, Object> data) {
        String question = (String) data.get("question");
        String correctAnswer = (String) data.get("correctAnswer");
        String explanation = (String) data.get("explanation");
        
        return new FillInTheBlankQuestion(question, correctAnswer, explanation);
    }

    private MatchingQuestion createMatchingQuestion(Map<String, Object> data) {
        String question = (String) data.get("question");
        @SuppressWarnings("unchecked")
        List<String> leftItems = (List<String>) data.get("leftItems");
        @SuppressWarnings("unchecked")
        List<String> rightItems = (List<String>) data.get("rightItems");
        @SuppressWarnings("unchecked")
        Map<String, String> correctMatches = (Map<String, String>) data.get("correctMatches");
        String explanation = (String) data.get("explanation");
        
        return new MatchingQuestion(question, leftItems, rightItems, correctMatches, explanation);
    }

    private OrderingQuestion createOrderingQuestion(Map<String, Object> data) {
        String question = (String) data.get("question");
        @SuppressWarnings("unchecked")
        List<String> items = (List<String>) data.get("items");
        @SuppressWarnings("unchecked")
        List<String> correctOrder = (List<String>) data.get("correctOrder");
        String explanation = (String) data.get("explanation");
        
        return new OrderingQuestion(question, items, correctOrder, explanation);
    }

    private MultipleSelectQuestion createMultipleSelectQuestion(Map<String, Object> data) {
        String question = (String) data.get("question");
        @SuppressWarnings("unchecked")
        List<String> choices = (List<String>) data.get("choices");
        @SuppressWarnings("unchecked")
        List<String> correctAnswers = (List<String>) data.get("correctAnswers");
        String explanation = (String) data.get("explanation");
        
        return new MultipleSelectQuestion(question, choices, correctAnswers, explanation);
    }
} 