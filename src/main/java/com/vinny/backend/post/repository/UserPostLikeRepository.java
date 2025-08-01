package com.vinny.backend.post.repository;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.mapping.UserPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long> {
    Optional<UserPostLike> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);

    int countByUserId(Long userId);
}