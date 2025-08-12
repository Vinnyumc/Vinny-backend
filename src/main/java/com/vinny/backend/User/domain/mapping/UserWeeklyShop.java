package com.vinny.backend.User.domain.mapping;

// src/main/java/com/example/shop/domain/UserWeeklyShop.java
import com.vinny.backend.Shop.domain.Shop;
import com.vinny.backend.User.domain.User;
import com.vinny.backend.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "user_weekly_shop",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","week_start","shop_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserWeeklyShop extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name="week_start", nullable=false)
    private LocalDate weekStart;

}
