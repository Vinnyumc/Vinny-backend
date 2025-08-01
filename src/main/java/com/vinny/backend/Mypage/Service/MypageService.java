package com.vinny.backend.Mypage.Service;
import com.vinny.backend.Mypage.dto.MypageLikedShopResponse;
import com.vinny.backend.Mypage.dto.MypagePostThumbnailResponse;
import com.vinny.backend.Mypage.dto.MypageProfileResponse;
import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.domain.ShopImage;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.mapping.UserShop;
import com.vinny.backend.Mypage.dto.MypageProfileResponse;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.User.repository.UserShopRepository;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import com.vinny.backend.post.domain.Post;
import com.vinny.backend.post.domain.PostImage;
import com.vinny.backend.post.repository.PostRepository;
import com.vinny.backend.post.repository.UserPostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserShopRepository userShopRepository;
    private final UserPostLikeRepository userPostLikeRepository;

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

}
