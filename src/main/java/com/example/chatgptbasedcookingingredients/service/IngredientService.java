package com.example.chatgptbasedcookingingredients.service;

import com.example.chatgptbasedcookingingredients.openai.OpenAiMessage;
import com.example.chatgptbasedcookingingredients.openai.OpenAiRequest;
import com.example.chatgptbasedcookingingredients.openai.OpenAiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class IngredientService {

    private final RestClient restClient;

    public IngredientService(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(
                        "Authorization",
                        "Bearer " + System.getenv("OPENAI_API_KEY")
                )
                .build();
    }

    public String categorizeIngredient(String ingredient) {

        OpenAiRequest request = new OpenAiRequest(
                "gpt-5-mini",
                List.of(
                        new OpenAiMessage(
                                "user",
                                "Classify the ingredient '" + ingredient +
                                        "' as exactly one of these values: vegan, vegetarian, regular. " +
                                        "Return only the category."
                        )
                )
        );

        OpenAiResponse response = restClient
                .post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(OpenAiResponse.class);

        if (response == null || response.choices().isEmpty()) {
            throw new IllegalStateException("No response from OpenAI");
        }

        return response
                .choices()
                .getFirst()
                .message()
                .content()
                .trim();
    }
}
