package com.example.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class GeoIPServiceTest {

    private GeoIPService geoIPService;
    private List<String> logLines;
    private static final Pattern IP_PATTERN = Pattern.compile("^(\\d+\\.\\d+\\.\\d+\\.\\d+).*");

    @BeforeEach
    void setUp() throws IOException {
        // Initialize the real GeoIPService with the MaxMind database
        String dbPath = "src/main/resources/GeoLite2-Country.mmdb";
        geoIPService = new GeoIPService(dbPath);

        // Ensure the database is properly initialized
        assertTrue(geoIPService.isInitialized(), "GeoIP database should be initialized");

        logLines = Files.readAllLines(Paths.get("src/main/resources/logs/short_log_file_for_text.log"));
    }

    @AfterEach
    void tearDown() {
        // Close the GeoIPService to free resources
        if (geoIPService != null) {
            geoIPService.close();
        }
    }

    @Test
    void testGeoIPResolution() {
        // Extract unique IPs from the log file
        Set<String> uniqueIPs = new HashSet<>();
        for (String line : logLines) {
            Matcher matcher = IP_PATTERN.matcher(line);
            if (matcher.find()) {
                uniqueIPs.add(matcher.group(1));
            }
        }

        // Verify we have extracted some IPs
        assertFalse(uniqueIPs.isEmpty(), "Should extract IPs from log file");

        // Test country resolution for each IP
        for (String ip : uniqueIPs) {
            String country = geoIPService.getCountryFromIP(ip);
            assertNotNull(country, "Country should not be null for IP: " + ip);
            System.out.println("IP: " + ip + " resolves to country: " + country);
        }
    }

    @Test
    void testInvalidIP() {
        // Test with invalid IP
        String country = geoIPService.getCountryFromIP("invalid.ip");
        assertEquals("Unknown", country, "Invalid IP should resolve to 'Unknown'");
    }

    @Test
    void testPrivateIP() {
        // Test with private IP ranges
        String country1 = geoIPService.getCountryFromIP("192.168.1.1");
        String country2 = geoIPService.getCountryFromIP("10.0.0.1");
        String country3 = geoIPService.getCountryFromIP("172.16.0.1");

        assertEquals("Unknown", country1, "Private IP should resolve to 'Unknown'");
        assertEquals("Unknown", country2, "Private IP should resolve to 'Unknown'");
        assertEquals("Unknown", country3, "Private IP should resolve to 'Unknown'");
    }
}
