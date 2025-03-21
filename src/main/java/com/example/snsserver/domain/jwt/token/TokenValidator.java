package com.example.snsserver.domain.jwt.token;

import io.jsonwebtoken.Claims;

public interface TokenValidator {
    boolean validateToken(String token);
    Claims parseToken(String token);
}
