package com.example.model;

/**
 * Represents a ratio or percentage with a value and a label.
 */
public class Ratio {
    private final double value;

    public Ratio(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%.2f%%", value * 100);
    }
}
