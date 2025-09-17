package com.example.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Service that resolves IP addresses to countries using the MaxMind GeoLite2 database.
 * This service works offline once the database file is downloaded.
 */
public class GeoIPService {
    private static final String UNKNOWN_COUNTRY = "Unknown";
    private final DatabaseReader reader;
    private boolean isInitialized = false;

    /**
     * Constructs a GeoIPService with the specified database file path.
     *
     * @param dbPath The path to the MaxMind GeoLite2 Country database file
     */
    public GeoIPService(String dbPath) {
        DatabaseReader tempReader = null;

        try {
            File database = new File(dbPath);
            if (database.exists()) {
                tempReader = new DatabaseReader.Builder(database).build();
                isInitialized = true;
            } else {
                System.err.println("GeoIP database file not found at: " + dbPath);
                System.err.println("Please download the MaxMind GeoLite2 Country database from https://dev.maxmind.com/geoip/geolite2-free-geolocation-data");
            }
        } catch (IOException e) {
            System.err.println("Error initializing GeoIP database: " + e.getMessage());
        }

        reader = tempReader;
    }

    /**
     * Resolves an IP address to a country name.
     *
     * @param ipAddress The IP address to resolve
     * @return The country name, or "Unknown" if the resolution fails
     */
    public String getCountryFromIP(String ipAddress) {
        if (!isInitialized || reader == null) {
            return UNKNOWN_COUNTRY;
        }

        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            CountryResponse response = reader.country(address);
            return response.getCountry().getName();
        } catch (UnknownHostException e) {
            System.err.println("Invalid IP address: " + ipAddress);
        } catch (AddressNotFoundException e) {
            // This is expected for private/local IPs, no need to log
        } catch (GeoIp2Exception | IOException e) {
            System.err.println("Error looking up country for IP " + ipAddress + ": " + e.getMessage());
        }

        return UNKNOWN_COUNTRY;
    }

    /**
     * Checks if the service is properly initialized with a database.
     *
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Closes the database reader to free resources.
     */
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.err.println("Error closing GeoIP database: " + e.getMessage());
            }
        }
    }
}
