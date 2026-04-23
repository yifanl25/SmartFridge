package api.web;

import api.dto.PreferenceRequest;
import controller.PreferenceController;
import model.HealthGoal;
import model.Preference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the user's health goal preference.
 * <p>
 * Exposes two endpoints:
 * <ul>
 *   <li>GET /api/preference — returns the current preference</li>
 *   <li>PUT /api/preference — saves a new health goal selection</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/preference")
public class PreferenceApiController {

    private final PreferenceController preferenceController;

    /**
     * Constructs the controller with the required preference controller.
     *
     * @param preferenceController manages reading and saving user preferences
     */
    public PreferenceApiController(PreferenceController preferenceController) {
        this.preferenceController = preferenceController;
    }

    /**
     * Returns the current user preference.
     * <p>
     * Called when the preference page loads to display the currently selected health goal.
     *
     * @return the current {@link Preference}
     */
    @GetMapping
    public Preference get() {
        return preferenceController.getPreference();
    }

    /**
     * Saves the user's selected health goal.
     * <p>
     * Accepts a JSON body with a {@code healthGoal} field. The value must match
     * one of the valid {@link HealthGoal} enum values (case-insensitive):
     * {@code MUSCLE_BUILDING}, {@code FAT_LOSS}, or {@code BLOOD_SUGAR_CARE}.
     * <p>
     * Returns 400 Bad Request if the body is null, the goal field is blank,
     * or the goal string does not match any valid {@link HealthGoal}.
     *
     * @param body the request body containing the selected health goal string
     * @return 200 with the updated {@link Preference}, or 400 if input is invalid
     */
    @PutMapping
    public ResponseEntity<Preference> save(@RequestBody PreferenceRequest body) {
        if (body == null || body.getHealthGoal() == null || body.getHealthGoal().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            HealthGoal goal = HealthGoal.valueOf(body.getHealthGoal().trim().toUpperCase());
            return ResponseEntity.ok(preferenceController.savePreference(goal));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
