package com.example.service;

/**
 * Factory class for creating and initializing GeoIPService instances.
 */
public class GeoIPServiceFactory {

    /**
     * Creates and initializes a GeoIPService instance.
     *
     * @param geoLiteDbPath Path to the GeoLite2 database file
     * @return The initialized GeoIPService or null if initialization failed
     */
    public static GeoIPService createGeoIPService(String geoLiteDbPath) {
        GeoIPService geoIPService = new GeoIPService(geoLiteDbPath);

        if (!geoIPService.isInitialized()) {
            System.err.println("Error: GeoIP database not initialized. Please download the database from:");
            System.err.println("https://dev.maxmind.com/geoip/geolite2-free-geolocation-data");
            System.err.println("and place it at: " + geoLiteDbPath);
            return null;
        }

        return geoIPService;
    }
}
