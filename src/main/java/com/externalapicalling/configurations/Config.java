package com.externalapicalling.configurations;

import com.externalapicalling.models.ProductEmbedding;
import com.externalapicalling.payload.Contents;
import com.externalapicalling.payload.EmbeddingRequest;
import com.externalapicalling.payload.EmbeddingResponse;
import com.externalapicalling.payload.Parts;
import com.externalapicalling.repository.ProductEmbeddingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration
public class Config {
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
    @Value("${gemini.api-key}")
    private String apiKey;
    EmbeddingRequest embeddingRequest = new EmbeddingRequest();
    private final ProductEmbeddingRepository productEmbeddingRepository;
    RestClient restClient;

    public Config(ProductEmbeddingRepository productEmbeddingRepository) {
        this.productEmbeddingRepository = productEmbeddingRepository;
        this.restClient = RestClient.create();
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            for (String products : products) {
                if(productEmbeddingRepository.existsByProductName(products)){
                    continue;
                }
                Parts parts = new Parts(products);
                Contents contents = new Contents();
                contents.setParts(List.of(parts));
                embeddingRequest.setContent(contents);
                EmbeddingResponse embeddingResponse = restClient
                        .post()
                        .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-2:embedContent")
                        .header("x-goog-api-key", apiKey)
                        .body(embeddingRequest)
                        .retrieve()
                        .body(EmbeddingResponse.class);
                float[] values = new float[embeddingResponse.getEmbedding().getValues().size()];
                for (int i = 0; i < values.length; i++) {
                    values[i] = embeddingResponse.getEmbedding().getValues().get(i).floatValue();
                }
                ProductEmbedding productEmbedding = new ProductEmbedding(products, values);
                productEmbeddingRepository.save(productEmbedding);
            }
        };
    }
}
