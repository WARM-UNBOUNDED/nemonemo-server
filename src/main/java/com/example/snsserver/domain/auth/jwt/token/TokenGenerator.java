package com.example.snsserver.domain.auth.jwt.token;

import java.util.Date;

public interface TokenGenerator {
    String generateToken(String subject, String authorities, Date expiration);
}
