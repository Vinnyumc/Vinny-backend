package com.vinny.backend.User.service;

import com.vinny.backend.User.domain.*;
import com.vinny.backend.User.domain.enums.UserStatus;
import com.vinny.backend.User.domain.mapping.*;
import com.vinny.backend.User.dto.*;
import com.vinny.backend.User.config.UserPreferenceChangedEvent;
import com.vinny.backend.User.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VintageStyleRepository vintageStyleRepository;
    private final BrandRepository brandRepository;
    private final VintageItemRepository vintageItemRepository;
    private final RegionRepository regionRepository;
    private final UserShopForYouService userShopForYouService;
    private final EntityManager em;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void completeOnboarding(Long userId, OnboardingRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("온보딩 진행 중인 사용자를 찾을 수 없습니다."));

        // 1. 닉네임/코멘트/상태
        user.updateNickname(requestDto.getNickname());
        user.updateComment(requestDto.getComment());
        user.changeStatus(UserStatus.ACTIVE);

        // 2. VintageStyle
        if (requestDto.getVintageStyleIds() != null && !requestDto.getVintageStyleIds().isEmpty()) {
            List<VintageStyle> vintageStyles = vintageStyleRepository.findAllById(requestDto.getVintageStyleIds());
            List<UserVintageStyle> userVintageStyles = vintageStyles.stream()
                    .map(style -> UserVintageStyle.builder().user(user).vintageStyle(style).build())
                    .toList();
            user.getUserVintageStyleList().clear();
            user.getUserVintageStyleList().addAll(userVintageStyles);
        }

        // 3. Brand
        if (requestDto.getBrandIds() != null && !requestDto.getBrandIds().isEmpty()) {
            List<Brand> brands = brandRepository.findAllById(requestDto.getBrandIds());
            List<UserBrand> userBrands = brands.stream()
                    .map(brand -> UserBrand.builder().user(user).brand(brand).build())
                    .toList();
            user.getUserBrandList().clear();
            user.getUserBrandList().addAll(userBrands);
        }

        // 4. VintageItem
        if (requestDto.getVintageItemIds() != null && !requestDto.getVintageItemIds().isEmpty()) {
            List<VintageItem> vintageItems = vintageItemRepository.findAllById(requestDto.getVintageItemIds());
            List<UserVintageItem> userVintageItems = vintageItems.stream()
                    .map(item -> UserVintageItem.builder().user(user).vintageItem(item).build())
                    .toList();
            user.getUserVintageItemList().clear();
            user.getUserVintageItemList().addAll(userVintageItems);
        }

        // 5. Region
        if (requestDto.getRegionIds() != null && !requestDto.getRegionIds().isEmpty()) {
            List<Region> regions = regionRepository.findAllById(requestDto.getRegionIds());
            List<UserRegion> userRegions = regions.stream()
                    .map(region -> UserRegion.builder().user(user).region(region).build())
                    .toList();
            user.getUserRegionList().clear();
            user.getUserRegionList().addAll(userRegions);
        }

        em.flush(); // 중요: 변경을 DB에 밀어넣음
        publisher.publishEvent(new UserPreferenceChangedEvent(userId));
    }

    @Transactional
    public void resetVintageStyle(Long userId, UserVintageStyleRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("취향 재설정 중인 사용자를 찾을 수 없습니다."));

        if (requestDto.getVintageStyleIds() != null && !requestDto.getVintageStyleIds().isEmpty()) {
            List<VintageStyle> vintageStyles = vintageStyleRepository.findAllById(requestDto.getVintageStyleIds());
            List<UserVintageStyle> userVintageStyles = vintageStyles.stream()
                    .map(style -> UserVintageStyle.builder().user(user).vintageStyle(style).build())
                    .toList();
            user.getUserVintageStyleList().clear();
            user.getUserVintageStyleList().addAll(userVintageStyles);
        }

        em.flush();
        publisher.publishEvent(new UserPreferenceChangedEvent(userId));
    }

    @Transactional
    public void resetBrand(Long userId, UserBrandRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("취향 재설정 중인 사용자를 찾을 수 없습니다."));

        if (requestDto.getBrandIds() != null && !requestDto.getBrandIds().isEmpty()) {
            List<Brand> brands = brandRepository.findAllById(requestDto.getBrandIds());
            List<UserBrand> userBrands = brands.stream()
                    .map(brand -> UserBrand.builder().user(user).brand(brand).build())
                    .toList();
            user.getUserBrandList().clear();
            user.getUserBrandList().addAll(userBrands);
        }

        em.flush();
        publisher.publishEvent(new UserPreferenceChangedEvent(userId));
    }

    @Transactional
    public void resetVintageItem(Long userId, UserVintageItemRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("취향 재설정 중인 사용자를 찾을 수 없습니다."));

        if (requestDto.getVintageItemIds() != null && !requestDto.getVintageItemIds().isEmpty()) {
            List<VintageItem> vintageItems = vintageItemRepository.findAllById(requestDto.getVintageItemIds());
            List<UserVintageItem> userVintageItems = vintageItems.stream()
                    .map(item -> UserVintageItem.builder().user(user).vintageItem(item).build())
                    .toList();
            user.getUserVintageItemList().clear();
            user.getUserVintageItemList().addAll(userVintageItems);
        }

        em.flush();
        publisher.publishEvent(new UserPreferenceChangedEvent(userId));
    }

    @Transactional
    public void resetRegion(Long userId, UserRegionRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("취향 재설정 중인 사용자를 찾을 수 없습니다."));

        if (requestDto.getRegionIds() != null && !requestDto.getRegionIds().isEmpty()) {
            List<Region> regions = regionRepository.findAllById(requestDto.getRegionIds());
            List<UserRegion> userRegions = regions.stream()
                    .map(region -> UserRegion.builder().user(user).region(region).build())
                    .toList();
            user.getUserRegionList().clear();
            user.getUserRegionList().addAll(userRegions);
        }


        em.flush();
        publisher.publishEvent(new UserPreferenceChangedEvent(userId));

    }
}
