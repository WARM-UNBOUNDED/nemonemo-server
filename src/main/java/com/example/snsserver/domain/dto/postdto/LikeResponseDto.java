package com.example.snsserver.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponseDto {
    private Long postId; // 추가
    private boolean liked;
    private long likeCount;
}