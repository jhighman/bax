package com.highman.logic;

import com.highman.exams.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * Loads exam questions from YAML files.
 */
public class QuestionLoader {
    private final String examDirectory;

    /**
     * Creates a new QuestionLoader for the specified directory.
     * @param examDirectory The directory containing exam YAML files
     */
    public QuestionLoader(String examDirectory) {
        this.examDirectory = examDirectory;
    }

    /**
     * Loads questions from a YAML file.
     * @param fileName The name of the YAML file
     * @return A list of exam components
     * @throws IOException If there is an error reading the file
     */
    public List<ExamComponent> loadQuestions(String fileName) throws IOException {
        File file = new File(examDirectory, fileName);
        if (!file.exists()) {
            throw new IOException("Exam file not found: " + fileName);
        }

        Yaml yaml = new Yaml();
        Object yamlContent = yaml.load(new FileInputStream(file));
        List<ExamComponent> components = new ArrayList<>();

        List<Map<String, Object>> questions;
        if (yamlContent instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> contentMap = (Map<String, Object>) yamlContent;
            if (contentMap.containsKey("questions")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> questionsList = (List<Map<String, Object>>) contentMap.get("questions");
                questions = questionsList;
            } else {
                throw new IOException("Invalid YAML format: missing 'questions' key");
            }
        } else if (yamlContent instanceof List) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> questionsList = (List<Map<String, Object>>) yamlContent;
            questions = questionsList;
        } else {
            throw new IOException("Invalid YAML format: expected a map with 'questions' key or a list of questions");
        }

        if (questions != null) {
            for (Map<String, Object> questionData : questions) {
                String type = (String) questionData.get("type");
                ExamComponent component = createComponent(type, questionData);
                if (component != null) {
                    components.add(component);
                }
            }
        }

