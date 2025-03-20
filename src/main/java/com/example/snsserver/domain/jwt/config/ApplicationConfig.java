package com.example.snsserver.domain.jwt.config;

import com.example.snsserver.domain.jwt.token.JwtTokenManager;
import com.example.snsserver.domain.jwt.token.TokenGenerator;
import com.example.snsserver.domain.jwt.token.TokenValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public TokenGenerator tokenGenerator(JwtTokenManager jwtTokenManager) {
        return jwtTokenManager;
    }

    @Bean
    public TokenValidator tokenValidator(JwtTokenManager jwtTokenManager) {
        return jwtTokenManager;
    }
}