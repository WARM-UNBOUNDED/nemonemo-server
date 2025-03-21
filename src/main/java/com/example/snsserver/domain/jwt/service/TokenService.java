package com.example.snsserver.domain.jwt.service;


import com.example.snsserver.domain.dto.TokenDto;
import com.example.snsserver.domain.jwt.constants.TokenConstants;
import com.example.snsserver.domain.jwt.exception.TokenValidationException;
import com.example.snsserver.domain.jwt.token.TokenGenerator;
import com.example.snsserver.domain.jwt.token.TokenValidator;
import com.example.snsserver.domain.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenGenerator tokenGenerator;
    private final TokenValidator tokenValidator;
    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public TokenDto createTokenDto(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date accessTokenExpiresIn = new Date(now + TokenConstants.ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + TokenConstants.REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = tokenGenerator.generateToken(
                authentication.getName(),
                authorities,
                accessTokenExpiresIn
        );

        String refreshToken = tokenGenerator.generateToken(
                authentication.getName(),
                authorities,
                refreshTokenExpiresIn
        );

        return new TokenDto(
                TokenConstants.BEARER_TYPE,
                accessToken,
                refreshToken,
                accessTokenExpiresIn.getTime()
        );
    }

    public Authentication getAuthentication(String token) {
        Claims claims = tokenValidator.parseToken(token);

        if (claims.get(TokenConstants.AUTHORITIES_KEY) == null) {
            throw new TokenValidationException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(TokenConstants.AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public boolean validateToken(String token) {
        return tokenValidator.validateToken(token);
    }
}
