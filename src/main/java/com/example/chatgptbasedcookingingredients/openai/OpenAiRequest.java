package com.example.chatgptbasedcookingingredients.openai;

import java.util.List;

public record OpenAiRequest(
        String model,
        List<OpenAiMessage> messages
) {
}
