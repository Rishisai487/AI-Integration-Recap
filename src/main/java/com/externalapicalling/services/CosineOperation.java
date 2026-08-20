package com.externalapicalling.services;

import java.util.List;

public class CosineOperation {
    public static double cosineSimilarity(
            List<Double> a,
            List<Double> b
    ) {
        double dotProduct = 0;
        double magnitudeA = 0;
        double magnitudeB = 0;

        for (int i = 0; i < a.size(); i++) {
            dotProduct += a.get(i) * b.get(i);

            magnitudeA += a.get(i) * a.get(i);
            magnitudeB += b.get(i) * b.get(i);
        }

        magnitudeA = Math.sqrt(magnitudeA);
        magnitudeB = Math.sqrt(magnitudeB);

        return dotProduct / (magnitudeA * magnitudeB);
    }
}
