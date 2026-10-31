package com.hoane.fbhelper.fbhelperextentionbe.filter;

import com.hoane.fbhelper.fbhelperextentionbe.service.RateLimitService;
import com.hoane.fbhelper.fbhelperextentionbe.utils.RateLimitPolicy;
import com.hoane.fbhelper.fbhelperextentionbe.utils.RateLimitRule;
import com.hoane.fbhelper.fbhelperextentionbe.utils.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final RateLimitPolicy rateLimitPolicy;

    public RateLimitFilter(RateLimitService rateLimitService, RateLimitPolicy rateLimitPolicy) {
        this.rateLimitService = rateLimitService;
        this.rateLimitPolicy = rateLimitPolicy;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RateLimitRule rule = rateLimitPolicy.resolve(request).orElse(null);

        if (rule == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String ipAddress = RequestUtils.getClientIp(request);
        String deviceId = RequestUtils.getDeviceId(request);

        String key = ipAddress + ":" + deviceId;

        boolean allow = rateLimitService.allowRequest(
                rule.id(),
                key,
                rule.limit(),
                rule.window()
        );

        if (!allow) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Too Many Requests");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
