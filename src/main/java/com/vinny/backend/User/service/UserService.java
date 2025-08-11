package com.vinny.backend.User.service;

import com.vinny.backend.User.domain.*;
import com.vinny.backend.User.domain.enums.UserStatus;
import com.vinny.backend.User.domain.mapping.*;
import com.vinny.backend.User.repository.*;
import com.vinny.backend.User.dto.OnboardingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VintageStyleRepository vintageStyleRepository;
    private final BrandRepository brandRepository;
    private final VintageItemRepository vintageItemRepository;
    private final RegionRepository regionRepository;

    @Transactional
    public void completeOnboarding(Long userId, OnboardingRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("온보딩 진행 중인 사용자를 찾을 수 없습니다."));

        // 1. 닉네임 업데이트 및 상태를 'ACTIVE'로 변경
        user.updateNickname(requestDto.getNickname());
        user.updateComment(requestDto.getComment());
        user.changeStatus(UserStatus.ACTIVE);

        // 2. VintageStyle 정보 저장
        if (requestDto.getVintageStyleIds() != null && !requestDto.getVintageStyleIds().isEmpty()) {
            List<VintageStyle> vintageStyles = vintageStyleRepository.findAllById(requestDto.getVintageStyleIds());
            List<UserVintageStyle> userVintageStyles = vintageStyles.stream()
                    .map(style -> UserVintageStyle.builder().user(user).vintageStyle(style).build())
                    .collect(Collectors.toList());
            user.getUserVintageStyleList().clear(); // 기존 정보가 있다면 초기화
            user.getUserVintageStyleList().addAll(userVintageStyles);
        }

        // 3. Brand 정보 저장
        if (requestDto.getBrandIds() != null && !requestDto.getBrandIds().isEmpty()) {
            List<Brand> brands = brandRepository.findAllById(requestDto.getBrandIds());
            List<UserBrand> userBrands = brands.stream()
                    .map(brand -> UserBrand.builder().user(user).brand(brand).build())
                    .collect(Collectors.toList());
            user.getUserBrandList().clear();
            user.getUserBrandList().addAll(userBrands);
        }

        // 4. VintageItem 정보 저장 (위와 동일한 패턴)
        if (requestDto.getVintageItemIds() != null && !requestDto.getVintageItemIds().isEmpty()) {
            List<VintageItem> vintageItems = vintageItemRepository.findAllById(requestDto.getVintageItemIds());
            List<UserVintageItem> userVintageItems = vintageItems.stream()
                    .map(item -> UserVintageItem.builder().user(user).vintageItem(item).build())
                    .collect(Collectors.toList());
            user.getUserVintageItemList().clear();
            user.getUserVintageItemList().addAll(userVintageItems);
        }

        // 5. Region 정보 저장 (위와 동일한 패턴)
        if (requestDto.getRegionIds() != null && !requestDto.getRegionIds().isEmpty()) {
            List<Region> regions = regionRepository.findAllById(requestDto.getRegionIds());
            List<UserRegion> userRegions = regions.stream()
                    .map(region -> UserRegion.builder().user(user).region(region).build())
                    .collect(Collectors.toList());
            user.getUserRegionList().clear();
            user.getUserRegionList().addAll(userRegions);
        }
    }
}