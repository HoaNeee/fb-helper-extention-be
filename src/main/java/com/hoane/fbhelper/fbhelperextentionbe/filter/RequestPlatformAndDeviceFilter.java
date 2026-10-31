package com.hoane.fbhelper.fbhelperextentionbe.filter;

import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.RequestPlatform;
import com.hoane.fbhelper.fbhelperextentionbe.utils.Utils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestPlatformAndDeviceFilter extends OncePerRequestFilter {

    private static final String X_REQUEST_PLATFORM = "X-Request-Platform";
    private static final String X_DEVICE_ID = "X-Device-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestPlatform = request.getHeader(X_REQUEST_PLATFORM);
            if (requestPlatform != null) {
                RequestPlatform requestPlatformEnum = Utils.getRequestPlatform(requestPlatform);
                request.setAttribute("requestPlatform", requestPlatformEnum.toString());
            }

            String deviceId = request.getHeader(X_DEVICE_ID);
            if (deviceId != null) {
                request.setAttribute("deviceId", deviceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Do nothing
        }

        filterChain.doFilter(request, response);
    }
}
