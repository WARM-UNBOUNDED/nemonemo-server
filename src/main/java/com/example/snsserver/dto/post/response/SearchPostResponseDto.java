package com.example.snsserver.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPostResponseDto {
    private List<PostResponseDto> posts;
    private long totalCount;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    public static SearchPostResponseDto from(Page<PostResponseDto> page) {
        return SearchPostResponseDto.builder()
                .posts(page.getContent())
                .totalCount(page.getTotalElements())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .build();
    }
}