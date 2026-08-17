package com.aiintegration.aiintegrationrecap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contents {
    private String role;
    private List<Parts> parts;

}
