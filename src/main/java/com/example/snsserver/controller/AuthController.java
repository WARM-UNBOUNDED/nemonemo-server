package com.example.snsserver.controller;

import com.example.snsserver.dto.auth.request.LoginRequestDto;
import com.example.snsserver.dto.auth.response.MemberResponseDto;
import com.example.snsserver.dto.auth.request.SignupRequestDto;
import com.example.snsserver.dto.auth.response.TokenDto;
import com.example.snsserver.dto.auth.request.TokenRequestDto;
import com.example.snsserver.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 관련 API", description = "사용자 인증 및 토큰 관리 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "사용자가 회원가입을 수행합니다.")
    public ResponseEntity<MemberResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        MemberResponseDto response = authService.signup(requestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자가 로그인하고, JWT 토큰을 반환받습니다.")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        TokenDto tokenDto = authService.login(requestDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "사용자가 JWT 토큰을 재발급받습니다.")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        TokenDto tokenDto = authService.reissue(tokenRequestDto);
        return ResponseEntity.ok(tokenDto);
    }
}