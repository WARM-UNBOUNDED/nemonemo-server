package com.example.snsserver.dto.follow.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponseDto {
    private Long followerId; // 팔로우하는 사용자 ID
    private Long followingId; // 팔로우 당하는 사용자 ID
    private boolean followed; // 팔로우 상태
}