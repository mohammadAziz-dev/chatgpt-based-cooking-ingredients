package com.example.chatgptbasedcookingingredients;

import com.example.chatgptbasedcookingingredients.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    String categorizeIngredient(@RequestBody String ingredient) {

        return ingredientService.categorizeIngredient(ingredient);
    }
}