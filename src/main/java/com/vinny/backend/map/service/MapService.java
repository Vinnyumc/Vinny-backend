package com.vinny.backend.map.service;

import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.enums.UserShopStatus;
import com.vinny.backend.User.domain.mapping.UserShop;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.User.repository.UserShopRepository;
import com.vinny.backend.auth.jwt.JwtProvider;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import com.vinny.backend.map.converter.MapConverter;
import com.vinny.backend.map.dto.MapResponseDto.MapListDto;
import com.vinny.backend.map.repository.MapRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MapRepository mapRepository;
    private final MapConverter mapConverter;
    private final UserShopRepository userShopRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final HttpServletRequest request;

    public List<MapListDto> getAllShopList() {
        List<Shop> shopList = mapRepository.findAll();
        return mapConverter.toMapListDtos(shopList);
    }

    public List<MapListDto> getFavoriteShopList() {
        Long userId = getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<UserShop> userShopList = userShopRepository.findAllByUserAndStatus(user, UserShopStatus.FAVORITE);

        List<Shop> favoriteShopList = userShopList.stream()
                .map(UserShop::getShop)
                .collect(Collectors.toList());

        return mapConverter.toMapListDtos(favoriteShopList);
    }

    private Long getCurrentUserId() {
        String token = jwtProvider.resolveToken(request);
        if (token == null || !jwtProvider.validateToken(token)) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }
        return jwtProvider.getUserIdFromToken(token);
    }
}
