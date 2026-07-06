package com.hoane.fbhelper.fbhelperextentionbe.exception;

import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleLoginException(Exception exception) {

        return ApiResponse.fail(401, "username or password is not correct", null);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundResource(ResourceNotFoundException exception) {

        Map<String, String> errs = new HashMap<>();
        errs.put(exception.getName(), exception.getMessage());

        return ApiResponse.fail(404, "Resource not found", errs);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ApiResponse<?>> handleUserExist(UserExistException exception) {

        return ApiResponse.fail(409, "User already exists", null);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleExistResource(ResourceExistsException exception) {
        Map<String, String> errs = new HashMap<>();
        errs.put(exception.getName(), exception.getMessage());
        return ApiResponse.fail(409, "Resource was exist", errs);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleErrors(RuntimeException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("server_error", exception.getMessage());

        return ApiResponse.fail(500, "Server internal error", error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errs = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errs.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return ApiResponse.fail(400, "Bad request", errs);
    }
}
