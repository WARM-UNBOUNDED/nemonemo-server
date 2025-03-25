package com.example.snsserver.domain.controller;

import com.example.snsserver.domain.dto.postdto.CommentRequestDto;
import com.example.snsserver.domain.dto.postdto.CommentResponseDto;
import com.example.snsserver.domain.service.postservice.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts") // 매핑 변경
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments") // 매핑 변경
    @Operation(summary = "댓글 생성", description = "게시물에 새 댓글을 추가합니다.")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.createComment(postId, requestDto));
    }

    @GetMapping("/{postId}/comments") // 매핑 변경
    @Operation(summary = "게시물 댓글 조회", description = "특정 게시물의 모든 댓글을 조회합니다.")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }
}