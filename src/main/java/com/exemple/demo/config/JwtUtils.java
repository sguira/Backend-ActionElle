package com.exemple.demo.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${app.secret-key}")
    private String secretKey;

    @Value("${app.expiration-time}")
    private long delay;

    public String generateToken(String username) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);

    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + delay))
                .signWith(getSignkey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getSignkey() {
        byte[] key = secretKey.getBytes();
        return new SecretKeySpec(key, SignatureAlgorithm.HS256.getJcaName());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String userName = extractUsername(token);

        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    public boolean isTokenExpired(String token) {
        return extractExpiDate(token).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiDate(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignkey())
                .parseClaimsJws(token)
                .getBody();
    }

}
