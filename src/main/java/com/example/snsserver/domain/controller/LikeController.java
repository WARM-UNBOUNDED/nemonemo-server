package com.example.snsserver.domain.controller;

import com.example.snsserver.domain.dto.LikeResponseDto;
import com.example.snsserver.domain.service.postservice.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Tag(name = "Like", description = "좋아요 관련 API")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "좋아요 토글", description = "게시물에 좋아요를 추가하거나 제거합니다.")
    public ResponseEntity<LikeResponseDto> toggleLike(@PathVariable Long postId) {
        LikeResponseDto response = likeService.toggleLike(postId);
        return ResponseEntity.ok(response);
    }
}