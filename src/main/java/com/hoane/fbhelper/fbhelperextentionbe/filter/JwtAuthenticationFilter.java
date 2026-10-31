package com.hoane.fbhelper.fbhelperextentionbe.filter;


import com.hoane.fbhelper.fbhelperextentionbe.exception.CustomAuthenticationEntryPoint;
import com.hoane.fbhelper.fbhelperextentionbe.service.CustomUserDetailsService;
import com.hoane.fbhelper.fbhelperextentionbe.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                boolean isValidateSuccess = jwtUtils.validateJwtToken(token);
                if (isValidateSuccess) {
                    String username = jwtUtils.getUsernameFromJwtToken(token);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (ExpiredJwtException | SignatureException e) {
            request.setAttribute("exception", e);
//            authenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("Invalid token", e));
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            request.setAttribute("exception", e);
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
