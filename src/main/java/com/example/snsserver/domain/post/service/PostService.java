package com.example.snsserver.domain.post.service;

import com.example.snsserver.common.service.BaseImageService;
import com.example.snsserver.common.service.SearchUtils;
import com.example.snsserver.domain.like.repository.LikeRepository;
import com.example.snsserver.domain.post.repository.PostRepository;
import com.example.snsserver.domain.auth.entity.Member;
import com.example.snsserver.domain.post.entity.Post;
import com.example.snsserver.dto.auth.request.PageRequestDto;
import com.example.snsserver.dto.auth.response.PageResponseDto;
import com.example.snsserver.dto.post.request.PostRequestDto;
import com.example.snsserver.dto.post.request.SearchPostRequestDto;
import com.example.snsserver.dto.post.response.PostResponseDto;
import com.example.snsserver.dto.post.response.SearchPostResponseDto;
import com.example.snsserver.domain.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService extends BaseImageService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final LikeRepository likeRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, MultipartFile file) {
        Member member = getCurrentMember();
        String filePath = uploadImage(file, "post");

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .filePath(filePath)
                .member(member)
                .build();

        Post savedPost = postRepository.save(post);
        return mapToResponseDto(savedPost);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<PostResponseDto> getAllPosts(PageRequestDto pageRequestDto) {
        Page<Post> postPage = postRepository.findAll(
                pageRequestDto.toPageableWithSort("createdAt", Sort.Direction.DESC));
        return PageResponseDto.from(postPage.map(this::mapToResponseDto));
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        Post post = findPostById(postId);
        return mapToResponseDto(post);
    }

    @Transactional(readOnly = true)
    public SearchPostResponseDto searchPosts(SearchPostRequestDto requestDto) {
        PageRequest pageable = PageRequest.of(requestDto.getPage(), requestDto.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Specification<Post> spec = SearchUtils.buildTitleSearchSpecification(requestDto.getKeyword());
        Page<Post> postPage = postRepository.findAll(spec, pageable);

        return SearchPostResponseDto.from(postPage.map(this::mapToResponseDto));
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, MultipartFile file) {
        Post post = findPostById(postId);
        validatePostOwner(post);

        String filePath = handleFileUpdate(post.getFilePath(), file);
        post = post.toBuilder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .filePath(filePath)
                .build();

        postRepository.save(post);
        return mapToResponseDto(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = findPostById(postId);
        validatePostOwner(post);

        deleteImage(post.getFilePath());
        postRepository.delete(post);
    }

    private PostResponseDto mapToResponseDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .filePath(post.getFilePath())
                .username(post.getMember().getUsername())
                .createdAt(post.getCreatedAt())
                .likeCount(likeRepository.countByPostId(post.getId()))
                .commentCount(post.getComments().size())
                .build();
    }

    private Member getCurrentMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.getMemberByUsername(username);
        if (member == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username);
        }
        return member;
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
    }

    private void validatePostOwner(Post post) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!post.getMember().getUsername().equals(username)) {
            throw new SecurityException("본인의 게시물만 수정/삭제할 수 있습니다.");
        }
    }

    private String handleFileUpdate(String existingFilePath, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            deleteImage(existingFilePath);
            return uploadImage(file, "post");
        }
        return existingFilePath;
    }
}