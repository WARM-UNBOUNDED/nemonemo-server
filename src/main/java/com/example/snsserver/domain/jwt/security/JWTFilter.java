package com.example.snsserver.domain.jwt.security;

import com.example.snsserver.domain.jwt.constants.TokenConstants;
import com.example.snsserver.domain.jwt.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = resolveToken(request);

            if (StringUtils.hasText(jwt) && tokenService.validateToken(jwt)) {
                Authentication authentication = tokenService.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다", authentication.getName());
            }
        } catch (Exception e) {
            log.error("Security Context에 인증 정보를 저장할 수 없습니다: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TokenConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TokenConstants.BEARER_PREFIX)) {
            return bearerToken.substring(TokenConstants.BEARER_PREFIX.length());
        }
        return null;
    }
}

