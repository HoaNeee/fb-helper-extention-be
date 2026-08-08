package com.hoane.fbhelper.fbhelperextentionbe.exception;

import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.ApiResponseCode;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.modelmapper.MappingException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import javax.security.auth.login.AccountLockedException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleLoginException(Exception exception) {
        return ApiResponse.fail(401, "username or password is not correct", null, ApiResponseCode.INVALID_CREDENTIALS);
    }

    @ExceptionHandler({AuthenticationException.class, SignatureException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(Exception exception) {
        return ApiResponse.fail(403, "Permission denied", null, ApiResponseCode.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<?>> handleExpiredJwtException(Exception exception) {
        return ApiResponse.fail(403, "Expired JWT Token", null, ApiResponseCode.TOKEN_EXPIRED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<?>> handleDisabledException(DisabledException exception) {
        return ApiResponse.fail(403, "User not activated", null, ApiResponseCode.USER_INACTIVE);
    }

    @ExceptionHandler({AccountLockedException.class, LockedException.class})
    public ResponseEntity<ApiResponse<?>> handleAccountLockedException(Exception exception) {
        return ApiResponse.fail(403, "User is locked", null, ApiResponseCode.USER_LOCKED);
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        Map<String, String> errs = new HashMap<>();

        String message = exception.getMessage();

        errs.put("error", message);

        if (message.contains("enums")) {
            errs.put("error", "Field not match");
        }


        return ApiResponse.fail(400, "Bad request", errs);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse<?>> handleMultipartException(MultipartException exception) {
        Map<String, String> errs = new HashMap<>();
        String message = exception.getMessage();
        errs.put("error", message);
        return ApiResponse.fail(400, "Bad request", errs);
    }

    @ExceptionHandler(MappingException.class)
    public ResponseEntity<ApiResponse<?>> handleMappingException(MappingException exception) {
        Map<String, String> errs = new HashMap<>();
        String message = exception.getMessage();
        errs.put("error", message);
        return ApiResponse.fail(400, "Bad request", errs);
    }
}