        return components;
    }

    /**
     * Creates an exam component from the given data.
     * @param type The type of component to create
     * @param data The component data
     * @return The created component
     */
    private ExamComponent createComponent(String type, Map<String, Object> data) {
        if (type == null) return null;
        
        // Get question text from either "text" or "question" field
        String questionText = (String) data.getOrDefault("text", data.get("question"));
        String explanation = (String) data.get("explanation");
        
        switch (type.toLowerCase()) {
            case "multiplechoice":
                MultipleChoiceQuestion mcq = new MultipleChoiceQuestion();
                mcq.setQuestionText(questionText);
                mcq.setExplanation(explanation);
                @SuppressWarnings("unchecked")
                List<String> choices = (List<String>) data.get("choices");
                if (choices != null) {
                    for (String choice : choices) {
                        mcq.addChoice(choice);
                    }
                }
                String correct = (String) data.getOrDefault("correct", data.get("correctAnswer"));
                if (correct != null && !correct.isEmpty()) {
                    int index = correct.charAt(0) - 'A';
                    if (index >= 0 && index < choices.size()) {
                        mcq.setCorrectAnswer(choices.get(index));
                    }
                }
                String codeSnippet = (String) data.get("codeSnippet");
                if (codeSnippet != null) {
                    mcq.setCodeSnippet(codeSnippet);
                }
                return mcq;
                
            case "truefalse":
                TrueFalseQuestion tfq = createTrueFalseQuestion(data);
                return tfq;
                
            case "fillintheblank":
                FillInTheBlankQuestion fibq = new FillInTheBlankQuestion();
                fibq.setQuestionText(questionText);
                fibq.setExplanation(explanation);
                String correctAnswer = (String) data.getOrDefault("correct", data.get("correctAnswer"));
                fibq.setCorrectAnswer(correctAnswer);
                return fibq;
                
            case "matching":
                MatchingQuestion mq = new MatchingQuestion();
                mq.setQuestionText(questionText);
                mq.setExplanation(explanation);
                @SuppressWarnings("unchecked")
                List<String> leftItems = (List<String>) data.get("leftItems");
                @SuppressWarnings("unchecked")
                List<String> rightItems = (List<String>) data.get("rightItems");
                @SuppressWarnings("unchecked")
                Map<String, String> correctMatches = (Map<String, String>) data.get("correctMatches");
                
                if (leftItems != null) {
                    mq.setLeftItems(leftItems);
                }
                if (rightItems != null) {
                    mq.setRightItems(rightItems);
                }
                if (correctMatches != null) {
                    mq.setCorrectMatches(correctMatches);
                }
                return mq;
                
            case "ordering":
                OrderingQuestion oq = new OrderingQuestion();
                oq.setQuestionText(questionText);
                oq.setExplanation(explanation);
                @SuppressWarnings("unchecked")
                List<String> items = (List<String>) data.get("items");
                if (items != null) {
                    for (String item : items) {
                        oq.addItem(item);
                    }
                }
                @SuppressWarnings("unchecked")
                List<String> correctOrder = (List<String>) data.get("correctOrder");
                if (correctOrder != null) {
                    oq.setCorrectOrder(correctOrder);
                }
                return oq;
                
            case "multipleselect":
                MultipleSelectQuestion msq = new MultipleSelectQuestion();
                msq.setQuestionText(questionText);
                msq.setExplanation(explanation);
                @SuppressWarnings("unchecked")
                List<String> options = (List<String>) data.get("choices");
                if (options != null) {
                    for (String option : options) {
                        msq.addOption(option);
                    }
                }
                @SuppressWarnings("unchecked")
                List<String> correctAnswers = (List<String>) data.get("correctAnswers");
                if (correctAnswers != null) {
                    List<String> selectedAnswers = new ArrayList<>();
                    for (String answer : correctAnswers) {
                        int index = answer.charAt(0) - 'A';
                        if (index >= 0 && index < options.size()) {
                            selectedAnswers.add(options.get(index));
                        }
                    }
                    msq.setCorrectAnswers(selectedAnswers);
                }
                return msq;

            case "code":
                CodeQuestion cq = new CodeQuestion();
                cq.setQuestionText(questionText);
                cq.setExplanation(explanation);
                String codeBlock = (String) data.get("codeBlock");
                correctAnswer = (String) data.getOrDefault("correct", data.get("correctAnswer"));
                if (codeBlock != null) {
                    cq.setCodeBlock(codeBlock);
                }
                if (correctAnswer != null) {
                    cq.setCorrectAnswer(correctAnswer);
                }
                return cq;

            case "codecompletion":
                CodeCompletionQuestion ccq = new CodeCompletionQuestion();
                ccq.setQuestionText(questionText);
                ccq.setExplanation(explanation);
                codeBlock = (String) data.get("codeBlock");
                correctAnswer = (String) data.getOrDefault("correct", data.get("correctAnswer"));
                String imagePath = (String) data.get("imagePath");
                if (codeBlock != null) {
                    ccq.setCodeBlock(codeBlock);
                }
                if (correctAnswer != null) {
                    ccq.setCorrectAnswer(correctAnswer);
                }
                if (imagePath != null) {
                    ccq.setImagePath(imagePath);
                }
                return ccq;

            case "tracing":
                TracingQuestion tq = new TracingQuestion();
                tq.setQuestionText(questionText);
                tq.setExplanation(explanation);
                codeBlock = (String) data.get("codeBlock");
                imagePath = (String) data.get("imagePath");
                
                if (codeBlock != null) {
                    tq.setCodeBlock(codeBlock);
                }
                if (imagePath != null) {
                    tq.setImagePath(imagePath);
                }
                
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> tracePoints = (List<Map<String, Object>>) data.get("tracePoints");
                if (tracePoints != null) {
                    for (Map<String, Object> point : tracePoints) {
                        String label = (String) point.get("label");
                        String pointAnswer = (String) point.get("correctAnswer");
                        if (label != null && pointAnswer != null) {
                            tq.addTracePoint(label, pointAnswer);
                        }
                    }
                }
                return tq;

            case "analysis":
                AnalysisQuestion aq = new AnalysisQuestion();
                aq.setQuestionText(questionText);
                aq.setExplanation(explanation);
                codeBlock = (String) data.get("codeBlock");
                imagePath = (String) data.get("imagePath");
                
                if (codeBlock != null) {
                    aq.setCodeBlock(codeBlock);
                }
                if (imagePath != null) {
                    aq.setImagePath(imagePath);
                }
                
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> steps = (List<Map<String, Object>>) data.get("steps");
                if (steps != null) {
                    for (Map<String, Object> step : steps) {
                        String label = (String) step.get("label");
                        String stepAnswer = (String) step.get("correctAnswer");
                        if (label != null && stepAnswer != null) {
                            aq.addStep(label, stepAnswer);
                        }
                    }
                }
                return aq;
        }
        
        return null;
    }

    private TrueFalseQuestion createTrueFalseQuestion(Map<String, Object> data) {
        String question = (String) data.get("question");
        String correct = (String) data.getOrDefault("correct", data.get("correctAnswer"));
        String explanation = (String) data.get("explanation");
        
        // Don't convert A/B to True/False here - let TrueFalseQuestion handle it
        return new TrueFalseQuestion(question, correct, explanation);
    }
} 