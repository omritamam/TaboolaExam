package com.example;

/**
 * Main entry point for the log analysis application.
 * Contains only constants and delegates execution to ApplicationRunner.
 */
public class Main {
    // File paths as constants
    private static final String LOG_FILE_PATH = "src/apache - full.log";
    private static final String GEO_LITE_DB_PATH = "src/main/resources/GeoLite2-Country.mmdb";

    public static void main(String[] args) {
        // Run the application with the configured paths
        ApplicationRunner.runApplication(LOG_FILE_PATH, GEO_LITE_DB_PATH);
    }
}