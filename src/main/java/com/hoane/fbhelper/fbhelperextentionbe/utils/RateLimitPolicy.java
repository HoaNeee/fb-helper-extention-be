package com.hoane.fbhelper.fbhelperextentionbe.utils;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;

@Component
public class RateLimitPolicy {

    // Rule cụ thể đặt trước rule tổng quát
    private final List<RateLimitRule> rules = List.of(

            RateLimitRule.of(
                    "login", "POST", "/api/v1/auth/login",
                    10, Duration.ofMinutes(1)
            ),

            RateLimitRule.of(
                    "register", "POST", "/api/v1/auth/register",
                    10, Duration.ofMinutes(1)
            ),

            RateLimitRule.of(
                    "file", "POST", "/api/v1/files/**",
                    50, Duration.ofMinutes(1)
            ),


            RateLimitRule.of(
                    "admin", "*", "/api/v1/admin/**",
                    100, Duration.ofMinutes(1)
            ),

            RateLimitRule.of(
                    "ping", "*", "/api/v1/ping/**",
                    10, Duration.ofMinutes(1)
            ),

            RateLimitRule.of(
                    "default", "*", "/api/v1/**",
                    150, Duration.ofMinutes(1)
            )
    );

    public Optional<RateLimitRule> resolve(
            HttpServletRequest request
    ) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());

        PathContainer parsedPath = PathContainer.parsePath(path);

//        System.out.println("Resolving rate limit rule...");
//        System.out.println("Uri: " + uri);
//        System.out.println("Context path: " + contextPath);
//        System.out.println("Resolving rate limit rule for request: " + request.getMethod() + " " + path);

        return rules.stream()
                .filter(rule -> rule.matches(
                        request.getMethod(), parsedPath
                ))
                .findFirst();
    }
}
