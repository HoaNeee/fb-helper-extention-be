package com.hoane.fbhelper.fbhelperextentionbe.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println("CustomAuthenticationEntryPoint: commence called: " + authException.toString());
        Throwable cause = authException.getCause();
        Exception exceptionToResolve = (cause instanceof Exception) ? (Exception) cause : authException;

        // Nếu trong request attribute có cất lỗi từ JwtFilter thì lấy ra
        if (request.getAttribute("exception") != null) {
            exceptionToResolve = (Exception) request.getAttribute("exception");
        }

        resolver.resolveException(request, response, null, exceptionToResolve);
    }
}
