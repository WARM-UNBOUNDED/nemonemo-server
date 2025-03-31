package com.example.snsserver.dto.follow.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowCountResponseDto {
    private long followingCount;
    private long followerCount;
}