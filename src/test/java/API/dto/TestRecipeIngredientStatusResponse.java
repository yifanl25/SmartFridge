package API.dto;

import api.dto.RecipeIngredientStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RecipeIngredientStatusResponse DTO.
 * Verifies all getters and setters work correctly.
 */
public class TestRecipeIngredientStatusResponse {

    private RecipeIngredientStatusResponse response;

    @BeforeEach
    void setUp() {
        response = new RecipeIngredientStatusResponse();
    }

    @Test
    void testSetAndGetName() {
        response.setName("Egg");
        assertEquals("Egg", response.getName());
    }


}