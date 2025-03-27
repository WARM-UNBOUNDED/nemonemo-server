package com.example.snsserver.domain.comment.service;

import com.example.snsserver.domain.comment.repository.CommentRepository;
import com.example.snsserver.domain.post.repository.PostRepository;
import com.example.snsserver.domain.comment.entity.Comment;
import com.example.snsserver.domain.auth.entity.Member;
import com.example.snsserver.domain.post.entity.Post;
import com.example.snsserver.dto.comment.request.CommentRequestDto;
import com.example.snsserver.dto.comment.response.CommentResponseDto;
import com.example.snsserver.domain.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberService memberService;

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto) {
        log.info("Creating comment for postId: {}, content: {}", postId, requestDto.getContent());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Authenticated username: {}", username);
        Member member = memberService.getMemberByUsername(username);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        log.info("Found post: {}, created by: {}", post.getId(), post.getMember().getUsername());

        // 권한 검증 로직 제거 또는 수정
        // 모든 인증된 사용자가 댓글을 추가할 수 있도록 변경
        // if (!post.getMember().getUsername().equals(username)) {
        //     log.error("User {} is not authorized to comment on post {}", username, postId);
        //     throw new SecurityException("You are not authorized to comment on this post");
        // }

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .member(member)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
        log.info("Comment saved with id: {}", comment.getId());
        return mapToResponseDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private CommentResponseDto mapToResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getMember().getUsername())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}