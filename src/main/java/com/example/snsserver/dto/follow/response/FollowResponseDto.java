package com.example.snsserver.dto.follow.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponseDto {
    private Long followerId;
    private Long followingId;
    private boolean followed;
}