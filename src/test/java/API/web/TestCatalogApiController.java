package API.web;

import api.web.CatalogApiController;
import controller.CatalogController;
import model.FoodCatalogEntry;
import model.FoodCategory;
import service.FoodCatalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CatalogApiController.
 * Directly instantiates the controller without starting a Spring server.
 */
public class TestCatalogApiController {

    private CatalogApiController catalogApiController;

    @BeforeEach
    void setUp() {
        FoodCategory cat = new FoodCategory("c1", "Dairy", "milk");
        FoodCatalog foodCatalog = new FoodCatalog(List.of(
                new FoodCatalogEntry("Milk", 7, cat),
                new FoodCatalogEntry("Mango", 5, cat)));
        CatalogController catalogController = new CatalogController(foodCatalog);
        catalogApiController = new CatalogApiController(catalogController);
    }

    /**
     * Tests that a matching prefix returns 200 OK with correct results.
     */
    @Test
    void testSuggestionsReturnsMatchingResults() {
        ResponseEntity<List<FoodCatalogEntry>> response =
                catalogApiController.suggestions("Mil");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Milk", response.getBody().get(0).getFoodName());
    }



    /**
     * Tests that a non-matching prefix returns 200 OK with empty list.
     */
    @Test
    void testSuggestionsReturnsEmptyForNonMatchingPrefix() {
        ResponseEntity<List<FoodCatalogEntry>> response =
                catalogApiController.suggestions("xyz");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    /**
     * Tests that null prefix returns 400 Bad Request.
     * Covers the null-check branch in suggestions().
     */
    @Test
    void testSuggestionsReturnsBadRequestForNullPrefix() {
        ResponseEntity<List<FoodCatalogEntry>> response =
                catalogApiController.suggestions(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}