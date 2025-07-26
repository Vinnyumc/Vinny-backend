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
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserShopService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final UserShopRepository userShopRepository;

    /**
     * 가게 즐겨찾기 추가
     */
    @Transactional
    public UserShopResponseDto.PreviewDto addFavoriteShop(UserShopRequestDto.LikeDto likeDto) {
        // 현재 로그인한 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        Long userId;
        try {
            userId = Long.parseLong(authentication.getName()); // name에 userId가 들어 있어야 함
        } catch (NumberFormatException e) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Shop shop = shopRepository.findById(likeDto.getShopId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.SHOP_NOT_FOUND));

        if (userShopRepository.findByUserAndShop(user, shop).isPresent()) {
            throw new IllegalStateException("이미 등록된 즐겨찾기입니다.");
        }

        UserShop userShop = UserShop.create(user, shop, UserShopStatus.FAVORITE);
        userShopRepository.save(userShop);

        return UserShopResponseDto.toPreviewDto(userShop);
    }

}

