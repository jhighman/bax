package com.highman;

import com.highman.exams.ExamComponent;
import com.highman.ui.ExamAppFrame;
import com.highman.logic.QuestionLoader;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main application class for the Java Exam Application.
 */
public class ExamAppSwing {
    private final ExamAppFrame frame;
    private final QuestionLoader questionLoader;

    /**
     * Creates a new ExamAppSwing.
     */
    public ExamAppSwing() {
        frame = new ExamAppFrame();
        questionLoader = new QuestionLoader("exams");
        loadQuestions();
    }

    /**
     * Loads questions from the exam files.
     */
    private void loadQuestions() {
        try {
            // Get list of exam files
            File examDir = new File("exams");
            if (!examDir.exists() || !examDir.isDirectory()) {
                JOptionPane.showMessageDialog(frame,
                    "Error: 'exams' directory not found.",
                    "Directory Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            File[] examFiles = examDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".yaml") || 
                name.toLowerCase().endsWith(".yml"));

            if (examFiles == null || examFiles.length == 0) {
                JOptionPane.showMessageDialog(frame,
                    "No exam files found in the 'exams' directory.\n" +
                    "Please add YAML files to the 'exams' directory.",
                    "No Exams Found",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            // Sort exam files alphabetically
            Arrays.sort(examFiles, Comparator.comparing(File::getName));

            // Create a dialog to select the exam
            String[] examNames = Arrays.stream(examFiles)
                .map(file -> {
                    String name = file.getName().replaceFirst("[.][^.]+$", ""); // Remove extension
                    name = name.replace("_", " ").replace("-", " ");
                    // Capitalize first letter of each word
                    name = Arrays.stream(name.split("\\s+"))
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                        .collect(Collectors.joining(" "));
                    return name + " (" + file.getName() + ")";
                })
                .toArray(String[]::new);

            String selectedExam = (String) JOptionPane.showInputDialog(frame,
                "Select an exam to take:",
                "Choose Exam",
                JOptionPane.QUESTION_MESSAGE,
                null,
                examNames,
                examNames[0]);

            if (selectedExam == null) {
                System.exit(0);
            }

            // Extract the filename from the selection
            String fileName = selectedExam.substring(selectedExam.indexOf("(") + 1, selectedExam.indexOf(")"));
            List<ExamComponent> components = questionLoader.loadQuestions(fileName);
            frame.setComponents(components);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, 
                "Error loading questions: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * Displays the application window.
     */
    public void display() {
        frame.setVisible(true);
    }

    /**
     * The main method to start the application.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ExamAppSwing().display();
        });
    }
}