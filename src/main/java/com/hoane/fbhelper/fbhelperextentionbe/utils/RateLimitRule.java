package com.hoane.fbhelper.fbhelperextentionbe.utils;


import java.time.Duration;

import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public record RateLimitRule(
        String id,
        String method,
        PathPattern pattern,
        int limit,
        Duration window
) {
    public static RateLimitRule of(
            String id,
            String method,
            String path,
            int limit,
            Duration window
    ) {
        return new RateLimitRule(
                id,
                method,
                PathPatternParser.defaultInstance.parse(path),
                limit,
                window
        );
    }

    public boolean matches(String requestMethod, PathContainer path) {
        return (method.equals("*")
                || method.equalsIgnoreCase(requestMethod))
                && pattern.matches(path);
    }
}

