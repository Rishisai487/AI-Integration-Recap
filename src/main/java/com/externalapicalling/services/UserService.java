package com.externalapicalling.services;

import com.externalapicalling.models.ProductEmbedding;
import com.externalapicalling.payload.*;
import com.externalapicalling.repository.ProductEmbeddingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class UserService {
    List<String> products = List.of(
            "Apple AirPods Pro wireless earbuds",
            "Samsung Galaxy Buds Bluetooth earbuds",
            "Sony WH-1000XM5 wireless headphones",
            "Nike running shoes",
            "Chicken biryani",
            "Dell wireless keyboard",
            "idly",
            "sambar"
    );
    List<EmbeddingResponse> embeddingResponses=new ArrayList<>();
    List<Double> scores=new ArrayList<>();
    List<Products> productsList=new ArrayList<>();
    EmbeddingRequest embeddingRequest =new EmbeddingRequest();
    @Value("${gemini.api-key}")
    private String apiKey;
    private final ProductEmbeddingRepository productEmbeddingRepository;
    RestClient restClient;
    public UserService(ProductEmbeddingRepository productEmbeddingRepository){
        this.productEmbeddingRepository = productEmbeddingRepository;
        this.restClient=RestClient.create();
    }
    public String getUser(String userPrompt){
        String promptText= """
            Answer the user's question based on the following relevant products if they help ,if not relevant say so .
            User question:%s
            Relevant products:
            """.formatted(userPrompt);
//        System.out.println("USER PROMPT = [" + userPrompt + "]");
        Parts parts=new Parts(userPrompt);
        Contents contents=new Contents();
        contents.setParts(List.of(parts));
        embeddingRequest.setContent(contents);
        EmbeddingResponse embeddingResponseFromQuery= restClient
                .post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-2:embedContent")
                .header("x-goog-api-key", apiKey)
                .body(embeddingRequest)
                .retrieve()
                .body(EmbeddingResponse.class);
        float[] values= new float[embeddingResponseFromQuery.getEmbedding().getValues().size()];
        for (int i=0;i<values.length;i++){
            values[i]=embeddingResponseFromQuery.getEmbedding().getValues().get(i).floatValue();
        }
        List<ProductEmbedding> topResults=productEmbeddingRepository.findSimilarProducts(Arrays.toString(values));
//        System.out.println(new ObjectMapper().writeValueAsString(topResults));
        for(ProductEmbedding productEmbedding:topResults){
            promptText+=productEmbedding.getProductName()+"\n";
        }
        Parts parts1=new Parts(promptText);
        System.out.println(promptText);
        Contents contents1=new Contents(List.of(parts1));
        GeminiRequest geminiRequest=new GeminiRequest(List.of(contents1));
        GeminiResponse geminiResponse=restClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent")
                .header("x-goog-api-key", apiKey)
                .body(geminiRequest)
                .retrieve()
                .body(GeminiResponse.class);
        return geminiResponse.getCandidates()[0].getContent().getParts()[0].getText();
    }
}
