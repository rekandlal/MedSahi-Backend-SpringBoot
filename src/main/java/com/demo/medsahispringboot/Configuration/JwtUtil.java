package com.demo.medsahispringboot.Configuration;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key key;
    private final long expirationMs;

    // In-memory blacklist for logout
    private final Map<String, Long> tokenBlacklist = new ConcurrentHashMap<>();

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    // 🔹 Generate JWT Token with roles
    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 Extract username (email)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 🔹 Extract roles
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    // 🔹 Extract expiration
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 🔹 Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    // 🔹 Token validation
    public boolean validateToken(String token, String username) {
        String extracted = extractUsername(token);
        return (extracted.equals(username) && !isTokenExpired(token) && !isTokenBlacklisted(token));
    }

    // 🔹 Check token expiry
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 🔹 Parse claims safely
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ---------------- Logout / Blacklist Support ----------------

    // Add token to blacklist
    public void blacklistToken(String token) {
        Date exp = extractExpiration(token);
        tokenBlacklist.put(token, exp.getTime());
    }

    // Check if token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        Long expiry = tokenBlacklist.get(token);
        if(expiry == null) return false;

        // Remove expired blacklisted token automatically
        if(System.currentTimeMillis() > expiry){
            tokenBlacklist.remove(token);
            return false;
        }
        return true;
    }
}
