package com.vinny.backend.User.service;

import com.vinny.backend.Shop.converter.ShopConverter;
import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.Shop.dto.ShopResponseDto;
import com.vinny.backend.Shop.repository.ShopQueryRepository;
import com.vinny.backend.Shop.repository.ShopRepository;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.domain.mapping.UserWeeklyShop;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.User.repository.UserShopForYouRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UserShopForYouService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final int DEFAULT_LIMIT = 3;

    private final ShopQueryRepository shopQueryRepository;
    private final UserShopForYouRepository weeklyRepo;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final ShopConverter shopConverter;
    private final EntityManager em;

    /**
     * 이번 주 추천 샵 조회. 없으면 생성 후 반환.
     */
    @Transactional
    public List<ShopResponseDto.HomeForYouThumbnailDto> getThisWeekForYou(Long userId) {
        // 지난 주 이전 데이터 정리만 수행 (이번 주는 건드리지 않음)
        deleteOldWeeklyShops(userId);

        LocalDate weekStart = getCurrentWeekStart();

        // 이번 주 데이터 있으면 그대로 반환
        List<UserWeeklyShop> saved = weeklyRepo.findByUser_IdAndWeekStart(userId, weekStart);
        if (saved == null || saved.isEmpty()) {
            // 없을 때만 생성 (현재 주 삭제 없음)
            saved = generateAndPersistWeekly(userId, weekStart);
        }

        List<Long> idsInOrder = saved.stream()
                .map(uws -> uws.getShop().getId())
                .filter(Objects::nonNull)
                .toList();
        if (idsInOrder.isEmpty()) return List.of();

        Map<Long,Integer> order = new HashMap<>();
        for (int i = 0; i < idsInOrder.size(); i++) order.put(idsInOrder.get(i), i);

        List<Shop> shops = shopRepository.findAllById(idsInOrder);

        return shops.stream()
                .sorted(Comparator.comparingInt(s -> order.getOrDefault(s.getId(), Integer.MAX_VALUE)))
                .map(shopConverter::toHomeForYouThumbnailDto)
                .limit(DEFAULT_LIMIT)
                .toList();
    }


    /**
     * 이번 주 추천을 재생성하여 반환.
     */
    @Transactional
    public List<ShopResponseDto.HomeForYouThumbnailDto> regenerateThisWeek(Long userId) {
        LocalDate weekStart = getCurrentWeekStart();

        // 이번 주 데이터만 지우고 재생성
        weeklyRepo.deleteByUser_IdAndWeekStart(userId, weekStart);
        List<UserWeeklyShop> saved = generateAndPersistWeekly(userId, weekStart);

        List<Long> shopIdsInOrder = saved.stream()
                .map(uws -> uws.getShop().getId())
                .toList();

        if (shopIdsInOrder.isEmpty()) return List.of();

        Map<Long, Integer> orderMap = new HashMap<>();
        for (int i = 0; i < shopIdsInOrder.size(); i++) {
            orderMap.put(shopIdsInOrder.get(i), i);
        }

        List<Shop> shops = shopRepository.findAllById(shopIdsInOrder);

        return shops.stream()
                .sorted(Comparator.comparingInt(s -> orderMap.getOrDefault(s.getId(), Integer.MAX_VALUE)))
                .map(shopConverter::toHomeForYouThumbnailDto)
                .limit(DEFAULT_LIMIT)
                .toList();
    }

    // ===================== 내부 유틸 =====================

    private LocalDate getCurrentWeekStart() {
        // KST 기준 월요일 시작
        LocalDate todayKst = LocalDate.now(KST);
        return todayKst.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * 추천 후보(id 리스트)를 가져와 이번 주 UserWeeklyShop를 생성/저장.
     */
    private List<UserWeeklyShop> generateAndPersistWeekly(Long userId, LocalDate weekStart) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        List<Long> matchedIds = new ArrayList<>(shopQueryRepository.findMatchedShopIdsRandomByUser(userId));
        if (matchedIds.size() > DEFAULT_LIMIT) {
            matchedIds = matchedIds.subList(0, DEFAULT_LIMIT);
        }
        if (matchedIds.isEmpty()) return List.of();

        List<UserWeeklyShop> saved = new ArrayList<>(matchedIds.size());
        for (Long shopId : matchedIds) {
            Shop shop = shopRepository.findById(shopId).orElse(null);
            if (shop == null) continue;

            saved.add(weeklyRepo.save(
                    UserWeeklyShop.builder()
                            .user(user)
                            .shop(shop)
                            .weekStart(weekStart)
                            .build()
            ));
        }
        em.flush();
        return saved;
    }


    /**
     * 이번 주보다 이전 주차 데이터 삭제.
     */
    @Transactional
    public void deleteOldWeeklyShops(Long userId) {
        LocalDate thisWeekStart = getCurrentWeekStart();
        weeklyRepo.deleteByUser_IdAndWeekStartLessThan(userId, thisWeekStart);
    }
}
