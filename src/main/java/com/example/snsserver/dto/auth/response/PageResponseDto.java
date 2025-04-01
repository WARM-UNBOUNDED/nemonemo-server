package com.example.snsserver.dto.auth.response;

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
public class PageResponseDto<T> {
    private List<T> content;
    private long totalElements;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    public static <T> PageResponseDto<T> from(Page<T> page) {
        return PageResponseDto.<T>builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .build();
    }
}