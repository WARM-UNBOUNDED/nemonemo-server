package com.example.snsserver.domain.comment.service;

import com.example.snsserver.domain.comment.repository.CommentRepository;
import com.example.snsserver.domain.post.repository.PostRepository;
import com.example.snsserver.domain.comment.entity.Comment;
import com.example.snsserver.domain.auth.entity.Member;
import com.example.snsserver.domain.post.entity.Post;
import com.example.snsserver.dto.auth.request.PageRequestDto;
import com.example.snsserver.dto.auth.response.PageResponseDto;
import com.example.snsserver.dto.comment.request.CommentRequestDto;
import com.example.snsserver.dto.comment.response.CommentResponseDto;
import com.example.snsserver.domain.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public PageResponseDto<CommentResponseDto> getCommentsByPost(Long postId, PageRequestDto pageRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        Page<Comment> commentPage = commentRepository.findByPostId(postId,
                pageRequestDto.toPageableWithSort("createdAt", Sort.Direction.DESC));
        Page<CommentResponseDto> commentResponsePage = commentPage.map(this::mapToResponseDto);

        log.info("Fetched {} comments for post {}", commentResponsePage.getTotalElements(), postId);
        return PageResponseDto.from(commentResponsePage);
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