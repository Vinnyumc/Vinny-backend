package com.vinny.backend.post.repository;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.mapping.UserPostBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPostBookmarkRepository extends JpaRepository<UserPostBookmark, Long> {
    Optional<UserPostBookmark> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
}
