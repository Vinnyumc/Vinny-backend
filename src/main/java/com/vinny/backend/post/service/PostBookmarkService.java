package com.vinny.backend.post.service;

import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.mapping.UserPostBookmark;
import com.vinny.backend.post.repository.PostRepository;
import com.vinny.backend.post.repository.UserPostBookmarkRepository;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if (userPostBookmarkRepository.existsByUserAndPost(user, post)) {
            throw new IllegalStateException("이미 북마크한 게시글입니다.");
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
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        UserPostBookmark bookmark = userPostBookmarkRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalStateException("북마크한 기록이 없습니다."));

        userPostBookmarkRepository.delete(bookmark);
    }
}
