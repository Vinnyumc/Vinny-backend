package com.vinny.backend.User.service;

import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.dto.UserPostSummaryDto;
import com.vinny.backend.User.dto.UserProfileDto;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.User.repository.UserShopRepository;
import com.vinny.backend.post.repository.PostRepository;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.repository.UserPostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserPostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserPostLikeRepository userPostLikeRepository;
    private final UserShopRepository userShopRepository;

    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        int postCount = postRepository.countByUserId(userId);
        int likedShopCount = userShopRepository.countByUserId(userId);

        return new UserProfileDto(
                user.getId(),
                user.getNickname(),
                user.getComment(),
                postCount,
                likedShopCount,
                user.getProfileImage(),
                user.getBackgroundImage()
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