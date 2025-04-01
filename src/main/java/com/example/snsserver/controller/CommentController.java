package com.example.snsserver.controller;

import com.example.snsserver.dto.auth.request.PageRequestDto;
import com.example.snsserver.dto.auth.response.PagedResponseDto;
import com.example.snsserver.dto.comment.request.CommentRequestDto;
import com.example.snsserver.dto.comment.response.CommentResponseDto;
import com.example.snsserver.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "댓글 생성", description = "게시물에 새 댓글을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 생성 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "게시물 없음")
    })
    public ResponseEntity<CommentResponseDto> createComment(
            @Parameter(description = "댓글이 달릴 게시물 ID", required = true, example = "1")
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(commentService.createComment(postId, requestDto));
    }

    @GetMapping("/{postId}/comments")
    @Operation(summary = "게시물 댓글 조회", description = "특정 게시물의 모든 댓글을 페이지 단위로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시물 없음")
    })
    public ResponseEntity<PagedResponseDto<CommentResponseDto>> getCommentsByPost(
            @Parameter(description = "조회할 게시물 ID", required = true, example = "1")
            @PathVariable Long postId,
            @Parameter(description = "페이지 요청 정보 (page, size)", required = true)
            @ModelAttribute PageRequestDto pageRequestDto) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId, pageRequestDto));
    }
}