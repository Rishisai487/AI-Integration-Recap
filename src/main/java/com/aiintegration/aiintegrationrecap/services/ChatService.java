package com.aiintegration.aiintegrationrecap.services;

import com.aiintegration.aiintegrationrecap.dto.*;
import com.aiintegration.aiintegrationrecap.tools.ToolService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        // Define Safety Settings
        List<SafetySetting> safetySettings = List.of(
                new SafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT", "BLOCK_NONE"),
                new SafetySetting("HARM_CATEGORY_HATE_SPEECH", "BLOCK_NONE"),
                new SafetySetting("HARM_CATEGORY_HARASSMENT", "BLOCK_NONE"),
                new SafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT", "BLOCK_NONE")
        );
// Attach them to the request payload
        geminiRequest.setSafetySettings(safetySettings);
        geminiRequest.getContents().add(content);
        PropertyDetails propertyDetails1=new PropertyDetails("string");
        Properties properties1=new Properties(Map.of("city",propertyDetails1));
        Properties properties2=new Properties(Map.of());
        Parameters parameters1=new Parameters("object",properties1);
        Parameters parameters2=new Parameters("object",properties2);
        FunctionDeclarations functionDeclarations1=new FunctionDeclarations("getWeather","Retrieves weather of any place if only it exists .As for the existence testing , its your job ",parameters1);
        FunctionDeclarations functionDeclarations2=new FunctionDeclarations("getProducts","Retrieves user shopping cart items, which may include electronics, groceries, health, or personal care products",parameters2);
        Tools tools=new Tools(List.of(functionDeclarations1,functionDeclarations2));
        geminiRequest.getTools().add(tools);
        GeminiResponse response=restClient
                .post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent")
                .header("x-goog-api-key",apiKey)
                .body(geminiRequest)
                .retrieve()
                .body(GeminiResponse.class);
        Contents modelContent=response.getCandidates().getFirst().getContent();
        Parts modelParts=modelContent.getParts().getFirst();
        FunctionCall functionCall=modelParts.getFunctionCall();
        if(functionCall!=null){
            geminiRequest.getContents().add(modelContent);
            String functionCalledFor=functionCall.getName();
            Parts responseParts=new Parts();
            FunctionResponse functionResponse=new FunctionResponse();
            if(functionCalledFor.equals("getWeather")){
                String weatherResponse= ToolService.getWeather(functionCall.getArgs().get("city").toString());
                functionResponse.setName("getWeather");
                functionResponse.setResponse(Map.of("output",weatherResponse));
            }
            else{
                List<String> productsResponse=ToolService.getProducts();
                functionResponse.setName("getProducts");
                functionResponse.setResponse(Map.of("output",productsResponse));
            }
            responseParts.setFunctionResponse(functionResponse);
            Contents responseContent=new Contents();
            responseContent.setRole("function");
            responseContent.setParts(List.of(responseParts));
            geminiRequest.getContents().add(responseContent);
            GeminiResponse geminiResponse= restClient.post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent")
                    .header("x-goog-api-key",apiKey)
                    .body(geminiRequest)
                    .retrieve()
                    .body(GeminiResponse.class);
            if(geminiResponse!=null && !geminiResponse.getCandidates().isEmpty()){
                return geminiResponse.getCandidates().getFirst().getContent().getParts().getFirst().getText();
            }
        }
        return response.getCandidates().getFirst()
                .getContent()
                .getParts().getFirst()
                .getText();
    }
}
