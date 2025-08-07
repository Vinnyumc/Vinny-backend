package com.vinny.backend.post.service;

import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.mapping.UserPostBookmark;
import com.vinny.backend.post.repository.PostRepository;
import com.vinny.backend.post.repository.UserPostBookmarkRepository;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostBookmarkService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserPostBookmarkRepository userPostBookmarkRepository;

    @Transactional
    public void bookmarkPost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if (userPostBookmarkRepository.existsByUserAndPost(user, post)) {
            throw new GeneralException(ErrorStatus.ALREADY_BOOKMARKED);
        }

        UserPostBookmark bookmark = UserPostBookmark.builder()
                .user(user)
                .post(post)
                .build();

        userPostBookmarkRepository.save(bookmark);
    }

    @Transactional
    public void unbookmarkPost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        UserPostBookmark bookmark = userPostBookmarkRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_NOT_FOUND));

        userPostBookmarkRepository.delete(bookmark);
    }
}
