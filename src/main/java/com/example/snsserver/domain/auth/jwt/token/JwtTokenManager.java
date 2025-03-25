package com.example.snsserver.domain.auth.jwt.token;

import com.example.snsserver.domain.auth.jwt.constants.TokenConstants;
import com.example.snsserver.domain.auth.jwt.exception.TokenValidationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenManager implements TokenGenerator, TokenValidator {
    private final Key key;

    public JwtTokenManager(@Value("${jwt.secret}") String secretKey) {
        log.info("Initializing JwtTokenManager with secret key: {}", secretKey);
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(String subject, String authorities, Date expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(TokenConstants.AUTHORITIES_KEY, authorities)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token has expired", e);
            throw new TokenValidationException("토큰이 만료되었습니다.");
        } catch (Exception e) {
            log.error("Invalid token", e);
            throw new TokenValidationException("유효하지 않은 토큰입니다.");
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            log.info("Validating token: {}", token);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            log.info("Token validated successfully");
            return true;
        } catch (JwtException e) {
            log.error("JWT validation failed: {}", e.getMessage(), e);
            return false;
        }
    }
}
