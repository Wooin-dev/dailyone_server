package com.wooin.dailyone.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    @Value("${jwt.token.expired-time-ms.default}")
    private static Long defaultExpiredTimeMs = 2592000000L;
    private static final String TOKEN_EMAIL_MAP_KEY = "email";

    public static String getEmailFromToken(String token, String key) {
        return extractClaims(token, key).get(TOKEN_EMAIL_MAP_KEY, String.class);
    }


    public static boolean isExpired(String token, String key) {
        Date expiredDate = extractClaims(token, key).getExpiration();
        return expiredDate.before(new Date());
    }

    private static Claims extractClaims(String token, String key) {
        return Jwts.parserBuilder().setSigningKey(getKey(key))
                .build().parseClaimsJws(token).getBody();
    }

    public static String generateToken(String email, String key) {
        return generateToken(email, key, defaultExpiredTimeMs);
    }
    public static String generateToken(String email, String key, long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put(TOKEN_EMAIL_MAP_KEY, email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
