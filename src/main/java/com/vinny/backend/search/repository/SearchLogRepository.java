package com.vinny.backend.search.repository;

import com.vinny.backend.search.domain.SearchLog;
import com.vinny.backend.User.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

    /**
     * 사용자의 특정 키워드 검색 로그 조회 (중복 체크용)
     */
    Optional<SearchLog> findByUserAndKeyword(User user, String keyword);

    /**
     * 사용자의 최근 검색어 목록 조회 (최신순)
     */
    @Query("SELECT s FROM SearchLog s WHERE s.user = :user ORDER BY s.searchedAt DESC")
    List<SearchLog> findByUserOrderBySearchedAtDesc(@Param("user") User user, Pageable pageable);

    /**
     * 사용자의 검색 로그 개수 조회
     */
    long countByUser(User user);

    /**
     * 사용자의 오래된 검색 로그 삭제 (최대 개수 초과시)
     */
    @Modifying
    @Query("DELETE FROM SearchLog s WHERE s.user = :user AND s.searchedAt < :cutoffTime")
    void deleteOldSearchLogs(@Param("user") User user, @Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * 사용자의 가장 오래된 검색 로그들 조회
     */
    @Query("SELECT s FROM SearchLog s WHERE s.user = :user ORDER BY s.searchedAt ASC")
    List<SearchLog> findOldestByUser(@Param("user") User user, Pageable pageable);


    /**
     * 사용자의 최근 검색어 목록 조회 (최신순)
     */
    @Query("SELECT s FROM SearchLog s WHERE s.user = :user ORDER BY s.searchedAt DESC")
    List<SearchLog> findRecentSearchesByUser(@Param("user") User user, Pageable pageable);


    /**
     * 사용자의 모든 검색어 삭제
     */
    @Modifying
    @Query("DELETE FROM SearchLog s WHERE s.user = :user")
    void deleteAllByUser(@Param("user") User user);

    /**
     * 사용자의 특정 검색어 삭제 (권한 체크 포함)
     */
    @Modifying
    @Query("DELETE FROM SearchLog s WHERE s.id = :searchLogId AND s.user.id = :userId")
    int deleteByIdAndUserId(@Param("searchLogId") Long searchLogId, @Param("userId") Long userId);

    Optional<SearchLog> findSearchLogIdByUserAndKeyword(User user, String normalizedKeyword);
}

