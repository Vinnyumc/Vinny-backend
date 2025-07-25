package com.vinny.backend.search.service;

import com.vinny.backend.User.domain.SearchLog;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.User.repository.UserRepository;
import com.vinny.backend.error.code.status.ErrorStatus;
import com.vinny.backend.error.exception.GeneralException;
import com.vinny.backend.search.dto.SearchLogCreateRequest;
import com.vinny.backend.search.dto.SearchLogResponse;
import com.vinny.backend.search.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;
    private final UserRepository userRepository;

    // 최대 저장 가능한 검색어 개수
    private static final int MAX_SEARCH_LOG_COUNT = 20;
    // 조회할 최근 검색어 개수
    private static final int RECENT_SEARCH_COUNT = 10;

    /**
     * 검색어 추가
     * - 중복 검색어는 시간만 업데이트
     * - 최대 개수 초과시 오래된 검색어 삭제
     */
    @Transactional
    public SearchLogResponse addSearchLog(Long userId, SearchLogCreateRequest request) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 키워드 정제 (앞뒤 공백 제거, 소문자 변환 등)
        String normalizedKeyword = normalizeKeyword(request.getKeyword());

        // 중복 검색어 확인
        Optional<SearchLog> existingSearchLog = searchLogRepository
                .findByUserAndKeyword(user, normalizedKeyword);

        SearchLog searchLog;
        if (existingSearchLog.isPresent()) {
            // 기존 검색어가 있으면 시간만 업데이트
            searchLog = updateExistingSearchLog(existingSearchLog.get());
            log.info("Updated existing search log for user: {}, keyword: {}", userId, normalizedKeyword);
        } else {
            // 새로운 검색어 추가
            searchLog = createNewSearchLog(user, normalizedKeyword);

            // 최대 개수 체크 및 정리
            cleanupOldSearchLogsIfNeeded(user);
            log.info("Created new search log for user: {}, keyword: {}", userId, normalizedKeyword);
        }

        return SearchLogResponse.from(searchLog);
    }

    /**
     * 키워드 정규화
     */
    private String normalizeKeyword(String keyword) {
        return keyword.trim().toLowerCase();
    }

    @Transactional
    public SearchLog updateExistingSearchLog(SearchLog existingSearchLog) {
        // 엔티티에 setter 없이 시간 업데이트하는 방법
        SearchLog updatedSearchLog = SearchLog.builder()
                .id(existingSearchLog.getId())
                .user(existingSearchLog.getUser())
                .keyword(existingSearchLog.getKeyword())
                .searchedAt(LocalDateTime.now()) // 현재 시간으로 업데이트
                .build();

        return searchLogRepository.save(updatedSearchLog);
    }

    /**
     * 새로운 검색어 생성
     */
    private SearchLog createNewSearchLog(User user, String keyword) {
        SearchLog searchLog = SearchLog.builder()
                .user(user)
                .keyword(keyword)
                .build();

        return searchLogRepository.save(searchLog);
    }

    /**
     * 최대 개수 초과시 오래된 검색어 삭제
     */
    private void cleanupOldSearchLogsIfNeeded(User user) {
        long currentCount = searchLogRepository.countByUser(user);

        if (currentCount > MAX_SEARCH_LOG_COUNT) {
            int deleteCount = (int) (currentCount - MAX_SEARCH_LOG_COUNT);
            List<SearchLog> oldestLogs = searchLogRepository
                    .findOldestByUser(user, PageRequest.of(0, deleteCount));

            searchLogRepository.deleteAll(oldestLogs);
            log.info("Cleaned up {} old search logs for user: {}", deleteCount, user.getId());
        }
    }
}