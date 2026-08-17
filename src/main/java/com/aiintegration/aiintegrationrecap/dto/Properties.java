package com.aiintegration.aiintegrationrecap.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Properties {
    @JsonValue
    private Map<String,PropertyDetails> fields;
}
