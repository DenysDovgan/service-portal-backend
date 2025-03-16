package com.colorlaboratory.serviceportalbackend.model.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ApiResponse {
    private String message;

    public static Map<String, String> getErrorMessage(String message) {
        return Map.of("error", message);
    }
}
