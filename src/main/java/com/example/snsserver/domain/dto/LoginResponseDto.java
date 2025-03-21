package com.example.snsserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private TokenDto tokenDto; // JWT 관련 데이터
}
