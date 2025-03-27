package com.example.snsserver.dto.follow.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowListResponseDto {
    private Long Id;
    private String username;
    private String name;
}