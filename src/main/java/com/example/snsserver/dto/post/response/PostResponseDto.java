package com.example.snsserver.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String filePath;
    private String username;
    private LocalDateTime createdAt;
    private long likeCount;
    private int commentCount;
    private List tags = new ArrayList<>();
}