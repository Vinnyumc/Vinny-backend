package com.vinny.backend.User.domain.mapping;

import com.vinny.backend.User.domain.Agreement;
import com.vinny.backend.User.domain.BaseEntity;
import com.vinny.backend.User.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserAgreement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false)
    private Agreement agreement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "agreed_at")
    private LocalDateTime agreedAt;

    @PrePersist
    protected void onCreate() {
        if (this.agreedAt == null) {
            this.agreedAt = LocalDateTime.now();
        }
    }
}
