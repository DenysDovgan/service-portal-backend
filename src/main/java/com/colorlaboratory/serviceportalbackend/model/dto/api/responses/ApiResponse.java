package com.colorlaboratory.serviceportalbackend.model.dto.api.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Fix ApiResponse usages
@Data
@AllArgsConstructor @NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>("error", message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }
}
