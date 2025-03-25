package com.example.snsserver.domain.jwt.constants;

public class TokenConstants {
    public static final String AUTHORITIES_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String BEARER_TYPE = "Bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24; // 24시간
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
}