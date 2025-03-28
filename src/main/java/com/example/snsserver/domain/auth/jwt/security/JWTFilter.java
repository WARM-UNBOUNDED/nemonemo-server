package com.example.snsserver.domain.auth.jwt.security;

import com.example.snsserver.domain.auth.jwt.constants.TokenConstants;
import com.example.snsserver.domain.auth.service.TokenService;
import com.example.snsserver.domain.auth.jwt.token.JwtTokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Swagger 관련 경로를 필터링에서 제외
        String path = request.getRequestURI();
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            log.debug("Swagger 경로 요청: {}, JWT 필터링 제외", path);
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);
        if (token != null) {
            try {
                if (jwtTokenManager.validateToken(token)) {
                    Authentication authentication = tokenService.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Security Context에 '{}' 인증 정보를 저장했습니다", authentication.getName());
                    log.info("Authentication authorities: {}", authentication.getAuthorities());
                    log.info("SecurityContext authentication: {}", SecurityContextHolder.getContext().getAuthentication());
                }
            } catch (ExpiredJwtException e) {
                log.warn("Token has expired: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has expired");
                return;
            } catch (Exception e) {
                log.error("Authentication failed: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TokenConstants.AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TokenConstants.BEARER_PREFIX)) {
            return bearerToken.substring(TokenConstants.BEARER_PREFIX.length());
        }
        return null;
    }
}