package com.example;

import com.example.calculator.CountryMetricCalculator;
import com.example.interfaces.IContentProvider;
import com.example.interfaces.IMetricCalculator;
import com.example.service.GeoIPService;
import com.example.service.GeoIPServiceFactory;

import java.util.ArrayList;
import java.util.List;

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
        IContentProvider contentProvider = createContentProvider(logFilePath);

        // Initialize and use GeoIPService with try-with-resources for automatic closing
        try (GeoIPService geoIPService = GeoIPServiceFactory.createGeoIPService(geoLiteDbPath)) {

            // Create metric calculators and log examiner
            List<IMetricCalculator> calculators = createMetricCalculators(geoIPService);
            LogExaminer logExaminer = new LogExaminer(calculators, contentProvider);

            // Process the log file and print results
            logExaminer.printResults();
        } // GeoIPService is automatically closed here
    }

    /**
     * Creates the content provider for reading log files
     *
     * @param logFilePath Path to the log file
     * @return Initialized content provider
     */
    private static IContentProvider createContentProvider(String logFilePath) {
        return new FileContentProvider(logFilePath);
    }

    /**
     * Creates the list of metric calculators used for log analysis
     *
     * @param geoIPService The GeoIP service to be used by calculators
     * @return List of initialized metric calculators
     */
    private static List<IMetricCalculator> createMetricCalculators(GeoIPService geoIPService) {
        List<IMetricCalculator> calculators = new ArrayList<>();
        calculators.add(new CountryMetricCalculator(geoIPService));
        // TODO: Add other metric calculators when implemented
        return calculators;
    }
}
