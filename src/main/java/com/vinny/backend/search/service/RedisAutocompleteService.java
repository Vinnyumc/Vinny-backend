package com.vinny.backend.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisAutocompleteService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String BRAND_KEY = "brand_autocomplete";
    private static final String SHOP_KEY = "shop_autocomplete";
    private static final String SUFFIX = "*";
    private static final int SCORE = 0;

    public void addToSortedBrandSet(String value) {
        Boolean result = redisTemplate.opsForZSet().add(BRAND_KEY, value, SCORE);
    }
    public void addToSortedShopSet(String value) {
        Boolean result = redisTemplate.opsForZSet().add(SHOP_KEY, value, SCORE);
    }

    public Long findBrandIndex(String value) {
        return redisTemplate.opsForZSet().rank(BRAND_KEY, value);
    }

    public Long findShopIndex(String value) {
        return redisTemplate.opsForZSet().rank(SHOP_KEY, value);
    }

    public Set<String> findBrandCandidates(Long index) {
        return redisTemplate.opsForZSet().range(BRAND_KEY, index, index + 200);
    }
    public Set<String> findShopCandidates(Long index) {
        return redisTemplate.opsForZSet().range(SHOP_KEY, index, index + 200);
    }
}
