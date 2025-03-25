package com.example.snsserver.domain.controller;

import com.example.snsserver.domain.dto.postdto.PostRequestDto;
import com.example.snsserver.domain.dto.postdto.PostResponseDto;
import com.example.snsserver.domain.service.postservice.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "게시물 생성", description = "파일과 함께 새 게시물을 생성합니다.")
    public ResponseEntity<PostResponseDto> createPost(
            @RequestPart(name = "post", required = true) @Valid PostRequestDto requestDto,
            @RequestPart(name = "file", required = false) MultipartFile file) throws IOException {
        log.info("Received createPost request - title: {}, file: {}",
                requestDto.getTitle(), file != null ? file.getOriginalFilename() : "none");
        PostResponseDto responseDto = postService.createPost(requestDto, file);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    @Operation(summary = "모든 게시물 조회", description = "모든 게시물을 조회합니다.")
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    @Operation(summary = "특정 게시물 조회", description = "특정 게시물을 조회합니다.")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PutMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "게시물 수정", description = "특정 게시물을 수정합니다.")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestPart("post") PostRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(postService.updatePost(postId, requestDto, file));
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "게시물 삭제", description = "특정 게시물을 삭제합니다.")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}





