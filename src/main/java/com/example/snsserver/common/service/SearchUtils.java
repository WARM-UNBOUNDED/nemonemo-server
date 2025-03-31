package com.example.snsserver.common.service;

import com.example.snsserver.common.exception.SearchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SearchUtils {

    public static <T> Specification<T> buildTitleSearchSpecification(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new SearchException("검색 키워드가 비어 있습니다.");
        }

        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + keyword.toLowerCase() + "%"
            ));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}