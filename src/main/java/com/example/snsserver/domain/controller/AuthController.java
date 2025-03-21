package com.example.snsserver.domain.controller;

import com.example.snsserver.domain.dto.LoginRequestDto;
import com.example.snsserver.domain.dto.SignupRequestDto;
import com.example.snsserver.domain.dto.TokenDto;
import com.example.snsserver.domain.dto.TokenRequestDto;
import com.example.snsserver.domain.service.AuthService;
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
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        authService.signup(requestDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
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
