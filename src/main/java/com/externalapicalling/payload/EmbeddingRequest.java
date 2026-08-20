    package com.externalapicalling.payload;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class EmbeddingRequest {
        private Contents content;
    }