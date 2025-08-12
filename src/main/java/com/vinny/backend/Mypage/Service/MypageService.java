package com.vinny.backend.Mypage.Service;
import com.vinny.backend.Mypage.dto.*;
import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.domain.ShopImage;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.mapping.UserShop;
import com.vinny.backend.Mypage.dto.MypageProfileResponse;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.User.repository.UserShopRepository;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.PostImage;
import com.vinny.backend.post.repository.PostRepository;
import com.vinny.backend.post.repository.UserPostBookmarkRepository;
import com.vinny.backend.post.repository.UserPostLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MypageService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserShopRepository userShopRepository;
    private final UserPostLikeRepository userPostLikeRepository;
    private final UserPostBookmarkRepository bookmarkRepository;

    @Transactional(readOnly = true)
    public MypageProfileResponse getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        int postCount = postRepository.countByUserId(userId);
        int likedShopCount = userShopRepository.countByUserId(userId);
        int savedCount = userPostLikeRepository.countByUserId(userId);

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


    @Transactional(readOnly = true)
    public List<MypagePostThumbnailResponse> getMyPostsWithOneImage(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);

        return posts.stream()
                .map(post -> new MypagePostThumbnailResponse(
                        post.getId(),
                        post.getImages().stream()
                                .findFirst()
                                .map(PostImage::getImageUrl)
                                .orElse(null)
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MypageLikedShopResponse> getLikedShops(Long userId) {
        List<UserShop> userShops = userShopRepository.findByUserId(userId);

        return userShops.stream()
                .map(userShop -> {
                    Shop shop = userShop.getShop();

                    String regionName = shop.getRegion() != null ? shop.getRegion().getName() : null;

                    String thumbnailUrl = shop.getShopImages().stream()
                            .findFirst()
                            .map(ShopImage::getImageUrl)
                            .orElse(null);

                    List<String> vintageStyles = shop.getShopVintageStyleList().stream()
                            .map(style -> style.getVintageStyle().getName())  // 연결된 엔티티에서 스타일 이름 추출
                            .toList();

                    return new MypageLikedShopResponse(
                            shop.getId(),
                            shop.getName(),
                            regionName,
                            shop.getAddress(),
                            thumbnailUrl,
                            vintageStyles
                    );
                })
                .toList();
    }

    /*
    Mypage 유저 정보 수정 api
     */
    @Transactional
    public MypageUserProfileDto updateProfile(Long userId, MypageUpdateProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (req.nickname() != null && !req.nickname().isBlank()) {
            user.updateNickname(req.nickname());
        }
        if (req.comment() != null) {
            user.updateComment(req.comment());
        }

        return toProfileDto(user);
    }

    private MypageUserProfileDto toProfileDto(User u) {
        return new MypageUserProfileDto(
                u.getId(),
                u.getNickname(),
                u.getProfileImage(),
                u.getBackgroundImage(),
                u.getComment()
        );
    }

    @Transactional
    public MypageUserProfileDto updateProfileImage(Long userId, MypageUpdateProfileImageRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        user.updateProfileImage(req.imageUrl());
        return toProfileDto(user);
    }

    @Transactional
    public MypageUserProfileDto updateBackgroundImage(Long userId,  MypageUpdateProfileImageRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        user.updateBackgroundImage(req.imageUrl());
        return toProfileDto(user);
    }

    @Transactional(readOnly = true)
    public List<MypageBookmarkPostResponse> list(Long userId) {
        log.info("list() userId={}", userId);
        long total = bookmarkRepository.countAll();
        long forUser = bookmarkRepository.countByUserId(userId);
        log.info("bookmark total={}, for userId={} -> {}", total, userId, forUser);
        // 1. 북마크된 Post ID 가져오기
        List<Long> postIds = bookmarkRepository.findBookmarkedPostIdsOrderByCreatedDesc(userId);
        log.info("북마크된 postIds: {}", postIds); // ✅ ID 확인

        if (postIds.isEmpty()) return List.of();

        // 2. ID로 Post 엔티티 한 번에 조회
        List<Post> posts = postRepository.findAllById(postIds);
        log.info("조회된 Post 개수: {}", posts.size()); // ✅ 개수 확인
        posts.forEach(p -> log.info("Post id: {}, images size: {}", p.getId(), p.getImages().size()));

        // 4. 원래 postIds 순서 유지하며 DTO 변환
        List<MypageBookmarkPostResponse> result = posts.stream()
                .map(post -> new MypageBookmarkPostResponse(
                        post.getId(),
                        post.getImages().stream()
                                .findFirst()
                                .map(PostImage::getImageUrl)
                                .orElse(null)
                ))
                .toList();

        log.info("최종 반환 DTO: {}", result); // ✅ 변환된 DTO 확인
        return result;
    }
}
