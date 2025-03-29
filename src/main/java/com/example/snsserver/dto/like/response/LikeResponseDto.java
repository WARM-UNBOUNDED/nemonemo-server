package com.example.snsserver.dto.like.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponseDto {
    private Long postId;
    private boolean liked;
    private long likeCount;
}