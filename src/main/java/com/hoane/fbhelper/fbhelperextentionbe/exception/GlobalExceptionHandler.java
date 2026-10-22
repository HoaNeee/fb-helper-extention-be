package com.hoane.fbhelper.fbhelperextentionbe.exception;

import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.ApiResponseCode;
import com.hoane.fbhelper.fbhelperextentionbe.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.modelmapper.MappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.security.auth.login.AccountLockedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleExceptionMethodNotSupported(HttpRequestMethodNotSupportedException exception) {

        return ApiResponse.fail(400, "Request is not supported");
    }


    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ApiResponse<?>> handleNotFound(Exception ex) {
        return ApiResponse.fail(404, "Endpoint not found");

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<?>> handleLoginException(Exception exception) {
        return ApiResponse.fail(401, "username or password is not correct", null, ApiResponseCode.INVALID_CREDENTIALS);
    }

    @ExceptionHandler({AccountLockedException.class, LockedException.class})
    public ResponseEntity<ApiResponse<?>> handleAccountLockedException(Exception exception) {
        return ApiResponse.fail(403, "User is locked", null, ApiResponseCode.USER_LOCKED);
    }

    @ExceptionHandler({AuthenticationException.class, SignatureException.class})
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(Exception exception) {
        return ApiResponse.fail(401, "Invalid token or authentication failed", null, ApiResponseCode.INVALID_CREDENTIALS);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<?>> handleExpiredJwtException(Exception exception) {
        return ApiResponse.fail(401, "Expired JWT Token", null, ApiResponseCode.TOKEN_EXPIRED);
    }

    @ExceptionHandler({InsufficientAuthenticationException.class, AccessDeniedException.class})
    public ResponseEntity<ApiResponse<?>> handleInsufficientAuthentication(InsufficientAuthenticationException exception) {
        return ApiResponse.fail(401, "Authentication required", null, ApiResponseCode.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<?>> handleDisabledException(DisabledException exception) {
        return ApiResponse.fail(403, "User not activated", null, ApiResponseCode.USER_INACTIVE);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthorizationException(Exception exception) {
        return ApiResponse.fail(403, "Permission denied", null, ApiResponseCode.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundResource(ResourceNotFoundException exception) {

        Map<String, String> errs = new HashMap<>();
        errs.put(exception.getName(), exception.getMessage());

        return ApiResponse.fail(404, "Resource not found", errs, ApiResponseCode.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<?>> handleConflict(ConflictException exception) {

        return ApiResponse.fail(409, exception.getMessage());
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<ApiResponse<?>> handleUserExist(UserExistException exception) {

        return ApiResponse.fail(409, "User already exists");
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleExistResource(ResourceExistsException exception) {
        Map<String, String> errs = new HashMap<>();
        errs.put(exception.getName(), exception.getMessage());
        return ApiResponse.fail(409, "Resource was exist", errs);
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleErrors(RuntimeException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("server_error", exception.getMessage());

        return ApiResponse.fail(500, "Server internal error", error);
    }
}
