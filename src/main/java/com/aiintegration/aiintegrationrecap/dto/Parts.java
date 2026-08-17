package com.aiintegration.aiintegrationrecap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parts {
    private String text;
    private String thoughtSignature;
    private FunctionCall functionCall;
    private FunctionResponse functionResponse;
}
