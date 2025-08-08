package com.vinny.backend.search.service;

import com.vinny.backend.User.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandSearchService {

    private final BrandRepository brandRepository;
    private final RedisAutocompleteService redisAutocompleteService;
    private static final String SUFFIX = "*";
    private static final int MAX_SIZE = 10;

    // 서버 기동 시 Redis 인덱싱
    @PostConstruct
    public void init() {

        List<String> brandNames = brandRepository.findAllBrandNames();
        if (brandNames.isEmpty()) {
            log.warn("⚠️ [BrandService] 불러온 브랜드명이 없습니다. DB를 확인하세요.");
        } else {
            for (String name : brandNames) {
                redisAutocompleteService.addToSortedSet(name + SUFFIX); // 완성형
                for (int i = name.length(); i > 0; --i) {
                    redisAutocompleteService.addToSortedSet(name.substring(0, i));
                }
                log.debug("✔️ Redis에 인덱싱: {}", name);
            }
            log.info("✅ [BrandService] 자동완성 인덱싱 완료 (총 {}개)", brandNames.size());
        }
    }

    // 자동완성 API
    public List<String> autocomplete(String query) {
        String normalized = StringUtils.capitalize(query);
        Long idx = redisAutocompleteService.findIndex(normalized);
        if (idx == null) return List.of();

        Set<String> candidates = redisAutocompleteService.findCandidates(idx);

        return candidates.stream()
                .filter(val -> val.endsWith(SUFFIX) && val.startsWith(normalized))
                .map(val -> val.substring(0, val.length() - 1)) // * 제거
                .limit(MAX_SIZE)
                .toList();
    }
}
