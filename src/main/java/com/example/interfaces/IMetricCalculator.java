package com.example.interfaces;

import java.util.List;
import com.example.model.MetricData;

/**
 * Interface for metric calculators that process log lines and compute statistics.
 */
public interface IMetricCalculator {
    /**
     * Loads log lines for processing.
     *
     * @param lines List of log lines to process
     */
    void loadLines(List<String> lines);

    /**
     * Computes statistics from the loaded lines.
     *
     * @return MetricData with the computed statistics
     */
    MetricData computeStats();
}
