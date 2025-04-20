package com.highman.assignment2;

// Creates a "set of dice" with two die objects and rolls both of them
public class Dice {
    private Die die1;
    private Die die2;
    int DiceSum;

    public Dice() {
        die1 = new Die();
        die2 = new Die();
    }

    public int getSum() {
        DiceSum = die1.getFaceValue() + die2.getFaceValue();
        return DiceSum;
    }

    public void roll() {
        die1.rollDie(); 
        die2.rollDie(); 
    }
}