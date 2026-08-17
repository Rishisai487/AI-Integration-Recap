package com.aiintegration.aiintegrationrecap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionCall {
    private String name;
    private Map<String, Object> args;
}
