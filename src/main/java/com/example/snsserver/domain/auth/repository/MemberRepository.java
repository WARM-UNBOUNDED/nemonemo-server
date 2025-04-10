package com.example.snsserver.domain.auth.repository;

import com.example.snsserver.domain.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.username = :username")
    Optional<Member> findByUsername(@Param("username") String username);

    boolean existsByUsername(String username);
}