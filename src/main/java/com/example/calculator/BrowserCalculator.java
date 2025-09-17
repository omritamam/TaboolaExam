//package com.example.calculator;
//
//import com.example.model.MetricData;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import com.example.interfaces.IMetricCalculator;
//import com.example.model.MetricData;
//
//public class BrowserCalculator implements IMetricCalculator {
//
//    private List<String> _lines;
//
//    private final List<String> knownBrowsers = List.of("Chrome", "Firefox", "Safari", "Edge", "Opera", "Internet Explorer");
//    private final Pattern browserPattern = Pattern.compile(".*(" + String.join("|", knownBrowsers) + ").*");
//
//    @Override
//    public void loadLines(List<String> lines) {
//        _lines = lines;
//    }
//
//    @Override
//    public MetricData computeStats() {
//        var res = new HashMap<String, Integer>();
//        if (_lines == null || _lines.isEmpty()) {
//            return new MetricData("Countries", new HashMap<>(), 0, LocalDateTime.now());
//        }
//
//        Map<String, Integer> browserCounts = new HashMap<>();
//        int processedLines = 0;
//
//        for (String line : _lines) {
//            String browser = extractBrowser(line);
//            if (browser != null) {
//                browserCounts.put(browser, browserCounts.getOrDefault(browser, 0) + 1);
//                processedLines++;
//            }
//        }
//        // Implementation for computing stats
//        return new MetricData();
//    }
//
//    private Optional<String> extractBrowserAddress(String logLine) {
//        if (logLine == null || logLine.isEmpty()) {
//            return null;
//        }
//        for (String browser : knownBrowsers) {
//
//        }
//        // create regex of a string that contains ine of known browsers
//
//
//        return null;
//    }
//}
