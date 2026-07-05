package com.hoane.fbhelper.fbhelperextentionbe.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Map;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private Map<String, String> errors;

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(int status, String message, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(status, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage(message);
        response.setData(data);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Request successful");
        response.setData(data);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<ApiResponse> success(int status, String message) {
        ApiResponse response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<ApiResponse<?>> fail(int status, String message, Map<String, String> errors) {
        ApiResponse<?> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        response.setErrors(errors);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<ApiResponse<?>> fail(String message, Map<String, String> errors) {
        ApiResponse<?> response = new ApiResponse<>();
        response.setStatus(400);
        response.setMessage(message);
        response.setErrors(errors);
        return ResponseEntity.status(400).body(response);
    }

    public static ResponseEntity<ApiResponse<?>> fail(Map<String, String> errors) {
        ApiResponse<?> response = new ApiResponse<>();
        response.setStatus(400);
        response.setMessage("Bad request");
        response.setErrors(errors);
        return ResponseEntity.status(400).body(response);
    }

    public static ResponseEntity<ApiResponse<?>> fail(int status, String message) {
        ApiResponse<?> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        return ResponseEntity.status(status).body(response);
    }

}
