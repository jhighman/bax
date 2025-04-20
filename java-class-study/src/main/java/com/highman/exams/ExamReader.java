package com.highman.exams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class ExamReader {
    private final String examDirectory;

    public ExamReader(String examDirectory) {
        this.examDirectory = examDirectory;
    }

    @SuppressWarnings("unchecked")
    public List<ExamComponent> loadQuestions(String fileName) throws IOException {
        File file = new File(examDirectory, fileName);
        if (!file.exists()) {
            throw new IOException("Exam file not found: " + fileName);
        }
        List<ExamComponent> components = new ArrayList<>();
        
        System.out.println("Looking for YAML files in: " + file.getAbsolutePath());
        try (FileInputStream input = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Map<String, List<Map<String, Object>>> data = yaml.load(input);
            if (data == null || !data.containsKey("questions")) {
                System.out.println("No 'questions' key found in " + file.getName());
                return components;
            }

            List<Map<String, Object>> questionList = data.get("questions");
            if (questionList == null) {
                System.out.println("'questions' list is null in " + file.getName());
                return components;
            }

            for (Map<String, Object> q : questionList) {
                String type = (String) q.get("type");
                String text = (String) q.get("text");
                String correct = (String) q.get("correct");
                if (correct == null) {
                    correct = (String) q.get("correctAnswer");
                }
                String explanation = (String) q.get("explanation");

                if (type == null || text == null) {
                    System.out.println("Skipping invalid question in " + file.getName() + ": " + q);
                    continue;
                }

                switch (type.toLowerCase()) {
                    case "multiplechoice":
                        List<String> choices = (List<String>) q.get("choices");
                        String codeSnippet = (String) q.get("codeSnippet");
                        String imagePath = (String) q.get("imagePath");
                        if (choices == null || correct == null) {
                            System.out.println("Skipping multiple choice question with no choices or correct answer: " + text);
                            continue;
                        }
                        MultipleChoiceQuestion mcq = new MultipleChoiceQuestion(text, choices, correct, explanation);
                        if (codeSnippet != null) {
                            mcq.setCodeSnippet(codeSnippet);
                        }
                        if (imagePath != null) {
                            mcq.setImagePath(imagePath);
                        }
                        components.add(mcq);
                        break;

                    case "truefalse":
                        if (correct == null) {
                            System.out.println("Skipping true/false question with no correct answer: " + text);
                            continue;
                        }
                        TrueFalseQuestion tfq = new TrueFalseQuestion(text, correct, explanation);
                        components.add(tfq);
                        break;

                    case "fillintheblank":
                        if (correct == null) {
                            System.out.println("Skipping fill-in-the-blank question with no correct answer: " + text);
                            continue;
                        }
                        FillInTheBlankQuestion fibq = new FillInTheBlankQuestion(text, correct, explanation);
                        components.add(fibq);
                        break;

                    case "matching":
                        List<String> leftItems = (List<String>) q.get("leftItems");
                        List<String> rightItems = (List<String>) q.get("rightItems");
                        Map<String, String> correctMatches = (Map<String, String>) q.get("correctMatches");
                        if (leftItems == null || rightItems == null || correctMatches == null) {
                            System.out.println("Skipping matching question with invalid data: " + text);
                            continue;
                        }
                        MatchingQuestion mq = new MatchingQuestion(text, leftItems, rightItems, correctMatches, explanation);
                        components.add(mq);
                        break;

                    case "ordering":
                        List<String> items = (List<String>) q.get("items");
                        List<String> correctOrder = (List<String>) q.get("correctOrder");
                        if (items == null || correctOrder == null) {
                            System.out.println("Skipping ordering question with invalid data: " + text);
                            continue;
                        }
                        OrderingQuestion oq = new OrderingQuestion(text, items, correctOrder, explanation);
                        components.add(oq);
                        break;

                    case "multipleselect":
                        List<String> msChoices = (List<String>) q.get("choices");
                        List<String> correctAnswers = (List<String>) q.get("correctAnswers");
                        if (msChoices == null || correctAnswers == null) {
                            System.out.println("Skipping multiple select question with invalid data: " + text);
                            continue;
                        }
                        MultipleSelectQuestion msq = new MultipleSelectQuestion(text, msChoices, correctAnswers, explanation);
                        components.add(msq);
                        break;

                    default:
                        System.out.println("Unknown question type '" + type + "' in " + file.getName());
                        continue;
                }
                System.out.println("Loaded question: " + text + " (Type: " + type + ")");
            }
        } catch (Exception e) {
            System.out.println("Error processing file " + file.getName() + ": " + e.getMessage());
        }
        
        System.out.println("Total questions loaded: " + components.size());
        return components;
    }
}