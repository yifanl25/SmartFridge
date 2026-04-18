package api.web;

import api.dto.PreferenceRequest;
import controller.PreferenceController;
import model.HealthGoal;
import model.Preference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Handles the health preference used for recipe recommendations.
 */
@RestController
@RequestMapping("/api/preference")
public class PreferenceApiController {

    private final PreferenceController preferenceController;

    public PreferenceApiController(PreferenceController preferenceController) {
        this.preferenceController = preferenceController;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> get() {
        Preference preference = preferenceController.getPreference();

        if (preference == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                Map.of("healthGoal", preference.getHealthGoal().name())
        );
    }

    @PutMapping
    public ResponseEntity<Void> save(@RequestBody PreferenceRequest body) {
        if (body == null || body.getHealthGoal() == null || body.getHealthGoal().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            HealthGoal goal = HealthGoal.valueOf(body.getHealthGoal().trim().toUpperCase());
            preferenceController.savePreference(goal);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> clear() {
        preferenceController.clearPreference();
        return ResponseEntity.noContent().build();
    }
}