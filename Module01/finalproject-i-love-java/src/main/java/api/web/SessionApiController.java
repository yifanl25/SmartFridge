package api.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.IGroceryService;
import service.IInventoryService;
import service.IPreferenceService;
import service.IRecommendationService;
import service.SessionReset;

/**
 * Resets the in-memory session used by the demo.
 */
@RestController
@RequestMapping("/api/session")
public class SessionApiController {

    private final IPreferenceService preferenceService;
    private final IInventoryService inventoryService;
    private final IRecommendationService recommendationService;
    private final IGroceryService groceryService;

    public SessionApiController(
            IPreferenceService preferenceService,
            IInventoryService inventoryService,
            IRecommendationService recommendationService,
            IGroceryService groceryService) {
        this.preferenceService = preferenceService;
        this.inventoryService = inventoryService;
        this.recommendationService = recommendationService;
        this.groceryService = groceryService;
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {
        SessionReset.clearAll(
                preferenceService,
                inventoryService,
                recommendationService,
                groceryService
        );
        return ResponseEntity.noContent().build();
    }
}