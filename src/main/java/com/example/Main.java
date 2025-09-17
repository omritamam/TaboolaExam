package com.example;

import com.example.calculator.CountryMetricCalculator;
import com.example.interfaces.IContentProvider;
import com.example.interfaces.IMetricCalculator;
import com.example.service.GeoIPService;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class that demonstrates the log analysis application.
 */
public class Main {
    public static void main(String[] args) {
        // Path to the log file
        String logFilePath = "src/apache - full.log";

        // Path to the GeoLite2 database file - update this with your actual path
        String geoLiteDbPath = "src/main/resources/GeoLite2-Country.mmdb";

        // Create a content provider for reading the log file
        IContentProvider contentProvider = new FileContentProvider(logFilePath);

        // Initialize GeoIP service for country resolution
        GeoIPService geoIPService = new GeoIPService(geoLiteDbPath);
        if (!geoIPService.isInitialized()) {
            System.err.println("Error: GeoIP database not initialized. Please download the database from:");
            System.err.println("https://dev.maxmind.com/geoip/geolite2-free-geolocation-data");
            System.err.println("and place it at: " + geoLiteDbPath);
            return;
        }

        // Create metric calculators
        List<IMetricCalculator> calculators = new ArrayList<>();
        calculators.add(new CountryMetricCalculator(geoIPService));
        // TODO: Add other metric calculators when implemented

        // Create the log examiner with dependencies
        LogExaminer logExaminer = new LogExaminer(calculators, contentProvider);

        // Process the log file and print results
        logExaminer.printResults();

        // Close resources
        geoIPService.close();
    }
}