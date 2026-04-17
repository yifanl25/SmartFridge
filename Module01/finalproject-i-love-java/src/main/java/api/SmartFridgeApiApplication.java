package api;

import api.config.FridgeBeansConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Starts the Spring Boot API for the Smart Fridge app.
 * Run this class directly or use {@code ./gradlew bootRun}.
 *
 * The console demo still uses {@link ui.Main}.
 */
@SpringBootApplication
@Import(FridgeBeansConfig.class)
public class SmartFridgeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartFridgeApiApplication.class, args);
    }
}