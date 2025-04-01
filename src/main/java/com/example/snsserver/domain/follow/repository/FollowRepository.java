package com.example.snsserver.domain.follow.repository;

import com.example.snsserver.domain.auth.entity.Member;
import com.example.snsserver.domain.follow.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Page<Follow> findByFollower(Member follower, Pageable pageable);
    Page<Follow> findByFollowing(Member following, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower = :follower")
    long countByFollower(@Param("follower") Member follower);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following = :following")
    long countByFollowing(@Param("following") Member following);
}