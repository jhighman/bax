package com.highman.assignment2;
import java.util.Random;

/**
 * Represents a single six-sided die that can be rolled.
 * 
 * This class simulates a standard six-sided die with values from 1 to 6.
 * The die can be rolled to generate a random value, and its current face
 * value can be retrieved or manually set.
 * 
 * @author Jeff Highman
 * @version 1.0
 */
public class Die {
    /** The current face value of the die */
    private int faceValue;

    /**
     * Constructs a new die with an initial face value of 1.
     */
    public Die() {
        faceValue = 1;
    }

    /**
     * Gets the current face value of the die.
     *
     * @return The current face value (1-6)
     */
    public int getFaceValue() {
        return faceValue;
    }

    /**
     * Sets the face value of the die.
     *
     * @param faceValue The new face value to set
     */
    public void setFaceValue(int faceValue) {
        this.faceValue = faceValue;
    }

    /**
     * Rolls the die to generate a random value between 1 and 6.
     * Uses Java's Random class to ensure fair distribution of values.
     */
    public void rollDie() {
        Random random = new Random();
        faceValue = random.nextInt(6) + 1;
    }
}