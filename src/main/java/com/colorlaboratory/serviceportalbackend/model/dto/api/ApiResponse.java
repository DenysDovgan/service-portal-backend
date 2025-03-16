package com.colorlaboratory.serviceportalbackend.model.dto.api;

import lombok.Data;

import java.util.Map;

@Data
public class ApiResponse {
    public static Map<String, String> errorMessage(String message) {
        return Map.of("error", message);
    }

    public static Map<String, String> message(String message) {
        return  Map.of("message", message);}
}
