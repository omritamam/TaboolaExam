package com.example;

import com.example.calculator.CountryMetricCalculator;
import com.example.interfaces.IContentProvider;
import com.example.interfaces.IMetricCalculator;
import com.example.service.GeoIPService;
import com.example.service.GeoIPServiceFactory;

import java.util.ArrayList;

/**
 * Responsible for running the log analysis application.
 * Contains the main application logic separated from the entry point.
 */
public class ApplicationRunner {

    /**
     * Runs the log analysis application.
     *
     * @param logFilePath Path to the log file to analyze
     * @param geoLiteDbPath Path to the GeoLite2 database file
     */
    public static void runApplication(String logFilePath, String geoLiteDbPath) {
        // Initialize content provider
        IContentProvider contentProvider = new FileContentProvider(logFilePath);

        var metricCalculators = new ArrayList<IMetricCalculator>(){};

        // Country metric calculator
        GeoIPService geoIPService = GeoIPServiceFactory.createGeoIPService(geoLiteDbPath);
        metricCalculators.add(new CountryMetricCalculator(geoIPService));

        // TODO: Add other metric metricCalculators when implemented

        // Process log file and compute statistics
        LogExaminerManager logExaminerManager = new LogExaminerManager(metricCalculators, contentProvider);
        logExaminerManager.printResults();

        // Dispose metricCalculators (which will handle disposing the GeoIPService)
        logExaminerManager.disposeCalculators();
    }
}
