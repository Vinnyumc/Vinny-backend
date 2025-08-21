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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
        // 지난 주 이전 데이터 정리만 수행
        deleteOldWeeklyShops(userId);

        LocalDate weekStart = getCurrentWeekStart();

        // 이번 주 데이터 조회(없으면 생성)
        List<UserWeeklyShop> saved = weeklyRepo.findByUser_IdAndWeekStart(userId, weekStart);
        if (saved == null || saved.isEmpty()) {
            saved = generateAndPersistWeekly(userId, weekStart);
        }

        // 저장된 후보에서 매 호출마다 랜덤 샘플 3개
        List<Shop> shops = saved.stream()
                .map(UserWeeklyShop::getShop)
                .filter(Objects::nonNull)
                .toList();

        if (shops.isEmpty()) return List.of();

        // 섞을 수 있도록 변경 가능한 리스트로 만들기
        List<Shop> shuffled = new ArrayList<>(shops);
        Collections.shuffle(shuffled, ThreadLocalRandom.current());

        return shuffled.stream()
                .limit(DEFAULT_LIMIT) // 최대 3개
                .map(shopConverter::toHomeForYouThumbnailDto)
                .toList();
    }



    /**
     * 이번 주 추천을 재생성하여 반환.
     */
    @Transactional
    public List<ShopResponseDto.HomeForYouThumbnailDto> regenerateThisWeek(Long userId) {
        LocalDate weekStart = getCurrentWeekStart();

        weeklyRepo.deleteByUser_IdAndWeekStart(userId, weekStart);
        em.flush();

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

        // 1) 후보 조회
        List<Long> rawIds = new ArrayList<>(shopQueryRepository.findMatchedShopIdsRandomByUser(userId));

        // 2) null 제거 + 중복 제거(순서 유지)
        LinkedHashSet<Long> dedup = new LinkedHashSet<>();
        for (Long id : rawIds) {
            if (id != null) dedup.add(id);
        }

        // 3) limit 적용 (중복 제거 후 적용해야 함)
        List<Long> matchedIds = dedup.stream()
                .limit(DEFAULT_LIMIT)
                .toList();

        if (matchedIds.isEmpty()) return List.of();

        List<UserWeeklyShop> saved = new ArrayList<>(matchedIds.size());

        for (Long shopId : matchedIds) {
            Shop shop = shopRepository.findById(shopId).orElse(null);
            if (shop == null) continue;

            // 4) 혹시 남아있는 동일 row가 있다면 방어적으로 skip
            boolean exists = weeklyRepo.existsByUser_IdAndWeekStartAndShop_Id(userId, weekStart, shopId);
            if (exists) continue;

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
