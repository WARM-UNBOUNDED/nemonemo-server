package com.example.snsserver.domain.auth.service;

import com.example.snsserver.domain.auth.repository.MemberRepository;
import com.example.snsserver.domain.auth.repository.RefreshTokenRepository;
import com.example.snsserver.domain.enums.Authority;
import com.example.snsserver.domain.auth.entity.Member;
import com.example.snsserver.domain.auth.entity.RefreshToken;
import com.example.snsserver.dto.auth.request.LoginRequestDto;
import com.example.snsserver.dto.auth.response.MemberResponseDto;
import com.example.snsserver.dto.auth.request.SignupRequestDto;
import com.example.snsserver.dto.auth.response.TokenDto;
import com.example.snsserver.dto.auth.request.TokenRequestDto;
import com.example.snsserver.domain.auth.jwt.exception.TokenValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public MemberResponseDto signup(SignupRequestDto requestDto) {
        log.info("Registering new user: {}", requestDto.getUsername());
        if (memberRepository.existsByUsername(requestDto.getUsername())) {
            log.warn("Username already exists: {}", requestDto.getUsername());
            throw new IllegalArgumentException("이미 가입된 아이디입니다.");
        }

        Member member = Member.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .authority(Authority.ROLE_USER) // 서버에서 기본값 설정
                .build();

        log.info("Member authority before save: {}", member.getAuthority());
        Member savedMember = memberRepository.save(member);
        log.info("User registered successfully: {}", savedMember.getUsername());
        return new MemberResponseDto(savedMember);
    }

    @Transactional
    public TokenDto login(LoginRequestDto requestDto) {
        log.info("User login attempt: {}", requestDto.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info("Authentication successful for user: {}", requestDto.getUsername());

        TokenDto tokenDto = tokenService.createTokenDto(authentication);
        log.info("Generated tokens for user: {}", requestDto.getUsername());

        RefreshToken refreshToken = refreshTokenRepository.findByUsername(authentication.getName())
                .orElseGet(() -> RefreshToken.builder()
                        .username(authentication.getName())
                        .tokenValue(tokenDto.getRefreshToken())
                        .build());

        refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);
        log.info("Refresh token saved for user: {}", authentication.getName());

        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        log.info("Token reissue request with refresh token: {}", tokenRequestDto.getRefreshToken());
        try {
            if (!tokenService.validateToken(tokenRequestDto.getRefreshToken())) {
                log.warn("Invalid refresh token: {}", tokenRequestDto.getRefreshToken());
                throw new TokenValidationException("유효하지 않은 Refresh Token입니다.");
            }
        } catch (TokenValidationException e) {
            log.error("Refresh token validation failed: {}", e.getMessage());
            throw new TokenValidationException("Refresh Token이 만료되었거나 유효하지 않습니다. 토큰을 확인하세요.");
        }

        Authentication authentication = tokenService.getAuthentication(tokenRequestDto.getAccessToken());
        log.info("Authentication retrieved for user: {}", authentication.getName());

        RefreshToken refreshToken = refreshTokenRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> {
                    log.error("No refresh token found for user: {}", authentication.getName());
                    return new IllegalArgumentException("로그아웃된 사용자입니다.");
                });

        if (!refreshToken.getTokenValue().equals(tokenRequestDto.getRefreshToken())) {
            log.warn("Refresh token mismatch for user: {}", authentication.getName());
            throw new TokenValidationException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenService.createTokenDto(authentication);
        refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);
        log.info("New tokens issued and refresh token updated for user: {}", authentication.getName());

        return tokenDto;
    }
}