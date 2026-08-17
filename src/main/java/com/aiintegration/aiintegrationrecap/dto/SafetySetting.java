package com.aiintegration.aiintegrationrecap.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SafetySetting {
    private String category;  // e.g., "HARM_CATEGORY_SEXUALLY_EXPLICIT"
    private String threshold; // e.g., "BLOCK_NONE" or "BLOCK_LOW_AND_ABOVE"
}
