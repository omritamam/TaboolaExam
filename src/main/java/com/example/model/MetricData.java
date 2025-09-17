package com.example.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Record to store metric calculation results.
 */
public record MetricData(
    String metricName,
    Map<String, Ratio> distribution,
    int totalLines,
    LocalDateTime creationTime
) {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(metricName).append(":\n");

        // Sort entries by ratio value in descending order
        distribution.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue().getValue(), e1.getValue().getValue()))
            .forEach(entry -> {
                builder.append(entry.getKey())
                      .append(" - ")
                      .append(entry.getValue())
                      .append("\n");
            });

        return builder.toString();
    }
}
