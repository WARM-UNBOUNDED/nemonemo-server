package com.example.snsserver.dto.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class SearchPostByTagRequestDto {
    @NotBlank(message = "태그는 필수입니다.")
    private String tag;

    @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
    private Integer page = 0;

    @Positive(message = "페이지 크기는 1 이상이어야 합니다.")
    private Integer size = 10;

}