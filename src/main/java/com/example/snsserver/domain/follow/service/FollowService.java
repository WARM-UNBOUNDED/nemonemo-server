package com.example.snsserver.domain.follow.service;

import com.example.snsserver.domain.follow.repository.FollowRepository;
import com.example.snsserver.domain.auth.repository.MemberRepository;
import com.example.snsserver.domain.follow.entity.Follow;
import com.example.snsserver.domain.auth.entity.Member;
import com.example.snsserver.dto.follow.response.FollowListResponseDto;
import com.example.snsserver.dto.follow.response.FollowResponseDto;
import com.example.snsserver.dto.follow.response.FollowCountResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FollowResponseDto toggleFollow(Long followingId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member follower = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
        Member following = memberRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 대상 사용자를 찾을 수 없습니다: " + followingId));

        // 자기 자신 팔로우 방지
        if (follower.getId().equals(following.getId())) {
            log.warn("User {} attempted to follow themselves", username);
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        boolean followed;
        if (followRepository.existsByFollowerIdAndFollowingId(follower.getId(), followingId)) {
            Follow follow = followRepository.findByFollowerIdAndFollowingId(follower.getId(), followingId).get();
            followRepository.delete(follow);
            log.info("User {} unfollowed user {}", follower.getUsername(), following.getUsername());
            followed = false;
        } else {
            Follow follow = Follow.builder()
                    .follower(follower)
                    .following(following)
                    .build();
            followRepository.save(follow);
            log.info("User {} followed user {}", follower.getUsername(), following.getUsername());
            followed = true;
        }

        return FollowResponseDto.builder()
                .followerId(follower.getId())
                .followingId(followingId)
                .followed(followed)
                .build();
    }

    @Transactional(readOnly = true)
    public List<FollowListResponseDto> getFollowingList(Long Id) {
        Member member = memberRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + Id));
        return member.getFollowingList().stream()
                .map(follow -> FollowListResponseDto.builder()
                        .Id(follow.getFollowing().getId())
                        .username(follow.getFollowing().getUsername())
                        .name(follow.getFollowing().getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FollowListResponseDto> getFollowerList(Long Id) {
        Member member = memberRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + Id));
        return member.getFollowerList().stream()
                .map(follow -> FollowListResponseDto.builder()
                        .Id(follow.getFollower().getId())
                        .username(follow.getFollower().getUsername())
                        .name(follow.getFollower().getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FollowListResponseDto> getMyFollowingList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
        return getFollowingList(member.getId());
    }

    @Transactional(readOnly = true)
    public List<FollowListResponseDto> getMyFollowerList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
        return getFollowerList(member.getId());
    }

    @Transactional(readOnly = true)
    public FollowCountResponseDto getMyFollowCount() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
        return getFollowCount(member.getId());
    }

    @Transactional(readOnly = true)
    public FollowCountResponseDto getFollowCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + memberId));
        long followingCount = member.getFollowingList().size();
        long followerCount = member.getFollowerList().size();
        log.info("Follow count for user {}: following={}, followers={}", member.getUsername(), followingCount, followerCount);
        return FollowCountResponseDto.builder()
                .followingCount(followingCount)
                .followerCount(followerCount)
                .build();
    }
}