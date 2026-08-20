package com.externalapicalling.services;

import com.externalapicalling.payload.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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
    Parts parts=new Parts();
    Contents contents=new Contents();
    @Value("${gemini.api-key}")
    private String apiKey;
    private String prompt= """
            Answer the user's question based on the following relevant products.
            User question:Dosa
            Relevant products:
            """;
    RestClient restClient;
    public UserService(){
        this.restClient=RestClient.create();
    }
    public String getUser(){
        for(String products : products){
            Parts parts=new Parts(products);
            Contents contents=new Contents();
            contents.setParts(List.of(parts));
            embeddingRequest.setContent(contents);
            EmbeddingResponse embeddingResponse= restClient
                    .post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-2:embedContent")
                    .header("x-goog-api-key", apiKey)
                    .body(embeddingRequest)
                    .retrieve()
                    .body(EmbeddingResponse.class);
            embeddingResponses.add(embeddingResponse);
        }
        parts.setText("Dosa");
        contents.setParts(List.of(parts));
        embeddingRequest.setContent(contents);
        EmbeddingResponse embeddingResponseFromQuery= restClient
                .post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-2:embedContent")
                .header("x-goog-api-key", apiKey)
                .body(embeddingRequest)
                .retrieve()
                .body(EmbeddingResponse.class);
        for(int i=0;i<products.size();i++){
            Double scoreValue=CosineOperation.cosineSimilarity(embeddingResponseFromQuery.getEmbedding().getValues(), embeddingResponses.get(i).getEmbedding().getValues());
            productsList.add(new Products(products.get(i),scoreValue));
        }
        productsList.sort((a,b)->Double.compare(b.getValues(),a.getValues()));
        List<Products> topResults=productsList.subList(0,Math.min(3,productsList.size()));
        for(Products products1:topResults){
            prompt +=" - "+products1.getProduct()+" \n";
            System.out.println(prompt);
        }
        Parts parts1=new Parts(prompt);
        Contents contents1=new Contents(List.of(parts1));
        GeminiRequest geminiRequest=new GeminiRequest(List.of(contents1));
        return restClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent")
                .header("x-goog-api-key", apiKey)
                .body(geminiRequest)
                .retrieve()
                .body(String.class);
    }
}
