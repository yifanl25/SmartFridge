package API.config;

import api.config.CorsConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CorsConfig.
 * Verifies the configuration class can be instantiated
 * and addCorsMappings() runs without throwing exceptions.
 */
public class TestCorsConfig {

    /**
     * Tests that CorsConfig can be instantiated without errors.
     */
    @Test
    void testCorsConfigInstantiatesWithoutException() {
        assertDoesNotThrow(() -> new CorsConfig());
    }

    /**
     * Tests that addCorsMappings() runs without throwing exceptions.
     * Uses a no-op CorsRegistry stub since the real one requires Spring context.
     */
    @Test
    void testAddCorsMappingsDoesNotThrow() {
        CorsConfig config = new CorsConfig();
        assertDoesNotThrow(() -> config.addCorsMappings(new org.springframework.web.servlet.config.annotation.CorsRegistry()));
    }
}