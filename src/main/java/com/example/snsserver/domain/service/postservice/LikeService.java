package com.example.snsserver.domain.service.postservice;

import com.example.snsserver.application.repository.LikeRepository;
import com.example.snsserver.application.repository.PostRepository;
import com.example.snsserver.domain.domain.Like;
import com.example.snsserver.domain.domain.Member;
import com.example.snsserver.domain.domain.Post;
import com.example.snsserver.domain.dto.LikeResponseDto;
import com.example.snsserver.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MemberService memberService;

    @Transactional
    public LikeResponseDto toggleLike(Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Toggling like for postId: {} by user: {}", postId, username);
        Member member = memberService.getMemberByUsername(username);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        boolean liked;
        Optional<Like> likeOptional = likeRepository.findByMemberIdAndPostId(member.getId(), postId);
        if (likeOptional.isPresent()) {
            likeRepository.delete(likeOptional.get());
            log.info("Like removed for postId: {} by user: {}", postId, username);
            liked = false;
        } else {
            Like like = Like.builder()
                    .member(member)
                    .post(post)
                    .build();
            likeRepository.save(like);
            log.info("Like added for postId: {} by user: {}", postId, username);
            liked = true;
        }

        long likeCount = getLikeCount(postId); // 별도 메서드로 분리
        return LikeResponseDto.builder()
                .postId(postId)
                .liked(liked)
                .likeCount(likeCount)
                .build();
    }

    @Transactional(readOnly = true)
    public long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}