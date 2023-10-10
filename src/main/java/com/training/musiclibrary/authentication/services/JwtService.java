package com.training.musiclibrary.authentication.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    // TODO : put in env file
    private final String SECRET_KEY = "KiXhQDkM6fTNBjxxcWj0pQa+4Dk10t+ghffchJxLpi4klt6DW9vFqm4zAF2T1e5U\n";
    public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 24 * 60;

    private final RevokedTokenService revokedTokenService;
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Generate token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    // Validate token
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token)
                && !revokedTokenService.isTokenRevoked(token);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpirationDateFromToken(token).before(new Date());
    }

    private Date extractExpirationDateFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getToken(String authorizationHeader) throws RuntimeException {

       if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
           throw new RuntimeException("Token not found in Authorization header");
       }

       return authorizationHeader.substring(7);
    }

}
