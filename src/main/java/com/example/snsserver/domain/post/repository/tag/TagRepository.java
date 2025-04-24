package com.example.snsserver.domain.post.repository.tag;

import com.example.snsserver.domain.post.entity.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}