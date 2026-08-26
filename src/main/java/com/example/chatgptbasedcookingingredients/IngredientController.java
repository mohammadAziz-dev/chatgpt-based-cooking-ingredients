package com.example.chatgptbasedcookingingredients;

import com.example.chatgptbasedcookingingredients.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    ResponseEntity<String> categorizeIngredient(@RequestBody(required = false) String ingredient) {
        if (ingredient == null || ingredient.isBlank()) {
            return ResponseEntity.badRequest().body("Ingredient must not be blank");
        }
        return ResponseEntity.ok(ingredientService.categorizeIngredient(ingredient));
    }
}