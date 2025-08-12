package com.vinny.backend.post.repository;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.mapping.UserPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long> {
    Optional<UserPostLike> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);

    int countByUserId(Long userId);

    @Query("""
           select pi.imageUrl
           from UserPostLike upl
           join upl.post p
           join p.images pi
           where upl.user.id = :userId
             and pi.id.sequence = (
                 select min(pi2.id.sequence)
                 from PostImage pi2
                 where pi2.post = p
             )
           order by upl.createdAt desc
           """)
    List<String> findFirstImageUrlsOfLikedPostsOrderByLikedAtDesc(Long userId);




}