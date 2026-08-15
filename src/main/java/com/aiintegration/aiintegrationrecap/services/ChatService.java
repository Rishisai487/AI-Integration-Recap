package com.aiintegration.aiintegrationrecap.services;

import com.aiintegration.aiintegrationrecap.dto.Contents;
import com.aiintegration.aiintegrationrecap.dto.GeminiRequest;
import com.aiintegration.aiintegrationrecap.dto.GeminiResponse;
import com.aiintegration.aiintegrationrecap.dto.Parts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ChatService {
    @Value("${gemini.apiKey}")
    String apiKey;
    private final RestClient restClient;
    public ChatService() {
        this.restClient = RestClient.create();
    }
    public String callGemini(String prompt) {
        Parts parts=new Parts();
        parts.setText(prompt);
        Contents content=new Contents("user",List.of(parts));
        GeminiRequest geminiRequest=new GeminiRequest();
        geminiRequest.getContents().add(content);
        GeminiResponse response=restClient
                .post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent")
                .header("x-goog-api-key",apiKey)
                .body(geminiRequest)
                .retrieve()
                .body(GeminiResponse.class);
        assert response != null;
        return response.getCandidates().getFirst()
                .getContent()
                .getParts().getFirst()
                .getText();
    }
}
