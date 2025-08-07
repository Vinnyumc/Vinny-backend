package com.vinny.backend.post.service;

import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.mapping.UserPostLike;
import com.vinny.backend.post.repository.PostRepository;
import com.vinny.backend.post.repository.UserPostLikeRepository;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
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
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if (userPostLikeRepository.existsByUserAndPost(user, post)) {
            throw new GeneralException(ErrorStatus.ALREADY_LIKED);
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
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        UserPostLike like = userPostLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LIKE_NOT_FOUND));

        userPostLikeRepository.delete(like);
    }
}
