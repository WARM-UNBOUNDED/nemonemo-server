package com.example.snsserver.domain.jwt.token;

import java.util.Date;

public interface TokenGenerator {
    String generateToken(String subject, String authorities, Date expiration);
}
