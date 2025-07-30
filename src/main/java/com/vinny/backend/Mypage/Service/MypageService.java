package com.vinny.backend.Mypage.Service;

import com.vinny.backend.Mypage.dto.MypageProfileResponse;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.User.repository.UserShopRepository;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import com.vinny.backend.post.repository.PostRepository;
import com.vinny.backend.post.repository.UserPostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserShopRepository userShopRepository;
    private final UserPostLikeRepository userSavePostRepository;

    @Transactional(readOnly = true)
    public MypageProfileResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        int postCount = postRepository.countByUserId(userId);
        int likedShopCount = userShopRepository.countByUserId(userId);
        int savedCount = userSavePostRepository.countByUserId(userId);

        return new MypageProfileResponse(
                user.getId(),
                user.getNickname(),
                user.getProfileImage(),
                user.getComment(),
                postCount,
                likedShopCount,
                savedCount
        );
    }
}
