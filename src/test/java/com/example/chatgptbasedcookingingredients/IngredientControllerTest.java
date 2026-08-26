package com.example.chatgptbasedcookingingredients;

import com.example.chatgptbasedcookingingredients.service.IngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @Mock
    private IngredientService ingredientService;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   ", "\t\n"})
    void categorizeIngredient_blankOrNullInput_returnsBadRequest(String ingredient) {
        IngredientController controller = new IngredientController(ingredientService);

        ResponseEntity<String> response = controller.categorizeIngredient(ingredient);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Ingredient must not be blank");
        verifyNoInteractions(ingredientService);
    }

    @Test
    void categorizeIngredient_validInput_returnsServiceResult() {
        when(ingredientService.categorizeIngredient("tomato")).thenReturn("vegan");
        IngredientController controller = new IngredientController(ingredientService);

        ResponseEntity<String> response = controller.categorizeIngredient("tomato");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("vegan");
        verify(ingredientService).categorizeIngredient("tomato");
    }
}
