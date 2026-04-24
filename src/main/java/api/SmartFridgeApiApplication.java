package api;

import api.config.FridgeBeansConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Spring Boot entry point for the official HTTP backend.
 * <p>
 * This process serves the Flutter frontend through {@code api.web.*ApiController}. The legacy
 * console/demo entry remains in {@link ui.Main} for backward compatibility.
 */
@SpringBootApplication
@Import(FridgeBeansConfig.class)
public class SmartFridgeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartFridgeApiApplication.class, args);
    }
}
