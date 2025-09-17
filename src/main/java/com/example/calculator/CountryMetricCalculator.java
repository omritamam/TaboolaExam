package com.example.calculator;

import com.example.interfaces.IMetricCalculator;
import com.example.model.MetricData;
import com.example.model.Ratio;
import com.example.service.GeoIPService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Metric calculator that computes the distribution of visitor countries from Apache log data.
 */
public class CountryMetricCalculator implements IMetricCalculator {
    private static final Pattern IP_PATTERN = Pattern.compile("^(\\d+\\.\\d+\\.\\d+\\.\\d+).*");
    private final GeoIPService geoIPService;
    private List<String> logLines;

    /**
     * Constructs a CountryMetricCalculator with the specified GeoIP service.
     *
     * @param geoIPService The service used for IP-to-country resolution
     */
    public CountryMetricCalculator(GeoIPService geoIPService) {
        this.geoIPService = geoIPService;
    }

    @Override
    public void loadLines(List<String> lines) {
        this.logLines = lines;
    }

    @Override
    public MetricData computeStats() {
        if (logLines == null || logLines.isEmpty()) {
            return new MetricData("Countries", new HashMap<>(), 0, LocalDateTime.now());
        }

        Map<String, Integer> countryCounts = new HashMap<>();
        int processedLines = 0;

        for (String line : logLines) {
            String ip = extractIPAddress(line);
            if (ip != null) {
                String country = geoIPService.getCountryFromIP(ip);
                countryCounts.put(country, countryCounts.getOrDefault(country, 0) + 1);
                processedLines++;
            }
        }

        // Convert counts to ratios
        Map<String, Ratio> countryDistribution = new HashMap<>();
        for (Map.Entry<String, Integer> entry : countryCounts.entrySet()) {
            double ratio = (double) entry.getValue() / processedLines;
            countryDistribution.put(entry.getKey(), new Ratio(ratio));
        }

        return new MetricData("Countries", countryDistribution, processedLines, LocalDateTime.now());
    }

    /**
     * Extracts the IP address from a log line.
     *
     * @param logLine The log line to parse
     * @return The extracted IP address, or null if not found
     */
    private String extractIPAddress(String logLine) {
        if (logLine == null || logLine.isEmpty()) {
            return null;
        }

        Matcher matcher = IP_PATTERN.matcher(logLine);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
