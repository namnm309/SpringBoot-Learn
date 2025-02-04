package com.nofear.nac.services;

import com.nofear.nac.exception.JwtValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTServiceImpl {

    @Value("${authorization.security.jwt.secret-key}")
    private String secretKey;

    @Value("${authorization.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${authorization.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(String token)  {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)  {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String subject) {
        return generateToken(new HashMap<>(), subject);
    }

    public String generateToken(Map<String, Object> extraClaims, String subject) {
        return buildToken(extraClaims, subject, jwtExpiration);
    }

    public String generateRefreshToken(String subject) {
        return buildToken(new HashMap<>(), subject, refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, String subject, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, String subject) {
        final String username = extractUsername(token);
        return (username.equals(subject)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token)  {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token)  {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {

            return Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (SignatureException ex) {
            throw new JwtValidationException("Invalid JWT signature.");
        } catch (Exception ex) {
            throw new JwtValidationException("Error parsing JWT token.");
        }

    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
