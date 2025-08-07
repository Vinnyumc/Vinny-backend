package com.vinny.backend.User.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.dto.UserPostSummaryDto;
import com.vinny.backend.User.dto.UserProfileDto;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import com.vinny.backend.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserPostService {

    private final UserRepository userRepository;

    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        return new UserProfileDto(
                user.getId(),
                user.getNickname(),
                user.getComment()
        );
    }

    public List<UserPostSummaryDto> getUserPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        List<Post> posts = user.getPosts();
        return posts.stream()
                .map(post -> new UserPostSummaryDto(
                        post.getId(),
                        //post.getContent(),
                        post.getPostImages().isEmpty() ? null : post.getPostImages().get(0).getImageUrl(),
                        post.getCreatedAt()
                ))
                .toList();
    }
}