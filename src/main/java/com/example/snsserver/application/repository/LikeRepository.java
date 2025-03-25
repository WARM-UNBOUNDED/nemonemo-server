package com.example.snsserver.application.repository;

import com.example.snsserver.domain.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
    long countByPostId(Long postId);
}