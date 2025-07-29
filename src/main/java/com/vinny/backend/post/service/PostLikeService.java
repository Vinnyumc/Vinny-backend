package com.vinny.backend.post.service;

import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.mapping.UserPostLike;
import com.vinny.backend.post.repository.PostRepository;
import com.vinny.backend.post.repository.UserPostLikeRepository;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserPostLikeRepository userPostLikeRepository;

    @Transactional
    public void likePost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (userPostLikeRepository.existsByUserAndPost(user, post)) {
            throw new IllegalStateException("이미 좋아요를 누른 게시글입니다.");
        }

        UserPostLike like = UserPostLike.builder()
                .user(user)
                .post(post)
                .build();

        userPostLikeRepository.save(like);
    }

    @Transactional
    public void unlikePost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        UserPostLike like = userPostLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalStateException("좋아요한 기록이 없습니다."));

        userPostLikeRepository.delete(like);
    }
}
