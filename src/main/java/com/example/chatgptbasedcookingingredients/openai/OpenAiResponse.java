package com.example.chatgptbasedcookingingredients.openai;

import java.util.List;

public record OpenAiResponse(
        List<OpenAiChoice> choices
) {
}
