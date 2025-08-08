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

    private static final String KEY = "brand_autocomplete";
    private static final String SUFFIX = "*";
    private static final int SCORE = 0;

    public void addToSortedSet(String value) {
        Boolean result = redisTemplate.opsForZSet().add(KEY, value, SCORE);
    }

    public Long findIndex(String value) {
        return redisTemplate.opsForZSet().rank(KEY, value);
    }

    public Set<String> findCandidates(Long index) {
        return redisTemplate.opsForZSet().range(KEY, index, index + 200);
    }
}
