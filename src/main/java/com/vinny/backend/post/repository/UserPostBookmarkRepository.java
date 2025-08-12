package com.vinny.backend.post.repository;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.mapping.UserPostBookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserPostBookmarkRepository extends JpaRepository<UserPostBookmark, Long> {
    Optional<UserPostBookmark> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);

    // 마이페이지: 내가 북마크한 Post ID를 최신순으로 (createdAt desc)
    @Query("""
        select p.id
        from UserPostBookmark b
        join b.post p
        where b.user.id = :userId
        order by b.createdAt desc
    """)
    List<Long> findBookmarkedPostIdsOrderByCreatedDesc(Long userId);

    @Query("select count(b) from UserPostBookmark b")
    long countAll();

    @Query("select count(b) from UserPostBookmark b where b.user.id=:userId")
    long countByUserId(Long userId);


}
