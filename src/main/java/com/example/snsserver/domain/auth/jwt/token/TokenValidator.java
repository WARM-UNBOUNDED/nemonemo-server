package com.example.snsserver.domain.auth.jwt.token;

import io.jsonwebtoken.Claims;

public interface TokenValidator {
    boolean validateToken(String token);
    Claims parseToken(String token);
}
