package com.highman.assignment2;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simulates and analyzes the probability distribution of rolling two dice.
 * 
 * This class demonstrates the statistical outcomes of rolling two six-sided dice
 * multiple times. It provides both numerical and visual representations of the
 * results through frequency counts and an ASCII histogram.
 * 
 * The program:
 * <ul>
 *   <li>Simulates rolling two dice a specified number of times</li>
 *   <li>Tracks the frequency of each possible sum (2-12)</li>
 *   <li>Displays the results as both raw numbers and a visual histogram</li>
 * </ul>
 * 
 * The histogram uses asterisks (*) to represent frequency ranges, with each row
 * representing an increment of {@value #GRAPH_SCALE_UNIT} occurrences.
 * 
 * @author Jeff Highman
 * @version 1.0
 * @see Dice
 * @see Die
 */
public class DiceTester {
    /** Number of times to roll the dice in the simulation */
    private static final int NUMBER_OF_ROLLS = 1000;
    
    /** Minimum possible sum when rolling two dice */
    private static final int MIN_DICE_SUM = 2;
    
    /** Maximum possible sum when rolling two dice */
    private static final int MAX_DICE_SUM = 12;
    
    /** Scale unit for the histogram's Y-axis */
    private static final int GRAPH_SCALE_UNIT = 25;
    
    /** Number of rows in the histogram */
    private static final int GRAPH_ROWS = 8;

    /**
     * Main entry point for the dice rolling simulation.
     * Creates a new {@link Dice} object, simulates multiple rolls,
     * and displays the results both numerically and graphically.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Dice dice = new Dice();
        Map<Integer, Integer> frequencies = simulateRolls(dice);
        printFrequencies(frequencies);
        printHistogram(frequencies);
    }

    /**
     * Simulates rolling two dice multiple times and records the frequency of each sum.
     * 
     * @param dice The {@link Dice} object used for rolling
     * @return A Map containing each possible sum (2-12) and its frequency of occurrence
     */
    private static Map<Integer, Integer> simulateRolls(Dice dice) {
        Map<Integer, Integer> frequencies = initializeFrequencyMap();
        
        for (int i = 0; i < NUMBER_OF_ROLLS; i++) {
            dice.roll();
            int sum = dice.getSum();
            frequencies.put(sum, frequencies.get(sum) + 1);
        }
        return frequencies;
    }

    /**
     * Initializes a frequency map with all possible dice sums set to zero.
     * Uses a LinkedHashMap to maintain insertion order for consistent display.
     *
     * @return A LinkedHashMap with keys from {@value #MIN_DICE_SUM} to {@value #MAX_DICE_SUM}
     */
    private static Map<Integer, Integer> initializeFrequencyMap() {
        Map<Integer, Integer> frequencies = new LinkedHashMap<>();
        for (int i = MIN_DICE_SUM; i <= MAX_DICE_SUM; i++) {
            frequencies.put(i, 0);
        }
        return frequencies;
    }

    /**
     * Prints the frequency count for each possible dice sum.
     * Output format: "Number of Xs are Y" where X is the sum and Y is the frequency.
     *
     * @param frequencies Map containing the frequency data for each sum
     */
    private static void printFrequencies(Map<Integer, Integer> frequencies) {
        frequencies.forEach((sum, count) -> 
            System.out.println("Number of " + sum + "s are " + count));
    }

    /**
     * Prints a visual histogram representing the frequency distribution.
     * The histogram shows frequency ranges using asterisks (*) with a scale
     * of {@value #GRAPH_SCALE_UNIT} occurrences per row.
     *
     * @param frequencies Map containing the frequency data for each sum
     */
    private static void printHistogram(Map<Integer, Integer> frequencies) {
        System.out.println("\nGraph");
        printHistogramBody(frequencies);
        printHistogramXAxis();
    }

    /**
     * Prints the main body of the histogram.
     * Each row represents a frequency range, with asterisks showing which
     * sums occurred at least that many times.
     *
     * @param frequencies Map containing the frequency data for each sum
     */
    private static void printHistogramBody(Map<Integer, Integer> frequencies) {
        for (int row = GRAPH_ROWS - 1; row >= 0; row--) {
            int threshold = row * GRAPH_SCALE_UNIT;
            printYAxisLabel(threshold);
            printHistogramRow(frequencies, threshold);
            System.out.println();
        }
    }

    /**
     * Prints the Y-axis label for a given frequency value.
     * Labels are right-aligned with a vertical bar separator.
     *
     * @param value The frequency value to display on the Y-axis
     */
    private static void printYAxisLabel(int value) {
        String label = String.format("%3d|", value);
        System.out.print(label);
    }

    /**
     * Prints a single row of the histogram.
     * An asterisk (*) is printed for each sum that occurred at least
     * as many times as the threshold value.
     *
     * @param frequencies Map containing the frequency data for each sum
     * @param threshold The minimum frequency needed to print an asterisk
     */
    private static void printHistogramRow(Map<Integer, Integer> frequencies, int threshold) {
        frequencies.values().forEach(freq -> 
            System.out.print(freq >= threshold ? "*  " : "   "));
    }

    /**
     * Prints the X-axis of the histogram.
     * Shows all possible sums (2-12) as labels below the histogram.
     */
    private static void printHistogramXAxis() {
        System.out.println("    --------------------------------");
        System.out.print("   ");
        for (int i = MIN_DICE_SUM; i <= MAX_DICE_SUM; i++) {
            System.out.printf(" %-2d", i);
        }
        System.out.println();
    }
}