package com.example.snsserver.domain.service;

import com.example.snsserver.application.repository.MemberRepository;
import com.example.snsserver.application.repository.RefreshTokenRepository;
import com.example.snsserver.domain.domain.Member;
import com.example.snsserver.domain.domain.RefreshToken;
import com.example.snsserver.domain.dto.LoginRequestDto;
import com.example.snsserver.domain.dto.SignupRequestDto;
import com.example.snsserver.domain.dto.TokenDto;
import com.example.snsserver.domain.dto.TokenRequestDto;
import com.example.snsserver.domain.jwt.exception.TokenValidationException;
import com.example.snsserver.domain.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        if (memberRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 가입된 아이디입니다.");
        }

        Member member = Member.builder()
                .name(requestDto.getName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .username(requestDto.getUsername())
                .authority(requestDto.getAuthority())
                .build();

        memberRepository.save(member);
    }

    @Transactional
    public TokenDto login(LoginRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        TokenDto tokenDto = tokenService.createTokenDto(authentication);

        RefreshToken refreshToken = refreshTokenRepository.findByUsername(authentication.getName())
                .orElseGet(() -> RefreshToken.builder()
                        .username(authentication.getName())
                        .tokenValue(tokenDto.getRefreshToken())
                        .build());

        refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        try {
            if (!tokenService.validateToken(tokenRequestDto.getRefreshToken())) {
                throw new TokenValidationException("유효하지 않은 Refresh Token입니다.");
            }
        } catch (TokenValidationException e) {
            throw new TokenValidationException("Refresh Token이 만료되었거나 유효하지 않습니다. 토큰을 확인하세요.");
        }

        Authentication authentication = tokenService.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("로그아웃된 사용자입니다."));

        if (!refreshToken.getTokenValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new TokenValidationException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenService.createTokenDto(authentication);
        refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }
}