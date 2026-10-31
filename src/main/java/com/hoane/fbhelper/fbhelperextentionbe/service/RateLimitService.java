package com.hoane.fbhelper.fbhelperextentionbe.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private record RateLimitKey(
            String ruleId,
            String clientId) {
    }

    private record WindowCounter(
            int count,
            long startedAt,
            long windowNanos) {
    }

    private final Map<RateLimitKey, WindowCounter> counter = new ConcurrentHashMap<>();

    public boolean allowRequest(String rule, String clientId, int limit, Duration window) {

        // System.out.println("Allowing request for rule: " + rule + ", clientId: " +
        // clientId + ", limit: " + limit + ", window: " + window);
        // System.out.println("Current counters: " + counter.toString());
        // System.out.println("Parse counter");
        // counter.forEach((k, v) -> {
        // System.out.println(k.ruleId() + " " + k.clientId() + " " + v.count() + " " +
        // v.startedAt() + " " + v.windowNanos());
        // });

        RateLimitKey rateLimitKey = new RateLimitKey(rule, clientId);
        long windowNanos = window.toNanos();

        WindowCounter windowCounter = counter.compute(rateLimitKey, (k, old) -> {
            long now = System.nanoTime();
            if (old == null || now - old.startedAt() >= old.windowNanos()) {
                return new WindowCounter(1, now, windowNanos);
            }

            if (old.count() >= limit) {
                return new WindowCounter(limit + 1, old.startedAt(), windowNanos);
            }

            return new WindowCounter(old.count() + 1, old.startedAt(), windowNanos);
        });

        return windowCounter.count() <= limit;
    }

    @Scheduled(fixedRate = 60_000)
    public void cleanUp() {
        if (counter.isEmpty()) {
            return;
        }
        System.out.println("Cleaning up rate limit counters...");
        long now = System.nanoTime();
        counter.forEach((k, v) -> {
            if (now - v.startedAt() >= v.windowNanos()) {
                counter.remove(k, v);
            }
        });
    }

    public int size() {
        return counter.size();
    }
}
