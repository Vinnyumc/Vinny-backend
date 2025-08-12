package com.vinny.backend.search.service;

import com.vinny.backend.Shop.repository.ShopRepository;
import com.vinny.backend.User.repository.BrandRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutocompleteSearchService {

    private final BrandRepository brandRepository;
    private final ShopRepository shopRepository;
    private final RedisAutocompleteService redisAutocompleteService;
    private static final String SUFFIX = "*";
    private static final int MAX_SIZE = 10;

    // 서버 기동 시 Redis 인덱싱
    @EventListener(ApplicationReadyEvent.class)
    @ConditionalOnProperty(name = "feature.autocomplete.init", havingValue = "true", matchIfMissing = false)
    public void init() {
        try {
            List<String> brandNames = brandRepository.findAllBrandNames();
            if (brandNames.isEmpty()) {
                log.warn("⚠️ [BrandService] 불러온 브랜드명이 없습니다. DB를 확인하세요.");
                return;
            }
            for (String name : brandNames) {
                redisAutocompleteService.addToSortedBrandSet(name + SUFFIX); // 완성형
                for (int i = name.length(); i > 0; --i) {
                    redisAutocompleteService.addToSortedBrandSet(name.substring(0, i));
                }
                log.debug("✔️ Redis에 인덱싱: {}", name);
            }
            log.info("✅ [BrandService] 자동완성 인덱싱 완료 (총 {}개)", brandNames.size());
        } catch (Exception e) {
            log.warn("Autocomplete warmup skipped: {}", e.getMessage());
        }
        try {
            List<String> shopNames = shopRepository.findAllShopNames();
            if (shopNames.isEmpty()) {
                log.warn("⚠️ [ShopService] 불러온 가게명이 없습니다. DB를 확인하세요.");
                return;
            }
            for (String name : shopNames) {
                redisAutocompleteService.addToSortedShopSet(name + SUFFIX); // 완성형
                for (int i = name.length(); i > 0; --i) {
                    redisAutocompleteService.addToSortedShopSet(name.substring(0, i));
                }
                log.debug("✔️ Redis에 인덱싱: {}", name);
            }
            log.info("✅ [ShopService] 자동완성 인덱싱 완료 (총 {}개)", shopNames.size());
        } catch (Exception e) {
            log.warn("Autocomplete warmup skipped: {}", e.getMessage());
        }
    }

    // 브랜드 자동완성 API
    public List<String> autocompleteBrandName(String query) {
        String normalized = StringUtils.capitalize(query);
        Long idx = redisAutocompleteService.findBrandIndex(normalized);
        if (idx == null) return List.of();

        Set<String> candidates = redisAutocompleteService.findBrandCandidates(idx);

        return candidates.stream()
                .filter(val -> val.endsWith(SUFFIX) && val.startsWith(normalized))
                .map(val -> val.substring(0, val.length() - 1)) // * 제거
                .limit(MAX_SIZE)
                .toList();
    }

    public List<String> autocompleteShopName(String query) {
        String normalized = StringUtils.capitalize(query);
        Long idx = redisAutocompleteService.findShopIndex(normalized);
        if (idx == null) return List.of();

        Set<String> candidates = redisAutocompleteService.findShopCandidates(idx);

        return candidates.stream()
                .filter(val -> val.endsWith(SUFFIX) && val.startsWith(normalized))
                .map(val -> val.substring(0, val.length() - 1)) // * 제거
                .limit(MAX_SIZE)
                .toList();
    }
}
