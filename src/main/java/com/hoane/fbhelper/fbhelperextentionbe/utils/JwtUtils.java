package com.hoane.fbhelper.fbhelperextentionbe.utils;


import com.hoane.fbhelper.fbhelperextentionbe.entity.wrapper.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private Long jwtExpired;

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("status", userDetails.getUserStatus())
                .expiration(new Date(System.currentTimeMillis() + jwtExpired))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public Object getPayloadFromJwtToken(String token) {
        try {
            Object payload = Jwts.parser().verifyWith((SecretKey) getSigningKey()).build().parseSignedClaims(token).getPayload();
            return payload;
        } catch (Exception e) {
            //do something
            e.printStackTrace();
        }
        return null;
    }

    public String getUsernameFromJwtToken(String token) {
        String payload = Jwts.parser().verifyWith((SecretKey) getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
        return payload;
    }

    public boolean validateJwtToken(String token) {
        Key key = this.getSigningKey();
        Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
        return true;
    }

}
