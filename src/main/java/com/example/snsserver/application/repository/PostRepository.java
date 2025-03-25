package com.example.snsserver.application.repository;

import com.example.snsserver.domain.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByMemberUsername(String username);
}