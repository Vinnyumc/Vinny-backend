package com.vinny.backend.User.repository;

import com.vinny.backend.post.domain.mapping.UserPostBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookmarkRepository extends JpaRepository<UserPostBookmark, Long> {
    int countByUserId(Long userId);
}