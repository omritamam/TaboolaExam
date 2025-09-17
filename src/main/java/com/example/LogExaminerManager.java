package com.example;

import com.example.interfaces.IContentProvider;
import com.example.interfaces.IMetricCalculator;
import com.example.model.MetricData;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class that processes log data using injected metric calculators.
 */
public class LogExaminerManager {
    private final List<IMetricCalculator> metricCalculators;
    private final IContentProvider contentProvider;

    /**
     * Constructor for LogExaminer with dependency injection.
     *
     * @param metricCalculators List of metric calculators to process the log data
     * @param contentProvider Content provider to read log data
     */
    public LogExaminerManager(List<IMetricCalculator> metricCalculators, IContentProvider contentProvider) {
        this.metricCalculators = metricCalculators;
        this.contentProvider = contentProvider;
    }

    /**
     * Processes the log file and computes statistics using the metric calculators.
     *
     * @return List of MetricData results from all calculators
     */
    public List<MetricData> processLogFile() {
        // Read all log lines from the content provider
        List<String> logLines = contentProvider.readContent();

        // Load the lines into each calculator
        for (IMetricCalculator calculator : metricCalculators) {
            calculator.loadLines(logLines);
        }

        // Compute statistics using each calculator
        List<MetricData> results = new ArrayList<>();
        for (IMetricCalculator calculator : metricCalculators) {
            results.add(calculator.computeStats());
        }

        return results;
    }

    /**
     * Prints the computed metrics in a formatted way.
     */
    public void printResults() {
        List<MetricData> results = processLogFile();
        for (MetricData data : results) {
            System.out.println(data);
            System.out.println(); // Add empty line between metrics
        }
    }

    /**
     * Disposes all metric calculators to release resources.
     */
    public void disposeCalculators() {
        for (IMetricCalculator calculator : metricCalculators) {
            calculator.dispose();
        }
    }
}
