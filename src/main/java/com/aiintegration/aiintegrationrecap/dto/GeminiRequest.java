package com.aiintegration.aiintegrationrecap.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GeminiRequest {
    private List<Contents> contents=new ArrayList<>();
}