package com.vinny.backend.Common.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 100, nullable = false)
    private String keyword;

    @Column(name = "searched_at")
    private LocalDateTime searchedAt;

    @PrePersist
    protected void onCreate() {
        if (this.searchedAt == null) {
            this.searchedAt = LocalDateTime.now();
        }
    }
}
