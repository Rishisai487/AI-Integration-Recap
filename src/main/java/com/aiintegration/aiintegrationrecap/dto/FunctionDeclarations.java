package com.aiintegration.aiintegrationrecap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FunctionDeclarations {
    private String name;
    private String description;
    private Parameters parameters;
}
