package com.hoane.fbhelper.fbhelperextentionbe.utils;

import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.LoginLocation;
import com.hoane.fbhelper.fbhelperextentionbe.entity.wrapper.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {


    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(Constant.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("role", userDetails.getRole())
                .claim("status", userDetails.getUserStatus())
                .expiration(new Date(System.currentTimeMillis() + Constant.JWT_EXPIRATION))
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
        Jwts.parser().verifyWith((SecretKey) getSigningKey()).build().parseSignedClaims(token);
        return true;

    }

}
