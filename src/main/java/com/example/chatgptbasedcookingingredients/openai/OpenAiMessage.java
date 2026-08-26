package com.example.chatgptbasedcookingingredients.openai;

public record OpenAiMessage(
        String role,
        String content
) {
}
