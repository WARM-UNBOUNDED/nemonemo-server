package com.example.snsserver.dto.auth.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class PageRequestDto {
    @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
    private Integer page = 0;

    @Positive(message = "페이지 크기는 1 이상이어야 합니다.")
    private Integer size = 10;

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }

    public Pageable toPageableWithSort(String sortBy, Sort.Direction direction) {
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}