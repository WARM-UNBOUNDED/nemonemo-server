package com.example.snsserver.domain.post.repository;

import com.example.snsserver.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    Page<Post> findAll(Pageable pageable);

    @Query("SELECT p.id, COUNT(l) FROM Post p LEFT JOIN Like l ON l.post = p WHERE p IN :posts GROUP BY p.id")
    List<Object[]> countLikesByPosts(@Param("posts") List<Post> posts);
}