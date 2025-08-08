package com.vinny.backend.User.service;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.repository.ShopRepository;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.enums.UserShopStatus;
import com.vinny.backend.User.domain.mapping.UserShop;
import com.vinny.backend.User.dto.UserShopRequestDto;
import com.vinny.backend.User.dto.UserShopResponseDto;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.User.repository.UserShopRepository;
import com.vinny.backend.auth.jwt.JwtProvider;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserShopService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final UserShopRepository userShopRepository;
    /**
     * 가게 찜 추가
     */
    @Transactional
    public UserShopResponseDto.PreviewDto addFavoriteShop(Long userId, UserShopRequestDto.LikeDto likeDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Shop shop = shopRepository.findById(likeDto.getShopId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.SHOP_NOT_FOUND));

        if (userShopRepository.findByUserAndShop(user, shop).isPresent()) {
            throw new GeneralException(ErrorStatus.USER_SHOP_EXIST);
        }

        UserShop userShop = UserShop.create(user, shop, UserShopStatus.FAVORITE);
        userShopRepository.save(userShop);

        return UserShopResponseDto.toPreviewDto(userShop);
    }

    /**
     * 가게 찜 삭제
     */
    @Transactional
    public String removeFavoriteShop(Long userId, Long shopId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SHOP_NOT_FOUND));

        UserShop userShop = userShopRepository.findByUserAndShop(user, shop)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_SHOP_NOT_FOUND));

        userShopRepository.delete(userShop);

        return String.format("가게 ID %d의 즐겨찾기가 삭제되었습니다.", shopId);
    }

    /**
     * 특정 사용자 찜 목록 조회
     */
    @Transactional(readOnly = true)
    public List<UserShopResponseDto.PreviewShopDetailDto> getFavoriteShops(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<UserShop> favoriteShops = userShopRepository.findAllByUserAndStatus(user, UserShopStatus.FAVORITE);

        return favoriteShops.stream()
                .map(UserShopResponseDto.PreviewShopDetailDto::from)
                .collect(Collectors.toList());
    }

}


