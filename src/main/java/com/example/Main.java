package com.example;

import com.example.interfaces.IContentProvider;
import com.example.interfaces.IMetricCalculator;
import com.example.model.MetricData;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class that demonstrates the log analysis application.
 */
public class Main {
    public static void main(String[] args) {
        // Path to the log file
        String logFilePath = "src/apache.log";

        // Create a content provider for reading the log file
        IContentProvider contentProvider = new FileContentProvider(logFilePath);

        // Create metric calculators (to be implemented)
        List<IMetricCalculator> calculators = new ArrayList<>();
        // TODO: Add metric calculators when implemented

        // Create the log examiner with dependencies
        LogExaminer logExaminer = new LogExaminer(calculators, contentProvider);

        // Process the log file and print results
        logExaminer.printResults();
    }
}