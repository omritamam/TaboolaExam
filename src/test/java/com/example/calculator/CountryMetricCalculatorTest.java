package com.example.calculator;

import com.example.model.MetricData;
import com.example.model.Ratio;
import com.example.service.GeoIPService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Disabled("Temporarily disabled until Mockito configuration issues are resolved")
@ExtendWith(MockitoExtension.class)
class CountryMetricCalculatorTest {

    @Mock
    private GeoIPService geoIPService;

    private CountryMetricCalculator calculator;
    private List<String> logLines;

    @BeforeEach
    void setUp() throws IOException {
        // Use the mocked GeoIPService
        calculator = new CountryMetricCalculator(geoIPService);

        // Load the sample log file from the new location
        logLines = Files.readAllLines(Paths.get("src/main/resources/logs/short_log_file_for_text.log"));

        // Setup mock behavior for common IPs in the log file
        when(geoIPService.getCountryFromIP("65.34.248.51")).thenReturn("United States");
        when(geoIPService.getCountryFromIP("50.97.37.217")).thenReturn("United States");
        when(geoIPService.getCountryFromIP("184.73.21.14")).thenReturn("United States");
        when(geoIPService.getCountryFromIP("82.166.148.154")).thenReturn("Belgium");
        when(geoIPService.getCountryFromIP("66.249.75.75")).thenReturn("United States");
        when(geoIPService.getCountryFromIP("71.51.154.195")).thenReturn("United States");
        when(geoIPService.getCountryFromIP("180.76.5.154")).thenReturn("China");
        when(geoIPService.getCountryFromIP("180.76.5.156")).thenReturn("China");
        when(geoIPService.getCountryFromIP("66.249.75.147")).thenReturn("United States");
        when(geoIPService.getCountryFromIP("180.76.5.159")).thenReturn("China");
        when(geoIPService.getCountryFromIP("50.16.97.196")).thenReturn("United States");
        when(geoIPService.getCountryFromIP("23.22.222.205")).thenReturn("United States");
        when(geoIPService.getCountryFromIP("75.98.9.254")).thenReturn("United States");
        when(geoIPService.getCountryFromIP("72.14.199.75")).thenReturn("United States");
        when(geoIPService.getCountryFromIP("108.246.244.234")).thenReturn("United States");
        when(geoIPService.getCountryFromIP("31.172.30.4")).thenReturn("Russia");

        // Default response for any other IP
        when(geoIPService.getCountryFromIP(anyString())).thenReturn("Unknown");
    }

    @Test
    void testComputeStats() {
        // Load the log lines into the calculator
        calculator.loadLines(logLines);

        // Compute the statistics
        MetricData result = calculator.computeStats();

        // Verify the result
        assertNotNull(result);
        assertEquals("Countries", result.metricName());
        assertEquals(logLines.size(), result.totalLines());

        // Verify the distribution contains data
        assertFalse(result.distribution().isEmpty(), "Country distribution should not be empty");

        // Verify that the sum of all ratios equals 1.0 (100%)
        double sumOfRatios = result.distribution().values().stream()
                .mapToDouble(Ratio::getValue)
                .sum();
        assertEquals(1.0, sumOfRatios, 0.0001);

        // Verify expected countries are in the distribution
        assertTrue(result.distribution().containsKey("United States"));
        assertTrue(result.distribution().containsKey("Belgium"));
        assertTrue(result.distribution().containsKey("China"));
        assertTrue(result.distribution().containsKey("Russia"));
    }

    @Test
    void testEmptyLogLines() {
        // Test with empty log lines
        calculator.loadLines(List.of());
        MetricData result = calculator.computeStats();

        assertNotNull(result);
        assertEquals("Countries", result.metricName());
        assertEquals(0, result.totalLines());
        assertTrue(result.distribution().isEmpty());
    }

    /**
     * Tests the IP address extraction functionality from log lines
     * using reflection to access the private extractIPAddress method.
     */
    @Test
    void testIPAddressExtraction() throws Exception {
        // Get access to the private extractIPAddress method
        Method extractIPMethod = CountryMetricCalculator.class.getDeclaredMethod("extractIPAddress", String.class);
        extractIPMethod.setAccessible(true);

        // Test IP extraction with various log lines from the sample file
        String[] testLines = {
            "65.34.248.51 - - [20/Jan/2013:04:33:29 -0600] \"GET /?utm_source=Contextin&utm_term=E4AFE73EA95769781618402254&utm_campaign=19056&utm_content=&utm_medium=0 HTTP/1.1\" 200 9983 \"-\" \"Mozilla/5.0 (iPad; CPU OS 6_0_1 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A523 Safari/8536.25\" 625 10246 - 233945",
            "50.97.37.217 - - [20/Jan/2013:05:10:21 -0600] \"POST /wp-cron.php?doing_wp_cron=1358680221.2960379123687744140625 HTTP/1.0\" 200 - \"-\" \"WordPress/3.5; http://50.97.37.217\" 275 190 - 517683",
            "184.73.21.14 - - [20/Jan/2013:05:10:21 -0600] \"GET / HTTP/1.0\" 200 9983 \"-\" \"Mozilla/5.0 (compatible; Windows; U; Windows NT 6.2; WOW64; en-US; rv:12.0) Gecko/20120403211507 Firefox/12.0\" 308 10198 - 664093",
            "82.166.148.154 - - [20/Jan/2013:06:56:32 -0600] \"GET /?utm_source=Contextin&utm_term=_ HTTP/1.1\" 200 9983 \"-\" \"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:15.0) Gecko/20100101 Firefox/15.0.1\" 503 10246 - 139430",
            "Invalid log line with no IP",
            ""
        };

        String[] expectedIPs = {
            "65.34.248.51",
            "50.97.37.217",
            "184.73.21.14",
            "82.166.148.154",
            null,
            null
        };

        for (int i = 0; i < testLines.length; i++) {
            String extractedIP = (String) extractIPMethod.invoke(calculator, testLines[i]);
            assertEquals(expectedIPs[i], extractedIP,
                    "IP extraction failed for log line " + i + ": " + testLines[i]);
        }
    }

    @Test
    void testSingleLogLine() {
        // Test the IP extraction with a single log line
        String logLine = "65.34.248.51 - - [20/Jan/2013:04:33:29 -0600] \"GET /?utm_source=Contextin&utm_term=E4AFE73EA95769781618402254&utm_campaign=19056&utm_content=&utm_medium=0 HTTP/1.1\" 200 9983 \"-\" \"Mozilla/5.0 (iPad; CPU OS 6_0_1 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A523 Safari/8536.25\" 625 10246 - 233945";

        // Load single line and check if it processes correctly
        calculator.loadLines(List.of(logLine));
        MetricData result = calculator.computeStats();

        assertNotNull(result);
        assertEquals(1, result.totalLines());

        // Verify the specific country for this IP
        assertEquals(1, result.distribution().size());
        assertTrue(result.distribution().containsKey("United States"));

        // The ratio for the single country should be 1.0 (100%)
        double ratio = result.distribution().get("United States").getValue();
        assertEquals(1.0, ratio);
    }
}
